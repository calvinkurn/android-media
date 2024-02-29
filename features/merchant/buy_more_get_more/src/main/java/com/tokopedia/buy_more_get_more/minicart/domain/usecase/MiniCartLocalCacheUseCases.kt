package com.tokopedia.buy_more_get_more.minicart.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buy_more_get_more.R as buy_more_get_moreR
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 14/08/23.
 */

class MiniCartLocalCacheUseCases @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatchers: CoroutineDispatchers
) {

    suspend fun saveToLocalCache(
        model: BmgmMiniCartDataUiModel,
        shopId: Long,
        warehouseId: Long,
        offerEndDate: String
    ) {
        withContext(dispatchers.io) {
            runCatching {
                val data = mapToCommonData(model, shopId, warehouseId, offerEndDate)
                PersistentCacheManager.instance.put(BmgmCommonDataModel.PARAM_KEY_BMGM_DATA, data)
            }
        }
    }

    suspend fun clearLocalCache() {
        withContext(dispatchers.io) {
            runCatching {
                PersistentCacheManager.instance.delete(BmgmCommonDataModel.PARAM_KEY_BMGM_DATA)
            }
        }
    }

    suspend fun getCartData(): BmgmCommonDataModel {
        return withContext(dispatchers.io) {
            PersistentCacheManager.instance.get<BmgmCommonDataModel>(
                BmgmCommonDataModel.PARAM_KEY_BMGM_DATA, BmgmCommonDataModel::class.java, null
            ) ?: throw RuntimeException("No cart data stored in local cache")
        }
    }

    private fun mapToCommonData(
        model: BmgmMiniCartDataUiModel,
        shopId: Long,
        warehouseId: Long,
        offerEndDate: String,
        showMiniCartFooter: Boolean = true
    ): BmgmCommonDataModel {
        val bottomSheetTitle = context.getString(buy_more_get_moreR.string.bmgm_mini_cart_bmgm_bottom_sheet_title)
        return BmgmCommonDataModel(
            bottomSheetTitle = bottomSheetTitle,
            offerId = model.offerId,
            offerEndDate = offerEndDate,
            warehouseId = warehouseId,
            shopId = shopId.toString(),
            finalPrice = model.finalPrice,
            priceBeforeBenefit = model.priceBeforeBenefit,
            hasReachMaxDiscount = model.hasReachMaxDiscount,
            showMiniCartFooter = showMiniCartFooter,
            tiersApplied = getTiersApplied(model.tiers)
        )
    }

    private fun getTiersApplied(productList: List<BmgmMiniCartVisitable>): List<BmgmCommonDataModel.TierModel> {
        return productList.filterIsInstance<BmgmMiniCartVisitable.TierUiModel>().map {
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