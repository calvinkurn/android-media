package com.tokopedia.home.analytics.v2

import android.annotation.SuppressLint
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.HomePageTracking.ECOMMERCE
import com.tokopedia.home.analytics.HomePageTracking.PROMOTIONS
import com.tokopedia.home.analytics.v2.BaseTracking.Event.PROMO_CLICK
import com.tokopedia.home.analytics.v2.BaseTracking.Event.PROMO_VIEW
import com.tokopedia.track.constant.TrackerConstant.TRACKER_ID
import com.tokopedia.track.constant.TrackerConstant.USERID

@SuppressLint("VisibleForTests")
object BusinessUnitTracking : BaseTracking(){
    private object CustomAction{
        const val BU_VIEW = "impression on bu widget"
        const val BU_CLICK = "click on bu widget"
        const val TAB_BU_CLICK = "click on bu widget tab"
    }

    private const val TRACKER_ID_TAB_SELECTED = 4894
    private const val TRACKER_ID_ITEM_VIEW = 22612
    private const val TRACKER_ID_ITEM_CLICK = 22613

    fun getPageSelected(
        bannerId: String,
        headerName: String,
        position: Int,
        channelId: String,
        campaignCode: String,
        userId: String,
    ): Map<String, Any>? =
        DataLayer.mapOf(
            Event.KEY, PROMO_CLICK,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CustomAction.TAB_BU_CLICK,
            Label.KEY, listOf(channelId, "0", headerName).joinToString(" - "),
            TRACKER_ID, TRACKER_ID_TAB_SELECTED,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            CampaignCode.KEY, campaignCode,
            ChannelId.KEY, channelId,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            USERID, userId,
            ECOMMERCE, DataLayer.mapOf(
                PROMO_CLICK, DataLayer.mapOf(
                    PROMOTIONS, DataLayer.listOf(
                        DataLayer.mapOf(
                            "creative_name", headerName,
                            "creative_slot", position.toString(),
                            "id", "${channelId}_${bannerId}",
                            "name", "/ - p${position} - bu widget - banner - 0 - 0 - 0 - $headerName",
                        )
                    )
                )
            )
        )

    fun getBusinessUnitView(
        bannerId: String,
        headerName: String,
        channelId: String,
        userType: String,
        categoryName: String,
        tabPosition: Int,
        position: Int,
        businessUnit: String,
        recommendationLogic: String,
        userId: String,
    ): MutableMap<String, Any> {
        val eventLabel = listOf(
            channelId,
            "0",
            headerName,
        ).joinToString(" - ")

        val promotionName =
            "/ - p${position} " +
                "- bu widget - banner " +
                "- $businessUnit " +
                "- $userType " +
                "- $recommendationLogic " +
                "- $position " +
                "- $categoryName" +
                "- $headerName"

        return DataLayer.mapOf(
            Event.KEY, PROMO_VIEW,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CustomAction.BU_VIEW,
            Label.KEY, eventLabel,
            TRACKER_ID, TRACKER_ID_ITEM_VIEW,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            ChannelId.KEY, channelId,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            USERID, userId,
            Ecommerce.KEY, DataLayer.mapOf(
                PROMO_VIEW, DataLayer.mapOf(
                    PROMOTIONS, DataLayer.listOf(
                        DataLayer.mapOf(
                            "creative_name", headerName,
                            "creative_slot", tabPosition.toString(),
                            "id", "${channelId}_${bannerId}",
                            "name", promotionName,
                        )
                    )
                )
            )
        )
    }

    fun getBusinessUnitClick(
        bannerId: String,
        headerName: String,
        channelId: String,
        userType: String,
        categoryName: String,
        tabPosition: Int,
        position: Int,
        businessUnit: String,
        recommendationLogic: String,
        campaignCode: String,
        userId: String,
    ): MutableMap<String, Any> {
        val eventLabel = listOf(
            channelId,
            userType,
            headerName,
        ).joinToString(" - ")

        val promotionName =
            "/ - p${position} " +
                "- bu widget - banner " +
                "- $businessUnit " +
                "- $userType " +
                "- $recommendationLogic " +
                "- $position " +
                "- $categoryName" +
                "- $headerName"

        return DataLayer.mapOf(
            Event.KEY, PROMO_CLICK,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CustomAction.BU_CLICK,
            Label.KEY, eventLabel,
            TRACKER_ID, TRACKER_ID_ITEM_CLICK,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            CampaignCode.KEY, campaignCode,
            USERID, userId,
            Ecommerce.KEY, DataLayer.mapOf(
                PROMO_CLICK, DataLayer.mapOf(
                    PROMOTIONS, DataLayer.listOf(
                        DataLayer.mapOf(
                            "creative_name", headerName,
                            "creative_slot", tabPosition.toString(),
                            "id", "${channelId}_${bannerId}",
                            "name", promotionName,
                            "dimension84", channelId,
                        )
                    )
                )
            )
        )
    }
}
