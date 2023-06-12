package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseProductUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRepurchaseProductCardBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowRepurchaseProductViewHolder(
    itemView: View,
    private val listener: TokoNowRepurchaseProductListener?
): AbstractViewHolder<TokoNowRepurchaseProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_repurchase_product_card
    }

    private var binding: ItemTokopedianowRepurchaseProductCardBinding? by viewBinding()

    override fun bind(product: TokoNowRepurchaseProductUiModel) {
        binding?.productCard?.apply {
            bind(product)

            setOnClickListener {
                goToProductDetail(product)
                listener?.onProductCardClicked(product.position, product)
            }

            setOnQuantityChangedListener { quantity ->
                listener?.onCartQuantityChanged(product, quantity)
            }

            setOnClickAddVariantListener {
                listener?.onAddVariantClicked(product)
            }

            addOnImpressionListener(product) {
                listener?.onProductCardImpressed(layoutPosition, product)
            }
        }
    }

    private fun goToProductDetail(data: TokoNowRepurchaseProductUiModel) {
        val uri = UriUtil.buildUri(
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            data.productId
        )
        val appLink = listener?.createAffiliateLink(uri)
        RouteManager.route(itemView.context, appLink)
    }

    interface TokoNowRepurchaseProductListener {
        fun onCartQuantityChanged(data: TokoNowRepurchaseProductUiModel, quantity: Int)
        fun onProductCardImpressed(position: Int, data: TokoNowRepurchaseProductUiModel)
        fun onProductCardClicked(position: Int, data: TokoNowRepurchaseProductUiModel)
        fun onAddVariantClicked(data: TokoNowRepurchaseProductUiModel)
        fun createAffiliateLink(url: String): String
    }
}
