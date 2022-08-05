package com.tokopedia.shop.common.graphql.domain.mapper

import com.tokopedia.shop.common.data.source.cloud.model.RestrictMetaModel
import com.tokopedia.shop.common.data.source.cloud.model.TokoPlusBadgeResponse
import com.tokopedia.shop.common.view.model.BadgeUiModel
import com.tokopedia.shop.common.view.model.TokoPlusBadgeUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 04/07/22.
 */

class TokoPlusBadgeMapper @Inject constructor() {

    companion object {
        private const val STATUS_ELIGIBLE = "eligible"
        private const val FREE_SHIPPING = "seller_bebas_ongkir_shop"
        private const val TOKO_PLUS = "bebas_ongkir_shop_plus_only"
    }

    fun mapToUiModel(data: TokoPlusBadgeResponse): TokoPlusBadgeUiModel {
        return TokoPlusBadgeUiModel(
            freeShipping = getBadge(data.restrictValidateRestriction.metaResponse, FREE_SHIPPING),
            tokoPlus = getBadge(data.restrictValidateRestriction.metaResponse, TOKO_PLUS)
        )
    }

    private fun getBadge(list: List<RestrictMetaModel>, restrictionName: String): BadgeUiModel {
        val data = list.firstOrNull { it.restrictionName == restrictionName }
        return BadgeUiModel(
            status = data?.metaData?.firstOrNull()?.status == STATUS_ELIGIBLE,
            badgeUrl = data?.metaData?.firstOrNull()?.metaStatus?.freeShippingTokoPlus?.badgeUrl.orEmpty()
        )
    }
}