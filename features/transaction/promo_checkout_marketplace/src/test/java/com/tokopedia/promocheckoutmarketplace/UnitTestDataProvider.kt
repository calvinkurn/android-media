package com.tokopedia.promocheckoutmarketplace

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.promocheckoutmarketplace.data.response.CouponListRecommendationResponse
import com.tokopedia.promocheckoutmarketplace.presentation.mapper.PromoCheckoutUiModelMapper
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest

val gson = Gson()
val uiModelmapper = PromoCheckoutUiModelMapper()

fun provideBasePromoResponseSuccessData(): CouponListRecommendationResponse {
    return gson.fromJson(UnitTestFileUtils().getJsonFromAsset("assets/base_coupon_recommendation_response_success.json"), CouponListRecommendationResponse::class.java)
}

fun provideBasePromoRequestData(): PromoRequest {
    return gson.fromJson(UnitTestFileUtils().getJsonFromAsset("assets/base_coupon_recommendation_request.json"), PromoRequest::class.java)
}

fun provideNoCurrentSelectedGlobalPromoData(): ArrayList<Visitable<*>> {
    val promoListUiModelList = ArrayList<Visitable<*>>()
    val selectedPromo = provideBasePromoResponseSuccessData().couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
    val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, 0, true, emptyList())
    selectedPromoUiModel.uiState.isSelected = false
    promoListUiModelList.add(selectedPromoUiModel)

    return promoListUiModelList
}

fun providePromoRequestWithSelectedGlobalPromo(): PromoRequest {
    val response = provideBasePromoResponseSuccessData()
    val promoRequest = provideBasePromoRequestData()
    promoRequest.codes.add(response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0].code)

    return promoRequest
}

fun provideNoCurrentSelectedMerchantPromoData(): ArrayList<Visitable<*>> {
    val promoListUiModelList = ArrayList<Visitable<*>>()
    val response = provideBasePromoResponseSuccessData()
    val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
    val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, 0, true, emptyList())
    selectedPromoUiModel.uiState.isSelected = false
    promoListUiModelList.add(selectedPromoUiModel)

    return promoListUiModelList
}

fun provideCurrentSelectedGlobalPromoData(): ArrayList<Visitable<*>> {
    val promoListUiModelList = ArrayList<Visitable<*>>()
    val selectedPromo = provideBasePromoResponseSuccessData().couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
    val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, 0, true, emptyList())
    selectedPromoUiModel.uiState.isSelected = true
    promoListUiModelList.add(selectedPromoUiModel)

    return promoListUiModelList
}

fun provideCurrentSelectedMerchantPromoData(): ArrayList<Visitable<*>> {
    val promoListUiModelList = ArrayList<Visitable<*>>()
    val selectedPromo = provideBasePromoResponseSuccessData().couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
    val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, 0, true, emptyList())
    selectedPromoUiModel.uiState.isSelected = true
    promoListUiModelList.add(selectedPromoUiModel)

    return promoListUiModelList
}