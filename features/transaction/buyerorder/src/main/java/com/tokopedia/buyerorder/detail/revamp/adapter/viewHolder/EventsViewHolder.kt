package com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.common.util.ApplinkOMSConstant
import com.tokopedia.buyerorder.common.util.BuyerUtils.clickActionButton
import com.tokopedia.buyerorder.databinding.VoucherItemCardEventsNewBinding
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.data.ItemsEvents
import com.tokopedia.buyerorder.detail.data.MetaDataInfo
import com.tokopedia.buyerorder.detail.revamp.adapter.EventDetailsListener
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.CONTENT_TYPE
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.DELIMITERS
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.E_TICKET_FORMAT
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.IS_ENTERTAIN
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.ITEM_EVENTS
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.KEY_BUTTON
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.KEY_POPUP
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.KEY_QRCODE
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.KEY_REDIRECT
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.KEY_REFRESH
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.KEY_VOUCHER_CODE
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.ZERO_QUANTITY
import com.tokopedia.buyerorder.detail.revamp.util.Utils.renderActionButtons
import com.tokopedia.buyerorder.detail.revamp.widget.RedeemVoucherView
import com.tokopedia.buyerorder.detail.view.customview.BookingCodeView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImageCircle

/**
 * created by @bayazidnasir on 23/8/2022
 */

