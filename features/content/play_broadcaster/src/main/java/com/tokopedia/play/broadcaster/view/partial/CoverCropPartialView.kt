package com.tokopedia.play.broadcaster.view.partial

import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import android.os.Handler
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.widget.PlayCropImageView
import com.tokopedia.play.broadcaster.view.widget.PlayRectCropImageOverlay
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.yalantis.ucrop.model.ExifInfo
import com.yalantis.ucrop.util.RectUtils

class CoverCropPartialView(
        container: ViewGroup,
        private val listener: Listener
) : PartialView(container, R.id.cl_cover_crop) {

    private val llImageContainer: LinearLayout = findViewById(R.id.ll_image_container)
    private val loaderImage: LoaderUnify = findViewById(R.id.loader_image)
    private val ivCropOverlay: PlayRectCropImageOverlay = findViewById(R.id.iv_crop_overlay)
    private val llCropAction: LinearLayout = findViewById(R.id.ll_crop_action)
    private val btnCropChange: UnifyButton = findViewById(R.id.btn_crop_change)
    private val btnCropAdd: UnifyButton = findViewById(R.id.btn_crop_add)

    fun show() {
        rootView.show()
    }

    fun hide() {
        rootView.hide()
    }

    fun clickAdd() {
        btnCropAdd.performClick()
    }

    fun setImageForCrop(imageUri: Uri?) {
        llImageContainer.removeAllViews()

        if (imageUri != null) {
            val ivPlayCoverCropImage = PlayCropImageView(rootView.context)
            ivPlayCoverCropImage.setImageUri(imageUri, null)
            ivPlayCoverCropImage.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            ivPlayCoverCropImage.isScaleEnabled = true
            ivPlayCoverCropImage.isRotateEnabled = false

            // need delay until onDraw for overlay is called to get the crop points
            Handler().postDelayed({
                ivPlayCoverCropImage.setCropRect(ivCropOverlay.getCropRect())
            }, SECONDS)

            llImageContainer.addView(ivPlayCoverCropImage)

            btnCropAdd.setOnClickListener {
                ivPlayCoverCropImage.viewBitmap?.let { bitmap ->
                    listener.onAddButtonClicked(
                            this,
                            ivPlayCoverCropImage.imageInputPath,
                            ivCropOverlay.getCropRect(),
                            RectUtils.trapToRect(ivPlayCoverCropImage.getCurrentImageCorners()),
                            ivPlayCoverCropImage.currentScale,
                            ivPlayCoverCropImage.currentAngle,
                            ivPlayCoverCropImage.exifInfo,
                            bitmap
                    )
                }
            }

            loaderImage.gone()
            btnCropAdd.isEnabled = true
            btnCropChange.isEnabled = true
        } else {
            loaderImage.show()
            btnCropAdd.isEnabled = false
            btnCropChange.isEnabled = false
        }

        btnCropChange.setOnClickListener {
            llImageContainer.removeAllViews()
            listener.onChangeButtonClicked(this)
        }
    }

    interface Listener {

        fun onAddButtonClicked(
                view: CoverCropPartialView,
                imageInputPath: String,
                cropRect: RectF,
                currentImageRect: RectF,
                currentScale: Float,
                currentAngle: Float,
                exifInfo: ExifInfo,
                viewBitmap: Bitmap
        )

        fun onChangeButtonClicked(view: CoverCropPartialView)
    }

    companion object {
        private const val SECONDS: Long = 1000
    }
}