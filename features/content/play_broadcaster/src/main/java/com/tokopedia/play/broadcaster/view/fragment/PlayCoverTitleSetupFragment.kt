package com.tokopedia.play.broadcaster.view.fragment

import android.Manifest
import android.app.Activity
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.CoverSourceEnum
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.util.cover.YalantisImageCropper
import com.tokopedia.play.broadcaster.util.cover.YalantisImageCropperImpl
import com.tokopedia.play.broadcaster.view.activity.PlayCoverCameraActivity
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayCoverImageChooserBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayGalleryImagePickerBottomSheet
import com.tokopedia.play.broadcaster.view.custom.PlayBottomSheetHeader
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.partial.CoverCropPartialView
import com.tokopedia.play.broadcaster.view.partial.CoverSetupPartialView
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastCoverSetupViewModel
import com.tokopedia.unifycomponents.Toaster
import com.yalantis.ucrop.model.ExifInfo
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by furqan on 02/06/20
 */
class PlayCoverTitleSetupFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val dispatcher: CoroutineDispatcherProvider
) : PlayBaseSetupFragment(),
        PlayGalleryImagePickerBottomSheet.Listener {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(dispatcher.main + job)

    private lateinit var viewModel: PlayBroadcastCoverSetupViewModel

    private lateinit var yalantisImageCropper: YalantisImageCropper

    private lateinit var bottomSheetHeader: PlayBottomSheetHeader

    private lateinit var coverSetupView: CoverSetupPartialView
    private lateinit var coverCropView: CoverCropPartialView

    private lateinit var coverImageChooserBottomSheet: PlayCoverImageChooserBottomSheet
    private lateinit var galleryImagePickerBottomSheet: PlayGalleryImagePickerBottomSheet

    override fun getScreenName(): String = "Play Cover Title Setup"

    override fun onInterceptBackPressed(): Boolean {
        val state = viewModel.cropState
        return if (state is CoverSetupState.Cropping) {
            onCancelCropImage(state.coverSource)
            true
        } else false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(PlayBroadcastCoverSetupViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_cover_title_setup, container, false)
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
    }

    override fun onGetCoverFromGallery(imageUri: Uri?) {
        imageUri?.let {
            viewModel.setCroppingCover(it, CoverSourceEnum.GALLERY)
        }
        getPlayCoverImageChooserBottomSheet().dismiss()
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
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_CAMERA_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.getParcelableExtra<Uri>(PlayCoverCameraActivity.EXTRA_IMAGE_URI)
            imageUri?.let(::onGetCoverFromCamera)
            getPlayCoverImageChooserBottomSheet().dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancelChildren()
        viewModel.saveCover(viewModel.coverUri, coverSetupView.coverTitle)
    }

    private fun onGetCoverFromCamera(imageUri: Uri?) {
        imageUri?.let {
            viewModel.setCroppingCover(it, CoverSourceEnum.CAMERA)
        }
    }

    private fun onGetCoverFromProduct(productId: Long, imageUrl: String) {
        scope.launch {
            val originalImageUrl = viewModel.getOriginalImageUrl(productId, imageUrl)
            Glide.with(requireContext())
                    .asBitmap()
                    .load(originalImageUrl)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            viewModel.setCroppingCover(resource, CoverSourceEnum.PRODUCT)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
        }
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
                        openCoverChooser()
                    }

                    override fun onNextButtonClicked(view: CoverSetupPartialView, coverTitle: String) {
                        val coverUri = viewModel.coverUri
                        if (coverUri != null && viewModel.isValidCoverTitle(coverTitle)) {
                            if (isGalleryPermissionGranted()) viewModel.uploadCover(coverUri, coverTitle)
                            else requestGalleryPermission(REQUEST_CODE_PERMISSION_UPLOAD)
                        }
                    }
                }
        )

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
                onCancelCropImage(CoverSourceEnum.NONE)
            }
        })
    }

    private fun setupView() {
        yalantisImageCropper = YalantisImageCropperImpl(requireContext())
        bottomSheetHeader.setHeader(getString(R.string.play_prepare_cover_title_title), isRoot = false)
    }

    private fun openCoverChooser() {
        getPlayCoverImageChooserBottomSheet()
                .show(childFragmentManager)
    }

    private fun showCoverCropLayout(coverImageUri: Uri) {
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

    private fun onCancelCropImage(source: CoverSourceEnum) {
        onBackFromCropping(source)
        showInitCoverLayout(null)
        removeCover()
    }

    private fun getPlayCoverImageChooserBottomSheet(): PlayCoverImageChooserBottomSheet {
        if (!::coverImageChooserBottomSheet.isInitialized) {
            val fragmentFactory = childFragmentManager.fragmentFactory
            val coverChooser = fragmentFactory.instantiate(
                    requireContext().classLoader,
                    PlayCoverImageChooserBottomSheet::class.java.name
            ) as PlayCoverImageChooserBottomSheet

            coverChooser.mListener = getCoverImageChooserListener()
            coverChooser.setShowListener { coverChooser.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }

            coverImageChooserBottomSheet = coverChooser
        }

        return coverImageChooserBottomSheet
    }

    private fun getCoverImageChooserListener(): PlayCoverImageChooserBottomSheet.Listener {
        return object: PlayCoverImageChooserBottomSheet.Listener {

            override fun onChooseProductCover(bottomSheet: PlayCoverImageChooserBottomSheet, productId: Long, imageUrl: String) {
                onGetCoverFromProduct(productId, imageUrl)
                bottomSheet.dismiss()
            }

            override fun onGetFromCamera(bottomSheet: PlayCoverImageChooserBottomSheet) {
                openCameraPage()
            }

            override fun onChooseFromGalleryClicked(bottomSheet: PlayCoverImageChooserBottomSheet) {
                openGalleryPage()
            }
        }
    }

    private fun getGalleryImagePickerBottomSheet(): PlayGalleryImagePickerBottomSheet {
        if (!::galleryImagePickerBottomSheet.isInitialized) {
            val fragmentFactory = childFragmentManager.fragmentFactory
            val imagePicker = fragmentFactory.instantiate(
                    requireContext().classLoader,
                    PlayGalleryImagePickerBottomSheet::class.java.name
            ) as PlayGalleryImagePickerBottomSheet

            imagePicker.mListener = this
            imagePicker.setShowListener { imagePicker.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }

            galleryImagePickerBottomSheet = imagePicker
        }

        return galleryImagePickerBottomSheet
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
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) openCoverChooser()
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

    private fun onBackFromCropping(source: CoverSourceEnum): Boolean {
        return when (source) {
            CoverSourceEnum.GALLERY -> {
                openGalleryPage()
                true
            }
            CoverSourceEnum.CAMERA -> {
                openCameraPage()
                true
            }
            else -> {
                openCoverChooser()
                true
            }
        }
    }

    private fun openCameraPage() {
        val cameraIntent = Intent(context, PlayCoverCameraActivity::class.java)
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA_CAPTURE)
    }

    private fun openGalleryPage() {
        getGalleryImagePickerBottomSheet()
                .show(childFragmentManager)
    }

    //region observe
    /**
     * Observe
     */
    private fun observeCropState() {
        viewModel.observableCropState.observe(viewLifecycleOwner, Observer {
            when (it) {
                CoverSetupState.Blank -> showInitCoverLayout(null)
                is CoverSetupState.Cropping -> showCoverCropLayout(it.coverImage)
                is CoverSetupState.Cropped -> showInitCoverLayout(it.coverImage)
            }
        })
    }

    private fun observeCoverTitle() {
        viewModel.observableCoverTitle.observe(viewLifecycleOwner, Observer {
            coverSetupView.coverTitle = it
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
        private const val REQUEST_CODE_PERMISSION_COVER_CHOOSER = 9090
        private const val REQUEST_CODE_PERMISSION_CROP_COVER = 9191
        private const val REQUEST_CODE_PERMISSION_START_CROP_COVER = 9292
        private const val REQUEST_CODE_PERMISSION_UPLOAD = 9393

        private const val REQUEST_CODE_CAMERA_CAPTURE = 2222
    }
}
