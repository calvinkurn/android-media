package com.tokopedia.tokopedianow.repurchase.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowProductGridCardBinding
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel
import com.tokopedia.utils.view.binding.viewBinding

class RepurchaseProductViewHolder(
    itemView: View,
    private val listener: RepurchaseProductCardListener? = null
): AbstractViewHolder<RepurchaseProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_grid_card
    }

    private var binding: ItemTokopedianowProductGridCardBinding? by viewBinding()

    override fun bind(item: RepurchaseProductUiModel) {
        binding?.productCard?.apply {
            setData(
                model = item.productCardModel
            )
            setOnClickListener {
                goToProductDetail(item)
                listener?.onClickProduct(item, item.position)
            }
            setOnClickQuantityEditorListener { quantity ->
                listener?.onAddToCartNonVariant(item, quantity)
            }
            setOnClickQuantityEditorVariantListener {
                listener?.onAddToCartVariant(item)
            }
            addOnImpressionListener(item) {
                listener?.onProductImpressed(item)
            }
        }
    }

    override fun bind(item: RepurchaseProductUiModel?, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == true && item != null) {
            binding?.productCard?.setData(
                model = item.productCardModel
            )
        }
    }

    private fun goToProductDetail(item: RepurchaseProductUiModel) {
        RouteManager.route(
            itemView.context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            item.productCardModel.productId
        )
    }

    interface RepurchaseProductCardListener {
        fun onClickProduct(
            item: RepurchaseProductUiModel,
            position: Int
        )
        fun onAddToCartVariant(
            item: RepurchaseProductUiModel
        )
        fun onAddToCartNonVariant(
            item: RepurchaseProductUiModel,
            quantity: Int
        )
        fun onProductImpressed(
            item: RepurchaseProductUiModel
        )
        fun onClickSimilarProduct()
    }
}
