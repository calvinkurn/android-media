package com.tokopedia.search.result.product.seamlessinspirationcard.utils

import com.tokopedia.search.result.product.deduplication.Deduplication
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView.Option
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.InspirationProductItemDataView

object InspirationSeamlessMapper {

    fun convertToInspirationList(
        inspirationOptions: List<Option>,
        externalReference: String,
        deduplication: Deduplication,
    ): Triple<List<InspirationKeywordDataView>, List<InspirationProductItemDataView>, Boolean> {
        val listOfInspirationKeywordItems = mutableListOf<InspirationKeywordDataView>()
        val listOfOptionProductItems = mutableListOf<InspirationProductItemDataView>()
        var isOneOrMoreItemIsEmptyImage = false
        inspirationOptions.forEach { option ->
            listOfInspirationKeywordItems.add(InspirationKeywordDataView.create(option))

            if (option.bannerImageUrl.isEmpty())
                isOneOrMoreItemIsEmptyImage = true

            listOfOptionProductItems.addAll(
                convertToInspirationProductDataView(option, externalReference, deduplication)
            )
        }
        return Triple(listOfInspirationKeywordItems, listOfOptionProductItems, isOneOrMoreItemIsEmptyImage)
    }

    fun convertToInspirationProductDataView(
        option: Option,
        externalReference: String,
        deduplication: Deduplication,
    ) = deduplication.removeDuplicate(option.product).mapIndexed { index, product ->
            InspirationProductItemDataView.create(
                product = product,
                option = option,
                index = index,
                externalReference = externalReference
            )
        }
}
