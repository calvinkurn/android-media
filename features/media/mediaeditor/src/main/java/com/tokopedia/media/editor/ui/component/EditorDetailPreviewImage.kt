package com.tokopedia.media.editor.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.utils.getDestinationUri
import com.yalantis.ucrop.view.CropImageView
import com.yalantis.ucrop.view.TransformImageView
import com.yalantis.ucrop.view.UCropView

class EditorDetailPreviewImage(context: Context, attributeSet: AttributeSet) :
    UCropView(context, attributeSet) {

    var onLoadComplete: (() -> Unit)? = null
    var onLoadFailure: ((e: Exception) -> Unit)? = null
    var onRotate: ((angle: Float) -> Unit)? = null
    var onScale: ((scale: Float) -> Unit)? = null

    val scaleNormalizeValue get() = cropImageView.scaleX * cropImageView.scaleY

    fun initialize(uriSource: Uri) {
        val resultDestination = getDestinationUri(context)
        cropImageView.setImageUri(uriSource, resultDestination)
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