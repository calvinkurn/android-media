package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import kotlinx.android.synthetic.main.item_buyer_order_detail_product_list_header.view.*

class ProductListHeaderViewHolder(
        itemView: View?,
        private val navigator: BuyerOrderDetailNavigator
) : AbstractViewHolder<ProductListUiModel.ProductListHeaderUiModel>(itemView), View.OnClickListener {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_product_list_header
    }

    init {
        setupClickListener()
    }

    private var element: ProductListUiModel.ProductListHeaderUiModel? = null

    override fun bind(element: ProductListUiModel.ProductListHeaderUiModel?) {
        element?.let {
            this.element = it
            setupShopBadge(it.shopBadge)
            setupShopName(it.shopName)
        }
    }

    override fun bind(element: ProductListUiModel.ProductListHeaderUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val oldItem = it.first
                val newItem = it.second
                if (oldItem is ProductListUiModel.ProductListHeaderUiModel && newItem is ProductListUiModel.ProductListHeaderUiModel) {
                    itemView.container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    this.element = newItem
                    if (oldItem.shopBadge != newItem.shopBadge) {
                        setupShopBadge(newItem.shopBadge)
                    }
                    if (oldItem.shopName != newItem.shopName) {
                        setupShopName(newItem.shopName)
                    }
                    itemView.container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.icBuyerOrderDetailSeeShopBadge, R.id.tvBuyerOrderDetailShopName, R.id.icBuyerOrderDetailSeeShopPage -> {
                goToShopPage()
            }
        }
    }

    private fun goToShopPage() {
        element?.let { navigator.goToShopPage(it.shopId) }
    }

    private fun setupClickListener() {
        itemView.apply {
            icBuyerOrderDetailSeeShopBadge?.setOnClickListener(this@ProductListHeaderViewHolder)
            tvBuyerOrderDetailShopName?.setOnClickListener(this@ProductListHeaderViewHolder)
            icBuyerOrderDetailSeeShopPage?.setOnClickListener(this@ProductListHeaderViewHolder)
        }
    }

    private fun setupShopBadge(shopBadge: Int) {
        when (shopBadge) {
            0 -> hideShopBadge()
            1 -> showPowerMerchantBadge()
            2 -> showOfficialShopBadge()
        }
    }

    private fun setupShopName(shopName: String) {
        itemView.tvBuyerOrderDetailShopName?.text = shopName
    }

    private fun hideShopBadge() {
        itemView.icBuyerOrderDetailSeeShopBadge?.gone()
    }

    private fun showPowerMerchantBadge() {
        itemView.icBuyerOrderDetailSeeShopBadge?.setImage(IconUnify.BADGE_PM_FILLED)
    }

    private fun showOfficialShopBadge() {
        itemView.icBuyerOrderDetailSeeShopBadge?.setImage(IconUnify.BADGE_OS_FILLED)
    }
}