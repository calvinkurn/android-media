package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview

import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewValidateDomain
import com.tokopedia.usecase.RequestParams
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import rx.Observable

/**
 * Created By @ilhamsuaib on 2020-01-06
 */

class SendReviewValidateUseCaseTest {

    @RelaxedMockK
    lateinit var requestParams: RequestParams

    @RelaxedMockK
    lateinit var reputationRepository: ReputationRepository

    private val sendReviewValidateUseCase by lazy {
        SendReviewValidateUseCase(reputationRepository)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test create observable success`() {
        every {
            reputationRepository.sendReviewValidation(any())
        } returns Observable.just(SendReviewValidateDomain(anyString(), anyInt(), anyInt()))
        val testSubscriber = sendReviewValidateUseCase.createObservable(requestParams).test()
        verifySequence {
            reputationRepository.sendReviewValidation(requestParams)
        }
        testSubscriber.assertCompleted()
        testSubscriber.assertNoErrors()
    }

    @Test
    fun `test create observable failed`() {
        val throwable = mockk<Throwable>()
        every {
            reputationRepository.sendReviewValidation(any())
        } returns Observable.error(throwable)
        val testSubscriber = sendReviewValidateUseCase.createObservable(requestParams).test()
        verifySequence {
            reputationRepository.sendReviewValidation(requestParams)
        }
        testSubscriber.assertError(throwable)
    }
}