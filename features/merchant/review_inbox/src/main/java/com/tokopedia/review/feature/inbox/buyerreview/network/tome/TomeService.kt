package com.tokopedia.review.feature.inbox.buyerreview.network.tome

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.network.NetworkRouter
import com.tokopedia.review.feature.inbox.buyerreview.network.BaseReputationService
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import retrofit2.Retrofit
import javax.inject.Inject

class TomeService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkRouter: NetworkRouter,
    private val userSession: UserSession
) : BaseReputationService() {

    companion object {
        val TAG: String = TomeService::class.java.simpleName
    }

    var api: TomeApi = initApiService()
        private set

    private fun initApiService(): TomeApi {
        val retrofit: Retrofit = createRetrofit(
            context,
            TokopediaUrl.getInstance().TOME,
            networkRouter,
            userSession
        )
        return retrofit.create(TomeApi::class.java)
    }
}