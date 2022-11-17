package com.tokopedia.media.editor.ui.component

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel.Companion.REMOVE_BG_TYPE_DEFAULT
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel.Companion.REMOVE_BG_TYPE_GRAY
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel.Companion.REMOVE_BG_TYPE_WHITE
import com.tokopedia.picker.common.basecomponent.UiComponent


class RemoveBackgroundToolUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, R.id.uc_tool_removebg) {

    private val btnRemoveBgOriginal: AppCompatImageView = findViewById(R.id.remove_bg_ori_btn)
    private val btnRemoveBgWhite: AppCompatImageView = findViewById(R.id.remove_bg_white_btn)
    private val btnRemoveBgGray: AppCompatImageView = findViewById(R.id.remove_bg_gray_btn)

    private val backgroundDrawable =
        ContextCompat.getDrawable(context, R.drawable.editor_rect_green_selected_thumbnail)

    init {
        btnRemoveBgOriginal.setOnClickListener {
            listener.onRemoveBackgroundClicked(REMOVE_BG_TYPE_DEFAULT)
            setButtonActive(REMOVE_BG_TYPE_DEFAULT)
        }

        btnRemoveBgWhite.setOnClickListener {
            listener.onRemoveBackgroundClicked(REMOVE_BG_TYPE_WHITE)
            setButtonActive(REMOVE_BG_TYPE_WHITE)
        }

        btnRemoveBgGray.setOnClickListener {
            listener.onRemoveBackgroundClicked(REMOVE_BG_TYPE_GRAY)
            setButtonActive(REMOVE_BG_TYPE_GRAY)
        }

        setButtonActive(REMOVE_BG_TYPE_DEFAULT)
    }

    fun setupView() {
        container().show()
    }

    private fun setButtonActive(buttonIndex: Int) {
        when (buttonIndex) {
            REMOVE_BG_TYPE_DEFAULT -> {
                btnRemoveBgOriginal.background = backgroundDrawable
                btnRemoveBgWhite.background = null
                btnRemoveBgGray.background = null
            }
            REMOVE_BG_TYPE_WHITE -> {
                btnRemoveBgWhite.background = backgroundDrawable
                btnRemoveBgOriginal.background = null
                btnRemoveBgGray.background = null
            }
            REMOVE_BG_TYPE_GRAY -> {
                btnRemoveBgGray.background = backgroundDrawable
                btnRemoveBgOriginal.background = null
                btnRemoveBgWhite.background = null
            }
        }
    }

    interface Listener {
        fun onRemoveBackgroundClicked(removeBgType: Int)
    }
}