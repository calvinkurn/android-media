package com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseProductUiModel

class RepurchaseProductViewHolder(
    itemView: View,
    private val listener: RepurchaseProductCardListener? = null
): AbstractViewHolder<RepurchaseProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_grid_card
    }

    private val productCard: ProductCardGridView? by lazy {
        itemView.findViewById(R.id.tokoNowGridProductCard)
    }

    override fun bind(item: RepurchaseProductUiModel) {
        productCard?.apply {
            setProductModel(item.productCard)

            setOnClickListener {
                goToProductDetail(item)
                listener?.onClickProduct(item)
            }

            setAddVariantClickListener {
                listener?.onAddToCartVariant(item)
            }

            setAddToCartNonVariantClickListener(object: ATCNonVariantListener {
                override fun onQuantityChanged(quantity: Int) {
                    listener?.onAddToCartNonVariant(item, quantity)
                }
            })

            setImageProductViewHintListener(item, object: ViewHintListener {
                override fun onViewHint() {
                    listener?.onProductImpressed(item)
                }
            })
        }
    }

    private fun goToProductDetail(item: RepurchaseProductUiModel) {
        RouteManager.route(
            itemView.context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            item.id
        )
    }

    interface RepurchaseProductCardListener {
        fun onClickProduct(item: RepurchaseProductUiModel)
        fun onAddToCartVariant(item: RepurchaseProductUiModel)
        fun onAddToCartNonVariant(item: RepurchaseProductUiModel, quantity: Int)
        fun onProductImpressed(item: RepurchaseProductUiModel)
    }
}
