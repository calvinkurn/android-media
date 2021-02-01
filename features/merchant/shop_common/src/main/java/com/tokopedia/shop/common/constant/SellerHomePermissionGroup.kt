package com.tokopedia.shop.common.constant

import androidx.annotation.StringDef

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(
        SellerHomePermissionGroup.PRODUCT,
        SellerHomePermissionGroup.CHAT,
        SellerHomePermissionGroup.ORDER,
        SellerHomePermissionGroup.DEFAULT
)
annotation class SellerHomePermissionGroup {
    companion object {
        const val PRODUCT = "Produk"
        const val CHAT = "Chat"
        const val ORDER = "Penjualan"
        const val DEFAULT = "ini"
    }
}