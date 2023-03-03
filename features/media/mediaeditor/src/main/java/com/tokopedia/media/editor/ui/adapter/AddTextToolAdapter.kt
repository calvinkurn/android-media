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
    private val asd: AddTextToolUiComponent.Listener
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
        holder.bind(mAddTextMenu[position]){
            when (position) {
                CHANGE_POSITION_INDEX -> asd.onChangePosition()
                SAVE_TEMPLATE_INDEX -> asd.onTemplateSave()
                FREE_TEXT_INDEX -> asd.onAddFreeText()
                BACKGROUND_TEXT_INDEX -> asd.onAddSingleBackgroundText()
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

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_DIVIDER = 1

        private const val CHANGE_POSITION_INDEX = 0
        private const val SAVE_TEMPLATE_INDEX = 1
        private const val FREE_TEXT_INDEX = 3
        private const val BACKGROUND_TEXT_INDEX = 4
    }
}

class AddTextViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    fun bind(addTextData: AddTextAction, listener: () -> Unit) {
        try {
            (view as ToolSelectionItem).apply {
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
        } catch (e: Exception) {}
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
    val textRef: Int,
    val iconId: Int? = null,
    val isDivider: Boolean = false,
    val iconRef: Int = 0,
    val isIconFull: Boolean = false
)
