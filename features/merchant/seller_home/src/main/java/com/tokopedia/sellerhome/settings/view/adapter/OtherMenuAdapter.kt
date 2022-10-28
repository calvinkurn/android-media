package com.tokopedia.sellerhome.settings.view.adapter

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.*
import com.tokopedia.seller.menu.common.view.uimodel.base.DividerType
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.common.SellerHomeConst
import com.tokopedia.sellerhome.settings.view.activity.SellerEduWebviewActivity
import com.tokopedia.sellerhomecommon.common.const.ShcConst
import java.util.*

class OtherMenuAdapter(
    private val context: Context?,
    private val listener: Listener,
    typeFactory: OtherMenuAdapterTypeFactory
) :
    BaseListAdapter<SettingUiModel, OtherMenuAdapterTypeFactory>(typeFactory) {

    companion object {
        private const val WEBVIEW_APPLINK_FORMAT = "%s?url=%s"
        private const val FEEDBACK_EXPIRED_DATE = 1638115199000 //28-11-2021
        private const val PRODUCT_COUPON_END_DATE = 1649869200000 // Wed Apr 14 2022 00:00:00
        private const val TOKOPEDIA_PLAY_END_DATE = 1652806800000 // Wed May 18 2022 00:00:00
        private const val SLASH_PRICE_END_DATE = 1657990800000 // Sun Jul 17 2022 00:00:00
    }

    private var isShowCentralizedPromoTag: Boolean = false

    private fun generateSettingList() = listOf(
        SettingTitleUiModel(context?.getString(R.string.setting_menu_improve_sales).orEmpty()),
        StatisticMenuItemUiModel(
            title = context?.getString(R.string.setting_menu_statistic).orEmpty(),
            clickApplink = ApplinkConstInternalMechant.MERCHANT_STATISTIC_DASHBOARD,
            iconUnify = IconUnify.GRAPH,
            tag = getStatisticNewTag()
        ),

        MenuItemUiModel(
            title = context?.getString(R.string.setting_menu_ads_and_shop_promotion).orEmpty(),
            clickApplink = ApplinkConstInternalSellerapp.CENTRALIZED_PROMO,
            eventActionSuffix = SettingTrackingConstant.SHOP_ADS_AND_PROMOTION,
            iconUnify = IconUnify.PROMO_ADS,
            tag = getCentralizedPromoTag()
        ),
        MenuItemUiModel(
            title = context?.getString(R.string.setting_menu_performance)
                .orEmpty(),
            clickApplink = ApplinkConstInternalMarketplace.SHOP_PERFORMANCE,
            eventActionSuffix = SettingTrackingConstant.SHOP_PERFORMANCE,
            iconUnify = IconUnify.PERFORMANCE,
        ),
        SettingTitleUiModel(
            context?.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_buyer_info)
                .orEmpty()
        ),
        MenuItemUiModel(
            title = context?.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_discussion)
                .orEmpty(),
            clickApplink = ApplinkConst.TALK,
            eventActionSuffix = SettingTrackingConstant.DISCUSSION,
            iconUnify = IconUnify.DISCUSSION
        ),
        MenuItemUiModel(
            title = context?.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_review)
                .orEmpty(),
            clickApplink = ApplinkConst.REPUTATION,
            eventActionSuffix = SettingTrackingConstant.REVIEW,
            iconUnify = IconUnify.STAR
        ),
        MenuItemUiModel(
            title = context?.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_complaint)
                .orEmpty(),
            clickApplink = null,
            eventActionSuffix = SettingTrackingConstant.COMPLAINT,
            iconUnify = IconUnify.PRODUCT_INFO
        ) {
            val applink = String.format(
                Locale.getDefault(),
                WEBVIEW_APPLINK_FORMAT,
                ApplinkConst.WEBVIEW,
                "${SellerBaseUrl.HOSTNAME}${SellerBaseUrl.RESO_INBOX_SELLER}"
            )
            val intent = RouteManager.getIntent(context, applink)
            context?.startActivity(intent)
        },
        DividerUiModel(),
        MenuItemUiModel(
            title = context?.getString(R.string.setting_menu_finance_service).orEmpty(),
            clickApplink = null,
            eventActionSuffix = SettingTrackingConstant.FINANCIAL_SERVICE,
            iconUnify = IconUnify.FINANCE
        ) {
            RouteManager.route(context, ApplinkConst.LAYANAN_FINANSIAL)
        },
        MenuItemUiModel(
            title = context?.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_seller_education_center)
                .orEmpty(),
            clickApplink = null,
            eventActionSuffix = SettingTrackingConstant.SELLER_CENTER,
            iconUnify = IconUnify.SHOP_INFO
        ) {
            context?.let {
                val intent = SellerEduWebviewActivity.createIntent(it)
                it.startActivity(intent)
            }
        },
        MenuItemUiModel(
            title = context?.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_tokopedia_care)
                .orEmpty(),
            clickApplink = ApplinkConst.TOKOPEDIA_CARE_HELP,
            eventActionSuffix = SettingTrackingConstant.TOKOPEDIA_CARE,
            iconUnify = IconUnify.CALL_CENTER
        ),
        DividerUiModel(DividerType.THIN_PARTIAL),
        MenuItemUiModel(
            title = context?.getString(R.string.setting_menu_setting)
                .orEmpty(),
            clickApplink = null,
            eventActionSuffix = SettingTrackingConstant.SETTINGS,
            iconUnify = IconUnify.SETTING,
            tag = getSettingsTag()
        ) {
            listener.goToSettings()
        }
    )

    private fun getStatisticNewTag(): String {
        val expiredDateMillis = ShcConst.TRAFFIC_INSIGHT_TAG_EXPIRED
        val todayMillis = Date().time
        return if (todayMillis < expiredDateMillis) {
            context?.getString(R.string.setting_new_tag).orEmpty()
        } else {
            SellerHomeConst.EMPTY_STRING
        }
    }

    private fun getSettingsTag(): String {
        val expiredDateMillis = FEEDBACK_EXPIRED_DATE
        val todayMillis = Date().time
        return if (todayMillis < expiredDateMillis) {
            context?.getString(R.string.setting_new_tag).orEmpty()
        } else {
            SellerHomeConst.EMPTY_STRING
        }
    }

    private fun getCentralizedPromoTag(): String {
        val shouldShow = isShowCentralizedPromoTag
        return if (shouldShow) {
            context?.getString(R.string.setting_new_tag).orEmpty()
        } else {
            SellerHomeConst.EMPTY_STRING
        }
    }

    private fun areDatesExpired(): Boolean {
        val todayMillis = Date().time
        val expirationDateList = listOf(
            getProductCouponEndDate(),
            getTokopediaPlayEndDate(),
            getSlashPriceEndDate()
        )
        return expirationDateList.all { expiredDate ->
            todayMillis >= expiredDate
        }
    }

    private fun getProductCouponEndDate(): Long = PRODUCT_COUPON_END_DATE

    private fun getTokopediaPlayEndDate(): Long = TOKOPEDIA_PLAY_END_DATE

    private fun getSlashPriceEndDate(): Long = SLASH_PRICE_END_DATE

    fun populateAdapterData() {
        clearAllElements()
        addElement(generateSettingList())
    }


    fun setCentralizedPromoTag(isShow: Boolean = false) {
        this.isShowCentralizedPromoTag = isShow
        populateAdapterData()
    }


    interface Listener {
        fun goToSettings()
    }
}
