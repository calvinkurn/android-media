package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.GetCatalogCouponListResponse
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetUiModel

internal object CatalogCouponListMapper {
    private const val RESPONSE_TYPE = "double"
    fun mapToClaimCouponWidgetUiModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val slugs = response.widgetParam.split("\\s*;\\s*")

        val categoryMenuUiModel = HomeClaimCouponWidgetUiModel(
            id = response.id,
            title = response.header.name,
            claimCouponList = listOf(),
            state = TokoNowLayoutState.LOADING,
            isDouble = response.type == RESPONSE_TYPE,
            slugs = slugs
        )
        return HomeLayoutItemUiModel(categoryMenuUiModel, state)
    }

    fun GetCatalogCouponListResponse.TokopointsCatalogWithCouponList?.mapToClaimCouponWidgetUiModelList(
        item: HomeClaimCouponWidgetUiModel
    ): List<HomeClaimCouponWidgetItemUiModel>? {
        return this?.catalogWithCouponList?.map { coupon ->
            HomeClaimCouponWidgetItemUiModel(
                id = coupon.id,
                smallImageUrlMobile = coupon.smallImageUrlMobile,
                imageUrlMobile = coupon.imageUrlMobile,
                ctaText = coupon.buttonStr,
                isDouble = item.isDouble,
                appLink = coupon.appLink
            )
        }
    }
}
