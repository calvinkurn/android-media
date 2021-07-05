package com.tokopedia.topads.common.view.sheet

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.topads.common.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import kotlinx.android.synthetic.main.topads_edit_select_fragment_product_list_sheet_sort.view.*
import java.util.*

class ProductSortSheetList: BottomSheetUnify() {

    var onItemClick: ((sortId: String) -> Unit)? = null
    var selectedSortText = 0
    var positionSort = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var contentView = View.inflate(context, R.layout.topads_edit_select_fragment_product_list_sheet_sort, null)
        setChild(contentView)
        setTitle(getString(R.string.topads_edit_urutkan))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sortListItems: Array<String> = resources.getStringArray(R.array.sort_product_list_array)

        val sortListItemUnify = mapToItemUnifyList(sortListItems)

        view.sortList.setData(sortListItemUnify)

        view.sortList?.let {
            it.onLoadFinish {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    it.setSelectedFilterOrSort(sortListItemUnify, positionSort.orZero())
                }

                it.setOnItemClickListener { adapterView, view, index, l ->
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


    private fun onItemSortClickedBottomSheet(position: Int, sortListItemUnify: ArrayList<ListItemUnify>, sortListUnify: ListUnify) {
        try {
            positionSort = position
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                sortListUnify.setSelectedFilterOrSort(sortListItemUnify, position)
            }
            selectedSortText = position
            sortListUnify.setSelectedFilterOrSort(sortListItemUnify, position)
            onItemClick?.invoke(sortListItemUnify[position].listTitleText)
            dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getSelectedSortId(): String {
        return when(selectedSortText){
            1 -> TERBARU
            3 -> TERENDAH
            0 -> TERLARIS
            2 -> TERTINGGI
            else -> TERBARU
        }
    }


    private fun ListUnify.setSelectedFilterOrSort(items: List<ListItemUnify>, position: Int) {
        val clickedItem = this.getItemAtPosition(position) as ListItemUnify
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            when (choiceMode) {
                ListView.CHOICE_MODE_SINGLE -> {
                    items.filter {
                        it.listRightRadiobtn?.isChecked ?: false
                    }.filterNot { it == clickedItem }.onEach { it.listRightRadiobtn?.isChecked = false }

                    clickedItem.listRightRadiobtn?.isChecked = true
                }
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
        val TERBARU = "newest"
        val TERENDAH = "cheapest"
        val TERLARIS = "most_sales"
        val TERTINGGI = "most_expensive"

        fun newInstance(): ProductSortSheetList {
            return ProductSortSheetList()
        }
    }
}
