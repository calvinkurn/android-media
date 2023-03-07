package com.tokopedia.promocheckoutmarketplace

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.promocheckoutmarketplace.data.response.CouponListRecommendationResponse
import com.tokopedia.promocheckoutmarketplace.presentation.mapper.PromoCheckoutUiModelMapper
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.FragmentUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoInputUiModel
import com.tokopedia.purchase_platform.common.constant.PAGE_CART
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest

object GetPromoListDataProvider {

    private val gson = Gson()
    private val uiModelmapper = PromoCheckoutUiModelMapper()
    private val fileUtil = UnitTestFileUtils()

    fun provideGetPromoListRequest(): PromoRequest {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_request.json"), PromoRequest::class.java)
    }

    fun provideGetPromoListResponseSuccessAllExpanded(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_success_eligible_and_ineligible.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseSuccessWithBoPromo(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_success_with_bo_promo.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseSuccessWithSelectedPromoBoClashing(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_success_with_selected_promo_bo_clashing.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseSuccessWithBoPromoNotSelected(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_success_with_bo_promo_unselected.json"), CouponListRecommendationResponse::class.java)
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

    fun provideGetPromoListResponseApplyManualSuccess(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_apply_manual_success.json"), CouponListRecommendationResponse::class.java)
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

    fun provideGetPromoListResponseSuccessWithClashingData(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_success_with_clashing_data.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseBoPromoInfoDataComplete(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_success_bo_info_bottom_sheet_complete.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseBoPromoInfoDataIncomplete(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_success_bo_info_bottom_sheet_incomplete.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideGetPromoListResponseBoPromoInfoDataEmpty(): CouponListRecommendationResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_list_response_success_bo_info_bottom_sheet_empty.json"), CouponListRecommendationResponse::class.java)
    }

    fun provideNoCurrentSelectedExpandedGlobalPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessAllExpanded()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList())
        selectedPromoUiModel.uiState.isSelected = false
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideNoCurrentSelectedExpandedGlobalPromoDataWithHeader(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessAllExpanded()
        val section = response.couponListRecommendation.data.couponSections[0]
        val subSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoHeaderUiModel = uiModelmapper.mapPromoListHeaderUiModel(subSection, section, 0, true)
        promoListUiModelList.add(selectedPromoHeaderUiModel)
        val unSelectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val unSelectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val unSelectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val unSelectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(unSelectedPromo, unSelectedPromoSubSection, unSelectedPromoSection, 0, emptyList())
        unSelectedPromoUiModel.uiState.isSelected = false
        promoListUiModelList.add(unSelectedPromoUiModel)
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[1]
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList())
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
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[1]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList())
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
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList())
        selectedPromoUiModel.uiState.isSelected = true
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideCurrentSelectedExpandedMerchantPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessAllExpanded()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[1]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList())
        selectedPromoUiModel.uiState.isSelected = true
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideCurrentUnSelectedExpandedGlobalAndMerchantPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()

        val response = provideGetPromoListResponseSuccessAllExpanded()
        val selectedPromoGlobal = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val selectedPromoGlobalSubSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoGlobalSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoGlobalUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromoGlobal, selectedPromoGlobalSubSection, selectedPromoGlobalSection, 0, emptyList())
        selectedPromoGlobalUiModel.uiState.isSelected = false
        promoListUiModelList.add(selectedPromoGlobalUiModel)

        val selectedPromoMerchant = response.couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
        val selectedPromoMerchantSubSection = response.couponListRecommendation.data.couponSections[0].subSections[1]
        val selectedPromoMerchantSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoMerchantUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromoMerchant, selectedPromoMerchantSubSection, selectedPromoMerchantSection, 0, emptyList())
        selectedPromoMerchantUiModel.uiState.isSelected = false
        promoListUiModelList.add(selectedPromoMerchantUiModel)

        return promoListUiModelList
    }

    fun provideCurrentSelectedExpandedGlobalPromoDataWithHeader(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessAllExpanded()
        val section = response.couponListRecommendation.data.couponSections[0]
        val subSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoHeaderUiModel = uiModelmapper.mapPromoListHeaderUiModel(subSection, section, 0, true)
        promoListUiModelList.add(selectedPromoHeaderUiModel)
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList())
        selectedPromoUiModel.uiState.isSelected = true
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideCurrentDisabledExpandedGlobalPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()

