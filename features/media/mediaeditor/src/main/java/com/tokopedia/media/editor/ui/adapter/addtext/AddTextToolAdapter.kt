package com.tokopedia.media.editor.ui.adapter.addtext

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.media.editor.ui.component.AddTextToolUiComponent
import com.tokopedia.media.editor.R as editorR

class AddTextToolAdapter(
    private val listener: AddTextToolUiComponent.Listener,
    private var isLocalTemplateReady: Boolean
): RecyclerView.Adapter<AddTextViewHolder>() {
    // icon ref will be replace by unify icon later
    private val mAddTextMenu = listOf(
        AddTextAction(editorR.string.add_text_change_position, iconRef = editorR.drawable.editor_icon_expand),
        AddTextAction(editorR.string.add_text_save_template, iconRef = editorR.drawable.editor_icon_template),
        AddTextAction(0, 0, true),
        AddTextAction(editorR.string.add_text_free_text, IconUnify.TEXT),
        AddTextAction(editorR.string.add_text_single_background_text,
            iconRef = editorR.drawable.editor_icon_text_background,
            isIconFull = true
        )
    )

    override fun getItemCount(): Int {
        return mAddTextMenu.size
    }

    override fun onBindViewHolder(holder: AddTextViewHolder, position: Int) {
        updateTemplateText()

        holder.bind(mAddTextMenu[position]){
            when (position) {
                CHANGE_POSITION_INDEX -> listener.onChangePosition()
                SAVE_TEMPLATE_INDEX -> listener.onTemplateSave(!isLocalTemplateReady)
                FREE_TEXT_INDEX -> listener.onAddFreeText()
                BACKGROUND_TEXT_INDEX -> listener.onAddSingleBackgroundText()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddTextViewHolder {
        return if (viewType == TYPE_ITEM) {
            AddTextViewHolder.create(parent)
        } else {
            AddTextViewHolder.createDivider(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mAddTextMenu[position].isDivider) {
            TYPE_DIVIDER
        } else {
            TYPE_ITEM
        }
    }

    // change text between "Simpan Template" & "Gunakan Template"
    fun updateTemplateText(isTemplateReady: Boolean = isLocalTemplateReady, needRefresh: Boolean = false) {
        if (isTemplateReady){
            mAddTextMenu.find {
                it.textRef == editorR.string.add_text_save_template
            }?.apply {
                this.textRef = editorR.string.add_text_apply_template
                if (needRefresh) notifyItemChanged(mAddTextMenu.indexOf(this))
                isLocalTemplateReady = true
            }
        }
    }

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_DIVIDER = 1

        // index is refer to mAddTextMenu
        // if edit free & background text please check AddTextUiModel
        private const val CHANGE_POSITION_INDEX = 0
        private const val SAVE_TEMPLATE_INDEX = 1
        const val FREE_TEXT_INDEX = 3
        const val BACKGROUND_TEXT_INDEX = 4
    }
}
