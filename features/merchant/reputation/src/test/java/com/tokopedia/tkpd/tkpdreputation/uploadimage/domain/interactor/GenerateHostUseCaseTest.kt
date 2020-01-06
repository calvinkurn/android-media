package com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.interactor

import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.repository.ImageUploadRepository
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.model.GenerateHostDomain
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import rx.Observable

/**
 * Created By @ilhamsuaib on 2020-01-06
 */

class GenerateHostUseCaseTest {

    @RelaxedMockK
    lateinit var imageUploadRepository: ImageUploadRepository

    private val generateHostUseCase by lazy {
        GenerateHostUseCase(imageUploadRepository)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success when create observable`() {
        val params = GenerateHostUseCase.getParam()
        every {
            imageUploadRepository.generateHost(any())
        } returns Observable.just(GenerateHostDomain(anyString(), anyString()))

        val testSubscriber = generateHostUseCase.createObservable(params).test()

        verify {
            imageUploadRepository.generateHost(params)
        }

        testSubscriber.assertCompleted()
        testSubscriber.assertNoErrors()
    }

    @Test
    fun `should failed when create observable`() {
        val params = GenerateHostUseCase.getParam()
        val throwable = Throwable()
        every {
            imageUploadRepository.generateHost(any())
        } returns Observable.error(throwable)

        val testSubscriber = generateHostUseCase.createObservable(params).test()

        verify {
            imageUploadRepository.generateHost(params)
        }

        testSubscriber.assertError(throwable)
    }
}