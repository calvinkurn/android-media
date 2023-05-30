package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import com.tokopedia.imageassets.TokopediaImageUrl

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.adapter.OwocProductBundlingItemAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.itemdecoration.ProductBundlingItemDecoration
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography

class OwocProductBundlingViewHolder(
    itemView: View?,
    private val navigator: BuyerOrderDetailNavigator?
) : AbstractViewHolder<OwocProductListUiModel.ProductBundlingUiModel>(itemView),
    OwocProductBundlingItemAdapter.ViewHolder.Listener {

    companion object {
        val LAYOUT = R.layout.item_owoc_product_bundling

        private const val PRODUCT_BUNDLING_IMAGE_ICON_URL =
            TokopediaImageUrl.PRODUCT_BUNDLING_IMAGE_ICON_URL
    }

    private val bundleItemAdapter: OwocProductBundlingItemAdapter =
        OwocProductBundlingItemAdapter(this)

    private val bundleItemDecoration = itemView?.context?.let {
        ProductBundlingItemDecoration(it)
    }

    private var containerLayout: ConstraintLayout? = null
    private var bundlingNameText: Typography? = null
    private var bundlingIconImage: ImageUnify? = null
    private var bundlingItemRecyclerView: RecyclerView? = null

    init {
        bindViews()
    }

    override fun bind(element: OwocProductListUiModel.ProductBundlingUiModel) {
        setupBundleHeader(element.bundleName, element.bundleIconUrl)
        setupBundleAdapter()
        setupBundleItems(element.bundleItemList)
    }

    override fun bind(
        element: OwocProductListUiModel.ProductBundlingUiModel?,
        payloads: MutableList<Any>
    ) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is OwocProductListUiModel.ProductBundlingUiModel && newItem is OwocProductListUiModel.ProductBundlingUiModel) {
                    if (oldItem.bundleName != newItem.bundleName || oldItem.bundleIconUrl != newItem.bundleIconUrl) {
                        setupBundleHeader(newItem.bundleName, newItem.bundleIconUrl)
                    }
                    if (oldItem.bundleItemList != newItem.bundleItemList) {
                        setupBundleItems(newItem.bundleItemList)
                    }
                    return
                }
            }
        }
    }

    override fun onBundleItemClicked(orderId: String, orderDetailId: String) {
        if (orderId != BuyerOrderDetailMiscConstant.WAITING_INVOICE_ORDER_ID) {
            navigator?.goToProductSnapshotPage(orderId, orderDetailId)
        } else {
            showToaster(getString(R.string.buyer_order_detail_error_message_cant_open_snapshot_when_waiting_invoice))
        }
    }

    private fun bindViews() {
        itemView.run {
            containerLayout = findViewById(R.id.container_owoc_bundling)
            bundlingNameText = findViewById(R.id.tv_owoc_bundling_name)
            bundlingIconImage = findViewById(R.id.iv_owoc_bundling_icon)
            bundlingItemRecyclerView = findViewById(R.id.rv_owoc_bundling)
        }
    }

    private fun setupBundleAdapter() {
        bundlingItemRecyclerView?.run {
            if (adapter != bundleItemAdapter) {
                layoutManager = LinearLayoutManager(context)
                adapter = bundleItemAdapter
            }
            if (itemDecorationCount.isZero()) {
                bundleItemDecoration?.let { addItemDecoration(it) }
            }
        }
    }

    private fun setupBundleHeader(bundleName: String, bundleIconUrl: String) {
        bundlingNameText?.text = bundleName
        val iconUrl = bundleIconUrl.ifEmpty {
            PRODUCT_BUNDLING_IMAGE_ICON_URL
        }
        bundlingIconImage?.loadImage(iconUrl)
    }

    private fun setupBundleItems(bundleItemList: List<OwocProductListUiModel.ProductUiModel>) {
        bundleItemAdapter.setItems(bundleItemList)
    }

    private fun showToaster(message: String) {
        itemView.parent?.parent?.parent?.let {
            if (it is View) {
                Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
            }
        }
    }
}
