package com.tokopedia.tokopedianow.data

import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.CatalogCouponListMapper.mapToClaimCouponWidgetUiModelList
import com.tokopedia.tokopedianow.home.domain.model.GetCatalogCouponListResponse
import com.tokopedia.tokopedianow.home.domain.model.Header
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.RedeemCouponResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetUiModel

object ClaimCouponDataFactory {
    fun createChannelLayout(widgetId: String, widgetTitle: String, slugText: String): List<HomeLayoutResponse> {
        return listOf(
            HomeLayoutResponse(
                id = widgetId,
                layout = "coupon_claim",
                header = Header(
                    name = widgetTitle,
                    serverTimeUnix = 0
                ),
                widgetParam = slugText
            )
        )
    }

    fun createCatalogCouponList(slugText: String): GetCatalogCouponListResponse.TokopointsCatalogWithCouponList {
        val resultStatus = GetCatalogCouponListResponse.TokopointsCatalogWithCouponList.ResultStatus(
            code = "200",
            message = listOf(),
            status = "success"
        )
        return GetCatalogCouponListResponse.TokopointsCatalogWithCouponList(
            catalogWithCouponList = listOf(
                GetCatalogCouponListResponse.TokopointsCatalogWithCouponList.CatalogWithCoupon(
                    id = "1",
                    appLink = "tokopedia://now",
                    buttonStr = "Klaim",
                    imageUrlMobile = "tokopedia://now/123",
                    smallImageUrlMobile = "tokopedia://now/1234",
                    slug = slugText
                )
            ),
            resultStatus = resultStatus
        )
    }

    fun createLayoutListUiModel(
        widgetId: String,
        widgetTitle: String,
        slugText: String,
        isError: Boolean
    ): HomeLayoutListUiModel {
        val slugs = slugText.split(";")

        val claimCoupon = HomeClaimCouponWidgetUiModel(
            id = widgetId,
            title = widgetTitle,
            claimCouponList = null,
            isDouble = false,
            slugs = slugs,
            state = TokoNowLayoutState.SHOW
        )

        return HomeLayoutListUiModel(
            items = listOf(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                if (isError) {
                    claimCoupon.copy(state = TokoNowLayoutState.HIDE)
                } else {
                    claimCoupon.copy(claimCouponList = createCatalogCouponList(slugText).mapToClaimCouponWidgetUiModelList(claimCoupon))
                }
            ),
            state = TokoNowLayoutState.UPDATE
        )
    }

    fun createRedeemCoupon(): RedeemCouponResponse {
        return RedeemCouponResponse(
            hachikoRedeem = RedeemCouponResponse.HachikoRedeem(
                coupons = listOf(
                    RedeemCouponResponse.HachikoRedeem.Coupon(
                        appLink = "tokopedia://now",
                        code = "BAC"
                    )
                )
            )
        )
    }
}
