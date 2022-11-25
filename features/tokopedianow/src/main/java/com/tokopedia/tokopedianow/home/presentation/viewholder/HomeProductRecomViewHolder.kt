package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.os.Parcelable
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowProductCardCarouselView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowProductRecommendationBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeProductRecomViewHolder(
    itemView: View,
    private val tokoNowView: TokoNowView? = null,
    private val listener: HomeProductRecomListener? = null
) : AbstractViewHolder<HomeProductRecomUiModel>(itemView),
    TokoNowProductCardCarouselView.TokoNowProductCardCarouselListener,
    TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_recommendation
    }

    private var binding: ItemTokopedianowProductRecommendationBinding? by viewBinding()

    private var channelId = ""
    private var headerName = ""

    override fun bind(element: HomeProductRecomUiModel) {
        binding?.apply {
            channelId = element.id
            headerName = element.headerModel?.title.orEmpty()

            productRecommendation.setItems(
                items = element.productList,
                seeMoreModel = element.seeMoreModel
            )
            productRecommendation.setHeader(
                header = element.headerModel
            )
            productRecommendation.setListener(
                productCardCarouselListener = this@HomeProductRecomViewHolder,
                headerCarouselListener = this@HomeProductRecomViewHolder
            )
        }
    }

    override fun bind(element: HomeProductRecomUiModel?, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == true && element != null) {
            binding?.productRecommendation?.setItems(
                items = element.productList,
                seeMoreModel = element.seeMoreModel
            )
        }
    }

    override fun onSeeAllClicked(
        headerName: String,
        appLink: String
    ) {
        listener?.onSeeAllClicked(
            channelId = channelId,
            appLink = appLink,
            headerName = headerName
        )
    }

    override fun onSeeMoreClicked(
        seeMoreUiModel: TokoNowSeeMoreCardCarouselUiModel
    ) {
        listener?.onSeeMoreClicked(
            channelId = channelId,
            appLink = seeMoreUiModel.appLink,
            headerName = seeMoreUiModel.headerName
        )
    }

    override fun onProductCardClicked(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel
    ) {
        listener?.onProductRecomClicked(
            product = product,
            channelId = channelId,
            headerName = headerName,
            position = layoutPosition
        )
    }

    override fun onProductCardImpressed(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel
    ) {
        listener?.onProductRecomImpressed(
            product = product,
            channelId = channelId,
            headerName = headerName,
            position = layoutPosition
        )
    }

    override fun onProductCardQuantityChanged(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel,
        quantity: Int
    ) {
        listener?.onProductRecomQuantityChanged(
            product = product,
            quantity = quantity,
            channelId = channelId
        )
    }

    override fun onProductCardAddVariantClicked(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel
    ) {
        listener?.onProductCardAddVariantClicked(
            product = product,
            position = position
        )
    }

    override fun saveScrollState(state: Parcelable?) {
        tokoNowView?.saveScrollState(layoutPosition, state)
    }

    override fun getScrollState(): Parcelable? {
        return tokoNowView?.getScrollState(layoutPosition)
    }

    override fun onChannelExpired() { /* nothing to do */ }

    interface HomeProductRecomListener {
        fun onProductRecomClicked(
            product: TokoNowProductCardCarouselItemUiModel,
            channelId: String,
            headerName: String,
            position: Int
        )
        fun onProductRecomImpressed(
            product: TokoNowProductCardCarouselItemUiModel,
            channelId: String,
            headerName: String,
            position: Int
        )
        fun onSeeAllClicked(
            channelId: String,
            appLink: String,
            headerName: String
        )
        fun onSeeMoreClicked(
            channelId: String,
            appLink: String,
            headerName: String
        )
        fun onProductRecomQuantityChanged(
            product: TokoNowProductCardCarouselItemUiModel,
            quantity: Int,
            channelId: String
        )
        fun onProductCardAddVariantClicked(
            product: TokoNowProductCardCarouselItemUiModel,
            position: Int
        )
    }
}
