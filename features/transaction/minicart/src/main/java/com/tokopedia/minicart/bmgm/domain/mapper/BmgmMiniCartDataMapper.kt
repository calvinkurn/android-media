package com.tokopedia.minicart.bmgm.domain.mapper

import com.tokopedia.minicart.bmgm.domain.model.GetMiniCartDataResponse
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.purchase_platform.common.feature.bmgm.uimodel.BmgmCommonDataUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 08/08/23.
 */

class BmgmMiniCartDataMapper @Inject constructor() {

    fun mapToUiModel(data: GetMiniCartDataResponse): BmgmMiniCartDataUiModel {
        return getDummy()
    }

    fun mapToCommonDataModel(): BmgmCommonDataUiModel {
        return BmgmCommonDataUiModel()
    }

    fun getDummy(): BmgmMiniCartDataUiModel {
        return BmgmMiniCartDataUiModel(
            offerId = 1,
            offerName = "Summer Sale",
            offerMessage = "Yay, kamu dapat <b>potongan Rp120 rb!</b>",
            totalDiscount = 50000.0,
            finalPrice = 3300000.0,
            priceBeforeBenefit = 3800000.0,
            hasReachMaxDiscount = false,
            showMiniCartFooter = true,
            tiersApplied = listOf(
                BmgmMiniCartVisitable.TierUiModel(
                    tierId = 1,
                    tierDiscountStr = "diskon 25%",
                    priceBeforeBenefit = 1100000.0,
                    priceAfterBenefit = 1035000.0,
                    products = listOf(
                        BmgmMiniCartVisitable.ProductUiModel(
                            productId = "111",
                            warehouseId = "12",
                            productName = "cyp - tetra",
                            productImage = "https://images.tokopedia.net/img/cache/500-square/VqbcmM/2022/9/21/f1237076-4c15-46cf-9359-388cf79d8bc9.jpg.webp?ect=4g",
                            productPrice = 50000001.0,
                            productPriceFmt = "Rp50.000",
                            quantity = 1
                        ),
                        BmgmMiniCartVisitable.ProductUiModel(
                            productId = "111",
                            warehouseId = "12",
                            productName = "cyp - tetra",
                            productImage = "https://images.tokopedia.net/img/cache/500-square/VqbcmM/2022/9/21/f1237076-4c15-46cf-9359-388cf79d8bc9.jpg.webp?ect=4g",
                            productPrice = 50000001.0,
                            productPriceFmt = "Rp50.000",
                            quantity = 1
                        ),
                        BmgmMiniCartVisitable.ProductUiModel(
                            productId = "111",
                            warehouseId = "12",
                            productName = "cyp - tetra",
                            productImage = "https://images.tokopedia.net/img/cache/500-square/VqbcmM/2022/9/21/f1237076-4c15-46cf-9359-388cf79d8bc9.jpg.webp?ect=4g",
                            productPrice = 50000001.0,
                            productPriceFmt = "Rp50.000",
                            quantity = 1
                        )
                    )
                ),
                BmgmMiniCartVisitable.TierUiModel(
                    tierId = BmgmMiniCartVisitable.TierUiModel.NON_DISCOUNT_TIER_ID,
                    tierDiscountStr = "diskon 25%",
                    priceBeforeBenefit = 1100000.0,
                    priceAfterBenefit = 1035000.0,
                    products = listOf(
                        BmgmMiniCartVisitable.ProductUiModel(
                            productId = "111",
                            warehouseId = "12",
                            productName = "cyp - tetra",
                            productImage = "https://images.tokopedia.net/img/cache/500-square/VqbcmM/2022/9/21/f1237076-4c15-46cf-9359-388cf79d8bc9.jpg.webp?ect=4g",
                            productPrice = 50000001.0,
                            productPriceFmt = "Rp50.000",
                            quantity = 1
                        )
                    )
                )
            )
        )
    }
}