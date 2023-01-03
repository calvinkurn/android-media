package com.tokopedia.sellerfeedback.presentation.util

import android.content.Context
import com.tokopedia.sellerfeedback.R

/**
 * Created By @ilhamsuaib on 12/10/21
 */

object ScreenShootPageHelper {

    private val PAGE_HOME = R.string.feedback_from_home
    private val PAGE_CHAT = R.string.feedback_from_chat
    private val PAGE_DISCUSS = R.string.feedback_from_discuss
    private val PAGE_ADD_PRODUCT = R.string.feedback_from_add_product
    private val PAGE_EDIT_PRODUCT = R.string.feedback_from_edit_product
    private val PAGE_PRODUCT_MANAGE = R.string.feedback_from_product_manage
    private val PAGE_SOM = R.string.feedback_from_som
    private val PAGE_STATISTIC = R.string.feedback_from_statistic
    private val PAGE_CENTRALIZED_PROMO = R.string.feedback_from_centralized_promo
    private val PAGE_SHOP = R.string.feedback_from_shop_page
    private val PAGE_REVIEW = R.string.feedback_from_review
    private val PAGE_COMPLAINED_ORDER = R.string.feedback_from_order_complaint
    private val PAGE_SHOP_SETTING = R.string.feedback_from_shop_setting
    private val PAGE_TOPADS= R.string.feedback_from_topads
    private val PAGE_FLASH_SALE_TOKOPEDIA= R.string.feedback_from_flashsaletokopedia
    private val PAGE_TOKOPEDIA_PLAY = R.string.feedback_from_tokopedia_play
    private val PAGE_FLASH_SALE_TOKO = R.string.feedback_from_flash_sale_toko
    private val PAGE_DISCOUNT_TOKO = R.string.feedback_from_diskon_toko
    private val PAGE_TOKOMEMBER = R.string.feedback_from_tokomember
    private val PAGE_FREE_ONGKIR = R.string.feedback_from_bebas_ongkir
    private val PAGE_BROADCAST_CHAT = R.string.feedback_from_broadcast_chat

    private val PAGE_OTHERS = R.string.feedback_from_others

    private const val SELLER_HOME_ACTIVITY =
        "com.tokopedia.sellerhome.view.activity.SellerHomeActivity"
    private const val ADD_EDIT_ACTIVITY =
        "com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity"

    private const val SELLER_HOME_PAGE = "seller_home"
    private const val PRODUCT_MANAGE_PAGE = "product_manage"
    private const val CHAT_PAGE = "top_chat"
    private const val SOM_PAGE = "som"
    private const val ADD_PRODUCT_PAGE = "add_product"
    private const val EDIT_PRODUCT_PAGE = "edit_product"

    fun getPageList(context: Context): List<String> {
        return listOf(
            context.getString(PAGE_HOME),
            context.getString(PAGE_CHAT),
            context.getString(PAGE_DISCUSS),
            context.getString(PAGE_ADD_PRODUCT),
            context.getString(PAGE_EDIT_PRODUCT),
            context.getString(PAGE_PRODUCT_MANAGE),
            context.getString(PAGE_SOM),
            context.getString(PAGE_STATISTIC),
            context.getString(PAGE_CENTRALIZED_PROMO),
            context.getString(PAGE_SHOP),
            context.getString(PAGE_REVIEW),
            context.getString(PAGE_COMPLAINED_ORDER),
            context.getString(PAGE_SHOP_SETTING),
            context.getString(PAGE_TOPADS),
            context.getString(PAGE_FLASH_SALE_TOKOPEDIA),
            context.getString(PAGE_TOKOPEDIA_PLAY),
            context.getString(PAGE_FLASH_SALE_TOKO),
            context.getString(PAGE_DISCOUNT_TOKO),
            context.getString(PAGE_TOKOMEMBER),
            context.getString(PAGE_FREE_ONGKIR),
            context.getString(PAGE_BROADCAST_CHAT),
            context.getString(PAGE_OTHERS)
        )
    }

