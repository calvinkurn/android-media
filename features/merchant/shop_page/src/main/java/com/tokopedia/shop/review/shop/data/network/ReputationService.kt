package com.tokopedia.shop.review.shop.data.network

import android.content.Context
import com.tokopedia.network.NetworkRouter
import com.tokopedia.shop.review.constant.ReputationBaseURL
import com.tokopedia.user.session.UserSession
import retrofit2.Retrofit

class ReputationService(private val context: Context?, private val networkRouter: NetworkRouter?, private val userSession: UserSession?) : BaseReputationService() {
    var api: ReputationApi? = null
        private set

    private fun initApiService() {
        val retrofit = createRetrofit(
                context,
                ReputationBaseURL.URL_REPUTATION,
                networkRouter,
                userSession)
        api = retrofit.create(ReputationApi::class.java)
    }

    companion object {
        val TAG = ReputationService::class.java.simpleName
    }

    init {
        initApiService()
    }
}