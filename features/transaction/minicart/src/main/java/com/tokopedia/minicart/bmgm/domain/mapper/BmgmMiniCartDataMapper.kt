package com.tokopedia.minicart.bmgm.domain.mapper

import com.tokopedia.minicart.bmgm.domain.model.GetMiniCartDataResponse
import com.tokopedia.purchase_platform.common.feature.bmgm.uimodel.BmgmCommonDataUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 08/08/23.
 */

class BmgmMiniCartDataMapper @Inject constructor() {

    fun mapToUiModel(data: GetMiniCartDataResponse): BmgmCommonDataUiModel {
        return getDummy()
    }

    fun getDummy(): BmgmCommonDataUiModel {
        return BmgmCommonDataUiModel(
            offerId = 1,
            offerName = "Summer Sale",
            offerMessage = "Yay, kamu dapat <b>potongan Rp120 rb!</b>",
            offerDiscount = 50000.0,
            hasReachMaxDiscount = false,
            products = listOf(
                BmgmCommonDataUiModel.BundledProductUiModel(
                    tierId = 1,
                    tierDiscountStr = "diskon 25%",
                    priceBeforeBenefit = 1100000.0,
                    priceAfterBenefit = 1035000.0,
                    products = listOf(
                        BmgmCommonDataUiModel.SingleProductUiModel(
                            productId = "111",
                            warehouseId = "12",
                            productName = "cyp - tetra",
                            productImage = "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/3/5/70588522/70588522_c5cf67db-92a6-4d3d-8c99-de0d2a79bbd7_760_760",
                            productPrice = 50000001.0,
                            productPriceFmt = "Rp50.000",
                            quantity = 1
                        ),
                        BmgmCommonDataUiModel.SingleProductUiModel(
                            productId = "111",
                            warehouseId = "12",
                            productName = "cyp - tetra",
                            productImage = "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/3/5/70588522/70588522_c5cf67db-92a6-4d3d-8c99-de0d2a79bbd7_760_760",
                            productPrice = 50000001.0,
                            productPriceFmt = "Rp50.000",
                            quantity = 1
                        ),
                        BmgmCommonDataUiModel.SingleProductUiModel(
                            productId = "111",
                            warehouseId = "12",
                            productName = "cyp - tetra",
                            productImage = "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/3/5/70588522/70588522_c5cf67db-92a6-4d3d-8c99-de0d2a79bbd7_760_760",
                            productPrice = 50000001.0,
                            productPriceFmt = "Rp50.000",
                            quantity = 1
                        )
                    )
                ),
                BmgmCommonDataUiModel.BundledProductUiModel(
                    tierId = 1,
                    tierDiscountStr = "diskon 25%",
                    priceBeforeBenefit = 1100000.0,
                    priceAfterBenefit = 1035000.0,
                    products = listOf(
                        BmgmCommonDataUiModel.SingleProductUiModel(
                            productId = "111",
                            warehouseId = "12",
                            productName = "cyp - tetra",
                            productImage = "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/3/5/70588522/70588522_c5cf67db-92a6-4d3d-8c99-de0d2a79bbd7_760_760",
                            productPrice = 50000001.0,
                            productPriceFmt = "Rp50.000",
                            quantity = 1
                        ),
                        BmgmCommonDataUiModel.SingleProductUiModel(
                            productId = "111",
                            warehouseId = "12",
                            productName = "cyp - tetra",
                            productImage = "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/3/5/70588522/70588522_c5cf67db-92a6-4d3d-8c99-de0d2a79bbd7_760_760",
                            productPrice = 50000001.0,
                            productPriceFmt = "Rp50.000",
                            quantity = 1
                        )
                    )
                ),
                BmgmCommonDataUiModel.SingleProductUiModel(
                    productId = "111",
                    warehouseId = "12",
                    productName = "cyp - tetra",
                    productImage = "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/3/5/70588522/70588522_c5cf67db-92a6-4d3d-8c99-de0d2a79bbd7_760_760",
                    productPrice = 50000001.0,
                    productPriceFmt = "Rp50.000",
                    quantity = 1
                )
            )
        )
    }
}