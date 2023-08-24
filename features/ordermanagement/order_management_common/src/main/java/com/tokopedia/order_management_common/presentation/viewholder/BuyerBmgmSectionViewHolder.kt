package com.tokopedia.order_management_common.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.order_management_common.R
import com.tokopedia.order_management_common.constants.OrderManagementConstants
import com.tokopedia.order_management_common.databinding.ItemOrderProductBmgmSectionBinding
import com.tokopedia.order_management_common.databinding.PartialBmgmAddOnSummaryBinding
import com.tokopedia.order_management_common.presentation.adapter.ProductBmgmItemAdapter
import com.tokopedia.order_management_common.presentation.uimodel.ProductBmgmSectionUiModel
import com.tokopedia.order_management_common.util.RecyclerViewItemDivider
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx

class BmgmSectionViewHolder(
    view: View?,
    private val listener: Listener,
    recyclerViewSharedPool: RecyclerView.RecycledViewPool
) : AbstractViewHolder<ProductBmgmSectionUiModel>(view),
    ProductBmgmItemAdapter.ViewHolder.Listener {

    companion object {
        val LAYOUT = R.layout.item_order_product_bmgm_section

        private const val ITEM_DECORATION_VERTICAL_MARGIN = 12
        private const val ITEM_DECORATION_HORIZONTAL_MARGIN = 16
    }

    private val bmgmItemAdapter = ProductBmgmItemAdapter(this, recyclerViewSharedPool)

    private val binding = ItemOrderProductBmgmSectionBinding.bind(itemView)

    init {
        setupBundleAdapter()
    }

    override fun bind(element: ProductBmgmSectionUiModel) {
        setupBmgmHeader(element.bmgmName, element.bmgmIconUrl)
        setupBmgmItems(element.bmgmItemList)
        setupBmgmTotalPrice(element.totalPriceText)
        setupBmgmTotalPriceReductionInfo(element.totalPriceReductionInfoText)
    }

    override fun bind(
        element: ProductBmgmSectionUiModel?,
        payloads: MutableList<Any>
    ) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is ProductBmgmSectionUiModel && newItem is ProductBmgmSectionUiModel) {
                    if (oldItem.bmgmName != newItem.bmgmName || oldItem.bmgmIconUrl != newItem.bmgmIconUrl) {
                        setupBmgmHeader(newItem.bmgmName, newItem.bmgmIconUrl)
                    }
                    if (oldItem.bmgmItemList != newItem.bmgmItemList) {
                        setupBmgmItems(newItem.bmgmItemList)
                    }
                    if (oldItem.totalPriceText != newItem.totalPriceText) {
                        setupBmgmTotalPrice(newItem.totalPriceText)
                    }
                    if (oldItem.totalPriceReductionInfoText != newItem.totalPriceReductionInfoText) {
                        setupBmgmTotalPriceReductionInfo(newItem.totalPriceReductionInfoText)
                    }
                    return
                }
            }
        }
    }

    override fun onCopyAddOnDescription(label: String, description: CharSequence) {
        listener.onCopyAddOnDescription(label, description)
    }

    override fun onBmgmItemClicked(item: ProductBmgmSectionUiModel.ProductUiModel) {
        if (item.orderId != OrderManagementConstants.WAITING_INVOICE_ORDER_ID) {
            listener.onBmgmItemClicked(item)
        } else {
            showToaster(getString(com.tokopedia.order_management_common.R.string.om_error_message_cant_open_snapshot_when_waiting_invoice))
        }
    }

    private fun showToaster(message: String) {
        itemView.parent?.parent?.parent?.let {
            if (it is View) {
                Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
            }
        }
    }

    private fun setupBundleAdapter() {
        binding.rvOrderBmgm.run {
            layoutManager = LinearLayoutManager(itemView.context)
            adapter = bmgmItemAdapter
            setupRecyclerViewItemDecoration()
        }
    }

    private fun setupRecyclerViewItemDecoration() {
        binding.rvOrderBmgm.run {
            if (itemDecorationCount.isZero()) {
                val dividerDrawable = try {
                    MethodChecker.getDrawable(
                        context,
                        R.drawable.om_detail_add_on_solid_divider
                    )
                } catch (t: Throwable) {
                    null
                }
                addItemDecoration(
                    RecyclerViewItemDivider(
                        dividerDrawable,
                        ITEM_DECORATION_VERTICAL_MARGIN.toPx(),
                        ITEM_DECORATION_VERTICAL_MARGIN.toPx(),
                        ITEM_DECORATION_HORIZONTAL_MARGIN.toPx(),
                    )
                )
            }
        }
    }


    private fun setupBmgmHeader(bmgmName: String, bmgmIconUrl: String) {
        binding.tvOrderBmgmName.text = bmgmName
        binding.ivOrderBmgmIcon.loadImage(bmgmIconUrl)
    }

    private fun setupBmgmItems(bmgmItemList: List<ProductBmgmSectionUiModel.ProductUiModel>) {
        bmgmItemAdapter.setItems(bmgmItemList)
    }

    private fun setupBmgmTotalPrice(price: String) {
        binding.tvOrderBmgmPriceValue.text = price
    }

    private fun setupBmgmTotalPriceReductionInfo(totalPriceReductionInfoText: String) {
        binding.tvOrderBmgmPriceMoreInfoLabel.text = totalPriceReductionInfoText
    }

    interface Listener {
        fun onCopyAddOnDescription(label: String, description: CharSequence)
        fun onBmgmItemClicked(uiModel: ProductBmgmSectionUiModel.ProductUiModel)
    }
}
