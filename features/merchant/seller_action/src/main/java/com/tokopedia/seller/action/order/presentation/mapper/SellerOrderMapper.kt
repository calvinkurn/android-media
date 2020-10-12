package com.tokopedia.seller.action.order.presentation.mapper

import android.content.Context
import android.net.Uri
import com.tokopedia.seller.action.common.presentation.slices.SellerSlice
import com.tokopedia.seller.action.order.domain.model.Order
import com.tokopedia.seller.action.order.presentation.slice.SellerOrderSlice
import com.tokopedia.seller.action.common.presentation.mapper.SellerActionMapper
import com.tokopedia.seller.action.common.presentation.model.SellerSuccessItem

class SellerOrderMapper(context: Context,
                        sliceUri: Uri): SellerActionMapper<Order>(context, sliceUri) {

    override fun <T : SellerSuccessItem> getSuccessSlice(itemList: List<T>): SellerSlice {
        return SellerOrderSlice(context, sliceUri, itemList.filterIsInstance(Order::class.java))
    }

}