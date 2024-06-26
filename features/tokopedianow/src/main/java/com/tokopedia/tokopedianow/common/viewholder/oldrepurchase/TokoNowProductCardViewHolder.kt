package com.tokopedia.tokopedianow.common.viewholder.oldrepurchase

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowProductListCardBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowProductCardViewHolder(
    itemView: View,
    private val listener: TokoNowProductCardListener?
): AbstractViewHolder<TokoNowProductCardUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_list_card
    }

    private var binding: ItemTokopedianowProductListCardBinding? by viewBinding()

    override fun bind(data: TokoNowProductCardUiModel) {
       binding?.productCardView?.apply {
            setProductModel(data.product)
            setOnClickListener {
                goToProductDetail(data)
                listener?.onProductCardClicked(data.position, data)
            }
            setAddVariantClickListener {
                listener?.onAddVariantClicked(data)
            }
            setAddToCartNonVariantClickListener(object: ATCNonVariantListener {
                override fun onQuantityChanged(quantity: Int) {
                    listener?.onCartQuantityChanged(data, quantity)
                }
            })
            setImageProductViewHintListener(data, object : ViewHintListener {
                override fun onViewHint() {
                    listener?.onProductCardImpressed(data.position, data)
                }
            })
        }
    }

    private fun goToProductDetail(data: TokoNowProductCardUiModel) {
        val uri = UriUtil.buildUri(
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            data.productId
        )
        val appLink = listener?.createAffiliateLink(uri)
        RouteManager.route(itemView.context, appLink)
    }

    interface TokoNowProductCardListener {
        fun onCartQuantityChanged(data: TokoNowProductCardUiModel, quantity: Int)
        fun onProductCardImpressed(position: Int, data: TokoNowProductCardUiModel)
        fun onProductCardClicked(position: Int, data: TokoNowProductCardUiModel)
        fun onAddVariantClicked(data: TokoNowProductCardUiModel)
        fun createAffiliateLink(url: String): String
    }
}
