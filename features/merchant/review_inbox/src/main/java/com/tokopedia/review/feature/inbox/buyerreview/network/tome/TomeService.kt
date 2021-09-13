package com.tokopedia.review.feature.inbox.buyerreview.network.tome

import android.content.Context
import com.tokopedia.network.NetworkRouter
import com.tokopedia.review.feature.inbox.buyerreview.network.BaseReputationService
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import retrofit2.Retrofit

class TomeService constructor(
    private val context: Context?,
    private val networkRouter: NetworkRouter?,
    private val userSession: UserSession?
) : BaseReputationService() {
    var api: TomeApi? = null
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
            TokopediaUrl.getInstance().TOME,
            networkRouter,
            userSession
        )
        api = retrofit!!.create(TomeApi::class.java)
    }

    companion object {
        val TAG: String = TomeService::class.java.getSimpleName()
    }

    init {
        initApiService()
    }
}