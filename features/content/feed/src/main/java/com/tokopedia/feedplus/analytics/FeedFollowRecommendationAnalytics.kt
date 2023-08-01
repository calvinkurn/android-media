package com.tokopedia.feedplus.analytics

import com.tokopedia.feedplus.presentation.model.FeedFollowRecommendationModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on August 01, 2023
 */
class FeedFollowRecommendationAnalytics @Inject constructor(
    private val userSession: UserSessionInterface
) {

    /**
     * Mynakama : https://mynakama.tokopedia.com/datatracker/requestdetail/view/3772
     * Row 50 - 54
     */

    /** Row 50 */
    fun eventImpressProfileRecommendation(
        profile: FeedFollowRecommendationModel.Profile
    ) {
        
    }

    /** Row 51 */
    fun eventClickProfileRecommendation(

    ) {

    }

    /** Row 52 */
    fun eventClickFollowProfileRecommendation(

    ) {

    }

    /** Row 53 */
    fun eventClickRemoveProfileRecommendation(

    ) {

    }

    /** Row 54 */
    fun eventSwipeProfileRecommendation(

    ) {

    }
}
