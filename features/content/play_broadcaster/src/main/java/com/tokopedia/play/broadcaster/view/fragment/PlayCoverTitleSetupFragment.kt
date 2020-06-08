package com.tokopedia.play.broadcaster.view.fragment

import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.CoverSourceEnum
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastChooseCoverBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastCoverFromGalleryBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.widget.PlayCropImageView
import com.yalantis.ucrop.callback.BitmapCropCallback
import com.yalantis.ucrop.model.CropParameters
import com.yalantis.ucrop.model.ExifInfo
import com.yalantis.ucrop.model.ImageState
import com.yalantis.ucrop.task.BitmapCropTask
import com.yalantis.ucrop.util.RectUtils
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_play_cover_title_setup.*
import kotlinx.android.synthetic.main.layout_play_cover_crop.*
import kotlinx.android.synthetic.main.layout_play_cover_title_setup.*
import javax.inject.Inject

/**
 * Created by furqan on 02/06/20
 */
class PlayCoverTitleSetupFragment @Inject constructor()
    : PlayBaseSetupFragment(), PlayBroadcastChooseCoverBottomSheet.Listener {

    private var selectedImageUrlList = arrayListOf<String>()
    private var liveTitle: String = ""
    private var selectedCoverUri: Uri? = null
    private var coverSource: CoverSourceEnum = CoverSourceEnum.NONE

    override fun getScreenName(): String = "Play Cover Title Setup"

    override fun onInterceptBackPressed(): Boolean = false

    override fun refresh() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            selectedImageUrlList = it.getStringArrayList(EXTRA_SELECTED_PRODUCT_IMAGE_URL_LIST)
                    ?: arrayListOf()
            liveTitle = it.getString(EXTRA_LIVE_TITLE, "") ?: ""
        } ?: arguments?.let {
            selectedImageUrlList = it.getStringArrayList(EXTRA_SELECTED_PRODUCT_IMAGE_URL_LIST)
                    ?: arrayListOf()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_cover_title_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList(EXTRA_SELECTED_PRODUCT_IMAGE_URL_LIST, selectedImageUrlList)
        outState.putString(EXTRA_LIVE_TITLE, liveTitle)
    }

    override fun onGetCoverFromCamera(imageUri: Uri?) {
        imageUri?.let {
            coverSource = CoverSourceEnum.CAMERA
            showCoverCropLayout()
            renderCoverCropLayout(it)
        }
    }

    override fun onGetCoverFromProduct(imageUrl: Uri?) {
        imageUrl?.let {
            coverSource = CoverSourceEnum.PRODUCT
            showCoverCropLayout()
            renderCoverCropLayout(it)
        }
    }

    override fun onChooseFromGalleryClicked() {
        val coverFromGalleryBottomSheet = PlayBroadcastCoverFromGalleryBottomSheet()
//        coverFromGalleryBottomSheet.listener = this
        coverFromGalleryBottomSheet.setShowListener { coverFromGalleryBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        coverFromGalleryBottomSheet.show(requireFragmentManager(), PlayBroadcastChooseCoverBottomSheet.TAG_CHOOSE_COVER)
    }

    private fun initView() {
        containerChangeCover.setOnClickListener {
            onChangeCoverClicked()
        }
        ivPlayCoverImage.setOnClickListener {
            onChangeCoverClicked()
        }
    }

    private fun setupView() {
        bottomSheetCoordinator.setupTitle(getString(R.string.play_prepare_cover_title_title))
        bottomSheetCoordinator.showBottomAction(false)

        etPlayCoverTitleText.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Neutral_N0))
        etPlayCoverTitleText.setHintTextColor(resources.getColor(R.color.play_white_68))
        etPlayCoverTitleText.setSingleLine(false)
        etPlayCoverTitleText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                liveTitle = text.toString()
                setupNextButton()
            }
        })
        tvPlayCoverTitleLabel.text = HtmlCompat.fromHtml(getString(R.string.play_prepare_cover_title_default_title_label),
                HtmlCompat.FROM_HTML_MODE_LEGACY)
        etPlayCoverTitleText.hint = getString(R.string.play_prepare_cover_title_default_title_placeholder)
        etPlayCoverTitleText.filters = arrayOf(InputFilter.LengthFilter(MAX_CHARS))

        setupNextButton()
        setupCoverLabelText()
    }

    private fun onChangeCoverClicked() {
        val changeCoverBottomSheet = PlayBroadcastChooseCoverBottomSheet.getInstance(selectedImageUrlList)
        changeCoverBottomSheet.listener = this
        changeCoverBottomSheet.setShowListener { changeCoverBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        changeCoverBottomSheet.show(requireFragmentManager(), PlayBroadcastChooseCoverBottomSheet.TAG_CHOOSE_COVER)

        liveTitle = etPlayCoverTitleText.text.toString()
    }

    private fun showCoverCropLayout() {
        containerPlayCoverTitleSetup.visibility = View.GONE
        containerPlayCoverCropSetup.visibility = View.VISIBLE
    }

    private fun hideCoverCropLayout() {
        containerPlayCoverTitleSetup.visibility = View.VISIBLE
        containerPlayCoverCropSetup.visibility = View.GONE
    }

    private fun renderCoverTitleLayout(resultImageUri: Uri) {
        selectedCoverUri = resultImageUri
        if (liveTitle.isNotEmpty()) etPlayCoverTitleText.setText(liveTitle)
        ivPlayCoverImage.setImageURI(selectedCoverUri)
        setupCoverLabelText()
        setupNextButton()
    }

    private fun renderCoverCropLayout(imageUri: Uri) {
        containerPlayCoverCropImage.removeAllViews()
        val ivPlayCoverCropImage = PlayCropImageView(requireContext())
        ivPlayCoverCropImage.setImageUri(imageUri, null)
        ivPlayCoverCropImage.isScaleEnabled = true
        ivPlayCoverCropImage.isRotateEnabled = false

        // need delay until onDraw for overlay is called to get the crop points
        ivPlayCoverCropImage.post {
            ivPlayCoverCropImage.layoutParams.height = resources.getDimensionPixelSize(R.dimen.play_cover_height)
        }
        Handler().postDelayed({
            ivPlayCoverCropImage.setCropRect(ivPlayCoverCropOverlay.getCropRect())
        }, SECONDS)

        containerPlayCoverCropImage.addView(ivPlayCoverCropImage)

        btnPlayCoverCropNext.setOnClickListener {
            ivPlayCoverCropImage.viewBitmap?.let {
                cropImage(ivPlayCoverCropImage.imageInputPath,
                        ivPlayCoverCropOverlay.getCropRect(),
                        RectUtils.trapToRect(ivPlayCoverCropImage.getCurrentImageCorners()),
                        ivPlayCoverCropImage.currentScale,
                        ivPlayCoverCropImage.currentAngle,
                        ivPlayCoverCropImage.exifInfo,
                        it)
            }
        }
    }

    private fun onImageCropped(resultImageUri: Uri) {
        hideCoverCropLayout()
        renderCoverTitleLayout(resultImageUri)
    }

    /**
     * The Crop Task already runs in background, so we don't need to use View Model
     */
    private fun cropImage(imagePath: String, cropRect: RectF, currentImageRect: RectF,
                          currentScale: Float, currentAngle: Float, exifInfo: ExifInfo,
                          viewBitmap: Bitmap) {

        val isPng = ImageUtils.isPng(imagePath)
        val imageOutputDirectory = ImageUtils.getTokopediaPhotoPath(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA, isPng)
        val imageState = ImageState(cropRect, currentImageRect,
                currentScale, currentAngle)
        val cropParams = CropParameters(ImageUtils.DEF_WIDTH, ImageUtils.DEF_HEIGHT,
                DEFAULT_COMPRESS_FORMAT, DEFAULT_COMPRESS_QUALITY,
                imagePath, imageOutputDirectory.absolutePath, exifInfo)

        context?.let {
            BitmapCropTask(it, viewBitmap, imageState, cropParams, object : BitmapCropCallback {
                override fun onBitmapCropped(resultUri: Uri, offsetX: Int, offsetY: Int, imageWidth: Int, imageHeight: Int) {
                    onImageCropped(resultUri)
                }

                override fun onCropFailure(t: Throwable) {
                    t.printStackTrace()
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }

    private fun setupCoverLabelText() {
        if (selectedCoverUri != null) {
            tvPlayChangeCoverLabel.text = getString(R.string.play_prepare_cover_title_change_cover_label)
        } else {
            tvPlayChangeCoverLabel.text = getString(R.string.play_prepare_cover_title_add_cover_label)
        }
    }

    private fun setupNextButton() {
        btnPlayPrepareBroadcastNext.isEnabled = liveTitle.isNotEmpty() && selectedCoverUri != null
    }

    companion object {
        const val EXTRA_SELECTED_PRODUCT_IMAGE_URL_LIST = "EXTRA_SELECTED_PRODUCT_IMAGE_URL_LIST"
        private const val EXTRA_LIVE_TITLE = "EXTRA_LIVE_TITLE"

        private const val DEFAULT_COMPRESS_QUALITY = 90
        private val DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG
        private const val MAX_CHARS = 38
        private const val SECONDS: Long = 1000
    }
}
