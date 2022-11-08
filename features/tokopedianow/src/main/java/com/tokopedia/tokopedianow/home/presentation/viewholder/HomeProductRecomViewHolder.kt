package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.os.Parcelable
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.common.util.TrackerUtil.getTrackerPosition
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowProductCardCarouselView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeProductRecomBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeProductRecomViewHolder(
    itemView: View,
    private val tokoNowView: TokoNowView? = null,
    private val listener: HomeProductRecomListener? = null,
) : AbstractViewHolder<HomeProductRecomUiModel>(itemView),
    TokoNowProductCardCarouselView.TokoNowProductCardCarouselListener,
    TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_product_recom
    }

    private var binding: ItemTokopedianowHomeProductRecomBinding? by viewBinding()

    private var channelId = ""
    private var headerName = ""

    override fun bind(element: HomeProductRecomUiModel) {
        binding?.apply {
            channelId = element.id
            headerName = element.headerModel?.title.orEmpty()
            productRecom.setItems(
                items = element.productList,
                seeMoreUiModel = element.seeMoreModel
            )
            productRecom.setHeader(
                header = element.headerModel
            )
            productRecom.setListener(
                productCardCarouselListener = this@HomeProductRecomViewHolder,
                headerCarouselListener = this@HomeProductRecomViewHolder
            )
        }
    }

    override fun onSeeAllClicked(
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
            headerName = headerName
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
            position = layoutPosition.getTrackerPosition().toString()
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
            position = layoutPosition.getTrackerPosition().toString()
        )
    }

    override fun onProductCardAnimationFinished(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel,
        quantity: Int
    ) {
        listener?.onProductRecomAnimationFinished(
            product = product,
            quantity = quantity,
            channelId = channelId,
            position = layoutPosition.getTrackerPosition().toString()
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
            channelId = channelId,
            position = layoutPosition.getTrackerPosition().toString()
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
            position: String
        )

        fun onProductRecomImpressed(
            product: TokoNowProductCardCarouselItemUiModel,
            channelId: String,
            headerName: String,
            position: String
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
            channelId: String,
            position: String
        )

        fun onProductRecomAnimationFinished(
            product: TokoNowProductCardCarouselItemUiModel,
            quantity: Int,
            channelId: String,
            position: String
        )

        fun onProductCardAddVariantClicked(
            product: TokoNowProductCardCarouselItemUiModel,
            position: Int
        )
    }
}
