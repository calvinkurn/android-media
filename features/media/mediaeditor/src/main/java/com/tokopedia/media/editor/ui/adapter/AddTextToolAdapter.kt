package com.tokopedia.media.editor.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.media.editor.ui.component.AddTextToolUiComponent
import com.tokopedia.media.editor.R as editorR
import com.tokopedia.media.editor.ui.widget.ToolSelectionItem
import com.tokopedia.unifycomponents.toPx

class AddTextToolAdapter(
    private val listener: AddTextToolUiComponent.Listener,
    private val isLocalTemplateReady: Boolean
): RecyclerView.Adapter<AddTextViewHolder>() {
    private var selectedIndex = FREE_TEXT_INDEX

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

class AddTextViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    private var selectionToolItem: ToolSelectionItem? = null

    fun bind(addTextData: AddTextAction, listener: () -> Unit) {
        try {
            (view as ToolSelectionItem).apply {
                selectionToolItem = this
                setTextTitle(addTextData.textRef)
                addTextData.iconId?.let {
                    setIcon(it)
                } ?: kotlin.run {
                    setImage(
                        addTextData.iconRef,
                        isFull = addTextData.isIconFull
                    )
                }
                setListener(listener)
            }
        } catch (_: Exception) {}
    }

    fun setActive() {
        selectionToolItem?.setActive()
    }

    fun setInactive() {
        selectionToolItem?.setInactive()
    }

    companion object {
        private const val DIVIDER_SIZE = 1
        private const val DIVIDER_HEIGHT = 64
        private const val DIVIDER_GAP = 18

        @SuppressLint("ResourcePackage")
        fun create(viewGroup: ViewGroup): AddTextViewHolder {
            return AddTextViewHolder(
                ToolSelectionItem(viewGroup.context)
            )
        }

        fun createDivider(viewGroup: ViewGroup): AddTextViewHolder {
            val view = View(viewGroup.context).apply {
                val lp = LinearLayout.LayoutParams(DIVIDER_SIZE.toPx(), DIVIDER_HEIGHT.toPx())
                lp.marginEnd = DIVIDER_GAP.toPx()
                layoutParams = lp

                setBackgroundColor(
                    ContextCompat.getColor(viewGroup.context, editorR.color.Unify_NN200)
                )
            }

            return AddTextViewHolder(view)
        }
    }
}

data class AddTextAction(
    var textRef: Int,
    val iconId: Int? = null,
    val isDivider: Boolean = false,
    val iconRef: Int = 0,
    val isIconFull: Boolean = false
)
