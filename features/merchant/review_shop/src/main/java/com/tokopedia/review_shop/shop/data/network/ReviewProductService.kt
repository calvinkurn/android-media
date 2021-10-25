package com.tokopedia.review_shop.shop.data.network

import android.content.Context
import com.tokopedia.network.NetworkRouter
import com.tokopedia.review_shop.constant.ReputationBaseURL
import com.tokopedia.review_shop.product.data.source.ReviewProductApi
import com.tokopedia.user.session.UserSession
import retrofit2.Retrofit

class ReviewProductService(private val context: Context?, private val networkRouter: NetworkRouter?, private val userSession: UserSession?) : BaseReputationService() {
    var api: ReviewProductApi? = null
        private set

    private fun initApiService() {
        val retrofit = createRetrofit(
                context,
                ReputationBaseURL.BASE_DOMAIN,
                networkRouter,
                userSession)
        api = retrofit.create(ReviewProductApi::class.java)
    }

    companion object {
        val TAG = ReviewProductService::class.java.simpleName
    }

    init {
        initApiService()
    }
}