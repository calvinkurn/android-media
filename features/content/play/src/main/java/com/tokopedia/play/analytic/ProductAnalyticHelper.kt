package com.tokopedia.play.analytic

import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel


/**
 * Created by mzennis on 20/04/21.
 */
class ProductAnalyticHelper(
    private val analytic: PlayAnalytic,
    private val newAnalytic: PlayNewAnalytic,
) {

    @TrackingField
    private val impressedProducts = mutableListOf<Pair<PlayProductUiModel.Product, Int>>()

    @TrackingField
    private val impressedVouchers = mutableListOf<MerchantVoucherUiModel>()

    private var sectionInfo: ProductSectionUiModel.Section = ProductSectionUiModel.Section.Empty

    fun trackImpressedProducts(
        products: Map<PlayProductUiModel.Product, Int>,
        section: ProductSectionUiModel.Section = ProductSectionUiModel.Section.Empty
    ) {
        if (products.isNotEmpty()) {
            impressedProducts.addAll(
                products.map { it.key to it.value }
            )
        }
        sectionInfo = section
    }

    fun trackImpressedVouchers(vouchers: List<MerchantVoucherUiModel>) {
        if (vouchers.isNotEmpty()) impressedVouchers.addAll(vouchers)
    }

    fun sendImpressedProductSheets() {
        sendImpressedPrivateVoucher()
    }

    /**
     * Send double tracker due to DA request
     */
    fun sendImpressedFeaturedProducts(partner: PartnerType) {
        analytic.impressFeaturedProducts(getFinalProducts())
        if(partner == PartnerType.TokoNow) newAnalytic.impressFeaturedProductNow(getFinalProducts())
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