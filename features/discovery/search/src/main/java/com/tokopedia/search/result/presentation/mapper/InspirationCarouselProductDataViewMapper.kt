package com.tokopedia.search.result.presentation.mapper

import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationCarouselProduct
import com.tokopedia.search.result.domain.model.SearchProductModel.ProductLabelGroup
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView

class InspirationCarouselProductDataViewMapper {

    fun convertToInspirationCarouselProductDataView(
            inspirationCarouselProduct: List<InspirationCarouselProduct>,
            productPosition: Int,
            inspirationCarouselType: String,
            layout: String,
            mapLabelGroupDataViewList: (List<ProductLabelGroup>) -> List<LabelGroupDataView>,
            optionTitle: String,
    ): List<InspirationCarouselDataView.Option.Product> {

        return inspirationCarouselProduct.mapIndexed { index, product ->
            InspirationCarouselDataView.Option.Product(
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
                    productPosition,
                    inspirationCarouselType,
                    product.ratingAverage,
                    mapLabelGroupDataViewList(product.labelGroupList),
                    layout,
                    product.originalPrice,
                    product.discountPercentage,
                    index + 1,
                    optionTitle
            )
        }
    }
}