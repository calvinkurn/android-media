package com.tokopedia.home_component.visitable

data class CouponWidgetDataItemModel(
    val benefitType: BenefitType = BenefitType(),
    val shopName: String = "",
    val benefitValue: String = "",
    val tnc: String = "",
    val expiredText: String = "",
    val expiredUnix: Long = 0L,
    val ctaButton: CtaButton = CtaButton(),
    val iconUrl: String = "",
    val backgroundUrl: String = "",
    val backgroundGradientColors: List<String> = emptyList()
) {

    data class BenefitType(
        val title: String = "",
        val textColor: String = ""
    )

    data class CtaButton(
        val text: String = "",
        val type: Type = Type.Redirect,
        val catalogId: String = "",
        val url: String = ""
    ) {

        sealed class Type(val value: String) {
            object Claim : Type("claim")
            object Redirect : Type("redirect")
        }
    }
}
