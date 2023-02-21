package com.tokopedia.tkpd.flashsale.domain.entity

data class FlashSaleProductSubmissionSseResult(
    val reservationId: String = "",
    val status: String = "",
    val countProcessedProduct: Int = 0,
    val countAllProduct: Int = 0,
    val campaignId: String = ""
){
    object Status{
        const val IN_PROGRESS = "in-progress"
        const val COMPLETE = "complete"
        const val PARTIAL_SUCCESS = "partial-success"
        const val FAIL = "fail"
    }
}
