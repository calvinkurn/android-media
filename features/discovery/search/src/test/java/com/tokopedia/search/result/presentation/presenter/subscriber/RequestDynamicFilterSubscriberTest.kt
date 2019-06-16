package com.tokopedia.search.result.presentation.presenter.subscriber

import com.tokopedia.discovery.common.data.DynamicFilterModel
import com.tokopedia.search.result.presentation.view.listener.RequestDynamicFilterListener
import org.junit.Test
import org.mockito.Mockito.*
import rx.Observable

class RequestDynamicFilterSubscriberTest {

    private val requestDynamicFilterListener = mock(RequestDynamicFilterListener::class.java)
    private val requestDynamicFilterSubscriber = RequestDynamicFilterSubscriber(requestDynamicFilterListener)

    @Test
    fun onNext_GivenNulls_ShouldRenderFail() {
        Observable.just(null).subscribe(requestDynamicFilterSubscriber)

        verify(requestDynamicFilterListener, never()).renderDynamicFilter(any())
        verify(requestDynamicFilterListener).renderFailRequestDynamicFilter()
    }

    @Test
    fun onNext_GivenDynamicFilterModel_ShouldRenderDynamicFilter() {
        val dynamicFilterModel = DynamicFilterModel()

        Observable.just(dynamicFilterModel).subscribe(requestDynamicFilterSubscriber)

        verify(requestDynamicFilterListener).renderDynamicFilter(dynamicFilterModel)
        verify(requestDynamicFilterListener, never()).renderFailRequestDynamicFilter()
    }

    @Test
    fun onError_ThrowingNullError_ShouldRenderFail() {
        Observable.error<DynamicFilterModel>(null).subscribe(requestDynamicFilterSubscriber)

        verify(requestDynamicFilterListener, never()).renderDynamicFilter(any())
        verify(requestDynamicFilterListener).renderFailRequestDynamicFilter()
    }

    @Test
    fun onError_ThrowingAnyException_ShouldPrintStackTraceAndRenderFail() {
        val throwable = mock(Throwable::class.java)
        doNothing().`when`(throwable).printStackTrace()
        Observable.error<DynamicFilterModel>(throwable).subscribe(requestDynamicFilterSubscriber)

        verify(throwable).printStackTrace()
        verify(requestDynamicFilterListener, never()).renderDynamicFilter(any())
        verify(requestDynamicFilterListener).renderFailRequestDynamicFilter()
    }
}