package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductCardUiModel

class HomeProductCardViewHolder(
    itemView: View,
    private val tokoNowView: TokoNowView?,
    private val listener: HomeProductCardListener?
): AbstractViewHolder<HomeProductCardUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_list_card
    }

    private val productCard = itemView.findViewById<ProductCardListView?>(R.id.productCardView)

    override fun bind(data: HomeProductCardUiModel) {
        productCard?.apply {
            setProductModel(data.product)
            setOnClickListener {
                goToProductDetail(data)
            }
            setAddVariantClickListener {
                goToAtcVariant(data)
            }
            setAddToCartNonVariantClickListener(object: ATCNonVariantListener {
                override fun onQuantityChanged(quantity: Int) {
                    listener?.onProductPurchaseQuantityChanged(data, quantity)
                }
            })
        }
    }

    private fun goToProductDetail(data: HomeProductCardUiModel) {
        RouteManager.route(
            itemView.context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            data.productId
        )
    }

    private fun goToAtcVariant(data: HomeProductCardUiModel) {
        (tokoNowView?.getFragmentPage() as? TokoNowHomeFragment)?.let { view ->
            AtcVariantHelper.goToAtcVariant(
                context = itemView.context,
                productId = data.productId,
                pageSource = TokoNowHomeFragment.SOURCE,
                isTokoNow = true,
                shopId = data.shopId,
                startActivitResult = view::startActivityForResult
            )
        }
    }

    interface HomeProductCardListener {
        fun onProductPurchaseQuantityChanged(data: HomeProductCardUiModel, quantity: Int)
    }
}