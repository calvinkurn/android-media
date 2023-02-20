package com.tokopedia.tkpd.flashsale.presentation.manageproductlist.uimodel

import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleProductSubmissionProgress
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleProductSubmissionSseResult
import com.tokopedia.tkpd.flashsale.domain.entity.ProductSubmissionResult
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.CampaignDetailBottomSheetModel

sealed class FlashSaleManageProductListUiEffect {
    data class AddIconCampaignDetailBottomSheet(
        val campaignDetailBottomSheetData: CampaignDetailBottomSheetModel? = null
    ) : FlashSaleManageProductListUiEffect()

    object ShowToasterSuccessDelete : FlashSaleManageProductListUiEffect()

    data class ShowToasterErrorDelete(
        val throwable: Throwable
    ) : FlashSaleManageProductListUiEffect()

    data class ShowErrorGetReservedProductList(
        val throwable: Throwable
    ) : FlashSaleManageProductListUiEffect()

    data class ConfigSubmitButton(val isEnableSubmitButton: Boolean) :
        FlashSaleManageProductListUiEffect()

    data class OnProductSubmitted(
        val result: ProductSubmissionResult
    ) : FlashSaleManageProductListUiEffect()

    data class ShowErrorSubmitDiscountedProduct(
        val throwable: Throwable
    ) : FlashSaleManageProductListUiEffect()

    data class ShowErrorLoadNextReservedProductList(
        val throwable: Throwable
    ) : FlashSaleManageProductListUiEffect()

    object ShowCoachMarkOnFirstProductItem : FlashSaleManageProductListUiEffect()

    object CloseManageProductListPage : FlashSaleManageProductListUiEffect()

    object ShowSubmitButton : FlashSaleManageProductListUiEffect()

    object ClearProductList : FlashSaleManageProductListUiEffect()

    data class OnProductSseSubmissionProgress(
        val flashSaleProductSubmissionSseResult: FlashSaleProductSubmissionSseResult
    ) : FlashSaleManageProductListUiEffect()

    data class OnSuccessAcknowledgeProductSubmissionSse(
        val totalSubmittedProduct: Int
    ) : FlashSaleManageProductListUiEffect()

    object OnSseOpen : FlashSaleManageProductListUiEffect()

}
