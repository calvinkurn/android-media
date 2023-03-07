package com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder

import android.view.View
import androidx.annotation.LayoutRes
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.databinding.VoucherItemCardDealsBinding
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.data.ItemsDealsOMP
import com.tokopedia.buyerorder.detail.data.MetaDataInfo
import com.tokopedia.buyerorder.detail.data.OrderDetails
import com.tokopedia.buyerorder.detail.revamp.adapter.EventDetailsListener
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.DELIMITERS
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.KEY_POPUP
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.KEY_REDIRECT
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.KEY_REDIRECT_EXTERNAL
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.KEY_VOUCHER_CODE
import com.tokopedia.buyerorder.detail.revamp.util.Utils.renderActionButtons
import com.tokopedia.buyerorder.detail.revamp.widget.RedeemVoucherView
import com.tokopedia.buyerorder.detail.view.customview.BookingCodeView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImageCircle
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * created by @bayazidnasir on 23/8/2022
 */

class DealsOMPViewHolder(
    itemView: View,
    private val gson: Gson,
    private val eventDetailsListener: EventDetailsListener,
) : AbstractViewHolder<ItemsDealsOMP>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.voucher_item_card_deals

        private const val DATE_FORMAT = " dd MMM yyyy hh:mm"
    }

    override fun bind(element: ItemsDealsOMP) {
        val binding = VoucherItemCardDealsBinding.bind(itemView)
        val metadata = element.item.getMetaDataInfo(gson)

        renderProducts(binding, metadata, element.item)
        setActionButton(binding, element.item, element.orderDetails)
    }

    private fun renderProducts(
        binding: VoucherItemCardDealsBinding,
        metadata: MetaDataInfo,
        item: Items,
    ){
        binding.ivDeal.loadImageCircle(metadata.productImage.ifEmpty { item.imageUrl })
        binding.tvDealIntro.text = metadata.name.ifEmpty { item.title }
        binding.tvBrandName.text = metadata.productName

        eventDetailsListener.sendOpenScreenDeals(true)

        binding.llValid.shouldShowWithAction(metadata.endTime.isNotEmpty()){
            binding.tvValidTillDate.text = getTimeMillis(metadata.endDate)
        }

        eventDetailsListener.setDealsBanner(metadata)
        eventDetailsListener.setDetailTitle(getString(R.string.detail_label))

        binding.customView1.setOnClickListener {
            RouteManager.route(itemView.context, metadata.productAppUrl)
        }
    }

    private fun setActionButton(
        binding: VoucherItemCardDealsBinding,
        item: Items,
        orderDetails: OrderDetails,
    ){

        if (orderDetails.actionButtons.isNotEmpty()){
            eventDetailsListener.setActionButtonEvent(orderDetails.actionButtons.first(), item, orderDetails)
        }

        if (item.actionButtons.isEmpty()){
            with(binding){
                progBar.gone()
                customView2.gone()
                tapActionDeals.gone()
            }
            return
        }

        binding.voucerCodeLayout.visible()
        item.actionButtons.forEachIndexed { index, actionButton ->
            when (actionButton.control) {
                KEY_VOUCHER_CODE -> {
                    renderBookingCode(binding, actionButton)
                }
                KEY_REDIRECT, KEY_REDIRECT_EXTERNAL -> {
                    renderRedeemVoucher(binding, actionButton, item, index)
                }
                KEY_POPUP -> {
                    clickActionButton(binding, actionButton, item, index)
                }
            }
        }
        binding.progBar.gone()
    }

    private fun renderBookingCode(binding: VoucherItemCardDealsBinding, actionButton: ActionButton) {
        val codes = actionButton.body.body.split(DELIMITERS)

        if (codes.isEmpty()){
            return
        }

        codes.forEach { code ->
            val bookingView = BookingCodeView(itemView.context, code, adapterPosition, actionButton.label, 0).apply {
                background = null
            }
            binding.voucerCodeLayout.addView(bookingView)
        }
    }

    private fun renderRedeemVoucher(
        binding: VoucherItemCardDealsBinding,
        actionButton: ActionButton,
        item: Items,
        index: Int
    ) {
        val redeemVoucherView = RedeemVoucherView(
            itemView.context,
            index,
            true,
            actionButton,
            item,
            { textView, items, count ->
                eventDetailsListener.onTapActionDeals(textView, actionButton, items, count, adapterPosition)
            },
            {
                eventDetailsListener.showRetryButtonToaster(it)
            }
        )
        binding.voucerCodeLayout.addView(redeemVoucherView)
    }

    private fun clickActionButton(
        binding: VoucherItemCardDealsBinding,
        actionButton: ActionButton,
        item: Items,
        index: Int
    ) {
        val actionTextButton = renderActionButtons(itemView.context, index, actionButton, item)
        binding.voucerCodeLayout.addView(actionTextButton)
        actionTextButton.setOnClickListener {
            eventDetailsListener.openQRFragment(actionButton, item)
        }
    }

    private fun getTimeMillis(date: String): String{
        return try {
            val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.ROOT)
            val dateMillis = Date(TimeUnit.SECONDS.toMillis(date.toLongOrZero()))
            dateFormat.format(dateMillis)
        } catch (e: Exception) {
            date
        }
    }
}