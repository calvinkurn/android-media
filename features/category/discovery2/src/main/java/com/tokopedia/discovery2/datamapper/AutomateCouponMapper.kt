package com.tokopedia.discovery2.datamapper

import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponCtaState
import com.tokopedia.discovery2.data.automatecoupon.CTA
import com.tokopedia.discovery2.data.automatecoupon.CtaRedirectionMetadata

object AutomateCouponMapper {
    fun List<CTA>?.mapToCtaState(): AutomateCouponCtaState {
        val cta = this?.firstOrNull() ?: return AutomateCouponCtaState.OutOfStock

        val jsonMetadata = CtaRedirectionMetadata.parse(cta.metadata.orEmpty())

        val properties = AutomateCouponCtaState.Properties(
            text = cta.text.orEmpty(),
            appLink = jsonMetadata.appLink,
            url = jsonMetadata.url
        )

        return when (cta.type.orEmpty()) {
            CTA_CLAIM -> AutomateCouponCtaState.Claim(jsonMetadata.catalogId, properties)
            CTA_REDIRECT -> AutomateCouponCtaState.Redirect(properties)
            else -> AutomateCouponCtaState.OutOfStock
        }
    }

    private const val CTA_CLAIM = "claim"
    private const val CTA_REDIRECT = "redirect"
}
