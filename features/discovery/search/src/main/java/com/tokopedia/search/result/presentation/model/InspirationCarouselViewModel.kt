package com.tokopedia.search.result.presentation.model

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

class InspirationCarouselViewModel(
        var title: String = "",
        var type: String = "",
        var position: Int = 0,
        var options: List<Option> = listOf()
) : Visitable<ProductListTypeFactory> {

    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }

    class Option(
            var title: String = "",
            var url: String = "",
            var applink: String = "",
            var product: List<Product> = listOf(),
            var inspirationCarouselType: String = ""
    ){
        class Product(
            var id: String = "",
            var name: String = "",
            var price: Int = 0,
            var priceStr: String = "",
            var imgUrl: String = "",
            var rating: Int = 0,
            var countReview: Int = 0,
            var url: String = "",
            var applink: String = "",
            var optionPosition: Int = 0,
            var inspirationCarouselType: String = ""
        ){
            fun getProductAsObjectDataLayer(): Any {
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

            fun getProductImpressionAsObjectDataLayer(): Any {
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


