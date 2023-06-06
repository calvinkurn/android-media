package com.tokopedia.feedplus.analytics

import android.os.Bundle
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedplus.analytics.FeedAnalytics.Companion.getContentType
import com.tokopedia.feedplus.analytics.FeedAnalytics.Companion.getPostType
import com.tokopedia.feedplus.analytics.FeedAnalytics.Companion.getPrefix
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.mvcwidget.AnimatedInfos
import com.tokopedia.mvcwidget.trackers.DefaultMvcTrackerImpl
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.track.TrackApp

/**
 * Created By : Muhammad Furqan on 03/05/23
 */
class FeedMVCAnalytics : DefaultMvcTrackerImpl() {

    var trackerData: FeedTrackerDataModel? = null
    var voucherList: List<AnimatedInfos?> = emptyList()

    override fun userClickEntryPoints(
        shopId: String,
        userId: String?,
        @MvcSource source: Int,
        isTokomember: Boolean,
        productId: String
    ) {
        val eventLabel = trackerData?.let {
            "${it.activityId} - ${it.authorId} - ${getPrefix(it.tabType)} - ${
            getPostType(
                it.typename,
                it.type,
                it.authorType.value,
                it.isFollowing
            )
            } - ${
            getContentType(
                it.typename,
                it.type,
                it.mediaType
            )
            } - ${it.contentScore} - ${it.hasVoucher} - ${it.campaignStatus} - ${it.entryPoint}"
        } ?: ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            EVENT,
            Bundle().apply {
                putString(KEY_EVENT, EVENT)
                putString(KEY_EVENT_ACTION, EVENT_ACTION)
                putString(KEY_EVENT_CATEGORY, EVENT_CATEGORY)
                putString(KEY_EVENT_LABEL, eventLabel)
                putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_CONTENT)
                putString(KEY_CURRENT_SITE, CURRENT_SITE_MARKETPLACE)
                putString(KEY_TRACKER_ID, "41607")
                putParcelableArrayList(
                    KEY_PROMOTIONS,
                    ArrayList(voucherList.filterNotNull().mapIndexed { index, animatedInfos ->
                        val name = MethodChecker.fromHtml(animatedInfos.title).toString()
                        Bundle().apply {
                            putString(KEY_CREATIVE_NAME, name)
                            putString(KEY_CREATIVE_SLOT, "${index + 1}")
                            putString(KEY_ITEM_ID, "")
                            putString(KEY_ITEM_NAME, name)
                        }
                    }),
                )
            }
        )
    }

    companion object {
        private const val KEY_EVENT = "event"
        private const val KEY_EVENT_CATEGORY = "eventCategory"
        private const val KEY_EVENT_ACTION = "eventAction"
        private const val KEY_EVENT_LABEL = "eventLabel"
        private const val KEY_TRACKER_ID = "trackerId"
        private const val KEY_BUSINESS_UNIT = "businessUnit"
        private const val KEY_CURRENT_SITE = "currentSite"
        private const val KEY_PROMOTIONS = "promotions"

        private const val KEY_CREATIVE_NAME = "creative_name"
        private const val KEY_CREATIVE_SLOT = "creative_slot"
        private const val KEY_ITEM_ID = "item_id"
        private const val KEY_ITEM_NAME = "item_name"

        private const val EVENT = "select_content"
        private const val EVENT_CATEGORY = "unified feed"
        private const val EVENT_ACTION = "click - voucher bottomsheet"
        private const val BUSINESS_UNIT_CONTENT = "content"
        private const val CURRENT_SITE_MARKETPLACE = "tokopediamarketplace"
    }
}
