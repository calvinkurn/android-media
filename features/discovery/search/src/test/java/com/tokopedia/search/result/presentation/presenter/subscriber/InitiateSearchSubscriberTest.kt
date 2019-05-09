package com.tokopedia.search.result.presentation.presenter.subscriber

import com.tokopedia.discovery.newdiscovery.base.InitiateSearchListener
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import rx.Observable

class InitiateSearchSubscriberTest {

    private val initiateSearchListener = mock(InitiateSearchListener::class.java)
    private val initiateSearchSubscriber = InitiateSearchSubscriber(initiateSearchListener)

    @Before
    fun setUp() {

    }

    @Test
    fun onNext_GotNulls_CallListenerHandleResponseError() {
        Observable.just(null).subscribe(initiateSearchSubscriber)

        verify(initiateSearchListener).onHandleResponseError()
    }

    @Test
    fun onCompleted() {
    }

    @Test
    fun onError() {
    }
}