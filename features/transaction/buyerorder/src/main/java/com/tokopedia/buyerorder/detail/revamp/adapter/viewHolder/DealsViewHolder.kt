package com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder

import android.view.View
import androidx.annotation.LayoutRes
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.common.util.ApplinkOMSConstant
import com.tokopedia.buyerorder.databinding.VoucherItemDealsBinding
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.data.ItemsDeals
import com.tokopedia.buyerorder.detail.data.MetaDataInfo
import com.tokopedia.buyerorder.detail.data.OrderDetails
import com.tokopedia.buyerorder.detail.revamp.adapter.EventDetailsListener
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.DELIMITERS
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.KEY_TEXT
import com.tokopedia.buyerorder.detail.revamp.widget.RedeemVoucherView
import com.tokopedia.buyerorder.detail.view.customview.BookingCodeView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImageCircle

/**
 * created by @bayazidnasir on 24/8/2022
 */

class DealsViewHolder(
    itemView: View,
    private val gson: Gson,
    private val eventDetailsListener: EventDetailsListener,
): AbstractViewHolder<ItemsDeals>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.voucher_item_deals
        const val ITEM_DEALS = 1
    }

    override fun bind(element: ItemsDeals) {
        val binding = VoucherItemDealsBinding.bind(itemView)
        val metadata = element.item.getMetaDataInfo(gson)

        renderProducts(binding, metadata, element.item, element.orderDetails)
        setTapAction(binding, element.item)
    }

    private fun renderProducts(
        binding: VoucherItemDealsBinding,
        metadata: MetaDataInfo,
        item: Items,
        orderDetails: OrderDetails
    ){
        binding.ivDeal.loadImageCircle(metadata.entityImage.ifEmpty { item.imageUrl })
        binding.tvDealIntro.text = metadata.entityProductName.ifEmpty { item.title }

        eventDetailsListener.sendThankYouEvent(metadata, ITEM_DEALS, orderDetails)
        eventDetailsListener.sendOpenScreenDeals(false)

        binding.llValid.shouldShowWithAction(metadata.endDate.isNotEmpty()){
            binding.tvValidTillDate.text = getString(R.string.order_detail_date_whitespace, metadata.endDate)
        }

        eventDetailsListener.setDealsBanner(metadata)

        if (item.actionButtons.isNotEmpty()){
            eventDetailsListener.setEventDetails(item.actionButtons.first(), item)
        }

        binding.tvBrandName.text = metadata.entityBrandName
        eventDetailsListener.setDetailTitle(getString(R.string.detail_label))
        binding.customView1.setOnClickListener {
            RouteManager.route(itemView.context, ApplinkOMSConstant.INTERNAL_DEALS+metadata.seoUrl)
        }
    }

    private fun setTapAction(
        binding: VoucherItemDealsBinding,
        item: Items,
    ){
        if (item.tapActions.isNotEmpty() && !item.isTapActionsLoaded) {
            with(binding){
                progBar.visible()
                tapActionDeals.gone()
                customView2.gone()
            }
            eventDetailsListener.setActionButtonGql(item.tapActions, adapterPosition, flag = true, true)
        } else {
            if (item.trackingNumber.isNotEmpty()){
                val codes = item.trackingNumber.split(DELIMITERS)
                if (codes.isEmpty()){
                    return
                }

                binding.voucerCodeLayout.visible()
                renderBookingCode(binding, codes, getString(R.string.voucher_code_title))
            } else {
                with(binding){
                    progBar.gone()
                    tapActionDeals.gone()
                    customView2.gone()
                }
            }
        }

        if (item.isTapActionsLoaded){
            with(binding){
                progBar.gone()
                customView2.visible()
                tapActionDeals.visible()
                tapActionDeals.removeAllViews()

                item.actionButtons.forEachIndexed { index, actionButton ->
                    if (actionButton.control.equals(KEY_TEXT, true)) {
                        renderRedeemVoucher(binding, actionButton, item, index)
                    } else {
                        val voucherCodes = actionButton.headerObject.voucherCodes.split(DELIMITERS)
                        renderBookingCode(binding, voucherCodes, actionButton.headerObject.itemLabel)
                    }
                }
            }
        }
    }

    private fun renderRedeemVoucher(
        binding: VoucherItemDealsBinding,
        actionButton: ActionButton,
        item: Items,
        index: Int
    ) {
        val redeemView = RedeemVoucherView(
            itemView.context,
            index,
            false,
            actionButton,
            item,
            { textView, items, count ->
                eventDetailsListener.onTapActionDeals(textView, actionButton, items, count, adapterPosition)
            },
            {
                eventDetailsListener.showRetryButtonToaster(it)
            }
        )
        binding.tapActionDeals.addView(redeemView)
    }

    private fun renderBookingCode(
        binding: VoucherItemDealsBinding,
        voucherCodes: List<String>,
        bookingCodeTitle: String,
    ) {
        voucherCodes.forEachIndexed { index, code ->
            val bookingCodeView = BookingCodeView(itemView.context,code, index, bookingCodeTitle, voucherCodes.size )
            binding.tapActionDeals.addView(bookingCodeView)
        }
    }
}
