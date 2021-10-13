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
    private val PAGE_SHOP_DECORATION = R.string.feedback_from_shop_decoration
    private val PAGE_REVIEW = R.string.feedback_from_review
    private val PAGE_COMPLAINED_ORDER = R.string.feedback_from_order_complaint
    private val PAGE_SHOP_SETTING = R.string.feedback_from_shop_setting
    private val PAGE_ADMIN_SETTING = R.string.feedback_from_admin_setting
    private val PAGE_OTHERS = R.string.feedback_from_others

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
            context.getString(PAGE_SHOP_DECORATION),
            context.getString(PAGE_REVIEW),
            context.getString(PAGE_COMPLAINED_ORDER),
            context.getString(PAGE_SHOP_SETTING),
            context.getString(PAGE_ADMIN_SETTING),
            context.getString(PAGE_OTHERS)
        )
    }

    fun getPageByClassName(context: Context, canonicalName: String): String {
        val pageMap = getPagesHasMap(context).entries
            .firstOrNull { it.value.contains(canonicalName) }
        return pageMap?.key ?: context.getString(PAGE_OTHERS)
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
            getShopDecorationPageMapper(context),
            getReviewPageMapper(context),
            getComplainedOrderPageMapper(context),
            getShopSettingPageMapper(context),
            getAdminSettingPageMapper(context)
        )
    }

    private fun getAdminSettingPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_ADMIN_SETTING) to listOf(
            ""
        )
    }

    private fun getShopSettingPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_SHOP_SETTING) to listOf(
            ""
        )
    }

    private fun getComplainedOrderPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_COMPLAINED_ORDER) to listOf(
            ""
        )
    }

    private fun getReviewPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_REVIEW) to listOf(
            ""
        )
    }

    private fun getShopDecorationPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_SHOP_DECORATION) to listOf(
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
            ""
        )
    }

    private fun getProductManagePageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_PRODUCT_MANAGE) to listOf(
            ""
        )
    }

    private fun getEditProductPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_EDIT_PRODUCT) to listOf(
            ""
        )
    }

    private fun getAddProductPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_ADD_PRODUCT) to listOf(
            ""
        )
    }

    private fun getDiscussPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_DISCUSS) to listOf(
            ""
        )
    }

    private fun getChatPageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_CHAT) to listOf(
            "com.tokopedia.topchat.chatlist.activity.ChatListActivity",
            "com.tokopedia.sellerhome.view.activity.SellerHomeActivity",
            "com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity"
        )
    }

    private fun getHomePageMapper(context: Context): Pair<String, List<String>> {
        return context.getString(PAGE_HOME) to listOf(
            "com.tokopedia.sellerhome.view.activity.SellerHomeActivity"
        )
    }
}