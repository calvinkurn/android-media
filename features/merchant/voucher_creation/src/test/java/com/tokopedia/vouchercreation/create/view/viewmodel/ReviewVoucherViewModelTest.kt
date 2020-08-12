package com.tokopedia.vouchercreation.create.view.viewmodel

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.vouchercreation.create.domain.usecase.*
import com.tokopedia.vouchercreation.create.view.uimodel.voucherreview.VoucherReviewUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import rx.Observable

@ExperimentalCoroutinesApi
class ReviewVoucherViewModelTest {

    companion object {
        private const val DUMMY_VOUCHER_BANNER = "banner"
        private const val DUMMY_VOUCHER_SQUARE = "square"
        private const val DUMMY_VOUCHER_BANNER_URL = "banner_url"
        private const val DUMMY_VOUCHER_SQUARE_URL = "square_url"
    }

    @RelaxedMockK
    lateinit var createVoucherUseCase: CreateVoucherUseCase

    @RelaxedMockK
    lateinit var updateVoucherUseCase: UpdateVoucherUseCase

    @RelaxedMockK
    lateinit var uploadVoucherUseCase: UploadVoucherUseCase

    @RelaxedMockK
    lateinit var saveBannerVoucherUseCase: SaveBannerVoucherUseCase

    @RelaxedMockK
    lateinit var saveSquareVoucherUseCase: SaveSquareVoucherUseCase

    @RelaxedMockK
    lateinit var dummyBitmap: Bitmap

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    fun cleanup() {
        testDispatcher.cleanupTestCoroutines()
    }

    private val testDispatcher by lazy {
        TestCoroutineDispatcher()
    }

    private val mViewModel by lazy {
        ReviewVoucherViewModel(testDispatcher, createVoucherUseCase, updateVoucherUseCase, uploadVoucherUseCase, saveBannerVoucherUseCase, saveSquareVoucherUseCase)
    }

    @Test
    fun `success create voucher`() = runBlocking {
        val dummyVoucherReviewUiModel = VoucherReviewUiModel()
        val dummyBannerUrl: String? = DUMMY_VOUCHER_BANNER_URL
        val dummySquareUrl: String? = DUMMY_VOUCHER_SQUARE_URL
        val dummySuccessResultCode = 1

        coEvery {
            saveBannerVoucherUseCase.executeOnBackground()
        } returns DUMMY_VOUCHER_BANNER
        coEvery {
            saveSquareVoucherUseCase.executeOnBackground()
        } returns DUMMY_VOUCHER_SQUARE
        coEvery {
            uploadVoucherUseCase.createObservable(any())
        } returns Observable.just(mutableListOf(dummyBannerUrl, dummySquareUrl))
        coEvery {
            createVoucherUseCase.executeOnBackground()
        } returns dummySuccessResultCode

        mViewModel.updateVoucher(
                dummyBitmap,
                dummyBitmap,
                dummyVoucherReviewUiModel,
                anyString(),
                anyInt())

        coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            saveBannerVoucherUseCase.executeOnBackground()
            saveSquareVoucherUseCase.executeOnBackground()
            uploadVoucherUseCase.createObservable(any())
        }
    }

}