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

class GetInitialDataPageUseCaseWithCoroutine constructor(
        context: Context,
        private val getFavoriteShopUsecase: GetFavoriteShopUseCaseWithCoroutine,
        private val getTopAdsShopUseCase: GetTopAdsShopUseCaseWithCoroutine,
        private val topAdsAddressHelper: TopAdsAddressHelper
): UseCase<DataFavorite>() {

    private val cacheHandler: CacheHandler = CacheHandler(context, CacheHandler.TOP_ADS_CACHE)
    private val random: Random = Random()
    private val userSession: UserSessionInterface = UserSession(context)

    override suspend fun executeOnBackground(): DataFavorite {
        return coroutineScope {
            val favoriteShop = async { getFavoriteShop() }
            val topAdsShop = async { getTopAdsShop() }
            validateDataFavorite(topAdsShop.await(), favoriteShop.await())
        }
    }

    private fun validateDataFavorite(adsShop: TopAdsShop, favoriteShop: FavoriteShop): DataFavorite {
        if (adsShop.isNetworkError &&
                favoriteShop.isNetworkError &&
                adsShop.topAdsShopItemList == null &&
                favoriteShop.data == null) {
            throw RuntimeException("all request network error")
        }
        val dataFavorite = DataFavorite()
        dataFavorite.topAdsShop = adsShop
        dataFavorite.favoriteShop = favoriteShop
        return dataFavorite
    }

    private suspend fun getFavoriteShop(): FavoriteShop {
        val defaultParams = GetFavoriteShopUsecase.defaultParams
        defaultParams.putBoolean(GetFavoriteShopUsecase.KEY_IS_FIRST_PAGE, true)
        getFavoriteShopUsecase.requestParams = defaultParams
        return getFavoriteShopUsecase.executeOnBackground()
    }

    private suspend fun getTopAdsShop(): TopAdsShop {
        val requestParams = GetTopAdsShopUseCase.defaultParams(topAdsAddressHelper.getAddressData())
        requestParams.putBoolean(GetTopAdsShopUseCase.KEY_IS_FORCE_REFRESH, false)
        requestParams.putString(GetTopAdsShopUseCase.KEY_USER_ID, userSession.userId)
        val preferredCacheList = cacheHandler.getArrayListInteger(CacheHandler.KEY_PREFERRED_CATEGORY)
        requestParams.putInt(GetTopAdsShopUseCase.KEY_DEP_ID, getRandomId(preferredCacheList))
        getTopAdsShopUseCase.requestParams = requestParams

        return getTopAdsShopUseCase.executeOnBackground()
    }

    private fun getRandomId(ids: List<Int>): Int {
        return if (ids.isEmpty()) {
            0
        } else {
            ids[random.nextInt(ids.size)]
        }
    }

}
