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

    init {
        initApiService()
    }

    var api: ReputationApiV2? = null
        private set

    private fun initApiService() {
        val retrofit = createRetrofit(
                context,
                ReputationBaseURL.URL_REPUTATION,
                networkRouter,
                userSession
        )
        api = retrofit.create(ReputationApiV2::class.java)
    }

}