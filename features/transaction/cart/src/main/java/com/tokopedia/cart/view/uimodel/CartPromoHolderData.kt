package com.tokopedia.cart.view.uimodel

import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata

data class CartPromoHolderData(
    var productUiModelList: MutableList<CartItemHolderData> = ArrayList(),
    var promoCodes: List<String> = emptyList(),
    var cartString: String = "",
    var cartStringGroup: String = "",
    var shopId: String = "",
    var boMetadata: BoMetadata = BoMetadata(),
    var warehouseId: Long = 0,
    var isPo: Boolean = false,
    var poDuration: String = "",
    var needToAddCodes: Boolean = false
)
