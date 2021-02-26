package com.tokopedia.tkpd.tkpdreputation.network.product

import android.content.Context
import com.tokopedia.network.NetworkRouter
import com.tokopedia.tkpd.tkpdreputation.constant.ReputationBaseURL
import com.tokopedia.tkpd.tkpdreputation.network.BaseReputationServiceV2
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductApiV2
import com.tokopedia.user.session.UserSession

class ReviewProductServiceV2(
        private val context: Context,
        private val networkRouter: NetworkRouter,
        private val userSession: UserSession
) : BaseReputationServiceV2() {

    val api: ReviewProductApiV2 = initApiService()

    private fun initApiService(): ReviewProductApiV2 {
        val retrofit = createRetrofit(
                context,
                ReputationBaseURL.BASE_DOMAIN,
                networkRouter,
                userSession
        )
        return retrofit.create(ReviewProductApiV2::class.java)
    }

}