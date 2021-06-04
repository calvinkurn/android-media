package com.tokopedia.play.broadcaster.view.fragment.setup.cover

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.util.cover.YalantisImageCropper
import com.tokopedia.play.broadcaster.util.cover.YalantisImageCropperImpl
import com.tokopedia.play.broadcaster.util.extension.getDialog
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.play.broadcaster.util.helper.CoverImagePickerHelper
import com.tokopedia.play.broadcaster.util.permission.PermissionHelper
import com.tokopedia.play.broadcaster.util.permission.PermissionHelperImpl
import com.tokopedia.play.broadcaster.util.permission.PermissionResultListener
import com.tokopedia.play.broadcaster.util.permission.PermissionStatusHandler
import com.tokopedia.play.broadcaster.util.preference.PermissionSharedPreferences
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity
import com.tokopedia.play.broadcaster.view.custom.PlayBottomSheetHeader
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.partial.CoverCropViewComponent
import com.tokopedia.play.broadcaster.view.partial.CoverSetupViewComponent
import com.tokopedia.play.broadcaster.view.state.Changeable
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.NotChangeable
import com.tokopedia.play.broadcaster.view.viewmodel.DataStoreViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayCoverSetupViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.detachableview.FragmentViewContainer
import com.tokopedia.play_common.detachableview.FragmentWithDetachableView
import com.tokopedia.play_common.detachableview.detachableView
import com.tokopedia.play_common.util.extension.exhaustive
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.Toaster
import com.yalantis.ucrop.model.ExifInfo
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by furqan on 02/06/20
 */
class PlayCoverSetupFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val dispatcher: CoroutineDispatchers,
        private val permissionPref: PermissionSharedPreferences,
        private val analytic: PlayBroadcastAnalytic
) : PlayBaseSetupFragment(), CoverCropViewComponent.Listener, CoverSetupViewComponent.Listener, FragmentWithDetachableView {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(dispatcher.main + job)

    private lateinit var viewModel: PlayCoverSetupViewModel
    private lateinit var dataStoreViewModel: DataStoreViewModel

    private lateinit var yalantisImageCropper: YalantisImageCropper

    private val bottomSheetHeader: PlayBottomSheetHeader by detachableView(R.id.bottom_sheet_header)

    private val fragmentViewContainer = FragmentViewContainer()

    private val coverSetupView by viewComponent(isEagerInit = true) {
        CoverSetupViewComponent(
                container = view as ViewGroup,
                dataSource = object : CoverSetupViewComponent.DataSource {
                    override fun getMaxTitleCharacters(): Int {
                        return viewModel.maxTitleChars
                    }

                    override fun isValidCoverTitle(coverTitle: String): Boolean {
                        return viewModel.isValidCoverTitle(coverTitle)
                    }

                    override fun getCurrentCoverUri(): Uri? {
                        return viewModel.coverUri
                    }
                },
                listener = this
        )
    }

    private val coverCropView by viewComponent { CoverCropViewComponent(it, this) }

    private var imagePickerHelper: CoverImagePickerHelper? = null

    private lateinit var dialogRationale: DialogUnify
    private val permissionStatusHandler: PermissionStatusHandler = {
        if (!isAllGranted()) showToaster(getString(R.string.play_storage_permission_denied_error), Toaster.TYPE_ERROR)
        else {
            when (requestCode) {
                REQUEST_CODE_PERMISSION_CROP_COVER -> {
                    if (isAllGranted()) coverCropView.clickAdd()
                    else showToaster(getString(R.string.play_storage_permission_denied_error), Toaster.TYPE_ERROR)
                }
                REQUEST_CODE_PERMISSION_UPLOAD -> {
                    if (isAllGranted()) coverSetupView.clickNext()
                    else showToaster(getString(R.string.play_storage_permission_denied_error), Toaster.TYPE_ERROR)
                }
                REQUEST_CODE_PERMISSION_COVER_CHOOSER -> {
                    if (isAllGranted()) openCoverChooser(CoverSource.None)
                }
            }
        }
    }
    private val permissionResultListener = object : PermissionResultListener {
        override fun onRequestPermissionResult(): PermissionStatusHandler {
            return permissionStatusHandler
        }

        override fun onShouldShowRequestPermissionRationale(permissions: Array<String>, requestCode: Int): Boolean {
            showDialogPermissionRationale()
            return true
        }
    }

    private var mListener: Listener? = null

    private var toasterBottomMargin = 0

    private val isEditCoverMode: Boolean
        get() = arguments?.getBoolean(EXTRA_IS_EDIT_COVER_MODE, false) ?: false

    private lateinit var permissionHelper: PermissionHelper

    override fun getScreenName(): String = "Play Cover Title Setup"

    override fun onInterceptBackPressed(): Boolean {
        try { Toaster.snackBar.dismiss() } catch (e: Throwable) {}

        val state = viewModel.cropState
        val coverChangeState = viewModel.coverChangeState()
        return when {
            state is CoverSetupState.Cropping -> {
                onChangeCoverFromCropping(state.coverSource)
                true
            }
            coverChangeState is NotChangeable -> {
                showToaster(
                        message = coverChangeState.reason.localizedMessage,
                        type = Toaster.TYPE_NORMAL
                )
                true
            }
            else -> false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(PlayCoverSetupViewModel::class.java)
        dataStoreViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DataStoreViewModel::class.java)
        permissionHelper = PermissionHelperImpl(this, permissionPref)
    }

    override fun onStart() {
        super.onStart()
        analytic.viewAddCoverTitleBottomSheet()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_cover_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeCropState()
        observeUploadCover()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)) return
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!getImagePickerHelper().onActivityResult(requestCode, resultCode, data)) {
            (activity as? PlayBroadcastActivity)?.startPreview()
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroyView() {
        imagePickerHelper = null
        super.onDestroyView()
        job.cancelChildren()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        getImagePickerHelper().onAttachFragment(childFragment)
    }

    override fun getViewContainer(): FragmentViewContainer {
        return fragmentViewContainer
    }

    /**
     * CoverCrop View Component Listener
     */
    override fun onAddButtonClicked(view: CoverCropViewComponent, imageInputPath: String, cropRect: RectF, currentImageRect: RectF, currentScale: Float, currentAngle: Float, exifInfo: ExifInfo, viewBitmap: Bitmap) {
        if (isGalleryPermissionGranted()) {
            scope.launch {
                try {
                    val croppedUri = withContext(dispatcher.io) {
                        yalantisImageCropper.cropImage(
                                inputPath = imageInputPath,
                                cropRect = cropRect,
                                currentRect = currentImageRect,
                                currentScale = currentScale,
                                currentAngle = currentAngle,
                                exifInfo = exifInfo,
                                viewBitmap = viewBitmap
                        )
                    }

                    viewModel.setDraftCroppedCover(croppedUri)
                    if (isEditCoverMode) shouldUploadCover()
                } catch (e: Throwable) {  /* Fail to crop */ }
            }
        } else requestGalleryPermission(REQUEST_CODE_PERMISSION_CROP_COVER)

        analytic.clickContinueOnCroppingPage()
    }

    override fun onChangeButtonClicked(view: CoverCropViewComponent) {
        onChangeCoverFromCropping(viewModel.source)
        analytic.clickChangeCoverOnCroppingPage()
    }

    /**
     * CoverSetup View Component Listener
     */
    override fun onImageAreaClicked(view: CoverSetupViewComponent) {
//        viewModel.saveCover(coverSetupView.coverTitle)
        requestGalleryPermission(REQUEST_CODE_PERMISSION_COVER_CHOOSER, isFullFlow = true)
        analytic.clickAddCover()
    }

    override fun onNextButtonClicked(view: CoverSetupViewComponent) {
        shouldUploadCover()
        analytic.clickContinueOnAddCoverAndTitlePage()
    }

    override fun onTitleAreaHasFocus() {
        analytic.clickAddTitle()
    }

    override fun onViewDestroyed(view: CoverSetupViewComponent) {
//        viewModel.saveCover(view.coverTitle)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    private fun onGetCoverFromCamera(imageUri: Uri?) {
        showCoverCropLayout(null)
        imageUri?.let {
            viewModel.setCroppingCoverByUri(it, CoverSource.Camera)
        }
    }

    private fun onGetCoverFromProduct(productId: Long, imageUrl: String) {
        viewModel.setCroppingProductCover(productId, imageUrl)
    }

    private fun onGetCoverFromGallery(imageUri: Uri?) {
        showCoverCropLayout(null)
        imageUri?.let {
            viewModel.setCroppingCoverByUri(it, CoverSource.Gallery)
        }
        getImagePickerHelper().dismiss()
    }

    private fun setupView() {
        yalantisImageCropper = YalantisImageCropperImpl(requireContext())
        bottomSheetHeader.setHeader(getString(R.string.play_prepare_cover_title_title), isRoot = false)

        bottomSheetHeader.setListener(object : PlayBottomSheetHeader.Listener {
            override fun onBackButtonClicked(view: PlayBottomSheetHeader) {
                bottomSheetCoordinator.goBack()
            }
        })
    }

    private fun showToaster(message: String, type: Int = Toaster.TYPE_NORMAL, duration: Int = Toaster.LENGTH_SHORT, actionLabel: String = "", actionListener: View.OnClickListener = View.OnClickListener { }) {
        if (toasterBottomMargin == 0) {
            val coverSetupBottomActionHeight = coverSetupView.getBottomActionView().height
            val bottomActionHeight = if (coverSetupBottomActionHeight != 0) coverSetupBottomActionHeight else coverCropView.getBottomActionView().height
            val offset8 = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            toasterBottomMargin = bottomActionHeight + offset8
        }

        view?.showToaster(
                message = message,
                type = type,
                duration = duration,
                actionLabel = actionLabel,
                actionListener = actionListener,
                bottomMargin = toasterBottomMargin
        )
    }

    private fun openCoverChooser(coverSource: CoverSource) {
        when (val coverChangeState = viewModel.coverChangeState()) {
            Changeable -> {
                getImagePickerHelper()
                        .show(coverSource)
            }
            is NotChangeable -> {
                showToaster(
                        message = coverChangeState.reason.localizedMessage,
                        type = Toaster.TYPE_NORMAL
                )
            }
        }
    }

    private fun showCoverCropLayout(coverImageUri: Uri?) {
        requestGalleryPermission(REQUEST_CODE_PERMISSION_START_CROP_COVER)

        coverSetupView.hide()
        coverCropView.show()

        coverCropView.setImageForCrop(coverImageUri)

        // called twice, the first one with null coverImageUri
        if (coverImageUri != null) analytic.viewCroppingPage()
    }

    private fun showInitCoverLayout(coverImageUri: Uri?) {
        coverSetupView.show()
        coverCropView.hide()

        coverSetupView.setImage(coverImageUri)
    }

    private fun removeCover() {
        viewModel.removeCover()
    }

    private fun onChangeCoverFromCropping(source: CoverSource) {
        if (mListener?.onCancelCropping(source) == false) {
            openCoverChooser(source)
            showInitCoverLayout(null)
            removeCover()
        }
    }

    private fun shouldUploadCover() {
        if (isGalleryPermissionGranted()) viewModel.uploadCover()
        else requestGalleryPermission(REQUEST_CODE_PERMISSION_UPLOAD)
    }

    private fun showDialogPermissionRationale() {
        getDialogRationale()
                .show()
    }

    private fun getImagePickerHelper(): CoverImagePickerHelper {
        if (imagePickerHelper == null) {
            imagePickerHelper = CoverImagePickerHelper(
                    context = requireContext(),
                    fragmentManager = childFragmentManager,
                    listener = object : CoverImagePickerHelper.OnChosenListener {
                        override fun onGetFromProduct(productId: Long, imageUrl: String) {
                            onGetCoverFromProduct(productId, imageUrl)
                        }

                        override fun onGetFromCamera(uri: Uri) {
                            onGetCoverFromCamera(uri)
                        }

                        override fun onGetFromGallery(uri: Uri) {
                            onGetCoverFromGallery(uri)
                        }
                    },
                    intentHandler = { intent, requestCode ->
                        (activity as? PlayBroadcastActivity)?.stopPreview()
                        startActivityForResult(intent, requestCode)
                    }
            )
        }
        return imagePickerHelper!!
    }

    private fun getDialogRationale(): DialogUnify {
        if (!::dialogRationale.isInitialized) {
            dialogRationale = requireContext().getDialog(
                    actionType = DialogUnify.HORIZONTAL_ACTION,
                    title = getString(R.string.play_storage_permission_rationale_title),
                    desc = getString(R.string.play_storage_permission_rationale_desc),
                    primaryCta = getString(R.string.play_yes),
                    primaryListener = { dialog ->
                        dialog.dismiss()
                        requestGalleryPermission(REQUEST_CODE_PERMISSION_COVER_CHOOSER, isFullFlow = false)
                    },
                    secondaryCta = getString(R.string.play_no),
                    secondaryListener = { dialog -> dialog.dismiss() }
            )
        }
        return dialogRationale
    }

    /**
     * Permission
     */
    private fun isGalleryPermissionGranted(): Boolean {
        return permissionHelper.isAllPermissionsGranted(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        )
    }

    private fun requestGalleryPermission(requestCode: Int, isFullFlow: Boolean = true) {
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (isFullFlow) {
            permissionHelper.requestMultiPermissionsFullFlow(
                    permissions = permissions,
                    requestCode = requestCode,
                    permissionResultListener = permissionResultListener
            )
        } else {
            permissionHelper.requestMultiPermissions(
                    permissions = permissions,
                    requestCode = requestCode,
                    statusHandler = permissionStatusHandler
            )
        }
    }

    private fun handleCroppingState(state: CoverSetupState.Cropping) {
        when (state) {
            is CoverSetupState.Cropping.Product -> getOriginalProductImage(state)
            is CoverSetupState.Cropping.Image -> showCoverCropLayout(state.coverImage)
        }.exhaustive
    }

    private fun getOriginalProductImage(productCropping: CoverSetupState.Cropping.Product) {
        showCoverCropLayout(null)

        scope.launch {
            try {
                val originalImageUrl = viewModel.getOriginalImageUrl(productCropping.productId, productCropping.imageUrl)
                Glide.with(requireContext())
                        .asBitmap()
                        .load(originalImageUrl)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                viewModel.setCroppingCoverByBitmap(resource, CoverSource.Product(productCropping.productId))
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {}
                        })
            } catch (e: Throwable) {
                showToaster(
                        message = e.localizedMessage,
                        type = Toaster.TYPE_ERROR,
                        duration = Toaster.LENGTH_LONG
                )
            }
        }
    }

    private fun onUploadSuccess() {
        scope.launch {
            val error = mListener?.onCoverSetupFinished(dataStoreViewModel.getDataStore())
            error?.let {
                yield()
                onUploadFailed(it)
            }
        }
    }

    private fun onUploadFailed(e: Throwable) {
        coverSetupView.setLoading(false)
        coverCropView.setLoading(false)

        showToaster(
                message = e.localizedMessage,
                type = Toaster.TYPE_ERROR
        )
    }

    //region observe
    /**
     * Observe
     */
    private fun observeCropState() {
        viewModel.observableCropState.observe(viewLifecycleOwner, Observer {
            when (it) {
                CoverSetupState.Blank -> showInitCoverLayout(null)
                is CoverSetupState.Cropping -> handleCroppingState(it)
                is CoverSetupState.Cropped.Draft -> {
                    if (!isEditCoverMode) showInitCoverLayout(it.coverImage)
                }
                is CoverSetupState.Cropped.Uploaded -> {
                    if (!isEditCoverMode) showInitCoverLayout(it.localImage)
                }
            }
        })
    }

    private fun observeCoverTitle() {
//        viewModel.observableCoverTitle.observe(viewLifecycleOwner, Observer {
//            coverSetupView.coverTitle = it
//        })
    }

    private fun observeUploadCover() {
        viewModel.observableUploadCoverEvent.observe(viewLifecycleOwner, Observer {
            when (it) {
                NetworkResult.Loading -> {
                    coverSetupView.setLoading(true)
                    coverCropView.setLoading(true)
                }
                is NetworkResult.Fail -> onUploadFailed(it.error)
                is NetworkResult.Success -> {
                    val data = it.data.getContentIfNotHandled()
                    if (data != null) onUploadSuccess()
                }
            }
        })
    }
    //endregion

    /**
     * Transition
     */
    private fun setupTransition() {
        setupEnterTransition()
        setupExitTransition()
        setupReturnTransition()
        setupReenterTransition()
    }

    private fun setupEnterTransition() {
        enterTransition = TransitionSet()
                .addTransition(Slide(Gravity.END))
                .addTransition(Fade(Fade.IN))
                .setStartDelay(200)
                .setDuration(300)

        sharedElementEnterTransition = TransitionSet()
                .addTransition(ChangeTransform())
                .addTransition(ChangeBounds())
                .setDuration(450)
    }

    private fun setupReturnTransition() {
        returnTransition = TransitionSet()
                .addTransition(Slide(Gravity.END))
                .addTransition(Fade(Fade.OUT))
                .setDuration(250)

        sharedElementReturnTransition = TransitionSet()
                .addTransition(ChangeTransform())
                .addTransition(ChangeBounds())
                .setDuration(450)
    }

    private fun setupExitTransition() {
        exitTransition = TransitionSet()
                .addTransition(Slide(Gravity.START))
                .addTransition(Fade(Fade.OUT))
                .setDuration(300)
    }

    private fun setupReenterTransition() {
        reenterTransition = TransitionSet()
                .addTransition(Slide(Gravity.START))
                .addTransition(Fade(Fade.IN))
                .setDuration(300)
    }

    companion object {
        const val EXTRA_IS_EDIT_COVER_MODE = "is_edit_cover_mode"

        private const val REQUEST_CODE_PERMISSION_CROP_COVER = 9191
        private const val REQUEST_CODE_PERMISSION_START_CROP_COVER = 9292
        private const val REQUEST_CODE_PERMISSION_UPLOAD = 9393
        private const val REQUEST_CODE_PERMISSION_COVER_CHOOSER = 9494
    }

    interface Listener {

        /**
         * @return true means cancel has been handled by the listener
         */
        fun onCancelCropping(coverSource: CoverSource): Boolean = false
        suspend fun onCoverSetupFinished(dataStore: PlayBroadcastSetupDataStore): Throwable?
    }
}
