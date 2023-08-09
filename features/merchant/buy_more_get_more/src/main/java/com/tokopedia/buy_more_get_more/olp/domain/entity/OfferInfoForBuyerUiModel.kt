package com.tokopedia.buy_more_get_more.olp.domain.entity

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.OlpAdapterTypeFactory

data class OfferInfoForBuyerUiModel(
    val responseHeader: ResponseHeader = ResponseHeader(),
    val offeringJsonData: String = "",
    val offerings: List<Offering> = emptyList()
): Visitable<OlpAdapterTypeFactory> {
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
        val tierList: List<Tier> = emptyList(),
        val shopData: ShopData = ShopData()
    ) {
        data class ShopData(
            val shopId: Long = 0,
            val shopName: String = "",
            val badge: String =""
        )
        data class Tier(
            val tierId: Long = 0,
            val level: Int = 0,
            val rules: List<Rule> = emptyList(),
            val benefits: List<Benefit> = emptyList(),
            val attributes: String = ""
        ) {
            data class Rule(
                val typeId: Long = 0,
                val operation: String = "",
                val value: Int = 0
            )

            data class Benefit(
                val typeId: Long = 0,
                val value: Int = 0
            )
        }
    }

    override fun type(typeFactory: OlpAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
