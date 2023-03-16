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

    fun createCatalogCouponList(
        slugs: List<String>,
        buttonStr: String
    ): GetCatalogCouponListResponse.TokopointsCatalogWithCouponList {
        val resultStatus = GetCatalogCouponListResponse.TokopointsCatalogWithCouponList.ResultStatus(
            code = "200",
            message = listOf(),
            status = "success"
        )
        return GetCatalogCouponListResponse.TokopointsCatalogWithCouponList(
            catalogWithCouponList = slugs.map { slug ->
                GetCatalogCouponListResponse.TokopointsCatalogWithCouponList.CatalogWithCoupon(
                    id = "1",
                    appLink = "tokopedia://now",
                    buttonStr = buttonStr,
                    imageUrlMobile = "tokopedia://now/123",
                    smallImageUrlMobile = "tokopedia://now/1234",
                    slug = slug
                )
            },
            resultStatus = resultStatus
        )
    }

    fun createLayoutListUiModel(
        widgetId: String,
        widgetTitle: String,
        slugText: String,
        isError: Boolean,
        warehouseId: String,
        buttonStr: String
    ): HomeLayoutListUiModel {
        val slugs = slugText.split(";")

        val claimCoupon = HomeClaimCouponWidgetUiModel(
            id = widgetId,
            title = widgetTitle,
            claimCouponList = listOf(),
            isDouble = false,
            slugs = slugs,
            state = TokoNowLayoutState.SHOW,
            slugText = slugText,
            warehouseId = warehouseId
        )

        return HomeLayoutListUiModel(
            items = listOf(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                if (isError) {
                    claimCoupon.copy(state = TokoNowLayoutState.HIDE)
                } else {
                    claimCoupon.copy(claimCouponList = createCatalogCouponList(slugs, buttonStr).mapToClaimCouponWidgetUiModelList(claimCoupon, slugs))
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
                        cta = "tokopedia://now",
                        code = "BAC"
                    )
                )
            )
        )
    }
}
