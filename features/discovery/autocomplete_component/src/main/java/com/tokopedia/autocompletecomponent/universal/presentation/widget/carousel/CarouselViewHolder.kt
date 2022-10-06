package com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.UniversalSearchCarouselItemLayoutBinding
import com.tokopedia.autocompletecomponent.universal.presentation.BaseUniversalDataView
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.utils.view.binding.viewBinding

class CarouselViewHolder(
    itemView: View,
    private val carouselListener: CarouselListener,
): AbstractViewHolder<CarouselDataView>(itemView) {
    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.universal_search_carousel_item_layout
    }

    private var binding: UniversalSearchCarouselItemLayoutBinding? by viewBinding()

    override fun bind(data: CarouselDataView) {
        bindTitle(data.data)
        bindSubtitle(data.data)
        bindSeeAll(data)
        bindCarousel(data)
    }

    private fun bindTitle(data: BaseUniversalDataView) {
        binding?.universalSearchCarouselTitle?.shouldShowWithAction(data.title.isNotEmpty()) {
            binding?.universalSearchCarouselTitle?.text = data.title
        }
    }

    private fun bindSubtitle(data: BaseUniversalDataView) {
        binding?.universalSearchCarouselSubtitle?.shouldShowWithAction(data.subtitle.isNotEmpty()) {
            binding?.universalSearchCarouselSubtitle?.text = data.subtitle
        }
    }

    private fun bindSeeAll(data: CarouselDataView) {
        binding?.universalSearchCarouselSeeAll?.shouldShowWithAction(data.data.applink.isNotEmpty()) {
            binding?.universalSearchCarouselSeeAll?.setOnClickListener {
                carouselListener.onCarouselSeeAllClick(data)
            }
        }
    }

    private fun bindCarousel(data: CarouselDataView) {
        val products = data.product

        binding?.universalSearchCarousel?.bindCarouselProductCardViewGrid(
            recyclerViewPool = carouselListener.carouselRecycledViewPool,
            productCardModelList = products.map {
                val discountPercentage =
                    if (it.discountPercentage == "0") "" else it.discountPercentage+"%"
                val labelGroups = it.labelGroups.map { labelGroup ->
                    ProductCardModel.LabelGroup(
                        position = labelGroup.position,
                        title = labelGroup.title,
                        type = labelGroup.type,
                        imageUrl = labelGroup.imageUrl,
                    )
                }

                ProductCardModel(
                    productName = it.title,
                    formattedPrice = it.price,
                    discountPercentage = discountPercentage,
                    slashedPrice = it.originalPrice,
                    productImageUrl = it.imageUrl,
                    countSoldRating = it.ratingAverage,
                    shopLocation = it.shop.city,
                    shopName = it.shop.name,
                    shopBadgeList = it.badge.map { badge ->
                        ProductCardModel.ShopBadge(
                            isShown = badge.show,
                            imageUrl = badge.imageUrl,
                        )
                    },
                    labelGroupList = labelGroups,
                    freeOngkir = ProductCardModel.FreeOngkir(
                        imageUrl = it.freeOngkir.imgUrl,
                        isActive = it.freeOngkir.isActive,
                    )
                )
            },
            carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener {
                override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                    val product = products.getOrNull(carouselProductCardPosition) ?: return
                    carouselListener.onCarouselItemClick(product)
                }
            },
            carouselProductCardOnItemImpressedListener = object: CarouselProductCardListener.OnItemImpressedListener {
                override fun onItemImpressed(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                    val product = products.getOrNull(carouselProductCardPosition) ?: return
                    carouselListener.onCarouselItemImpressed(product)
                }

                override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                    return products.getOrNull(carouselProductCardPosition)
                }
            },
        )
    }
}