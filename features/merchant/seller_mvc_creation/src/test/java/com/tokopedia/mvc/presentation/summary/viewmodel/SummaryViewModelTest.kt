package com.tokopedia.mvc.presentation.summary.viewmodel

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.mvc.R
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.VoucherDetailData
import com.tokopedia.mvc.domain.entity.VoucherValidationResult
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import com.tokopedia.mvc.domain.usecase.AddEditCouponFacadeUseCase
import com.tokopedia.mvc.domain.usecase.GetCouponImagePreviewFacadeUseCase
import com.tokopedia.mvc.domain.usecase.MerchantPromotionGetMVDataByIDUseCase
import com.tokopedia.mvc.domain.usecase.VoucherValidationPartialUseCase
import com.tokopedia.mvc.util.tracker.SummaryPageTracker
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SummaryViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: SummaryViewModel

    @RelaxedMockK
    internal lateinit var merchantPromotionGetMVDataByIDUseCase: MerchantPromotionGetMVDataByIDUseCase

    @RelaxedMockK
    internal lateinit var getCouponImagePreviewUseCase: GetCouponImagePreviewFacadeUseCase

    @RelaxedMockK
    internal lateinit var addEditCouponFacadeUseCase: AddEditCouponFacadeUseCase

    @RelaxedMockK
    internal lateinit var voucherValidationPartialUseCase: VoucherValidationPartialUseCase

    @RelaxedMockK
    internal lateinit var tracker: SummaryPageTracker

    @RelaxedMockK
    internal lateinit var sharedPreference: SharedPreferences

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SummaryViewModel(
            CoroutineTestDispatchersProvider,
            merchantPromotionGetMVDataByIDUseCase,
            getCouponImagePreviewUseCase,
            addEditCouponFacadeUseCase,
            voucherValidationPartialUseCase,
            tracker,
            sharedPreference
        )
    }

    @Test
    fun `When setConfiguration at Adding flow, Expect trigger correct value at livedata`() {
        with(viewModel) {
            setConfiguration(VoucherConfiguration(voucherId = 0L))
            updateProductList(listOf(SelectedProduct(123123L, emptyList())))
            assert(configuration.getOrAwaitValue().voucherId == 0L)
            assert(maxExpense.getOrAwaitValue() == 0L)
            assert(enableCouponTypeChange.getOrAwaitValue())
            assert(submitButtonText.getOrAwaitValue() == R.string.smvc_summary_page_submit_text)
            assert(products.getOrAwaitValue().isNotEmpty())
        }
    }

    @Test
    fun `When setConfiguration at Editing flow, Expect trigger correct value at livedata`() {
        with(viewModel) {
            setConfiguration(VoucherConfiguration(voucherId = 123123L))
            updateProductList(listOf(SelectedProduct(123123L, emptyList())))
            assert(configuration.getOrAwaitValue().voucherId == 123123L)
            assert(maxExpense.getOrAwaitValue() == 0L)
            assert(!enableCouponTypeChange.getOrAwaitValue())
            assert(submitButtonText.getOrAwaitValue() == R.string.smvc_save)
            assert(products.getOrAwaitValue().isNotEmpty())
        }
    }

    @Test
    fun `When setConfiguration at Duplicate flow, Expect trigger correct value at livedata`() {
        with(viewModel) {
            setConfiguration(VoucherConfiguration(voucherId = 123123L))
            setAsDuplicateCoupon()
            assert(configuration.getOrAwaitValue().voucherId == 0L)
        }
    }

    @Test
    fun `When validateTnc, Expect trigger correct value at isInputValue livedata`() {
        with(viewModel) {
            validateTnc(false)
            assert(!isInputValid.getOrAwaitValue())
        }
    }

    @Test
    fun `When checkIsAdding, Expect correct output`() {
        with(viewModel) {
            assert(checkIsAdding(VoucherConfiguration(voucherId = 0)))
            assert(!checkIsAdding(VoucherConfiguration(voucherId = 123123)))
        }
    }

    @Test
    fun `When getMaxExpenses, Expect correct output`() {
        with(viewModel) {
            assert(getMaxExpenses(VoucherConfiguration(benefitIdr = 100L, quota = 1)).isMoreThanZero())
            assert(getMaxExpenses(VoucherConfiguration(benefitType = BenefitType.PERCENTAGE, benefitMax = 100L, quota = 1)).isMoreThanZero())
        }
    }

    @Test
    fun `When setupEditMode is Success, Expect trigger changes to configuration and products livedata`() = runBlocking {
        coEvery {
            merchantPromotionGetMVDataByIDUseCase.execute(any())
        } returns VoucherDetailData(
            voucherId = 123123L,
            voucherStartTime = "2016-09-17T08:14:03+00:00",
            voucherFinishTime = "2016-10-17T08:14:03+00:00"
        )

        viewModel.setupEditMode(123123L)
        coVerify { merchantPromotionGetMVDataByIDUseCase.execute(any()) }

        val resultConfiguration = viewModel.configuration.getOrAwaitValue()
        val resultProducts = viewModel.products.getOrAwaitValue()
        assert(resultConfiguration.voucherId == 123123L)
        assert(resultProducts.isEmpty())
    }

    @Test
    fun `When setupEditMode is Error, Expect trigger changes to error livedata`() = runBlocking {
        coEvery {
            merchantPromotionGetMVDataByIDUseCase.execute(any())
        } throws MessageErrorException("error")

        viewModel.setupEditMode(123123L)
        coVerify { merchantPromotionGetMVDataByIDUseCase.execute(any()) }

        assert(viewModel.error.getOrAwaitValue().message == "error")
    }

    @Test
    fun `When previewImage is Success, Expect trigger changes to couponImage livedata`() {
        runBlocking {
            coEvery {
                getCouponImagePreviewUseCase.execute(any(), any(), any(), any())
            } returns byteArrayOf(Byte.MIN_VALUE, Byte.MAX_VALUE)

            viewModel.previewImage(VoucherConfiguration(), emptyList(), ImageRatio.HORIZONTAL)

            // await, expect value changed
            viewModel.couponImage.getOrAwaitValue()
        }
    }

    @Test
    fun `When previewImage is Error, Expect trigger changes to error livedata`() = runBlocking {
        coEvery {
            getCouponImagePreviewUseCase.execute(any(), any(), any(), any())
        } throws MessageErrorException("error")

        viewModel.previewImage(VoucherConfiguration(), emptyList(), ImageRatio.HORIZONTAL)

        assert(viewModel.error.getOrAwaitValue().message == "error")
    }

    @Test
    fun `When addCoupon is Success, Expect trigger changes to uploadCouponSuccess livedata`() {
        runBlocking {
            coEvery {
                addEditCouponFacadeUseCase.executeAdd(any(), any(), any())
            } returns 1
            viewModel.updateProductList(listOf())

            viewModel.addCoupon(VoucherConfiguration(voucherId = 123123L))

            assert(viewModel.uploadCouponSuccess.getOrAwaitValue().voucherId == 123123L)
            assert(!viewModel.isLoading.getOrAwaitValue())
        }
    }

    @Test
    fun `When addCoupon is Error, Expect trigger changes to errorUpload livedata`() = runBlocking {
        coEvery {
            addEditCouponFacadeUseCase.executeAdd(any(), any(), any())
        } throws MessageErrorException("error")
        viewModel.updateProductList(listOf())

        viewModel.addCoupon(VoucherConfiguration())

        assert(viewModel.errorUpload.getOrAwaitValue().message == "error")
    }

    @Test
    fun `When editCoupon is Success, Expect trigger changes to uploadCouponSuccess livedata`() {
        runBlocking {
            coEvery {
                addEditCouponFacadeUseCase.executeEdit(any(), any())
            } returns true
            viewModel.updateProductList(listOf())

            viewModel.editCoupon(VoucherConfiguration(voucherId = 123123L))

            assert(viewModel.uploadCouponSuccess.getOrAwaitValue().voucherId == 123123L)
            assert(!viewModel.isLoading.getOrAwaitValue())
        }
    }

    @Test
    fun `When editCoupon is Error, Expect trigger changes to errorUpload livedata`() = runBlocking {
        coEvery {
            addEditCouponFacadeUseCase.executeEdit(any(), any())
        } throws MessageErrorException("error")
        viewModel.updateProductList(listOf())

        viewModel.editCoupon(VoucherConfiguration())

        assert(viewModel.errorUpload.getOrAwaitValue().message == "error")
    }

    @Test
    fun `When saveCoupon called, Expect trigger function based on their flow`() = runBlocking {
        with(viewModel) {
            // test add flow (no voucher id)
            viewModel.setConfiguration(VoucherConfiguration())
            viewModel.configuration.getOrAwaitValue()
            saveCoupon()

            // test edit flow
            viewModel.setConfiguration(VoucherConfiguration(voucherId = 123123L))
            viewModel.configuration.getOrAwaitValue()
            saveCoupon()
        }
    }

    @Test
    fun `When handleVoucherInputValidation is Success, Expect trigger changes to uploadCouponSuccess livedata`() = runBlocking {
        coEvery {
            voucherValidationPartialUseCase.execute(any())
        } returns generateVoucherValidationResult()

        with(viewModel) {
            handleVoucherInputValidation(VoucherConfiguration())
            assert(couponPeriods.getOrAwaitValue().isNotEmpty())
        }
    }

    @Test
    fun `When handleVoucherInputValidation is Error, Expect trigger changes to error livedata`() = runBlocking {
        coEvery {
            voucherValidationPartialUseCase.execute(any())
        } throws MessageErrorException("error")

        viewModel.handleVoucherInputValidation(VoucherConfiguration())

        assert(viewModel.error.getOrAwaitValue().message == "error")
    }

    private fun generateVoucherValidationResult(): VoucherValidationResult {
        return VoucherValidationResult(
            totalAvailableQuota = 0,
            validationDate = listOf(
                VoucherValidationResult.ValidationDate(
                    dateEnd = "",
                    dateStart = "",
                    hourEnd = "",
                    hourStart = "",
                    totalLiveTime = "",
                    available = true,
                    notAvailableReason = "",
                    type = 0
                )
            ),
            validationError = VoucherValidationResult.ValidationError(
                benefitIdr = "",
                benefitMax = "",
                benefitPercent = "",
                benefitType = "",
                code = "",
                couponName = "",
                couponType = "",
                dateEnd = "",
                dateStart = "",
                hourEnd = "",
                hourStart = "",
                image = "",
                imageSquare = "",
                isPublic = "",
                minPurchase = "",
                minPurchaseType = "",
                minimumTierLevel = "",
                quota = ""
            ),
            validationProduct = listOf()
        )
    }
}
