package com.tokopedia.shop.flashsale.presentation.creation.rule

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.flashsale.common.tracker.ShopFlashSaleTracker
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.MerchantCampaignTNC.TncRequest
import com.tokopedia.shop.flashsale.domain.entity.RelatedCampaign
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignCreationUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignDetailUseCase
import com.tokopedia.shop.flashsale.domain.usecase.aggregate.ValidateCampaignCreationEligibilityUseCase
import com.tokopedia.shop.flashsale.util.CampaignDataGenerator
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.addTimeToSpesificDate
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Calendar.DATE
import java.util.concurrent.TimeUnit

class CampaignRuleViewModelTest {

    @RelaxedMockK
    lateinit var getSellerCampaignDetailUseCase: GetSellerCampaignDetailUseCase

    @RelaxedMockK
    lateinit var doSellerCampaignCreationUseCase: DoSellerCampaignCreationUseCase

    @RelaxedMockK
    lateinit var validateCampaignCreationEligibilityUseCase: ValidateCampaignCreationEligibilityUseCase

    @RelaxedMockK
    lateinit var tracker: ShopFlashSaleTracker

    @RelaxedMockK
    lateinit var campaignObserver: Observer<in Result<CampaignUiModel>>

    @RelaxedMockK
    lateinit var selectedPaymentTypeObserver: Observer<in PaymentType?>

    @RelaxedMockK
    lateinit var isUniqueBuyerObserver: Observer<in Boolean?>

    @RelaxedMockK
    lateinit var isCampaignRelationObserver: Observer<in Boolean?>

    @RelaxedMockK
    lateinit var relatedCampaignsObserver: Observer<in List<RelatedCampaign>?>

    @RelaxedMockK
    lateinit var isRelatedCampaignsVisibleObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var isRelatedCampaignButtonActiveObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var isRelatedCampaignRemovedObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var isAllInputValidObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var tncClickEventObserver: Observer<in TncRequest>

    @RelaxedMockK
    lateinit var isTNCConfirmedObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var isCampaignCreationAllowedObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var saveDraftActionStateObserver: Observer<in CampaignRuleActionResult>

    @RelaxedMockK
    lateinit var createCampaignActionStateObserver: Observer<in CampaignRuleActionResult>

    @RelaxedMockK
    lateinit var addRelatedCampaignButtonEventObserver: Observer<in AddRelatedCampaignRequest>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var viewModel: CampaignRuleViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = CampaignRuleViewModel(
            getSellerCampaignDetailUseCase,
            doSellerCampaignCreationUseCase,
            validateCampaignCreationEligibilityUseCase,
            tracker,
            CoroutineTestDispatchersProvider
        )

