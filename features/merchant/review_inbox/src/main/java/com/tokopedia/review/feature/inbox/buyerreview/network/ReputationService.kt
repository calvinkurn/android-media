package com.tokopedia.review.feature.inbox.buyerreview.network

import android.content.Context
import com.tokopedia.network.NetworkRouter
import com.tokopedia.user.session.UserSession
import retrofit2.Retrofit

class ReputationService constructor(
    private val context: Context?,
    private val networkRouter: NetworkRouter?,
    private val userSession: UserSession?
) : BaseReputationService() {
    var api: ReputationApi? = null
        private set

    public override fun createRetrofit(
        context: Context?,
        baseUrl: String?,
        networkRouter: NetworkRouter?,
        userSession: UserSession?
    ): Retrofit? {
        return super.createRetrofit(context, baseUrl, networkRouter, userSession)
    }

    private fun initApiService() {
        val retrofit: Retrofit? = createRetrofit(
            context,
            ReputationBaseURL.URL_REPUTATION,
            networkRouter,
            userSession
        )
        api = retrofit!!.create(ReputationApi::class.java)
    }

    companion object {
        val TAG: String = ReputationService::class.java.getSimpleName()
    }

    init {
        initApiService()
    }
}