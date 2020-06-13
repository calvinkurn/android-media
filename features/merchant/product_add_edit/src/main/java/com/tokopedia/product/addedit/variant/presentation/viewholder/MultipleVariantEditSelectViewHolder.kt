package com.tokopedia.product.addedit.variant.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.variant.presentation.model.OptionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import kotlinx.android.synthetic.main.item_multiple_variant_edit_select.view.*

class MultipleVariantEditSelectViewHolder(itemView: View, clickListener: OnFieldClickListener)
    : RecyclerView.ViewHolder(itemView) {

    interface OnFieldClickListener {
        fun onFieldClicked(position: Int)
    }

    fun bindData(selectionInputModel: SelectionInputModel) {
        itemView.textSelection.text = selectionInputModel.variantName
        val dataList = mapOptionsToListItems(selectionInputModel.options)

        itemView.listUnifySelection.setData(dataList)
    }

    private fun mapOptionsToListItems(options: List<OptionInputModel>) = ArrayList(
            options.map {
                createListItem(it.value)
            })

    private fun createListItem(text: String): ListItemUnify {
        val listItem = ListItemUnify(text, "")
        listItem.setVariant(null, ListItemUnify.CHECKBOX, text)
        listItem.isBold = false
        return listItem
    }

}