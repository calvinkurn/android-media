package com.tokopedia.product.manage.common.feature.variant.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.feature.variant.adapter.factory.ProductVariantAdapterFactory
import com.tokopedia.product.manage.common.feature.variant.data.model.Picture
import com.tokopedia.product.manage.common.feature.variant.data.model.CampaignType
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class ProductVariant(
    val id: String,
    val name: String,
    val status: ProductStatus,
    val combination: List<Int>,
    val isPrimary: Boolean,
    val isCampaign: Boolean,
    val price: Double,
    val sku: String,
    val stock: Int,
    val pictures: List<Picture>,
    val isAllStockEmpty: Boolean = false,
    val access: ProductManageAccess,
    val campaignTypeList: List<CampaignType>?,
    val maxStock: Int?,
    val notifymeCount: Int,
    val stockAlertStatus: Int,
    val stockAlertCount: Int,
    val isBelowStockAlert: Boolean,
    val hasDTStock: Boolean,
    val isTokoCabang: Boolean
) : Visitable<ProductVariantAdapterFactory> {

    companion object {
        private const val HAVE_STOCK_ALERT = 2
    }

    override fun type(typeFactory: ProductVariantAdapterFactory): Int {
        return typeFactory.type(this)
    }

    fun isActive(): Boolean {
        return status == ProductStatus.ACTIVE
    }

    fun isInactive(): Boolean {
        return status == ProductStatus.INACTIVE
    }

    fun isEmpty(): Boolean {
        return stock == 0
    }

    fun haveNotifyMe(): Boolean {
        return notifymeCount > 0
    }

    fun haveStockAlertActive(): Boolean {
        return stockAlertStatus == HAVE_STOCK_ALERT
    }
}
