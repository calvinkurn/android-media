package com.tokopedia.play.analytic

import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel


/**
 * Created by mzennis on 20/04/21.
 */
class ProductAnalyticHelper(
        private val analytic: PlayAnalytic
) {

    @TrackingField
    private val impressedProducts = mutableListOf<Pair<PlayProductUiModel.Product, Int>>()

    @TrackingField
    private val impressedVouchers = mutableListOf<MerchantVoucherUiModel>()

    fun trackImpressedProducts(products: List<Pair<PlayProductUiModel.Product, Int>>) {
        if (products.isNotEmpty()) impressedProducts.addAll(products)
    }

    fun trackImpressedVouchers(vouchers: List<MerchantVoucherUiModel>) {
        if (vouchers.isNotEmpty()) impressedVouchers.addAll(vouchers)
    }

    fun sendImpressedProductSheets() {
        sendImpressedBottomSheetProducts()
        sendImpressedPrivateVoucher()
    }

    fun sendImpressedFeaturedProducts() {
        analytic.impressFeaturedProducts(getFinalProducts())
        clearProducts()
    }

    private fun sendImpressedBottomSheetProducts() {
        analytic.impressBottomSheetProducts(getFinalProducts())
        clearProducts()
    }

    private fun sendImpressedPrivateVoucher() {
        val voucher = impressedVouchers.distinctBy { it.id }.firstOrNull { it.highlighted }
        voucher?.let { analytic.impressionPrivateVoucher(it) }
        impressedVouchers.clear()
    }

    private fun getFinalProducts() = impressedProducts.distinctBy { it.first.id }

    private fun clearProducts() {
        impressedProducts.clear()
    }
}