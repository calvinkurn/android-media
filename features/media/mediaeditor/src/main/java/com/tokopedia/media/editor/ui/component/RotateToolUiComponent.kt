package com.tokopedia.media.editor.ui.component

import android.annotation.SuppressLint
import android.net.Uri
import android.view.ViewGroup
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.data.repository.RotateFilterRepositoryImpl
import com.tokopedia.media.editor.ui.component.slider.MediaEditorSlider
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.utils.generateFileName
import com.tokopedia.media.editor.utils.getDestinationUri
import com.tokopedia.media.editor.utils.getEditorSaveFolderDir
import com.tokopedia.media.loader.loadImage
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.yalantis.ucrop.view.UCropView
import java.io.File
import javax.inject.Inject

class RotateToolUiComponent(viewGroup: ViewGroup, val listener: Listener) :
    UiComponent(viewGroup, R.id.uc_tool_rotate),
    MediaEditorSlider.Listener {

    @Inject
    lateinit var rotateFilterRepositoryImpl: RotateFilterRepositoryImpl

    private val rotateSlider = findViewById<MediaEditorSlider>(R.id.slider_rotate)
    private val flipBtn = findViewById<IconUnify>(R.id.flip_btn)
    private val rotateBtn = findViewById<IconUnify>(R.id.rotate_btn)
    val ucropView = findViewById<UCropView>(R.id.ucrop_rotate)
    private var data: EditorDetailUiModel? = null

    @SuppressLint("ClickableViewAccessibility")
    fun setupView(paramData: EditorDetailUiModel) {
        container().show()

        rotateSlider.setRangeSliderValue(
            0,
            180,
            1
        )

        rotateSlider.listener = this

        val sourceUri = Uri.fromFile(File(paramData.originalUrl))
        val destinationUri = getDestinationUri(context)
        ucropView.cropImageView.setImageUri(sourceUri, destinationUri)

        ucropView.cropImageView.setOnTouchListener { _, _ -> true }

        flipBtn.setOnClickListener {
            ucropView.cropImageView.scaleX = -ucropView.cropImageView.scaleX
        }

        rotateBtn.setOnClickListener {
            ucropView.cropImageView.postRotate(90f * ucropView.cropImageView.scaleX)

            ucropView.cropImageView.zoomOutImage(ucropView.cropImageView.minScale + 0.01f)
            ucropView.cropImageView.setImageToWrapCropBounds(false)
        }
    }

    override fun valueUpdated(step: Int, value: Float) {
        listener.onRotateValueChanged(value * ucropView.cropImageView.scaleX)
    }

    interface Listener {
        fun onRotateValueChanged(value: Float)
    }
}