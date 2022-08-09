package com.tokopedia.media.editor.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Handler
import android.util.AttributeSet
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.media.editor.ui.uimodel.EditorCropRectModel
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorRotateModel
import com.tokopedia.media.editor.utils.getDestinationUri
import com.tokopedia.media.loader.loadImageWithEmptyTarget
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.yalantis.ucrop.callback.BitmapCropCallback
import com.yalantis.ucrop.view.CropImageView
import com.yalantis.ucrop.view.TransformImageView
import com.yalantis.ucrop.view.UCropView
import kotlin.math.abs
import com.tokopedia.unifyprinciples.R as principleR

class EditorDetailPreviewImage(context: Context, attributeSet: AttributeSet) :
    UCropView(context, attributeSet) {

    var onLoadComplete: (() -> Unit)? = null
    var onLoadFailure: ((e: Exception) -> Unit)? = null
    var onRotate: ((angle: Float) -> Unit)? = null
    var onScale: ((scale: Float) -> Unit)? = null

    val scaleNormalizeValue get() = cropImageView.scaleX * cropImageView.scaleY

    fun initializeRotate(uriSource: Uri) {
        val resultDestination = getDestinationUri(context)
        cropImageView.setImageUri(uriSource, resultDestination)
        overlayView.setDimmedColor(ContextCompat.getColor(context, principleR.color.Unify_Static_White))
        initListener()
    }

    fun initializeBrightness(uriSource: Uri){
        val resultDestination = getDestinationUri(context)
        cropImageView.setImageUri(uriSource, resultDestination)
        hideOverlay()
        initListener()
    }

    fun initializeContrast(uriSource: Uri){
        val resultDestination = getDestinationUri(context)
        cropImageView.setImageUri(uriSource, resultDestination)
        hideOverlay()
        initListener()
    }

    fun initializeWatermark(uriSource: Uri){
        val resultDestination = getDestinationUri(context)
        cropImageView.setImageUri(uriSource, resultDestination)
        hideOverlay()
        initListener()
    }

    fun initializeRemoveBackground(uriSource: Uri){
        val resultDestination = getDestinationUri(context)
        cropImageView.setImageUri(uriSource, resultDestination)
        hideOverlay()
        initListener()
    }

    fun initializeCrop(uriSource: Uri){
        val resultDestination = getDestinationUri(context)
        cropImageView.setImageUri(uriSource, resultDestination)
        initListener()
    }

    @SuppressLint("ClickableViewAccessibility")
            /**
             * Component will lose the ability to interact with user input via touch
             * if you want to enable the interaction again please re-init the component
             */
    fun disabledTouchEvent() {
        cropImageView.setOnTouchListener { _, _ ->
            true
        }
    }

    fun getScale(): Pair<Float, Float>{
        return Pair(cropImageView.scaleX, cropImageView.scaleY)
    }

    fun hideOverlay(){
        overlayView.hide()
    }

    fun showOverlay(){
        overlayView.show()
    }

    fun getBitmap(): Bitmap{
        return cropImageView.drawable.toBitmap()
    }

    // conditional crop function for rotate feature
    fun cropRotate(
        finalRotationDegree: Float,
        sliderValue: Float,
        rotateNumber: Int,
        data: EditorDetailUiModel?,
        isRotate: Boolean,
        onCropFinish: (cropResult: Bitmap) -> Unit,
    ) {
        val bitmap = cropImageView.drawable.toBitmap()
        val cropViewScale = getScale()
        val cropViewScaleX = cropViewScale.first
        val cropViewScaleY = cropViewScale.second

        // if rotated image is same with original ratio without overflow, ucrop will skip it
        // need to manually crop & save
        if (cropImageView.currentAngle % 90f == 0f && rotateNumber != 0 && isRotate) {
            val matrix = Matrix()
            matrix.preScale(
                cropViewScaleX,
                cropViewScaleY
            )

            matrix.postRotate(abs(finalRotationDegree))

            // set crop area on data that will be pass to landing pass for state
            data?.rotateData = EditorRotateModel(
                sliderValue,
                cropViewScaleX,
                cropViewScaleY,
                0,
                0,
                bitmap.width,
                bitmap.height,
                rotateNumber
            )

            onCropFinish(Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true))
            return
        }

        // if rotated image is overflow from the original ratio then we can use ucrop crop feature
        cropImageView.cropAndSaveImage(
            Bitmap.CompressFormat.PNG,
            100,
            object : BitmapCropCallback {
                override fun onBitmapCropped(
                    resultUri: Uri,
                    offsetX: Int,
                    offsetY: Int,
                    imageWidth: Int,
                    imageHeight: Int
                ) {
                    onCropFinish(
                        getProcessedBitmap(
                            bitmap,
                            offsetX, offsetY, imageWidth, imageHeight,
                            finalRotationDegree = finalRotationDegree,
                            sliderValue = sliderValue,
                            rotateNumber = if(isRotate) rotateNumber else  -1,
                            data = data
                        )
                    )
                }

                override fun onCropFailure(t: Throwable) {
                    Toast.makeText(context, "Crop Error", Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    // crop function that support mirroring feature
    fun getProcessedBitmap(
        originalBitmap: Bitmap,
        offsetX: Int,
        offsetY: Int,
        imageWidth: Int,
        imageHeight: Int,
        finalRotationDegree: Float,
        sliderValue: Float,
        rotateNumber: Int,
        data: EditorDetailUiModel?
    ): Bitmap {
        val originalWidth = originalBitmap.width
        val originalHeight = originalBitmap.height

        val scale = getScale()
        val scaleX = scale.first
        val scaleY = scale.second

        // matrix scale didn't affect rotation value, positive will always clockwise on matrix
        val rotateDegree = abs(finalRotationDegree)

        val matrix = Matrix()

        matrix.preScale(scaleX, scaleY)
        matrix.postRotate(
            rotateDegree,
            (originalWidth / 2).toFloat(),
            (originalHeight / 2).toFloat()
        )

        val rotatedBitmap = Bitmap.createBitmap(
            originalBitmap,
            0,
            0,
            originalWidth,
            originalHeight,
            matrix,
            true
        )

        // set crop area on data that will be pass to landing pass for state
        if (rotateNumber >= 0){
            data?.rotateData = EditorRotateModel(
                sliderValue,
                scaleX,
                scaleY,
                offsetX,
                offsetY,
                imageWidth,
                imageHeight,
                rotateNumber
            )
        } else {
            data?.cropBound = EditorCropRectModel(
                offsetX,
                offsetY,
                imageWidth,
                imageHeight,
                cropImageView.currentScale,
                ""
            )
        }

//        return Bitmap.createBitmap(rotatedBitmap, offsetX, offsetY, imageWidth, imageHeight)
        return if (rotateNumber != -1)
            Bitmap.createBitmap(rotatedBitmap, offsetX, offsetY, imageWidth, imageHeight)
        else {
            val normalizeX = if (scaleX == -1f) rotatedBitmap.width - (offsetX + imageWidth) else offsetX
            val normalizeY = if (scaleY == -1f) rotatedBitmap.height - (offsetY + imageHeight) else offsetY
            Bitmap.createBitmap(rotatedBitmap, normalizeX, normalizeY, imageWidth, imageHeight)
        }

    }

    private fun initListener(){
        cropImageView.setTransformImageListener(object: TransformImageView.TransformImageListener{
            override fun onLoadComplete() {
                this@EditorDetailPreviewImage.onLoadComplete?.invoke()
            }

            override fun onLoadFailure(e: java.lang.Exception) {
                this@EditorDetailPreviewImage.onLoadFailure?.invoke(e)
            }

            override fun onRotate(currentAngle: Float) {
                this@EditorDetailPreviewImage.onRotate?.invoke(currentAngle)
            }

            override fun onScale(currentScale: Float) {
                this@EditorDetailPreviewImage.onScale?.invoke(currentScale)
            }
        })
    }
}