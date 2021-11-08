package com.tokopedia.tokopedianow.repurchase.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ATCNonVariantListener
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

        private const val PARAM_CATEGORY_L1 = "category_l1"
    }

    private var binding: ItemTokopedianowProductGridCardBinding? by viewBinding()

    override fun bind(item: RepurchaseProductUiModel) {
        binding?.tokoNowGridProductCard?.apply {
            setProductModel(item.productCard)

            setOnClickListener {
                goToProductDetail(item)
                listener?.onClickProduct(item, adapterPosition)
            }

            setAddVariantClickListener {
                listener?.onAddToCartVariant(item)
            }

            setSimilarProductClickListener {
                goToCategoryPage(item)
                listener?.onClickSimilarProduct()
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

    private fun goToCategoryPage(item: RepurchaseProductUiModel) {
        val uri = UriUtil.buildUriAppendParam(
            ApplinkConstInternalTokopediaNow.CATEGORY,
            mapOf(PARAM_CATEGORY_L1 to item.categoryId)
        )
        RouteManager.route(itemView.context, uri)
    }

    private fun goToProductDetail(item: RepurchaseProductUiModel) {
        RouteManager.route(
            itemView.context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            item.id
        )
    }

    interface RepurchaseProductCardListener {
        fun onClickProduct(item: RepurchaseProductUiModel, position: Int)
        fun onAddToCartVariant(item: RepurchaseProductUiModel)
        fun onAddToCartNonVariant(item: RepurchaseProductUiModel, quantity: Int)
        fun onProductImpressed(item: RepurchaseProductUiModel)
        fun onClickSimilarProduct()
    }
}
