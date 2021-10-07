package com.tokopedia.buyerorder.detail.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.data.MetaDataInfo
import com.tokopedia.buyerorder.detail.data.OrderDetails
import com.tokopedia.buyerorder.detail.view.adapter.ItemsAdapter.KEY_VOUCHER_CODE
import com.tokopedia.buyerorder.detail.view.customview.BookingCodeView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.voucher_item_card_deals.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class DealsOMPViewHolder(private val setEventDetails: ItemsAdapter.SetEventDetails, itemView: View, viewType: Int): RecyclerView.ViewHolder(itemView) {

    fun bind(orderDetails: OrderDetails, item: Items){
        val metadata = Gson().fromJson(item.metaData, MetaDataInfo::class.java)

        itemView?.apply {
            iv_deal.loadImage(
                    if (metadata.productImage.isNullOrEmpty()) {
                        item.imageUrl
                    } else metadata.productImage
            )

            tv_deal_intro.text = if (metadata.name.isNullOrEmpty()){
                item.title
            } else metadata.name

            tv_brand_name.text = metadata.productName

            setEventDetails.sendThankYouEvent(metadata, ItemsAdapter.ITEM_DEALS, orderDetails)

            if (metadata.endTime.isNullOrEmpty()){
                ll_valid.gone()
            } else {
                ll_valid.show()
                tv_valid_till_date.text = getDateMilis(metadata.endTime)
            }

            setEventDetails.setDealsBanner(item)
            setEventDetails.setDetailTitle(resources.getString(R.string.detail_label))

            customView1.setOnClickListener {
                RouteManager.route(context, metadata.productAppUrl)
            }

            if (item.actionButtons != null && item.actionButtons.size > 0){
                voucerCodeLayout.show()
                for (i in 0 until item.actionButtons.size) {
                    val actionButton: ActionButton = item.actionButtons.get(i)
                    if (actionButton.buttonType.equals(KEY_VOUCHER_CODE)) {
                        val bookingCodeView = BookingCodeView(context, actionButton.getBody().getBody(), i, actionButton.getLabel(), 0)
                        bookingCodeView.background = null
                        voucerCodeLayout.addView(bookingCodeView)
                    }
                }
                prog_bar.gone()
            } else {
                prog_bar.gone()
                customView2.gone()
                tapAction_deals.gone()
            }

            if (item.actionButtons != null && item.actionButtons.size > 0){
                setEventDetails.setEventDetails(item.actionButtons.get(0), item)
            }
        }
    }

    private fun getDateMilis(date: String): String? {
        try {
            val dateFormat = SimpleDateFormat(" dd MMM YYYY hh:mm")
            val dateMilis = Date(TimeUnit.SECONDS.toMillis(date.toLong()))
            return dateFormat.format(dateMilis).toString()
        } catch (e: Exception){
            return date
        }
    }

    companion object{

    }
}