package com.tokopedia.minicart.bmgm.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by @ilhamsuaib on 14/08/23.
 */

class MiniCartLocalCacheUseCases @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = dispatchers.io

    fun saveToLocalCache(
        model: BmgmMiniCartDataUiModel,
        shopId: Long,
        warehouseId: Long,
        offerEndDate: String
    ) {
        launch {
            val data = mapToCommonData(model, shopId, warehouseId, offerEndDate)
            PersistentCacheManager.instance.put(BmgmCommonDataModel.PARAM_KEY_BMGM_DATA, data)
        }
    }

    fun clearLocalCache() {
        launch {
            PersistentCacheManager.instance.delete(BmgmCommonDataModel.PARAM_KEY_BMGM_DATA)
        }
    }

    fun getCartData(): BmgmCommonDataModel {
        val commonData = PersistentCacheManager.instance.get<BmgmCommonDataModel>(
            BmgmCommonDataModel.PARAM_KEY_BMGM_DATA, BmgmCommonDataModel::class.java, null
        )
        return commonData ?: throw RuntimeException("No cart data stored in local cache")
    }

    private fun mapToCommonData(
        model: BmgmMiniCartDataUiModel,
        shopId: Long,
        warehouseId: Long,
        offerEndDate: String,
        showMiniCartFooter: Boolean = true
    ): BmgmCommonDataModel {
        return BmgmCommonDataModel(
            offerId = model.offerId,
            warehouseId = warehouseId,
            shopId = shopId.toString(),
            finalPrice = model.finalPrice,
            priceBeforeBenefit = model.priceBeforeBenefit,
            hasReachMaxDiscount = model.hasReachMaxDiscount,
            showMiniCartFooter = showMiniCartFooter,
            tiersApplied = getTiersApplied(model.tiersApplied)
        )
    }

    private fun getTiersApplied(productList: List<BmgmMiniCartVisitable.TierUiModel>): List<BmgmCommonDataModel.TierModel> {
        return productList.map {
            BmgmCommonDataModel.TierModel(
                tierId = it.tierId,
                tierMessage = it.tierMessage,
                tierDiscountStr = it.tierDiscountStr,
                priceBeforeBenefit = it.priceBeforeBenefit,
                priceAfterBenefit = it.priceAfterBenefit,
                products = mapProductList(it.products)
            )
        }
    }

    private fun mapProductList(products: List<BmgmMiniCartVisitable.ProductUiModel>): List<BmgmCommonDataModel.ProductModel> {
        return products.distinctBy { it.productId }.map { p ->
            BmgmCommonDataModel.ProductModel(
                productId = p.productId,
                warehouseId = p.warehouseId,
                productName = p.productName,
                productImage = p.productImage,
                productPrice = p.finalPrice,
                cartId = p.cartId,
                quantity = p.quantity,
            )
        }
    }
}