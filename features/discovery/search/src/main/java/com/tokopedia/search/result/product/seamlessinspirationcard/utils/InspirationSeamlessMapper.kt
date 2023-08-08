package com.tokopedia.search.result.product.seamlessinspirationcard.utils

import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.InspirationProductItemDataView

object InspirationSeamlessMapper {

    fun convertToInspirationList(
        inspirationOptions: List<InspirationCarouselDataView.Option>,
        externalReference: String
    ): Triple<List<InspirationKeywordDataView>, List<InspirationProductItemDataView>, Boolean> {
        val listOfInspirationKeywordItems = mutableListOf<InspirationKeywordDataView>()
        val listOfOptionProductItems = mutableListOf<InspirationProductItemDataView>()
        var isOneOrMoreItemIsEmptyImage = false
        inspirationOptions.forEach { option ->
            listOfInspirationKeywordItems.add(
                option.convertToInspirationKeywordDataView()
            )

            if (option.bannerImageUrl.isEmpty()) {
                isOneOrMoreItemIsEmptyImage = true
            }

            listOfOptionProductItems.addAll(
                option.getProductAndConvertToInspirationProductDataView(
                    externalReference
                )
            )
        }
        return Triple(listOfInspirationKeywordItems, listOfOptionProductItems, isOneOrMoreItemIsEmptyImage)
    }

    private fun InspirationCarouselDataView.Option.convertToInspirationKeywordDataView() =
        InspirationKeywordDataView.create(this)

    private fun InspirationCarouselDataView.Option.getProductAndConvertToInspirationProductDataView(
        externalReference: String
    ): List<InspirationProductItemDataView> {
        val listOfOptionProductItems = mutableListOf<InspirationProductItemDataView>()
        val products = this.product
        products.forEachIndexed { index, product ->
            listOfOptionProductItems.add(
                product.convertToInspirationProduct(
                    option = this,
                    index = index,
                    externalReference = externalReference
                )
            )
        }
        return listOfOptionProductItems
    }

    private fun InspirationCarouselDataView.Option.Product.convertToInspirationProduct(
        option: InspirationCarouselDataView.Option,
        index: Int,
        externalReference: String = ""
    ) = InspirationProductItemDataView.create(
            product = this,
            option = option,
            index = index,
            externalReference
        )
}
