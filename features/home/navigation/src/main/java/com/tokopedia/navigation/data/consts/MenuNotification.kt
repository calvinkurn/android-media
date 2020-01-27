package com.tokopedia.navigation.data.consts

import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.navigation.GlobalNavConstant.*
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.model.DrawerNotification
import com.tokopedia.navigation.domain.model.PurchaseNotification
import com.tokopedia.navigation.domain.model.SaleNotification

fun Fragment.buyerMenu(): List<PurchaseNotification> {
    val menus = mutableListOf<PurchaseNotification>()

    //menu
    val item = PurchaseNotification()
    item.id = PEMBELIAN
    item.title = getString(R.string.pembelian)

    //child menu
    val child = mutableListOf<DrawerNotification.ChildDrawerNotification>()

    //title
    child.add(DrawerNotification.ChildDrawerNotification(MENUNGGU_PEMBAYARAN,
            0, getString(R.string.menunggu_pembayaran), ApplinkConst.PMS))

    //sub-item
    child.add(DrawerNotification.ChildDrawerNotification(MENUNGGU_KONFIRMASI,
            R.drawable.ic_ts_waiting_confirm, getString(R.string.menunggu_konfirmasi), ApplinkConst.MARKETPLACE_WAITING_CONFIRMATION))

    child.add(DrawerNotification.ChildDrawerNotification(PESANAN_DIPROSES,
            R.drawable.ic_ts_order_process, getString(R.string.pesanan_diproses), ApplinkConst.MARKETPLACE_ORDER_PROCESSED))

    child.add(DrawerNotification.ChildDrawerNotification(SEDANG_DIKIRIM,
            R.drawable.ic_ts_goods_sent_green, getString(R.string.sedang_dikirim), ApplinkConst.MARKETPLACE_SENT))

    child.add(DrawerNotification.ChildDrawerNotification(SAMPAI_TUJUAN,
            R.drawable.ic_ts_goods_receive_green, getString(R.string.sampai_tujuan), ApplinkConst.MARKETPLACE_DELIVERED))

    item.childs = child
    menus.add(item)

    return menus
}

fun Fragment.sellerMenu(): List<SaleNotification> {
    val menus = mutableListOf<SaleNotification>()

    //menu
    val item = SaleNotification()
    item.id = PENJUALAN
    item.title = getString(R.string.penjualan)

    //child menu
    val child = mutableListOf<DrawerNotification.ChildDrawerNotification>()

    //title
    child.add(DrawerNotification.ChildDrawerNotification(MENUNGGU_PEMBAYARAN,
            0, getString(R.string.menunggu_pembayaran), ApplinkConst.PMS))

    //sub-item
    child.add(DrawerNotification.ChildDrawerNotification(PESANAN_BARU,
            R.drawable.ic_ts_new_order, getString(R.string.pesanan_baru), ApplinkConst.SELLER_NEW_ORDER))

    child.add(DrawerNotification.ChildDrawerNotification(SIAP_DIKIRIM,
            R.drawable.ic_ts_ready_sent, getString(R.string.siap_dikirim), ApplinkConst.SELLER_PURCHASE_READY_TO_SHIP))

    child.add(DrawerNotification.ChildDrawerNotification(SEDANG_DIKIRIM,
            R.drawable.ic_ts_goods_sent_yellow, getString(R.string.sedang_dikirim), ApplinkConst.SELLER_PURCHASE_SHIPPED))

    child.add(DrawerNotification.ChildDrawerNotification(SAMPAI_TUJUAN,
            R.drawable.ic_ts_goods_receive_yellow, getString(R.string.sampai_tujuan), ApplinkConst.SELLER_PURCHASE_DELIVERED))

    item.childs = child
    menus.add(item)

    return menus
}