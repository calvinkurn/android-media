package com.tokopedia.tokopedianow.search.presentation.viewholder

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView
import com.tokopedia.productcard.compact.productcardcarousel.presentation.customview.ProductCardCompactCarouselView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowBroadmatchBinding
import com.tokopedia.tokopedianow.search.presentation.listener.BroadMatchListener
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchDataView
import com.tokopedia.utils.view.binding.viewBinding

class BroadMatchViewHolder(
    itemView: View,
    private val listener: BroadMatchListener
): AbstractViewHolder<BroadMatchDataView>(itemView),
    ProductCardCompactCarouselView.ProductCardCompactCarouselBasicListener,
    TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_broadmatch
    }

    private var binding: ItemTokopedianowBroadmatchBinding? by viewBinding()

    override fun bind(element: BroadMatchDataView) {
        binding?.apply {
            setItems(
                items = element.broadMatchItemModelList,
                seeMoreModel = element.seeMoreModel
            )
            setHeader(
                headerModel = element.headerModel
            )
            setListener(
                productCardCarouselListener = this@BroadMatchViewHolder,
                headerCarouselListener = this@BroadMatchViewHolder
            )
        }
    }

    override fun bind(element: BroadMatchDataView?, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == true && element != null) {
            binding?.setItems(
                items = element.broadMatchItemModelList.map { it.copy() },
                seeMoreModel = element.seeMoreModel
            )
        }
    }

    private fun ItemTokopedianowBroadmatchBinding.setItems(
        items: List<Visitable<*>>,
        seeMoreModel: ProductCardCompactCarouselSeeMoreUiModel? = null
    ) {
        productCardCarousel.bindItems(
            items = items,
            seeMoreModel  = seeMoreModel
        )
    }

    private fun ItemTokopedianowBroadmatchBinding.setHeader(
        headerModel: TokoNowDynamicHeaderUiModel? = null
    ) {
        header.showIfWithBlock(headerModel != null) {
            headerModel?.apply {
                setModel(this)
            }
        }
    }

    private fun ItemTokopedianowBroadmatchBinding.setListener(
        productCardCarouselListener: ProductCardCompactCarouselView.ProductCardCompactCarouselListener,
        headerCarouselListener: TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener
    ) {
        productCardCarousel.setListener(
            productCardCarouselListener = productCardCarouselListener,
        )
        header.setListener(
            headerListener =  headerCarouselListener
        )
    }

    override fun onProductCardClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        listener.onBroadMatchItemClicked(
            broadMatchItemDataView = product,
            broadMatchIndex = position
        )
    }

    override fun onProductCardImpressed(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        listener.onBroadMatchItemImpressed(
            broadMatchItemDataView = product,
            broadMatchIndex = position
        )
    }

    override fun onProductCardQuantityChanged(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel,
        quantity: Int
    ) {
        listener.onBroadMatchItemATCNonVariant(
            broadMatchItemDataView = product,
            quantity = quantity,
            broadMatchIndex = position
        )
    }

    override fun onSeeMoreClicked(seeMoreUiModel: ProductCardCompactCarouselSeeMoreUiModel) {
        listener.onBroadMatchSeeAllClicked(
            title = seeMoreUiModel.headerName,
            appLink = seeMoreUiModel.appLink
        )
    }

    override fun onProductCardAddToCartBlocked() = listener.onBroadMatchAddToCartBlocked()

    override fun onSeeAllClicked(
        context: Context,
        channelId: String,
        headerName: String,
        appLink: String,
        widgetId: String
    ) {
        listener.onBroadMatchSeeAllClicked(
            title = headerName,
            appLink = appLink
        )
    }

    override fun onChannelExpired() { /* nothing to do */ }

    override fun onProductCardAddVariantClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) { /* nothing to do */ }
}
