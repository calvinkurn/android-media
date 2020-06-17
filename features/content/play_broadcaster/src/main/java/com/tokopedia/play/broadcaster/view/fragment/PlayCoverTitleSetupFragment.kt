package com.tokopedia.play.broadcaster.view.fragment

import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.CoverSourceEnum
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastChooseCoverBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastCoverFromGalleryBottomSheet
import com.tokopedia.play.broadcaster.view.custom.PlayBottomSheetHeader
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.partial.CoverCropPartialView
import com.tokopedia.play.broadcaster.view.partial.CoverSetupPartialView
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastCoverSetupViewModel
import com.yalantis.ucrop.callback.BitmapCropCallback
import com.yalantis.ucrop.model.ExifInfo
import kotlinx.android.synthetic.*
import java.io.File
import javax.inject.Inject

/**
 * Created by furqan on 02/06/20
 */
class PlayCoverTitleSetupFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory
) : PlayBaseSetupFragment(), PlayBroadcastChooseCoverBottomSheet.Listener,
        PlayBroadcastCoverFromGalleryBottomSheet.Listener {

    private lateinit var viewModel: PlayBroadcastCoverSetupViewModel

    private var selectedCoverUri: Uri? = null
    private var coverSource: CoverSourceEnum = CoverSourceEnum.NONE

    private lateinit var bottomSheetHeader: PlayBottomSheetHeader

    private lateinit var coverSetupView: CoverSetupPartialView
    private lateinit var coverCropView: CoverCropPartialView

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

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }

    override fun onGetCoverFromCamera(imageUri: Uri?) {
        imageUri?.let {
            coverSource = CoverSourceEnum.CAMERA
            showCoverCropLayout()
            renderCoverCropLayout(it)
        }
    }

    override fun onGetCoverFromProduct(productId: Long, imageUrl: String) {
        coverSource = CoverSourceEnum.PRODUCT
        viewModel.getOriginalImageUrl(requireContext(),
                productId,
                imageUrl
        )
        showCoverCropLayout()
    }

    override fun onChooseFromGalleryClicked() {
        val coverFromGalleryBottomSheet = PlayBroadcastCoverFromGalleryBottomSheet()
        coverFromGalleryBottomSheet.listener = this
        coverFromGalleryBottomSheet.setShowListener { coverFromGalleryBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        coverFromGalleryBottomSheet.show(requireFragmentManager(), PlayBroadcastChooseCoverBottomSheet.TAG_CHOOSE_COVER)
    }

    override fun onGetCoverFromGallery(imageUri: Uri?) {
        imageUri?.let {
            coverSource = CoverSourceEnum.GALLERY
            showCoverCropLayout()
            renderCoverCropLayout(it)
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
                        onChangeCoverClicked()
                    }

                    override fun onNextButtonClicked(view: CoverSetupPartialView) {
                        //next
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

    private fun onChangeCoverClicked() {
        val changeCoverBottomSheet =
                childFragmentManager.fragmentFactory.instantiate(requireContext().classLoader, PlayBroadcastChooseCoverBottomSheet::class.java.name) as PlayBroadcastChooseCoverBottomSheet
        changeCoverBottomSheet.listener = this
        changeCoverBottomSheet.setShowListener { changeCoverBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        changeCoverBottomSheet.show(requireFragmentManager(), PlayBroadcastChooseCoverBottomSheet.TAG_CHOOSE_COVER)
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
            else -> onChangeCoverClicked()
        }
    }

    private fun onImageCropped(resultImageUri: Uri) {
        selectedCoverUri = resultImageUri
        hideCoverCropLayout()
        renderCoverTitleLayout(resultImageUri)
    }

    private fun onFinishCoverTitleSetup() {
        if (::viewModel.isInitialized && selectedCoverUri != null) {
            viewModel.uploadCover(File(selectedCoverUri?.path).absolutePath)
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
}
