package com.tokopedia.imagepicker_insta.views.adapters

import com.tokopedia.imagepicker_insta.models.ImageAdapterData

class SelectionOrder {

    private var itemSelectionOrderList: ArrayList<ImageAdapterData> = arrayListOf()

    fun insert(item: ImageAdapterData) {
        itemSelectionOrderList.remove(item)
        itemSelectionOrderList.add(0, item)
    }

    fun remove(item: ImageAdapterData) {
        itemSelectionOrderList.remove(item)
    }

    fun getPreviousSelectedItem():ImageAdapterData?{
        if(itemSelectionOrderList.size>1){
            return itemSelectionOrderList[1]
        }
        return null
    }

    fun clear(){
        itemSelectionOrderList.clear()
    }

    fun getOrderList(): ArrayList<ImageAdapterData> {
        return itemSelectionOrderList
    }
}