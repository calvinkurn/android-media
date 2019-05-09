package com.tokopedia.search.result.presentation.presenter.subscriber

import com.tokopedia.discovery.newdiscovery.base.InitiateSearchListener
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import rx.Observable

class InitiateSearchSubscriberTest {

    private val initiateSearchListener = mock(InitiateSearchListener::class.java)
    private val initiateSearchSubscriber = InitiateSearchSubscriber(initiateSearchListener)

    @Before
    fun setUp() {

    }

    private fun verifyInitiateSearchListener(wantedNumberOfInvocations: Int) : InitiateSearchListener {
        return verify(initiateSearchListener, times(wantedNumberOfInvocations))
    }

    @Test
    fun onNext_GotNulls_CallListenerHandleResponseError() {
        Observable.just(null).subscribe(initiateSearchSubscriber)

        verifyInitiateSearchListener(0).onHandleResponseSearch(anyBoolean())
        verifyInitiateSearchListener(0).onHandleApplink(anyString())
        verifyInitiateSearchListener(0).onHandleResponseUnknown()
        verifyInitiateSearchListener(1).onHandleResponseError()
    }

    @Test
    fun onError() {

    }
}