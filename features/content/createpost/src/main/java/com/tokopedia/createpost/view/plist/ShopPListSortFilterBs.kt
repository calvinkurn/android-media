package com.tokopedia.createpost.view.plist

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.tokopedia.createpost.common.view.plist.*
import com.tokopedia.createpost.createpost.R
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import kotlinx.android.synthetic.main.bs_shop_page_plist_sort.view.*
import java.util.*
import android.view.LayoutInflater as LayoutInflater1

class ShopPListSortFilterBs(val vm: ShopPageProductListViewModel, val listener: ShopPageListener, var sortListItems: List<ShopPagePListSortItem>) : BottomSheetUnify() {

    private var rvSort: ListUnify? = null
    var positionSort = 0
    var selectedSortText = 0

    companion object {
        const val DEFAULT_SORT_VALUE_NAME = "Pembaruan Terakhir"
        @JvmStatic
        fun newInstance(vm: ShopPageProductListViewModel, listener: ShopPageListener, sortListItems: List<ShopPagePListSortItem>): ShopPListSortFilterBs {
            return ShopPListSortFilterBs(vm, listener, sortListItems)
        }
    }

    override fun onCreateView(inflater: LayoutInflater1, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        addListObserver()
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

     private fun setData() {
         val sortListItemUnify = mapToItemUnifyList(sortListItems)

         view?.rv_sort?.setData(sortListItemUnify)

         view?.rv_sort?.let {
             it.onLoadFinish {
                 sortListItems.forEachIndexed { index, shopPagePListSortItem ->
                     if (shopPagePListSortItem.name == DEFAULT_SORT_VALUE_NAME)
                         positionSort = index
                 }

                 if (vm.getSortPosition() == -1)
                     sortListItemUnify[positionSort].listRightRadiobtn?.isChecked = true
                 else
                     it.setSelectedFilterOrSort(sortListItemUnify, vm.getSortPosition().orZero())


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
            listener.sortProductCriteriaClicked(sortListItems[position].name)

            vm.setNewSortValue(sortListItems[position], position)
            positionSort = position
            selectedSortText = position
            sortListUnify.setSelectedFilterOrSort(sortListItemUnify, position)
            dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun ListUnify.setSelectedFilterOrSort(items: List<ListItemUnify>, position: Int) {
        val clickedItem = this.getItemAtPosition(position) as ListItemUnify
        when (choiceMode) {
            ListView.CHOICE_MODE_SINGLE -> {
                items.filter {
                    it.listRightRadiobtn?.isChecked ?: false
                }.filterNot { it == clickedItem }.onEach { it.listRightRadiobtn?.isChecked = false }

                clickedItem.listRightRadiobtn?.isChecked = true
            }
        }
    }

    private fun mapToItemUnifyList(list: List<ShopPagePListSortItem>): ArrayList<ListItemUnify> {
        val itemUnifyList: ArrayList<ListItemUnify> = arrayListOf()
        list.map {
            val data = ListItemUnify(title = it.name, description = "")
            data.setVariant(rightComponent = ListItemUnify.RADIO_BUTTON)
            itemUnifyList.add(data)
        }
        return itemUnifyList
    }

    private fun addListObserver() = vm.sortLiveData.observe(this, {
        it?.let {
            when (it) {
                is Loading -> {
                }
                is Success -> {
                    sortListItems = it.data.sortData.result
                    setData()
                }
                is ErrorMessage -> {

                }
                else -> {

                }
            }
        }
    })

    private fun initLayout() {
        context?.getString(R.string.feed_product_page_filter_title)?.let { setTitle(it) }
        isDragable = false
        isHideable = true
        isKeyboardOverlap = false
        showCloseIcon = true

        showKnob = true
        val contentView = View.inflate(context,
            R.layout.bs_shop_page_plist_sort, null)
        rvSort = contentView?.findViewById(R.id.rv_sort)
        setChild(contentView)
    }
}