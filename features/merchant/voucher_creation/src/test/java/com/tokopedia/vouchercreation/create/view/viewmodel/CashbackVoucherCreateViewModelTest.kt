package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.vouchercreation.common.consts.VoucherRecommendationStatus
import com.tokopedia.vouchercreation.create.data.source.PromotionTypeUiListStaticDataSource
import com.tokopedia.vouchercreation.create.domain.model.VoucherRecommendationData
import com.tokopedia.vouchercreation.create.domain.usecase.GetVoucherRecommendationUseCase
import com.tokopedia.vouchercreation.create.domain.usecase.validation.CashbackPercentageValidationUseCase
import com.tokopedia.vouchercreation.create.domain.usecase.validation.CashbackRupiahValidationUseCase
import com.tokopedia.vouchercreation.create.view.enums.CashbackType
import com.tokopedia.vouchercreation.create.view.enums.PromotionType
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.uimodel.validation.CashbackPercentageValidation
import com.tokopedia.vouchercreation.create.view.uimodel.validation.CashbackRupiahValidation
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.CashbackPercentageInfoUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt

@ExperimentalCoroutinesApi
class CashbackVoucherCreateViewModelTest {

    companion object {
        private const val DUMMY_PERCENTAGE = 10
        private const val DUMMY_QUOTA = 10
        private const val DUMMY_MAX_VALUE = 10000
        private const val DUMMY_MIN_PURCHASE = 5000
        private const val DUMMY_ESTIMATION = DUMMY_QUOTA * DUMMY_MAX_VALUE
        private const val DUMMY_ERROR_MESSAGE = "error"
    }

    @RelaxedMockK
    lateinit var cashbackRupiahValidationUseCase: CashbackRupiahValidationUseCase

    @RelaxedMockK
    lateinit var cashbackPercentageValidationUseCase: CashbackPercentageValidationUseCase

    @RelaxedMockK
    lateinit var getVoucherRecommendationUseCase: GetVoucherRecommendationUseCase

    @RelaxedMockK
    lateinit var expenseEstimationObserver: Observer<in Int>

    @RelaxedMockK
    lateinit var cashbackPercentageInfoUiModelObserver: Observer<in CashbackPercentageInfoUiModel>

    @Suppress("UNCHECKED_CAST")
    private val idrRecommendationData: VoucherRecommendationData by lazy {
        getPrivateField(mViewModel, "idrRecommendationData") as VoucherRecommendationData
    }

    @Suppress("UNCHECKED_CAST")
    private val percentageRecommendationData: VoucherRecommendationData by lazy {
        getPrivateField(mViewModel, "percentageRecommendationData") as VoucherRecommendationData
    }

    @Suppress("UNCHECKED_CAST")
    private val mRupiahMaximumDiscountErrorPairLiveData: MutableLiveData<Pair<Boolean, String>> by lazy {
        getPrivateField(mViewModel, "mRupiahMaximumDiscountErrorPairLiveData") as MutableLiveData<Pair<Boolean, String>>
    }

    @Suppress("UNCHECKED_CAST")
    private val mRupiahMinimumPurchaseErrorPairLiveData: MutableLiveData<Pair<Boolean, String>> by lazy {
        getPrivateField(mViewModel, "mRupiahMinimumPurchaseErrorPairLiveData") as MutableLiveData<Pair<Boolean, String>>
    }

    @Suppress("UNCHECKED_CAST")
    private val mRupiahVoucherQuotaErrorPairLiveData: MutableLiveData<Pair<Boolean, String>> by lazy {
        getPrivateField(mViewModel, "mRupiahVoucherQuotaErrorPairLiveData") as MutableLiveData<Pair<Boolean, String>>
    }

    @Suppress("UNCHECKED_CAST")
    private val mPercentageDiscountAmountErrorPairLiveData: MutableLiveData<Pair<Boolean, String>> by lazy {
        getPrivateField(mViewModel, "mPercentageDiscountAmountErrorPairLiveData") as MutableLiveData<Pair<Boolean, String>>
    }

