package com.tokopedia.search.result.product.semlessproduct.utils

import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.semlessproduct.seamlesskeywordoptions.InspirationKeywordDataView
import com.tokopedia.search.result.product.semlessproduct.seamlessproduct.InspirationProductItemDataView

object InspirationSeamlessMapper {

    fun convertToInspirationList(
        inspirationOptions: List<InspirationCarouselDataView.Option>,
        type: String,
        externalReference: String
    ):
        Pair<List<InspirationKeywordDataView>, List<InspirationProductItemDataView>> {
        val listOfInspirationKeywordItems = mutableListOf<InspirationKeywordDataView>()
        val listOfOptionProductItems = mutableListOf<InspirationProductItemDataView>()
        inspirationOptions.map { option ->
            listOfInspirationKeywordItems.add(
                option.convertToInspirationKeywordDataView(type)
            )
            listOfOptionProductItems.addAll(option.getTopThreeOfInspirationProduct(externalReference))
        }
        return Pair(listOfInspirationKeywordItems, listOfOptionProductItems)
    }

    private fun InspirationCarouselDataView.Option.convertToInspirationKeywordDataView(type: String) =
        InspirationKeywordDataView.create(
            this,
            type,
        )

    private fun InspirationCarouselDataView.Option.getTopThreeOfInspirationProduct(externalReference : String): List<InspirationProductItemDataView> {
        val listOfOptionProductItems = mutableListOf<InspirationProductItemDataView>()
        this.product.mapIndexed { index, product ->
            if (index < MAXIMUM_INSPIRATION_PRODUCT_ITEM) {
                listOfOptionProductItems.add(
                    product.convertToInspirationProduct(
                        option = this,
                        index = index
                    )
                )
            }
        }
        return listOfOptionProductItems
    }

    private fun InspirationCarouselDataView.Option.Product.convertToInspirationProduct(
        option: InspirationCarouselDataView.Option,
        index: Int,
        externalReference: String = ""
    ) =
        InspirationProductItemDataView.create(
            product = this,
            option = option,
            index = index,
            externalReference
        )
}
