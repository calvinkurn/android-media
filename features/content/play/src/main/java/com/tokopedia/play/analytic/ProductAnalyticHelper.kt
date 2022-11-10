package com.tokopedia.play.analytic

import com.tokopedia.play.ui.productsheet.adapter.ProductSheetAdapter
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

    //different product bcs it might be diff add new
    @TrackingField
    private val impressedProducts = mutableListOf<Pair<PlayProductUiModel.Product, Int>>()

    //For bottom sheet products
    @TrackingField
    private val impressedBottomSheet = mutableListOf<ProductSheetAdapter.Item.Product>()

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

    fun trackImpressedProductsBottomSheet(
        products : List<ProductSheetAdapter.Item.Product>
    ) {
        if (products.isEmpty()) return
        impressedBottomSheet.addAll(products)
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

    fun sendImpressedBottomSheet(partner: PartnerType){
        if(partner == PartnerType.TokoNow) newAnalytic.impressProductBottomSheetNow(impressedBottomSheet)
        else analytic.impressBottomSheetProduct(impressedBottomSheet)
        impressedBottomSheet.clear()
    }

    private fun sendImpressedPrivateVoucher() {
        val voucher = impressedVouchers.distinctBy { it.id }.firstOrNull { it.highlighted }
        voucher?.let { analytic.impressionPrivateVoucher(it) }
        impressedVouchers.clear()
    }

    private fun getFinalProducts() = impressedProducts

    private fun clearProducts() {
        impressedProducts.clear()
    }
}
