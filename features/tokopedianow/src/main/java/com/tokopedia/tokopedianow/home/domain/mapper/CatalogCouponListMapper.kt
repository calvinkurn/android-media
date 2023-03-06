package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.util.QueryParamUtil.mapToQueryParamsMap
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.GetCatalogCouponListResponse
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.RedeemCouponResponse
import com.tokopedia.tokopedianow.home.presentation.model.HomeClaimCouponDataModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetUiModel

object CatalogCouponListMapper {
    private const val RESPONSE_STYLE_PARAMS_COLUMNS = "columns"
    private const val RESPONSE_TYPE = "double"
    fun mapToClaimCouponWidgetUiModel(response: HomeLayoutResponse, state: HomeLayoutItemState, warehouseId: String): HomeLayoutItemUiModel {
        val slugs = response.widgetParam.split(";")

        val categoryMenuUiModel = HomeClaimCouponWidgetUiModel(
            id = response.id,
            title = response.header.name,
            claimCouponList = listOf(),
            state = TokoNowLayoutState.LOADING,
            isDouble = response.styleParam.mapToQueryParamsMap()[RESPONSE_STYLE_PARAMS_COLUMNS] == RESPONSE_TYPE,
            slugs = slugs,
            slugText = response.widgetParam,
            warehouseId = warehouseId
        )
        return HomeLayoutItemUiModel(categoryMenuUiModel, state)
    }

    fun GetCatalogCouponListResponse.TokopointsCatalogWithCouponList?.mapToClaimCouponWidgetUiModelList(
        item: HomeClaimCouponWidgetUiModel
    ): List<HomeClaimCouponWidgetItemUiModel>? {
        return this?.catalogWithCouponList?.map { coupon ->
            HomeClaimCouponWidgetItemUiModel(
                id = coupon.id,
                widgetId = item.id,
                smallImageUrlMobile = coupon.smallImageUrlMobile,
                imageUrlMobile = coupon.imageUrlMobile,
                ctaText = coupon.buttonStr,
                isDouble = item.isDouble,
                slugText = item.slugText,
                appLink = coupon.appLink,
                couponName = coupon.title,
                warehouseId = item.warehouseId
            )
        }
    }

    fun RedeemCouponResponse.mapToClaimCouponDataModel(): HomeClaimCouponDataModel {
        val coupon = hachikoRedeem?.coupons?.firstOrNull()
        return HomeClaimCouponDataModel(
            appLink = coupon?.appLink.orEmpty(),
            code = coupon?.code.orEmpty()
        )
    }
}
