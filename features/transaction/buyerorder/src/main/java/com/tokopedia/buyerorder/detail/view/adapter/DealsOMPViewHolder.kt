package com.tokopedia.buyerorder.detail.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.databinding.VoucherItemCardDealsBinding
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.data.MetaDataInfo
import com.tokopedia.buyerorder.detail.data.OrderDetails
import com.tokopedia.buyerorder.detail.view.adapter.ItemsAdapter.KEY_POPUP
import com.tokopedia.buyerorder.detail.view.adapter.ItemsAdapter.KEY_REDIRECT
import com.tokopedia.buyerorder.detail.view.adapter.ItemsAdapter.KEY_REDIRECT_EXTERNAL
import com.tokopedia.buyerorder.detail.view.adapter.ItemsAdapter.KEY_VOUCHER_CODE
import com.tokopedia.buyerorder.detail.view.customview.BookingCodeView
import com.tokopedia.buyerorder.detail.view.customview.RedeemVoucherView
import com.tokopedia.buyerorder.detail.view.presenter.OrderListDetailPresenter
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageCircle
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

        val binding = VoucherItemCardDealsBinding.bind(itemView)
        val metadata = Gson().fromJson(item.metaData, MetaDataInfo::class.java)

        with(binding){
            ivDeals.loadImageCircle(
                if (metadata.productImage.isEmpty()) {
                    item.imageUrl
                } else metadata.productImage
            )

            tvDealIntro.text = if (metadata.name.isEmpty()) item.title else metadata.name
            tvBrandName.text = metadata.productName

            setEventDetails.sendOpenScreenDeals(true)

            if (metadata.endTime.isEmpty()){
                llValid.gone()
            } else {
                llValid.show()
                tvValidTillDate.text = getDateMillis(metadata.endTime)
            }

            setEventDetails.setDealsBanner(item)
            setEventDetails.setDetailTitle(itemView.context.resources.getString(R.string.detail_label))

            customView1.setOnClickListener {
                RouteManager.route(itemView.context, metadata.productAppUrl)
            }

            if (item.actionButtons.isNotEmpty()){
                voucerCodeLayout.show()
                for (i in item.actionButtons.indices) {
                    val actionButton: ActionButton = item.actionButtons[i]
                    if (actionButton.control == KEY_VOUCHER_CODE) {
                        val codes = actionButton.body.body.split(",").toTypedArray()
                        if (codes.isNotEmpty()) {
                            codes.forEach {
                                val bookingCodeView = BookingCodeView(
                                    itemView.context, it, i,
                                    actionButton.label, 0
                                )
                                bookingCodeView.background = null
                                voucerCodeLayout.addView(bookingCodeView)
                            }
                        }
                    } else if (actionButton.control == KEY_REDIRECT || actionButton.control == KEY_REDIRECT_EXTERNAL){
                        val redeemVoucherView = RedeemVoucherView(itemView.context, i, actionButton, item,
                            actionButton.body, presenter, positionHolder, setTapActionDeals,
                            setEventDetails, true, item.actionButtons.size - 1 == i)
                        voucerCodeLayout.addView(redeemVoucherView)
                    } else if (actionButton.control == KEY_POPUP){
                        val actionTextButton = adapter.renderActionButtons(i, actionButton, item)
                        voucerCodeLayout.addView(actionTextButton)
                        actionTextButton?.setOnClickListener {
                            setEventDetails.openShowQRFragment(actionButton, item)
                        }
                    }
                }
                progBar.gone()
            } else {
                progBar.gone()
                customView2.gone()
                tapActionDeals.gone()
            }

            if (orderDetails.actionButtons.isNotEmpty()){
                setEventDetails.setActionButtonEvent(item,
                    orderDetails.actionButtons[0], orderDetails)
            }
        }
    }

    private fun getDateMillis(date: String): String? {
        return try {
            val dateFormat = SimpleDateFormat(" dd MMM YYYY hh:mm")
            val dateMillis = Date(TimeUnit.SECONDS.toMillis(date.toLong()))
            dateFormat.format(dateMillis).toString()
        } catch (e: Exception){
            date
        }
    }
}