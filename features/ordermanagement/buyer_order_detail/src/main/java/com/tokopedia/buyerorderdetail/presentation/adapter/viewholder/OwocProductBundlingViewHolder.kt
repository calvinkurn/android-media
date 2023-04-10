package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import com.tokopedia.imageassets.TokopediaImageUrl

import android.animation.LayoutTransition
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.adapter.OwocProductBundlingItemAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.itemdecoration.ProductBundlingItemDecoration
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography

class OwocProductBundlingViewHolder(
    itemView: View?,
    private val navigator: BuyerOrderDetailNavigator
): AbstractViewHolder<OwocProductListUiModel.ProductBundlingUiModel>(itemView), OwocProductBundlingItemAdapter.ViewHolder.Listener {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_product_bundling

        private const val PRODUCT_BUNDLING_IMAGE_ICON_URL = TokopediaImageUrl.PRODUCT_BUNDLING_IMAGE_ICON_URL
    }

    private val bundleItemAdapter = OwocProductBundlingItemAdapter(this)
    private val bundleItemDecoration by lazy {
        itemView?.context?.let {
            ProductBundlingItemDecoration(it)
        }
    }

    private var containerLayout: ConstraintLayout? = null
    private var bundlingNameText: Typography? = null
    private var bundlingIconImage: ImageUnify? = null
    private var bundlingItemRecyclerView: RecyclerView? = null
    private var bundlingPriceText: Typography? = null

    init {
        bindViews()
        setupBundleAdapter()
    }

    override fun bind(element: OwocProductListUiModel.ProductBundlingUiModel) {
        setupBundleHeader(element.bundleName, element.bundleIconUrl)
        setupBundleItems(element.bundleItemList)
        setupBundleTotalPrice(element.totalPriceText)
    }

    override fun bind(
        element: OwocProductListUiModel.ProductBundlingUiModel?,
        payloads: MutableList<Any>
    ) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is OwocProductListUiModel.ProductBundlingUiModel && newItem is OwocProductListUiModel.ProductBundlingUiModel) {
                    containerLayout?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    if (oldItem.bundleName != newItem.bundleName || oldItem.bundleIconUrl != newItem.bundleIconUrl) {
                        setupBundleHeader(newItem.bundleName, newItem.bundleIconUrl)
                    }
                    if (oldItem.bundleItemList != newItem.bundleItemList) {
                        setupBundleItems(newItem.bundleItemList)
                    }
                    if (oldItem.totalPriceText != newItem.totalPriceText) {
                        setupBundleTotalPrice(newItem.totalPriceText)
                    }
                    return
                }
            }
        }
    }

    override fun onBundleItemClicked(orderId: String, orderDetailId: String, orderStatusId: String) {
        if (orderId != BuyerOrderDetailMiscConstant.WAITING_INVOICE_ORDER_ID) {
            navigator.goToProductSnapshotPage(orderId, orderDetailId)
            BuyerOrderDetailTracker.eventClickProduct(orderStatusId, orderId)
        } else {
            showToaster(getString(R.string.buyer_order_detail_error_message_cant_open_snapshot_when_waiting_invoice))
        }
    }

    private fun bindViews() {
        itemView.run {
            containerLayout = findViewById(R.id.container_bom_detail_bundling)
            bundlingNameText = findViewById(R.id.tv_bom_detail_bundling_name)
            bundlingIconImage = findViewById(R.id.iv_bom_detail_bundling_icon)
            bundlingItemRecyclerView = findViewById(R.id.rv_bom_detail_bundling)
            bundlingPriceText = findViewById(R.id.tv_bom_detail_bundling_price_value)
        }
    }

    private fun setupBundleAdapter() {
        bundlingItemRecyclerView?.run {
            layoutManager = LinearLayoutManager(itemView.context)
            adapter = bundleItemAdapter
            bundleItemDecoration?.let { addItemDecoration(it) }
        }
    }

    private fun setupBundleHeader(bundleName: String, bundleIconUrl: String) {
        bundlingNameText?.text = bundleName
        val iconUrl =
            if (bundleIconUrl.isEmpty()) {
                PRODUCT_BUNDLING_IMAGE_ICON_URL
            } else {
                bundleIconUrl
            }
        bundlingIconImage?.setImageUrl(iconUrl)
    }

    private fun setupBundleItems(bundleItemList: List<OwocProductListUiModel.ProductUiModel>) {
        bundleItemAdapter.setItems(bundleItemList)
    }

    private fun setupBundleTotalPrice(price: String) {
        bundlingPriceText?.text = price
    }

    private fun showToaster(message: String) {
        itemView.parent?.parent?.parent?.let {
            if (it is View) {
                Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
            }
        }
    }
}
