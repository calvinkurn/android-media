package com.tokopedia.search.result.presentation.presenter.subscriber

import com.tokopedia.search.result.domain.model.SearchShopModel
import com.tokopedia.search.result.presentation.view.listener.SearchShopListener
import org.junit.Test
import org.mockito.Mockito.*
import rx.Observable

class SearchShopSubscriberTest {

    private val searchShopListener = mock(SearchShopListener::class.java)
    private val searchShopSubscriber = SearchShopSubscriber(searchShopListener)

    @Test
    fun onNext_GivenNulls_ShouldCallSearchShopFail() {
        Observable.just(null).subscribe(searchShopSubscriber)

        verify(searchShopListener, never()).onSearchShopSuccess(any(), any())
        verify(searchShopListener).onSearchShopFailed()
        verify(searchShopListener, never()).getDynamicFilter()
    }

    @Test
    fun onNext_GivenSearchShopModel_ShouldCallSearchShopSuccess() {
        val searchShopModel = SearchShopModel()
        Observable.just(searchShopModel).subscribe(searchShopSubscriber)

        verify(searchShopListener).onSearchShopSuccess(any(), any())
        verify(searchShopListener, never()).onSearchShopFailed()
        verify(searchShopListener).getDynamicFilter()
    }

    @Test
    fun onError() {

    }
}