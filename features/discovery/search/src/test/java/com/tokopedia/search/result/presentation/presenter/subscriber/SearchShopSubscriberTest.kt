package com.tokopedia.search.result.presentation.presenter.subscriber

import com.tokopedia.search.result.domain.model.SearchShopModel
import com.tokopedia.search.result.presentation.mapper.ShopViewModelMapper
import com.tokopedia.search.result.presentation.model.ShopViewModel
import com.tokopedia.search.result.presentation.view.listener.SearchShopListener
import org.junit.Test
import org.mockito.Mockito.*
import rx.Observable

class SearchShopSubscriberTest {

    private val searchShopListener = mock(SearchShopListener::class.java)
    private val shopViewModelMapper = mock(ShopViewModelMapper::class.java)
    private val searchShopSubscriber = SearchShopSubscriber(searchShopListener, shopViewModelMapper)

    @Test
    fun onNext_GivenNulls_ShouldCallSearchShopFail() {
        Observable.just(null).subscribe(searchShopSubscriber)

        verify(searchShopListener, never()).onSearchShopSuccess(null, false)
        verify(searchShopListener).onSearchShopFailed()
        verify(searchShopListener).getDynamicFilter()
    }

    @Test
    fun onNext_GivenSearchShopModel_ShouldCallSearchShopSuccess() {
        val searchShopModel = SearchShopModel()
        val shopViewModel = ShopViewModel()
        `when`(shopViewModelMapper.convertToShopViewModel(searchShopModel)).thenReturn(shopViewModel)

        Observable.just(searchShopModel).subscribe(searchShopSubscriber)

        verify(searchShopListener).onSearchShopSuccess(shopViewModel.shopItemList, shopViewModel.isHasNextPage)
        verify(searchShopListener, never()).onSearchShopFailed()
        verify(searchShopListener).getDynamicFilter()
    }

    @Test
    fun onError_ThrowingNullError_ShouldRenderFail() {
        Observable.error<SearchShopModel>(null).subscribe(searchShopSubscriber)

        verify(searchShopListener, never()).onSearchShopSuccess(null, false)
        verify(searchShopListener).onSearchShopFailed()
        verify(searchShopListener, never()).getDynamicFilter()
    }

    @Test
    fun onError_ThrowingAnyException_ShouldPrintStackTraceAndRenderFail() {
        val throwable = mock(Throwable::class.java)
        doNothing().`when`(throwable).printStackTrace()
        Observable.error<SearchShopModel>(throwable).subscribe(searchShopSubscriber)

        verify(throwable).printStackTrace()
        verify(searchShopListener, never()).onSearchShopSuccess(null, false)
        verify(searchShopListener).onSearchShopFailed()
        verify(searchShopListener, never()).getDynamicFilter()
    }
}