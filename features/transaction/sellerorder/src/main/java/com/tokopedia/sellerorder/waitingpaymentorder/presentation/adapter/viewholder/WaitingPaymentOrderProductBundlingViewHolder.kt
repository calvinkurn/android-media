package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.RecyclerViewItemDivider
import com.tokopedia.sellerorder.databinding.ItemWaitingPaymentOrderProductBundlingBinding
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.WaitingPaymentOrderProductsAdapter
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory.WaitingPaymentOrderProductsAdapterTypeFactory
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderUiModel
import com.tokopedia.utils.view.binding.viewBinding

class WaitingPaymentOrderProductBundlingViewHolder(
    itemView: View
) : AbstractViewHolder<WaitingPaymentOrderUiModel.ProductBundlingUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_waiting_payment_order_product_bundling
    }

    private val binding by viewBinding<ItemWaitingPaymentOrderProductBundlingBinding>()

    private val adapter: WaitingPaymentOrderProductsAdapter by lazy {
        WaitingPaymentOrderProductsAdapter(WaitingPaymentOrderProductsAdapterTypeFactory())
    }

    override fun bind(element: WaitingPaymentOrderUiModel.ProductBundlingUiModel?) {
        element?.run {
            setupProductBundlingHeader(name, iconUrl)
            setupProductBundlingProducts(products)
        }
    }

    private fun setupProductBundlingProducts(products: List<WaitingPaymentOrderUiModel.ProductUiModel>) {
        binding?.rvSomProductBundling?.run {
            if (adapter == null) {
                isNestedScrollingEnabled = false
                adapter = this@WaitingPaymentOrderProductBundlingViewHolder.adapter
            }
            if (itemDecorationCount == Int.ZERO) {
                val margins = getDimens(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
                addItemDecoration(
                    RecyclerViewItemDivider(
                        divider = MethodChecker.getDrawable(
                            context, R.drawable.som_detail_product_bundling_product_divider
                        ),
                        topMargin = margins,
                        bottomMargin = margins,
                        applyMarginAfterLastItem = true
                    )
                )
            }
            this@WaitingPaymentOrderProductBundlingViewHolder.adapter.updateProducts(products)
        }
    }

    private fun setupProductBundlingHeader(name: String, iconUrl: String) {
        binding?.run {
            tvSomBundleName.text = name
            icSomProductBundling.setImageUrl(iconUrl)
        }
    }
}