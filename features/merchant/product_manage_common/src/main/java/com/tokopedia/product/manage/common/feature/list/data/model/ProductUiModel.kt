package com.tokopedia.product.manage.common.feature.list.data.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.manage.common.feature.list.view.adapter.factory.ProductManageAdapterFactory
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductCampaignType
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class ProductUiModel(
    val id: String,
    val title: String?,
    val imageUrl: String?,
    val minPrice: PriceUiModel?,
    val maxPrice: PriceUiModel?,
    val status: ProductStatus?,
    val url: String?,
    val cashBack: Int,
    val stock: Int?,
    val isFeatured: Boolean?,
    val isVariant: Boolean?,
    val multiSelectActive: Boolean,
    val isChecked: Boolean,
    val hasStockReserved: Boolean,
    val topAdsInfo: TopAdsInfo?,
    val access: ProductManageAccess?,
    val isCampaign: Boolean,
    val campaignTypeList: List<ProductCampaignType>?,
    val isProductBundling: Boolean,
    val suspendLevel: Int,
    val hasStockAlert: Boolean,
    val stockAlertActive: Boolean,
    val stockAlertCount: Int,
    val maxStock: Int?,
    val isShopModerate: Boolean,
    val haveNotifyMeOOS: Boolean,
    val notifyMeOOSCount: String,
    val notifyMeOOSWording: String,
    val isEmptyStock: Boolean,
    val isStockGuaranteed: Boolean,
    val isTobacco: Boolean
) : Visitable<ProductManageAdapterFactory> {
    override fun type(typeFactory: ProductManageAdapterFactory): Int {
        return typeFactory.type(this)
    }

    fun isVariant(): Boolean = isVariant == true

    fun isActive(): Boolean = status == ProductStatus.ACTIVE
    fun isInactive(): Boolean = status == ProductStatus.INACTIVE
    fun isViolation(): Boolean =
        status == ProductStatus.VIOLATION || status == ProductStatus.MODERATED

    fun isNotViolation(): Boolean = !(isViolation() || isPending())
    fun isPending(): Boolean = status == ProductStatus.PENDING
    fun isEmpty(): Boolean = status == ProductStatus.EMPTY || stock == 0
    fun hasTopAds(): Boolean = topAdsInfo?.isTopAds == true || topAdsInfo?.isAutoAds == true

    fun hasEditPriceAccess() = access?.editPrice == true && !isShopModerate
    fun hasEditProductAccess() = access?.editProduct == true

    fun getCampaignTypeCount() = campaignTypeList?.count().orZero()
    fun isSuspend(): Boolean = suspendLevel != 0
    fun isSuspendLevelTwoUntilFour(): Boolean = suspendLevel > 1
    fun isNotSuspendLevelTwoUntilFour(): Boolean = !(isSuspendLevelTwoUntilFour())
}
