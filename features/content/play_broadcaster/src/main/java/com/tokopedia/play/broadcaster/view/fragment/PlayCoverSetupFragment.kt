package com.tokopedia.play.broadcaster.view.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.util.cover.YalantisImageCropper
import com.tokopedia.play.broadcaster.util.cover.YalantisImageCropperImpl
import com.tokopedia.play.broadcaster.util.exhaustive
import com.tokopedia.play.broadcaster.util.helper.CoverImagePickerHelper
import com.tokopedia.play.broadcaster.util.showToaster
import com.tokopedia.play.broadcaster.view.activity.PlayCoverCameraActivity
import com.tokopedia.play.broadcaster.view.custom.PlayBottomSheetHeader
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.partial.CoverCropPartialView
import com.tokopedia.play.broadcaster.view.partial.CoverSetupPartialView
import com.tokopedia.play.broadcaster.view.state.Changeable
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.NotChangeable
import com.tokopedia.play.broadcaster.view.viewmodel.DataStoreViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayCoverSetupViewModel
import com.tokopedia.unifycomponents.Toaster
import com.yalantis.ucrop.model.ExifInfo
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by furqan on 02/06/20
 */
class PlayCoverSetupFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val dispatcher: CoroutineDispatcherProvider
) : PlayBaseSetupFragment() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(dispatcher.main + job)

    private lateinit var viewModel: PlayCoverSetupViewModel
    private lateinit var dataStoreViewModel: DataStoreViewModel

    private lateinit var yalantisImageCropper: YalantisImageCropper

    private lateinit var bottomSheetHeader: PlayBottomSheetHeader

    private lateinit var coverSetupView: CoverSetupPartialView
    private lateinit var coverCropView: CoverCropPartialView

    private lateinit var imagePickerHelper: CoverImagePickerHelper

    private var mListener: Listener? = null

    private val isEditCoverMode: Boolean
        get() = arguments?.getBoolean(EXTRA_IS_EDIT_COVER_MODE, false) ?: false

    override fun getScreenName(): String = "Play Cover Title Setup"

    override fun onInterceptBackPressed(): Boolean {
        val state = viewModel.cropState
        val coverChangeState = viewModel.coverChangeState()
        return if (state is CoverSetupState.Cropping) {
            onChangeCoverFromCropping(state.coverSource)
            true
        } else if (coverChangeState is NotChangeable) {
            requireView().showToaster(
                    message = coverChangeState.reason.localizedMessage,
                    type = Toaster.TYPE_NORMAL
            )
            true
        }else false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(PlayCoverSetupViewModel::class.java)
        dataStoreViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DataStoreViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_cover_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeCropState()
        observeCoverTitle()
        observeUploadCover()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION_COVER_CHOOSER -> onCoverChooserPermissionResult(grantResults)
            REQUEST_CODE_PERMISSION_CROP_COVER -> onCoverCropAddPermissionResult(grantResults)
            REQUEST_CODE_PERMISSION_START_CROP_COVER -> {}
            REQUEST_CODE_PERMISSION_UPLOAD -> onUploadPermissionResult(grantResults)
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!getImagePickerHelper().onActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancelChildren()
        viewModel.saveCover(coverSetupView.coverTitle)
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

    private fun initView(view: View) {
        with(view) {
            bottomSheetHeader = findViewById(R.id.bottom_sheet_header)
        }

        coverSetupView = CoverSetupPartialView(
                container = view as ViewGroup,
                dataSource = object : CoverSetupPartialView.DataSource {
                    override fun isValidCoverTitle(coverTitle: String): Boolean {
                        return viewModel.isValidCoverTitle(coverTitle)
                    }

                    override fun getCurrentCoverUri(): Uri? {
                        return viewModel.coverUri
                    }
                },
                listener = object : CoverSetupPartialView.Listener {
                    override fun onImageAreaClicked(view: CoverSetupPartialView) {
                        viewModel.saveCover(coverSetupView.coverTitle)
                        openCoverChooser(CoverSource.None)
                    }

                    override fun onNextButtonClicked(view: CoverSetupPartialView, coverTitle: String) {
                        shouldUploadCover(coverTitle)
                    }
                }
        )
        viewLifecycleOwner.lifecycle.addObserver(coverSetupView)

        coverCropView = CoverCropPartialView(view, object : CoverCropPartialView.Listener {
            override fun onAddButtonClicked(
                    view: CoverCropPartialView,
                    imageInputPath: String,
                    cropRect: RectF,
                    currentImageRect: RectF,
                    currentScale: Float,
                    currentAngle: Float,
                    exifInfo: ExifInfo,
                    viewBitmap: Bitmap
            ) {
                if (isGalleryPermissionGranted()) {
                    scope.launch {
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

                        viewModel.setCroppedCover(croppedUri)
                    }
                } else requestGalleryPermission(REQUEST_CODE_PERMISSION_CROP_COVER)

            }

            override fun onChangeButtonClicked(view: CoverCropPartialView) {
                onChangeCoverFromCropping(viewModel.source)
            }
        })
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

    private fun openCoverChooser(coverSource: CoverSource) {
        when (val coverChangeState = viewModel.coverChangeState()) {
            Changeable -> {
                getImagePickerHelper()
                        .show(coverSource)
            }
            is NotChangeable -> {
                requireView().showToaster(
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

    private fun shouldUploadCover(coverTitle: String) {
        val coverUri = viewModel.coverUri
        if (coverUri != null && viewModel.isValidCoverTitle(coverTitle)) {
            if (isGalleryPermissionGranted()) viewModel.uploadCover(bottomSheetCoordinator.channelId, coverTitle)
            else requestGalleryPermission(REQUEST_CODE_PERMISSION_UPLOAD)
        }
    }

    private fun getImagePickerHelper(): CoverImagePickerHelper {
        if (!::imagePickerHelper.isInitialized) {
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
                        startActivityForResult(intent, requestCode)
                    }
            )
        }
        return imagePickerHelper
    }

    /**
     * Permission
     */
    private fun isGalleryPermissionGranted(): Boolean {
        return arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).map {
            ContextCompat.checkSelfPermission(requireContext(), it)
        }.all { it == PackageManager.PERMISSION_GRANTED }
    }

    private fun requestGalleryPermission(requestCode: Int) = requestPermissions(
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), requestCode
    )

    private fun onCoverChooserPermissionResult(grantResults: IntArray) {
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) openCoverChooser(CoverSource.None)
        else {
            Toaster.make(requireView(), "Cover Chooser Permission Failed")
        }
    }

    private fun onCoverCropAddPermissionResult(grantResults: IntArray) {
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) coverCropView.clickAdd()
        else {
            Toaster.make(requireView(), "Cover Crop Permission Failed")
        }
    }

    private fun onUploadPermissionResult(grantResults: IntArray) {
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) coverSetupView.clickNext()
        else {
            Toaster.make(requireView(), "Upload Permission Failed")
        }
    }

    private fun openCameraPage() {
        val cameraIntent = Intent(context, PlayCoverCameraActivity::class.java)
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA_CAPTURE)
    }

    private fun openGalleryPage() {
        getImagePickerHelper()
                .show(CoverSource.Gallery)
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
            //TODO("Remove delay")
            delay(1200)
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
        }
    }

    private fun finishSetup() {
        mListener?.onCoverSetupFinished(dataStoreViewModel.getDataStore())
    }

    //region observe
    /**
     * Observe
     */
    private fun observeCropState() {
        viewModel.observableCropState.observe(viewLifecycleOwner, Observer {
            @Suppress("IMPLICIT_CAST_TO_ANY")
            when (it) {
                CoverSetupState.Blank -> showInitCoverLayout(null)
                is CoverSetupState.Cropping -> handleCroppingState(it)
                is CoverSetupState.Cropped -> {
                    if (isEditCoverMode) shouldUploadCover(coverTitle = viewModel.savedCoverTitle)
                    else showInitCoverLayout(it.coverImage)
                }
            }.exhaustive
        })
    }

    private fun observeCoverTitle() {
        viewModel.observableCoverTitle.observe(viewLifecycleOwner, Observer {
            coverSetupView.coverTitle = it
        })
    }

    private fun observeUploadCover() {
        viewModel.observableUploadCoverEvent.observe(viewLifecycleOwner, Observer {
            when (it) {
                NetworkResult.Loading -> {
                    coverSetupView.setLoading(true)
                    coverCropView.setLoading(true)
                }
                is NetworkResult.Fail -> {
                    coverSetupView.setLoading(false)
                    coverCropView.setLoading(false)
                    Toaster.make(requireView(), it.error.localizedMessage)
                }
                is NetworkResult.Success -> {
                    val data = it.data.getContentIfNotHandled()
                    if (data != null) {
                        coverSetupView.setLoading(false)
                        coverCropView.setLoading(false)
                        finishSetup()
                    }
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
        setupReturnTransition()
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

    companion object {
        const val EXTRA_IS_EDIT_COVER_MODE = "is_edit_cover_mode"

        private const val REQUEST_CODE_PERMISSION_COVER_CHOOSER = 9090
        private const val REQUEST_CODE_PERMISSION_CROP_COVER = 9191
        private const val REQUEST_CODE_PERMISSION_START_CROP_COVER = 9292
        private const val REQUEST_CODE_PERMISSION_UPLOAD = 9393

        private const val REQUEST_CODE_CAMERA_CAPTURE = 2222
    }

    interface Listener {

        /**
         * @return true means cancel has been handled by the listener
         */
        fun onCancelCropping(coverSource: CoverSource): Boolean = false
        fun onCoverSetupFinished(dataStore: PlayBroadcastSetupDataStore)
    }
}
