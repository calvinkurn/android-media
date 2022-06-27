package com.tokopedia.media.editor.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.uimodel.ToolUiModel
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.unifyprinciples.Typography

class EditorToolAdapter constructor(
    private val tools: List<ToolUiModel> = mutableListOf(),
    private val listener: EditorToolViewHolder.Listener
) : RecyclerView.Adapter<EditorToolViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditorToolViewHolder {
        return EditorToolViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: EditorToolViewHolder, position: Int) {
        holder.bind(tools[position])
    }

    override fun getItemCount() = tools.size

}

class EditorToolViewHolder(
    view: View,
    private val listener: Listener
) : RecyclerView.ViewHolder(view) {

    private val icTool: IconUnify = view.findViewById(R.id.ic_tool)
    private val txtName: Typography = view.findViewById(R.id.txt_name)

    private val context = view.context

    fun bind(tool: ToolUiModel) {
        txtName.text = context.getString(tool.name)
        icTool.setImage(tool.icon)

        itemView.setOnClickListener {
            listener.onItemClicked(tool.id)
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.partial_editor_tool_item

        fun create(viewGroup: ViewGroup, listener: Listener): EditorToolViewHolder {
            val layout = LayoutInflater
                .from(viewGroup.context)
                .inflate(LAYOUT, viewGroup, false)
            return EditorToolViewHolder(layout, listener)
        }
    }

    interface Listener {
        fun onItemClicked(@EditorToolType type: Int)
    }

}