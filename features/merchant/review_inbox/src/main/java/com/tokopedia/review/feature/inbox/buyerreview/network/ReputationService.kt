package com.tokopedia.review.feature.inbox.buyerreview.network

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.network.NetworkRouter
import com.tokopedia.user.session.UserSession
import retrofit2.Retrofit
import javax.inject.Inject

class ReputationService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkRouter: NetworkRouter,
    private val userSession: UserSession
) : BaseReputationService() {

    companion object {
        val TAG: String = ReputationService::class.java.simpleName
    }

    var api: ReputationApi = initApiService()
        private set

    private fun initApiService(): ReputationApi {
        val retrofit: Retrofit = createRetrofit(
            context,
            ReputationBaseURL.URL_REPUTATION,
            networkRouter,
            userSession
        )
        return retrofit.create(ReputationApi::class.java)
    }
}