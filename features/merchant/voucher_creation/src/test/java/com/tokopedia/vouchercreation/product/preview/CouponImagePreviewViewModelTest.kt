package com.tokopedia.vouchercreation.product.preview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.entity.ImageRatio
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponImagePreviewFacadeUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CouponImagePreviewViewModelTest {

    @RelaxedMockK
    lateinit var getCouponImagePreviewUseCase: GetCouponImagePreviewFacadeUseCase

    @RelaxedMockK
    lateinit var couponImageObserver: Observer<in Result<ByteArray>>


    @get:Rule
    val rule = InstantTaskExecutorRule()


    private val viewModel by lazy {
        CouponImagePreviewViewModel(
            CoroutineTestDispatchersProvider,
            getCouponImagePreviewUseCase
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel.couponImage.observeForever(couponImageObserver)
    }

    @After
    fun tearDown() {
        viewModel.couponImage.removeObserver(couponImageObserver)
    }

    @Test
    fun `When create coupon success, should emit success to observer`() = runBlocking {
        //Given
        val isCreateMode = true
        val couponInformation = mockk<CouponInformation>()
        val couponSetting = mockk<CouponSettings>()
        val parentProductIds = listOf<Long>(2111)
        val expected = ByteArray(100)

        coEvery {
            getCouponImagePreviewUseCase.execute(
                isCreateMode,
                couponInformation,
                couponSetting,
                parentProductIds,
                ImageRatio.SQUARE
            )
        } returns expected

        //When
        viewModel.previewImage(
            isCreateMode,
            couponInformation,
            couponSetting,
            parentProductIds,
            ImageRatio.SQUARE
        )

        //Then
        coVerify { couponImageObserver.onChanged(Success(expected)) }
    }


    @Test
    fun `When create coupon error, should emit error to observer`() = runBlocking {
        //Given
        val error = MessageErrorException()
        val isCreateMode = true
        val couponInformation = mockk<CouponInformation>()
        val couponSetting = mockk<CouponSettings>()
        val parentProductIds = listOf<Long>(2111)

        coEvery {
            getCouponImagePreviewUseCase.execute(
                isCreateMode,
                couponInformation,
                couponSetting,
                parentProductIds,
                ImageRatio.SQUARE
            )
        } throws error

        //When
        viewModel.previewImage(
            isCreateMode,
            couponInformation,
            couponSetting,
            parentProductIds,
            ImageRatio.SQUARE
        )

        //Then
        coVerify { couponImageObserver.onChanged(Fail(error)) }
    }


}