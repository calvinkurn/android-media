package com.tokopedia.product.addedit.variant.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_COUNT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_COUNT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_POSITION
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import com.tokopedia.unifyprinciples.Typography

class MultipleVariantEditSelectViewHolder(itemView: View, val clickListener: OnFieldClickListener)
    : RecyclerView.ViewHolder(itemView) {

    interface OnFieldClickListener {
        fun onFieldClicked(selectionPosition: Int, optionPosition: Int, value: Boolean)
    }

    private var levelCount = 0
    private var dataList: ArrayList<ListItemUnify> = arrayListOf()
    private val textSelection: Typography? = itemView.findViewById(R.id.textSelection)
    private val listUnifySelection: ListUnify? = itemView.findViewById(R.id.listUnifySelection)

    fun bindData(selectedItem: List<Boolean>, selections: List<SelectionInputModel>) {
        levelCount = selections.size
        dataList = mapToListItems(selectedItem, selections)
        listUnifySelection?.setData(dataList)
        listUnifySelection?.onLoadFinish {
            listUnifySelection.setOnItemClickListener { _, _, position, _ ->
                val checkedItem = dataList[position]
                checkedItem.listRightCheckbox?.performClick()
            }

            dataList.forEachIndexed { position, listItemUnify ->
                listItemUnify.listRightCheckbox?.isChecked = selectedItem.getOrNull(position) ?: false
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
            VARIANT_VALUE_LEVEL_ONE_COUNT -> {
                textSelection?.gone()
            }
            VARIANT_VALUE_LEVEL_TWO_COUNT -> {
                selections.getOrNull(VARIANT_VALUE_LEVEL_ONE_POSITION)?.let {
                    val title = it.options.getOrNull(adapterPosition)?.value.orEmpty()
                    textSelection?.text = title
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
        listItem.setVariant(null, ListItemUnify.CHECKBOX, text)
        listItem.isBold = false
        return listItem
    }

}