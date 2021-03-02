package com.tokopedia.home.beranda.presentation.view.adapter.datamodel

data class HomeBalanceModel (
        val balanceDrawerItemModels: Map<Int, BalanceDrawerItemModel>,
        val balanceType: Int = TYPE_STATE_1
) {
    companion object {
        // State 1: Ovo, Coupon, Bebas Ongkir
        const val TYPE_STATE_1 = 1

        // State 2: Tokopoints, Ovo, Bebas Ongkir
        const val TYPE_STATE_2 = 2

        // State 3: Tokopoints, Coupon, Bebas Ongkir
        const val TYPE_STATE_3 = 3
    }
}

// free ongkir, tokopoint, coupon

//simple logic :
//title : check tag title, then check text title
//description : check tag description, then check text description
data class BalanceDrawerItemModel(
        val applink: String = "",
        val iconImageUrl: String = "",
        val defaultIconRes: Int? = null,
        val balanceTitleTextAttribute: BalanceTextAttribute? = null,
        val balanceSubTitleTextAttribute: BalanceTextAttribute? = null,
        val balanceTitleTagAttribute: BalanceTagAttribute? = null,
        val balanceSubTitleTagAttribute: BalanceTagAttribute? = null,
        val drawerItemType: Int = TYPE_TOKOPOINT
) {
    companion object {
        const val TYPE_TOKOPOINT = 1

        const val TYPE_FREE_ONGKIR = 2

        const val TYPE_COUPON = 3
    }
}

data class BalanceTextAttribute(
        val colour: String = "",
        val text: String = "",
        val isBold: Boolean = false
)

data class BalanceTagAttribute(
        val text: String = "",
        val backgroundColour: String = ""
)