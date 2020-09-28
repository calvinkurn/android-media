package com.tokopedia.product.addedit.variant.presentation.viewholder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.product.addedit.common.util.setPrimarySelected
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_COUNT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_COUNT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_POSITION
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import kotlinx.android.synthetic.main.item_multiple_variant_edit_select.view.*

class SelectVariantMainViewHolder(itemView: View, val clickListener: OnFieldClickListener)
    : RecyclerView.ViewHolder(itemView) {

    interface OnFieldClickListener {
        fun onFieldClicked(level1Position: Int, level2Position: Int, value: Boolean)
    }

    private var levelCount = 0
    private var dataList: ArrayList<ListItemUnify> = arrayListOf()
    private var context: Context? = null

    fun bindData(selectedItemMap: MutableList<Boolean>, selections: List<SelectionInputModel>) {
        levelCount = selections.size
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
        when (levelCount) {
            VARIANT_VALUE_LEVEL_ONE_COUNT -> {
                itemView.textSelection.gone()
            }
            VARIANT_VALUE_LEVEL_TWO_COUNT -> {
                selections.getOrNull(VARIANT_VALUE_LEVEL_ONE_POSITION)?.let {
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
            VARIANT_VALUE_LEVEL_ONE_COUNT -> {
                selections.getOrNull(VARIANT_VALUE_LEVEL_ONE_POSITION)?.let {
                    title = it.options.getOrNull(adapterPosition)?.value.orEmpty()
                }
            }
            VARIANT_VALUE_LEVEL_TWO_COUNT -> {
                selections.getOrNull(VARIANT_VALUE_LEVEL_TWO_POSITION)?.let {
                    title = it.options.getOrNull(index)?.value.orEmpty()
                }
            }
        }
        createListItem(title)
    })

    private fun createListItem(text: String): ListItemUnify {
        val listItem = ListItemUnify(text, "")
        listItem.setVariant(null, ListItemUnify.RADIO_BUTTON, text)
        listItem.isBold = false
        return listItem
    }

}