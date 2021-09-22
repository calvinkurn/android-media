package com.tokopedia.homenav.mainnav.view.datamodel.account

data class ProfileOvoDataModel(
    var ovoSaldo: String = "-",
    var ovoPoint: String = "",

    /**
     * Status
     */
    var isGetOvoError: Boolean = false,
    var isTokopointExternalAmountError: Boolean = false
)
