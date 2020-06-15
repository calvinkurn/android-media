package com.tokopedia.product.addedit.variant.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.variant.presentation.model.OptionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import kotlinx.android.synthetic.main.item_multiple_variant_edit_select.view.*

class MultipleVariantEditSelectViewHolder(itemView: View, val clickListener: OnFieldClickListener)
    : RecyclerView.ViewHolder(itemView) {

    interface OnFieldClickListener {
        fun onFieldClicked(selectionPosition: Int, optionPosition: Int, value: Boolean)
    }

    private var dataList: ArrayList<ListItemUnify> = arrayListOf()

    fun bindData(selectionInputModel: SelectionInputModel, selectedItemMap: HashMap<Int, Boolean>) {
        dataList = mapOptionsToListItems(selectionInputModel.options)
        itemView.textSelection.text = selectionInputModel.variantName
        itemView.listUnifySelection.setData(dataList)
        itemView.listUnifySelection.onLoadFinish {
            itemView.listUnifySelection.setOnItemClickListener { _, _, position, _ ->
                val selectedItem = dataList[position]
                selectedItem.listRightCheckbox?.performClick()
            }

            dataList.forEachIndexed { position, listItemUnify ->
                listItemUnify.listRightCheckbox?.isChecked = selectedItemMap[position] ?: false
                listItemUnify.listRightCheckbox?.setOnClickListener {
                    val isChecked = listItemUnify.listRightCheckbox?.isChecked ?: false
                    clickListener.onFieldClicked(adapterPosition, position, isChecked)
                }
            }
        }
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