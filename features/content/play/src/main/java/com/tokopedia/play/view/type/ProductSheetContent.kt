package com.tokopedia.play.view.type

/**
 * Created by jegul on 03/03/20
 */
sealed class ProductSheetContent

data class ProductSheetVoucher(
        val type: MerchantVoucherType,
        val title: String,
        val subtitle: String
) : ProductSheetContent()

data class ProductSheetProduct(
        val id: String,
        val imageUrl: String,
        val title: String,
        val price: ProductPrice
) : ProductSheetContent()