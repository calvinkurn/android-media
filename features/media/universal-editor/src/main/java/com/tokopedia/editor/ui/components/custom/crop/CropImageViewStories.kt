package com.tokopedia.editor.ui.components.custom.crop

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import android.util.AttributeSet
import com.yalantis.ucrop.model.ImageState
import com.yalantis.ucrop.util.BitmapLoadUtils
import com.yalantis.ucrop.util.RectUtils
import com.yalantis.ucrop.view.CropImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.OutputStream

open class CropImageViewStories : CropImageView {

    private val mCropRectClone: RectF = RectF()

    private var outputPath: Uri? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun setImageToWrapCropBounds() {
        // expected to override original action to do nothing
    }

    override fun setImageToWrapCropBounds(animate: Boolean) {
        // expected to override original action to do nothing
    }

    override fun setCropRect(cropRect: RectF) {
        mCropRectClone.set(
            cropRect.left - paddingLeft,
            cropRect.top - paddingTop,
            cropRect.right - paddingRight,
            cropRect.bottom - paddingBottom
        )
        super.setCropRect(cropRect)
    }

    override fun setImageUri(imageUri: Uri, outputUri: Uri?) {
        outputPath = outputUri
        super.setImageUri(imageUri, outputUri)
    }

    override fun getMaxScale(): Float {
        return 30f
    }

    override fun getMinScale(): Float {
        return 0.1f
    }

    fun customCrop(onFinish: (url: String?) -> Unit) {
        val imageState = ImageState(
            mCropRectClone, RectUtils.trapToRect(mCurrentImageCorners),
            currentScale, currentAngle
        )

        val mCurrentImageRect = imageState.currentImageRect
        val mCurrentScale = imageState.currentScale

        val bitmapResult = Bitmap.createBitmap(mCropRectClone.width().toInt(), mCropRectClone.height().toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmapResult)

        canvas.drawRect(0f, 0f, bitmapResult.width.toFloat(),bitmapResult.height.toFloat(), Paint())

        viewBitmap?.let {
            val scaledWidth = (it.width * mCurrentScale).toInt()
            val scaledHight = (it.height * mCurrentScale).toInt()
            val scaledBitmap = Bitmap.createScaledBitmap(it, scaledWidth, scaledHight, true)

            val matrix = Matrix()
            matrix.preRotate(currentAngle)

            val rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledWidth, scaledHight, matrix, false)

            val finalBitmap = Bitmap.createBitmap(rotatedBitmap.width, rotatedBitmap.height, rotatedBitmap.config)
            Canvas(finalBitmap).apply {
                drawColor(Color.BLACK)
                drawBitmap(rotatedBitmap, 0f, 0f, null)
            }

            val xPos = (mCropRectClone.left - mCurrentImageRect.left) * -1f
            val yPos = (mCropRectClone.top - mCurrentImageRect.top) * -1f

            canvas.drawBitmap(
                finalBitmap,
                xPos,
                yPos,
                null
            )
        }

        CoroutineScope(Dispatchers.IO).launch {
            async {
                saveImage(bitmapResult)

                with(Dispatchers.Main) {
                    onFinish(outputPath?.path)
                }
            }
        }
    }

    fun processStyledAttributesOpen(a: TypedArray) {
        processStyledAttributes(a)
    }

    private fun saveImage(croppedBitmap: Bitmap) {
        val context: Context = context ?: return

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

    companion object {
        private const val IMAGE_QUALITY = 100
    }
}