    fun getPageByClassName(context: Context, canonicalName: String): String {
        if (canonicalName.isBlank()) {
            return context.getString(PAGE_OTHERS)
        }
        val pageName =
            if (canonicalName == SELLER_HOME_ACTIVITY || canonicalName == ADD_EDIT_ACTIVITY) {
                getPageFromMultipleFragmentPage(context)
            } else {
                canonicalName
            }
        val pageMap = getPagesHasMap(context).entries
            .firstOrNull { it.value.contains(pageName) }
        return pageMap?.key ?: context.getString(PAGE_OTHERS)
    }

    private fun getPageFromMultipleFragmentPage(context: Context): String {
        val otherPage = context.getString(PAGE_OTHERS)
        val sharedPref = ScreenshotPreferenceManager(context.applicationContext)

        val pages = listOf(
            SELLER_HOME_PAGE, PRODUCT_MANAGE_PAGE, CHAT_PAGE, SOM_PAGE,
            ADD_PRODUCT_PAGE, EDIT_PRODUCT_PAGE
        )
        val selectedPage = sharedPref.getSelectedFragment(defVal = otherPage)
        val page = pages.firstOrNull { it == selectedPage }

        return page ?: otherPage
    }

    private fun getPagesHasMap(context: Context): Map<String, List<String>> {
        return mapOf(
            getHomePageMapper(context),
            getChatPageMapper(context),
            getDiscussPageMapper(context),
            getAddProductPageMapper(context),
            getEditProductPageMapper(context),
            getProductManagePageMapper(context),
            getSomPageMapper(context),
            getStatisticPageMapper(context),
            getCentralizedPromoPageMapper(context),
            getShopPageMapper(context),
            getReviewPageMapper(context),
            getShopSettingPageMapper(context),
            getTopAdsPageMapper(context),
            getFlashSaleTokopediaPageMapper(context),
            getTokopediaPlayPageMapper(context),
            getFlashSaleTokoPageMapper(context),
            getShopDiscountPageMapper(context),
            getTokoMemberPageMapper(context),
            getBebasOngkirPageMapper(context),
            getBroadcastChatPageMapper(context)
        )
    }

