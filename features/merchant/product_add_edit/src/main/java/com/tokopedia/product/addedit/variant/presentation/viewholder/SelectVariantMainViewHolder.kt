package com.tokopedia.product.addedit.variant.presentation.viewholder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.product.addedit.common.util.setPrimarySelected
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import kotlinx.android.synthetic.main.item_multiple_variant_edit_select.view.*

class SelectVariantMainViewHolder(itemView: View, val clickListener: OnFieldClickListener)
    : RecyclerView.ViewHolder(itemView) {

    interface OnFieldClickListener {
        fun onFieldClicked(level1Position: Int, level2Position: Int, value: Boolean)
    }

    private var dataList: ArrayList<ListItemUnify> = arrayListOf()
    private var context: Context? = null

    fun bindData(selectedItemMap: MutableList<Boolean>, selections: List<SelectionInputModel>) {
        dataList = mapToListItems(selectedItemMap, selections)
        context = itemView.context
        itemView.listUnifySelection.setData(dataList)
        itemView.listUnifySelection.onLoadFinish {
            itemView.listUnifySelection.setOnItemClickListener { _, _, position, _ ->
                val selectedItem = dataList[position]
                selectedItem.listRightRadiobtn?.performClick()
            }

            dataList.forEachIndexed { position, listItemUnify ->
                val isPositionChecked = selectedItemMap.getOrNull(position) ?: false
                listItemUnify.setPrimarySelected(context, isPositionChecked)

                listItemUnify.listRightRadiobtn?.setOnClickListener {
                    val isChecked = listItemUnify.listRightRadiobtn?.isChecked ?: false
                    clickListener.onFieldClicked(adapterPosition, position, isChecked)
                }
            }
        }
        setupListTitle(selections)
    }

    private fun setupListTitle(selections: List<SelectionInputModel>) {
        if (selections.size > 1) {
            val selection = selections.getOrNull(0)
            selection?.let {
                val title = it.options.getOrNull(adapterPosition)?.value.orEmpty()
                itemView.textSelection.text = title
            }
        } else {
            itemView.textSelection.gone()
        }
    }

    private fun mapToListItems(options: List<Boolean>, selections: List<SelectionInputModel>) = ArrayList(
            options.mapIndexed { index, _ ->
                val selectionLevel1 = selections.getOrNull(0)
                val selectionLevel2 = selections.getOrNull(1)
                var title = ""
                if (selectionLevel1 != null && selectionLevel2 != null) {
                    title = selectionLevel2.options.getOrNull(index)?.value.orEmpty()
                } else if (selectionLevel1 != null) {
                    title = selectionLevel1.options.getOrNull(adapterPosition)?.value.orEmpty()
                }
                createListItem(title)
            }
    )

    private fun createListItem(text: String): ListItemUnify {
        val listItem = ListItemUnify(text, "")
        listItem.setVariant(null, ListItemUnify.RADIO_BUTTON, text)
        listItem.isBold = false
        return listItem
    }

}