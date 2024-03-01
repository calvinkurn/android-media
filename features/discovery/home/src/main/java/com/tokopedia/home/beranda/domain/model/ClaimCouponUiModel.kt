package com.tokopedia.home.beranda.domain.model

import com.tokopedia.network.exception.MessageErrorException

data class ClaimCouponUiModel(
    val isRedeemSucceed: Boolean = false,
    val redirectUrl: String = "",
    val redirectAppLink: String = "",
    val errorException: MessageErrorException? = null
)
