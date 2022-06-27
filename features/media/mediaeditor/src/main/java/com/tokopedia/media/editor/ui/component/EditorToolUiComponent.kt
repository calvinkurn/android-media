package com.tokopedia.media.editor.ui.component

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.adapter.EditorToolAdapter
import com.tokopedia.media.editor.ui.uimodel.ToolUiModel
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.picker.common.types.EditorToolType

class EditorToolUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, R.id.uc_editor_tool_container)
    , EditorToolAdapter.EditorToolViewHolder.Listener {

    private val lstTool: RecyclerView = findViewById(R.id.lst_tool)

    private val adapter by lazy {
        EditorToolAdapter(ToolUiModel.create(), this)
    }

    fun setupView() {
        lstTool.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        lstTool.adapter = adapter
    }

    override fun onItemClicked(type: Int) {
        listener.onEditorToolClicked(type)
    }

    interface Listener {
        fun onEditorToolClicked(@EditorToolType type: Int)
    }

}