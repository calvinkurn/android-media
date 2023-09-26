package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import com.tokopedia.imageassets.TokopediaImageUrl

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.databinding.ItemOwocProductBundlingBinding
import com.tokopedia.buyerorderdetail.presentation.adapter.OwocProductBundlingItemAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.itemdecoration.ProductBundlingItemDecoration
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.OwocRecyclerviewPoolListener
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.Toaster

class OwocProductBundlingViewHolder(
    view: View?,
    private val navigator: BuyerOrderDetailNavigator?,
    private val recyclerviewPoolListener: OwocRecyclerviewPoolListener
) : AbstractViewHolder<OwocProductListUiModel.ProductBundlingUiModel>(view),
    OwocProductBundlingItemAdapter.ViewHolder.Listener {

    companion object {
        val LAYOUT = R.layout.item_owoc_product_bundling

        private const val PRODUCT_BUNDLING_IMAGE_ICON_URL =
            TokopediaImageUrl.PRODUCT_BUNDLING_IMAGE_ICON_URL
    }

    private val bundleItemAdapter: OwocProductBundlingItemAdapter =
        OwocProductBundlingItemAdapter(this)

    private val bundleItemDecoration = itemView.context?.let {
        ProductBundlingItemDecoration(it)
    }

    private val binding = ItemOwocProductBundlingBinding.bind(itemView)

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

    private fun setupBundleAdapter() {
        binding.rvOwocBundling.run {
            if (adapter != bundleItemAdapter) {
                layoutManager = LinearLayoutManager(context)
                adapter = bundleItemAdapter
                itemAnimator = null
                isNestedScrollingEnabled = false
                setRecycledViewPool(recyclerviewPoolListener.parentPool)
            }
            if (itemDecorationCount.isZero()) {
                bundleItemDecoration?.let { addItemDecoration(it) }
            }
        }
    }

    private fun setupBundleHeader(bundleName: String, bundleIconUrl: String) {
        binding.tvOwocBundlingName.text = bundleName
        val iconUrl = bundleIconUrl.ifEmpty {
            PRODUCT_BUNDLING_IMAGE_ICON_URL
        }
        binding.ivOwocBundlingIcon.loadImage(iconUrl)
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
