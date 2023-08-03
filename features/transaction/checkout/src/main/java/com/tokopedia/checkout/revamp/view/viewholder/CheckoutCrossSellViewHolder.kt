package com.tokopedia.checkout.revamp.view.viewholder

import android.annotation.SuppressLint
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutCrossSellBinding
import com.tokopedia.checkout.databinding.ItemCheckoutCrossSellItemBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.adapter.CrossSellAdapter
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutDonationModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEgoldModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil
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
//        LinearSnapHelper().attachToRecyclerView(binding.rvCheckoutCrossSell)
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
        renderCrossSellItem(crossSellItem)
        binding.rvCheckoutCrossSell.isVisible = false
        binding.itemCheckoutCrossSellItem.dividerCheckoutCrossSellItem.isVisible = false
        binding.itemCheckoutCrossSellItem.root.isVisible = true
    }

    private fun renderCrossSellItem(crossSellItem: CheckoutCrossSellItem) {
        when (crossSellItem) {
            is CheckoutCrossSellModel -> renderCrossSell(
                crossSellItem,
                binding.itemCheckoutCrossSellItem
            )

            is CheckoutDonationModel -> renderDonation(
                crossSellItem,
                binding.itemCheckoutCrossSellItem
            )

            is CheckoutEgoldModel -> renderEgold(crossSellItem, binding.itemCheckoutCrossSellItem)
        }
    }

    private fun renderCrossSell(
        crossSellModel: CheckoutCrossSellModel,
        itemBinding: ItemCheckoutCrossSellItemBinding
    ) {
        itemBinding.tvCheckoutCrossSellItem.text = crossSellModel.crossSellModel.info.title
    }

    private fun renderDonation(
        donationModel: CheckoutDonationModel,
        itemBinding: ItemCheckoutCrossSellItemBinding
    ) {
        itemBinding.tvCheckoutCrossSellItem.text = donationModel.donation.title
    }

    @SuppressLint("SetTextI18n")
    private fun renderEgold(
        egoldModel: CheckoutEgoldModel,
        itemBinding: ItemCheckoutCrossSellItemBinding
    ) {
        val text = "${egoldModel.egoldAttributeModel.titleText} (${
        CurrencyFormatUtil.convertPriceValueToIdrFormat(
            egoldModel.egoldAttributeModel.buyEgoldValue,
            false
        ).removeDecimalSuffix()
        })"
        if (egoldModel.egoldAttributeModel.isEnabled) {
            itemBinding.cbCheckoutCrossSellItem.isEnabled = true
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                itemBinding.root.foreground = ContextCompat.getDrawable(
                    binding.root.context,
                    com.tokopedia.purchase_platform.common.R.drawable.fg_enabled_item
                )
            }
        } else {
            itemBinding.cbCheckoutCrossSellItem.isEnabled = false
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                itemBinding.root.foreground = ContextCompat.getDrawable(
                    binding.root.context,
                    com.tokopedia.purchase_platform.common.R.drawable.fg_disabled_item
                )
            }
        }
        itemBinding.tvCheckoutCrossSellItem.text = text
//        itemBinding.root.setOnClickListener {
//            if (egoldModel.egoldAttributeModel.isEnabled) {
//                itemBinding.cbCheckoutCrossSellItem.isChecked = !itemBinding.cbCheckoutCrossSellItem.isChecked
//            }
//        }
        itemBinding.cbCheckoutCrossSellItem.setOnCheckedChangeListener { _, _ -> }
        itemBinding.cbCheckoutCrossSellItem.isChecked = egoldModel.egoldAttributeModel.isChecked
        itemBinding.cbCheckoutCrossSellItem.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (egoldModel.egoldAttributeModel.isEnabled) {
                listener.onEgoldChecked(isChecked)
            }
        }
    }

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
