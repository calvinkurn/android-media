package com.tokopedia.product.addedit.variant.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.list.ListItemUnify
import kotlinx.android.synthetic.main.item_multiple_variant_edit_select.view.*

class MultipleVariantEditSelectViewHolder(itemView: View, clickListener: OnFieldClickListener)
    : RecyclerView.ViewHolder(itemView) {

    interface OnFieldClickListener {
        fun onFieldClicked(position: Int)
    }

    fun bindData(text: String) {
        itemView.textSelection.text = text
        val dataList = arrayListOf(
                createListItem("S"),
                createListItem("M"),
                createListItem("L")
        )
        itemView.listUnifySelection.setData(dataList)
    }

    private fun createListItem(text: String): ListItemUnify {
        val listItem = ListItemUnify(text, "")
        listItem.setVariant(null, ListItemUnify.CHECKBOX, text)
        return listItem
    }

}