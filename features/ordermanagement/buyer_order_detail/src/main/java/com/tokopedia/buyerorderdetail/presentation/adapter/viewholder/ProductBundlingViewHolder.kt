package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.adapter.ProductBundlingItemAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.itemdecoration.ProductBundlingItemDecoration
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ProductBundlingViewHolder(
        itemView: View?,
        private val listener: Listener,
        private val navigator: BuyerOrderDetailNavigator
): BaseToasterViewHolder<ProductListUiModel.ProductBundlingUiModel>(itemView), ProductBundlingItemAdapter.ViewHolder.Listener {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_product_bundling

        private const val PRODUCT_BUNDLING_IMAGE_ICON_URL = "https://images.tokopedia.net/img/android/others/ic_product_bundling.png"
    }

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
        itemView?.run {
            containerLayout = findViewById(R.id.container_bom_detail_bundling)
            bundlingNameText = findViewById(R.id.tv_bom_detail_bundling_name)
            bundlingIconImage = findViewById(R.id.iv_bom_detail_bundling_icon)
            bundlingItemRecyclerView = findViewById(R.id.rv_bom_detail_bundling)
            bundlingPriceText = findViewById(R.id.tv_bom_detail_bundling_price_value)
        }
    }

    override fun bind(element: ProductListUiModel.ProductBundlingUiModel) {
        setupBundleHeader(element.bundleName)
        setupBundleAdapter(element.bundleItemList)
        setupBundleTotalPrice(element.totalPriceText)
    }

    override fun onBundleItemClicked(orderId: String, orderDetailId: String, orderStatusId: String) {
        if (orderId != BuyerOrderDetailMiscConstant.WAITING_INVOICE_ORDER_ID) {
            navigator.goToProductSnapshotPage(orderId, orderDetailId)
            BuyerOrderDetailTracker.eventClickProduct(orderStatusId, orderId)
        } else {
            showToaster(getString(R.string.buyer_order_detail_error_message_cant_open_snapshot_when_waiting_invoice))
        }
    }

    override fun onBundleItemAddToCart(uiModel: ProductListUiModel.ProductBundlingItemUiModel) {
        listener.onPurchaseAgainButtonClicked(uiModel)
    }

    override fun onBundleItemSeeSimilarProducts(uiModel: ProductListUiModel.ProductBundlingItemUiModel) {
        navigator.openAppLink(uiModel.button.url, false)
        // TODO: Set correct tracker
    }

    private fun setupBundleHeader(bundleName: String) {
        bundlingNameText?.text = bundleName
        bundlingIconImage?.setImageUrl(PRODUCT_BUNDLING_IMAGE_ICON_URL)
    }

    private fun setupBundleAdapter(bundleItemList: List<ProductListUiModel.ProductBundlingItemUiModel>) {
        bundlingItemRecyclerView?.run {
            layoutManager = LinearLayoutManager(itemView.context)
            adapter = ProductBundlingItemAdapter(this@ProductBundlingViewHolder, bundleItemList)
            bundleItemDecoration?.let {
                addItemDecoration(it)
            }
        }
    }

    private fun setupBundleTotalPrice(price: String) {
        bundlingPriceText?.text = price
    }

    interface Listener {
        fun onPurchaseAgainButtonClicked(uiModel: ProductListUiModel.ProductBundlingItemUiModel)
    }

}