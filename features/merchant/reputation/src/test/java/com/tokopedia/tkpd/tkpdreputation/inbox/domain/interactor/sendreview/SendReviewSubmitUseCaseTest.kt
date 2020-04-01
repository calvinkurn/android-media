package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview

import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewSubmitDomain
import com.tokopedia.usecase.RequestParams
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import rx.Observable

/**
 * Created By @ilhamsuaib on 2020-01-06
 */

class SendReviewSubmitUseCaseTest {

    @RelaxedMockK
    lateinit var reputationRepository: ReputationRepository

    @RelaxedMockK
    lateinit var requestParams: RequestParams

    @Before
    fun before() {
        MockKAnnotations.init(this)
    }

    private val sendReviewSubmitUseCase by lazy {
        SendReviewSubmitUseCase(reputationRepository)
    }

    @Test
    fun `should success when create observable`() {
        every {
            reputationRepository.sendReviewSubmit(any())
        } returns Observable.just(SendReviewSubmitDomain(anyInt(), anyInt()))

        val testSubscriber = sendReviewSubmitUseCase.createObservable(requestParams).test()

        verify {
            reputationRepository.sendReviewSubmit(requestParams)
        }

        testSubscriber.assertCompleted()
        testSubscriber.assertNoErrors()
    }

    @Test
    fun `should failed when create observable`() {
        val throwable = Throwable()
        every {
            reputationRepository.sendReviewSubmit(any())
        } returns Observable.error(throwable)

        val testSubscriber = sendReviewSubmitUseCase.createObservable(requestParams).test()

        verify {
            reputationRepository.sendReviewSubmit(requestParams)
        }

        testSubscriber.assertError(throwable)
    }
}