package com.tokopedia.shop.common.data.model

data class RestrictionEngineModel(
        var productId : Int = 0,
        var status : String = "",
        var buttonLabel: String? = null,
        var voucherIconUrl: String? = null,
        var actions : List<Actions> = listOf()
)

data class Actions (
        var actionType : String = "",
        var title : String = "",
        var description : String = "",
        var actionUrl : String = "",
        var attributeName : String = ""
)