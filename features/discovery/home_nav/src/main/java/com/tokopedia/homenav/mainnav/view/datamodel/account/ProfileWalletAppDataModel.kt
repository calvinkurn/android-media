package com.tokopedia.homenav.mainnav.view.datamodel.account

data class ProfileWalletAppDataModel(
    var gopayBalance: String = "",
    var gopayPointsBalance: String = "",
    var walletAppImageUrl: String = "",
    var isEligibleForWalletApp: Boolean = false,
    var walletAppActivationCta: String = "",

    /**
     * Status
     */
    var isWalletAppLinked: Boolean = false,
    var isWalletAppFailed: Boolean = false
)
