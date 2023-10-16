package com.tokopedia.content.common.view

/**
 * Created by meyta.taliti on 11/05/23.
 */
data class ContentTaggedProductUiModel(
    val id: String,
    val parentID: String,
    val showGlobalVariant: Boolean,
    val shop: Shop,
    val title: String,
    val imageUrl: String,
    val price: Price,
    val appLink: String,
    val campaign: Campaign,
    val affiliate: Affiliate,
    val stock: Stock,
) {
    data class Affiliate(
        val id: String,
        val channel: String
    ) {
        companion object {
            val Empty get() = Affiliate(id = "", channel = "")
        }
    }

    data class Shop(
        val id: String,
        val name: String
    )

    data class DiscountedPrice(
        val discount: Int,
        val originalFormattedPrice: String,
        val formattedPrice: String,
        override val price: Double
    ) : Price

    data class CampaignPrice(
        val originalFormattedPrice: String,
        val formattedPrice: String,
        override val price: Double
    ) : Price

    data class NormalPrice(
        val formattedPrice: String,
        override val price: Double
    ) : Price

    sealed interface Price {
        val price : Double
    }

    val finalPrice: Double
        get() {
            return when (val price = this.price) {
                is DiscountedPrice -> price.price
                is NormalPrice -> price.price
                is CampaignPrice -> price.price
            }
        }

    data class Campaign(
        val type: CampaignType,
        val status: CampaignStatus,
        val isExclusiveForMember: Boolean
    ) {
        val isUpcoming: Boolean
            get() {
                return status is CampaignStatus.Upcoming
            }
    }

    sealed class CampaignStatus {
        object Unknown : CampaignStatus()
        object Upcoming : CampaignStatus()
        data class Ongoing(
            val stockLabel: String,
            val stockInPercent: Float
        ) : CampaignStatus()
    }

    enum class CampaignType {
        FlashSaleToko, RilisanSpecial, NoCampaign
    }

    sealed class Stock {
        object OutOfStock: Stock()
        object Available: Stock() //add raw stock if needed
    }

    enum class SourceType {
        Organic, NonOrganic
    }
}
