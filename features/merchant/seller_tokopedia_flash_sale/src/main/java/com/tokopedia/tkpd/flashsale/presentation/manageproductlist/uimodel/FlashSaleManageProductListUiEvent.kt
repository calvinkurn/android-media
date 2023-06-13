package com.tokopedia.tkpd.flashsale.presentation.manageproductlist.uimodel

import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct

sealed class FlashSaleManageProductListUiEvent {

    data class GetReservedProductList(
        val reservationId: String,
        val page: Int
    ) : FlashSaleManageProductListUiEvent()

    data class GetCampaignDetailBottomSheet(
        val campaignId: String
    ) : FlashSaleManageProductListUiEvent()

    data class DeleteProductFromReserved(
        val productData: ReservedProduct.Product,
        val reservationId: String,
        val campaignId: String
    ) : FlashSaleManageProductListUiEvent()

    data class LoadNextReservedProduct(
        val reservationId: String,
        val page: Int
    ) : FlashSaleManageProductListUiEvent()

    object CheckShouldEnableButtonSubmit : FlashSaleManageProductListUiEvent()
    data class SubmitDiscountedProduct(
        val reservationId: String,
        val campaignId: String
    ) : FlashSaleManageProductListUiEvent()

    data class UpdateProductData(
        val productData: ReservedProduct.Product
    ) : FlashSaleManageProductListUiEvent()

}
