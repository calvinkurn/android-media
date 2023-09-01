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
import android.os.Handler
import android.util.AttributeSet
import androidx.core.graphics.values
import com.tokopedia.editor.ui.model.ImagePlacementModel
import com.tokopedia.unifycomponents.toPx
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
        setBackgroundColor(Color.RED)

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
        return 30f
    }

    override fun getMinScale(): Float {
        return 0.1f
    }

    // used as finish listener since setImageUri process using setImageBitmap on load complete
    override fun setImageBitmap(bitmap: Bitmap?) {
        super.setImageBitmap(bitmap)

        Handler().postDelayed({
            listener?.onFinish()
        }, FINISH_DELAY)
    }

    fun customCrop(onFinish: (placementModel: ImagePlacementModel) -> Unit) {
        val imageState = ImageState(
            mCropRect,
            RectUtils.trapToRect(mCurrentImageCorners),
            currentScale,
            currentAngle
        )

        val mCurrentImageRect = imageState.currentImageRect
        val mCurrentScale = imageState.currentScale

        val bitmapResult = Bitmap.createBitmap(
            mCropRect.width().toInt(),
            mCropRect.height().toInt(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmapResult)

        canvas.drawRect(
            0f,
            0f,
            bitmapResult.width.toFloat(),
            bitmapResult.height.toFloat(),
            Paint()
        )

        viewBitmap?.let {
            val scaledWidth = (it.width * mCurrentScale).toInt()
            val scaledHight = (it.height * mCurrentScale).toInt()
            val scaledBitmap = Bitmap.createScaledBitmap(it, scaledWidth, scaledHight, true)

            val matrix = Matrix()
            matrix.preRotate(currentAngle)

            val rotatedBitmap =
                Bitmap.createBitmap(scaledBitmap, 0, 0, scaledWidth, scaledHight, matrix, false)

            val finalBitmap =
                Bitmap.createBitmap(rotatedBitmap.width, rotatedBitmap.height, rotatedBitmap.config)
            Canvas(finalBitmap).apply {
                drawColor(Color.BLACK)
                drawBitmap(rotatedBitmap, 0f, 0f, null)
            }

            val xPos = (mCropRect.left - mCurrentImageRect.left) * -1f

            // top use 0f since we override the value for visual purpose on OverlayViewStories.kt on setOnTargetAspectRatio
            val yPos = (0f - mCurrentImageRect.top) * -1f

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
                    val imageMatrix = imageMatrix.values()
                    val translateX = imageMatrix[INDEX_TRANSLATE_X]
                    val translateY = imageMatrix[INDEX_TRANSLATE_Y]

                    onFinish(
                        ImagePlacementModel(
                            outputPath?.path ?: "",
                            currentScale,
                            currentAngle,
                            translateX,
                            translateY
                        )
                    )
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

    interface Listener {
        fun onFinish()
    }

    companion object {
        private const val INDEX_TRANSLATE_X = 2
        private const val INDEX_TRANSLATE_Y = 5

        private const val IMAGE_QUALITY = 100

        private const val FINISH_DELAY = 250L
    }
}