    private fun getShopSettingPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_SHOP_SETTING) to listOf(
            "com.tokopedia.sellerhome.settings.view.activity.MenuSettingActivity",
            "com.tokopedia.shop.settings.basicinfo.view.activity.ShopSettingsInfoActivity",
            "com.tokopedia.shop.settings.notes.view.activity.ShopSettingsNotesActivity",
            "com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditScheduleActivity",
            "com.tokopedia.manageaddress.ui.shoplocation.ShopLocationActivity",
            "com.tokopedia.editshipping.ui.shopeditaddress.ShopEditAddressActivity",
            "com.tokopedia.editshipping.ui.shippingeditor.ShippingEditorActivity",
            "com.tokopedia.activation.ui.ActivationPageActivity",
            "com.tokopedia.profilecompletion.profileinfo.view.activity.ProfileInfoActivity",
            "com.tokopedia.settingbank.view.activity.SettingBankActivity",
            "com.tokopedia.settingnotif.usersetting.view.activity.UserNotificationSettingActivity",
        )
    }

    private fun getReviewPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_REVIEW) to listOf(
            "com.tokopedia.review.feature.inbox.presentation.InboxReputationActivity",
            "com.tokopedia.review.feature.reputationhistory.view.activity.SellerReputationInfoActivity",
            "com.tokopedia.review.feature.inbox.buyerreview.view.activity.InboxReputationDetailActivity"
        )
    }

    private fun getShopPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_SHOP) to listOf(
            "com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity"
        )
    }

    private fun getCentralizedPromoPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_CENTRALIZED_PROMO) to listOf(
            "com.tokopedia.centralizedpromo.view.activity.CentralizedPromoActivity"
        )
    }

    private fun getStatisticPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_STATISTIC) to listOf(
            "com.tokopedia.statistic.view.activity.StatisticActivity"
        )
    }

    private fun getSomPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_SOM) to listOf(
            "com.tokopedia.sellerorder.list.presentation.activities.SomListActivity",
            "com.tokopedia.sellerorder.filter.presentation.activity.SomSubFilterActivity",
            "com.tokopedia.sellerorder.detail.presentation.activity.SomDetailActivity",
            "com.tokopedia.sellerorder.detail.presentation.activity.SomSeeInvoiceActivity",
            "com.tokopedia.sellerorder.detail.presentation.activity.SomDetailLogisticInfoActivity",
            "com.tokopedia.sellerorder.detail.presentation.activity.SomDetailBookingCodeActivity",
            "com.tokopedia.sellerorder.confirmshipping.presentation.activity.SomConfirmShippingActivity",
            "com.tokopedia.sellerorder.confirmshipping.presentation.activity.SomScanResiActivity",
            SOM_PAGE
        )
    }

    private fun getProductManagePageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_PRODUCT_MANAGE) to listOf(
            "com.tokopedia.product.manage.feature.list.view.activity.ProductManageActivity",
            "com.tokopedia.product.manage.feature.filter.presentation.activity.ProductManageFilterExpandActivity",
            "com.tokopedia.product.manage.feature.stockreminder.view.activity.StockReminderActivity",
            "com.tokopedia.product.manage.feature.etalase.view.activity.EtalasePickerActivity",
            "com.tokopedia.product.manage.feature.cashback.presentation.activity.ProductManageSetCashbackActivity",
            "com.tokopedia.product.manage.feature.campaignstock.ui.activity.CampaignStockActivity",
            PRODUCT_MANAGE_PAGE
        )
    }

    private fun getEditProductPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_EDIT_PRODUCT) to listOf(
            "com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity",
            EDIT_PRODUCT_PAGE
        )
    }

    private fun getAddProductPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_ADD_PRODUCT) to listOf(
            "com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity",
            ADD_PRODUCT_PAGE
        )
    }

    private fun getDiscussPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_DISCUSS) to listOf(
            "com.tokopedia.talk.feature.inbox.presentation.activity.TalkInboxActivity",
            "com.tokopedia.talk.feature.reply.presentation.activity.TalkReplyActivity",
        )
    }

    private fun getChatPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_CHAT) to listOf(
            "com.tokopedia.topchat.chatlist.activity.ChatListActivity",
            "com.tokopedia.sellerhome.view.activity.SellerHomeActivity",
            "com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity",
            CHAT_PAGE
        )
    }

    private fun getHomePageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_HOME) to listOf(
            "com.tokopedia.sellerhome.view.activity.SellerHomeActivity",
            SELLER_HOME_PAGE
        )
    }

    private fun getTopAdsPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_TOPADS) to listOf(
            "com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity"
        )
    }

    private fun getFlashSaleTokopediaPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_FLASH_SALE_TOKOPEDIA) to listOf(
            "com.tokopedia.tkpd.flashsale.presentation.list.container.FlashSaleListActivity"
        )
    }

    private fun getTokopediaPlayPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_TOKOPEDIA_PLAY) to listOf(
            "com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity"
        )
    }

    private fun getBebasOngkirPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_FREE_ONGKIR) to listOf(
            "com.tokopedia.webview.BaseSimpleWebViewActivity?url=https://m.tokopedia.com/bebas-ongkir"
        )
    }

    private fun getBroadcastChatPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_BROADCAST_CHAT) to listOf(
            "com.tokopedia.webview.BaseSimpleWebViewActivity?url=https://m.tokopedia.com/broadcast-chat"
        )
    }

    private fun getFlashSaleTokoPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_FLASH_SALE_TOKO) to listOf(
            "com.tokopedia.shop.flashsale.presentation.list.container.CampaignListActivity"
        )
    }

    private fun getShopDiscountPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_DISCOUNT_TOKO) to listOf(
            "com.tokopedia.shopdiscount.manage.presentation.container.DiscountedProductManageActivity"
        )
    }

    private fun getTokoMemberPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_TOKOMEMBER) to listOf(
            "com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberMainActivity",
            "com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashIntroActivity"
        )
    }
}
