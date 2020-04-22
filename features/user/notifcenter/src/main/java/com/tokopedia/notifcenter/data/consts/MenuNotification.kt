package com.tokopedia.notifcenter.data.consts

import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.notifcenter.GlobalNavConstant.*
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.model.DrawerNotification
import com.tokopedia.notifcenter.data.viewbean.PurchaseNotificationViewBean
import com.tokopedia.notifcenter.data.viewbean.SaleNotificationViewBean

private const val EMPTY_ICON = 0

fun Fragment.buyerMenu(): List<PurchaseNotificationViewBean> {
    val menus = mutableListOf<PurchaseNotificationViewBean>()
    val item = PurchaseNotificationViewBean()
    val itemChild = listOf(
            DrawerNotification.ChildDrawerNotification(
                    MENUNGGU_PEMBAYARAN,
                    EMPTY_ICON,
                    context?.getString(R.string.menunggu_pembayaran),
                    ApplinkConst.PMS),
            DrawerNotification.ChildDrawerNotification(
                    MENUNGGU_KONFIRMASI,
                    R.drawable.ic_ts_waiting_confirm,
                    context?.getString(R.string.menunggu_konfirmasi),
                    ApplinkConst.MARKETPLACE_WAITING_CONFIRMATION),
            DrawerNotification.ChildDrawerNotification(
                    PESANAN_DIPROSES,
                    R.drawable.ic_ts_order_process,
                    context?.getString(R.string.pesanan_diproses),
                    ApplinkConst.MARKETPLACE_ORDER_PROCESSED),
            DrawerNotification.ChildDrawerNotification(
                    SEDANG_DIKIRIM,
                    R.drawable.ic_ts_goods_sent_green,
                    context?.getString(R.string.sedang_dikirim),
                    ApplinkConst.MARKETPLACE_SENT),
            DrawerNotification.ChildDrawerNotification(
                    SAMPAI_TUJUAN,
                    R.drawable.ic_ts_goods_receive_green,
                    context?.getString(R.string.sampai_tujuan),
                    ApplinkConst.MARKETPLACE_DELIVERED)
    )

    item.id = PEMBELIAN
    item.title = context?.getString(R.string.pembelian)
    item.childs = itemChild
    menus.add(item)
    return menus
}

fun Fragment.sellerMenu(): List<SaleNotificationViewBean> {
    val menus = mutableListOf<SaleNotificationViewBean>()
    val item = SaleNotificationViewBean()
    val itemChild = listOf(
            DrawerNotification.ChildDrawerNotification(
                    MENUNGGU_PEMBAYARAN,
                    EMPTY_ICON,
                    context?.getString(R.string.menunggu_pembayaran),
                    ApplinkConst.PMS),
            DrawerNotification.ChildDrawerNotification(
                    PESANAN_BARU,
                    R.drawable.ic_ts_new_order,
                    context?.getString(R.string.pesanan_baru),
                    ApplinkConst.SELLER_NEW_ORDER),
            DrawerNotification.ChildDrawerNotification(
                    SIAP_DIKIRIM,
                    R.drawable.ic_ts_ready_sent,
                    context?.getString(R.string.siap_dikirim),
                    ApplinkConst.SELLER_PURCHASE_READY_TO_SHIP),
            DrawerNotification.ChildDrawerNotification(
                    SEDANG_DIKIRIM,
                    R.drawable.ic_ts_goods_sent_yellow,
                    context?.getString(R.string.sedang_dikirim),
                    ApplinkConst.SELLER_PURCHASE_SHIPPED),
            DrawerNotification.ChildDrawerNotification(
                    SAMPAI_TUJUAN,
                    R.drawable.ic_ts_goods_receive_yellow,
                    context?.getString(R.string.sampai_tujuan),
                    ApplinkConst.SELLER_PURCHASE_DELIVERED)
    )

    item.id = PENJUALAN
    item.title = context?.getString(R.string.penjualan)
    item.childs = itemChild
    menus.add(item)
    return menus
}