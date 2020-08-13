package com.tokopedia.favorite.view

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.favorite.view.viewmodel.FavoriteShopViewModel
import com.tokopedia.favorite.view.viewmodel.TopAdsShopItem

/**
 * @author Kulomady on 1/19/17.
 */
internal interface FavoriteContract {
    interface View : CustomerView {
        fun refreshDataFavorite(elementList: List<Visitable<*>?>?)
        fun showInitialDataPage(dataFavorite: List<Visitable<*>?>?)
        fun showMoreDataFavoriteShop(elementList: List<Visitable<*>?>?)
        fun addFavoriteShop(shopViewModel: FavoriteShopViewModel?)
        fun showRefreshLoading()
        fun hideRefreshLoading()
        fun stopTracePerformanceMonitoring()
        val isLoading: Boolean
        fun showLoadMoreLoading()
        fun showErrorLoadMore()
        fun showErrorLoadData()
        fun showFavoriteShopFailedMessage()
        fun dismissFavoriteShopFailedMessage()
        fun showTopadsShopFailedMessage()
        fun dismissTopadsShopFailedMessage()
        fun validateMessageError()
        fun showErrorAddFavoriteShop()
        fun stopLoadingFavoriteShop()
        fun sendFavoriteShopImpression(clickUrl: String?)
    }

    interface Presenter : CustomerPresenter<View?> {
        fun loadInitialData()
        fun addFavoriteShop(view: android.view.View?, shopItem: TopAdsShopItem?)
        fun refreshAllDataFavoritePage()
        fun loadMoreFavoriteShop()
        fun onSaveDataBeforeRotate(outState: Bundle?)
        fun onViewStateRestored(outState: Bundle?)
    }
}
