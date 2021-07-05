package com.tokopedia.seller.action.order.presentation.mapper

import android.content.Context
import android.net.Uri
import com.tokopedia.seller.action.common.presentation.mapper.SellerActionMapper
import com.tokopedia.seller.action.common.presentation.model.SellerSuccessItem
import com.tokopedia.seller.action.common.presentation.slices.SellerSlice
import com.tokopedia.seller.action.common.utils.SellerActionUtils.convertToOrderDateTitle
import com.tokopedia.seller.action.order.domain.model.Order
import com.tokopedia.seller.action.order.presentation.slice.SellerOrderSlice

class SellerOrderMapper(context: Context,
                        sliceUri: Uri,
                        private val date: String? = null)
    : SellerActionMapper<Order>(context, sliceUri, date.convertToOrderDateTitle(context)) {

    override fun <T : SellerSuccessItem> getSuccessSlice(itemList: List<T>): SellerSlice {
        return SellerOrderSlice(context, sliceUri, date, itemList.filterIsInstance(Order::class.java))
    }

}