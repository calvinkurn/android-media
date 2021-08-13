package com.tokopedia.notifcenter.data.consts

import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.MENUNGGU_KONFIRMASI
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.MENUNGGU_PEMBAYARAN
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.PEMBELIAN
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.PENJUALAN
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.PESANAN_BARU
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.PESANAN_DIPROSES
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.SAMPAI_TUJUAN
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.SEDANG_DIKIRIM
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.SIAP_DIKIRIM
import com.tokopedia.notifcenter.data.model.DrawerNotification
import com.tokopedia.notifcenter.data.viewbean.BuyerNotificationViewBean
import com.tokopedia.notifcenter.data.viewbean.SellerNotificationViewBean

private const val EMPTY_ICON = 0

fun Fragment.buyerMenu(): List<BuyerNotificationViewBean> {
    val menus = mutableListOf<BuyerNotificationViewBean>()
    val item = BuyerNotificationViewBean()
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
                    context?.getString(R.string.pesanan_diproses_uoh),
                    ApplinkConst.MARKETPLACE_ORDER_PROCESSED),
            DrawerNotification.ChildDrawerNotification(
                    SEDANG_DIKIRIM,
                    R.drawable.ic_ts_goods_sent_green,
                    context?.getString(R.string.sedang_dikirim_uoh),
                    ApplinkConst.MARKETPLACE_SENT),
            DrawerNotification.ChildDrawerNotification(
                    SAMPAI_TUJUAN,
                    R.drawable.ic_ts_goods_receive_green,
                    context?.getString(R.string.sampai_tujuan_uoh),
                    ApplinkConst.MARKETPLACE_DELIVERED)
    )

    item.id = PEMBELIAN
    item.title = context?.getString(R.string.pembelian)
    item.childs = itemChild
    menus.add(item)
    return menus
}

fun Fragment.sellerMenu(): List<SellerNotificationViewBean> {
    val menus = mutableListOf<SellerNotificationViewBean>()
    val item = SellerNotificationViewBean()
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