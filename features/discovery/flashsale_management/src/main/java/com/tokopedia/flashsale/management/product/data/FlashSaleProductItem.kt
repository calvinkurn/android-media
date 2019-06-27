package com.tokopedia.flashsale.management.product.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flashsale.management.product.adapter.FlashSaleProductAdapterTypeFactory

/**
 * Created by hendry on 19/11/18.
 */
abstract class FlashSaleProductItem : Visitable<FlashSaleProductAdapterTypeFactory>, Parcelable{
    abstract fun getProductId(): Int
    abstract fun getProductName(): String
    abstract fun getProductImageUrl(): String
    abstract fun getProductPrice(): Int
    abstract fun getProductDepartmentId(): List<Int>
    abstract fun getProductDepartmentName(): List<String>
    abstract fun getCampOriginalPrice(): Int
    abstract fun getDiscountPercentage(): Float
    abstract fun getDiscountedPrice(): Int
    abstract fun getCustomStock(): Int
    abstract fun getOriginalCustomStock(): Int
    abstract fun getStockSoldPercentage(): Int
    abstract fun isEligible(): Boolean
    abstract fun getMessage(): String
    abstract fun getProductStatus(): Int
    abstract fun getCampaignStatusId(): Int
    abstract fun getCampaignAdminStatusId(): Int

    override fun type(typeFactory: FlashSaleProductAdapterTypeFactory) = typeFactory.type(this)
}