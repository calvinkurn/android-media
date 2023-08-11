package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.compact.productcardcarousel.presentation.customview.ProductCardCompactCarouselView.ProductCardCompactCarouselListener
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowProductAdsCarouselBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowAdsCarouselViewHolder(
    itemView: View,
    private val listener: ProductAdsCarouselListener?
) : AbstractViewHolder<TokoNowAdsCarouselUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_ads_carousel
    }

    private var binding: ItemTokopedianowProductAdsCarouselBinding? by viewBinding()

    override fun bind(uiModel: TokoNowAdsCarouselUiModel) {
        when (uiModel.state) {
            TokoNowLayoutState.LOADED -> {
                showContent(uiModel)
                hideShimmer()
            }
            else -> {
                hideContent()
                showShimmer()
            }
        }
    }

    override fun bind(uiModel: TokoNowAdsCarouselUiModel, payloads: MutableList<Any>) {
        if(payloads.firstOrNull() == true) {
            binding?.productCardCarousel?.bindItems(uiModel.items)
        }
    }

    private fun showContent(uiModel: TokoNowAdsCarouselUiModel) {
        binding?.apply {
            val title = getString(R.string.tokopedianow_product_ads_carousel_title)

            containerBackground.setBackgroundResource(R.drawable.tokopedianow_bg_ads_carousel_rounded)
            header.setModel(TokoNowDynamicHeaderUiModel(title = title))
            productCardCarousel.setListener(createProductCardListener(title))
            productCardCarousel.bindItems(uiModel.items)

            containerBackground.show()
            productCardCarousel.show()
            imageBackground.show()
            header.show()
        }
    }

    private fun createProductCardListener(title: String): ProductCardCompactCarouselListener {
        return object: ProductCardCompactCarouselListener {
            override fun onProductCardClicked(
                position: Int,
                product: ProductCardCompactCarouselItemUiModel
            ) {
                listener?.onProductCardClicked(position, title, product)
            }

            override fun onProductCardImpressed(
                position: Int,
                product: ProductCardCompactCarouselItemUiModel
            ) {
                listener?.onProductCardImpressed(position, title, product)
            }

            override fun onProductCardQuantityChanged(
                position: Int,
                product: ProductCardCompactCarouselItemUiModel,
                quantity: Int
            ) {
                listener?.onProductCardQuantityChanged(position, product, quantity)
            }

            override fun onProductCardAddVariantClicked(
                position: Int,
                product: ProductCardCompactCarouselItemUiModel
            ) {
                listener?.onProductCardAddVariantClicked(position, product)
            }

            override fun onSeeMoreClicked(seeMoreUiModel: ProductCardCompactCarouselSeeMoreUiModel) {

            }

            override fun onProductCardAddToCartBlocked() {
                listener?.onProductCardAddToCartBlocked()
            }
        }
    }

    private fun hideContent() {
        binding?.apply {
            productCardCarousel.hide()
            containerBackground.hide()
            imageBackground.hide()
        }
    }

    private fun showShimmer() {
        binding?.apply {
            titleShimmer.show()
            productCardShimmering.root.show()
        }
    }

    private fun hideShimmer() {
        binding?.apply {
            titleShimmer.hide()
            productCardShimmering.root.hide()
        }
    }
}
