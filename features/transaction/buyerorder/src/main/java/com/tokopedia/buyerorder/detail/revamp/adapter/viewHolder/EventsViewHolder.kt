package com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.unifyprinciples.R as unifyPrinciplesR
import com.tokopedia.buyerorder.common.util.ApplinkOMSConstant
import com.tokopedia.buyerorder.common.util.BuyerUtils.clickActionButton
import com.tokopedia.buyerorder.databinding.VoucherItemCardEventsBinding
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.data.ItemsEvents
import com.tokopedia.buyerorder.detail.data.MetaDataInfo
import com.tokopedia.buyerorder.detail.view.customview.BookingCodeView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.unifyprinciples.Typography

/**
 * created by @bayazidnasir on 23/8/2022
 */

class EventsViewHolder(itemView: View): AbstractViewHolder<ItemsEvents>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.voucher_item_card_events

        private const val IS_ENTERTAIN = 1
        private const val ZERO_QUANTITY = 0
        private const val E_TICKET_FORMAT = "%s %s"
        private const val STROKE_WIDTH = 1
        private const val ZERO_MARGIN = 0
        private const val KEY_REFRESH = "refresh"
        private const val KEY_VOUCHER_CODE = "vouchercodes"
        private const val KEY_BUTTON = "button"
        private const val KEY_REDIRECT = "redirect"
        private const val CONTENT_TYPE = "application/pdf"
        private const val KEY_QRCODE = "qrcode"
        private const val KEY_POPUP = "popup"
    }

    override fun bind(element: ItemsEvents) {
        val binding = VoucherItemCardEventsBinding.bind(itemView)
        val metadata = getMetadata(element.item)

        renderProducts(binding, metadata, element.item)
        renderAddress(binding, metadata)
        renderTime(binding, metadata)
        renderInfoName(binding, metadata, element.item)

        if (element.orderDetails.actionButtons.isNotEmpty()){
            //TODO hit action button event
        }
        //TODO : setPassengerEvent
        //TODO : setDetailTitleEvent

        binding.customView1.setOnClickListener {
            RouteManager.route(itemView.context, ApplinkOMSConstant.INTERNAL_DEALS+metadata.seoUrl)
        }

        setTapActionButtons(binding, metadata, element.item)
    }

    private fun renderProducts(
        binding: VoucherItemCardEventsBinding,
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
        binding: VoucherItemCardEventsBinding,
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
        binding: VoucherItemCardEventsBinding,
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
        binding: VoucherItemCardEventsBinding,
        metadata: MetaDataInfo,
        item: Items,
    ){
        binding.llValid.shouldShowWithAction(item.category.isNotEmpty()){
            binding.tvValidTillDate.text = getString(R.string.order_detail_date_whitespace, metadata.name)
        }
    }

    private fun setTapActionButtons(
        binding: VoucherItemCardEventsBinding,
        metadata: MetaDataInfo,
        item: Items,
    ){
        if (!item.isTapActionsLoaded){
            binding.progBar.gone()
            binding.customView2.visible()
            binding.tapActionEvents.visible()
            binding.tapActionEvents.removeAllViews()
            val totalTicketCount = metadata.quantity

            if (item.actionButtons.size == ZERO_QUANTITY) setETicket(binding, totalTicketCount)
            item.actionButtons.forEachIndexed { index, actionButton ->
                val actionTextView = renderActionButtons(index, actionButton, item)
                if (actionButton.control.equals(KEY_REFRESH, true)) {
                    //TODO : refactor redeemVoucher
                } else if (actionButton.control.equals(KEY_VOUCHER_CODE, true)) {
                    if (actionButton.body.body.isNotEmpty()){
                        val voucherCodes = actionButton.body.body.split(",")
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
                } else {
                    if (actionButton.control.equals(KEY_BUTTON, true)) {
                        //TODO : hit action Button gql
                    } else {
                        setActionButtonClick(actionTextView, actionButton, item)
                    }
                    binding.tapActionEvents.addView(actionTextView)
                }
                setEventInfo(binding, actionButton, totalTicketCount)
            }

        }
    }

    private fun setETicket(binding: VoucherItemCardEventsBinding, totalCount: Int){
        if (totalCount > ZERO_QUANTITY) {
            binding.tvBrandName.text = String.format(
                E_TICKET_FORMAT,
                totalCount,
                getString(R.string.event_ticket_voucher_multiple))
        } else {
            binding.tvBrandName.text = getString(R.string.event_ticket_voucher_count)
        }
    }

    private fun setEventInfo(binding: VoucherItemCardEventsBinding, actionButton: ActionButton, totalCount: Int){
        when{
            actionButton.control.equals(KEY_REDIRECT, true)
                    || actionButton.control.equals(KEY_REFRESH, true) -> {
                        setETicket(binding, totalCount)
                    }
            actionButton.control.equals(KEY_POPUP, true) -> {
                if (totalCount > ZERO_QUANTITY) {
                    binding.tvBrandName.text = String.format(
                        E_TICKET_FORMAT,
                        totalCount,
                        getString(R.string.event_ticket_qrcode_multiple)
                    )
                } else {
                    binding.tvBrandName.text = getString(R.string.event_ticket_qrcode_count)
                }
            }
            actionButton.control.equals(KEY_VOUCHER_CODE, true) -> {
                if (totalCount > ZERO_QUANTITY) {
                    binding.tvBrandName.text = String.format(
                        E_TICKET_FORMAT,
                        totalCount,
                        getString(R.string.event_ticket_booking_multiple)
                    )
                } else {
                    binding.tvBrandName.text = getString(R.string.event_ticket_booking_count)
                }
            }
        }
    }

    private fun renderActionButtons(
        position: Int,
        actionButton: ActionButton,
        item: Items,
    ) : Typography {
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(
                ZERO_MARGIN,
                getDimension(com.tokopedia.resources.common.R.dimen.dp_8),
                ZERO_MARGIN,
                ZERO_MARGIN,
            )
        }

        return Typography(itemView.context).apply {
            setPadding(
                getDimension(unifyPrinciplesR.dimen.unify_space_16),
                getDimension(unifyPrinciplesR.dimen.unify_space_16),
                getDimension(unifyPrinciplesR.dimen.unify_space_16),
                getDimension(unifyPrinciplesR.dimen.unify_space_16),
            )
            setTextColor(MethodChecker.getColor(context, unifyPrinciplesR.color.Unify_N0))
            layoutParams = params
            gravity = Gravity.CENTER_HORIZONTAL
            text = actionButton.label

            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE

            if (actionButton.actionColor.background.isNotEmpty()) {
                shape.setColor(Color.parseColor(actionButton.actionColor.background))
            } else {
                shape.setColor(MethodChecker.getColor(context, unifyPrinciplesR.color.Unify_G400))
            }

            if (actionButton.actionColor.border.isNotEmpty()) {
                shape.setStroke(
                    STROKE_WIDTH,
                    Color.parseColor(actionButton.actionColor.border)
                )
            }

            if (actionButton.actionColor.textColor.isNotEmpty()) {
                setTextColor(Color.parseColor(actionButton.actionColor.textColor))
            } else {
                setTextColor(MethodChecker.getColor(context, unifyPrinciplesR.color.Unify_N0))
            }

            if (position == item.actionButtons.size - 1 &&  item.actionButtons.isEmpty()){
                val radius = context.resources.getDimension(unifyPrinciplesR.dimen.unify_space_4)
                shape.cornerRadii = floatArrayOf(0F, 0F, 0F, 0F, radius, radius, radius, radius)
            } else {
                shape.cornerRadius = context.resources.getDimension(unifyPrinciplesR.dimen.unify_space_4)
            }

            background = shape
        }
    }

    private fun setActionButtonClick(textView: TextView?, actionButton: ActionButton, item: Items){

        fun isDownloadable(actionButton: ActionButton): Boolean{
            return if (actionButton.header.isNotEmpty()){
                return actionButton.headerObject.contentType.equals(CONTENT_TYPE, true)
            } else {
                false
            }
        }

        when{
            actionButton.control.equals(KEY_REDIRECT, true) -> {
                if (actionButton.body.body.isEmpty() && actionButton.body.appURL.isEmpty()){
                    return
                }
                when{
                    textView == null -> {
                        RouteManager.route(itemView.context, actionButton.body.appURL)
                    }
                    isDownloadable(actionButton) -> {
                        textView.setOnClickListener {
                            clickActionButton(itemView.context, actionButton.body.appURL, true,){
                                //TODO : ASK permission
                            }
                            //TODO : set uri pdp using @params uri
                        }
                    }
                    else -> {
                        textView.setOnClickListener {
                            clickActionButton(itemView.context, actionButton.body.appURL, false){
                                //TODO : ASK permission
                            }
                        }
                    }
                }
            }
            actionButton.control.equals(KEY_QRCODE, true) -> {
                textView?.setOnClickListener {
                    //TODO : openshoQRfrag
                }
            }
            actionButton.control.equals(KEY_POPUP, true) -> {
                textView?.setOnClickListener {
                    //TODO : openshoQRfrag
                }
            }
        }
    }

    private fun getMetadata(item: Items): MetaDataInfo {
        val gson = Gson()
        return gson.fromJson(item.metaData, MetaDataInfo::class.java)
    }

    private fun getDimension(@DimenRes dimenId: Int): Int{
        return itemView.context.resources.getDimension(dimenId).toIntSafely()
    }
}