package com.tokopedia.sellerhomedrawer.domain.service

import android.content.Context
import com.tokopedia.network.CommonNetwork
import com.tokopedia.network.NetworkRouter
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession

class SellerDrawerService(val context: Context,
                          val userSession: UserSession,
                          val networkRouter: NetworkRouter) {

    lateinit var api: SellerDrawerApi

    companion object {
        val BASE_URL = TokopediaUrl.getInstance().GQL
    }

    init {
        initApiService()
    }

    protected fun initApiService() {
        val retrofit = CommonNetwork.createRetrofit(context, BASE_URL, networkRouter, userSession)
        api = retrofit.create(SellerDrawerApi::class.java)
    }
}