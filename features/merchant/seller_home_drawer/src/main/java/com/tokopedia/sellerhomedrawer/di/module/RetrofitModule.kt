package com.tokopedia.sellerhomedrawer.di.module

import android.content.Context
import com.tokopedia.network.CommonNetwork
import com.tokopedia.network.NetworkRouter
import com.tokopedia.sellerhomedrawer.data.constant.SellerDrawerUrl
import com.tokopedia.sellerhomedrawer.di.SellerHomeDashboardScope
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@SellerHomeDashboardScope
@Module
class RetrofitModule {

    private val baseUrl = SellerDrawerUrl.User.URL_NOTIFICATION

    @Provides
    fun provideRetrofit(context: Context,
                        networkRouter: NetworkRouter,
                        userSession: UserSession): Retrofit {
        return CommonNetwork.createRetrofit(context, baseUrl, networkRouter, userSession)
    }

    @Provides
    fun provideNetworkRouter(context: Context): NetworkRouter {
        return (context.applicationContext as NetworkRouter)
    }
}