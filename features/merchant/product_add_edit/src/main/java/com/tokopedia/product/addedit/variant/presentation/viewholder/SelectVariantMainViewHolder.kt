package com.tokopedia.product.addedit.variant.presentation.viewholder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.common.util.setPrimarySelected
import com.tokopedia.product.addedit.variant.presentation.model.OptionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import kotlinx.android.synthetic.main.item_multiple_variant_edit_select.view.*

class SelectVariantMainViewHolder(itemView: View, val clickListener: OnFieldClickListener)
    : RecyclerView.ViewHolder(itemView) {

    interface OnFieldClickListener {
        fun onFieldClicked(selectionPosition: Int, optionPosition: Int, value: Boolean)
    }

    private var dataList: ArrayList<ListItemUnify> = arrayListOf()
    private var context: Context? = null

    fun bindData(selectionInputModel: SelectionInputModel, selectedItemMap: MutableList<Boolean>) {
        dataList = mapOptionsToListItems(selectionInputModel.options)
        context = itemView.context
        itemView.textSelection.text = selectionInputModel.variantName
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
    }

    private fun mapOptionsToListItems(options: List<OptionInputModel>) = ArrayList(
            options.map {
                createListItem(it.value)
            })

    private fun createListItem(text: String): ListItemUnify {
        val listItem = ListItemUnify(text, "")
        listItem.setVariant(null, ListItemUnify.RADIO_BUTTON, text)
        listItem.isBold = false
        return listItem
    }

}