        val response = provideGetPromoListResponseSuccessAllExpanded()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList())

        selectedPromoUiModel.uiState.isSelected = false
        selectedPromoUiModel.uiState.isDisabled = true
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideCurrentDisabledExpandedMerchantPromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()

        val response = provideGetPromoListResponseSuccessAllExpanded()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[1]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList())

        selectedPromoUiModel.uiState.isSelected = false
        selectedPromoUiModel.uiState.isDisabled = true
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideExpandedMerchantParentNotEligiblePromoData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()

        val response = provideGetPromoListResponseSuccessAllExpanded()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[1].coupons[0]
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[1]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList())

        selectedPromoUiModel.uiState.isParentEnabled = false
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideCurrentNoSelectedPromoListResponseWithClashingData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessWithClashingData()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList())
        selectedPromoUiModel.uiState.isSelected = false
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun provideCurrentSelectedPromoListResponseWithClashingData(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessWithClashingData()
        val selectedPromo = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val selectedPromoSubSection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoSection = response.couponListRecommendation.data.couponSections[0]
        val selectedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(selectedPromo, selectedPromoSubSection, selectedPromoSection, 0, emptyList())
        selectedPromoUiModel.uiState.isSelected = true
        promoListUiModelList.add(selectedPromoUiModel)

        return promoListUiModelList
    }

    fun providePromoListWithClashingBoPromo(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessWithBoPromo()
        val section = response.couponListRecommendation.data.couponSections[0]
        val subSectionWithBoClashingPromo = response.couponListRecommendation.data.couponSections[0].subSections[2]
        val selectedPromoHeaderUiModel = uiModelmapper.mapPromoListHeaderUiModel(subSectionWithBoClashingPromo, section, 0, true)
        promoListUiModelList.add(selectedPromoHeaderUiModel)
        val boClashingPromo = response.couponListRecommendation.data.couponSections[0].subSections[2].coupons[0]
        val boClashingPromoUiModel = uiModelmapper.mapPromoListItemUiModel(boClashingPromo, subSectionWithBoClashingPromo, section, 0, emptyList())
        promoListUiModelList.add(boClashingPromoUiModel)

        return promoListUiModelList
    }

    fun providePromoListWithMultipleClashingBoPromo(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessWithBoPromo()

        val section = response.couponListRecommendation.data.couponSections[0]
        val subSectionWithBoClashingPromo = response.couponListRecommendation.data.couponSections[0].subSections[2]
        val selectedPromoHeaderUiModel = uiModelmapper.mapPromoListHeaderUiModel(subSectionWithBoClashingPromo, section, 0, true)
        promoListUiModelList.add(selectedPromoHeaderUiModel)
        val boClashingPromo = response.couponListRecommendation.data.couponSections[0].subSections[2].coupons[0]
        val boClashingPromoUiModel = uiModelmapper.mapPromoListItemUiModel(boClashingPromo, subSectionWithBoClashingPromo, section, 0, emptyList())
        promoListUiModelList.add(boClashingPromoUiModel)

        val otherSubSectionWithBoClashingPromo = response.couponListRecommendation.data.couponSections[0].subSections[3]
        val otherPromoHeaderUiModel = uiModelmapper.mapPromoListHeaderUiModel(otherSubSectionWithBoClashingPromo, section, 1, true)
        promoListUiModelList.add(otherPromoHeaderUiModel)
        val otherBoClashingPromo = response.couponListRecommendation.data.couponSections[0].subSections[3].coupons[0]
        val otherBoClashingPromoUiModel = uiModelmapper.mapPromoListItemUiModel(otherBoClashingPromo, otherSubSectionWithBoClashingPromo, section, 1, emptyList())
        promoListUiModelList.add(otherBoClashingPromoUiModel)

        return promoListUiModelList
    }

    fun providePromoListWithBoPlusAsRecommendedPromo(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessWithBoPromo()
        val section = response.couponListRecommendation.data.couponSections[0]
        val subSectionBoPlus = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoHeaderUiModel = uiModelmapper.mapPromoListHeaderUiModel(subSectionBoPlus, section, 0, true)
        promoListUiModelList.add(selectedPromoHeaderUiModel)
        val boPlusPromo = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val boPlusPromoUiModel = uiModelmapper.mapPromoListItemUiModel(boPlusPromo, subSectionBoPlus, section, 0, emptyList())
        boPlusPromoUiModel.uiState.isRecommended = true
        promoListUiModelList.add(boPlusPromoUiModel)

        return promoListUiModelList
    }

    fun providePromoListWithClashingSectionRecommendedPromo(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessWithBoPromo()
        val section = response.couponListRecommendation.data.couponSections[0]
        val subsection = response.couponListRecommendation.data.couponSections[0].subSections[2]
        val selectedPromoHeaderUiModel = uiModelmapper.mapPromoListHeaderUiModel(subsection, section, 0, true)
        promoListUiModelList.add(selectedPromoHeaderUiModel)
        val clashingPromo = response.couponListRecommendation.data.couponSections[0].subSections[2].coupons[0]
        val clashingPromoUiModel = uiModelmapper.mapPromoListItemUiModel(clashingPromo, subsection, section, 0, emptyList())
        promoListUiModelList.add(clashingPromoUiModel)
        val recommendedPromo = response.couponListRecommendation.data.couponSections[0].subSections[2].coupons[1]
        val recommendedPromoUiModel = uiModelmapper.mapPromoListItemUiModel(recommendedPromo, subsection, section, 0, emptyList())
        recommendedPromoUiModel.uiState.isRecommended = true
        promoListUiModelList.add(recommendedPromoUiModel)

        return promoListUiModelList
    }

    fun providePromoListGlobalParentNotEnabled(): ArrayList<Visitable<*>> {
        val promoListUiModelList = ArrayList<Visitable<*>>()
        val response = provideGetPromoListResponseSuccessAllIneligible()
        val section = response.couponListRecommendation.data.couponSections[0]
        val subsection = response.couponListRecommendation.data.couponSections[0].subSections[0]
        val selectedPromoHeaderUiModel = uiModelmapper.mapPromoListHeaderUiModel(subsection, section, 0, false)
        promoListUiModelList.add(selectedPromoHeaderUiModel)
        val coupon = response.couponListRecommendation.data.couponSections[0].subSections[0].coupons[0]
        val couponPromoUiModel = uiModelmapper.mapPromoListItemUiModel(coupon, subsection, section, 0, emptyList())
        promoListUiModelList.add(couponPromoUiModel)

        return promoListUiModelList
    }

    fun providePromoInputData(): PromoInputUiModel {
        return uiModelmapper.mapPromoInputUiModel()
    }

    fun provideFragmentData(): FragmentUiModel {
        return uiModelmapper.mapFragmentUiModel(PAGE_CART, "Error message")
    }

}