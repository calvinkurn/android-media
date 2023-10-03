package com.tokopedia.editor.ui.widget.crop

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import android.os.Handler
import android.util.AttributeSet
import androidx.core.graphics.values
import com.tokopedia.editor.ui.model.CustomCropResult
import com.yalantis.ucrop.model.ImageState
import com.yalantis.ucrop.util.BitmapLoadUtils
import com.yalantis.ucrop.util.RectUtils
import com.yalantis.ucrop.view.CropImageView
import java.io.File
import java.io.OutputStream
import kotlin.math.abs
import kotlin.math.max

open class StoryCropImageView : CropImageView {

    private val mCropRect: RectF = RectF()

    private var outputPath: Uri? = null

    var listener: Listener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun setImageToWrapCropBounds() {
        // expected to override original action to do nothing
    }

    override fun setImageToWrapCropBounds(animate: Boolean) {
        // expected to override original action to do nothing
    }

    override fun setCropRect(cropRect: RectF) {
        mCropRect.set(
            cropRect.left - paddingLeft,
            cropRect.top - paddingTop,
            cropRect.right - paddingRight,
            cropRect.bottom - paddingBottom
        )
        super.setCropRect(cropRect)
    }

    override fun setImageUri(imageUri: Uri, outputUri: Uri?) {
        outputPath = outputUri

        outputPath?.path?.let {
            val file = File(it)
            if (!file.isFile) {
                file.createNewFile()
            }
        }

        super.setImageUri(imageUri, outputPath)
    }

    override fun getMaxScale(): Float {
        return MAX_ZOOM
    }

    override fun getMinScale(): Float {
        return MIN_ZOOM
    }

    // used as finish listener since setImageUri process using setImageBitmap on load complete
    override fun setImageBitmap(bitmap: Bitmap?) {
        super.setImageBitmap(bitmap)

        Handler().postDelayed({
            listener?.onFinish()
        }, FINISH_DELAY)
    }

    fun customCrop(onFinish: (cropResult: CustomCropResult) -> Unit) {
        val imageState = ImageState(
            mCropRect,
            RectUtils.trapToRect(mCurrentImageCorners),
            currentScale,
            currentAngle
        )

        val (cropWidthSize, cropHeightSize) = Pair(
            mCropRect.width().toInt(),
            mCropRect.height().toInt()
        )

        val mCurrentImageRect = imageState.currentImageRect
        val mCurrentScale = imageState.currentScale

        val bitmapResult = Bitmap.createBitmap(
            cropWidthSize,
            cropHeightSize,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmapResult)
        canvas.drawRect(
            0f,
            0f,
            cropWidthSize.toFloat(),
            cropHeightSize.toFloat(),
            Paint().apply {
                color = Color.BLACK
            }
        )

        viewBitmap?.let {
            val matrix = Matrix()
            matrix.preRotate(currentAngle)

            val rotateBitmap = Bitmap.createBitmap(it, 0, 0, it.width, it.height, matrix, false)

            val cropX = ((mCurrentImageRect.left * -1) / mCurrentScale ).toInt()
            val cropY = ((mCurrentImageRect.top * -1) / mCurrentScale ).toInt()

            val sourceWidth = cropWidthSize / mCurrentScale
            val sourceHeight = cropHeightSize / mCurrentScale

            val sourceRect = Rect(cropX, cropY, (cropX + sourceWidth).toInt(), (cropY + sourceHeight).toInt())
            val targetRect = Rect(0,0, cropWidthSize, cropHeightSize)

            canvas.drawRect(0f, 0f, bitmapResult.width.toFloat(), bitmapResult.height.toFloat(), Paint().apply {
                color = Color.BLACK
            })
            canvas.drawBitmap(rotateBitmap, sourceRect, targetRect, null)
        }

        onFinish(
            CustomCropResult(
                bitmapResult, imageMatrix.values(), outputPath?.path ?: ""
            )
        )
    }

    fun processStyledAttributesOpen(a: TypedArray) {
        processStyledAttributes(a)
    }

    private fun saveImage(croppedBitmap: Bitmap) {
        outputPath?.path?.let {
            var outputStream: OutputStream? = null
            try {
                outputStream =
                    context.contentResolver.openOutputStream(Uri.fromFile(File(it)))
                croppedBitmap.compress(Bitmap.CompressFormat.PNG, IMAGE_QUALITY, outputStream)
                croppedBitmap.recycle()
            } finally {
                BitmapLoadUtils.close(outputStream)
            }
        }
    }

    interface Listener {
        fun onFinish()
    }

    companion object {
        private const val IMAGE_QUALITY = 100

        private const val FINISH_DELAY = 250L

        private const val MAX_ZOOM = 20f // 20x zoom in
        private const val MIN_ZOOM = 0.1f // 0.1x zoom out
    }
}
