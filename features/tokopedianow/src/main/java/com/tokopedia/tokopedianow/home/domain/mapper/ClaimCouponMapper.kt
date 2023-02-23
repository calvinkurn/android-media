package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetUiModel

object ClaimCouponMapper {
    fun mapToClaimCouponUiModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val categoryMenuUiModel = HomeClaimCouponWidgetUiModel(
            id = response.id,
            claimCouponList = listOf()
        )
        return HomeLayoutItemUiModel(categoryMenuUiModel, state)
    }
}
