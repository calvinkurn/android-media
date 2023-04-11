package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class OwocProductListHeaderViewHolder(
    itemView: View?,
    private val navigator: BuyerOrderDetailNavigator
) : AbstractViewHolder<OwocProductListUiModel.ProductListHeaderUiModel>(itemView), View.OnClickListener {

    companion object {
        val LAYOUT = R.layout.item_owoc_product_list_header
    }

    private val ivOwocShopTier = itemView?.findViewById<ImageView>(R.id.ivOwocShopTier)
    private val tvOwocShopLabel = itemView?.findViewById<Typography>(R.id.tvOwocShopLabel)
    private val tvOwocOrderInvoice = itemView?.findViewById<Typography>(R.id.tvOwocOrderInvoice)
    private val labelOwoc = itemView?.findViewById<Label>(R.id.labelOwoc)
    private val btnOwocMoreDetail = itemView?.findViewById<UnifyButton>(R.id.btnOwocMoreDetail)

    init {
        setupClickListener()
    }

    private var element: OwocProductListUiModel.ProductListHeaderUiModel? = null

    override fun bind(element: OwocProductListUiModel.ProductListHeaderUiModel?) {
        element?.let {
            this.element = it
            setupShopBadge(it.shopBadgeUrl)
            setupShopName(it.shopName)
            setupInvoice(it.invoiceNumber)
            setupButtonOrLabel(it.fromShopId, it.currentShopId)
        }
    }

    override fun bind(element: OwocProductListUiModel.ProductListHeaderUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is OwocProductListUiModel.ProductListHeaderUiModel && newItem is OwocProductListUiModel.ProductListHeaderUiModel) {
                    this.element = newItem
                    if (oldItem.shopBadgeUrl != newItem.shopBadgeUrl) {
                        setupShopBadge(newItem.shopBadgeUrl)
                    }
                    if (oldItem.shopName != newItem.shopName) {
                        setupShopName(newItem.shopName)
                    }
                    if (oldItem.invoiceNumber != newItem.invoiceNumber) {
                        setupInvoice(newItem.invoiceNumber)
                    }
                    if (oldItem.fromShopId != newItem.fromShopId || oldItem.currentShopId != newItem.currentShopId) {
                        setupButtonOrLabel(newItem.fromShopId, newItem.currentShopId)
                    }
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnOwocMoreDetail -> {
                goToOtherBomDetail()
            }
        }
    }

    private fun goToOtherBomDetail() {
        element?.let {
            navigator.goToBomDetailPage(it.orderId)
        }
    }

    private fun setupClickListener() {
        labelOwoc?.setOnClickListener(this@OwocProductListHeaderViewHolder)
    }

    private fun setupButtonOrLabel(fromShopId: String, currentShopId: String) {
        labelOwoc?.showWithCondition(fromShopId == currentShopId)
        btnOwocMoreDetail?.showWithCondition(fromShopId != currentShopId)
    }

    private fun setupShopBadge(shopBadgeUrl: String) {
        if (shopBadgeUrl.isBlank()) {
            hideShopBadge()
        } else {
            showShopBadge(shopBadgeUrl)
        }
    }

    private fun setupShopName(shopName: String) {
        tvOwocShopLabel?.text = MethodChecker.fromHtml(shopName)
    }

    private fun setupInvoice(invoice: String) {
        tvOwocOrderInvoice?.text = invoice
    }

    private fun hideShopBadge() {
        ivOwocShopTier?.gone()
    }

    private fun showShopBadge(url: String) {
        ivOwocShopTier?.let {
            it.loadImage(url) {
                setErrorDrawable(com.tokopedia.kotlin.extensions.R.drawable.ic_loading_error)
            }
        }
    }
}
