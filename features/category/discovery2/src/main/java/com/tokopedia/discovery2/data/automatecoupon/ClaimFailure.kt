package com.tokopedia.discovery2.data.automatecoupon

sealed class ClaimFailure {
    object Unauthorized : ClaimFailure()
    data class Ineligible(val message: String) : ClaimFailure()
}
