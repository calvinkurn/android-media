package com.tokopedia.favorite.domain.interactor

import android.content.Context
import com.tokopedia.favorite.domain.interactor.GetFavoriteShopUsecase.Companion.defaultParams
import com.tokopedia.favorite.domain.interactor.GetTopAdsShopUseCase.Companion.defaultParams
import com.tokopedia.favorite.domain.model.DataFavorite
import com.tokopedia.favorite.domain.model.FavoriteShop
import com.tokopedia.favorite.domain.model.TopAdsShop
import com.tokopedia.topads.sdk.utils.CacheHandler
import com.tokopedia.topads.sdk.utils.TopAdsAddressHelper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable
import java.util.*

/**
 * @author Kulomady on 2/9/17.
 */
class GetAllDataFavoriteUseCase(
        private val context: Context,
        private val getFavoriteShopUsecase: GetFavoriteShopUsecase,
        private val getTopAdsShopUseCase: GetTopAdsShopUseCase,
        private val topAdsAddressHelper: TopAdsAddressHelper
) : UseCase<DataFavorite>() {

    private val cacheHandler: CacheHandler = CacheHandler(context, CacheHandler.TOP_ADS_CACHE)
    private val random: Random = Random()
    private val userSession: UserSessionInterface = UserSession(context)

    override fun createObservable(requestParams: RequestParams): Observable<DataFavorite> {
        return Observable.zip(topAdsShop, favoriteShopList) {
            adsShop, favoriteShop -> validateDataFavorite(adsShop, favoriteShop)
        }
    }

    private fun validateDataFavorite(
            adsShop: TopAdsShop,
            favoriteShop: FavoriteShop
    ): DataFavorite {
        if (adsShop.isNetworkError
                && favoriteShop.isNetworkError
                && adsShop.topAdsShopItemList == null
                && favoriteShop.data == null) {
            throw RuntimeException("all network error")
        }
        val dataFavorite = DataFavorite()
        dataFavorite.topAdsShop = adsShop
        dataFavorite.favoriteShop = favoriteShop
        return dataFavorite
    }

    private val topAdsShop: Observable<TopAdsShop>
        get() {
            val requestParams = defaultParams(topAdsAddressHelper.getAddressData())
            requestParams.putBoolean(GetTopAdsShopUseCase.KEY_IS_FORCE_REFRESH, true)
            requestParams.putString(GetTopAdsShopUseCase.KEY_USER_ID, userSession.userId)
            val preferredCacheList = cacheHandler.getArrayListInteger(CacheHandler.KEY_PREFERRED_CATEGORY)
            requestParams.putInt(GetTopAdsShopUseCase.KEY_DEP_ID, getRandomId(preferredCacheList))
            return getTopAdsShopUseCase.createObservable(requestParams)
        }

    private val favoriteShopList: Observable<FavoriteShop>
        get() {
            val defaultParams = defaultParams
            defaultParams.putBoolean(GetFavoriteShopUsecase.KEY_IS_FIRST_PAGE, true)
            return getFavoriteShopUsecase.createObservable(defaultParams)
        }

    private fun getRandomId(ids: List<Int>): Int {
        return if (ids.isNotEmpty()) {
            ids[random.nextInt(ids.size)]
        } else {
            0
        }
    }

}
