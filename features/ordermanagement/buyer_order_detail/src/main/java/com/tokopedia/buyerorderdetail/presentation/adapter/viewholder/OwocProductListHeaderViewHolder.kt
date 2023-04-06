package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.Typography

class OwocProductListHeaderViewHolder(
    itemView: View?,
    private val navigator: BuyerOrderDetailNavigator
) : AbstractViewHolder<OwocProductListUiModel.ProductListHeaderUiModel>(itemView), View.OnClickListener {

    companion object {
        val LAYOUT = R.layout.item_owoc_product_list_header
    }

    private val container = itemView?.findViewById<ConstraintLayout>(R.id.container)
    private val icBuyerOrderDetailSeeShopBadge = itemView?.findViewById<ImageView>(R.id.icBuyerOrderDetailSeeShopBadge)
    private val icBuyerOrderDetailSeeShopPage = itemView?.findViewById<IconUnify>(R.id.icBuyerOrderDetailSeeShopPage)
    private val tvBuyerOrderDetailShopName = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailShopName)

    init {
        setupClickListener()
    }

    private var element: OwocProductListUiModel.ProductListHeaderUiModel? = null

    override fun bind(element: OwocProductListUiModel.ProductListHeaderUiModel?) {
        element?.let {
            this.element = it
            setupShopBadge(it.shopBadgeUrl)
            setupShopName(it.shopName)
        }
    }

    override fun bind(element: OwocProductListUiModel.ProductListHeaderUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is OwocProductListUiModel.ProductListHeaderUiModel && newItem is OwocProductListUiModel.ProductListHeaderUiModel) {
                    container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    this.element = newItem
                    if (oldItem.shopBadgeUrl != newItem.shopBadgeUrl) {
                        setupShopBadge(newItem.shopBadgeUrl)
                    }
                    if (oldItem.shopName != newItem.shopName) {
                        setupShopName(newItem.shopName)
                    }
                    container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
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
        element?.let {
            navigator.goToShopPage(it.shopId)
            BuyerOrderDetailTracker.eventClickShopName(it.orderStatusId, it.orderId)
        }
    }

    private fun setupClickListener() {
        icBuyerOrderDetailSeeShopBadge?.setOnClickListener(this@OwocProductListHeaderViewHolder)
        tvBuyerOrderDetailShopName?.setOnClickListener(this@OwocProductListHeaderViewHolder)
        icBuyerOrderDetailSeeShopPage?.setOnClickListener(this@OwocProductListHeaderViewHolder)
    }

    private fun setupShopBadge(shopBadgeUrl: String) {
        if (shopBadgeUrl.isBlank()) {
            hideShopBadge()
        } else {
            showShopBadge(shopBadgeUrl)
        }
    }

    private fun setupShopName(shopName: String) {
        tvBuyerOrderDetailShopName?.text = MethodChecker.fromHtml(shopName)
    }

    private fun hideShopBadge() {
        icBuyerOrderDetailSeeShopBadge?.gone()
    }

    private fun showShopBadge(url: String) {
        icBuyerOrderDetailSeeShopBadge?.let {
            it.loadImage(url) {
                setErrorDrawable(com.tokopedia.kotlin.extensions.R.drawable.ic_loading_error)
            }
        }
    }
}
