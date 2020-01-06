package com.tokopedia.tkpd.tkpdreputation.createreputation.ui.fragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ProductRevGetForm
import com.tokopedia.tkpd.tkpdreputation.createreputation.usecase.GetProductReputationForm
import com.tokopedia.tkpd.tkpdreputation.createreputation.util.LoadingView
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewSubmitUseCase
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewUseCase
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewValidateUseCase
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewValidateDomain
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.interactor.GenerateHostUseCase
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.interactor.UploadImageUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import rx.Observable
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.schedulers.Schedulers
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 2020-01-03
 */

@ExperimentalCoroutinesApi
class CreateReviewViewModelTest {

    @RelaxedMockK
    lateinit var sendReviewValidateUseCase: SendReviewValidateUseCase

    @RelaxedMockK
    lateinit var generateHostUseCase: GenerateHostUseCase

    @RelaxedMockK
    lateinit var uploadImageUseCase: UploadImageUseCase

    @RelaxedMockK
    lateinit var sendReviewSubmitUseCase: SendReviewSubmitUseCase

    @RelaxedMockK
    lateinit var getProductReputationForm: GetProductReputationForm

    @RelaxedMockK
    lateinit var reputationRepository: ReputationRepository

    private val sendReviewWithoutImageUseCase: SendReviewValidateUseCase by lazy {
        spyk(SendReviewValidateUseCase(reputationRepository))
    }

    private val sendReviewWithImageUseCase: SendReviewUseCase by lazy {
        spyk(SendReviewUseCase(sendReviewValidateUseCase, generateHostUseCase, uploadImageUseCase, sendReviewSubmitUseCase))
    }

    private val mViewModel by lazy {
        CreateReviewViewModel(Dispatchers.Unconfined, getProductReputationForm, sendReviewWithoutImageUseCase, sendReviewWithImageUseCase)
    }

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
            override fun getMainThreadScheduler(): Scheduler {
                return Schedulers.immediate()
            }
        })
        MockKAnnotations.init(this)
    }

    @Suppress("UnstableApiUsage")
    @After
    fun afterTest() {
        RxAndroidPlugins.getInstance().reset()
    }

    @Test
    fun `should success when submit review with image`() {
        every {
            sendReviewWithImageUseCase.createObservable(any())
        } returns Observable.just(SendReviewDomain(true))

        mViewModel.submitReview(anyString(), anyString(), anyString(), anyString(), anyString(), anyFloat(), listOf(anyString()), anyBoolean())

        verify {
            sendReviewWithImageUseCase.createObservable(any())
        }

        assertTrue(mViewModel.getSubmitReviewResponse.observeAwaitValue() is com.tokopedia.tkpd.tkpdreputation.createreputation.util.Success)
    }

    @Test
    fun `should fail when submit review with image`() {
        every {
            sendReviewWithImageUseCase.createObservable(any())
        } returns Observable.error(Throwable())

        mViewModel.submitReview(anyString(), anyString(), anyString(), anyString(), anyString(), anyFloat(), listOf(anyString()), anyBoolean())

        verify {
            sendReviewWithImageUseCase.createObservable(any())
        }

        assertTrue(mViewModel.getSubmitReviewResponse.observeAwaitValue() is com.tokopedia.tkpd.tkpdreputation.createreputation.util.Fail)
    }

    @Test
    fun `should success when submit review without image`() {
        every {
            sendReviewWithoutImageUseCase.createObservable(any())
        } returns Observable.just(SendReviewValidateDomain(anyString(), anyInt(), anyInt()))

        mViewModel.submitReview(anyString(), anyString(), anyString(), anyString(), anyString(), anyFloat(), emptyList(), anyBoolean())

        verify {
            sendReviewWithoutImageUseCase.createObservable(any())
        }

        assertTrue(mViewModel.getSubmitReviewResponse.observeAwaitValue() is com.tokopedia.tkpd.tkpdreputation.createreputation.util.Success)
    }

    @Test
    fun `should fail when submit review without image`() {
        every {
            sendReviewWithoutImageUseCase.createObservable(any())
        } returns Observable.error(Throwable())

        mViewModel.submitReview(anyString(), anyString(), anyString(), anyString(), anyString(), anyFloat(), emptyList(), anyBoolean())

        verify {
            sendReviewWithoutImageUseCase.createObservable(any())
        }

        assertTrue(mViewModel.getSubmitReviewResponse.observeAwaitValue() is com.tokopedia.tkpd.tkpdreputation.createreputation.util.Fail)
    }

    @Test
    fun `should success when get product reputation form`() {
        mockkObject(GetProductReputationForm)

        coEvery {
            getProductReputationForm.getReputationForm(GetProductReputationForm.createRequestParam(anyInt(), anyInt()))
        } returns ProductRevGetForm()

        mViewModel.getProductReputation(anyInt(), anyInt())

        coVerify {
            getProductReputationForm.getReputationForm(GetProductReputationForm.createRequestParam(anyInt(), anyInt()))
        }

        assertTrue(mViewModel.getReputationDataForm.observeAwaitValue() is Success)
    }

    @Test
    fun `should fail when get product reputation form`() {
        mockkObject(GetProductReputationForm)

        coEvery {
            getProductReputationForm.getReputationForm(GetProductReputationForm.createRequestParam(anyInt(), anyInt()))
        } throws Throwable()

        mViewModel.getProductReputation(anyInt(), anyInt())

        coVerify {
            getProductReputationForm.getReputationForm(GetProductReputationForm.createRequestParam(anyInt(), anyInt()))
        }

        assertTrue(mViewModel.getReputationDataForm.observeAwaitValue() is Fail)
    }

    private fun <T> LiveData<T>.observeAwaitValue(): T? {
        var value: T? = null
        val latch = CountDownLatch(1)
        val observer = Observer<T> { t ->
            value = t
            latch.countDown()
        }
        observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)
        return value
    }
}