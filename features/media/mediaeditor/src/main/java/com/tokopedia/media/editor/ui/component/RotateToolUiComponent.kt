package com.tokopedia.media.editor.ui.component

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.RectF
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.view.ViewGroup
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.data.repository.RotateFilterRepositoryImpl
import com.tokopedia.media.editor.ui.component.slider.MediaEditorSlider
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorRotateModel
import com.tokopedia.media.editor.utils.generateFileName
import com.tokopedia.media.editor.utils.getDestinationUri
import com.tokopedia.media.editor.utils.getEditorSaveFolderDir
import com.tokopedia.media.loader.loadImage
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.yalantis.ucrop.view.TransformImageView
import com.yalantis.ucrop.view.UCropView
import java.io.File
import java.lang.Exception
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.min

class RotateToolUiComponent(viewGroup: ViewGroup, val listener: Listener) :
    UiComponent(viewGroup, R.id.uc_tool_rotate),
    MediaEditorSlider.Listener {

    @Inject
    lateinit var rotateFilterRepositoryImpl: RotateFilterRepositoryImpl

    private val rotateSlider = findViewById<MediaEditorSlider>(R.id.slider_rotate)
    private val flipBtn = findViewById<IconUnify>(R.id.flip_btn)
    private val rotateBtn = findViewById<IconUnify>(R.id.rotate_btn)
    private val uCropView = findViewById<UCropView>(R.id.ucrop_rotate)
    val cropImageView = uCropView.cropImageView
    val cropOverlay = uCropView.overlayView

    private var scaleX = 1f
    private var scaleY = 1f

    var isRatioRotated = false
    var sliderValue = 0f

    var originalWidth = 0
    var originalHeight = 0

    @SuppressLint("ClickableViewAccessibility")
    fun setupView(paramData: EditorDetailUiModel) {
        container().show()

        rotateSlider.setRangeSliderValue(
            0,
            180,
            1,
            paramData.rotateData?.rotateDegree?.toInt() ?: 0
        )

        rotateSlider.listener = this

        val sourceUri = Uri.fromFile(File(paramData.originalUrl))
        val destinationUri = getDestinationUri(context)
        cropImageView.setImageUri(sourceUri, destinationUri)

        cropImageView.setTransformImageListener(object: TransformImageView.TransformImageListener{
            override fun onLoadComplete() {
                initializePreviousValue(paramData.rotateData)
                val drawable = cropImageView.drawable
                originalWidth = drawable.intrinsicWidth
                originalHeight = drawable.intrinsicHeight
                Log.d("asdasd","${drawable.intrinsicWidth} | ${drawable.intrinsicHeight}")
            }

            override fun onLoadFailure(e: Exception) {}

            override fun onRotate(currentAngle: Float) {
                val drawable = cropImageView.drawable
                Log.d("asdasd","${drawable.intrinsicWidth} | ${drawable.intrinsicHeight}")
            }

            override fun onScale(currentScale: Float) {
                Log.d("asdasd","scale = $currentScale")
            }
        })

        cropImageView.setOnTouchListener { _, _ -> true }

        cropOverlay.setShowCropGrid(false)
        cropOverlay.setDimmedColor(Color.WHITE)

        flipBtn.setOnClickListener {
            if (!isRatioRotated) {
                scaleX = -cropImageView.scaleX
                scaleY = 1f
            } else {
                scaleX = 1f
                scaleY = -cropImageView.scaleY
            }
            cropImageView.scaleX = scaleX
            cropImageView.scaleY = scaleY

            updateRotation()
        }

        rotateBtn.setOnClickListener {
            cropImageView.postRotate(90f)

            if (isRatioRotated) {
                cropOverlay.setTargetAspectRatio(originalWidth/originalHeight.toFloat())
            } else {
                cropOverlay.setTargetAspectRatio(originalHeight/originalWidth.toFloat())
            }

            cropImageView.zoomOutImage(0.01f)

            isRatioRotated = !isRatioRotated

            updateRotation()
        }
    }

    private fun updateRotation() {
        val scaleNormalizeValue = min(scaleX, scaleY)
        listener.onRotateValueChanged(
            sliderValue * scaleNormalizeValue,
            scaleX,
            scaleY
        )
    }

    private fun initializePreviousValue(rotateData: EditorRotateModel?){
        if (rotateData == null) return

        val cropView = uCropView.cropImageView
        cropView.post {
            cropView.scaleX = rotateData.scaleX
//            cropView.targetAspectRatio = rotateData.imageRatio
            cropView.postRotate(rotateData.rotateDegree)
            cropView.setImageToWrapCropBounds(false)
        }
    }

    override fun valueUpdated(step: Int, value: Float) {
        sliderValue = value
        updateRotation()
    }

    interface Listener {
        fun onRotateValueChanged(rotateValue: Float, scaleX: Float, scaleY: Float)
    }
}