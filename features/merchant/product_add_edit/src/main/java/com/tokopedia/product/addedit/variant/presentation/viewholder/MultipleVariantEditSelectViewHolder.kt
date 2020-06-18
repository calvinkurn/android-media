package com.tokopedia.product.addedit.variant.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import kotlinx.android.synthetic.main.item_multiple_variant_edit_select.view.*

class MultipleVariantEditSelectViewHolder(itemView: View, val clickListener: OnFieldClickListener)
    : RecyclerView.ViewHolder(itemView) {

    interface OnFieldClickListener {
        fun onFieldClicked(selectionPosition: Int, optionPosition: Int, value: Boolean)
    }

    private var levelCount = 0
    private var dataList: ArrayList<ListItemUnify> = arrayListOf()

    fun bindData(selectedItem: List<Boolean>, selections: List<SelectionInputModel>) {
        levelCount = selections.size
        dataList = mapToListItems(selectedItem, selections)
        itemView.listUnifySelection.setData(dataList)
        itemView.listUnifySelection.onLoadFinish {
            itemView.listUnifySelection.setOnItemClickListener { _, _, position, _ ->
                val checkedItem = dataList[position]
                checkedItem.listRightCheckbox?.performClick()
            }

            dataList.forEachIndexed { position, listItemUnify ->
                listItemUnify.listRightCheckbox?.isChecked = selectedItem[position] ?: false
                listItemUnify.listRightCheckbox?.setOnClickListener {
                    val isChecked = listItemUnify.listRightCheckbox?.isChecked ?: false
                    clickListener.onFieldClicked(adapterPosition, position, isChecked)
                }
            }
        }
        setupListTitle(selections)
    }

    private fun setupListTitle(selections: List<SelectionInputModel>) {
        when (levelCount) {
            1 -> {
                itemView.textSelection.gone()
            }
            2 -> {
                selections.getOrNull(0)?.let {
                    val title = it.options.getOrNull(adapterPosition)?.value.orEmpty()
                    itemView.textSelection.text = title
                }
            }
        }
    }

    private fun mapToListItems(
            selectedItem: List<Boolean>,
            selections: List<SelectionInputModel>
    ) = ArrayList(selectedItem.mapIndexed { index, _ ->
        var title = ""
        when (levelCount) {
            1 -> {
                selections.getOrNull(0)?.let {
                    title = it.options.getOrNull(adapterPosition)?.value.orEmpty()
                }
            }
            2 -> {
                selections.getOrNull(1)?.let {
                    title = it.options.getOrNull(index)?.value.orEmpty()
                }
            }
        }
        createListItem(title)
    })

    private fun createListItem(text: String): ListItemUnify {
        val listItem = ListItemUnify(text, "")
        listItem.setVariant(null, ListItemUnify.CHECKBOX, text)
        listItem.isBold = false
        return listItem
    }

}