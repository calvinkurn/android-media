package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox

import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.ProductRevIncentiveOvoDomain
import com.tokopedia.usecase.RequestParams
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import rx.Observable

class GetProductIncentiveOvoUseCaseTest {

    @RelaxedMockK
    lateinit var getProductIncentiveOvoUseCase: GetProductIncentiveOvoUseCase

    @Before
    fun before() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success when create observable`() {
        val params = RequestParams.EMPTY

        every {
            getProductIncentiveOvoUseCase.createObservable(any())
        } returns Observable.just(ProductRevIncentiveOvoDomain())

        val testSubscriber = getProductIncentiveOvoUseCase.createObservable(params).test()
        verify {
            getProductIncentiveOvoUseCase.createObservable(any())
        }
        testSubscriber.assertCompleted()
        testSubscriber.assertNoErrors()
    }

    @Test
    fun `should failed when create observable`() {
        val throwable = Throwable()

        val params = RequestParams.EMPTY

        every {
            getProductIncentiveOvoUseCase.createObservable(any())
        } returns Observable.error(throwable)

        val testSubscriber = getProductIncentiveOvoUseCase.createObservable(params).test()
        verify {
            getProductIncentiveOvoUseCase.createObservable(any())
        }
        testSubscriber.assertError(throwable)
    }
}