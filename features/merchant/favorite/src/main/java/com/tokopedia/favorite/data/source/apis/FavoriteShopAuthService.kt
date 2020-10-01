package com.tokopedia.favorite.data.source.apis

import com.tokopedia.core.network.retrofit.services.AuthService
import com.tokopedia.url.TokopediaUrl.Companion.getInstance
import retrofit2.Retrofit

/**
 * Created by naveengoyal on 5/7/18.
 */
class FavoriteShopAuthService : AuthService<FavoriteShopApi>() {

    override fun initApiService(retrofit: Retrofit) {
        api = retrofit.create(FavoriteShopApi::class.java)
    }

    override fun getBaseUrl(): String {
        return getInstance().GQL
    }

    override fun getApi(): FavoriteShopApi {
        return api
    }

}