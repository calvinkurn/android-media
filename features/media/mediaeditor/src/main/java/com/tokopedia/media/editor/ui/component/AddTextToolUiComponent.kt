package com.tokopedia.media.editor.ui.component

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.adapter.AddTextToolAdapter
import com.tokopedia.media.editor.ui.adapter.AddTextViewHolder
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel
import com.tokopedia.picker.common.basecomponent.UiComponent

class AddTextToolUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, R.id.uc_tool_add_text) {

    private val mRv = findViewById<RecyclerView>(R.id.add_text_rv)

    fun setupView(data: EditorAddTextUiModel?) {
        container().show()

        mRv.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        mRv.adapter = AddTextToolAdapter(listener)

        updateItemActiveState(data)
    }

    fun updateItemActiveState(data: EditorAddTextUiModel?) {
        mRv.post {
            data?.let {
                if (it.textValue.isNotEmpty()) {
                    setSelectedState(it.textTemplate)
                }
            }
        }
    }

    private fun setSelectedState(index: Int) {
        for (i in 0 until mRv.childCount) {
            (mRv.getChildViewHolder(mRv.getChildAt(i)) as AddTextViewHolder).apply {
                if (i == index) {
                    setActive()
                } else {
                    setInactive()
                }
            }
        }
    }

    interface Listener {
        fun onChangePosition()
        fun onTemplateSave()
        fun onAddFreeText()
        fun onAddSingleBackgroundText()
    }
}
