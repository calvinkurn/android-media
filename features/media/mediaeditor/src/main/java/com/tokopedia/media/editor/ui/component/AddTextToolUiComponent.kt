package com.tokopedia.media.editor.ui.component

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.data.entity.AddTextTemplateMode
import com.tokopedia.media.editor.ui.adapter.addtext.AddTextToolAdapter
import com.tokopedia.media.editor.ui.adapter.addtext.AddTextViewHolder
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel
import com.tokopedia.picker.common.basecomponent.UiComponent

class AddTextToolUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, R.id.uc_tool_add_text) {

    private val mRecycleView = findViewById<RecyclerView>(R.id.add_text_rv)

    fun setupView(data: EditorAddTextUiModel?, isLocalTemplateReady: Boolean) {
        container().show()

        mRecycleView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        mRecycleView.adapter = AddTextToolAdapter(listener, isLocalTemplateReady)

        updateItemActiveState(data)
    }

    fun updateItemActiveState(data: EditorAddTextUiModel?) {
        mRecycleView.post {
            data?.let {
                if (it.textValue.isNotEmpty()) {
                    setSelectedState(it.textTemplate.value)
                }
            }
        }
    }

    private fun setSelectedState(index: Int) {
        val mappedIndex = AddTextTemplateMode.templateToToolId(index).value

        for (i in 0 until mRecycleView.childCount) {
            (mRecycleView.getChildViewHolder(mRecycleView.getChildAt(i)) as AddTextViewHolder).apply {
                if (i == mappedIndex) {
                    setActive()
                } else {
                    setInactive()
                }
            }
        }
    }

    fun updateSaveToApply() {
        (mRecycleView.adapter as AddTextToolAdapter).updateTemplateText(true, needRefresh = true)
    }

    interface Listener {
        fun onChangePosition()
        fun onTemplateSave(isSave: Boolean)
        fun onAddFreeText()
        fun onAddSingleBackgroundText()
    }
}
