package com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder

import android.view.View
import androidx.annotation.LayoutRes
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.common.util.ApplinkOMSConstant
import com.tokopedia.buyerorder.databinding.VoucherItemDealsBinding
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.data.ItemsDeals
import com.tokopedia.buyerorder.detail.data.MetaDataInfo
import com.tokopedia.buyerorder.detail.data.OrderDetails
import com.tokopedia.buyerorder.detail.revamp.adapter.EventDetailsListener
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
    private val eventDetailsListener: EventDetailsListener
): AbstractViewHolder<ItemsDeals>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.voucher_item_deals

        private const val KEY_TEXT = "text"
        private const val DELIMITERS = ","
        private const val ITEM_DEALS = 1
    }

    override fun bind(element: ItemsDeals) {
        val binding = VoucherItemDealsBinding.bind(itemView)
        val metadata = Gson().fromJson(element.item.metaData, MetaDataInfo::class.java)

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

        eventDetailsListener.setDealsBanner(item)

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
            eventDetailsListener.setActionButtonGql(item.tapActions, adapterPosition, true)
        } else {
            if (item.trackingNumber.isNotEmpty()){
                val codes = item.trackingNumber.split(DELIMITERS)
                if (codes.isEmpty()){
                    return
                }

                binding.voucerCodeLayout.visible()
                codes.forEachIndexed { index, code ->
                    val bookingCodeView = BookingCodeView(
                        itemView.context,
                        code,
                        index,
                        getString(R.string.voucher_code_title),
                        codes.size).apply {
                        background = null
                    }
                    binding.voucerCodeLayout.addView(bookingCodeView)
                }
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
                        //TODO : create and add redeemVoucherView
                    } else {
                        val voucherCodes = actionButton.headerObject.voucherCodes.split(DELIMITERS)
                        voucherCodes.forEachIndexed { i, code ->
                            val bookingCodeView = BookingCodeView(itemView.context,code, i, actionButton.headerObject.itemLabel, voucherCodes.size )
                            binding.tapActionDeals.addView(bookingCodeView)
                        }
                    }
                }
            }
        }

    }
}