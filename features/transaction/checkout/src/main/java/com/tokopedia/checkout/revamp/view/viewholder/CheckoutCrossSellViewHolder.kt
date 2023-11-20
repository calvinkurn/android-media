package com.tokopedia.checkout.revamp.view.viewholder

import android.annotation.SuppressLint
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutCrossSellBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.adapter.CrossSellAdapter
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellItem
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.model.ImpressHolder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CheckoutCrossSellViewHolder(
    private val binding: ItemCheckoutCrossSellBinding,
    private val listener: CheckoutAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    private val adapter: CrossSellAdapter = CrossSellAdapter(listener)

    init {
        binding.rvCheckoutCrossSell.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCheckoutCrossSell.adapter = adapter
        LinearSnapHelper().attachToRecyclerView(binding.rvCheckoutCrossSell)
    }

    fun bind(checkoutCrossSellGroupModel: CheckoutCrossSellGroupModel) {
        if (checkoutCrossSellGroupModel.crossSellList.isEmpty()) {
            binding.dividerCheckoutCrossSell.isVisible = false
            binding.rvCheckoutCrossSell.isVisible = false
            binding.itemCheckoutCrossSellItem.root.isVisible = false
        } else if (checkoutCrossSellGroupModel.crossSellList.size == 1) {
            binding.dividerCheckoutCrossSell.isVisible = true
            val crossSellItem = checkoutCrossSellGroupModel.crossSellList[0]
            renderCrossSellSingleItem(crossSellItem)
        } else {
            renderCrossSellItems(checkoutCrossSellGroupModel)
            binding.dividerCheckoutCrossSell.isVisible = true
            binding.rvCheckoutCrossSell.isVisible = true
            binding.itemCheckoutCrossSellItem.root.isVisible = false
        }
    }

    private fun renderCrossSellSingleItem(crossSellItem: CheckoutCrossSellItem) {
        CheckoutCrossSellItemView.renderCrossSellItem(crossSellItem, binding.itemCheckoutCrossSellItem, listener)
        binding.rvCheckoutCrossSell.isVisible = false
        binding.itemCheckoutCrossSellItem.dividerCheckoutCrossSellItem.isVisible = false
        binding.itemCheckoutCrossSellItem.root.isVisible = true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderCrossSellItems(checkoutCrossSellGroupModel: CheckoutCrossSellGroupModel) {
        var parentWidth = binding.root.width
        if (parentWidth <= 0) {
            parentWidth = listener.getParentWidth()
        }
        if (parentWidth <= 0) {
            parentWidth = 500.dpToPx(binding.root.context.resources.displayMetrics)
        }
        val oldList = adapter.list
        adapter.parentWidth = parentWidth
        val newList = checkoutCrossSellGroupModel.crossSellList
        adapter.list = newList
        if (oldList.size != newList.size) {
            adapter.notifyDataSetChanged()
        } else {
            adapter.notifyItemRangeChanged(0, newList.size)
        }
        if (checkoutCrossSellGroupModel.shouldTriggerScrollInteraction) {
            binding.rvCheckoutCrossSell.addOnImpressionListener(ImpressHolder()) {
                if (checkoutCrossSellGroupModel.shouldTriggerScrollInteraction) {
                    checkoutCrossSellGroupModel.shouldTriggerScrollInteraction = false
                    GlobalScope.launch {
                        delay(500)
                        val scrollX = 80.dpToPx(binding.root.context.resources.displayMetrics)
                        binding.rvCheckoutCrossSell.smoothScrollBy(
                            scrollX,
                            0
                        )
                        delay(500)
                        binding.rvCheckoutCrossSell.smoothScrollBy(
                            scrollX * -1,
                            0
                        )
                    }
                }
            }
        }
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_cross_sell
    }
}
