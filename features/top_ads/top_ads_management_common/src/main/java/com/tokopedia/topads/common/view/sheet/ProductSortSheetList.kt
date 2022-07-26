package com.tokopedia.topads.common.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.CONST_0
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.CONST_1
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.CONST_2
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.CONST_3
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import java.util.*

class ProductSortSheetList : BottomSheetUnify() {

    var onItemClick: ((sortId: String) -> Unit)? = null
    private var selectedSortText = 0
    private var positionSort = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val contentView = View.inflate(context,
            R.layout.topads_edit_select_fragment_product_list_sheet_sort,
            null)
        setChild(contentView)
        setTitle(getString(R.string.topads_edit_urutkan))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sortListItems: Array<String> = resources.getStringArray(R.array.sort_product_list_array)

        val sortListItemUnify = mapToItemUnifyList(sortListItems)

        view.findViewById<ListUnify>(R.id.sortList)?.setData(sortListItemUnify)

        view.findViewById<ListUnify>(R.id.sortList)?.let {
            it.onLoadFinish {

                it.setSelectedFilterOrSort(sortListItemUnify, positionSort.orZero())

                it.setOnItemClickListener { _, _, index, _ ->
                    onItemSortClickedBottomSheet(index, sortListItemUnify, it)
                }
                sortListItemUnify.forEachIndexed { index, listItemUnify ->
                    listItemUnify.listRightRadiobtn?.setOnClickListener { _ ->
                        onItemSortClickedBottomSheet(index, sortListItemUnify, it)
                    }
                }
            }
        }
    }


    private fun onItemSortClickedBottomSheet(
        position: Int, sortListItemUnify: ArrayList<ListItemUnify>, sortListUnify: ListUnify,
    ) {
        try {
            positionSort = position
            sortListUnify.setSelectedFilterOrSort(sortListItemUnify, position)
            selectedSortText = position
            sortListUnify.setSelectedFilterOrSort(sortListItemUnify, position)
            onItemClick?.invoke(sortListItemUnify[position].listTitleText)
            dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getSelectedSortId(): String {
        return when (selectedSortText) {
            CONST_1 -> TERBARU
            CONST_3 -> TERENDAH
            CONST_0 -> TERLARIS
            CONST_2 -> TERTINGGI
            else -> TERBARU
        }
    }


    private fun ListUnify.setSelectedFilterOrSort(items: List<ListItemUnify>, position: Int) {
        val clickedItem = this.getItemAtPosition(position) as ListItemUnify
        when (choiceMode) {
            ListView.CHOICE_MODE_SINGLE -> {
                items.filter {
                    it.listRightRadiobtn?.isChecked ?: false
                }.filterNot { it == clickedItem }
                    .onEach { it.listRightRadiobtn?.isChecked = false }

                clickedItem.listRightRadiobtn?.isChecked = true
            }
        }
    }

    private fun mapToItemUnifyList(list: Array<String>): ArrayList<ListItemUnify> {
        val itemUnifyList: ArrayList<ListItemUnify> = arrayListOf()
        list.map {
            val data = ListItemUnify(title = it, description = "")
            data.setVariant(rightComponent = ListItemUnify.RADIO_BUTTON)
            itemUnifyList.add(data)
        }
        return itemUnifyList
    }

    companion object {
        const val TERBARU = "newest"
        const val TERENDAH = "cheapest"
        const val TERLARIS = "most_sales"
        const val TERTINGGI = "most_expensive"

        fun newInstance(): ProductSortSheetList {
            return ProductSortSheetList()
        }
    }
}
