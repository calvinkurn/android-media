package com.tokopedia.search.result.presentation.presenter.subscriber

import com.tokopedia.search.result.domain.model.SearchShopModel
import com.tokopedia.search.result.presentation.mapper.ShopViewModelMapper
import com.tokopedia.search.result.presentation.view.listener.SearchShopListener
import rx.Subscriber

class SearchShopSubscriber @JvmOverloads constructor(
    private val searchShopListener : SearchShopListener,
    private val shopViewModelMapper: ShopViewModelMapper = ShopViewModelMapper()
) : Subscriber<SearchShopModel>() {

    override fun onNext(searchShopModel: SearchShopModel?) {
        if(searchShopModel == null) {
            searchShopListener.onSearchShopFailed()
            return
        }

        val shopViewModel = shopViewModelMapper.convertToShopViewModel(searchShopModel)
        searchShopListener.onSearchShopSuccess(shopViewModel.shopItemList, shopViewModel.isHasNextPage)
    }

    override fun onCompleted() {
        searchShopListener.getDynamicFilter()
    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
        searchShopListener.onSearchShopFailed()
    }
}