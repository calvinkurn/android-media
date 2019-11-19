package com.tokopedia.navigation.presentation.adapter.viewholder.transaction

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.navigation.GlobalNavConstant.*
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.model.DrawerNotification
import com.tokopedia.navigation.domain.model.PurchaseNotification

class PurchaseViewHolder(val view: View): AbstractViewHolder<PurchaseNotification>(view) {

    private val context = view.context

    private val txtTitle  = view.findViewById<TextView>(R.id.txt_title)
    private val btnLoadMore  = view.findViewById<TextView>(R.id.btn_load_more)
    private val viewOther  = view.findViewById<RelativeLayout>(R.id.view_other)

    private val txtWaitingPayment = view.findViewById<TextView>(R.id.txt_waiting_payment)

    private val txtWaitingConfirm = view.findViewById<TextView>(R.id.txt_waiting_confirm)
    private val txtOrderProcessed = view.findViewById<TextView>(R.id.txt_order_process)
    private val txtGoodsSent = view.findViewById<TextView>(R.id.txt_goods_sent)
    private val txtGoodsReceive = view.findViewById<TextView>(R.id.txt_goods_receive)

    private val imgWaitingConfirm = view.findViewById<ImageView>(R.id.img_waiting_confirm)
    private val imgOrderProcessed = view.findViewById<ImageView>(R.id.img_order_process)
    private val imgGoodsSent = view.findViewById<ImageView>(R.id.img_goods_sent)
    private val imgGoodsReceive = view.findViewById<ImageView>(R.id.img_goods_receive)

    override fun bind(element: PurchaseNotification) {
        if (element.id == PEMBELIAN) {
            txtTitle.text = element.title
            hasViewOther(element.childs)
            initListChildItem(element.childs)
        }

        btnLoadMore.setOnClickListener {

        }
    }

    private fun hasViewOther(child: List<DrawerNotification.ChildDrawerNotification>) {
        if (child.first().id == MENUNGGU_PEMBAYARAN) {
            viewOther.show()
            txtWaitingPayment.text = child.first().title
            viewOther.setOnClickListener {
                RouteManager.route(context, child.first().applink)
            }
        }
    }

    private fun initListChildItem(child: List<DrawerNotification.ChildDrawerNotification>) {
        child.forEach { notif ->
            when(notif.id) {
                MENUNGGU_PEMBAYARAN -> {
                    txtWaitingPayment.text = notif.title
                }
                MENUNGGU_KONFIRMASI -> {
                    txtWaitingConfirm.text = notif.title
                    imgWaitingConfirm.setImageResource(notif.icon)
                    imgWaitingConfirm.setOnClickListener {
                        RouteManager.route(context, notif.applink)
                    }
                }
                PESANAN_DIPROSES -> {
                    txtOrderProcessed.text = notif.title
                    imgOrderProcessed.setImageResource(notif.icon)
                    imgOrderProcessed.setOnClickListener {
                        RouteManager.route(context, notif.applink)
                    }
                }
                SEDANG_DIKIRIM -> {
                    txtGoodsSent.text = notif.title
                    imgGoodsSent.setImageResource(notif.icon)
                    imgGoodsSent.setOnClickListener {
                        RouteManager.route(context, notif.applink)
                    }
                }
                SAMPAI_TUJUAN -> {
                    txtGoodsReceive.text = notif.title
                    imgGoodsReceive.setImageResource(notif.icon)
                    imgGoodsReceive.setOnClickListener {
                        RouteManager.route(context, notif.applink)
                    }
                }
            }
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_transaction_purchese
    }

}