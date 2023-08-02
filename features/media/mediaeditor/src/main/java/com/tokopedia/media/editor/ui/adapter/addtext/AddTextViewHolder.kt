package com.tokopedia.media.editor.ui.adapter.addtext

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.editor.ui.widget.ToolSelectionItemView
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.R as principleR

class AddTextViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    private var selectionToolItem: ToolSelectionItemView? = null

    fun bind(addTextData: AddTextAction, listener: () -> Unit) {
        try {
            (view as ToolSelectionItemView).apply {
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
                ToolSelectionItemView(viewGroup.context)
            )
        }

        fun createDivider(viewGroup: ViewGroup): AddTextViewHolder {
            val view = View(viewGroup.context).apply {
                val lp = LinearLayout.LayoutParams(DIVIDER_SIZE.toPx(), DIVIDER_HEIGHT.toPx())
                lp.marginEnd = DIVIDER_GAP.toPx()
                layoutParams = lp

                setBackgroundColor(
                    ContextCompat.getColor(viewGroup.context, principleR.color.Unify_NN200)
                )
            }

            return AddTextViewHolder(view)
        }
    }
}
