package com.tokopedia.unifyorderhistory.util

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyorderhistory.data.model.UohListOrder
import com.tokopedia.unifyorderhistory.data.model.UohTypeData

object UohDataHelper {
    fun getIndexUohItemByUuid(uohList: MutableList<UohTypeData>, uuid: String): Int {
        var indexReturned = RecyclerView.NO_POSITION
        for ((index, data) in uohList.withIndex()) {
            if (data.typeLayout == UohConsts.TYPE_ORDER_LIST &&
                data.dataObject is UohListOrder.UohOrders.Order &&
                data.dataObject.orderUUID == uuid
            ) {
                if (indexReturned == RecyclerView.NO_POSITION) indexReturned = index
            }
        }
        return indexReturned
    }
}
