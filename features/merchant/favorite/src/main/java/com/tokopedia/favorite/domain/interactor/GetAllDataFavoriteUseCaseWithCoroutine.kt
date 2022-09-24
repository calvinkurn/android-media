package com.tokopedia.favorite.domain.interactor

import android.content.Context
import com.tokopedia.favorite.domain.model.DataFavorite
import com.tokopedia.favorite.domain.model.FavoriteShop
import com.tokopedia.favorite.domain.model.TopAdsShop
import com.tokopedia.topads.sdk.utils.CacheHandler
import com.tokopedia.topads.sdk.utils.TopAdsAddressHelper
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.util.*

class GetAllDataFavoriteUseCaseWithCoroutine(
        context: Context,
        private val getFavoriteShopUsecase: GetFavoriteShopUseCaseWithCoroutine,
        private val getTopAdsShopUseCase: GetTopAdsShopUseCaseWithCoroutine,
        private val topAdsAddressHelper: TopAdsAddressHelper
): UseCase<DataFavorite>() {

    private val cacheHandler: CacheHandler = CacheHandler(context, CacheHandler.TOP_ADS_CACHE)
    private val random: Random = Random()
    private val userSession: UserSessionInterface = UserSession(context)

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

    private suspend fun getTopAdsShop(): TopAdsShop {
        val requestParams = GetTopAdsShopUseCase.defaultParams(topAdsAddressHelper.getAddressData())
        requestParams.putBoolean(GetTopAdsShopUseCase.KEY_IS_FORCE_REFRESH, true)
        requestParams.putString(GetTopAdsShopUseCase.KEY_USER_ID, userSession.userId)
        val preferredCacheList = cacheHandler.getArrayListInteger(CacheHandler.KEY_PREFERRED_CATEGORY)
        requestParams.putInt(GetTopAdsShopUseCase.KEY_DEP_ID, getRandomId(preferredCacheList))
        getTopAdsShopUseCase.requestParams = requestParams

        return getTopAdsShopUseCase.executeOnBackground()
    }

    private suspend fun getFavoriteShopList(): FavoriteShop {
        val defaultParams = GetFavoriteShopUsecase.defaultParams
        defaultParams.putBoolean(GetFavoriteShopUsecase.KEY_IS_FIRST_PAGE, true)
        getFavoriteShopUsecase.requestParams = defaultParams
        return getFavoriteShopUsecase.executeOnBackground()
    }

    private fun getRandomId(ids: List<Int>): Int {
        return if (ids.isNotEmpty()) {
            ids[random.nextInt(ids.size)]
        } else {
            0
        }
    }

    override suspend fun executeOnBackground(): DataFavorite {
        return coroutineScope {
            val favoriteShopList = async { getFavoriteShopList() }
            val topAds = async { getTopAdsShop() }
            validateDataFavorite(topAds.await(), favoriteShopList.await())
        }
    }

}
