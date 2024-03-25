package com.tokopedia.search.result.product.inspirationlistatc.postatccarousel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.TYPE_ANNOTATION_PRODUCT_COLOR_CHIPS
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.byteio.ByteIOTrackingData
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView.Option
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselProductDataViewMapper

data class InspirationListPostAtcDataView(
    val productAtc : Option.Product = Option.Product(),
    val option: Option = Option(),
    val type: String = "",
    val isVisible: Boolean = true
) : Visitable<ProductListTypeFactory> {
    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }

    object InspirationListPostAtcDataViewMapper {
        fun convertToInspirationListPostAtcDataView (
            byteIOTrackingData: ByteIOTrackingData,
            productDataView: Option.Product,
            inspirationPostAtc: SearchProductModel.InspirationCarouselData?,
        ): Visitable<*>?  {
            val mapper = InspirationCarouselProductDataViewMapper()
            val optionModel = inspirationPostAtc?.inspirationCarouselOptions?.get(0) ?: return null
            return InspirationListPostAtcDataView(
                productAtc = productDataView,
                option = Option(
                    inspirationPostAtc.title,
                    optionModel.subtitle,
                    optionModel.iconSubtitle,
                    optionModel.url,
                    optionModel.applink,
                    optionModel.bannerImageUrl,
                    optionModel.bannerLinkUrl,
                    optionModel.bannerApplinkUrl,
                    optionModel.identifier,
                    mapper.convertToInspirationCarouselProductDataView(
                        inspirationCarouselProduct = optionModel.inspirationCarouselProducts,
                        productPosition = inspirationPostAtc.position,
                        inspirationCarouselType = inspirationPostAtc.type,
                        layout = inspirationPostAtc.layout,
                        mapLabelGroupDataViewList = { it.mapToLabelGroupDataViewList() },
                        optionTitle = optionModel.title,
                        carouselTitle = inspirationPostAtc.title,
                        dimension90 = "",
                        externalReference = "",
                        trackingOption = inspirationPostAtc.trackingOption.toIntOrZero(),
                        byteIOTrackingData = byteIOTrackingData,
                    ),
                    inspirationPostAtc.type,
                    inspirationPostAtc.layout,
                    inspirationPostAtc.position,
                    inspirationPostAtc.title,
                    inspirationPostAtc.position,
                    false,
                    if (inspirationPostAtc.type == TYPE_ANNOTATION_PRODUCT_COLOR_CHIPS) optionModel.meta else "",
                    if (inspirationPostAtc.type != TYPE_ANNOTATION_PRODUCT_COLOR_CHIPS) optionModel.meta else "",
                    optionModel.componentId,
                    inspirationPostAtc.trackingOption.toIntOrZero(),
                    "",
                    createInspirationCarouselCardButtonViewModel(optionModel),
                    InspirationCarouselDataView.Bundle.create(optionModel),
                    "",
                    "",
                    byteIOTrackingData,
                ),
                type = inspirationPostAtc.type
            )
        }

        private fun List<SearchProductModel.ProductLabelGroup>.mapToLabelGroupDataViewList() =
            this.map { labelGroupModel ->
                LabelGroupDataView(
                    labelGroupModel.position,
                    labelGroupModel.type,
                    labelGroupModel.title,
                    labelGroupModel.url
                )
            }

        private fun createInspirationCarouselCardButtonViewModel(
            option: SearchProductModel.InspirationCarouselOption,
        ): InspirationCarouselDataView.CardButton {
            return InspirationCarouselDataView.CardButton(
                option.cardButton.title,
                option.cardButton.applink,
            )
        }
    }

}