    @Suppress("UNCHECKED_CAST")
    private val mPercentageMaximumDiscountErrorPairLiveData: MutableLiveData<Pair<Boolean, String>> by lazy {
        getPrivateField(mViewModel, "mPercentageMaximumDiscountErrorPairLiveData") as MutableLiveData<Pair<Boolean, String>>
    }

    @Suppress("UNCHECKED_CAST")
    private val mPercentageMinimumPurchaseErrorPairLiveData: MutableLiveData<Pair<Boolean, String>> by lazy {
        getPrivateField(mViewModel, "mPercentageMinimumPurchaseErrorPairLiveData") as MutableLiveData<Pair<Boolean, String>>
    }

    @Suppress("UNCHECKED_CAST")
    private val mPercentageVoucherQuotaErrorPairLiveData: MutableLiveData<Pair<Boolean, String>> by lazy {
        getPrivateField(mViewModel, "mPercentageVoucherQuotaErrorPairLiveData") as MutableLiveData<Pair<Boolean, String>>
    }

    @Suppress("UNCHECKED_CAST")
    private val mRupiahMaximumDiscountLiveData: MutableLiveData<Int> by lazy {
        getPrivateField(mViewModel, "mRupiahMaximumDiscountLiveData") as MutableLiveData<Int>
    }

    @Suppress("UNCHECKED_CAST")
    private val mRupiahMinimumPurchaseLiveData: MutableLiveData<Int> by lazy {
        getPrivateField(mViewModel, "mRupiahMinimumPurchaseLiveData") as MutableLiveData<Int>
    }

    @Suppress("UNCHECKED_CAST")
    private val mRupiahVoucherQuotaLiveData: MutableLiveData<Int> by lazy {
        getPrivateField(mViewModel, "mRupiahVoucherQuotaLiveData") as MutableLiveData<Int>
    }

    @Suppress("UNCHECKED_CAST")
    private val mPercentageDiscountAmountLiveData: MutableLiveData<Int> by lazy {
        getPrivateField(mViewModel, "mPercentageDiscountAmountLiveData") as MutableLiveData<Int>
    }

    @Suppress("UNCHECKED_CAST")
    private val mPercentageMaximumDiscountLiveData: MutableLiveData<Int> by lazy {
        getPrivateField(mViewModel, "mPercentageMaximumDiscountLiveData") as MutableLiveData<Int>
    }

    @Suppress("UNCHECKED_CAST")
    private val mPercentageMinimumPurchaseLiveData: MutableLiveData<Int> by lazy {
        getPrivateField(mViewModel, "mPercentageMinimumPurchaseLiveData") as MutableLiveData<Int>
    }

    @Suppress("UNCHECKED_CAST")
    private val mPercentageVoucherQuotaLiveData: MutableLiveData<Int> by lazy {
        getPrivateField(mViewModel, "mPercentageVoucherQuotaLiveData") as MutableLiveData<Int>
    }

    @Suppress("UNCHECKED_CAST")
    private val mIdrVoucherRecommendationResult: MutableLiveData<Result<VoucherRecommendationData>> by lazy {
        getPrivateField(mViewModel, "mIdrVoucherRecommendationResult") as MutableLiveData<Result<VoucherRecommendationData>>
    }

    @Suppress("UNCHECKED_CAST")
    private val mPercentageVoucherRecommendationResult: MutableLiveData<Result<VoucherRecommendationData>> by lazy {
        getPrivateField(mViewModel, "mPercentageVoucherRecommendationResult") as MutableLiveData<Result<VoucherRecommendationData>>
    }

    @Suppress("UNCHECKED_CAST")
    private val mVoucherRecommendationStatus: MutableLiveData<Int> by lazy {
        getPrivateField(mViewModel, "mVoucherRecommendationStatus") as MutableLiveData<Int>
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var mViewModel: CashbackVoucherCreateViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mViewModel = CashbackVoucherCreateViewModel(CoroutineTestDispatchersProvider, cashbackRupiahValidationUseCase, cashbackPercentageValidationUseCase, getVoucherRecommendationUseCase)

        mViewModel.expenseEstimationLiveData.observeForever(expenseEstimationObserver)
        mViewModel.cashbackPercentageInfoUiModelLiveData.observeForever(cashbackPercentageInfoUiModelObserver)
    }

