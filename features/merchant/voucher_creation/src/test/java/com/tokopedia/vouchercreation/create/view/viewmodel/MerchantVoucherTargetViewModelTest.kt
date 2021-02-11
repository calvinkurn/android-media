package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.vouchercreation.create.data.source.VoucherTargetStaticDataSource
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.create.domain.usecase.validation.VoucherTargetValidationUseCase
import com.tokopedia.vouchercreation.create.view.enums.VoucherTargetCardType
import com.tokopedia.vouchercreation.create.view.uimodel.validation.VoucherTargetValidation
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertarget.VoucherTargetItemUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

@ExperimentalCoroutinesApi
class MerchantVoucherTargetViewModelTest {

    companion object {
        private const val DUMMY_PROMO_CODE = "PROMO"
        private const val DUMMY_PREFIX = "TEST"
        private const val DUMMY_TARGET_TYPE = VoucherTargetType.PUBLIC
    }

    @RelaxedMockK
    lateinit var voucherTargetValidationUseCase: VoucherTargetValidationUseCase

    lateinit var mViewModel: MerchantVoucherTargetViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mViewModel = MerchantVoucherTargetViewModel(CoroutineTestDispatchersProvider, voucherTargetValidationUseCase)
    }

    @Test
    fun `setting default voucher target list data will change the live data value to the data from static source`() {
        mViewModel.setDefaultVoucherTargetListData()

        assert(mViewModel.voucherTargetListData.value == VoucherTargetStaticDataSource.getVoucherTargetItemUiModelList())
    }

    @Test
    fun `setting promo code will notify user to return to initial value`() {
        mViewModel.setPromoCode(anyString(), anyString())

        assert(mViewModel.shouldReturnToInitialValue.value == false)
    }

    @Test
    fun `setting promo code will change voucher target list data`() {
        mViewModel.setPromoCode(DUMMY_PROMO_CODE, DUMMY_PREFIX)

        assert(mViewModel.voucherTargetListData.value == listOf(
                VoucherTargetItemUiModel(
                        voucherTargetType = VoucherTargetCardType.PUBLIC,
                        isEnabled = false,
                        isHavePromoCard = false),
                VoucherTargetItemUiModel(
                        voucherTargetType = VoucherTargetCardType.PRIVATE,
                        isEnabled = true,
                        isHavePromoCard = true,
                        promoCode = DUMMY_PREFIX + DUMMY_PROMO_CODE)
        ))
    }

    @Test
    fun `setting promo code will change the private promo code value`() {
        mViewModel.setPromoCode(DUMMY_PROMO_CODE, anyString())

        assert(mViewModel.privateVoucherPromoCode.value == DUMMY_PROMO_CODE)
    }

    @Test
    fun `setting active voucher target type will update the live data`() {
        mViewModel.setActiveVoucherTargetType(DUMMY_TARGET_TYPE)

        assert(mViewModel.voucherTargetTypeLiveData.value == DUMMY_TARGET_TYPE)
    }

    @Test
    fun `reloading voucher target data will change private voucher promo code`() {
        mViewModel.setReloadVoucherTargetData(DUMMY_TARGET_TYPE, DUMMY_PROMO_CODE, anyString())

        assert(mViewModel.privateVoucherPromoCode.value == DUMMY_PROMO_CODE)
    }

    @Test
    fun `reloading voucher target data will change target type live data`() {
        mViewModel.setReloadVoucherTargetData(DUMMY_TARGET_TYPE, anyString(), anyString())

        assert(mViewModel.voucherTargetTypeLiveData.value == DUMMY_TARGET_TYPE)
    }

    @Test
    fun `reloading voucher target data will should notify user to return to initial value if the voucher is private`() {
        mViewModel.setReloadVoucherTargetData(VoucherTargetType.PRIVATE, anyString(), anyString())

        assert(mViewModel.shouldReturnToInitialValue.value == true)
    }

    @Test
    fun `reloading voucher target data will change voucher target list data`() {
        mViewModel.setReloadVoucherTargetData(DUMMY_TARGET_TYPE, DUMMY_PROMO_CODE, DUMMY_PREFIX)

        assert(mViewModel.voucherTargetListData.value == listOf(
                VoucherTargetItemUiModel(
                        voucherTargetType = VoucherTargetCardType.PUBLIC,
                        isEnabled = DUMMY_TARGET_TYPE == VoucherTargetType.PUBLIC,
                        isHavePromoCard = false),
                VoucherTargetItemUiModel(
                        voucherTargetType = VoucherTargetCardType.PRIVATE,
                        isEnabled = DUMMY_TARGET_TYPE == VoucherTargetType.PRIVATE,
                        isHavePromoCard = DUMMY_TARGET_TYPE == VoucherTargetType.PRIVATE && DUMMY_PROMO_CODE.isNotEmpty(),
                        promoCode = DUMMY_PREFIX + DUMMY_PROMO_CODE)
        ))
    }

    @Test
    fun `success validate voucher target`() = runBlocking {
        val dummyVoucherTargetValidation = VoucherTargetValidation()

        coEvery {
            voucherTargetValidationUseCase.executeOnBackground()
        } returns dummyVoucherTargetValidation

        mViewModel.validateVoucherTarget(anyString(), anyString())

        coVerify {
            voucherTargetValidationUseCase.executeOnBackground()
        }

        assert(mViewModel.voucherTargetValidationLiveData.value == Success(dummyVoucherTargetValidation))
    }

    @Test
    fun `fail validate voucher target`() = runBlocking {
        val dummyThrowable = MessageErrorException("")

        coEvery {
            voucherTargetValidationUseCase.executeOnBackground()
        } throws dummyThrowable

        mViewModel.validateVoucherTarget(anyString(), anyString())

        coVerify {
            voucherTargetValidationUseCase.executeOnBackground()
        }

        assert(mViewModel.voucherTargetValidationLiveData.value is Fail)
    }

}