package com.tokopedia.media.editor.ui.component

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.adapter.EditorToolAdapter
import com.tokopedia.media.editor.ui.adapter.EditorToolViewHolder
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.media.editor.ui.uimodel.ToolUiModel.Companion.create
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.unifycomponents.toPx

class ToolsUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, R.id.uc_editor_tool_container), EditorToolViewHolder.Listener {

    private val lstTool: RecyclerView = findViewById(R.id.lst_tool)

    // set to EditorToolAdapter.EMPTY_LABEL_STATE to clear label "Baru"
    private val newLabelShow = EditorToolType.ADD_TEXT

    private val adapter by lazy {
        EditorToolAdapter(
            listener = this,
            newLabelShow = newLabelShow
        )
    }

    fun setupView(tools: List<Int>) {
        lstTool.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        lstTool.adapter = adapter

        adapter.addItem(tools.create())

        if (newLabelShow != EditorToolAdapter.EMPTY_LABEL_STATE) {
            findViewById<RelativeLayout>(R.id.uc_editor_tool_container).apply {
                updatePadding(bottom = TOOL_LABEL_PADDING.toPx())
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setupActiveTools(editorUiModel: EditorUiModel) {
        adapter.setupActiveTools(editorUiModel)
    }

    override fun onItemClicked(type: Int) {
        listener.onEditorToolClicked(type)
    }

    interface Listener {
        fun onEditorToolClicked(@EditorToolType type: Int)
    }

    companion object {
        private const val TOOL_LABEL_PADDING = 10
    }
}
