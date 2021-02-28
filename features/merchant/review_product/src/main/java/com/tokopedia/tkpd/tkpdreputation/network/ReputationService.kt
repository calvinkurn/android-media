package com.tokopedia.tkpd.tkpdreputation.network

import android.content.Context
import com.tokopedia.network.NetworkRouter
import com.tokopedia.tkpd.tkpdreputation.constant.ReputationBaseURL
import com.tokopedia.user.session.UserSession

class ReputationService(
        private val context: Context,
        private val networkRouter: NetworkRouter,
        private val userSession: UserSession
) : BaseReputationService() {

    val api = initApiService()

    private fun initApiService(): ReputationApi {
        val retrofit = createRetrofit(
                context,
                ReputationBaseURL.URL_REPUTATION,
                networkRouter,
                userSession
        )
        return retrofit.create(ReputationApi::class.java)
    }

}