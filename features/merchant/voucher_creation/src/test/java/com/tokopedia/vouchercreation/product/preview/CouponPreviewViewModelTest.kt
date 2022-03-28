package com.tokopedia.vouchercreation.product.preview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
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
    lateinit var createCouponObserver: Observer<in Result<Int>>

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
        viewModel.createCoupon.observeForever(createCouponObserver)
    }

    @After
    fun tearDown() {
        viewModel.createCoupon.removeObserver(createCouponObserver)
    }

    @Test
    fun `When get coupon detail success4, should emit success to observer`() = runBlocking {
        //Given
        val isCreateMode = true
        val couponInformation = mockk<CouponInformation>()
        val couponSetting = mockk<CouponSettings>()
        val couponProduct = mockk<CouponProduct>()
        val couponProducts = listOf(couponProduct)
        val parentProductIds = listOf<Long>(2111)

        val createdCouponId = 29347923

        coEvery { createCouponUseCase.execute( isCreateMode,  couponInformation, couponSetting, couponProducts, parentProductIds) } returns createdCouponId

        //When
        viewModel.createCoupon(isCreateMode, couponInformation, couponSetting, couponProducts, parentProductIds)

        //Then
        coVerify { createCouponObserver.onChanged(Success(createdCouponId)) }
    }



}