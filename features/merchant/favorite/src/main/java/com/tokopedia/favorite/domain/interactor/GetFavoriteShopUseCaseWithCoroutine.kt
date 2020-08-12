package com.tokopedia.favorite.domain.interactor

import com.tokopedia.favorite.domain.FavoriteRepository
import com.tokopedia.favorite.domain.model.FavoriteShop
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class GetFavoriteShopUseCaseWithCoroutine constructor(
        private val favoriteRepository: FavoriteRepository
): UseCase<FavoriteShop>() {

    companion object {
        val KEY_OPTION_LOCATION = "option_location";
        val KEY_OPTION_NAME = "option_name";
        val KEY_PAGE = "page";
        val KEY_PER_PAGE = "per_page";
        val KEY_IS_FIRST_PAGE = "isFirstPage";
        val DEFAULT_OPTION_NAME = "";
        val DEFAULT_OPTION_LOCATION = "";
        val DEFAULT_PER_PAGE = "20";
        val INITIAL_VALUE = "1";

        fun getDefaultParams(): RequestParams {
            val params = RequestParams.create()
            params.putString(GetFavoriteShopUsecase.KEY_OPTION_LOCATION, GetFavoriteShopUsecase.DEFAULT_OPTION_LOCATION)
            params.putString(GetFavoriteShopUsecase.KEY_OPTION_NAME, GetFavoriteShopUsecase.DEFAULT_OPTION_NAME)
            params.putString(GetFavoriteShopUsecase.KEY_PER_PAGE, GetFavoriteShopUsecase.DEFAULT_PER_PAGE)
            params.putString(GetFavoriteShopUsecase.KEY_PAGE, GetFavoriteShopUsecase.INITIAL_VALUE)
            return params
        }
    }

    override suspend fun executeOnBackground(): FavoriteShop {
        TODO("Not yet implemented")
    }

}