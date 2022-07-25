package com.tokopedia.media.editor.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.ToolUiModel
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifyprinciples.Typography

class EditorToolAdapter constructor(
    private val tools: MutableList<ToolUiModel> = mutableListOf(),
    private val listener: EditorToolViewHolder.Listener
) : RecyclerView.Adapter<EditorToolViewHolder>() {

    var stateList: List<EditorDetailUiModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditorToolViewHolder {
        return EditorToolViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: EditorToolViewHolder, position: Int) {
        val toolModel = tools[position]
        var isActive = false

        stateList?.forEach {
            isActive = when(toolModel.id){
                EditorToolType.BRIGHTNESS -> it.brightnessValue != null
                EditorToolType.CONTRAST -> it.contrastValue != null
                EditorToolType.ROTATE -> it.rotateData != null
                EditorToolType.WATERMARK -> it.watermarkMode != null
                EditorToolType.REMOVE_BACKGROUND -> it.removeBackgroundUrl != null
                EditorToolType.CROP -> it.cropBound != null
                else -> false
            }

            // if found related edit state then stop loop, only need 1 state
            if(isActive) return@forEach
        }

        holder.bind(toolModel, isActive)
    }

    override fun getItemCount() = tools.size

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(items: List<ToolUiModel>) {
        tools.clear()
        tools.addAll(items)
        notifyDataSetChanged()
    }

}

class EditorToolViewHolder(
    view: View,
    private val listener: Listener
) : RecyclerView.ViewHolder(view) {

    private val icTool: IconUnify = view.findViewById(R.id.ic_tool)
    private val txtName: Typography = view.findViewById(R.id.txt_name)
    private val toolNotification: NotificationUnify = view.findViewById(R.id.ic_tool_notification)

    private val context = view.context

    fun bind(tool: ToolUiModel, isActive: Boolean = false) {
        txtName.text = context.getString(tool.name)
        icTool.setImage(tool.icon)

        itemView.setOnClickListener {
            listener.onItemClicked(tool.id)
        }

        toolNotification.showWithCondition(isActive)
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