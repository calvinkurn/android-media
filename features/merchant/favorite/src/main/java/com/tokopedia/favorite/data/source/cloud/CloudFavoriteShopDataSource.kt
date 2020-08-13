package com.tokopedia.favorite.data.source.cloud

import android.content.Context
import android.content.res.Resources
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.TKPDMapParam
import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.favorite.R
import com.tokopedia.favorite.data.FavoriteShopResponseValidator
import com.tokopedia.favorite.data.mapper.FavoritShopGraphQlMapper
import com.tokopedia.favorite.data.source.apis.FavoriteShopAuthService
import com.tokopedia.favorite.data.source.local.LocalFavoriteShopDataSource
import com.tokopedia.favorite.domain.interactor.GetFavoriteShopUsecase
import com.tokopedia.favorite.domain.model.FavoritShopResponseData
import com.tokopedia.favorite.domain.model.FavoriteShop
import com.tokopedia.network.data.model.response.GraphqlResponse
import com.tokopedia.user.session.UserSession
import retrofit2.Response
import rx.Observable
import rx.functions.Action1
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

/**
 * @author Kulomady on 1/19/17.
 */
class CloudFavoriteShopDataSource(private val context: Context, private val gson: Gson) {

    private val favoriteShopAuthService = FavoriteShopAuthService()
    private val userSession  = UserSession(context)

    fun getFavorite(param: HashMap<String, String>?,
                    isMustSaveToCache: Boolean): Observable<FavoriteShop> {
        val tkpdMapParam = TKPDMapParam<String, String>()
        tkpdMapParam.putAll(param!!)
        val paramWithAuth = AuthUtil.generateParamsNetwork(userSession.userId, userSession.deviceId, tkpdMapParam)
        return if (isMustSaveToCache) {
            favoriteShopAuthService.api
                    .getFavoritShopsData(getRequestPayload(context, paramWithAuth))
                    .doOnNext(validateResponse())
                    .map(FavoritShopGraphQlMapper(context, gson))
        } else {
            favoriteShopAuthService.api
                    .getFavoritShopsData(getRequestPayload(context, paramWithAuth))
                    .map(FavoritShopGraphQlMapper(context, gson))
        }
    }

    private fun getRequestPayload(context: Context, params: TKPDMapParam<String, String>): String {
        return String.format(
                loadRawString(context.resources, R.raw.favorit_shop_query),
                params[GetFavoriteShopUsecase.KEY_PAGE],
                params[GetFavoriteShopUsecase.KEY_PER_PAGE]
        )
    }

    private fun loadRawString(resources: Resources, resId: Int): String {
        val rawResource = resources.openRawResource(resId)
        val content = streamToString(rawResource)
        try {
            rawResource.close()
        } catch (e: IOException) {
        }
        return content
    }

    private fun streamToString(`in`: InputStream): String {
        var temp: String
        val bufferedReader = BufferedReader(InputStreamReader(`in`))
        val stringBuilder = StringBuilder()
        try {
            temp = bufferedReader.readLine()
            while (temp != null) {
                stringBuilder.append("$temp\n")
                temp = bufferedReader.readLine()
            }
        } catch (e: IOException) {
        }
        return stringBuilder.toString()
    }

    private fun validateResponse(): Action1<Response<GraphqlResponse<FavoritShopResponseData>>> {
        return FavoriteShopResponseValidator.validate { response -> saveResponseToCache(response) }
    }

    private fun saveResponseToCache(response: Response<GraphqlResponse<FavoritShopResponseData>>) {
        PersistentCacheManager.instance.put(
                LocalFavoriteShopDataSource.CACHE_KEY_FAVORITE_SHOP,
                response.body().toString(),
                -System.currentTimeMillis()
        )
    }

}