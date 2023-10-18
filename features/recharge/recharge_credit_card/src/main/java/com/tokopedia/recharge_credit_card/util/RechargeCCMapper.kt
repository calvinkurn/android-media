package com.tokopedia.recharge_credit_card.util

import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCPromo
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCRecommendation

object RechargeCCMapper {

    fun mapRechargeCCRecomToTopupRecom(recommendations: List<RechargeCCRecommendation>): List<TopupBillsRecommendation> {
        return recommendations.map {
            TopupBillsRecommendation(
                iconUrl = it.iconUrl,
                title = it.title,
                clientNumber = it.clientNumber,
                applink = it.applink,
                weblink = it.weblink,
                productPrice = it.productPrice,
                type = it.type,
                categoryId = it.categoryId,
                productId = it.productId,
                isAtc = it.isAtc,
                operatorId = it.operatorId,
                description = it.description,
                position = it.position,
                label = it.label,
                token = it.token
            )
        }
    }

    fun mapRechargeCCPromoToTopupPromo(promos: List<RechargeCCPromo>): List<TopupBillsPromo> {
        return promos.map {
            TopupBillsPromo(
                id = it.id,
                urlBannerPromo = it.urlBannerPromo,
                title = it.title,
                subtitle = it.subtitle,
                promoCode = it.promoCode,
                voucherCodeCopied = it.voucherCodeCopied
            )
        }
    }
}
