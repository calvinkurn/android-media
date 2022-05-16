package com.tokopedia.vouchercreation.product.preview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponDetailWithMetadata
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponWithMetadata
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponDetailFacadeUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponDetailUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponFacadeUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.InitiateCouponUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.create.CreateCouponFacadeUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.update.UpdateCouponFacadeUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.*

class CouponPreviewViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    val dummyThrowable = MessageErrorException("")

    private lateinit var mViewModel: CouponPreviewViewModel

    @RelaxedMockK
    lateinit var initiateCouponUseCase: InitiateCouponUseCase

    @RelaxedMockK
    lateinit var createCouponFacadeUseCase: CreateCouponFacadeUseCase

    @RelaxedMockK
    lateinit var updateCouponFacadeUseCase: UpdateCouponFacadeUseCase

    @RelaxedMockK
    lateinit var getCouponFacadeUseCase: GetCouponFacadeUseCase

    @RelaxedMockK
    lateinit var getCouponDetailFacadeUseCase: GetCouponDetailUseCase

    @RelaxedMockK
    lateinit var couponInformation: CouponInformation

    @RelaxedMockK
    lateinit var couponSettings: CouponSettings

    @RelaxedMockK
    lateinit var couponWithMetaDataUiModel: CouponWithMetadata

    @RelaxedMockK
    lateinit var couponDetailWithMetaDataUiModel: CouponDetailWithMetadata

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mViewModel = CouponPreviewViewModel(
            testCoroutineDispatcherProvider,
            initiateCouponUseCase,
            createCouponFacadeUseCase,
            updateCouponFacadeUseCase,
            getCouponFacadeUseCase
        )
    }

    @After
    fun cleanup() {

    }

    @Test
    fun `success create coupon`() {
        with(mViewModel) {
            coEvery {
                createCouponFacadeUseCase.execute(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns anyInt()

            createCoupon(
                true,
                couponInformation,
                couponSettings,
                listOf(),
                listOf()
            )

            coVerify {
                createCouponFacadeUseCase.execute(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            }

            assert(createCoupon.value == Success(anyInt()))
        }
    }

    @Test
    fun `fail create coupon`() {
        with(mViewModel) {

            coEvery {
                createCouponFacadeUseCase.execute(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            } throws dummyThrowable

            createCoupon(
                true,
                couponInformation,
                couponSettings,
                listOf(),
                listOf()
            )

            coVerify {
                createCouponFacadeUseCase.execute(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            }

            assert(createCoupon.value is Fail)
        }
    }

    @Test
    fun `success update coupon`() {
        with(mViewModel) {
            coEvery {
                updateCouponFacadeUseCase.execute(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns anyBoolean()

            updateCoupon(
                0,
                couponInformation,
                couponSettings,
                listOf(),
                listOf()
            )

            coVerify {
                updateCouponFacadeUseCase.execute(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            }

            assert(updateCouponResult.value == Success(anyBoolean()))
        }
    }

    @Test
    fun `fail update coupon`() {
        with(mViewModel) {

            coEvery {
                updateCouponFacadeUseCase.execute(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            } throws dummyThrowable

            updateCoupon(
                0,
                couponInformation,
                couponSettings,
                listOf(),
                listOf()
            )

            coVerify {
                updateCouponFacadeUseCase.execute(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            }

            assert(updateCouponResult.value is Fail)
        }
    }

    @Test
    fun `success get coupon detail`() {
        with(mViewModel) {
            coEvery {
                getCouponFacadeUseCase.execute(
                    any(),
                    any(),
                    any()
                )
            } returns couponWithMetaDataUiModel

            getCouponDetail(
                0,
                CouponPreviewFragment.Mode.CREATE
            )

            coVerify {
                getCouponFacadeUseCase.execute(
                    any(),
                    any(),
                    any()
                )
            }

            assert(couponDetail.value == Success(couponWithMetaDataUiModel))
        }
    }

    @Test
    fun `fail get coupon detail`() {
        with(mViewModel) {
            coEvery {
                getCouponFacadeUseCase.execute(
                    any(),
                    any(),
                    any()
                )
            } throws dummyThrowable

            getCouponDetail(
                0,
                CouponPreviewFragment.Mode.CREATE
            )

            coVerify {
                getCouponFacadeUseCase.execute(
                    any(),
                    any(),
                    any()
                )
            }

            assert(couponDetail.value is Fail)
        }
    }
}