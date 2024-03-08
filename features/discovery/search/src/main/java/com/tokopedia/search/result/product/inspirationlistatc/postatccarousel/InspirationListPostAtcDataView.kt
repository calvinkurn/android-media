package com.tokopedia.search.result.product.inspirationlistatc.postatccarousel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
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
            productAtc : Option.Product,
            inspirationPostAtc: SearchProductModel.InspirationCarouselData?,
        ): Visitable<*>?  {
            val mapper = InspirationCarouselProductDataViewMapper()
            val opt = inspirationPostAtc?.inspirationCarouselOptions?.get(0) ?: return null
            return  InspirationListPostAtcDataView(
                productAtc = productAtc,
                option = Option(
                    inspirationPostAtc.title,
                    opt.subtitle,
                    opt.iconSubtitle,
                    opt.url,
                    opt.applink,
                    opt.bannerImageUrl,
                    opt.bannerLinkUrl,
                    opt.bannerApplinkUrl,
                    opt.identifier,
                    mapper.convertToInspirationCarouselProductDataView(
                        opt.inspirationCarouselProducts,
                        inspirationPostAtc.position,
                        inspirationPostAtc.type,
                        inspirationPostAtc.layout,
                        { it.mapToLabelGroupDataViewList() },
                        opt.title,
                        inspirationPostAtc.title,
                        "",
                        "",
                        inspirationPostAtc.trackingOption.toIntOrZero()
                    ),
                    inspirationPostAtc.type,
                    inspirationPostAtc.layout,
                    inspirationPostAtc.position,
                    inspirationPostAtc.title,
                    inspirationPostAtc.position,
                    false,
                    if (inspirationPostAtc.type == SearchConstant.InspirationCarousel.TYPE_ANNOTATION_PRODUCT_COLOR_CHIPS) opt.meta else "",
                    if (inspirationPostAtc.type != SearchConstant.InspirationCarousel.TYPE_ANNOTATION_PRODUCT_COLOR_CHIPS) opt.meta else "",
                    opt.componentId,
                    inspirationPostAtc.trackingOption.toIntOrZero(),
                    "",
                    createInspirationCarouselCardButtonViewModel(opt),
                    InspirationCarouselDataView.Bundle.create(opt),
                    "",
                    "",
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
