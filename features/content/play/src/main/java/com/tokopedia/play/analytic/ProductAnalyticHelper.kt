package com.tokopedia.play.analytic

import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel


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

    private var sectionInfo: ProductSectionUiModel.Section = ProductSectionUiModel.Section.Empty

    fun trackImpressedProducts(products: List<Pair<PlayProductUiModel.Product, Int>>, section: ProductSectionUiModel.Section = ProductSectionUiModel.Section.Empty) {
        if (products.isNotEmpty()) {
            impressedProducts.addAll(products)
        }
        sectionInfo = section
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
        analytic.impressBottomSheetProducts(getFinalProducts(), sectionInfo)
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