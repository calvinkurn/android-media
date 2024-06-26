package com.tokopedia.promocheckoutmarketplace

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.promocheckoutmarketplace.data.response.CouponListRecommendationResponse
import com.tokopedia.promocheckoutmarketplace.presentation.mapper.PromoCheckoutUiModelMapper
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.FragmentUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoInputUiModel
import com.tokopedia.purchase_platform.common.constant.PAGE_CART
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest

object GetPromoListDataProvider {

    private val GSON = Gson()
    private val UI_MODEL_MAPPER = PromoCheckoutUiModelMapper()
    private val FILE_UTIL = UnitTestFileUtils()

    fun provideGetPromoListRequest(): PromoRequest {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_request.json"), PromoRequest::class.java)
    }

    fun provideGetPromoListResponseSuccessAllExpanded(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_success_eligible_and_ineligible.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseSuccessWithBoPromo(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_success_with_bo_promo.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseSuccessWithSelectedPromoBoClashing(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_success_with_selected_promo_bo_clashing.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseSuccessWithBoPromoNotSelected(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_success_with_bo_promo_unselected.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseSuccessWithPreSelectedPromo(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_success_with_pre_selected_promo.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseSuccessAllEligible(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_success_all_eligible.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseSuccessAllIneligible(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_success_all_ineligible.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseApplyManualFailed(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_apply_manual_failed.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseApplyManualSuccess(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_apply_manual_success.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseError(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_error.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseEmptyStateEmpty(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_empty_state_empty.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseEmptyStateCouponListEmpty(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_empty_state_coupon_list_empty.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseEmptyStatePhoneVerification(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_empty_state_phone_verification.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseEmptyStateBlacklisted(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_empty_state_blacklisted.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseEmptyStateUnknown(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_empty_state_unknown.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseSuccessWithClashingData(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_success_with_clashing_data.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseBoPromoInfoDataComplete(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_success_bo_info_bottom_sheet_complete.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseBoPromoInfoDataIncomplete(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_success_bo_info_bottom_sheet_incomplete.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseBoPromoInfoDataEmpty(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_success_bo_info_bottom_sheet_empty.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseBoAndMvcSecondaryRecommended(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_list_response_success_bo_and_mvc_secondary_recommended.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideCouponListRecommendationWithMvcPrimaryPromoNotClashResponse(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/coupon_list_recommendation_with_mvc_primary_promo_not_clash_response.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideCouponListRecommendationWithMvcSecondaryPromoNotClashResponse(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/coupon_list_recommendation_with_mvc_secondary_promo_not_clash_response.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideCouponListRecommendationWithMvcSecondaryPromoClashResponse(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/coupon_list_recommendation_with_mvc_secondary_promo_clash_response.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideCouponListRecommendationWithMultipleOrderMvcSecondaryResponse(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/coupon_list_recommendation_with_multiple_order_mvc_secondary_promo_response.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideCouponListRecommendationWithSecondaryAndPrimaryRecommendedResponse(): CouponListRecommendationResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/coupon_list_recommendation_with_secondary_and_primary_recommended_response.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideNoCurrentSelectedExpandedGlobalPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessAllExpanded()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val recommendedPromo = response.couponListRecommendation.data.promoRecommendation.codes
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList(), recommendedPromo)
        selectedPromoUiModel.uiState.isSelected = false
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideNoCurrentSelectedExpandedGlobalPromoDataWithHeader(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessAllExpanded()
        val section = response.couponListRecommendation.data.couponSections[0]
        val subSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val recommendedPromo = response.couponListRecommendation.data.promoRecommendation.codes
        val selectedPromoHeaderUiModel = UI_MODEL_MAPPER.mapPromoListHeaderUiModel(subSection, section, 0, true)
        promoListUiModelList.add(selectedPromoHeaderUiModel)
        val unSelectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val unSelectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val unSelectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val unSelectedPromoUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(unSelectedPromo, unSelectedPromoSubSection, unSelectedPromoSection, 0, emptyList(), recommendedPromo)
        unSelectedPromoUiModel.uiState.isSelected = false
        promoListUiModelList.add(unSelectedPromoUiModel)
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[1]
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList(), recommendedPromo)
        selectedPromoUiModel.uiState.isSelected = true
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun providePromoRequestWithSelectedExpandedGlobalPromo(): PromoRequest {
        val response = provideGetPromoListResponseSuccessAllExpanded()
        val promoRequest = provideGetPromoListRequest()
        promoRequest.codes.add(response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0].code)

        return promoRequest
    }

    fun provideNoCurrentSelectedExpandedMerchantPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessAllExpanded()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
        val recommendedPromo = response.couponListRecommendation.data.promoRecommendation.codes
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[1]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList(), recommendedPromo)
        selectedPromoUiModel.uiState.isSelected = false
        promoListUiModelList.add(selectedPromoUiModel)

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

    fun provideCurrentSelectedExpandedGlobalPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessAllExpanded()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val recommendedPromo = response.couponListRecommendation.data.promoRecommendation.codes
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList(), recommendedPromo)
        selectedPromoUiModel.uiState.isSelected = true
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideCurrentSelectedExpandedMerchantPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessAllExpanded()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
        val recommendedPromo = response.couponListRecommendation.data.promoRecommendation.codes
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[1]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList(), recommendedPromo)
        selectedPromoUiModel.uiState.isSelected = true
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideCurrentUnSelectedExpandedGlobalAndMerchantPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()

        val response = provideGetPromoListResponseSuccessAllExpanded()
        val recommendedPromo = response.couponListRecommendation.data.promoRecommendation.codes
        val selectedPromoGlobal = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val selectedPromoGlobalSubSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoGlobalSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoGlobalUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(selectedPromoGlobal, selectedPromoGlobalSubSection, selectedPromoGlobalSection, 0, emptyList(), recommendedPromo)
        selectedPromoGlobalUiModel.uiState.isSelected = false
        promoListUiModelList.add(selectedPromoGlobalUiModel)

        val selectedPromoMerchant = response.couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
        val selectedPromoMerchantSubSection = response.couponListRecommendation.data.couponSections[0].subSections[1]
        val selectedPromoMerchantSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoMerchantUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(selectedPromoMerchant, selectedPromoMerchantSubSection, selectedPromoMerchantSection, 0, emptyList(), recommendedPromo)
        selectedPromoMerchantUiModel.uiState.isSelected = false
        promoListUiModelList.add(selectedPromoMerchantUiModel)

        return promoListUiModelList
    }

    fun provideCurrentSelectedExpandedGlobalPromoDataWithHeader(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessAllExpanded()
        val section = response.couponListRecommendation.data.couponSections[0]
        val subSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoHeaderUiModel = UI_MODEL_MAPPER.mapPromoListHeaderUiModel(subSection, section, 0, true)
        promoListUiModelList.add(selectedPromoHeaderUiModel)
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val recommendedPromo = response.couponListRecommendation.data.promoRecommendation.codes
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList(), recommendedPromo)
        selectedPromoUiModel.uiState.isSelected = true
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideCurrentDisabledExpandedGlobalPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()

        val response = provideGetPromoListResponseSuccessAllExpanded()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val recommendedPromo = response.couponListRecommendation.data.promoRecommendation.codes
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList(), recommendedPromo)

        selectedPromoUiModel.uiState.isSelected = false
        selectedPromoUiModel.uiState.isDisabled = true
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideCurrentDisabledExpandedMerchantPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()

        val response = provideGetPromoListResponseSuccessAllExpanded()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
        val recommendedPromo = response.couponListRecommendation.data.promoRecommendation.codes
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[1]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList(), recommendedPromo)

        selectedPromoUiModel.uiState.isSelected = false
        selectedPromoUiModel.uiState.isDisabled = true
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideExpandedMerchantParentNotEligiblePromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()

        val response = provideGetPromoListResponseSuccessAllExpanded()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
        val recommendedPromo = response.couponListRecommendation.data.promoRecommendation.codes
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[1]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList(), recommendedPromo)

        selectedPromoUiModel.uiState.isParentEnabled = false
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideCurrentNoSelectedPromoListResponseWithClashingData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessWithClashingData()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val recommendedPromo = response.couponListRecommendation.data.promoRecommendation.codes
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList(), recommendedPromo)
        selectedPromoUiModel.uiState.isSelected = false
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideCurrentSelectedPromoListResponseWithClashingData(): ArrayList<Visitable<*>> {
        val potentialClashingPromo = "NPNVTU0Z"
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessWithClashingData()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val recommendedPromo = response.couponListRecommendation.data.promoRecommendation.codes
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, listOf(potentialClashingPromo), recommendedPromo)
        selectedPromoUiModel.uiState.isSelected = true
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun providePromoListWithClashingBoPromo(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessWithBoPromo()
        val recommendedPromo = response.couponListRecommendation.data.promoRecommendation.codes
        val section = response.couponListRecommendation.data.couponSections[0]
        val subSectionWithBoClashingPromo = response.couponListRecommendation.data.couponSections[0].subSections[2]
        val selectedPromoHeaderUiModel = UI_MODEL_MAPPER.mapPromoListHeaderUiModel(subSectionWithBoClashingPromo, section, 0, true)
        promoListUiModelList.add(selectedPromoHeaderUiModel)
        val boClashingPromo = response.couponListRecommendation.data.couponSections[0].subSections[2].coupons[0]
        val boClashingPromoUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(boClashingPromo, subSectionWithBoClashingPromo, section, 0, emptyList(), recommendedPromo)
        promoListUiModelList.add(boClashingPromoUiModel)

        return promoListUiModelList
    }

    fun providePromoListWithMultipleClashingBoPromo(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessWithBoPromo()

        val recommendedPromo = response.couponListRecommendation.data.promoRecommendation.codes
        val section = response.couponListRecommendation.data.couponSections[0]
        val subSectionWithBoClashingPromo = response.couponListRecommendation.data.couponSections[0].subSections[2]
        val selectedPromoHeaderUiModel = UI_MODEL_MAPPER.mapPromoListHeaderUiModel(subSectionWithBoClashingPromo, section, 0, true)
        promoListUiModelList.add(selectedPromoHeaderUiModel)
        val boClashingPromo = response.couponListRecommendation.data.couponSections[0].subSections[2].coupons[0]
        val boClashingPromoUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(boClashingPromo, subSectionWithBoClashingPromo, section, 0, emptyList(), recommendedPromo)
        promoListUiModelList.add(boClashingPromoUiModel)

        val otherSubSectionWithBoClashingPromo = response.couponListRecommendation.data.couponSections[0].subSections[3]
        val otherPromoHeaderUiModel = UI_MODEL_MAPPER.mapPromoListHeaderUiModel(otherSubSectionWithBoClashingPromo, section, 1, true)
        promoListUiModelList.add(otherPromoHeaderUiModel)
        val otherBoClashingPromo = response.couponListRecommendation.data.couponSections[0].subSections[3].coupons[0]
        val otherBoClashingPromoUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(otherBoClashingPromo, otherSubSectionWithBoClashingPromo, section, 1, emptyList(), recommendedPromo)
        promoListUiModelList.add(otherBoClashingPromoUiModel)

        return promoListUiModelList
    }

    fun providePromoListWithBoPlusAsRecommendedPromo(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessWithBoPromo()
        val recommendedPromo = response.couponListRecommendation.data.promoRecommendation.codes
        val section = response.couponListRecommendation.data.couponSections[0]
        val subSectionBoPlus = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoHeaderUiModel = UI_MODEL_MAPPER.mapPromoListHeaderUiModel(subSectionBoPlus, section, 0, true)
        promoListUiModelList.add(selectedPromoHeaderUiModel)
        val boPlusPromo = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val boPlusPromoUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(boPlusPromo, subSectionBoPlus, section, 0, emptyList(), recommendedPromo)
        boPlusPromoUiModel.uiState.isRecommended = true
        promoListUiModelList.add(boPlusPromoUiModel)

        return promoListUiModelList
    }

    fun providePromoListWithClashingSectionRecommendedPromo(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessWithBoPromo()
        val recommendedPromo = response.couponListRecommendation.data.promoRecommendation.codes
        val section = response.couponListRecommendation.data.couponSections[0]
        val subsection = response.couponListRecommendation.data.couponSections[0].subSections[2]
        val selectedPromoHeaderUiModel = UI_MODEL_MAPPER.mapPromoListHeaderUiModel(subsection, section, 0, true)
        promoListUiModelList.add(selectedPromoHeaderUiModel)
        val clashingPromo = response.couponListRecommendation.data.couponSections[0].subSections[2].coupons[0]
        val clashingPromoUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(clashingPromo, subsection, section, 0, emptyList(), recommendedPromo)
        promoListUiModelList.add(clashingPromoUiModel)
        val selectedRecommendedPromo = response.couponListRecommendation.data.couponSections[0].subSections[2].coupons[1]
        val recommendedPromoUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(selectedRecommendedPromo, subsection, section, 0, emptyList(), recommendedPromo)
        recommendedPromoUiModel.uiState.isRecommended = true
        promoListUiModelList.add(recommendedPromoUiModel)

        return promoListUiModelList
    }

    fun providePromoListGlobalParentNotEnabled(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessAllIneligible()
        val recommendedPromo = response.couponListRecommendation.data.promoRecommendation.codes
        val section = response.couponListRecommendation.data.couponSections[0]
        val subsection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoHeaderUiModel = UI_MODEL_MAPPER.mapPromoListHeaderUiModel(subsection, section, 0, false)
        promoListUiModelList.add(selectedPromoHeaderUiModel)
        val coupon = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val couponPromoUiModel = UI_MODEL_MAPPER.mapPromoListItemUiModel(coupon, subsection, section, 0, emptyList(), recommendedPromo)
        promoListUiModelList.add(couponPromoUiModel)

        return promoListUiModelList
    }

    fun providePromoInputData(): PromoInputUiModel {
        return UI_MODEL_MAPPER.mapPromoInputUiModel()
    }

    fun provideFragmentData(): FragmentUiModel {
        return UI_MODEL_MAPPER.mapFragmentUiModel(PAGE_CART, "Error message")
    }
}
