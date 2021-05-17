package com.tokopedia.search.result.presentation.model

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant.ProductCardLabel.LABEL_INTEGRITY
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.presentation.view.adapter.InspirationCarouselOptionTypeFactory
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

class InspirationCarouselDataView(
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
            val bannerImageUrl: String = "",
            val bannerLinkUrl: String = "",
            val bannerApplinkUrl: String = "",
            val identifier: String = "",
            var product: List<Product> = listOf(),
            val inspirationCarouselType: String = "",
            val layout: String = "",
            val position: Int = 0,
            val carouselTitle: String = "",
            val optionPosition: Int = 0,
            var isChipsActive: Boolean = false,
    ): Visitable<InspirationCarouselOptionTypeFactory>{

        override fun type(typeFactory: InspirationCarouselOptionTypeFactory): Int {
            return typeFactory.type(layout)
        }

        fun shouldAddBannerCard(): Boolean {
            return bannerImageUrl.isNotEmpty() || title.isNotEmpty()
        }

        fun getBannerDataLayer(keyword: String): Any {
            return DataLayer.mapOf(
                "creative", carouselTitle,
                "id", "0",
                "name", "/search - $keyword",
                "position", position
            )
        }

        fun hasProducts() = product.isNotEmpty()

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
                val inspirationCarouselType: String = "",
                val ratingAverage: String = "",
                val labelGroupDataList: List<LabelGroupDataView> = listOf(),
                val layout: String = "",
                val originalPrice: String = "",
                val discountPercentage: Int = 0,
                val position: Int = 0,
                val optionTitle: String = "",
                val shopLocation: String = "",
                val badgeItemDataViewList: List<BadgeItemDataView> = listOf(),
        ): ImpressHolder(), Visitable<InspirationCarouselOptionTypeFactory> {

            override fun type(typeFactory: InspirationCarouselOptionTypeFactory): Int {
                return typeFactory.type(layout)
            }

            fun willShowSalesAndRating(): Boolean{
                return ratingAverage.isNotEmpty() && getLabelIntegrity() != null
            }

            fun getLabelIntegrity(): LabelGroupDataView? {
                return findLabelGroup(LABEL_INTEGRITY)
            }

            private fun findLabelGroup(position: String): LabelGroupDataView? {
                return labelGroupDataList.find { it.position == position }
            }

            fun willShowRating(): Boolean{
                return ratingAverage.isNotEmpty()
            }

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

            fun getInspirationCarouselChipsProductAsObjectDataLayer(filterSortParams: String): Any {
                return DataLayer.mapOf(
                        "brand", "none / other",
                        "category", "none / other",
                        "dimension61", if (filterSortParams.isEmpty()) "none / other" else filterSortParams,
                        "id", id,
                        "list", "/search - carousel chips",
                        "name", name,
                        "position", position,
                        "price", price,
                        "variant", "none / other"
                )
            }
        }
    }
}


