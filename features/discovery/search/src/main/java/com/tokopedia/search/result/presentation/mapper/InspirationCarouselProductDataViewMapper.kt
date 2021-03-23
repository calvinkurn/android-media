package com.tokopedia.search.result.presentation.mapper

import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationCarouselProduct
import com.tokopedia.search.result.domain.model.SearchProductModel.ProductLabelGroup
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel

class InspirationCarouselProductDataViewMapper {

    fun convertToInspirationCarouselProductDataView(
            inspirationCarouselProduct: List<InspirationCarouselProduct>,
            position: Int,
            inspirationCarouselType: String,
            layout: String,
            mapLabelGroupDataViewList: (List<ProductLabelGroup>) -> List<LabelGroupViewModel>,
    ): List<InspirationCarouselViewModel.Option.Product> {

        return inspirationCarouselProduct.map { product ->
            InspirationCarouselViewModel.Option.Product(
                    product.id,
                    product.name,
                    product.price,
                    product.priceStr,
                    product.imgUrl,
                    product.rating,
                    product.countReview,
                    product.url,
                    product.applink,
                    product.description,
                    position,
                    inspirationCarouselType,
                    product.ratingAverage,
                    mapLabelGroupDataViewList(product.labelGroupList),
                    layout,
                    product.originalPrice,
                    product.discountPercentage
            )
        }
    }
}