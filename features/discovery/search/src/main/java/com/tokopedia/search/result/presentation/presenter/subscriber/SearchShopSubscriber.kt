package com.tokopedia.search.result.presentation.presenter.subscriber

import com.tokopedia.search.result.domain.model.SearchShopModel
import com.tokopedia.search.result.presentation.mapper.ShopViewModelMapper
import com.tokopedia.search.result.presentation.view.listener.SearchShopListener
import rx.Subscriber

class SearchShopSubscriber(
    private val searchShopListener : SearchShopListener
) : Subscriber<SearchShopModel>() {

    override fun onNext(searchShopModel: SearchShopModel?) {
        if(searchShopModel == null) {
            searchShopListener.onFailed()
            return
        }

        val shopViewModel = ShopViewModelMapper().convertToShopViewModel(searchShopModel)
        searchShopListener.onSuccess(shopViewModel.shopItemList, shopViewModel.isHasNextPage)
    }

    override fun onCompleted() {
        searchShopListener.getDynamicFilter()
    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
        searchShopListener.onFailed()
    }
}