    @After
    fun cleanup() {
        mViewModel.expenseEstimationLiveData.removeObserver(expenseEstimationObserver)
        mViewModel.cashbackPercentageInfoUiModelLiveData.removeObserver(cashbackPercentageInfoUiModelObserver)
    }


    @Test
    fun `adding rupiah maximum discount text field value will change expense estimation value if quota is already set`() {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Rupiah.VoucherQuota)
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Rupiah.MaximumDiscount)

            assert(expenseEstimationLiveData.value == DUMMY_ESTIMATION)
        }
    }

    @Test
    fun `adding rupiah maximum discount text field value wont change expense estimation value if quota hasn't been set`() {
        with(mViewModel) {
            val initialValue = expenseEstimationLiveData.value
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Rupiah.MaximumDiscount)

            assert(expenseEstimationLiveData.value == initialValue)
        }
    }

    @Test
    fun `adding rupiah voucher quota text field value will change expense estimation value if maximum discount is already set`() {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Rupiah.MaximumDiscount)
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Rupiah.VoucherQuota)

            assert(expenseEstimationLiveData.value == DUMMY_ESTIMATION)
        }
    }

    @Test
    fun `adding rupiah voucher quota text field value wont change expense estimation value if maximum discount hasn't been set`() {
        with(mViewModel) {
            val initialValue = expenseEstimationLiveData.value
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Rupiah.VoucherQuota)

            assert(expenseEstimationLiveData.value == initialValue)
        }
    }

    @Test
    fun `adding percentage maximum discount text field value will change expense estimation value if quota is already set`() {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Percentage.VoucherQuota)
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Percentage.MaximumDiscount)

            assert(expenseEstimationLiveData.value == DUMMY_ESTIMATION)
        }
    }

    @Test
    fun `adding percentage maximum discount text field value wont change expense estimation value if quota hasn't been set`() {
        with(mViewModel) {
            val initialValue = expenseEstimationLiveData.value
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Percentage.MaximumDiscount)

            assert(expenseEstimationLiveData.value == initialValue)
        }
    }

    @Test
    fun `adding percentage voucher quota text field value will change expense estimation value if maximum discount is already set`() {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Percentage.MaximumDiscount)
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Percentage.VoucherQuota)

            assert(expenseEstimationLiveData.value == DUMMY_ESTIMATION)
        }
    }

    @Test
    fun `adding percentage voucher quota text field value wont change expense estimation value if maximum discount hasn't been set`() {
        with(mViewModel) {
            val initialValue = expenseEstimationLiveData.value
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Percentage.VoucherQuota)

            assert(expenseEstimationLiveData.value == initialValue)
        }
    }

    @Test
    fun `setting cashback type to Rupiah will change expense estimation value if required values are provided`() {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Rupiah.VoucherQuota)
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Rupiah.MaximumDiscount)
            changeCashbackType(CashbackType.Rupiah)

            assert(expenseEstimationLiveData.value == DUMMY_ESTIMATION)
        }
    }

    @Test
    fun `setting cashback type to Percentage will change expense estimation value if required values are provided`() {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Percentage.VoucherQuota)
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Percentage.MaximumDiscount)
            changeCashbackType(CashbackType.Percentage)

            assert(expenseEstimationLiveData.value == DUMMY_ESTIMATION)
        }
    }

    @Test
    fun `setting cashback type to Rupiah without provided value will change expense estimation value to zero`() {
        with(mViewModel) {
            changeCashbackType(CashbackType.Rupiah)

            assert(expenseEstimationLiveData.value == 0)
        }
    }

    @Test
    fun `setting cashback type to Percentage without provided value will change expense estimation value to zero`() {
        with(mViewModel) {
            changeCashbackType(CashbackType.Percentage)

            assert(expenseEstimationLiveData.value == 0)
        }
    }

    @Test
    fun `success validating cashback rupiah value`() = runBlocking {
        val successResponse = CashbackRupiahValidation()

        coEvery {
            cashbackRupiahValidationUseCase.executeOnBackground()
        } returns successResponse

        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Rupiah.MaximumDiscount)
            addTextFieldValueToCalculation(DUMMY_MIN_PURCHASE, PromotionType.Cashback.Rupiah.MinimumPurchase)
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Rupiah.VoucherQuota)

            validateCashbackRupiahValues()

            coVerify {
                cashbackRupiahValidationUseCase.executeOnBackground()
            }

            assert(rupiahValidationLiveData.value == Success(successResponse))
        }
    }

    @Test
    fun `fail validating cashback rupiah value`() = runBlocking {
        val throwable = MessageErrorException("")

        coEvery {
            cashbackRupiahValidationUseCase.executeOnBackground()
        } throws throwable

        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Rupiah.MaximumDiscount)
            addTextFieldValueToCalculation(DUMMY_MIN_PURCHASE, PromotionType.Cashback.Rupiah.MinimumPurchase)
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Rupiah.VoucherQuota)

            validateCashbackRupiahValues()

            coVerify {
                cashbackRupiahValidationUseCase.executeOnBackground()
            }

            assert(rupiahValidationLiveData.value is Fail)
        }
    }

    @Test
    fun `success validating cashback percentage value`() = runBlocking {
        val successResponse = CashbackPercentageValidation()

        coEvery {
            cashbackPercentageValidationUseCase.executeOnBackground()
        } returns successResponse

        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_PERCENTAGE, PromotionType.Cashback.Percentage.Amount)
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Percentage.MaximumDiscount)
            addTextFieldValueToCalculation(DUMMY_MIN_PURCHASE, PromotionType.Cashback.Percentage.MinimumPurchase)
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Percentage.VoucherQuota)

            validateCashbackPercentageValues()

            coVerify {
                cashbackPercentageValidationUseCase.executeOnBackground()
            }

            assert(percentageValidationLiveData.value == Success(successResponse))
        }
    }

    @Test
    fun `fail validating cashback percentage value`() = runBlocking {
        val throwable = MessageErrorException("")

        coEvery {
            cashbackPercentageValidationUseCase.executeOnBackground()
        } throws throwable

        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_PERCENTAGE, PromotionType.Cashback.Percentage.Amount)
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Percentage.MaximumDiscount)
            addTextFieldValueToCalculation(DUMMY_MIN_PURCHASE, PromotionType.Cashback.Percentage.MinimumPurchase)
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Percentage.VoucherQuota)

            validateCashbackPercentageValues()

            coVerify {
                cashbackPercentageValidationUseCase.executeOnBackground()
            }

            assert(percentageValidationLiveData.value is Fail)
        }
    }

    @Test
    fun `refreshing value twice without initially changing cashback type will change voucher image value to rupiah`() = runBlocking {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Rupiah.MaximumDiscount)
            refreshValue()
            refreshValue()

            assert((voucherImageValueLiveData.value as? VoucherImageType.Rupiah)?.value == DUMMY_MAX_VALUE)
        }
    }

    @Test
    fun `refreshing value twice with changing cashback type will change voucher image value to percentage`() = runBlocking {
        with(mViewModel) {
            changeCashbackType(CashbackType.Percentage)
            addTextFieldValueToCalculation(DUMMY_PERCENTAGE, PromotionType.Cashback.Percentage.Amount)
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Percentage.MaximumDiscount)
            refreshValue()
            refreshValue()

            val isSuccess = (voucherImageValueLiveData.value as? VoucherImageType.Percentage)?.run {
                value == DUMMY_MAX_VALUE && percentage == DUMMY_PERCENTAGE
            }

            assert(isSuccess ?: false)
        }
    }

    @Test
    fun `changing cashback type will change value list live data`() {
        with(mViewModel) {
            changeCashbackType(CashbackType.Rupiah)

            assert(rupiahValueList.value?.contentEquals(arrayOf(anyInt(), anyInt(), anyInt()))
                    ?: false)

            changeCashbackType(CashbackType.Percentage)

            assert(percentageValueList.value?.contentEquals(arrayOf(anyInt(), anyInt(), anyInt(), anyInt()))
                    ?: false)
        }
    }

    @Test
    fun `adding error pair will change error pair live data`() {
        with(mViewModel) {
            val dummyPair = Pair(true, DUMMY_ERROR_MESSAGE)
            addErrorPair(dummyPair.first, dummyPair.second, PromotionType.Cashback.Rupiah.MaximumDiscount)
            addErrorPair(dummyPair.first, dummyPair.second, PromotionType.Cashback.Rupiah.MinimumPurchase)
            addErrorPair(dummyPair.first, dummyPair.second, PromotionType.Cashback.Rupiah.VoucherQuota)
            addErrorPair(dummyPair.first, dummyPair.second, PromotionType.Cashback.Percentage.Amount)
            addErrorPair(dummyPair.first, dummyPair.second, PromotionType.Cashback.Percentage.MaximumDiscount)
            addErrorPair(dummyPair.first, dummyPair.second, PromotionType.Cashback.Percentage.MinimumPurchase)
            addErrorPair(dummyPair.first, dummyPair.second, PromotionType.Cashback.Percentage.VoucherQuota)

            changeCashbackType(CashbackType.Rupiah)

            assert(rupiahErrorPairList.value?.contentEquals(arrayOf(dummyPair, dummyPair, dummyPair))
                    ?: false)

            changeCashbackType(CashbackType.Percentage)

            assert(percentageErrorPairList.value?.contentEquals(arrayOf(dummyPair, dummyPair, dummyPair, dummyPair))
                    ?: false)
        }
    }

    @Test
    fun `changing cashback type will change its live data`() {
        with(mViewModel) {
            changeCashbackType(CashbackType.Percentage)

            assert(cashbackTypeData.value == CashbackType.Percentage)
        }
    }

    @Test
    fun `changing percentage maximum discount will change cashback percentage info ui model`() {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Percentage.MaximumDiscount)

            assert(cashbackPercentageInfoUiModelLiveData.value == CashbackPercentageInfoUiModel(
                    anyInt(),
                    anyInt(),
                    anyInt(),
                    DUMMY_MAX_VALUE
            ))
        }
    }

    @Test
    fun `when static voucher recommendation data is retrieved expect predefined values`() {
        val staticRecommendationData = mViewModel.getStaticRecommendationData()
        // asserting voucher discount amount
        assert(PromotionTypeUiListStaticDataSource.InitialValue.DISCOUNT == staticRecommendationData.voucherDiscountAmt)
        // asserting minimum purchase
        assert(PromotionTypeUiListStaticDataSource.InitialValue.MINIMUM_PURCHASE == staticRecommendationData.voucherMinimumAmt)
        // asserting voucher quota
        assert(PromotionTypeUiListStaticDataSource.InitialValue.VOUCHER_QUOTA == staticRecommendationData.voucherQuota)
    }

    @Test
    fun  `when updating idr recommendation data expect new idr recommendation data`() {
        val expectedData = VoucherRecommendationData(voucherDiscountAmt = 10000)
        mViewModel.updateVoucherRecommendation(CashbackType.Rupiah, expectedData)
        assert(idrRecommendationData == expectedData)
    }

    @Test
    fun `when updating percentage recommendation data expect new percentage recommendation data `() {
        val expectedData = VoucherRecommendationData(voucherMinimumAmt = 10000)
        mViewModel.updateVoucherRecommendation(CashbackType.Percentage, expectedData)
        assert(percentageRecommendationData == expectedData)
    }

    @Test
    fun `when updating recommendation status with idr recommendation data applied expect with recommendation status `() {
        val expectedStatus = VoucherRecommendationStatus.WITH_RECOMMENDATION
        // recommendation data
        idrRecommendationData.voucherDiscountAmtMax = 10000
        idrRecommendationData.voucherMinimumAmt = 10000
        idrRecommendationData.voucherQuota = 10000
        // field values
        mRupiahMaximumDiscountLiveData.value = 10000
        mRupiahMinimumPurchaseLiveData.value = 10000
        mRupiahVoucherQuotaLiveData.value = 10000
        mViewModel.updateRecommendationStatus(CashbackType.Rupiah)
        assert(mVoucherRecommendationStatus.value == expectedStatus)
    }

    @Test
    fun `when updating recommendation status with edited idr recommendation data applied expect edited recommendation status`() {
        val expectedStatus = VoucherRecommendationStatus.EDITED_RECOMMENDATION
        // recommendation data
        idrRecommendationData.voucherDiscountAmtMax = 10000
        idrRecommendationData.voucherMinimumAmt = 10000
        idrRecommendationData.voucherQuota = 10000
        // field values
        mRupiahMaximumDiscountLiveData.value = 10000
        mRupiahMinimumPurchaseLiveData.value = 20000
        mRupiahVoucherQuotaLiveData.value = 10000
        mViewModel.updateRecommendationStatus(CashbackType.Rupiah)
        assert(mVoucherRecommendationStatus.value == expectedStatus)
    }

    @Test
    fun `when updating recommendation status with no idr recommendation data applied expect no recommendation status`() {
        val expectedStatus = VoucherRecommendationStatus.NO_RECOMMENDATION
        // recommendation data
        idrRecommendationData.voucherDiscountAmtMax = 10000
        idrRecommendationData.voucherMinimumAmt = 10000
        idrRecommendationData.voucherQuota = 10000
        // field values
        mRupiahMaximumDiscountLiveData.value = 20000
        mRupiahMinimumPurchaseLiveData.value = 20000
        mRupiahVoucherQuotaLiveData.value = 20000
        mViewModel.updateRecommendationStatus(CashbackType.Rupiah)
        assert(mVoucherRecommendationStatus.value == expectedStatus)
    }

    @Test
    fun `when updating recommendation status with percentage recommendation data applied expect with recommendation status `() {
        val expectedStatus = VoucherRecommendationStatus.WITH_RECOMMENDATION
        // recommendation data
        percentageRecommendationData.voucherDiscountAmt = 10000
        percentageRecommendationData.voucherDiscountAmtMax = 10000
        percentageRecommendationData.voucherMinimumAmt = 10000
        percentageRecommendationData.voucherQuota = 10000
        // field values
        mPercentageDiscountAmountLiveData.value = 10000
        mPercentageMaximumDiscountLiveData.value = 10000
        mPercentageMinimumPurchaseLiveData.value = 10000
        mPercentageVoucherQuotaLiveData.value = 10000
        mViewModel.updateRecommendationStatus(CashbackType.Percentage)
        assert(mVoucherRecommendationStatus.value == expectedStatus)
    }

    @Test
    fun `when updating recommendation status with edited percentage recommendation data applied expect edited recommendation status `() {
        val expectedStatus = VoucherRecommendationStatus.EDITED_RECOMMENDATION
        // recommendation data
        percentageRecommendationData.voucherDiscountAmt = 10000
        percentageRecommendationData.voucherDiscountAmtMax = 10000
        percentageRecommendationData.voucherMinimumAmt = 10000
        percentageRecommendationData.voucherQuota = 10000
        // field values
        mPercentageDiscountAmountLiveData.value = 20000
        mPercentageMaximumDiscountLiveData.value = 20000
        mPercentageMinimumPurchaseLiveData.value = 10000
        mPercentageVoucherQuotaLiveData.value = 10000
        mViewModel.updateRecommendationStatus(CashbackType.Percentage)
        assert(mVoucherRecommendationStatus.value == expectedStatus)
    }

    @Test
    fun `when updating recommendation status with no percentage recommendation data applied expect edited recommendation status `() {
        val expectedStatus = VoucherRecommendationStatus.NO_RECOMMENDATION
        // recommendation data
        percentageRecommendationData.voucherDiscountAmt = 10000
        percentageRecommendationData.voucherDiscountAmtMax = 10000
        percentageRecommendationData.voucherMinimumAmt = 10000
        percentageRecommendationData.voucherQuota = 10000
        // field values
        mPercentageDiscountAmountLiveData.value = 20000
        mPercentageMaximumDiscountLiveData.value = 20000
        mPercentageMinimumPurchaseLiveData.value = 20000
        mPercentageVoucherQuotaLiveData.value = 20000
        mViewModel.updateRecommendationStatus(CashbackType.Percentage)
        assert(mVoucherRecommendationStatus.value == expectedStatus)
    }

    @Test
    fun `when resetting idr error fields expect no errors on idr fields`() {
        val emptyErrorPair = Pair(false, "")
        mViewModel.resetErrorPairList(CashbackType.Rupiah)
        assert(mRupiahMaximumDiscountErrorPairLiveData.value == emptyErrorPair)
        assert(mRupiahMinimumPurchaseErrorPairLiveData.value == emptyErrorPair)
        assert(mRupiahVoucherQuotaErrorPairLiveData.value == emptyErrorPair)
    }

    @Test
    fun `when resetting percentage error fields expect no errors on percentage fields`() {
        val emptyErrorPair = Pair(false, "")
        mViewModel.resetErrorPairList(CashbackType.Percentage)
        assert(mPercentageDiscountAmountErrorPairLiveData.value == emptyErrorPair)
        assert(mPercentageMaximumDiscountErrorPairLiveData.value == emptyErrorPair)
        assert(mPercentageMinimumPurchaseErrorPairLiveData.value == emptyErrorPair)
        assert(mPercentageVoucherQuotaErrorPairLiveData.value == emptyErrorPair)
    }

    @Test
    fun `when getting voucher recommendation from api expect getVoucherRecommendationUseCase to be executed`() {
        mViewModel.getVoucherRecommendationFromApi()
        coVerify {
            getVoucherRecommendationUseCase.executeOnBackground()
        }
    }

    @Test
    fun `when getVoucherRecommendationUseCase is successful expect successful results`() {
        val successResponse = VoucherRecommendationData()
        coEvery {
            getVoucherRecommendationUseCase.executeOnBackground()
        } returns successResponse
        mViewModel.getVoucherRecommendationFromApi()
        assert(mIdrVoucherRecommendationResult.value == Success(successResponse))
        assert(mPercentageVoucherRecommendationResult.value == Success(successResponse))
    }

    @Test
    fun `when getVoucherRecommendationUseCase is failed expect fail results`() {
        val throwable = MessageErrorException("")
        val failResponse = VoucherRecommendationData()
        coEvery {
            getVoucherRecommendationUseCase.executeOnBackground()
        } throws throwable
        mViewModel.getVoucherRecommendationFromApi()
        assert(mIdrVoucherRecommendationResult.value is Fail)
        assert(mPercentageVoucherRecommendationResult.value is Fail)
    }

    @Test
    fun `when getting idr recommendation data from view model expect idr recommendation data`() {
        val expectedData = VoucherRecommendationData(voucherDiscountAmt = 10000, voucherMinimumAmt = 100)
        mViewModel.updateVoucherRecommendation(CashbackType.Rupiah, expectedData)
        val actualData = mViewModel.getVoucherRecommendationData(CashbackType.Rupiah)
        assert(expectedData == actualData)
    }

    @Test
    fun `when getting percentage recommendation data from view model expect percentage recommendation data`() {
        val expectedData = VoucherRecommendationData(voucherDiscountAmt = 20000, voucherMinimumAmt = 200)
        mViewModel.updateVoucherRecommendation(CashbackType.Percentage, expectedData)
        val actualData = mViewModel.getVoucherRecommendationData(CashbackType.Percentage)
        assert(expectedData == actualData)
    }

    // helper functions

    private fun getPrivateField(owner: Any, name: String): Any? {
        return owner::class.java.getDeclaredField(name).let {
            it.isAccessible = true
            return@let it.get(owner)
        }
    }
}