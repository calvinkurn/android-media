package com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.common.util.BuyerUtils.clickActionButton
import com.tokopedia.buyerorder.databinding.VoucherItemDefaultBinding
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.data.ItemsDefault
import com.tokopedia.buyerorder.detail.data.MetaDataInfo
import com.tokopedia.buyerorder.detail.revamp.adapter.EventDetailsListener
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.ITEMS_DEALS
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.KEY_BUTTON
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.KEY_REDIRECT
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.KEY_TEXT
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.ZERO_QUANTITY
import com.tokopedia.buyerorder.detail.revamp.util.Utils.renderActionButtons
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.visible

/**
 * created by @bayazidnasir on 23/8/2022
 */

class DefaultViewHolder(
    itemView: View,
    private val gson: Gson,
    private val eventDetailsListener: EventDetailsListener,
) : AbstractViewHolder<ItemsDefault>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.voucher_item_default
    }

    private var hasViews = false

    override fun bind(element: ItemsDefault) {
        val binding = VoucherItemDefaultBinding.bind(itemView)
        val metadata = element.item.getMetaDataInfo(gson)

        eventDetailsListener.sendThankYouEvent(metadata, ITEMS_DEALS, element.orderDetails)
        eventDetailsListener.sendOpenScreenDeals(false)
        eventDetailsListener.setDetailTitle(getString(R.string.purchase_detail))

        renderProductName(binding, metadata)
        renderEntityPackage(binding, metadata)
        renderQuantity(binding, metadata, element.item)
        renderDate(binding, metadata)
        requestTapActions(binding, element.item)
        setDivider(binding)
        setTapActions(binding, element.item)
        setButtonActions(binding, element.item)
    }

    private fun renderProductName(
        binding: VoucherItemDefaultBinding,
        metadata: MetaDataInfo,
    ){
        if (metadata.entityProductName.isNotEmpty()) {
            hasViews = true
            binding.rightEvent.text = metadata.entityProductName
        } else {
            binding.llEvent.gone()
        }
    }

    private fun renderEntityPackage(
        binding: VoucherItemDefaultBinding,
        metadata: MetaDataInfo,
    ){
        if (metadata.entityPackages.isNotEmpty()) {
            if (metadata.entityPackages.first().displayName.isEmpty()) {
                binding.llCategoryTicket.gone()
            } else {
                binding.rightCategoryTicket.text = metadata.entityPackages.first().displayName
                hasViews = true
            }
        } else {
            binding.llCategoryTicket.gone()
        }
    }

    private fun renderQuantity(
        binding: VoucherItemDefaultBinding,
        metadata: MetaDataInfo,
        item: Items,
    ){
        if (item.quantity == ZERO_QUANTITY) {
            binding.llTotalTicket.gone()
        } else {
            hasViews = true
            binding.rightTotalTicket.text = metadata.totalTicketCount.toString()
        }
    }

    private fun renderDate(
        binding: VoucherItemDefaultBinding,
        metadata: MetaDataInfo,
    ){
        binding.llValid.shouldShowWithAction(metadata.endDate.isNotEmpty()){
            binding.tvValidTillDate.text = getString(R.string.order_detail_date_whitespace, metadata.endDate)
            hasViews = true
        }

        if (metadata.startDate.isNotEmpty()){
            hasViews = true
            binding.tvStartDate.text = getString(R.string.order_detail_date_whitespace, metadata.startDate)
            binding.llTanggalEvent.visible()
        } else {
            binding.llTanggalEvent.gone()
        }
    }

    private fun requestTapActions(
        binding: VoucherItemDefaultBinding,
        item: Items,
    ){
        if (item.tapActions.isNotEmpty() && !item.isTapActionsLoaded){
            binding.progBar.visible()
            binding.tapAction.gone()
            eventDetailsListener.setActionButtonGql(item.tapActions, adapterPosition, flag = true, true)
        }
    }

    private fun setDivider(binding: VoucherItemDefaultBinding){
        if (!hasViews) {
            binding.customView1.gone()
            binding.divider1.gone()
        } else {
            binding.customView1.visible()
            binding.divider1.visible()
        }
    }

    private fun setTapActions(
        binding: VoucherItemDefaultBinding,
        item: Items,
    ){
        if (item.isTapActionsLoaded) {
            binding.progBar.gone()
            if (item.tapActions.size == ZERO_QUANTITY) {
                binding.tapAction.gone()
            } else {
                renderTapActions(binding, item)
            }
        } else if (item.tapActions.isEmpty()){
            binding.progBar.gone()
        }
    }

    private fun renderTapActions(binding: VoucherItemDefaultBinding, item: Items) {
        binding.tapAction.visible()
        binding.tapAction.removeAllViews()
        item.tapActions.forEachIndexed { i, actionButton ->
            val actionTextView = renderActionButtons(itemView.context, i, actionButton, item)
            if (actionButton.control.equals(KEY_BUTTON, true)){
                eventDetailsListener.setActionButtonGql(item.actionButtons, adapterPosition, flag = true, true)
            } else {
                setActionButtonClick(actionTextView, actionButton)
            }
            binding.tapAction.addView(actionTextView)
        }
    }

    private fun setButtonActions(
        binding: VoucherItemDefaultBinding,
        item: Items,
    ){
        if (item.actionButtons.isEmpty()) {
            binding.actionButton.gone()
        } else {
            renderButtonActions(binding, item)
        }
    }

    private fun renderButtonActions(binding: VoucherItemDefaultBinding, item: Items) {
        binding.actionButton.visible()
        binding.actionButton.removeAllViews()
        item.actionButtons.forEachIndexed { index, actionButton ->
            val actionTextView = renderActionButtons(itemView.context, index, actionButton, item)

            if (actionButton.control.equals(KEY_TEXT, true)){
                return@forEachIndexed
            }

            if (item.isActionButtonLoaded) {
                setActionButtonClick(null, actionButton)
            } else {
                actionTextView.setOnClickListener {
                    if (actionButton.control.equals(KEY_BUTTON, true)){
                        eventDetailsListener.setActionButtonGql(item.actionButtons, adapterPosition, flag = true, true)
                    } else {
                        setActionButtonClick(actionTextView, actionButton)
                    }
                }
            }

            binding.actionButton.addView(actionTextView)
        }
    }

    private fun setActionButtonClick(
        textView: TextView?,
        actionButton: ActionButton,
    ) {
        if (!actionButton.control.equals(KEY_REDIRECT, true)){
            return
        }

        if (actionButton.body.toString().isEmpty() && actionButton.body.appURL.isEmpty()){
            return
        }

        if (textView == null) {
            RouteManager.route(itemView.context, actionButton.body.appURL)
        } else {
            textView.setOnClickListener {
                clickActionButton(
                    itemView.context,
                    actionButton.body.appURL,
                    false
                ){ uri ->
                    eventDetailsListener.askPermission(uri, false, "")
                }
            }
        }
    }

}