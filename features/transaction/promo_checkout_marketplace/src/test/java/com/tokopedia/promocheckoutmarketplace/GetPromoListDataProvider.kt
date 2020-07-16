package com.tokopedia.promocheckoutmarketplace

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.promocheckoutmarketplace.data.response.CouponListRecommendationResponse
import com.tokopedia.promocheckoutmarketplace.presentation.mapper.PromoCheckoutUiModelMapper
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest

object GetPromoListDataProvider {

    private val gson = Gson()
    private val uiModelmapper = PromoCheckoutUiModelMapper()
    private val fileUtil = UnitTestFileUtils()

    fun provideGetPromoListRequest(): PromoRequest {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_request.json"), PromoRequest::class.java)
    }

    fun provideGetPromoListResponseSuccessAllExpanded(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_success_all_expanded.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseSuccessAllCollapsed(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_success_all_collapsed.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseSuccessWithPreSelectedPromo(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_success_with_pre_selected_promo.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseSuccessAllEligible(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_success_all_eligible.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseSuccessAllIneligible(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_success_all_ineligible.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseApplyManualFailed(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_apply_manual_failed.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseError(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_error.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseEmptyStateEmpty(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_empty_state_empty.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseEmptyStateCouponListEmpty(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_empty_state_coupon_list_empty.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseEmptyStatePhoneVerification(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_empty_state_phone_verification.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseEmptyStateBlacklisted(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_empty_state_blacklisted.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseEmptyStateUnknown(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_empty_state_unknown.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideNoCurrentSelectedExpandedGlobalPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val selectedPromo = provideGetPromoListResponseSuccessAllExpanded().couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, 0, true, emptyList())
        selectedPromoUiModel.uiState.isSelected = false
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideNoCurrentSelectedCollapsedGlobalPromoData(): ArrayList<Visitable<*>> {
        val response = provideGetPromoListResponseSuccessAllCollapsed()
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
        val response = provideGetPromoListResponseSuccessAllExpanded()
        val promoRequest = provideGetPromoListRequest()
        promoRequest.codes.add(response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0].code)

        return promoRequest
    }

    fun providePromoRequestWithSelectedCollapsedGlobalPromo(): PromoRequest {
        val response = provideGetPromoListResponseSuccessAllCollapsed()
        val promoRequest = provideGetPromoListRequest()
        promoRequest.codes.add(response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0].code)

        return promoRequest
    }

    fun provideNoCurrentSelectedExpandedMerchantPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessAllExpanded()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, 0, true, emptyList())
        selectedPromoUiModel.uiState.isSelected = false
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideNoCurrentSelectedCollapsedMerchantPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessAllCollapsed()
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
        val response = provideGetPromoListResponseSuccessAllExpanded()
        val promoRequest = provideGetPromoListRequest()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
        promoRequest.orders.forEach {
            if (it.shopId == selectedPromo.shopId.toLong()) {
                it.codes.add(selectedPromo.code)
            }
        }

        return promoRequest
    }

    fun providePromoRequestWithSelectedCollapsedMerchantPromo(): PromoRequest {
        val response = provideGetPromoListResponseSuccessAllExpanded()
        val promoRequest = provideGetPromoListRequest()
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
        val selectedPromo = provideGetPromoListResponseSuccessAllExpanded().couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, 0, true, emptyList())
        selectedPromoUiModel.uiState.isSelected = true
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideCurrentSelectedExpandedMerchantPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val selectedPromo = provideGetPromoListResponseSuccessAllExpanded().couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, 0, true, emptyList())
        selectedPromoUiModel.uiState.isSelected = true
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideCurrentSelectedCollapsedGlobalPromoData(): ArrayList<Visitable<*>> {
        val response = provideGetPromoListResponseSuccessAllCollapsed()
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
        val response = provideGetPromoListResponseSuccessAllCollapsed()
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