package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel

class TokoNowProductCardViewHolder(
    itemView: View,
    private val listener: TokoNowProductCardListener?
): AbstractViewHolder<TokoNowProductCardUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_list_card
    }

    private val productCard = itemView.findViewById<ProductCardListView?>(R.id.productCardView)

    override fun bind(data: TokoNowProductCardUiModel) {
        productCard?.apply {
            setProductModel(data.product)
            setOnClickListener {
                goToProductDetail(data)
                listener?.onProductCardClicked(adapterPosition, data)
            }
            setAddVariantClickListener {
                listener?.onAddVariantClicked(data)
            }
            setAddToCartNonVariantClickListener(object: ATCNonVariantListener {
                override fun onQuantityChanged(quantity: Int) {
                    listener?.onProductQuantityChanged(data, quantity)
                }
            })
            setImageProductViewHintListener(data, object : ViewHintListener {
                override fun onViewHint() {
                    listener?.onProductCardImpressed(data)
                }
            })
        }
    }

    private fun goToProductDetail(data: TokoNowProductCardUiModel) {
        RouteManager.route(
            itemView.context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            data.productId
        )
    }

    interface TokoNowProductCardListener {
        fun onProductQuantityChanged(data: TokoNowProductCardUiModel, quantity: Int)
        fun onProductCardImpressed(data: TokoNowProductCardUiModel)
        fun onProductCardClicked(position: Int, data: TokoNowProductCardUiModel)
        fun onAddVariantClicked(data: TokoNowProductCardUiModel)
    }
}