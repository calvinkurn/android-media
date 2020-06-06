package com.tokopedia.play.broadcaster.view.fragment

import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastChooseCoverBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
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
class PlayCoverTitleSetupFragment @Inject constructor() : PlayBaseSetupFragment(), PlayBroadcastChooseCoverBottomSheet.Listener {

    private var imageLeft: Float = 0f
    private var imageTop: Float = 0f
    private var imageRight: Float = 0f
    private var imageBottom: Float = 0f

    override fun getScreenName(): String = "Play Cover Title Setup"

    override fun refresh() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_cover_title_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeLiveData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }

    override fun onGetCoverFromCamera(imageUri: Uri?) {
        imageUri?.let {
            showCoverCropLayout()
            renderCoverCropLayout(it)
        }
    }

    private fun initView() {
        containerChangeCover.setOnClickListener {
            onChangeCoverClicked()
        }
    }

    private fun setupView() {
        bottomSheetCoordinator.setupTitle(getString(R.string.play_prepare_cover_title_title))

        etPlayCoverTitleText.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Neutral_N0))
        etPlayCoverTitleText.setHintTextColor(resources.getColor(R.color.play_white_68))
        etPlayCoverTitleText.setSingleLine(false)
        tvPlayCoverTitleLabel.text = HtmlCompat.fromHtml(getString(R.string.play_prepare_cover_title_default_title_label),
                HtmlCompat.FROM_HTML_MODE_LEGACY)
        etPlayCoverTitleText.hint = getString(R.string.play_prepare_cover_title_default_title_placeholder)
        etPlayCoverTitleText.filters = arrayOf(InputFilter.LengthFilter(MAX_CHARS))
    }

    private fun observeLiveData() {
    }

    private fun onChangeCoverClicked() {
        val changeCoverBottomSheet = PlayBroadcastChooseCoverBottomSheet.getInstance()
        changeCoverBottomSheet.listener = this
        changeCoverBottomSheet.setShowListener { changeCoverBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        changeCoverBottomSheet.show(requireFragmentManager(), PlayBroadcastChooseCoverBottomSheet.TAG_CHOOSE_COVER)
    }

    private fun showCoverCropLayout() {
        containerPlayCoverTitleSetup.visibility = View.GONE
        containerPlayCoverCropSetup.visibility = View.VISIBLE
    }

    private fun hideCoverCropLayout() {
        containerPlayCoverTitleSetup.visibility = View.VISIBLE
        containerPlayCoverCropSetup.visibility = View.GONE
    }

    private fun renderCoverCropLayout(imageUri: Uri) {
        ivPlayCoverCropImage.setImageUri(imageUri, null)
        ivPlayCoverCropImage.isScaleEnabled = true
        ivPlayCoverCropImage.isRotateEnabled = false

        Handler().postDelayed({
            ivPlayCoverCropImage.setCropRect(ivPlayCoverCropOverlay.getCropRect())
        }, SECONDS)

        imageLeft = ivPlayCoverCropImage.left.toFloat()
        imageTop = ivPlayCoverCropImage.top.toFloat()
        imageRight = ivPlayCoverCropImage.right.toFloat()
        imageBottom = ivPlayCoverCropImage.bottom.toFloat()

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

    companion object {
        private const val DEFAULT_COMPRESS_QUALITY = 90
        private val DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG
        private const val MAX_CHARS = 38
        private const val SECONDS: Long = 1000
    }
}
