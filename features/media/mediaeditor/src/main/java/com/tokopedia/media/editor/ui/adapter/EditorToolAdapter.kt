package com.tokopedia.media.editor.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visibleWithCondition
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.media.editor.ui.uimodel.ToolUiModel
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifyprinciples.Typography

class EditorToolAdapter constructor(
    private val tools: MutableList<ToolUiModel> = mutableListOf(),
    private val listener: EditorToolViewHolder.Listener,
    private var newLabelShow: Int
) : RecyclerView.Adapter<EditorToolViewHolder>() {

    private var stateList: List<EditorDetailUiModel>? = null
    private var isAutoCropped: Boolean = false

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(items: List<ToolUiModel>) {
        tools.clear()
        tools.addAll(items)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setupActiveTools(editorUiModel: EditorUiModel) {
        stateList = editorUiModel.getFilteredStateList().toMutableList()
        isAutoCropped = editorUiModel.isAutoCropped
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditorToolViewHolder {
        return EditorToolViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: EditorToolViewHolder, position: Int) {
        val toolModel = tools[position]
        var isActive = false

        stateList?.let {
            it.forEachIndexed { index, editorDetailUiModel ->
                isActive = when (toolModel.id) {
                    EditorToolType.BRIGHTNESS -> editorDetailUiModel.brightnessValue != null
                    EditorToolType.CONTRAST -> editorDetailUiModel.contrastValue != null
                    EditorToolType.ROTATE -> editorDetailUiModel.cropRotateValue.isRotate
                    EditorToolType.WATERMARK -> editorDetailUiModel.watermarkMode != null
                    EditorToolType.REMOVE_BACKGROUND -> editorDetailUiModel.removeBackgroundUrl != null
                    EditorToolType.ADD_LOGO -> editorDetailUiModel.addLogoValue.overlayLogoUrl.isNotEmpty()
                    EditorToolType.CROP -> {
                        if (isAutoCropped && index == 0 && (editorDetailUiModel.originalRatio != editorDetailUiModel.cropRotateValue.getRatio()))
                            false
                        else
                            editorDetailUiModel.cropRotateValue.isCrop
                    }
                    EditorToolType.ADD_TEXT -> editorDetailUiModel.addTextValue?.textImagePath?.isNotEmpty() == true
                    else -> false
                }

                // if found related edit state then stop loop, only need 1 state
                if (isActive) return@let
            }
        }

        holder.bind(
            toolModel,
            isActive,
            toolModel.id == newLabelShow,
            newLabelShow > EMPTY_LABEL_STATE
        )
    }

    override fun getItemCount() = tools.size

    companion object {
        const val EMPTY_LABEL_STATE = -1
    }
}

class EditorToolViewHolder(
    view: View,
    private val listener: Listener
) : RecyclerView.ViewHolder(view) {

    private val icTool: IconUnify = view.findViewById(R.id.ic_tool)
    private val txtName: Typography = view.findViewById(R.id.txt_name)
    private val toolNotification: NotificationUnify = view.findViewById(R.id.ic_tool_notification)
    private val newLabel: Label = view.findViewById(R.id.new_label)

    private val context = view.context

    fun bind(tool: ToolUiModel, isActive: Boolean = false, isShowNewLabel: Boolean = false, isLabeledSize: Boolean) {
        txtName.text = context.getString(tool.name)
        icTool.setImage(tool.icon)

        itemView.setOnClickListener {
            listener.onItemClicked(tool.id)
        }

        toolNotification.showWithCondition(isActive)

        if (isLabeledSize) {
            newLabel.visibleWithCondition(isShowNewLabel)
        } else {
            newLabel.showWithCondition(isShowNewLabel)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.partial_editor_tool_item

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
