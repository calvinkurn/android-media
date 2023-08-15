package com.tokopedia.minicart.bmgm.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 14/08/23.
 */

class LocalCacheUseCase @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) {

    suspend fun saveToLocalCache(model: BmgmMiniCartDataUiModel) {
        withContext(dispatchers.io) {
            val data = mapToCommonData(model)
            PersistentCacheManager.instance.put(BmgmCommonDataModel.PARAM_KEY_BMGM_DATA, data)
        }
    }

    suspend fun clearLocalCache() {
        withContext(dispatchers.io) {
            PersistentCacheManager.instance.delete(BmgmCommonDataModel.PARAM_KEY_BMGM_DATA)
        }
    }

    private fun mapToCommonData(model: BmgmMiniCartDataUiModel): BmgmCommonDataModel {
        return BmgmCommonDataModel(
            offerId = model.offerId,
            offerName = model.offerName,
            offerMessage = model.offerMessage,
            totalDiscount = model.totalDiscount,
            finalPrice = model.finalPrice,
            priceBeforeBenefit = model.priceBeforeBenefit,
            hasReachMaxDiscount = model.hasReachMaxDiscount,
            showMiniCartFooter = model.showMiniCartFooter,
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
        return products.map { p ->
            BmgmCommonDataModel.ProductModel(
                productId = p.productId,
                warehouseId = p.warehouseId,
                productName = p.productName,
                productImage = p.productImage,
                productPrice = p.productPrice,
                productPriceFmt = p.productPriceFmt,
                quantity = p.quantity,
            )
        }
    }
}