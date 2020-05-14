package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview

import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewValidateDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.interactor.GenerateHostUseCase
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.interactor.UploadImageUseCase
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import rx.Observable

/**
 * Created By @ilhamsuaib on 2020-01-06
 */

class SendReviewUseCaseTest {

    @RelaxedMockK
    lateinit var reputationRepository: ReputationRepository

    private val sendReviewValidateUseCase by lazy {
        SendReviewValidateUseCase(reputationRepository)
    }

    @RelaxedMockK
    lateinit var generateHostUseCase: GenerateHostUseCase

    @RelaxedMockK
    lateinit var uploadImageUseCase: UploadImageUseCase

    @RelaxedMockK
    lateinit var sendReviewSubmitUseCase: SendReviewSubmitUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private val sendReviewUseCase by lazy {
        SendReviewUseCase(sendReviewValidateUseCase, generateHostUseCase, uploadImageUseCase, sendReviewSubmitUseCase)
    }

    @Test
    fun `should success when create observable`() {
        val params = RequestParams.create().apply {
            putObject(SendReviewUseCase.PARAM_LIST_IMAGE, arrayListOf<ImageUpload>())
            putObject(SendReviewUseCase.PARAM_LIST_DELETED_IMAGE, listOf<ImageUpload>())
        }

        every {
            sendReviewValidateUseCase.createObservable(any())
        } returns Observable.just(SendReviewValidateDomain(anyString(), anyInt(), anyInt()))

        val testSubscriber = sendReviewUseCase.createObservable(params).test()
        verify {
            sendReviewValidateUseCase.createObservable(any())
        }
        testSubscriber.assertCompleted()
        testSubscriber.assertNoErrors()
    }

    @Test
    fun `should failed when create observable`() {
        val throwable = Throwable()

        val params = RequestParams.create().apply {
            putObject(SendReviewUseCase.PARAM_LIST_IMAGE, arrayListOf<ImageUpload>())
            putObject(SendReviewUseCase.PARAM_LIST_DELETED_IMAGE, listOf<ImageUpload>())
        }

        every {
            sendReviewValidateUseCase.createObservable(any())
        } returns Observable.error(throwable)

        val testSubscriber = sendReviewUseCase.createObservable(params).test()
        verify {
            sendReviewValidateUseCase.createObservable(any())
        }
        testSubscriber.assertError(throwable)
    }
}