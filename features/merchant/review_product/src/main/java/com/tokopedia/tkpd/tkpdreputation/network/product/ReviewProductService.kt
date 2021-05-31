package com.tokopedia.tkpd.tkpdreputation.network.product

import android.content.Context
import com.tokopedia.network.NetworkRouter
import com.tokopedia.tkpd.tkpdreputation.constant.ReputationBaseURL
import com.tokopedia.tkpd.tkpdreputation.network.BaseReputationService
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductApi
import com.tokopedia.user.session.UserSession

class ReviewProductService(
        private val context: Context,
        private val networkRouter: NetworkRouter,
        private val userSession: UserSession
) : BaseReputationService() {

    val api: ReviewProductApi = initApiService()

    private fun initApiService(): ReviewProductApi {
        val retrofit = createRetrofit(
                context,
                ReputationBaseURL.BASE_DOMAIN,
                networkRouter,
                userSession
        )
        return retrofit.create(ReviewProductApi::class.java)
    }

}