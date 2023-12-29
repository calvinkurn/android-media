package com.tokopedia.tokopedianow.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.util.QueryParamUtil.mapToQueryParamsMap
import com.tokopedia.tokopedianow.home.domain.mapper.CatalogCouponListMapper.RESPONSE_STYLE_PARAMS_COLUMNS
import com.tokopedia.tokopedianow.home.domain.mapper.CatalogCouponListMapper.RESPONSE_TYPE
import com.tokopedia.tokopedianow.home.domain.mapper.CatalogCouponListMapper.mapToClaimCouponWidgetUiModelList
import com.tokopedia.tokopedianow.home.domain.model.GetCatalogCouponListResponse
import com.tokopedia.tokopedianow.home.domain.model.Header
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.RedeemCouponResponse
import com.tokopedia.tokopedianow.home.mapper.HomeHeaderMapper.createHomeHeaderUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetUiModel

object ClaimCouponDataFactory {
    fun createChannelLayout(
        widgetId: String,
        widgetTitle: String,
        slugText: String,
        styleParam: String
    ): List<HomeLayoutResponse> {
        return listOf(
            HomeLayoutResponse(
                id = widgetId,
                layout = "coupon_claim",
                header = Header(
                    name = widgetTitle,
                    serverTimeUnix = 0
                ),
                widgetParam = slugText,
                styleParam = styleParam
            )
        )
    }

    fun createCatalogCouponList(
        slugs: List<String>,
        buttonStr: String,
        isEmpty: Boolean
    ): GetCatalogCouponListResponse.TokopointsCatalogWithCouponList {
        val resultStatus =
            GetCatalogCouponListResponse.TokopointsCatalogWithCouponList.ResultStatus(
                code = "200",
                message = listOf(),
                status = "success"
            )
        return GetCatalogCouponListResponse.TokopointsCatalogWithCouponList(
            catalogWithCouponList =
            if (isEmpty) {
                listOf()
            } else {
                slugs.map { slug ->
                    GetCatalogCouponListResponse.TokopointsCatalogWithCouponList.CatalogWithCoupon(
                        id = "1",
                        appLink = "tokopedia://now",
                        buttonStr = buttonStr,
                        imageUrlMobile = "tokopedia://now/123",
                        smallImageUrlMobile = "tokopedia://now/1234",
                        slug = slug
                    )
                }
            },
            resultStatus = resultStatus
        )
    }

    fun createLayoutListUiModel(
        widgetId: String,
        widgetTitle: String,
        slugText: String,
        isError: Boolean,
        isEmpty: Boolean,
        warehouseId: String,
        buttonStr: String,
        styleParam: String
    ): HomeLayoutListUiModel {
        val slugs = slugText.split(";")

        val claimCoupon = HomeClaimCouponWidgetUiModel(
            id = widgetId,
            title = widgetTitle,
            claimCouponList = listOf(),
            isDouble = styleParam.mapToQueryParamsMap()[RESPONSE_STYLE_PARAMS_COLUMNS] == RESPONSE_TYPE,
            slugs = slugs,
            state = TokoNowLayoutState.SHOW,
            slugText = slugText,
            warehouseId = warehouseId
        )

        val newListClaimCoupon: List<Visitable<*>> = if (isEmpty) {
            listOf<Visitable<*>>(
                createHomeHeaderUiModel()
            )
        } else if (isError) {
            listOf<Visitable<*>>(
                createHomeHeaderUiModel(),
                claimCoupon.copy(state = TokoNowLayoutState.HIDE)
            )
        } else {
            listOf<Visitable<*>>(
                createHomeHeaderUiModel(),
                claimCoupon.copy(
                    claimCouponList = createCatalogCouponList(
                        slugs,
                        buttonStr,
                        false
                    ).mapToClaimCouponWidgetUiModelList(claimCoupon, slugs)
                )
            )
        }

        return HomeLayoutListUiModel(
            items = newListClaimCoupon,
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
