package com.tokopedia.sellerfeedback.presentation.util

/**
 * Created By @ilhamsuaib on 12/10/21
 */

object ScreenShootPageHelper {

    private const val PAGE_HOME = "Home"
    private const val PAGE_CHAT = "Chat"
    private const val PAGE_DISCUSS = "Diskusi"
    private const val PAGE_ADD_PRODUCT = "Tambah Produk"
    private const val PAGE_EDIT_PRODUCT = "Ubah Produk"
    private const val PAGE_PRODUCT_MANAGE = "Produk Toko"
    private const val PAGE_SOM = "Penjualan"
    private const val PAGE_STATISTIC = "Statistik"
    private const val PAGE_CENTRALIZED_PROMO = "Iklan dan Promosi"
    private const val PAGE_SHOP_DECORATION = "Dekorasi Toko"
    private const val PAGE_REVIEW = "Ulasan"
    private const val PAGE_COMPLAINED_ORDER = "Pesanan Dikomplain"
    private const val PAGE_SHOP_SETTING = "Pengaturan Toko"
    private const val PAGE_ADMIN_SETTING = "Pengaturan Admin"
    private const val PAGE_OTHERS = "Lainnya"

    fun getPageByClass(canonicalName: String): String {
        getPageList().entries.forEach {
            if (it.value.contains(canonicalName)) {
                return canonicalName
            }
        }
        return PAGE_OTHERS
    }

    fun getPageList(): Map<String, List<String>> {
        return mapOf(
            getHomePageMapper(), getChatPageMapper()
        )
    }

    private fun getChatPageMapper(): Pair<String, List<String>> {
        return PAGE_CHAT to listOf(
            "com.tokopedia.topchat.chatlist.activity.ChatListActivity",
            "com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity"
        )
    }

    private fun getHomePageMapper(): Pair<String, List<String>> {
        return PAGE_HOME to listOf(
            "com.tokopedia.sellerhome.view.activity.SellerHomeActivity"
        )
    }
}