        with(viewModel) {
            campaign.observeForever(campaignObserver)
            selectedPaymentType.observeForever(selectedPaymentTypeObserver)
            isUniqueBuyer.observeForever(isUniqueBuyerObserver)
            isCampaignRelation.observeForever(isCampaignRelationObserver)
            relatedCampaigns.observeForever(relatedCampaignsObserver)
            isRelatedCampaignsVisible.observeForever(isRelatedCampaignsVisibleObserver)
            isRelatedCampaignButtonActive.observeForever(isRelatedCampaignButtonActiveObserver)
            isRelatedCampaignRemoved.observeForever(isRelatedCampaignRemovedObserver)
            isAllInputValid.observeForever(isAllInputValidObserver)
            tncClickEvent.observeForever(tncClickEventObserver)
            isTNCConfirmed.observeForever(isTNCConfirmedObserver)
            isCampaignCreationAllowed.observeForever(isCampaignCreationAllowedObserver)
            saveDraftActionState.observeForever(saveDraftActionStateObserver)
            createCampaignActionState.observeForever(createCampaignActionStateObserver)
            addRelatedCampaignButtonEvent.observeForever(addRelatedCampaignButtonEventObserver)
        }
    }

    @After
    fun tearDown() {
        with(viewModel) {
            campaign.removeObserver(campaignObserver)
            selectedPaymentType.removeObserver(selectedPaymentTypeObserver)
            isUniqueBuyer.removeObserver(isUniqueBuyerObserver)
            isCampaignRelation.removeObserver(isCampaignRelationObserver)
            relatedCampaigns.removeObserver(relatedCampaignsObserver)
            isRelatedCampaignsVisible.removeObserver(isRelatedCampaignsVisibleObserver)
            isRelatedCampaignButtonActive.removeObserver(isRelatedCampaignButtonActiveObserver)
            isRelatedCampaignRemoved.removeObserver(isRelatedCampaignRemovedObserver)
            isAllInputValid.removeObserver(isAllInputValidObserver)
            tncClickEvent.removeObserver(tncClickEventObserver)
            isTNCConfirmed.removeObserver(isTNCConfirmedObserver)
            isCampaignCreationAllowed.removeObserver(isCampaignCreationAllowedObserver)
            saveDraftActionState.removeObserver(saveDraftActionStateObserver)
            createCampaignActionState.removeObserver(createCampaignActionStateObserver)
            addRelatedCampaignButtonEvent.removeObserver(addRelatedCampaignButtonEventObserver)
        }
    }

    @Test
    fun `When fetchCampaignDetail success with no related campaigns, observer will receive the data for non-unique buyer and campaign rule submitted`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val result = CampaignDataGenerator.generateCampaignUiModel(isUniqueBuyer = false)
                val expected = Success(result)

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns result

                getCampaignDetail(campaignId)

                val actual = campaign.getOrAwaitValue(time = 0L, timeUnit = TimeUnit.SECONDS)
                assertEquals(expected, actual)
                assertEquals(expected.data.paymentType, selectedPaymentType.value)
                assertEquals(expected.data.isUniqueBuyer, isUniqueBuyer.value)
                assertEquals(expected.data.isCampaignRelation, isCampaignRelation.value)
                assertEquals(
                    expected.data.relatedCampaigns.filter { it.id != campaignId },
                    relatedCampaigns.value
                )
                assertEquals(expected.data.isCampaignRuleSubmit, isTNCConfirmed.value)
            }
        }
    }

    @Test
    fun `When fetchCampaignDetail success with related campaigns, observer will receive the data for non-unique buyer and campaign rule submitted`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val relatedCampaignId: Long = 1002
                val relatedCampaigns =
                    CampaignDataGenerator.generateRelatedCampaigns(campaignId = relatedCampaignId)
                val result = CampaignDataGenerator.generateCampaignUiModel(
                    isUniqueBuyer = false,
                    relatedCampaigns = relatedCampaigns
                )
                val expected = Success(result)

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns result

                getCampaignDetail(campaignId)

                val actual = campaign.getOrAwaitValue(time = 0L, timeUnit = TimeUnit.SECONDS)
                assertEquals(expected, actual)
                assertEquals(expected.data.paymentType, selectedPaymentType.value)
                assertEquals(expected.data.isUniqueBuyer, isUniqueBuyer.value)
                assertEquals(expected.data.isCampaignRelation, isCampaignRelation.value)
                assertEquals(
                    expected.data.relatedCampaigns.filter { it.id != campaignId },
                    this.relatedCampaigns.value
                )
                assertEquals(expected.data.isCampaignRuleSubmit, isTNCConfirmed.value)
            }
        }
    }

    @Test
    fun `When fetchCampaignDetail success, observer will receive the data for unique buyer and campaign rule submitted`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val result = CampaignDataGenerator.generateCampaignUiModel(isUniqueBuyer = true)
                val expected = Success(result)

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns result

                getCampaignDetail(campaignId)

                val actual = campaign.getOrAwaitValue(time = 0L, timeUnit = TimeUnit.SECONDS)
                assertEquals(expected, actual)
                assertEquals(expected.data.paymentType, selectedPaymentType.value)
                assertEquals(expected.data.isUniqueBuyer, isUniqueBuyer.value)
                assertEquals(expected.data.isCampaignRuleSubmit, isTNCConfirmed.value)
            }
        }
    }

    @Test
    fun `When fetchCampaignDetail success, observer will receive the data of campaign rule not submitted`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val result = CampaignDataGenerator.generateCampaignUiModel(
                    isUniqueBuyer = true,
                    isCampaignRuleSubmit = false
                )
                val expected = Success(result)

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns result

                getCampaignDetail(campaignId)

                val actual = campaign.getOrAwaitValue(time = 0L, timeUnit = TimeUnit.SECONDS)
                assertEquals(expected, actual)
                assertEquals(expected.data.isCampaignRuleSubmit, isTNCConfirmed.value)
            }
        }
    }

    @Test
    fun `When fetchCampaignDetail failed, observer will receive the error`() {
        runBlocking {
            with(viewModel) {
                val dummyThrowable = MessageErrorException("Error Occurred")
                val campaignId: Long = 1001
                val expected = Fail(dummyThrowable)

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } throws dummyThrowable

                getCampaignDetail(campaignId)

                val actual = campaign.getOrAwaitValue(time = 0L, timeUnit = TimeUnit.SECONDS)
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When reFetchCampaignDetail success, after campaignId is valid`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val result = CampaignDataGenerator.generateCampaignUiModel(
                    isUniqueBuyer = true,
                    isCampaignRuleSubmit = false
                )
                val expected = Success(result)

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns result

                getCampaignDetail(campaignId)

                reFetchCampaignDetail()

                val actual = campaign.getOrAwaitValue(time = 0L, timeUnit = TimeUnit.SECONDS)
                assertEquals(expected, actual)
                assertEquals(expected.data.isCampaignRuleSubmit, isTNCConfirmed.value)
            }
        }
    }

    @Test
    fun `When reFetchCampaignDetail success, after campaignId is invalid`() {
        runBlocking {
            with(viewModel) {
                reFetchCampaignDetail()
            }
        }
    }

    @Test
    fun `When on campaign saved in draft successfully and rules are successfully validated`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaign = CampaignDataGenerator.generateCampaignUiModel(
                    isUniqueBuyer = false,
                    upcomingDate = DateUtil.getCurrentDate().addTimeToSpesificDate(DATE, 2),
                )
                val param = CampaignDataGenerator.generateCampaignCreationParam(
                    paymentType = null,
                    isTNCConfirmed = true,
                    isUniqueBuyer = false,
                    isCampaignRelation = true,
                    campaignData = campaign,
                )
                val result = CampaignDataGenerator.generateCampaignCreationResult()
                val response = Success(result)
                val expected = CampaignRuleActionResult.Success

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns campaign

                coEvery {
                    doSellerCampaignCreationUseCase.execute(param)
                } returns result

                getCampaignDetail(campaignId)

                saveCampaignCreationDraft()

                val actual =
                    saveDraftActionState.getOrAwaitValue(time = 0L, timeUnit = TimeUnit.SECONDS)
                assert(response.data.isSuccess)
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When on campaign saved in draft is failed (isSuccess = false), but had satisfied the required rules`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaign = CampaignDataGenerator.generateCampaignUiModel(
                    isUniqueBuyer = false,
                    upcomingDate = DateUtil.getCurrentDate().addTimeToSpesificDate(DATE, 2),
                )
                val param = CampaignDataGenerator.generateCampaignCreationParam(
                    paymentType = null,
                    isTNCConfirmed = true,
                    isUniqueBuyer = false,
                    isCampaignRelation = true,
                    campaignData = campaign,
                )
                val result = CampaignDataGenerator.generateCampaignCreationResult(isSuccess = false)
                val response = Success(result)
                val expected = CampaignRuleActionResult.Fail(
                    CampaignRuleError(
                        result.errorTitle,
                        result.errorMessage
                    )
                )

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns campaign

                coEvery {
                    doSellerCampaignCreationUseCase.execute(param)
                } returns result

                getCampaignDetail(campaignId)

                saveCampaignCreationDraft()

                val actual =
                    saveDraftActionState.getOrAwaitValue(time = 0L, timeUnit = TimeUnit.SECONDS)
                assert(!response.data.isSuccess)
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When on campaign saved in draft is failed, but had satisfied the required rules`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaign = CampaignDataGenerator.generateCampaignUiModel(
                    isUniqueBuyer = false,
                    upcomingDate = DateUtil.getCurrentDate().addTimeToSpesificDate(DATE, 2),
                )
                val param = CampaignDataGenerator.generateCampaignCreationParam(
                    paymentType = null,
                    isTNCConfirmed = true,
                    isUniqueBuyer = false,
                    isCampaignRelation = true,
                    campaignData = campaign,
                )
                val dummyThrowable = MessageErrorException("Error Occurred")
                val expected =
                    CampaignRuleActionResult.Fail(CampaignRuleError(cause = dummyThrowable))

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns campaign

                coEvery {
                    doSellerCampaignCreationUseCase.execute(param)
                } throws dummyThrowable

                getCampaignDetail(campaignId)

                saveCampaignCreationDraft()

                val actual =
                    saveDraftActionState.getOrAwaitValue(time = 0L, timeUnit = TimeUnit.SECONDS)
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When on campaign saved in draft is failed on campaign not loaded`() {
        runBlocking {
            with(viewModel) {
                val dummyThrowable = MessageErrorException("Error Occurred")
                val campaignId: Long = 1001
                val expected = CampaignRuleActionResult.DetailNotLoaded

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } throws dummyThrowable

                getCampaignDetail(campaignId)

                saveCampaignCreationDraft()

                val actual =
                    saveDraftActionState.getOrAwaitValue(time = 0L, timeUnit = TimeUnit.SECONDS)
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When on campaign saved in draft is failed by validation input rule`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaign = CampaignDataGenerator.generateCampaignUiModel(
                    isCampaignRuleSubmit = false,
                    upcomingDate = DateUtil.getCurrentDate().addTimeToSpesificDate(DATE, 2),
                )
                val validationResult = CampaignRuleValidationResult.BothSectionsInvalid
                val expected = CampaignRuleActionResult.ValidationFail(validationResult)

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns campaign

                getCampaignDetail(campaignId)

                saveCampaignCreationDraft()

                val actual =
                    saveDraftActionState.getOrAwaitValue(time = 0L, timeUnit = TimeUnit.SECONDS)
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onCreateCampaignButtonClicked success, observer will receive the data`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaign = CampaignDataGenerator.generateCampaignUiModel(
                    isUniqueBuyer = false,
                    upcomingDate = DateUtil.getCurrentDate().addTimeToSpesificDate(DATE, 2),
                )
                val param = CampaignDataGenerator.generateCampaignCreationParam(
                    paymentType = null,
                    isTNCConfirmed = true,
                    isUniqueBuyer = false,
                    isCampaignRelation = true,
                    campaignData = campaign,
                )
                val creation = CampaignDataGenerator.generateCampaignCreationResult()
                val expected = CampaignRuleActionResult.Success

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns campaign

                coEvery {
                    doSellerCampaignCreationUseCase.execute(param)
                } returns creation

                getCampaignDetail(campaignId)

                onCreateCampaignButtonClicked()

                val actual = createCampaignActionState.getOrAwaitValue(
                    time = 0L,
                    timeUnit = TimeUnit.SECONDS
                )
                assert(creation.isSuccess)
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onCreateCampaignButtonClicked failed, observer will receive the data`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaign = CampaignDataGenerator.generateCampaignUiModel(
                    isUniqueBuyer = false,
                    upcomingDate = DateUtil.getCurrentDate().addTimeToSpesificDate(DATE, 2),
                )
                val param = CampaignDataGenerator.generateCampaignCreationParam(
                    paymentType = null,
                    isTNCConfirmed = true,
                    isUniqueBuyer = false,
                    isCampaignRelation = true,
                    campaignData = campaign,
                )
                val creation =
                    CampaignDataGenerator.generateCampaignCreationResult(isSuccess = false)
                val expected = CampaignRuleActionResult.Fail(
                    CampaignRuleError(
                        creation.errorTitle,
                        creation.errorMessage
                    )
                )

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns campaign

                coEvery {
                    doSellerCampaignCreationUseCase.execute(param)
                } returns creation

                getCampaignDetail(campaignId)

                onCreateCampaignButtonClicked()

                val actual = createCampaignActionState.getOrAwaitValue(
                    time = 0L,
                    timeUnit = TimeUnit.SECONDS
                )
                assert(!creation.isSuccess)
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onCreateCampaignButtonClicked is in draft, observer will receive the data`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaign = CampaignDataGenerator.generateCampaignUiModel(
                    status = CampaignStatus.DRAFT,
                    isUniqueBuyer = false,
                    upcomingDate = DateUtil.getCurrentDate().addTimeToSpesificDate(DATE, 2),
                )
                val result = CampaignDataGenerator.generateCampaignCreationEligibility()
                val response = Success(result)
                val expected = CampaignRuleActionResult.ShowConfirmation

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns campaign

                coEvery {
                    validateCampaignCreationEligibilityUseCase.execute(campaignId)
                } returns result

                getCampaignDetail(campaignId)

                onCreateCampaignButtonClicked()

                val actual = createCampaignActionState.getOrAwaitValue(
                    time = 0L,
                    timeUnit = TimeUnit.SECONDS
                )
                assert(response.data.isEligible)
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onCreateCampaignButtonClicked is in draft and is not eligible`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaign = CampaignDataGenerator.generateCampaignUiModel(
                    status = CampaignStatus.DRAFT,
                    isUniqueBuyer = false,
                    upcomingDate = DateUtil.getCurrentDate().addTimeToSpesificDate(DATE, 2),
                )
                val result =
                    CampaignDataGenerator.generateCampaignCreationEligibility(isEligible = false)
                val response = Success(result)
                val expected =
                    CampaignRuleActionResult.ValidationFail(CampaignRuleValidationResult.NotEligible)

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns campaign

                coEvery {
                    validateCampaignCreationEligibilityUseCase.execute(campaignId)
                } returns result

                getCampaignDetail(campaignId)

                onCreateCampaignButtonClicked()

                val actual = createCampaignActionState.getOrAwaitValue(
                    time = 0L,
                    timeUnit = TimeUnit.SECONDS
                )
                assert(!response.data.isEligible)
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onCreateCampaignButtonClicked is in draft and failed, observer will receive error`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaign = CampaignDataGenerator.generateCampaignUiModel(
                    status = CampaignStatus.DRAFT,
                    isUniqueBuyer = false,
                    upcomingDate = DateUtil.getCurrentDate().addTimeToSpesificDate(DATE, 2),
                )
                val dummyThrowable = MessageErrorException("Error Occurred")
                val expected =
                    CampaignRuleActionResult.Fail(CampaignRuleError(cause = dummyThrowable))

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns campaign

                coEvery {
                    validateCampaignCreationEligibilityUseCase.execute(campaignId)
                } throws dummyThrowable

                getCampaignDetail(campaignId)

                onCreateCampaignButtonClicked()

                val actual = createCampaignActionState.getOrAwaitValue(
                    time = 0L,
                    timeUnit = TimeUnit.SECONDS
                )
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onCreateCampaignConfirmed success, observer will receive the data`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaign = CampaignDataGenerator.generateCampaignUiModel(
                    isUniqueBuyer = false,
                    upcomingDate = DateUtil.getCurrentDate().addTimeToSpesificDate(DATE, 2),
                )
                val param = CampaignDataGenerator.generateCampaignCreationParam(
                    paymentType = null,
                    isTNCConfirmed = true,
                    isUniqueBuyer = false,
                    isCampaignRelation = true,
                    campaignData = campaign,
                    isUpdate = false,
                )
                val creation = CampaignDataGenerator.generateCampaignCreationResult()
                val expected = CampaignRuleActionResult.Success

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns campaign

                coEvery {
                    doSellerCampaignCreationUseCase.execute(param)
                } returns creation

                getCampaignDetail(campaignId)

                onCreateCampaignConfirmed()

                val actual = createCampaignActionState.getOrAwaitValue(
                    time = 0L,
                    timeUnit = TimeUnit.SECONDS
                )
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onCreateCampaignConfirmed failed, observer will receive the data`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaign = CampaignDataGenerator.generateCampaignUiModel(
                    isUniqueBuyer = false,
                    upcomingDate = DateUtil.getCurrentDate().addTimeToSpesificDate(DATE, 2),
                )
                val param = CampaignDataGenerator.generateCampaignCreationParam(
                    paymentType = null,
                    isTNCConfirmed = true,
                    isUniqueBuyer = false,
                    isCampaignRelation = true,
                    campaignData = campaign,
                    isUpdate = false,
                )
                val creation =
                    CampaignDataGenerator.generateCampaignCreationResult(isSuccess = false)
                val expected = CampaignRuleActionResult.Fail(
                    CampaignRuleError(
                        creation.errorTitle,
                        creation.errorMessage
                    )
                )

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns campaign

                coEvery {
                    doSellerCampaignCreationUseCase.execute(param)
                } returns creation

                getCampaignDetail(campaignId)

                onCreateCampaignConfirmed()

                val actual = createCampaignActionState.getOrAwaitValue(
                    time = 0L,
                    timeUnit = TimeUnit.SECONDS
                )
                assert(!creation.isSuccess)
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onCreateCampaignConfirmed failed on campaign not loaded, observer will receive the data`() {
        runBlocking {
            with(viewModel) {
                val dummyThrowable = MessageErrorException("Error Occurred")
                val campaignId: Long = 1001
                val expected = CampaignRuleActionResult.DetailNotLoaded

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } throws dummyThrowable

                getCampaignDetail(campaignId)

                onCreateCampaignConfirmed()

                val actual = createCampaignActionState.getOrAwaitValue(
                    time = 0L,
                    timeUnit = TimeUnit.SECONDS
                )
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onCreateCampaignConfirmed failed on validateCampaignRuleInput InvalidBuyerOptions, observer will receive the data`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaign = CampaignDataGenerator.generateCampaignUiModel(
                    isUniqueBuyer = false, isCampaignRelation = false,
                    upcomingDate = DateUtil.getCurrentDate().addTimeToSpesificDate(DATE, 2),
                )
                val validationResult = CampaignRuleValidationResult.InvalidBuyerOptions
                val expected = CampaignRuleActionResult.ValidationFail(validationResult)

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns campaign

                getCampaignDetail(campaignId)

                onCreateCampaignConfirmed()

                val actual = createCampaignActionState.getOrAwaitValue(
                    time = 0L,
                    timeUnit = TimeUnit.SECONDS
                )
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onCreateCampaignConfirmed failed on validateCampaignRuleInput BothSectionsInvalid, observer will receive the data`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaign = CampaignDataGenerator.generateCampaignUiModel(
                    isCampaignRuleSubmit = false,
                    upcomingDate = DateUtil.getCurrentDate().addTimeToSpesificDate(DATE, 2),
                )
                val validationResult = CampaignRuleValidationResult.BothSectionsInvalid
                val expected = CampaignRuleActionResult.ValidationFail(validationResult)

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns campaign

                getCampaignDetail(campaignId)

                onCreateCampaignConfirmed()

                val actual = createCampaignActionState.getOrAwaitValue(
                    time = 0L,
                    timeUnit = TimeUnit.SECONDS
                )
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onRegularPaymentMethodSelected invoked`() {
        runBlocking {
            with(viewModel) {
                val expected = PaymentType.REGULAR

                onRegularPaymentMethodSelected()

                val actual = selectedPaymentType.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onInstantPaymentMethodSelected invoked`() {
        runBlocking {
            with(viewModel) {
                val expected = PaymentType.INSTANT

                onInstantPaymentMethodSelected()

                val actual = selectedPaymentType.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onRequireUniqueAccountSelected invoked`() {
        runBlocking {
            with(viewModel) {
                val expected = false

                onRequireUniqueAccountSelected()

                val actual = isUniqueBuyer.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onNotRequireUniqueAccountSelected invoked`() {
        runBlocking {
            with(viewModel) {
                val expected = true

                onNotRequireUniqueAccountSelected()

                val actual = isUniqueBuyer.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onAllowCampaignRelation invoked`() {
        runBlocking {
            with(viewModel) {
                val expected = true

                onAllowCampaignRelation()

                val actual = isCampaignRelation.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onDisallowCampaignRelation invoked`() {
        runBlocking {
            with(viewModel) {
                val expected = false

                onDisallowCampaignRelation()

                val actual = isCampaignRelation.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onTNCCheckboxUnchecked invoked`() {
        runBlocking {
            with(viewModel) {
                val expected = false

                onTNCCheckboxUnchecked()

                val actual = isTNCConfirmed.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onTNCConfirmationClicked invoked`() {
        runBlocking {
            with(viewModel) {
                val expected = true

                onTNCConfirmationClicked()

                val actual = isTNCConfirmed.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onTNCCheckboxChecked invoked, is not unique buyer and is campaign related`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaign = CampaignDataGenerator.generateCampaignUiModel(isUniqueBuyer = false)
                val expected =
                    CampaignDataGenerator.generateMerchantCampaignTNCRequest(isUniqueBuyer = false)

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns campaign

                getCampaignDetail(campaignId)

                onTNCCheckboxChecked()

                val actual = tncClickEvent.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onTNCCheckboxChecked invoked, is unique buyer and is campaign related`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaign = CampaignDataGenerator.generateCampaignUiModel()
                val expected =
                    CampaignDataGenerator.generateMerchantCampaignTNCRequest(isCampaignRelation = false)

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns campaign

                getCampaignDetail(campaignId)

                onTNCCheckboxChecked()

                val actual = tncClickEvent.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onTNCCheckboxChecked invoked, is not unique buyer and is not campaign related`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaign = CampaignDataGenerator.generateCampaignUiModel(
                    isUniqueBuyer = false,
                    isCampaignRelation = false
                )
                val expected = CampaignDataGenerator.generateMerchantCampaignTNCRequest(
                    isUniqueBuyer = false,
                    isCampaignRelation = false
                )

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns campaign

                getCampaignDetail(campaignId)

                onTNCCheckboxChecked()

                val actual = tncClickEvent.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onTNCCheckboxChecked invoked, is unique buyer and is not campaign related`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaign =
                    CampaignDataGenerator.generateCampaignUiModel(isCampaignRelation = false)
                val expected =
                    CampaignDataGenerator.generateMerchantCampaignTNCRequest(isCampaignRelation = false)

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns campaign

                getCampaignDetail(campaignId)

                onTNCCheckboxChecked()

                val actual = tncClickEvent.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onTNCCheckboxChecked invoked, but campaign is not loaded`() {
        runBlocking {
            with(viewModel) {
                val expected = null

                onTNCCheckboxChecked()

                val actual = selectedPaymentType.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When relatedCampaigns is not empty and onRemoveRelatedCampaign invoked`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val relatedCampaignId: Long = 1002
                val relatedCampaigns =
                    CampaignDataGenerator.generateRelatedCampaigns(campaignId = relatedCampaignId)
                val expectedRelatedCampaign = emptyList<RelatedCampaign>()
                val expectedFlag = true
                val result = CampaignDataGenerator.generateCampaignUiModel(
                    isUniqueBuyer = false,
                    relatedCampaigns = relatedCampaigns
                )

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns result

                getCampaignDetail(campaignId)

                onRemoveRelatedCampaign(relatedCampaigns[0])

                val actualRelatedCampaign = this.relatedCampaigns.getOrAwaitValue()
                val actualFlag = isRelatedCampaignRemoved.getOrAwaitValue()
                assertEquals(expectedRelatedCampaign, actualRelatedCampaign)
                assertEquals(expectedFlag, actualFlag)
            }
        }
    }

    @Test
    fun `When relatedCampaigns is null and onRemoveRelatedCampaign invoked`() {
        runBlocking {
            with(viewModel) {
                val relatedCampaignId: Long = 1002
                val relatedCampaigns =
                    CampaignDataGenerator.generateRelatedCampaigns(campaignId = relatedCampaignId)
                val expectedRelatedCampaign = null
                val expectedFlag = true

                onRemoveRelatedCampaign(relatedCampaigns[0])

                val actualRelatedCampaign = this.relatedCampaigns.getOrAwaitValue()
                val actualFlag = isRelatedCampaignRemoved.getOrAwaitValue()
                assertEquals(expectedRelatedCampaign, actualRelatedCampaign)
                assertEquals(expectedFlag, actualFlag)
            }
        }
    }

    @Test
    fun `When onRelatedCampaignsChanged invoked`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val expected =
                    CampaignDataGenerator.generateRelatedCampaigns(campaignId = campaignId)

                onRelatedCampaignsChanged(expected)

                val actual = relatedCampaigns.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When relatedCampaigns is not empty onAddRelatedCampaignButtonClicked invoked`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val relatedCampaignId: Long = 1002
                val relatedCampaigns =
                    CampaignDataGenerator.generateRelatedCampaigns(campaignId = relatedCampaignId)
                val expected = AddRelatedCampaignRequest(campaignId, relatedCampaigns)
                val result = CampaignDataGenerator.generateCampaignUiModel(
                    isUniqueBuyer = false,
                    relatedCampaigns = relatedCampaigns
                )

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns result

                getCampaignDetail(campaignId)

                onAddRelatedCampaignButtonClicked()

                val actual = addRelatedCampaignButtonEvent.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When relatedCampaigns is empty onAddRelatedCampaignButtonClicked invoked`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val relatedCampaigns =
                    CampaignDataGenerator.generateRelatedCampaigns(campaignId = campaignId)
                val expected = AddRelatedCampaignRequest(campaignId, emptyList())
                val result = CampaignDataGenerator.generateCampaignUiModel(
                    isUniqueBuyer = false,
                    relatedCampaigns = relatedCampaigns
                )

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns result

                getCampaignDetail(campaignId)

                onAddRelatedCampaignButtonClicked()

                val actual = addRelatedCampaignButtonEvent.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When relatedCampaigns is null and campaignId is invalid, onAddRelatedCampaignButtonClicked invoked`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = -1
                val expected = AddRelatedCampaignRequest(campaignId, emptyList())

                onAddRelatedCampaignButtonClicked()

                val actual = addRelatedCampaignButtonEvent.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `check is isInputChanged are change and then restart the TNC Confirmation Status`() {
        runBlocking {
            val method =
                viewModel.javaClass.getDeclaredMethod("resetTNCConfirmationStatusIfDataChanged")

            method.isAccessible = true
            method.invoke(viewModel)
            assertFalse(viewModel.isTNCConfirmed.getOrAwaitValue())
        }
    }

    @Test
    fun `check isInputChanged are false and then restart the TNC Confirmation Status is not running`() {
        runBlocking {
            viewModel.setPrivateProperty("initialPaymentType", PaymentType.INSTANT)
            viewModel.setPrivateProperty("initialUniqueBuyer", true)
            viewModel.setPrivateProperty("initialCampaignRelation", true)
            viewModel.setPrivateProperty(
                "initialCampaignRelations",
                CampaignDataGenerator.generateRelatedCampaigns()
            )

            viewModel.onInstantPaymentMethodSelected()
            viewModel.onNotRequireUniqueAccountSelected()
            viewModel.onAllowCampaignRelation()
            viewModel.onRelatedCampaignsChanged(CampaignDataGenerator.generateRelatedCampaigns())
            val method =
                viewModel.javaClass.getDeclaredMethod("resetTNCConfirmationStatusIfDataChanged")

            method.isAccessible = true
            method.invoke(viewModel)
            verify(exactly = 0) {
                viewModel.onTNCCheckboxUnchecked()
            }
        }
    }
    
    @Test
    fun `check validateCampaignRuleInput output is false`(){
        runBlocking {
            viewModel.onRegularPaymentMethodSelected()
            viewModel.onNotRequireUniqueAccountSelected()
            viewModel.onAllowCampaignRelation()
            viewModel.onRelatedCampaignsChanged(CampaignDataGenerator.generateRelatedCampaigns())
            val method =
                viewModel.javaClass.getDeclaredMethod("initInputValidationCollector")
            method.isAccessible = true
            method.invoke(viewModel)
            assertTrue(viewModel.isAllInputValid.getOrAwaitValue())
        }
    }

    @Test
    fun `check validateCampaignRuleInput output is false a`(){
        runBlocking {
            val method =
                viewModel.javaClass.getDeclaredMethod("initInputValidationCollector")
            method.isAccessible = true
            launch {
                method.invoke(viewModel)
            }
            viewModel.onRegularPaymentMethodSelected()
            viewModel.onNotRequireUniqueAccountSelected()
            viewModel.onAllowCampaignRelation()
            viewModel.onRelatedCampaignsChanged(CampaignDataGenerator.generateRelatedCampaigns())

            assertFalse(viewModel.isAllInputValid.getOrAwaitValue())
        }
    }

    private inline fun <reified T> T.setPrivateProperty(name: String, value: Any?) {
        T::class.java.getDeclaredField(name)
            .apply { isAccessible = true }
            .set(this, value)
    }
}
