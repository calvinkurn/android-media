package com.tokopedia.home.account

data class Variant(
        val feature: String,
        val variant: String
)

object  A{
    val test2 = mapOf("eventCategory" to "abtesting",
            "feature" to listOf(
                    Variant("Headline Ads New Design", "Headline Ads A"),
                    Variant("Power Merchant Link", "Dijual Oleh")
            ),
            "event" to "abtesting",
            "user_id" to "18110133"
    )
}
