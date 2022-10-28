package com.tokopedia.favorite.domain.interactor

import com.tokopedia.favorite.domain.FavoriteRepository
import com.tokopedia.favorite.domain.model.TopAdsShop
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * @author Kulomady on 2/9/17.
 */
class GetTopAdsShopUseCase(private val favoriteRepository: FavoriteRepository) : UseCase<TopAdsShop>() {

    companion object {
        const val KEY_IS_FORCE_REFRESH = "isForceRefresh"
        const val KEY_ITEM = "item"
        const val KEY_SRC = "src"
        const val KEY_PAGE = "page"
        const val KEY_USER_ID = "user_id"
        const val KEY_DEP_ID = "dep_id"
        const val KEY_EP = "ep"
        const val KEY_TEMPLATE_ID = "template_id"
        const val KEY_DEVICE = "device"
        const val TOPADS_PAGE_DEFAULT_VALUE = "1"
        const val TOPADS_ITEM_DEFAULT_VALUE = "4"
        const val SRC_FAV_SHOP_VALUE = "fav_shop"
        const val EP_VALUE = "headline"
        const val DEVICE_VALUE = "android"
        const val TEMPLATE_ID_VALUE = "3"

        fun defaultParams(addressData: Map<String, String>): RequestParams {
            val params = RequestParams.create().apply {
                putString(KEY_PAGE, TOPADS_PAGE_DEFAULT_VALUE)
                putString(KEY_ITEM, TOPADS_ITEM_DEFAULT_VALUE)
                putString(KEY_SRC, SRC_FAV_SHOP_VALUE)
                putString(KEY_EP, EP_VALUE)
                putString(KEY_DEVICE, DEVICE_VALUE)
                putString(KEY_TEMPLATE_ID, TEMPLATE_ID_VALUE)
            }
            params.putAll(addressData)
            return params
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<TopAdsShop> {
        val isFreshData = isForceRefresh(requestParams)
        val param = requestParams.parameters
        return if (isFreshData) {
            favoriteRepository.getFreshTopAdsShop(requestParams.parameters)
        } else {
            favoriteRepository.getTopAdsShop(param)
        }
    }

    private fun isForceRefresh(requestParams: RequestParams): Boolean {
        val isForceRefresh = requestParams.getBoolean(KEY_IS_FORCE_REFRESH, false)
        requestParams.clearValue(KEY_IS_FORCE_REFRESH)
        return isForceRefresh
    }

}
