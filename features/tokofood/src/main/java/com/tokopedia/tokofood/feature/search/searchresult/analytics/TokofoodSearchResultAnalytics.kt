package com.tokopedia.tokofood.feature.search.searchresult.analytics

import android.os.Bundle
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants
import com.tokopedia.tokofood.common.domain.response.Merchant
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodFilterSortMapper
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.constant.TrackerConstant
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TokofoodSearchResultAnalytics @Inject constructor(private val userSession: UserSessionInterface) {

    private val tracker by lazy { TrackApp.getInstance().gtm }

    fun sendCompleteFilterImpressionTracking(keyword: String, destinationId: String) {
        val dataLayer = Bundle().apply {
            setEventInfo(
                eventName = TokoFoodAnalyticsConstants.VIEW_ITEM,
                eventAction = TokoFoodAnalyticsConstants.VIEW_COMPLETE_FILTER_TOKOFOOD,
                eventCategory = TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RESULT_PAGE,
                eventLabel = keyword
            )
            setAdditionalInfo()
            putTrackerId(TokoFoodAnalyticsConstants.TRACKER_ID_35773)
            putDestinationId(destinationId)
            setCurrentUserId()
        }

        tracker.sendEnhanceEcommerceEvent(
            TokoFoodAnalyticsConstants.VIEW_ITEM,
            dataLayer
        )
    }

    fun sendCompleteFilterClickTracking(keyword: String) {
        Tracker.Builder()
            .setEvent(TokoFoodAnalyticsConstants.CLICK_PG)
            .setEventAction(TokoFoodAnalyticsConstants.CLICK_COMPLETE_FILTER_TOKOFOOD)
            .setEventCategory(TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RESULT_PAGE)
            .setEventLabel(keyword)
            .setCustomProperty(
                TokoFoodAnalyticsConstants.TRACKER_ID,
                TokoFoodAnalyticsConstants.TRACKER_ID_35774
            )
            .setBusinessUnit(TokoFoodAnalytics.PHYSICAL_GOODS)
            .setCurrentSite(TokoFoodAnalytics.TOKOPEDIA_MARKETPLACE)
            .build()
            .send()
    }

    fun sendSortImpressionTracking(
        keyword: String,
        destinationId: String,
        sorts: List<Sort>,
        sortValue: String
    ) {
        val eventLabel = "$keyword - $sortValue"
        val dataLayer = Bundle().apply {
            setEventInfo(
                eventName = TokoFoodAnalyticsConstants.VIEW_ITEM,
                eventAction = TokoFoodAnalyticsConstants.VIEW_SORT_FILTER_TOKOFOOD,
                eventCategory = TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RESULT_PAGE,
                eventLabel = eventLabel
            )
            putPromotions(sorts)
            setAdditionalInfo()
            putTrackerId(TokoFoodAnalyticsConstants.TRACKER_ID_35775)
            putDestinationId(destinationId)
            setCurrentUserId()
        }

        tracker.sendEnhanceEcommerceEvent(
            TokoFoodAnalyticsConstants.VIEW_ITEM,
            dataLayer
        )
    }

    fun sendSortClickTracking(keyword: String, sortValue: String, destinationId: String) {
        val eventLabel = "$keyword - $sortValue"
        Tracker.Builder()
            .setEvent(TokoFoodAnalyticsConstants.CLICK_PG)
            .setEventAction(TokoFoodAnalyticsConstants.CLICK_SORT_FILTER_TOKOFOOD)
            .setEventCategory(TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RESULT_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(
                TokoFoodAnalyticsConstants.TRACKER_ID,
                TokoFoodAnalyticsConstants.TRACKER_ID_35776
            )
            .setBusinessUnit(TokoFoodAnalytics.PHYSICAL_GOODS)
            .setCurrentSite(TokoFoodAnalytics.TOKOPEDIA_MARKETPLACE)
            .setCustomProperty(TokoFoodAnalyticsConstants.DESTINATION_ID, destinationId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendMiniFilterImpressionTracking(
        keyword: String,
        options: List<Option>,
        destinationId: String
    ) {
        val dataLayer = Bundle().apply {
            setEventInfo(
                eventName = TokoFoodAnalyticsConstants.VIEW_ITEM,
                eventAction = TokoFoodAnalyticsConstants.VIEW_MINI_FILTER_TOKOFOOD,
                eventCategory = TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RESULT_PAGE,
                eventLabel = String.EMPTY
            )
            putPromotions(keyword, options)
            setAdditionalInfo()
            putTrackerId(TokoFoodAnalyticsConstants.TRACKER_ID_35777)
            putDestinationId(destinationId)
            setCurrentUserId()
        }

        tracker.sendEnhanceEcommerceEvent(
            TokoFoodAnalyticsConstants.VIEW_ITEM,
            dataLayer
        )
    }

    fun sendMiniFilterClickTracking(
        keyword: String,
        options: List<Option>,
        isSelected: Boolean
    ) {
        val eventLabel = "$keyword - ${getFilterValue(options)} - $isSelected"
        Tracker.Builder()
            .setEvent(TokoFoodAnalyticsConstants.CLICK_PG)
            .setEventAction(TokoFoodAnalyticsConstants.CLICK_MINI_FILTER_TOKOFOOD)
            .setEventCategory(TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RESULT_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(
                TokoFoodAnalyticsConstants.TRACKER_ID,
                TokoFoodAnalyticsConstants.TRACKER_ID_35778
            )
            .setBusinessUnit(TokoFoodAnalytics.PHYSICAL_GOODS)
            .setCurrentSite(TokoFoodAnalytics.TOKOPEDIA_MARKETPLACE)
            .build()
            .send()
    }

    fun sendSubmitFilterClickTracking() {
        Tracker.Builder()
            .setEvent(TokoFoodAnalyticsConstants.CLICK_PG)
            .setEventAction(TokoFoodAnalyticsConstants.CLICK_ON_SUBMIT_FILTER_TOKOFOOD)
            .setEventCategory(TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RESULT_PAGE)
            .setEventLabel(String.EMPTY)
            .setCustomProperty(
                TokoFoodAnalyticsConstants.TRACKER_ID,
                TokoFoodAnalyticsConstants.TRACKER_ID_35785
            )
            .setBusinessUnit(TokoFoodAnalytics.PHYSICAL_GOODS)
            .setCurrentSite(TokoFoodAnalytics.TOKOPEDIA_MARKETPLACE)
            .build()
            .send()
    }

    fun sendAddressWidgetImpressionTracking(
        destinationId: String
    ) {
        val dataLayer = Bundle().apply {
            setEventInfo(
                eventName = TokoFoodAnalyticsConstants.VIEW_ITEM,
                eventAction = TokoFoodAnalyticsConstants.VIEW_ADDRESS_WIDGET_IN_SRP_TOKOFOOD,
                eventCategory = TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RESULT_PAGE,
                eventLabel = String.EMPTY
            )
            putParcelableArrayList(TokoFoodAnalyticsConstants.KEY_EE_PROMOTIONS, arrayListOf<Bundle>())
            setAdditionalInfo()
            putTrackerId(TokoFoodAnalyticsConstants.TRACKER_ID_36853)
            putDestinationId(destinationId)
            setCurrentUserId()
        }

        tracker.sendEnhanceEcommerceEvent(
            TokoFoodAnalyticsConstants.VIEW_ITEM,
            dataLayer
        )
    }

    fun sendAddressWidgetClickTracking(destinationId: String) {
        Tracker.Builder()
            .setEvent(TokoFoodAnalyticsConstants.CLICK_PG)
            .setEventAction(TokoFoodAnalyticsConstants.CLICK_ADDRESS_WIDGET_TOKOFOOD)
            .setEventCategory(TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RESULT_PAGE)
            .setEventLabel(String.EMPTY)
            .setCustomProperty(
                TokoFoodAnalyticsConstants.TRACKER_ID,
                TokoFoodAnalyticsConstants.TRACKER_ID_35786
            )
            .setBusinessUnit(TokoFoodAnalytics.PHYSICAL_GOODS)
            .setCurrentSite(TokoFoodAnalytics.TOKOPEDIA_MARKETPLACE)
            .setCustomProperty(TokoFoodAnalyticsConstants.DESTINATION_ID, destinationId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendMerchantCardImpressionTracking(
        destinationId: String,
        keyword: String,
        merchant: Merchant,
        sortFilterValue: String,
        index: Int
    ) {
        val dataLayer = Bundle().apply {
            setEventInfo(
                eventName = TokoFoodAnalyticsConstants.VIEW_ITEM,
                eventAction = TokoFoodAnalyticsConstants.VIEW_MERCHANT_CARD_IN_SRP_TOKOFOOD,
                eventCategory = TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RESULT_PAGE,
                eventLabel = keyword
            )
            putPromotions(merchant, sortFilterValue, index)
            setAdditionalInfo()
            putTrackerId(TokoFoodAnalyticsConstants.TRACKER_ID_36854)
            putDestinationId(destinationId)
            setCurrentUserId()
        }

        tracker.sendEnhanceEcommerceEvent(
            TokoFoodAnalyticsConstants.VIEW_ITEM,
            dataLayer
        )
    }

    fun sendMerchantCardClickTracking(
        destinationId: String,
        keyword: String,
        merchant: Merchant,
        sortFilterValue: String,
        index: Int
    ) {
        val dataLayer = Bundle().apply {
            setEventInfo(
                eventName = TokoFoodAnalyticsConstants.SELECT_CONTENT,
                eventAction = TokoFoodAnalyticsConstants.CLICK_MERCHANT_CARD_TOKOFOOD,
                eventCategory = TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RESULT_PAGE,
                eventLabel = keyword
            )
            putPromotions(merchant, sortFilterValue, index)
            setAdditionalInfo()
            putTrackerId(TokoFoodAnalyticsConstants.TRACKER_ID_35787)
            putDestinationId(destinationId)
            setCurrentUserId()
        }

        tracker.sendEnhanceEcommerceEvent(
            TokoFoodAnalyticsConstants.SELECT_CONTENT,
            dataLayer
        )
    }

    fun sendOtherBranchesClickTracking(
        destinationId: String,
        keyword: String,
        merchant: Merchant
    ) {
        val eventLabel = "$keyword - ${merchant.id} - ${merchant.name}"
        Tracker.Builder()
            .setEvent(TokoFoodAnalyticsConstants.CLICK_PG)
            .setEventAction(TokoFoodAnalyticsConstants.CLICK_OTHER_BRANCHES)
            .setEventCategory(TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RESULT_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(
                TokoFoodAnalyticsConstants.TRACKER_ID,
                TokoFoodAnalyticsConstants.TRACKER_ID_35788
            )
            .setBusinessUnit(TokoFoodAnalytics.PHYSICAL_GOODS)
            .setCurrentSite(TokoFoodAnalytics.TOKOPEDIA_MARKETPLACE)
            .setCustomProperty(TokoFoodAnalyticsConstants.DESTINATION_ID, destinationId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    private fun Bundle.setEventInfo(
        eventName: String,
        eventAction: String,
        eventCategory: String,
        eventLabel: String
    ) {
        putString(TrackAppUtils.EVENT, eventName)
        putString(TrackAppUtils.EVENT_ACTION, eventAction)
        putString(TrackAppUtils.EVENT_CATEGORY, eventCategory)
        putString(TrackAppUtils.EVENT_LABEL, eventLabel)
    }

    private fun Bundle.setAdditionalInfo() {
        putString(TrackerConstant.BUSINESS_UNIT, TokoFoodAnalytics.PHYSICAL_GOODS)
        putString(TrackerConstant.CURRENT_SITE, TokoFoodAnalytics.TOKOPEDIA_MARKETPLACE)
    }

    private fun Bundle.putTrackerId(trackerId: String) {
        putString(TokoFoodAnalyticsConstants.TRACKER_ID, trackerId)
    }

    private fun Bundle.putDestinationId(destinationId: String) {
        putString(TokoFoodAnalyticsConstants.DESTINATION_ID, destinationId)
    }

    private fun Bundle.setCurrentUserId() {
        putString(TokoFoodAnalyticsConstants.USER_ID, userSession.userId)
    }

    private fun Bundle.putPromotions(sortList: List<Sort>) {
        val itemBundles = arrayListOf<Bundle>()
        itemBundles.addAll(
            sortList.mapIndexed { index, sort ->
                Bundle().apply {
                    putString(
                        TokoFoodAnalyticsConstants.KEY_EE_CREATIVE_NAME,
                        TokoFoodAnalyticsConstants.NULL
                    )
                    putInt(TokoFoodAnalyticsConstants.KEY_EE_CREATIVE_SLOT, index)
                    putString(
                        TokoFoodAnalyticsConstants.KEY_EE_ITEM_ID,
                        TokoFoodAnalyticsConstants.NULL
                    )
                    putString(TokoFoodAnalyticsConstants.KEY_EE_ITEM_NAME, sort.value)
                }
            }
        )
        putParcelableArrayList(TokoFoodAnalyticsConstants.KEY_EE_PROMOTIONS, itemBundles)
    }

    private fun Bundle.putPromotions(
        keyword: String,
        optionList: List<Option>
    ) {
        val itemBundles = arrayListOf<Bundle>()
        itemBundles.addAll(
            optionList.mapIndexed { index, option ->
                val itemName = "$keyword - ${option.value} - ${option.inputState.toBoolean()}"
                Bundle().apply {
                    putString(
                        TokoFoodAnalyticsConstants.KEY_EE_CREATIVE_NAME,
                        TokoFoodAnalyticsConstants.NULL
                    )
                    putInt(TokoFoodAnalyticsConstants.KEY_EE_CREATIVE_SLOT, index)
                    putString(
                        TokoFoodAnalyticsConstants.KEY_EE_ITEM_ID,
                        TokoFoodAnalyticsConstants.NULL
                    )
                    putString(TokoFoodAnalyticsConstants.KEY_EE_ITEM_NAME, itemName)
                }
            }
        )
        putParcelableArrayList(TokoFoodAnalyticsConstants.KEY_EE_PROMOTIONS, itemBundles)
    }

    private fun Bundle.putPromotions(merchant: Merchant, sortFilterValue: String, index: Int) {
        val itemBundles = arrayListOf<Bundle>()
        itemBundles.add(
            Bundle().apply {
                putString(
                    TokoFoodAnalyticsConstants.KEY_EE_CREATIVE_NAME,
                    TokoFoodAnalyticsConstants.NULL
                )
                putInt(TokoFoodAnalyticsConstants.KEY_EE_CREATIVE_SLOT, index)
                putString(TokoFoodAnalytics.KEY_DIMENSION_61, sortFilterValue)
                putString(
                    TokoFoodAnalyticsConstants.KEY_EE_ITEM_ID,
                    "${merchant.id} - ${merchant.name}"
                )
                putString(
                    TokoFoodAnalyticsConstants.KEY_EE_ITEM_NAME,
                    "${merchant.addressLocality} - ${merchant.etaFmt} - ${merchant.distanceFmt} - ${merchant.ratingFmt}"
                )
            }
        )
        putParcelableArrayList(TokoFoodAnalyticsConstants.KEY_EE_PROMOTIONS, itemBundles)
    }

    private fun getFilterValue(options: List<Option>): String {
        return options.filter { it.inputState.toBoolean() }
            .joinToString(TokofoodFilterSortMapper.OPTION_SEPARATOR) { it.value }
    }

}
