package com.tkpd.atc_variant.data.uidata

import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData

/**
 * Created by Yehezkiel on 17/05/21
 */
data class PartialButtonDataModel(
        val isProductSelectedBuyable: Boolean = false,
        val isShopOwner: Boolean = false,
        val cartTypeData: CartTypeData? = null,
        val alternateText: String = ""
)