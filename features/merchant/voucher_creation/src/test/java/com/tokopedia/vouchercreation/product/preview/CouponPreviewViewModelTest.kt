package com.tokopedia.vouchercreation.product.preview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponWithMetadata
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponFacadeUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.InitiateCouponUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.create.CreateCouponFacadeUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.update.UpdateCouponFacadeUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CouponPreviewViewModelTest {

    @MockK
    lateinit var initiateCouponUseCase: InitiateCouponUseCase

    @MockK
    lateinit var createCouponUseCase: CreateCouponFacadeUseCase

    @MockK
    lateinit var updateCouponUseCase: UpdateCouponFacadeUseCase

    @MockK
    lateinit var getCouponDetailUseCase: GetCouponFacadeUseCase

    @RelaxedMockK
    lateinit var areInputValidObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var createCouponObserver: Observer<in Result<Int>>

    @RelaxedMockK
    lateinit var updateCouponResultObserver: Observer<in Result<Boolean>>

    @RelaxedMockK
    lateinit var couponDetailObserver: Observer<in Result<CouponWithMetadata>>

    @RelaxedMockK
    lateinit var couponCreationEligibilityObserver: Observer<in Result<Int>>

    @get:Rule
    val rule = InstantTaskExecutorRule()


    private val viewModel by lazy {
        CouponPreviewViewModel(
            CoroutineTestDispatchersProvider,
            initiateCouponUseCase, createCouponUseCase, updateCouponUseCase, getCouponDetailUseCase
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel.areInputValid.observeForever(areInputValidObserver)
        viewModel.createCoupon.observeForever(createCouponObserver)
        viewModel.updateCouponResult.observeForever(updateCouponResultObserver)
        viewModel.couponDetail.observeForever(couponDetailObserver)
        viewModel.couponCreationEligibility.observeForever(couponCreationEligibilityObserver)
    }

    @After
    fun tearDown() {
        viewModel.areInputValid.removeObserver(areInputValidObserver)
        viewModel.createCoupon.removeObserver(createCouponObserver)
        viewModel.updateCouponResult.removeObserver(updateCouponResultObserver)
        viewModel.couponDetail.removeObserver(couponDetailObserver)
        viewModel.couponCreationEligibility.removeObserver(couponCreationEligibilityObserver)
    }

    @Test
    fun `When create coupon success, should emit success to observer`() = runBlocking {
        //Given
        val isCreateMode = true
        val couponInformation = mockk<CouponInformation>()
        val couponSetting = mockk<CouponSettings>()
        val couponProduct = mockk<CouponProduct>()
        val couponProducts = listOf(couponProduct)
        val parentProductIds = listOf<Long>(2111)

        val createdCouponId = 29347923

        coEvery {
            createCouponUseCase.execute(
                isCreateMode,
                couponInformation,
                couponSetting,
                couponProducts,
                parentProductIds
            )
        } returns createdCouponId

        //When
        viewModel.createCoupon(
            isCreateMode,
            couponInformation,
            couponSetting,
            couponProducts,
            parentProductIds
        )

        //Then
        coVerify { createCouponObserver.onChanged(Success(createdCouponId)) }
    }


    @Test
    fun `When create coupon error, should emit error to observer`() = runBlocking {
        //Given
        val error = MessageErrorException()
        val isCreateMode = true
        val couponInformation = mockk<CouponInformation>()
        val couponSetting = mockk<CouponSettings>()
        val couponProduct = mockk<CouponProduct>()
        val couponProducts = listOf(couponProduct)
        val parentProductIds = listOf<Long>(2111)

        coEvery {
            createCouponUseCase.execute(
                isCreateMode,
                couponInformation,
                couponSetting,
                couponProducts,
                parentProductIds
            )
        } throws error

        //When
        viewModel.createCoupon(
            isCreateMode,
            couponInformation,
            couponSetting,
            couponProducts,
            parentProductIds
        )

        //Then
        coVerify { createCouponObserver.onChanged(Fail(error)) }
    }

    @Test
    fun `When update coupon success, should emit success to observer`() = runBlocking {
        //Given
        val isUpdateSuccess = true
        val couponInformation = mockk<CouponInformation>()
        val couponSetting = mockk<CouponSettings>()
        val couponProduct = mockk<CouponProduct>()
        val couponProducts = listOf(couponProduct)
        val parentProductIds = listOf<Long>(2111)

        val couponId : Long = 29347923

        coEvery {
            updateCouponUseCase.execute(
                couponId,
                couponInformation,
                couponSetting,
                couponProducts,
                parentProductIds
            )
        } returns isUpdateSuccess

        //When
        viewModel.updateCoupon(
            couponId,
            couponInformation,
            couponSetting,
            couponProducts,
            parentProductIds
        )

        //Then
        coVerify { updateCouponResultObserver.onChanged(Success(isUpdateSuccess)) }
    }

    @Test
    fun `When update coupon error, should emit error to observer`() = runBlocking {
        //Given
        val error = MessageErrorException()
        val couponInformation = mockk<CouponInformation>()
        val couponSetting = mockk<CouponSettings>()
        val couponProduct = mockk<CouponProduct>()
        val couponProducts = listOf(couponProduct)
        val parentProductIds = listOf<Long>(2111)

        val couponId : Long = 29347923

        coEvery {
            updateCouponUseCase.execute(
                couponId,
                couponInformation,
                couponSetting,
                couponProducts,
                parentProductIds
            )
        } throws error

        //When
        viewModel.updateCoupon(
            couponId,
            couponInformation,
            couponSetting,
            couponProducts,
            parentProductIds
        )

        //Then
        coVerify { updateCouponResultObserver.onChanged(Fail(error)) }
    }
}