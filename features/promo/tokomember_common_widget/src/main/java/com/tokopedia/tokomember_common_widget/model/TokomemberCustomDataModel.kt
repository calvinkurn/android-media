package com.tokopedia.tokomember_common_widget.model

data class TokomemberShopCardModel(
    var shopName: String = "",
    var shopMemberName: String = "",
    var shopType: Int = 0,
    var startDate: String = "",
    var endData: String = ""
)

data class TokomemberProgramCardModel(
    var programStatus: Int = 0,
    var programStartDate: String = "",
    var programStartTime: String = "",
    var programEndTime: String = "",
    var programEndDate: String = "",
    var programMember: String = "",
    var programTransaction: String = ""
)