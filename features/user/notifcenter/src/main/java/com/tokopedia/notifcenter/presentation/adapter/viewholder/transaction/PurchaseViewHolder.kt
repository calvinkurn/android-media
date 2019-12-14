package com.tokopedia.notifcenter.presentation.adapter.viewholder.transaction

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.GlobalNavConstant.*
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.domain.model.DrawerNotification
import com.tokopedia.notifcenter.domain.model.PurchaseNotification
import com.tokopedia.notifcenter.listener.TransactionMenuListener

class PurchaseViewHolder(
        val view: View,
        val listener: TransactionMenuListener
): AbstractViewHolder<PurchaseNotification>(view) {

    private val context = view.context

    private val txtTitle  = view.findViewById<TextView>(R.id.txt_title)
    private val btnLoadMore  = view.findViewById<TextView>(R.id.btn_load_more)
    private val viewOther  = view.findViewById<RelativeLayout>(R.id.view_other)

    private val txtWaitingPayment = view.findViewById<TextView>(R.id.txt_waiting_payment)

    private val txtWaitingConfirm = view.findViewById<TextView>(R.id.txt_waiting_confirm)
    private val txtOrderProcessed = view.findViewById<TextView>(R.id.txt_order_process)
    private val txtGoodsSent = view.findViewById<TextView>(R.id.txt_goods_sent)
    private val txtGoodsReceive = view.findViewById<TextView>(R.id.txt_goods_receive)

    //counter badge
    private val txtCounterWaitingPayment = view.findViewById<TextView>(R.id.txt_counter_waiting_payment)
    private val txtCounterWaitingConfirm = view.findViewById<TextView>(R.id.txt_counter_waiting_confirm)
    private val txtCounterOrderProcessed = view.findViewById<TextView>(R.id.txt_counter_order_process)
    private val txtCounterGoodsSent = view.findViewById<TextView>(R.id.txt_counter_goods_sent)
    private val txtCounterGoodsReceive = view.findViewById<TextView>(R.id.txt_counter_goods_receive)

    private val imgWaitingConfirm = view.findViewById<ImageView>(R.id.img_waiting_confirm)
    private val imgOrderProcessed = view.findViewById<ImageView>(R.id.img_order_process)
    private val imgGoodsSent = view.findViewById<ImageView>(R.id.img_goods_sent)
    private val imgGoodsReceive = view.findViewById<ImageView>(R.id.img_goods_receive)

    override fun bind(element: PurchaseNotification) {
        if (element.id == PEMBELIAN) {
            txtTitle.text = element.title
            hasViewOther(element)
            initListChildItem(element)
        }

        btnLoadMore.setOnClickListener {
            RouteManager.route(context, ApplinkConst.PURCHASE_ORDER)
        }
    }

    private fun hasViewOther(element: DrawerNotification) {
        if (element.childs.first().id == MENUNGGU_PEMBAYARAN) {
            viewOther.show()
            txtWaitingPayment.text = element.childs.first().title
            viewOther.setOnClickListener {
                RouteManager.route(context, element.childs.first().applink)
                listener.sendTrackingData(element.title, element.childs.first().title)
            }
        }
    }

    private fun initListChildItem(element: DrawerNotification) {
        element.childs.forEach { notif ->
            when(notif.id) {
                MENUNGGU_PEMBAYARAN -> {
                    txtWaitingPayment.text = notif.title
                    if (notif.badge != null) {
                        if (notif.badge == 0) {
                            txtCounterWaitingPayment.hide()
                        } else {
                            txtCounterWaitingPayment.show()
                        }
                    }
                    txtCounterWaitingPayment.text = if (notif.badge != null) {
                        notif.badge.toString()
                    } else {
                        "0"
                    }
                }
                MENUNGGU_KONFIRMASI -> {
                    txtWaitingConfirm.text = notif.newLineTitle
                    if (notif.badge != null) {
                        if (notif.badge == 0) {
                            txtCounterWaitingConfirm.hide()
                        } else {
                            txtCounterWaitingConfirm.show()
                        }
                    }
                    txtCounterWaitingConfirm.text = if (notif.badge != null) {
                        notif.badge.toString()
                    } else {
                        "0"
                    }
                    imgWaitingConfirm.setImageResource(notif.icon)
                    imgWaitingConfirm.setOnClickListener {
                        RouteManager.route(context, notif.applink)
                        listener.sendTrackingData(element.title, notif.title)
                    }
                }
                PESANAN_DIPROSES -> {
                    txtOrderProcessed.text = notif.newLineTitle
                    if (notif.badge != null) {
                        if (notif.badge == 0) {
                            txtCounterOrderProcessed.hide()
                        } else {
                            txtCounterOrderProcessed.show()
                        }
                    }
                    txtCounterOrderProcessed.text = if (notif.badge != null) {
                        notif.badge.toString()
                    } else {
                        "0"
                    }
                    imgOrderProcessed.setImageResource(notif.icon)
                    imgOrderProcessed.setOnClickListener {
                        RouteManager.route(context, notif.applink)
                        listener.sendTrackingData(element.title, notif.title)
                    }
                }
                SEDANG_DIKIRIM -> {
                    txtGoodsSent.text = notif.newLineTitle
                    if (notif.badge != null) {
                        if (notif.badge == 0) {
                            txtCounterGoodsSent.hide()
                        } else {
                            txtCounterGoodsSent.show()
                        }
                    }
                    txtCounterGoodsSent.text = if (notif.badge != null) {
                        notif.badge.toString()
                    } else {
                        "0"
                    }
                    imgGoodsSent.setImageResource(notif.icon)
                    imgGoodsSent.setOnClickListener {
                        RouteManager.route(context, notif.applink)
                        listener.sendTrackingData(element.title, notif.title)
                    }
                }
                SAMPAI_TUJUAN -> {
                    txtGoodsReceive.text = notif.newLineTitle
                    if (notif.badge != null) {
                        if (notif.badge == 0) {
                            txtCounterGoodsReceive.hide()
                        } else {
                            txtCounterGoodsReceive.show()
                        }
                    }
                    txtCounterGoodsReceive.text = if (notif.badge != null) {
                        notif.badge.toString()
                    } else {
                        "0"
                    }
                    imgGoodsReceive.setImageResource(notif.icon)
                    imgGoodsReceive.setOnClickListener {
                        RouteManager.route(context, notif.applink)
                        listener.sendTrackingData(element.title, notif.title)
                    }
                }
            }
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_transaction_purchese
    }

}