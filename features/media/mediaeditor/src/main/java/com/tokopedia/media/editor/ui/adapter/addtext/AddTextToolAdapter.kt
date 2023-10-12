package com.tokopedia.media.editor.ui.adapter.addtext

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.media.editor.ui.component.AddTextToolUiComponent
import com.tokopedia.media.editor.data.entity.AddTextToolId
import com.tokopedia.media.editor.R as editorR

class AddTextToolAdapter(
    private val listener: AddTextToolUiComponent.Listener,
    private var isLocalTemplateReady: Boolean
): RecyclerView.Adapter<AddTextViewHolder>() {
    // icon ref will be replace by unify icon later
    private val mAddTextMenu = MutableList<AddTextAction?>(0) { null }

    init {
        initToolList()
    }

    override fun getItemCount(): Int {
        return mAddTextMenu.size
    }

    override fun onBindViewHolder(holder: AddTextViewHolder, position: Int) {
        updateTemplateText()

        mAddTextMenu[position]?.let {
            holder.bind(it){
                when (AddTextToolId.getToolIdByIndex(position)) {
                    AddTextToolId.CHANGE_POSITION_INDEX -> listener.onChangePosition()
                    AddTextToolId.SAVE_TEMPLATE_INDEX -> listener.onTemplateSave(!isLocalTemplateReady)
                    AddTextToolId.FREE_TEXT_INDEX -> listener.onAddFreeText()
                    AddTextToolId.BACKGROUND_TEXT_INDEX -> listener.onAddSingleBackgroundText()
                    else -> {}
                }
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
        return if (mAddTextMenu[position]?.isDivider == true) {
            TYPE_DIVIDER
        } else {
            TYPE_ITEM
        }
    }

    // change text between "Simpan Template" & "Gunakan Template"
    fun updateTemplateText(isTemplateReady: Boolean = isLocalTemplateReady, needRefresh: Boolean = false) {
        if (isTemplateReady){
            mAddTextMenu.find {
                it?.textRef == editorR.string.add_text_save_template
            }?.apply {
                this.textRef = editorR.string.add_text_apply_template
                if (needRefresh) notifyItemChanged(mAddTextMenu.indexOf(this))
                isLocalTemplateReady = true
            }
        }
    }

    private fun initToolList() {
        mAddTextMenu.add(
            AddTextToolId.CHANGE_POSITION_INDEX.value,
            AddTextAction(
                editorR.string.add_text_change_position,
                iconRef = editorR.drawable.editor_icon_expand
            )
        )

        mAddTextMenu.add(
            AddTextToolId.SAVE_TEMPLATE_INDEX.value,
            AddTextAction(
                editorR.string.add_text_save_template, IconUnify.TEMPLATE)
        )

        mAddTextMenu.add(
            AddTextToolId.DIVIDER.value,
            AddTextAction(0, 0, true)
        )

        mAddTextMenu.add(
            AddTextToolId.FREE_TEXT_INDEX.value,
            AddTextAction(editorR.string.add_text_free_text, IconUnify.TEXT)
        )

        mAddTextMenu.add(
            AddTextToolId.BACKGROUND_TEXT_INDEX.value,
            AddTextAction(
                editorR.string.add_text_single_background_text,
                iconRef = editorR.drawable.editor_icon_text_background,
                isIconFull = true
            )
        )
    }

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_DIVIDER = 1
    }
}
