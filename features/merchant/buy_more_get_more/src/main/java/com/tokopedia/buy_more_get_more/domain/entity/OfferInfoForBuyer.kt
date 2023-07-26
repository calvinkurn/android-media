package com.tokopedia.buy_more_get_more.domain.entity

import com.google.gson.annotations.SerializedName

data class OfferInfoForBuyer(
    val responseHeader: ResponseHeader = ResponseHeader(),
    val offeringJsonData: String = "",
    val offerings: List<Offering> = emptyList()
) {
    data class ResponseHeader(
        val success: Boolean = true,
        val error_code: Long = 0,
        val processTime: String = ""
    )

    data class Offering(
        val id: Long = 0,
        val offerName: String = "",
        val offerTypeId: Long = 0,
        val startDate: String = "",
        val endDate: String = "",
        val maxAppliedTier: Int = 0,
        val tierList: List<Tier> = emptyList()
    ) {
        data class Tier(
            val tierId: Long = 0,
            val level: Int = 0,
            val rules: List<Rule> = emptyList(),
            val benefits: List<Benefit> = emptyList(),
            val attributes: String = ""
        ) {
            data class Rule(
                val type: String = "",
                val operation: String = "",
                val value: Int = 0
            )

            data class Benefit(
                val type: String = "",
                val value: Int = 0
            )
        }
    }
}
