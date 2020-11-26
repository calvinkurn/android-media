package com.tokopedia.search.result.presentation.model

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.adapter.InspirationCarouselOptionTypeFactory
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

class InspirationCarouselViewModel(
        val title: String = "",
        val type: String = "",
        val position: Int = 0,
        val layout: String = "",
        val options: List<Option> = listOf()
) : Visitable<ProductListTypeFactory> {

    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }

    class Option(
            val title: String = "",
            val url: String = "",
            val applink: String = "",
            val product: List<Product> = listOf(),
            val inspirationCarouselType: String = "",
            val layout: String = ""
    ): Visitable<InspirationCarouselOptionTypeFactory>{

        override fun type(typeFactory: InspirationCarouselOptionTypeFactory): Int {
            return typeFactory.type(layout)
        }

        class Product(
            val id: String = "",
            val name: String = "",
            val price: Int = 0,
            val priceStr: String = "",
            val imgUrl: String = "",
            val rating: Int = 0,
            val countReview: Int = 0,
            val url: String = "",
            val applink: String = "",
            val description: List<String> = listOf(),
            val optionPosition: Int = 0,
            val inspirationCarouselType: String = ""
        ){
            fun getInspirationCarouselListProductAsObjectDataLayer(): Any {
                return DataLayer.mapOf(
                        "name", name,
                        "id", id,
                        "price", price,
                        "brand", "none / other",
                        "category", "none / other",
                        "variant", "none / other",
                        "list", "/search - carousel",
                        "position", optionPosition,
                        "attribution", "none / other"
                )
            }

            fun getInspirationCarouselInfoProductAsObjectDataLayer(): Any {
                return DataLayer.mapOf(
                        "id", id,
                        "name", "/search - carousel",
                        "creative", name,
                        "position", optionPosition,
                        "category", "none / other"
                )
            }

            fun getInspirationCarouselListProductImpressionAsObjectDataLayer(): Any {
                return DataLayer.mapOf(
                        "name", name,
                        "id", id,
                        "price", price,
                        "brand", "none / other",
                        "category", "none / other",
                        "variant", "none / other",
                        "list", "/search - carousel",
                        "position", optionPosition
                )
            }
        }
    }
}


