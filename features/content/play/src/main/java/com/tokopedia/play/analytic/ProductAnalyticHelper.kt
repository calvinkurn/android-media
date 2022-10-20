package com.tokopedia.play.analytic

import com.tokopedia.play.ui.toolbar.model.PartnerType
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

    /**
     * Send double tracker due to DA request
     */
    fun sendImpressedFeaturedProducts(partner: PartnerType) {
        analytic.impressFeaturedProducts(getFinalProducts())
        if(partner == PartnerType.TokoNow) newAnalytic.impressFeaturedProductNow(getFinalProducts())
        clearProducts()
    }

    private fun getFinalProducts() = impressedProducts.distinctBy { it.first.id }

    private fun clearProducts() {
        impressedProducts.clear()
    }
}