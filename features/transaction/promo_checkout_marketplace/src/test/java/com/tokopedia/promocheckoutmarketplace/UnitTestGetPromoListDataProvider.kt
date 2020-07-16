package com.tokopedia.promocheckoutmarketplace

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.promocheckoutmarketplace.data.response.CouponListRecommendationResponse
import com.tokopedia.promocheckoutmarketplace.presentation.mapper.PromoCheckoutUiModelMapper
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest

object UnitTestGetPromoListDataProvider {

    private val gson = Gson()
    private val uiModelmapper = PromoCheckoutUiModelMapper()
    private val fileUtil = UnitTestFileUtils()

    fun provideBasePromoResponseSuccessDataAllExpanded(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/base_promo_recommendation_response_success_all_expanded.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideBasePromoResponseSuccessDataAllCollapsed(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/base_promo_recommendation_response_success_all_collapsed.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideBasePromoResponseSuccessDataWithPreSelectedPromo(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/base_promo_recommendation_response_success_with_pre_selected_promo.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideBasePromoRequestData(): PromoRequest {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/base_promo_recommendation_request.json"), PromoRequest::class.java)
    }

    fun provideBasePromoResponseSuccessDataAllEligible(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/base_promo_recommendation_response_success_all_eligible.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideBasePromoResponseSuccessDataAllIneligible(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/base_promo_recommendation_response_success_all_ineligible.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideBasePromoResponseApplyManualFailed(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/base_promo_recommendation_response_apply_manual_failed.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideBasePromoResponseError(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/base_promo_recommendation_response_error.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideBasePromoResponseEmptyStateEmpty(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/base_promo_recommendation_response_empty_state_empty.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideBasePromoResponseEmptyStateCouponListEmpty(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/base_promo_recommendation_response_empty_state_coupon_list_empty.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideBasePromoResponseEmptyStatePhoneVerification(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/base_promo_recommendation_response_empty_state_phone_verification.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideBasePromoResponseEmptyStateBlacklisted(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/base_promo_recommendation_response_empty_state_blacklisted.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideBasePromoResponseEmptyStateUnknown(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/base_promo_recommendation_response_empty_state_unknown.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideNoCurrentSelectedExpandedGlobalPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val selectedPromo = provideBasePromoResponseSuccessDataAllExpanded().couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, 0, true, emptyList())
        selectedPromoUiModel.uiState.isSelected = false
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideNoCurrentSelectedCollapsedGlobalPromoData(): ArrayList<Visitable<*>> {
        val response = provideBasePromoResponseSuccessDataAllCollapsed()
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, 0, true, emptyList())
        selectedPromoUiModel.uiState.isSelected = false
        val selectedPromoList = ArrayList<PromoListItemUiModel>()
        selectedPromoList.add(selectedPromoUiModel)
        val selectedPromoHeaderUiModel = uiModelmapper.mapPromoListHeaderUiModel(response.couponListRecommendation.data.couponSections[0].subSections[0], 0, true)
        selectedPromoHeaderUiModel.uiData.tmpPromoItemList = selectedPromoList
        promoListUiModelList.add(selectedPromoHeaderUiModel)

        return promoListUiModelList
    }

    fun providePromoRequestWithSelectedExpandedGlobalPromo(): PromoRequest {
        val response = provideBasePromoResponseSuccessDataAllExpanded()
        val promoRequest = provideBasePromoRequestData()
        promoRequest.codes.add(response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0].code)

        return promoRequest
    }

    fun providePromoRequestWithSelectedCollapsedGlobalPromo(): PromoRequest {
        val response = provideBasePromoResponseSuccessDataAllCollapsed()
        val promoRequest = provideBasePromoRequestData()
        promoRequest.codes.add(response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0].code)

        return promoRequest
    }

    fun provideNoCurrentSelectedExpandedMerchantPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideBasePromoResponseSuccessDataAllExpanded()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, 0, true, emptyList())
        selectedPromoUiModel.uiState.isSelected = false
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideNoCurrentSelectedCollapsedMerchantPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideBasePromoResponseSuccessDataAllCollapsed()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, 0, true, emptyList())
        selectedPromoUiModel.uiState.isSelected = false
        val selectedPromoList = ArrayList<PromoListItemUiModel>()
        selectedPromoList.add(selectedPromoUiModel)
        val selectedPromoHeaderUiModel = uiModelmapper.mapPromoListHeaderUiModel(response.couponListRecommendation.data.couponSections[0].subSections[1], 0, true)
        selectedPromoHeaderUiModel.uiData.tmpPromoItemList = selectedPromoList
        promoListUiModelList.add(selectedPromoHeaderUiModel)

        return promoListUiModelList
    }

    fun providePromoRequestWithSelectedExpandedMerchantPromo(): PromoRequest {
        val response = provideBasePromoResponseSuccessDataAllExpanded()
        val promoRequest = provideBasePromoRequestData()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
        promoRequest.orders.forEach {
            if (it.shopId == selectedPromo.shopId.toLong()) {
                it.codes.add(selectedPromo.code)
            }
        }

        return promoRequest
    }

    fun providePromoRequestWithSelectedCollapsedMerchantPromo(): PromoRequest {
        val response = provideBasePromoResponseSuccessDataAllExpanded()
        val promoRequest = provideBasePromoRequestData()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
        promoRequest.orders.forEach {
            if (it.shopId == selectedPromo.shopId.toLong()) {
                it.codes.add(selectedPromo.code)
            }
        }

        return promoRequest
    }

    fun provideCurrentSelectedExpandedGlobalPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val selectedPromo = provideBasePromoResponseSuccessDataAllExpanded().couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, 0, true, emptyList())
        selectedPromoUiModel.uiState.isSelected = true
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideCurrentSelectedExpandedMerchantPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val selectedPromo = provideBasePromoResponseSuccessDataAllExpanded().couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, 0, true, emptyList())
        selectedPromoUiModel.uiState.isSelected = true
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideCurrentSelectedCollapsedGlobalPromoData(): ArrayList<Visitable<*>> {
        val response = provideBasePromoResponseSuccessDataAllCollapsed()
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, 0, true, emptyList())
        selectedPromoUiModel.uiState.isSelected = true
        val selectedPromoList = ArrayList<PromoListItemUiModel>()
        selectedPromoList.add(selectedPromoUiModel)
        val selectedPromoHeaderUiModel = uiModelmapper.mapPromoListHeaderUiModel(response.couponListRecommendation.data.couponSections[0].subSections[0], 0, true)
        selectedPromoHeaderUiModel.uiData.tmpPromoItemList = selectedPromoList
        promoListUiModelList.add(selectedPromoHeaderUiModel)

        return promoListUiModelList
    }

    fun provideCurrentSelectedCollapsedMerchantPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideBasePromoResponseSuccessDataAllCollapsed()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, 0, true, emptyList())
        selectedPromoUiModel.uiState.isSelected = true
        val selectedPromoList = ArrayList<PromoListItemUiModel>()
        selectedPromoList.add(selectedPromoUiModel)
        val selectedPromoHeaderUiModel = uiModelmapper.mapPromoListHeaderUiModel(response.couponListRecommendation.data.couponSections[0].subSections[1], 0, true)
        selectedPromoHeaderUiModel.uiData.tmpPromoItemList = selectedPromoList
        promoListUiModelList.add(selectedPromoHeaderUiModel)

        return promoListUiModelList
    }

}