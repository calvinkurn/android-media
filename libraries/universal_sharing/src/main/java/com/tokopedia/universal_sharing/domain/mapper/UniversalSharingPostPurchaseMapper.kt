package com.tokopedia.universal_sharing.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseModel
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseProductModel
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseShopModel
import com.tokopedia.universal_sharing.util.UniversalShareConst
import com.tokopedia.universal_sharing.view.bottomsheet.typefactory.UniversalSharingTypeFactory
import com.tokopedia.universal_sharing.view.model.UniversalSharingPostPurchaseProductUiModel
import com.tokopedia.universal_sharing.view.model.UniversalSharingPostPurchaseShopTitleUiModel
import javax.inject.Inject

class UniversalSharingPostPurchaseMapper @Inject constructor() {

    fun mapToUiModel(data: UniversalSharingPostPurchaseModel):
        List<Visitable<in UniversalSharingTypeFactory>> {
        val result = ArrayList<Visitable<in UniversalSharingTypeFactory>>()
        data.shopList.forEach {
            // Add shop title
            val shopUiModel = generateShopUiModel(it.getShopType(), it.shopName)
            result.add(shopUiModel)
            // Add products
            it.productList.forEach { product ->
                val productUiModel = generateProductUiModel(it.shopName, product)
                result.add(productUiModel)
            }
        }
        return result
    }

    private fun generateShopUiModel(
        shopType: UniversalSharingPostPurchaseShopModel.ShopType,
        shopName: String
    ): UniversalSharingPostPurchaseShopTitleUiModel {
        val iconUrl = getIconUrl(shopType)
        return UniversalSharingPostPurchaseShopTitleUiModel(
            iconUrl = iconUrl,
            name = shopName
        )
    }

    private fun getIconUrl(shopType: UniversalSharingPostPurchaseShopModel.ShopType): String {
        return when (shopType) {
            UniversalSharingPostPurchaseShopModel.ShopType.OFFICIAL_STORE ->
                UniversalShareConst.ShopTypeIcon.OFFICIAL_STORE
            UniversalSharingPostPurchaseShopModel.ShopType.PM ->
                UniversalShareConst.ShopTypeIcon.PM
            UniversalSharingPostPurchaseShopModel.ShopType.PM_PRO ->
                UniversalShareConst.ShopTypeIcon.PM_PRO
            UniversalSharingPostPurchaseShopModel.ShopType.REGULAR -> ""
        }
    }

    private fun generateProductUiModel(
        shopName: String,
        product: UniversalSharingPostPurchaseProductModel
    ): UniversalSharingPostPurchaseProductUiModel {
        return UniversalSharingPostPurchaseProductUiModel(
            orderId = product.orderId,
            productId = product.productId,
            name = product.productName,
            price = product.productPrice,
            imageUrl = product.imageUrl,
            shopName = shopName
        )
    }
}
