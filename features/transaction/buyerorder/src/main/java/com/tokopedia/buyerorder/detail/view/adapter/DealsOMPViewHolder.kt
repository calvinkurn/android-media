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
import com.tokopedia.buyerorder.detail.view.adapter.ItemsAdapter.*
import com.tokopedia.buyerorder.detail.view.customview.BookingCodeView
import com.tokopedia.buyerorder.detail.view.customview.RedeemVoucherView
import com.tokopedia.buyerorder.detail.view.presenter.OrderListDetailPresenter
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.voucher_item_card_deals.view.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit


class DealsOMPViewHolder(private val setEventDetails: ItemsAdapter.SetEventDetails,
                         private val adapter: ItemsAdapter,
                         itemView: View,
                         private val presenter: OrderListDetailPresenter,
                         private val setTapActionDeals: RedeemVoucherView.SetTapActionDeals):
        RecyclerView.ViewHolder(itemView) {

    fun bind(orderDetails: OrderDetails, item: Items, positionHolder: Int){
        val metadata = Gson().fromJson(item.metaData, MetaDataInfo::class.java)

        itemView?.apply {
            iv_deal?.loadImage(
                    if (metadata.productImage.isNullOrEmpty()) {
                        item.imageUrl
                    } else metadata.productImage
            )

            tv_deal_intro?.text = if (metadata.name.isNullOrEmpty()){
                item.title
            } else metadata.name

            tv_brand_name?.text = metadata.productName

            setEventDetails.sendOpenScreenDeals(true)

            if (metadata.endTime.isNullOrEmpty()){
                ll_valid?.gone()
            } else {
                ll_valid?.show()
                tv_valid_till_date?.text = getDateMilis(metadata.endTime)
            }

            setEventDetails.setDealsBanner(item)
            setEventDetails.setDetailTitle(resources.getString(R.string.detail_label))

            customView1?.setOnClickListener {
                RouteManager.route(context, metadata.productAppUrl)
            }

            if (item.actionButtons != null && item.actionButtons.size > 0){
                voucerCodeLayout?.show()
                for (i in 0 until item.actionButtons.size) {
                    val actionButton: ActionButton = item.actionButtons.get(i)
                    if (actionButton.control.equals(KEY_VOUCHER_CODE)) {
                        val codes = actionButton.body.body.split(",").toTypedArray()
                        if (codes.size > 0) {
                            codes.forEach {
                                val bookingCodeView = BookingCodeView(
                                    context, it, i,
                                    actionButton.label, 0
                                )
                                bookingCodeView?.background = null
                                voucerCodeLayout?.addView(bookingCodeView)
                            }
                        }
                    } else if (actionButton.control.equals(KEY_REDIRECT)){
                        val redeemVoucherView = RedeemVoucherView(context, i, actionButton, item,
                                actionButton.body, presenter, positionHolder, setTapActionDeals,
                                setEventDetails, true)
                        voucerCodeLayout?.addView(redeemVoucherView)
                    } else if (actionButton.control.equals(KEY_POPUP)){
                        val actionTextButton = adapter.renderActionButtons(i, actionButton, item)
                        voucerCodeLayout?.addView(actionTextButton)
                        actionTextButton?.setOnClickListener {
                            setEventDetails.openShowQRFragment(actionButton, item)
                        }
                    }
                }
                prog_bar?.gone()
            } else {
                prog_bar?.gone()
                customView2?.gone()
                tapAction_deals?.gone()
            }

            if (orderDetails.actionButtons() != null && orderDetails.actionButtons().size > 0){
                setEventDetails.setActionButtonEvent(item, orderDetails.actionButtons().get(0), orderDetails)
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
}