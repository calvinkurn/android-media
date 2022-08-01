package com.tokopedia.media.editor.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Handler
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorRotateModel
import com.tokopedia.media.editor.utils.getDestinationUri
import com.yalantis.ucrop.view.CropImageView
import com.yalantis.ucrop.view.TransformImageView
import com.yalantis.ucrop.view.UCropView
import com.tokopedia.unifyprinciples.R as principleR

class EditorDetailPreviewImage(context: Context, attributeSet: AttributeSet) :
    UCropView(context, attributeSet) {

    var onLoadComplete: (() -> Unit)? = null
    var onLoadFailure: ((e: Exception) -> Unit)? = null
    var onRotate: ((angle: Float) -> Unit)? = null
    var onScale: ((scale: Float) -> Unit)? = null

    val scaleNormalizeValue get() = cropImageView.scaleX * cropImageView.scaleY

    fun initializeRotate(uriSource: Uri, previousData: EditorRotateModel?) {
        val resultDestination = getDestinationUri(context)
        cropImageView.setImageUri(uriSource, resultDestination)
        overlayView.setDimmedColor(ContextCompat.getColor(context, principleR.color.Unify_Static_White))
        initListener(previousData, ROTATE_EDITOR)
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

    private fun initListener(data: Any?, previousDataModel: Int){
        if(data == null) return
        cropImageView.setTransformImageListener(object: TransformImageView.TransformImageListener{
            override fun onLoadComplete() {
                if(previousDataModel == ROTATE_EDITOR){

                    this@EditorDetailPreviewImage.onLoadComplete?.invoke()
                }
            }

            override fun onLoadFailure(e: java.lang.Exception) {}

            override fun onRotate(currentAngle: Float) {}

            override fun onScale(currentScale: Float) {}
        })
    }

    companion object{
        private const val ROTATE_EDITOR = 0
        private const val CROP_EDITOR = 1
    }
}