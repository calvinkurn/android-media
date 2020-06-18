package com.tokopedia.play.broadcaster.view.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.RectF
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.type.PlayCoverImageType
import com.tokopedia.play.broadcaster.ui.model.CoverSourceEnum
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayCoverImageChooserBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayGalleryImagePickerBottomSheet
import com.tokopedia.play.broadcaster.view.custom.PlayBottomSheetHeader
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.partial.CoverCropPartialView
import com.tokopedia.play.broadcaster.view.partial.CoverSetupPartialView
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastCoverSetupViewModel
import com.tokopedia.unifycomponents.Toaster
import com.yalantis.ucrop.callback.BitmapCropCallback
import com.yalantis.ucrop.model.ExifInfo
import javax.inject.Inject

/**
 * Created by furqan on 02/06/20
 */
class PlayCoverTitleSetupFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory
) : PlayBaseSetupFragment(), PlayCoverImageChooserBottomSheet.Listener,
        PlayGalleryImagePickerBottomSheet.Listener {

    private lateinit var viewModel: PlayBroadcastCoverSetupViewModel

    private var selectedCoverUri: Uri? = null
    private var coverSource: CoverSourceEnum = CoverSourceEnum.NONE

    private lateinit var bottomSheetHeader: PlayBottomSheetHeader

    private lateinit var coverSetupView: CoverSetupPartialView
    private lateinit var coverCropView: CoverCropPartialView

    private lateinit var coverImageChooserBottomSheet: PlayCoverImageChooserBottomSheet
    private lateinit var galleryImagePickerBottomSheet: PlayGalleryImagePickerBottomSheet

    override fun getScreenName(): String = "Play Cover Title Setup"

    override fun onInterceptBackPressed(): Boolean = false

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

        observeSelectedCover()
        observeImageEcsLink()
        observeOriginalImageUri()
    }

    override fun onCoverChosen(coverImage: PlayCoverImageType) {
        when (coverImage) {
            is PlayCoverImageType.Camera -> onGetCoverFromCamera(coverImage.uri)
            is PlayCoverImageType.Product -> onGetCoverFromProduct(coverImage.productId, coverImage.imageUrl)
        }
    }

    private fun onGetCoverFromCamera(imageUri: Uri?) {
        imageUri?.let {
            coverSource = CoverSourceEnum.CAMERA
            showCoverCropLayout()
            renderCoverCropLayout(it)
        }
    }

    private fun onGetCoverFromProduct(productId: Long, imageUrl: String) {
        coverSource = CoverSourceEnum.PRODUCT
        viewModel.getOriginalImageUrl(requireContext(),
                productId,
                imageUrl
        )
        showCoverCropLayout()
    }

    override fun onChooseFromGalleryClicked() {
        getGalleryImagePickerBottomSheet()
                .show(childFragmentManager)
    }

    override fun onGetCoverFromGallery(imageUri: Uri?) {
        imageUri?.let {
            coverSource = CoverSourceEnum.GALLERY
            showCoverCropLayout()
            renderCoverCropLayout(it)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION_COVER_CHOOSER -> onCoverChooserPermissionResult(grantResults)
            REQUEST_CODE_PERMISSION_CROP_COVER -> onCoverCropAddPermissionResult(grantResults)
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
                        return selectedCoverUri
                    }
                },
                listener = object : CoverSetupPartialView.Listener {
                    override fun onImageAreaClicked(view: CoverSetupPartialView) {
                        openCoverChooser()
                    }

                    override fun onNextButtonClicked(view: CoverSetupPartialView, coverTitle: String) {
                        val coverImagePath = selectedCoverUri
                        if (coverImagePath != null && viewModel.isValidCoverTitle(coverTitle)) {
                            if (isGalleryPermissionGranted()) viewModel.setCover(coverImagePath.toString(), coverTitle)
                            else requestGalleryPermission(REQUEST_CODE_PERMISSION_CROP_COVER)
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
                viewModel.cropImage(requireContext(),
                        imageInputPath,
                        cropRect,
                        currentImageRect,
                        currentScale,
                        currentAngle,
                        exifInfo,
                        viewBitmap,
                        object : BitmapCropCallback {
                            override fun onBitmapCropped(resultUri: Uri, offsetX: Int, offsetY: Int, imageWidth: Int, imageHeight: Int) {
                                val finalResultUri = viewModel.validateImageMinSize(resultUri)
                                onImageCropped(finalResultUri)
                            }

                            override fun onCropFailure(t: Throwable) {
                                t.printStackTrace()
                            }
                        }
                )
            }

            override fun onChangeButtonClicked(view: CoverCropPartialView) {
                onCancelCropImage()
            }
        })
    }

    private fun setupView() {
        bottomSheetHeader.setHeader(getString(R.string.play_prepare_cover_title_title), isRoot = false)
    }

    private fun openCoverChooser() {
        if (isGalleryPermissionGranted()) {
            getPlayCoverImageChooserBottomSheet()
                    .show(childFragmentManager)
        } else {
            requestGalleryPermission(REQUEST_CODE_PERMISSION_COVER_CHOOSER)
        }
    }

    private fun showCoverCropLayout() {
        coverSetupView.hide()
        coverCropView.show()
    }

    private fun hideCoverCropLayout() {
        coverSetupView.show()
        coverCropView.hide()
    }

    private fun renderCoverTitleLayout(resultImageUri: Uri?) {
        resultImageUri?.let {
            selectedCoverUri = it
            coverSetupView.setImage(it)
        }

        coverSetupView.updateViewState()
    }

    private fun renderCoverCropLayout(imageUri: Uri) {
        coverCropView.setImageForCrop(imageUri)
    }

    private fun onCancelCropImage() {
        hideCoverCropLayout()
        renderCoverTitleLayout(null)
        when (coverSource) {
            CoverSourceEnum.GALLERY -> onChooseFromGalleryClicked()
            else -> openCoverChooser()
        }
    }

    private fun onImageCropped(resultImageUri: Uri) {
        selectedCoverUri = resultImageUri
        hideCoverCropLayout()
        renderCoverTitleLayout(resultImageUri)
    }

    private fun getPlayCoverImageChooserBottomSheet(): PlayCoverImageChooserBottomSheet {
        if (!::coverImageChooserBottomSheet.isInitialized) {
            val fragmentFactory = childFragmentManager.fragmentFactory
            val coverChooser = fragmentFactory.instantiate(
                    requireContext().classLoader,
                    PlayCoverImageChooserBottomSheet::class.java.name
            ) as PlayCoverImageChooserBottomSheet

            coverChooser.mListener = this
            coverChooser.setShowListener { coverChooser.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }

            coverImageChooserBottomSheet = coverChooser
        }

        return coverImageChooserBottomSheet
    }

    private fun getGalleryImagePickerBottomSheet(): PlayGalleryImagePickerBottomSheet {
        if (!::galleryImagePickerBottomSheet.isInitialized) {
            val fragmentFactory = childFragmentManager.fragmentFactory
            val imagePicker = fragmentFactory.instantiate(
                    requireContext().classLoader,
                    PlayGalleryImagePickerBottomSheet::class.java.name
            ) as PlayGalleryImagePickerBottomSheet

            imagePicker.listener = this
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

    //region observe
    /**
     * Observe
     */
    private fun observeSelectedCover() {
        viewModel.observableSelectedCover.observe(viewLifecycleOwner, Observer {

        })
    }

    private fun observeOriginalImageUri() {
        viewModel.originalImageUri.observe(viewLifecycleOwner, Observer {
            if (coverSource == CoverSourceEnum.PRODUCT) {
                renderCoverCropLayout(it)
            }
        })
    }

    private fun observeImageEcsLink() {
        viewModel.imageEcsLink.observe(viewLifecycleOwner, Observer {
            selectedCoverUri?.let { selectedCover ->
                if (it.isNotEmpty()) {

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
        private const val REQUEST_CODE_PERMISSION_COVER_CHOOSER = 9191
        private const val REQUEST_CODE_PERMISSION_CROP_COVER = 9090
    }
}
