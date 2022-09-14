package com.tokopedia.tokofood.feature.search.searchresult.analytics

import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.tokofood.common.domain.response.Merchant
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TokofoodSearchResultAnalytics @Inject constructor(private val userSession: UserSessionInterface) {

    private val tracker by lazy { TrackApp.getInstance().gtm }

    fun sendCompleteFilterImpressionTracking(keyword: String) {
        // TODO("Not yet implemented")
    }

    // TODO: Determine whether we still need to send filter values
    fun sendCompleteFilterClickTracking(keyword: String) {
        // TODO("Not yet implemented")
    }

    fun sendSortImpressionTracking(keyword: String, sorts: List<Sort>) {
        // TODO("Not yet implemented")
    }

    fun sendSortClickTracking(keyword: String, sortValue: String) {
        // TODO("Not yet implemented")
    }

    fun sendMiniFilterImpressionTracking(keyword: String, options: List<Option>) {
        // TODO("Not yet implemented")
    }

    fun sendMiniFilterClickTracking(
        keyword: String,
        options: List<Option>,
        isSelected: Boolean
    ) {
        // TODO("Not yet implemented")
    }

    fun sendSubmitFilterClickTracking() {
        // TODO("Not yet implemented")
    }

    fun sendAddressWidgetClickTracking(destinationId: String) {
        // TODO("Not yet implemented")
    }

    fun sendMerchantCardClickTracking(
        destinationId: String,
        keyword: String,
        merchant: Merchant,
        sortValue: String
    ) {
        // TODO("Not yet implemented")
    }

    fun sendOtherBranchesClickTracking(
        destinationId: String,
        keyword: String,
        merchant: Merchant
    ) {
        // TODO("Not yet implemented")
    }
    
}