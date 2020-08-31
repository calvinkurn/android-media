package com.tokopedia.play.view.uimodel.mocker

import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PinnedProductUiModel
import com.tokopedia.play.view.uimodel.ProductLineUiModel
import com.tokopedia.play.view.uimodel.ProductSheetUiModel
import com.tokopedia.play.view.wrapper.PlayResult

/**
 * Created by jegul on 02/04/20
 */
object PlayUiMocker {

    fun getMockPinnedProduct() = PinnedProductUiModel(
            partnerName = "GSK Official Store",
            title = "Ayo belanja barang pilihan kami sebelum kehabisan!",
            hasPromo = true
    )

    fun getMockProductSheetContent() = PlayResult.Success(ProductSheetUiModel(
            title = "Barang & Promo Pilihan",
            partnerId = 123,
            voucherList = List(5) { voucherIndex ->
                MerchantVoucherUiModel(
                        type = if (voucherIndex % 2 == 0) MerchantVoucherType.Discount else MerchantVoucherType.Shipping,
                        title = if (voucherIndex % 2 == 0) "Cashback ${(voucherIndex + 1) * 2}rb" else "Gratis ongkir ${(voucherIndex + 1) * 2}rb",
                        description = "min. pembelian ${(voucherIndex + 1)}00rb"
                )
//                            VoucherPlaceholderUiModel
            },
            productList = List(5) {
                ProductLineUiModel(
                        id = "697897875",
                        shopId = "123",
                        imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/5/8/52943980/52943980_908dc570-338d-46d5-aed2-4871f2840d0d_1664_1664",
                        title = "Product $it",
                        isVariantAvailable = true,
                        price = if (it % 2 == 0) {
                            OriginalPrice("Rp20$it.000", 20000)
                        } else {
                            DiscountedPrice(
                                    originalPrice = "Rp20$it.000",
                                    discountPercent = it * 10,
                                    discountedPrice = "Rp2$it.000",
                                    discountedPriceNumber = 20000
                            )
                        },
                        stock = if (it % 2 == 0) {
                            OutOfStock
                        } else {
                            StockAvailable(it * 10)
                        },
                        minQty = 2,
                        isFreeShipping = true,
                        applink = "tokopedia://login"
                )
//                            ProductPlaceholderUiModel
            }
    ))

    fun getMockVoucherOnlySocket(productSheetUiModel: PlayResult<ProductSheetUiModel>?): PlayResult.Success<ProductSheetUiModel> {
        val currentProduct = if (productSheetUiModel is PlayResult.Success) productSheetUiModel.data else ProductSheetUiModel.empty()
        return PlayResult.Success(currentProduct.copy(
                voucherList = List(5) { voucherIndex ->
                    MerchantVoucherUiModel(
                            type = if (voucherIndex % 2 == 0) MerchantVoucherType.Discount else MerchantVoucherType.Shipping,
                            title = if (voucherIndex % 2 == 0) "Cashback ${(voucherIndex + 1) * 2}rb" else "Gratis ongkir ${(voucherIndex + 1) * 2}rb",
                            description = "min. pembelian ${(voucherIndex + 1)}00rb"
                    )
                }
        ))
    }

    fun getMockProductOnlySocket(productSheetUiModel: PlayResult<ProductSheetUiModel>?): PlayResult.Success<ProductSheetUiModel> {
        val currentProduct = if (productSheetUiModel is PlayResult.Success) productSheetUiModel.data else ProductSheetUiModel.empty()
        return PlayResult.Success(currentProduct.copy(
                productList = List(5) {
                    ProductLineUiModel(
                            id = it.toString(),
                            shopId = "123",
                            imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/5/8/52943980/52943980_908dc570-338d-46d5-aed2-4871f2840d0d_1664_1664",
                            title = "Product $it",
                            isVariantAvailable = true,
                            price = if (it % 2 == 0) {
                                OriginalPrice("Rp20$it.000", 20000)
                            } else {
                                DiscountedPrice(
                                        originalPrice = "Rp20$it.000",
                                        discountPercent = it * 10,
                                        discountedPrice = "Rp2$it.000",
                                        discountedPriceNumber = 20000
                                )
                            },
                            stock = if (it % 2 == 0) {
                                OutOfStock
                            } else {
                                StockAvailable(it * 10)
                            },
                            minQty = 2,
                            isFreeShipping = true,
                            applink = null
                    )
                }
        ))
    }
}