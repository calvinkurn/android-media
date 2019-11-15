package com.tokopedia.product.detail.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.datamodel.*

interface DynamicProductDetailAdapterFactory {
    fun type(data: ProductSnapshotDataModel): Int
    fun type(data: ProductSocialProofDataModel): Int
    //    fun type(data: ProductShippingInfoDataModel) : Int
//    fun type(data: ProductShopVoucherDataModel) : Int
    fun type(data: ProductShopInfoDataModel): Int
    fun type(data: ProductDiscussionDataModel): Int
    fun type(data: ProductInfoDataModel): Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}