package com.tokopedia.shop.review.shop.data.network

import android.content.Context
import com.tokopedia.network.NetworkRouter
import com.tokopedia.shop.review.constant.ReputationBaseURL
import com.tokopedia.user.session.UserSession
import retrofit2.Retrofit

class ReviewActService(private val context: Context?, private val networkRouter: NetworkRouter?, private val userSession: UserSession?) : BaseReputationService() {
    var api: ReviewActApi? = null
        private set

    private fun initApiService() {
        val retrofit = createRetrofit(
                context,
                ReputationBaseURL.URL_REVIEW_ACTION,
                networkRouter,
                userSession)
        api = retrofit.create(ReviewActApi::class.java)
    }

    companion object {
        val TAG = ReviewActService::class.java.simpleName
    }

    init {
        initApiService()
    }
}