package com.tokopedia.media.editor.ui.component

import android.annotation.SuppressLint
import android.view.ViewGroup
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.data.repository.RotateFilterRepositoryImpl
import com.tokopedia.media.editor.ui.component.slider.MediaEditorSlider
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.picker.common.basecomponent.UiComponent
import javax.inject.Inject

class RotateToolUiComponent(viewGroup: ViewGroup, val listener: Listener) :
    UiComponent(viewGroup, R.id.uc_tool_rotate),
    MediaEditorSlider.Listener {

    @Inject
    lateinit var rotateFilterRepositoryImpl: RotateFilterRepositoryImpl

    private val rotateSlider = findViewById<MediaEditorSlider>(R.id.slider_rotate)
    private val flipBtn = findViewById<IconUnify>(R.id.flip_btn)
    private val rotateBtn = findViewById<IconUnify>(R.id.rotate_btn)
//    private val uCropView = findViewById<UCropView>(R.id.ucrop_rotate)
//    val cropImageView = uCropView.cropImageView
//    val cropOverlay = uCropView.overlayView

    private var scaleX = 1f
    private var scaleY = 1f
    private val scaleNormalizeValue get() = scaleX * scaleY

    var isRatioRotated = false
    var sliderValue = 0f

    var originalWidth = 0
    var originalHeight = 0

    var rotateNumber = 0

    @SuppressLint("ClickableViewAccessibility")
    fun setupView(paramData: EditorDetailUiModel) {
        container().show()

        rotateSlider.setRangeSliderValue(
            0,
            180,
            1,
            paramData.cropRotateValue.rotateDegree.toInt()
        )

        rotateSlider.listener = this

//        val sourceUri = Uri.fromFile(File(paramData.originalUrl))
//        val destinationUri = getDestinationUri(context)
//        cropImageView.setImageUri(sourceUri, destinationUri)
//
//        cropImageView.setTransformImageListener(object: TransformImageView.TransformImageListener{
//            override fun onLoadComplete() {
//                initializePreviousValue(paramData.rotateData)
//                val drawable = cropImageView.drawable
//                originalWidth = drawable.intrinsicWidth
//                originalHeight = drawable.intrinsicHeight
//                Log.d("asdasd","${drawable.intrinsicWidth} | ${drawable.intrinsicHeight}")
//            }
//
//            override fun onLoadFailure(e: Exception) {}
//
//            override fun onRotate(currentAngle: Float) {
//                val drawable = cropImageView.drawable
//                Log.d("asdasd","${drawable.intrinsicWidth} | ${drawable.intrinsicHeight}")
//            }
//
//            override fun onScale(currentScale: Float) {
//                Log.d("asdasd","scale = $currentScale")
//            }
//        })
//
//        cropImageView.setOnTouchListener { _, _ -> true }

//        cropOverlay.setShowCropGrid(false)
//        cropOverlay.setDimmedColor(Color.WHITE)

        flipBtn.setOnClickListener {
//            if (!isRatioRotated) {
//                scaleX = -scaleX
//            } else {
//                scaleY = -scaleY
//            }
//
//            cropImageView.scaleX = -cropImageView.scaleX
//
//            updateRotation()

            listener.onImageMirror()
        }

        rotateBtn.setOnClickListener {
//            cropImageView.postRotate(90f * scaleNormalizeValue)
//
//            if (isRatioRotated) {
//                cropOverlay.setTargetAspectRatio(originalWidth/originalHeight.toFloat())
//            } else {
//                cropOverlay.setTargetAspectRatio(originalHeight/originalWidth.toFloat())
//            }
//
//            cropImageView.zoomOutImage(0.01f)
//
//            isRatioRotated = !isRatioRotated
//            rotateNumber++
//
//            updateRotation()

            listener.onImageRotate(ROTATE_BTN_DEGREE)
        }
    }

//    fun getFinalRotationDegree(): Float{
//        return ((rotateNumber * ROTATE_BTN_DEGREE) + sliderValue)
//    }

//    fun getScale(): Pair<Float, Float>{
//        return Pair(scaleX, scaleY)
//    }

    private fun updateRotation() {
        listener.onRotateValueChanged(
            sliderValue * scaleNormalizeValue
        )
    }

//    private fun initializePreviousValue(rotateData: EditorRotateModel?){
//        if (rotateData == null) return
//
//        val cropView = uCropView.cropImageView
//        cropView.post {
//            cropView.scaleX = rotateData.scaleX
//            cropView.postRotate(rotateData.rotateDegree)
//            cropView.setImageToWrapCropBounds(false)
//        }
//    }

    override fun valueUpdated(step: Int, value: Float) {
        sliderValue = value
        updateRotation()
    }

    interface Listener {
        fun onRotateValueChanged(rotateValue: Float)
        fun onImageMirror()
        fun onImageRotate(rotateDegree: Float)
    }

    companion object {
        const val ROTATE_BTN_DEGREE = 90f
    }
}