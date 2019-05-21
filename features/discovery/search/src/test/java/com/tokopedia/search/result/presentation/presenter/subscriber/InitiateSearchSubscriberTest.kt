package com.tokopedia.search.result.presentation.presenter.subscriber

import com.tokopedia.discovery.newdiscovery.base.InitiateSearchListener
import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel
import org.junit.Test
import org.mockito.Mockito.*
import rx.Observable

class InitiateSearchSubscriberTest {

    private val initiateSearchListener = mock(InitiateSearchListener::class.java)
    private val initiateSearchSubscriber = InitiateSearchSubscriber(initiateSearchListener)

    private fun verifyInitiateSearchListener(wantedNumberOfInvocations: Int) : InitiateSearchListener {
        return verify(initiateSearchListener, times(wantedNumberOfInvocations))
    }

    @Test
    fun onNext_GivenNulls_CallListenerHandleResponseError() {
        Observable.just(null).subscribe(initiateSearchSubscriber)

        verifyInitiateSearchListener(0).onHandleResponseSearch(anyBoolean())
        verifyInitiateSearchListener(0).onHandleApplink(anyString())
        verifyInitiateSearchListener(0).onHandleResponseUnknown()
        verifyInitiateSearchListener(1).onHandleResponseError()
    }

    @Test
    fun onNext_GivenNullSearchProduct_ShouldHandleResponseSearchWithHasCatalogFalse() {
        val initiateSearchModel = InitiateSearchModel(null)

        subscribeAndVerifyHandleRepsonseSearch(initiateSearchModel)
    }

    @Test
    fun onNext_GivenNullRedirection_ShouldHandleResponseSearchWithHasCatalogFalse() {
        val initiateSearchModel = InitiateSearchModel(InitiateSearchModel.SearchProduct(null))

        subscribeAndVerifyHandleRepsonseSearch(initiateSearchModel)
    }

    @Test
    fun onNext_GivenNullRedirectApplink_ShouldHandleReponseSearchWithHasCatalogFalse() {
        val initiateSearchModel = InitiateSearchModel(InitiateSearchModel.SearchProduct(
            InitiateSearchModel.SearchProduct.Redirection(null)))

        subscribeAndVerifyHandleRepsonseSearch(initiateSearchModel)
    }

    @Test
    fun onNext_GivenNullCatalogs_ShouldHandleResponseSearchWithHasCatalogFalse() {
        val initiateSearchModel = InitiateSearchModel(InitiateSearchModel.SearchProduct(null, null))

        subscribeAndVerifyHandleRepsonseSearch(initiateSearchModel)
    }

    @Test
    fun onNext_GivenEmptyCatalogs_ShouldHandleResponseSearchWithHasCatalogFalse() {
        val initiateSearchModel = InitiateSearchModel(InitiateSearchModel.SearchProduct(null, listOf()))

        subscribeAndVerifyHandleRepsonseSearch(initiateSearchModel)
    }

    private fun subscribeAndVerifyHandleRepsonseSearch(initiateSearchModel: InitiateSearchModel) {
        Observable.just(initiateSearchModel).subscribe(initiateSearchSubscriber)

        verifyInitiateSearchListener(1).onHandleResponseSearch(false)
        verifyInitiateSearchListener(0).onHandleApplink(anyString())
        verifyInitiateSearchListener(0).onHandleResponseUnknown()
        verifyInitiateSearchListener(0).onHandleResponseError()
    }

    @Test
    fun onNext_GivenSomeCatalogs_ShouldHandleResponseSearchWithHasCatalogTrue() {
        val initiateSearchModel = InitiateSearchModel(InitiateSearchModel.SearchProduct(
            null,
            mutableListOf<InitiateSearchModel.SearchProduct.Catalog>().apply {
                add(InitiateSearchModel.SearchProduct.Catalog())
            }
        ))

        Observable.just(initiateSearchModel).subscribe(initiateSearchSubscriber)

        verifyInitiateSearchListener(1).onHandleResponseSearch(true)
        verifyInitiateSearchListener(0).onHandleApplink(anyString())
        verifyInitiateSearchListener(0).onHandleResponseUnknown()
        verifyInitiateSearchListener(0).onHandleResponseError()
    }

    @Test
    fun onNext_GivenRandomApplinkString_ShouldHandleReponseApplink() {
        val redirectApplink = "some_random_string_or_could_also_be_real_applink"
        val initiateSearchModel = InitiateSearchModel(InitiateSearchModel.SearchProduct(
            InitiateSearchModel.SearchProduct.Redirection(redirectApplink)))

        Observable.just(initiateSearchModel).subscribe(initiateSearchSubscriber)

        verifyInitiateSearchListener(0).onHandleResponseSearch(anyBoolean())
        verifyInitiateSearchListener(1).onHandleApplink(redirectApplink)
        verifyInitiateSearchListener(0).onHandleResponseUnknown()
        verifyInitiateSearchListener(0).onHandleResponseError()
    }

    @Test
    fun onError_ObservableThrowingNullError_ShouldHandleResponseError() {
        Observable.error<InitiateSearchModel>(null).subscribe(initiateSearchSubscriber)

        verifyInitiateSearchListener(0).onHandleResponseSearch(anyBoolean())
        verifyInitiateSearchListener(0).onHandleApplink(anyString())
        verifyInitiateSearchListener(0).onHandleResponseUnknown()
        verifyInitiateSearchListener(1).onHandleResponseError()
    }

    @Test
    fun onError_ObservableThrowingException_ShouldHandleResponseError() {
        val throwable = mock(Throwable::class.java)
        doNothing().`when`(throwable).printStackTrace()
        Observable.error<InitiateSearchModel>(throwable).subscribe(initiateSearchSubscriber)

        verify(throwable).printStackTrace()
        verifyInitiateSearchListener(0).onHandleResponseSearch(anyBoolean())
        verifyInitiateSearchListener(0).onHandleApplink(anyString())
        verifyInitiateSearchListener(0).onHandleResponseUnknown()
        verifyInitiateSearchListener(1).onHandleResponseError()
    }
}