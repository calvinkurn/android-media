package com.tokopedia.media.editor.ui.component

import android.net.Uri
import android.view.ViewGroup
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.data.repository.RotateFilterRepositoryImpl
import com.tokopedia.media.editor.ui.component.slider.MediaEditorSlider
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.yalantis.ucrop.view.UCropView
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

    fun setupView(paramData: EditorDetailUiModel) {
        container().show()

        rotateSlider.setRangeSliderValue(
            0,
            180,
            1
        )

        rotateSlider.listener = this

        val uri = Uri.parse("/storage/emulated/0/Android/data/com.tokopedia.tkpd/cache/editor-cache/20220718_121153.png")
        ucropView.cropImageView.setImageURI(uri)

        flipBtn.setOnClickListener {
            ucropView.cropImageView.scaleX = -ucropView.cropImageView.scaleX
        }

        rotateBtn.setOnClickListener {
            ucropView.cropImageView.postRotate(90f * ucropView.cropImageView.scaleX)
        }
    }

    override fun valueUpdated(step: Int, value: Float) {
        listener.onRotateValueChanged(value * ucropView.cropImageView.scaleX)
    }

    interface Listener {
        fun onRotateValueChanged(value: Float)
    }
}