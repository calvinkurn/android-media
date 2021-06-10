package com.tokopedia.favorite.domain.interactor

import com.tokopedia.favorite.domain.FavoriteRepository
import com.tokopedia.favorite.domain.model.FavoriteShop
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetFavoriteShopUseCaseWithCoroutine
@Inject constructor(
        private val favoriteRepository: FavoriteRepository
): UseCase<FavoriteShop>() {

    companion object {
        const val KEY_OPTION_LOCATION = "option_location"
        const val KEY_OPTION_NAME = "option_name"
        const val KEY_PAGE = "page"
        const val KEY_PER_PAGE = "per_page"
        const val KEY_IS_FIRST_PAGE = "isFirstPage"
        const val DEFAULT_OPTION_NAME = ""
        const val DEFAULT_OPTION_LOCATION = ""
        const val DEFAULT_PER_PAGE = "20"
        const val INITIAL_VALUE = "1"

        val defaultParams: RequestParams
            get() {
                val params = RequestParams.create()
                params.putString(KEY_OPTION_LOCATION, DEFAULT_OPTION_LOCATION)
                params.putString(KEY_OPTION_NAME, DEFAULT_OPTION_NAME)
                params.putString(KEY_PER_PAGE, DEFAULT_PER_PAGE)
                params.putString(KEY_PAGE, INITIAL_VALUE)
                return params
            }
    }

    lateinit var requestParams: RequestParams

    override suspend fun executeOnBackground(): FavoriteShop {
        val isFirstPage =
                requestParams.getBoolean(GetFavoriteShopUsecase.KEY_IS_FIRST_PAGE, false)
        requestParams.clearValue(GetFavoriteShopUsecase.KEY_IS_FIRST_PAGE)
        val paramsAllInString = requestParams.paramsAllValueInString
        return if (isFirstPage) {
            favoriteRepository.suspendGetFirstPageFavoriteShop(paramsAllInString)
        } else {
            favoriteRepository.suspendGetFavoriteShop(paramsAllInString)
        }
    }

}
