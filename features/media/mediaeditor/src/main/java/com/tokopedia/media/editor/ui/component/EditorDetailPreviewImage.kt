package com.tokopedia.media.editor.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.yalantis.ucrop.view.CropImageView
import com.yalantis.ucrop.view.TransformImageView
import com.yalantis.ucrop.view.UCropView

class EditorDetailPreviewImage(context: Context, attributeSet: AttributeSet) :
    UCropView(context, attributeSet) {

    private var listener: Listener? = null

    private var tempScaleX = 1f
    private var tempScaleY = 1f

    private val scaleNormalizeValue get() = scaleX * scaleY
    private var rotateNumber = 0
    var sliderValue = 0f

    fun initialize(listener: Listener?, uri: Uri) {
        cropImageView.setImageUri(uri, Uri.parse(TEMP_OUTPUT_FILE))
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

    fun mirrorImage(isMirrorXAxis: Boolean, isMirrorYAxis: Boolean){
        if(isMirrorXAxis){
            scaleX = -scaleX
        }

        if(isMirrorYAxis){
            scaleY = -scaleY
        }
    }

    fun getFinalRotationDegree(): Float{
        return ((rotateNumber * RotateToolUiComponent.ROTATE_BTN_DEGREE) + sliderValue)
    }

    fun getScale(): Pair<Float, Float>{
        return Pair(scaleX, scaleY)
    }

    fun hideOverlay(){
        overlayView.hide()
    }

    fun showOverlay(){
        overlayView.show()
    }

    private fun initListener(){
        cropImageView.setTransformImageListener(object: TransformImageView.TransformImageListener{
            override fun onLoadComplete() {
                listener?.onLoadComplete()
            }

            override fun onLoadFailure(e: java.lang.Exception) {
                listener?.onLoadFailure(e)
            }

            override fun onRotate(currentAngle: Float) {
                listener?.onRotate(currentAngle)
            }

            override fun onScale(currentScale: Float) {
                listener?.onScale(currentScale)
            }
        })
    }

    interface Listener {
        fun onLoadComplete()
        fun onLoadFailure(e: Exception)
        fun onRotate(currentAngle: Float)
        fun onScale(currentScale: Float)
    }

    companion object {
        private const val TEMP_OUTPUT_FILE = "media_editor_temp_result"
    }
}