class EventsViewHolder(
    itemView: View,
    private val gson: Gson,
    private val eventDetailsListener: EventDetailsListener,
): AbstractViewHolder<ItemsEvents>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.voucher_item_card_events_new
    }

    override fun bind(element: ItemsEvents) {
        val binding = VoucherItemCardEventsNewBinding.bind(itemView)
        val metadata = element.item.getMetaDataInfo(gson)

        eventDetailsListener.sendThankYouEvent(metadata, ITEM_EVENTS, element.orderDetails)

        renderProducts(binding, metadata, element.item)
        renderAddress(binding, metadata)
        renderTime(binding, metadata)
        renderInfoName(binding, metadata, element.item)

        if (element.orderDetails.actionButtons.isNotEmpty()){
            eventDetailsListener.setActionButtonEvent(element.orderDetails.actionButtons.first(), element.item, element.orderDetails)
        }

        eventDetailsListener.setPassengerEvent(element.item)
        eventDetailsListener.setDetailTitle(getString(R.string.detail_label_events))

        binding.customView1.setOnClickListener {
            RouteManager.route(itemView.context, ApplinkOMSConstant.INTERNAL_DEALS+metadata.seoUrl)
        }

        setTapActionButtons(binding, metadata, element.item)
    }

    private fun renderProducts(
        binding: VoucherItemCardEventsNewBinding,
        metadata: MetaDataInfo,
        item: Items,
    ) {
        if (metadata.productImage.isNotEmpty()) {
            binding.ivDeal.loadImageCircle(metadata.productImage)
        } else {
            binding.ivDeal.loadImageCircle(metadata.entityImage)
        }

        if (metadata.entityProductName.isNotEmpty()) {
            binding.tvDealIntro.text = metadata.entityProductName
        } else {
            binding.tvDealIntro.text = item.title
        }
    }

    private fun renderAddress(
        binding: VoucherItemCardEventsNewBinding,
        metadata: MetaDataInfo
    ){
        if (metadata.locationName.isNotEmpty()){
            binding.cityEvent.text = metadata.locationName
        }

        if (metadata.locationDesc.isNotEmpty()){
            binding.addressEvent.text = metadata.locationDesc
        }
    }

    private fun renderTime(
        binding: VoucherItemCardEventsNewBinding,
        metadata: MetaDataInfo
    ){
        if (metadata.isHiburan == IS_ENTERTAIN) {
            if (metadata.endTime.isNotEmpty()){
                binding.tanggalEventsTitle.visible()
                binding.tanggalEventsTitle.text = getString(R.string.text_valid_till)
                binding.tanggalEvents.text = metadata.endTime
            }
        } else {
            if (metadata.endTime.isNotEmpty() && metadata.startTime.isNotEmpty()){
                binding.tanggalEventsTitle.visible()
                binding.tanggalEventsTitle.text = getString(R.string.tanggal_events)
                binding.tanggalEvents.text = itemView.context.getString(R.string.event_date_label, metadata.startTime, metadata.endTime)
            }
        }
    }

    private fun renderInfoName(
        binding: VoucherItemCardEventsNewBinding,
        metadata: MetaDataInfo,
        item: Items,
    ){
        binding.llValid.shouldShowWithAction(item.category.isNotEmpty()){
            binding.tvValidTillDate.text = getString(R.string.order_detail_date_whitespace, metadata.name)
        }
    }

    private fun setTapActionButtons(
        binding: VoucherItemCardEventsNewBinding,
        metadata: MetaDataInfo,
        item: Items,
    ){
        if (!item.isTapActionsLoaded){
            binding.progBar.gone()
            binding.customView2.visible()
            binding.tapActionEvents.visible()
            binding.tapActionEvents.removeAllViews()
            val totalTicketCount = metadata.quantity

            if (item.actionButtons.size == ZERO_QUANTITY) {
                renderBrandName(
                    binding,
                    totalTicketCount,
                    getString(R.string.event_ticket_voucher_multiple),
                    getString(R.string.event_ticket_voucher_count)
                )
            }

            setActionButton(binding, item, totalTicketCount)

        }
    }

    private fun setActionButton(binding: VoucherItemCardEventsNewBinding, item: Items, totalCount: Int) {
        item.actionButtons.forEachIndexed { index, actionButton ->
            when{
                actionButton.control.equals(KEY_REFRESH, true) -> {
                    renderRedeemVoucher(binding, actionButton, item, index)
                }
                actionButton.control.equals(KEY_VOUCHER_CODE, true) -> {
                    renderBookingCode(binding, actionButton)
                }
                else -> {
                    setTapActions(binding, actionButton, item, index)
                }
            }
            setEventInfo(binding, actionButton, totalCount)
        }
    }

    private fun renderRedeemVoucher(
        binding: VoucherItemCardEventsNewBinding,
        actionButton: ActionButton,
        item: Items,
        index: Int,
    ) {
        val redeemView = RedeemVoucherView(
            itemView.context,
            index,
            false,
            actionButton,
            item,
            { textView,  items, count ->
                eventDetailsListener.onTapActionDeals(textView, actionButton, items, count, adapterPosition)
            },
            {
                eventDetailsListener.showRetryButtonToaster(it)
            }
        )
        binding.tapActionEvents.addView(redeemView)
    }

    private fun renderBookingCode(
        binding: VoucherItemCardEventsNewBinding,
        actionButton: ActionButton,
    ) {
        if (actionButton.body.body.isNotEmpty()){
            val voucherCodes = actionButton.body.body.split(DELIMITERS)
            if (voucherCodes.isNotEmpty()){
                binding.voucerCodeLayout.visible()
                voucherCodes.forEachIndexed { i, voucher ->
                    val bookingCodeView = BookingCodeView(
                        itemView.context,
                        voucher,
                        i,
                        getString(R.string.voucher_code_title),
                        voucherCodes.size
                    ).apply {
                        background = null
                    }
                    binding.voucerCodeLayout.addView(bookingCodeView)
                }
            }
        }
    }

    private fun setTapActions(
        binding: VoucherItemCardEventsNewBinding,
        actionButton: ActionButton,
        item: Items,
        index: Int,
    ) {
        val actionTextView = renderActionButtons(itemView.context, index, actionButton, item)
        if (actionButton.control.equals(KEY_BUTTON, true)) {
            eventDetailsListener.setActionButtonGql(item.tapActions, adapterPosition, flag = true, true)
        } else {
            setActionButtonClick(actionTextView, actionButton, item)
        }
        binding.tapActionEvents.addView(actionTextView)
    }

    private fun setEventInfo(binding: VoucherItemCardEventsNewBinding, actionButton: ActionButton, totalCount: Int){
        when{
            actionButton.control.equals(KEY_REDIRECT, true)
            || actionButton.control.equals(KEY_REFRESH, true) -> {
                renderBrandName(
                    binding,
                    totalCount,
                    getString(R.string.event_ticket_voucher_multiple),
                    getString(R.string.event_ticket_voucher_count)
                )
            }
            actionButton.control.equals(KEY_POPUP, true) -> {
                renderBrandName(
                    binding,
                    totalCount,
                    getString(R.string.event_ticket_qrcode_multiple),
                    getString(R.string.event_ticket_qrcode_count)
                )
            }
            actionButton.control.equals(KEY_VOUCHER_CODE, true) -> {
                renderBrandName(
                    binding,
                    totalCount,
                    getString(R.string.event_ticket_booking_multiple),
                    getString(R.string.event_ticket_booking_count)
                )
            }
        }
    }

    private fun renderBrandName(
        binding: VoucherItemCardEventsNewBinding,
        totalCount: Int,
        brandLabel: String,
        brandName: String
    ) {
        if (totalCount > ZERO_QUANTITY) {
            binding.tvBrandName.text = String.format(
                E_TICKET_FORMAT,
                totalCount,
                brandLabel
            )
        } else {
            binding.tvBrandName.text = brandName
        }
    }

    private fun setActionButtonClick(textView: TextView?, actionButton: ActionButton, item: Items){

        when{
            actionButton.control.equals(KEY_REDIRECT, true) -> {
                if (actionButton.body.body.isEmpty() && actionButton.body.appURL.isEmpty()){
                    return
                }
                viewClick(textView, actionButton)
            }
            actionButton.control.equals(KEY_QRCODE, true) || actionButton.control.equals(KEY_POPUP, true) -> {
                textView?.setOnClickListener {
                    eventDetailsListener.openQRFragment(actionButton, item)
                }
            }
        }
    }

    private fun viewClick(textView: TextView?, actionButton: ActionButton) {

        val isDownloadable = if (actionButton.header.isNotEmpty()){
            actionButton.headerObject.contentType.equals(CONTENT_TYPE, true)
        } else {
            false
        }

        when{
            textView == null -> {
                RouteManager.route(itemView.context, actionButton.body.appURL)
            }
            isDownloadable -> {
                textView.setOnClickListener {
                    clickActionButton(itemView.context, actionButton.body.appURL, true,){
                        eventDetailsListener.askPermission(it, true, getString(R.string.oms_order_detail_ticket_title))
                    }
                }
            }
            else -> {
                textView.setOnClickListener {
                    clickActionButton(itemView.context, actionButton.body.appURL, false){
                        eventDetailsListener.askPermission(it, false, "")
                    }
                }
            }
        }
    }
}