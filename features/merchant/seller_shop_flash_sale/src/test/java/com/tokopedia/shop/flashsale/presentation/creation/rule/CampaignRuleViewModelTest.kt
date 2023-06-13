package com.tokopedia.shop.flashsale.presentation.creation.rule

import androidx.lifecycle.MutableLiveData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.RelatedCampaign
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import com.tokopedia.shop.flashsale.util.CampaignDataGenerator
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.addTimeToSpesificDate
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.DATE
import java.util.concurrent.TimeUnit

class CampaignRuleViewModelTest: CampaignRuleViewModelTestFixture() {

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
        runBlockingTest {
            with(viewModel) {
                val campaignId: Long = 1001
                val result = CampaignDataGenerator.generateCampaignUiModel(isUniqueBuyer = true)
                val expected = Success(result)

                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns result

                getCampaignDetail(campaignId)

                val actual = campaign.getOrAwaitValue(time = 0L, timeUnit = TimeUnit.SECONDS)
                Assert.assertEquals(expected, actual)
                Assert.assertEquals(expected.data.paymentType, selectedPaymentType.value)
                Assert.assertEquals(expected.data.isUniqueBuyer, isUniqueBuyer.value)
                Assert.assertEquals(expected.data.isCampaignRuleSubmit, isTNCConfirmed.value)
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
    fun `check getCampaignRelationIds when return is list is emtpy`() {
        val method =
            viewModel.javaClass.getDeclaredMethod("getCampaignRelationIds", Long::class.java)
        method.isAccessible = true
        viewModel.onNotRequireUniqueAccountSelected()
        val result = method.invoke(viewModel, 1) as List<Long>
        assertTrue(result.isEmpty())
    }

    @Test
    fun `check getCampaignRelationIds when return is list is one and the value is id 1`() {
        val method =
            viewModel.javaClass.getDeclaredMethod("getCampaignRelationIds", Long::class.java)
        method.isAccessible = true
        viewModel.onRequireUniqueAccountSelected()
        viewModel.onAllowCampaignRelation()
        val result = method.invoke(viewModel, 1) as List<Long>
        assertFalse(result.isEmpty())
        assertEquals(1, result.get(0))
    }

    @Test
    fun `check getCampaignRelationIds when return is list is two and the value is id 1001, 1002`() {
        val method =
            viewModel.javaClass.getDeclaredMethod("getCampaignRelationIds", Long::class.java)
        method.isAccessible = true
        viewModel.onRequireUniqueAccountSelected()
        viewModel.onDisallowCampaignRelation()
        viewModel.onRelatedCampaignsChanged(CampaignDataGenerator.generateRelatedCampaigns())
        val result = method.invoke(viewModel, 1002) as List<Long>
        assertFalse(result.isEmpty())
        assertEquals(1002, result.get(1))
    }

    @Test
    fun `check getCampaignRelationIds when relatedCampaigns is null`() {
        val method =
            viewModel.javaClass.getDeclaredMethod("getCampaignRelationIds", Long::class.java)
        method.isAccessible = true
        viewModel.onRequireUniqueAccountSelected()
        viewModel.onDisallowCampaignRelation()
        val result = method.invoke(viewModel, 1002) as List<Long>
        assertTrue(result.isEmpty())
    }

    @Test
    fun `check getCampaignRelationIds when relatedCampaigns is empty list`() {
        val method =
            viewModel.javaClass.getDeclaredMethod("getCampaignRelationIds", Long::class.java)
        method.isAccessible = true
        viewModel.onRequireUniqueAccountSelected()
        viewModel.onDisallowCampaignRelation()
        viewModel.onRelatedCampaignsChanged(arrayListOf())
        val result = method.invoke(viewModel, 1002) as List<Long>
        assertFalse(result.isEmpty())
        assertEquals(1002, result.get(0))
    }

    @Test
    fun `check validateCampaignRuleInput output is false`() {
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
    fun `check validateCampaignRuleInput output is false a`() {
        runBlocking {
            val method =
                viewModel.javaClass.getDeclaredMethod("initInputValidationCollector")
            method.isAccessible = true
            method.invoke(viewModel)
            assertFalse(viewModel.isAllInputValid.getOrAwaitValue())
        }
    }

    @Test
    fun `check isRelatedCampaignValid output is false when relatedCampaignsValue is empty`() {
        runBlocking {

            val selectedPaymentType =
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<PaymentType?>>("_selectedPaymentType")
            selectedPaymentType?.value = null
            val isUniqueBuyer =
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<Boolean?>>("_isUniqueBuyer")
            isUniqueBuyer?.value = false
            val isCampaignRelation=
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<Boolean?>>("_isCampaignRelation")
            isCampaignRelation?.value = false
            val relatedCampaigns =
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<List<RelatedCampaign>?>>(
                    "_relatedCampaigns"
                )
            relatedCampaigns?.value = arrayListOf()

            val method =
                viewModel.javaClass.getDeclaredMethod("initInputValidationCollector")
            method.isAccessible = true
            method.invoke(viewModel)
            assertFalse(viewModel.isAllInputValid.getOrAwaitValue())
        }
    }

    @Test
    fun `check isRelatedCampaignValid output is false when relatedCampaignsValue is null`() {
        runBlocking {

            val selectedPaymentType=
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<PaymentType?>>("_selectedPaymentType")
            selectedPaymentType?.value = null
            val isUniqueBuyer=
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<Boolean?>>("_isUniqueBuyer")
            isUniqueBuyer?.value = false
            val isCampaignRelation=
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<Boolean?>>("_isCampaignRelation")
            isCampaignRelation?.value = false
            val relatedCampaigns =
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<List<RelatedCampaign>?>>(
                    "_relatedCampaigns"
                )
            relatedCampaigns?.value = null

            val method =
                viewModel.javaClass.getDeclaredMethod("initInputValidationCollector")
            method.isAccessible = true
            method.invoke(viewModel)
            assertFalse(viewModel.isAllInputValid.getOrAwaitValue())
        }
    }


    @Test
    fun `check isRelatedCampaignValid output is false when relatedCampaignsValue is has one value`() {
        runBlocking {

            val selectedPaymentType=
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<PaymentType?>>("_selectedPaymentType")
            selectedPaymentType?.value = null
            val isUniqueBuyer =
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<Boolean?>>("_isUniqueBuyer")
            isUniqueBuyer?.value = false
            val isCampaignRelation=
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<Boolean?>>("_isCampaignRelation")
            isCampaignRelation?.value = false
            val relatedCampaigns =
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<List<RelatedCampaign>?>>(
                    "_relatedCampaigns"
                )
            relatedCampaigns?.value = CampaignDataGenerator.generateRelatedCampaigns()

            val method =
                viewModel.javaClass.getDeclaredMethod("initInputValidationCollector")
            method.isAccessible = true
            method.invoke(viewModel)
            assertFalse(viewModel.isAllInputValid.getOrAwaitValue())
        }
    }

    @Test
    fun `check validateCampaignRuleInput output is BothSectionsInvalid`() {
        runBlocking {
            val selectedPaymentType=
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<PaymentType?>>("_selectedPaymentType")
            selectedPaymentType?.value = null
            val isUniqueBuyer=
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<Boolean?>>("_isUniqueBuyer")
            isUniqueBuyer?.value = false
            val isCampaignRelation=
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<Boolean?>>("_isCampaignRelation")
            isCampaignRelation?.value = false
            val relatedCampaigns =
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<List<RelatedCampaign>?>>(
                    "_relatedCampaigns"
                )
            relatedCampaigns?.value = arrayListOf()

            val method =
                viewModel.javaClass.getDeclaredMethod(
                    "validateCampaignRuleInput",
                    CampaignUiModel::class.java
                )
            method.isAccessible = true
            val result = method.invoke(
                viewModel,
                CampaignDataGenerator.generateCampaignUiModel(isUniqueBuyer = false)
            ) as CampaignRuleValidationResult
            assertTrue(result is CampaignRuleValidationResult.BothSectionsInvalid)
        }
    }

    @Test
    fun `check validateCampaignRuleInput output is InvalidPaymentMethod`() {
        runBlocking {
            val selectedPaymentType=
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<PaymentType?>>("_selectedPaymentType")
            selectedPaymentType?.value = null
            val isUniqueBuyer=
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<Boolean?>>("_isUniqueBuyer")
            isUniqueBuyer?.value = true
            val isCampaignRelation=
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<Boolean?>>("_isCampaignRelation")
            isCampaignRelation?.value = false
            val relatedCampaigns =
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<List<RelatedCampaign>?>>(
                    "_relatedCampaigns"
                )
            relatedCampaigns?.value = arrayListOf()

            val method =
                viewModel.javaClass.getDeclaredMethod(
                    "validateCampaignRuleInput",
                    CampaignUiModel::class.java
                )
            method.isAccessible = true
            val result = method.invoke(
                viewModel,
                CampaignDataGenerator.generateCampaignUiModel(isUniqueBuyer = false)
            ) as CampaignRuleValidationResult
            assertTrue(result is CampaignRuleValidationResult.InvalidPaymentMethod)
        }
    }

    @Test
    fun `check validateCampaignRuleInput output is TNCNotAccepted`() {
        runBlocking {
            val selectedPaymentType=
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<PaymentType?>>("_selectedPaymentType")
            selectedPaymentType?.value = PaymentType.INSTANT
            val isUniqueBuyer=
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<Boolean?>>("_isUniqueBuyer")
            isUniqueBuyer?.value = true
            val isCampaignRelation=
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<Boolean?>>("_isCampaignRelation")
            isCampaignRelation?.value = false
            val relatedCampaigns=
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<List<RelatedCampaign>?>>(
                    "_relatedCampaigns"
                )
            relatedCampaigns?.value = CampaignDataGenerator.generateRelatedCampaigns()
            val isTNCConfirmed =
                viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<Boolean>>("_isTNCConfirmed")
            isTNCConfirmed?.value = false

            val method =
                viewModel.javaClass.getDeclaredMethod(
                    "validateCampaignRuleInput",
                    CampaignUiModel::class.java
                )
            method.isAccessible = true
            val result = method.invoke(
                viewModel,
                CampaignDataGenerator.generateCampaignUiModel(isUniqueBuyer = false)
            ) as CampaignRuleValidationResult
            assertTrue(result is CampaignRuleValidationResult.TNCNotAccepted)
        }
    }

    @Test
    fun `check validateCampaignRuleInput output is InvalidCampaignTime`() {
        val selectedPaymentType=
            viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<PaymentType?>>("_selectedPaymentType")
        selectedPaymentType?.value = PaymentType.INSTANT
        val isUniqueBuyer=
            viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<Boolean?>>("_isUniqueBuyer")
        isUniqueBuyer?.value = true
        val isCampaignRelation=
            viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<Boolean?>>("_isCampaignRelation")
        isCampaignRelation?.value = false
        val relatedCampaigns=
            viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<List<RelatedCampaign>?>>(
                "_relatedCampaigns"
            )
        relatedCampaigns?.value = CampaignDataGenerator.generateRelatedCampaigns()
        val isTNCConfirmed =
            viewModel.getPrivateProperty<CampaignRuleViewModel, MutableLiveData<Boolean>>("_isTNCConfirmed")
        isTNCConfirmed?.value = true

        val input = CampaignDataGenerator.generateCampaignUiModel(isUniqueBuyer = true)
            .copy(upcomingDate = createBeforeToDay())

        val method =
            viewModel.javaClass.getDeclaredMethod(
                "validateCampaignRuleInput",
                CampaignUiModel::class.java
            )
        method.isAccessible = true
        val result = method.invoke(
            viewModel,
            input
        ) as CampaignRuleValidationResult
        assertTrue(result is CampaignRuleValidationResult.InvalidCampaignTime)
    }

    @Test
    fun `check validateCampaignRuleInput return false`() {
        val method =
            viewModel.javaClass.getDeclaredMethod(
                "validateCampaignRuleInput",
                PaymentType::class.java,
                java.lang.Boolean::class.java,
                java.lang.Boolean::class.java,
                java.util.List::class.java
            )
        method.isAccessible = true
        val result = method.invoke(
            viewModel, null, null, null, null
        ) as Boolean
        assertFalse(result)
    }

    @Test
    fun `check validateCampaignRuleInput return true`() {
        val method =
            viewModel.javaClass.getDeclaredMethod(
                "validateCampaignRuleInput",
                PaymentType::class.java,
                java.lang.Boolean::class.java,
                java.lang.Boolean::class.java,
                java.util.List::class.java
            )
        method.isAccessible = true
        val result = method.invoke(
            viewModel, PaymentType.INSTANT, true, true, CampaignDataGenerator.generateRelatedCampaigns()
        ) as Boolean
        assertTrue(result)
    }

    @Test
    fun `check validateCampaignRuleInput return false because list is empty`() {
        val method =
            viewModel.javaClass.getDeclaredMethod(
                "validateCampaignRuleInput",
                PaymentType::class.java,
                java.lang.Boolean::class.java,
                java.lang.Boolean::class.java,
                java.util.List::class.java
            )
        method.isAccessible = true
        val result = method.invoke(
            viewModel, PaymentType.INSTANT, false, false, arrayListOf<RelatedCampaign>()
        ) as Boolean
        assertFalse(result)
    }

    @Test
    fun `check validateCampaignRuleInput return false because list is null`() {
        val method =
            viewModel.javaClass.getDeclaredMethod(
                "validateCampaignRuleInput",
                PaymentType::class.java,
                java.lang.Boolean::class.java,
                java.lang.Boolean::class.java,
                java.util.List::class.java
            )
        method.isAccessible = true
        val result = method.invoke(
            viewModel, PaymentType.INSTANT, false, false, null
        ) as Boolean
        assertFalse(result)
    }

    @Test
    fun `check validateCampaignRuleInput when isUniqueBuyer is null and isCampaignRelation, relatedCampaigns is null`() {
        val method =
            viewModel.javaClass.getDeclaredMethod(
                "validateCampaignRuleInput",
                PaymentType::class.java,
                java.lang.Boolean::class.java,
                java.lang.Boolean::class.java,
                java.util.List::class.java
            )
        method.isAccessible = true
        val result = method.invoke(
            viewModel, PaymentType.INSTANT, null, null, null
        ) as Boolean
        assertFalse(result)
    }

    @Test
    fun `check validateCampaignRuleInput when isUniqueBuyer is null and isCampaignRelation but relatedCampaigns is empty`() {
        val method =
            viewModel.javaClass.getDeclaredMethod(
                "validateCampaignRuleInput",
                PaymentType::class.java,
                java.lang.Boolean::class.java,
                java.lang.Boolean::class.java,
                java.util.List::class.java
            )
        method.isAccessible = true
        val result = method.invoke(
            viewModel, PaymentType.INSTANT, null, null, emptyList<RelatedCampaign>()
        ) as Boolean
        assertFalse(result)
    }

    @Test
    fun `check validateCampaignRuleInput when isUniqueBuyer is null and isCampaignRelation but relatedCampaigns is one`() {
        val method =
            viewModel.javaClass.getDeclaredMethod(
                "validateCampaignRuleInput",
                PaymentType::class.java,
                java.lang.Boolean::class.java,
                java.lang.Boolean::class.java,
                java.util.List::class.java
            )
        method.isAccessible = true
        val result = method.invoke(
            viewModel, PaymentType.INSTANT, null, null, CampaignDataGenerator.generateRelatedCampaigns()
        ) as Boolean
        assertFalse(result)
    }

    private inline fun <reified T> T.setPrivateProperty(name: String, value: Any?) {
        T::class.java.getDeclaredField(name)
            .apply { isAccessible = true }
            .set(this, value)
    }

    inline fun <reified T : Any, R> T.getPrivateProperty(name: String): R? =
        T::class.java.getDeclaredField(name)
            .apply { isAccessible = true }
            .get(this) as R

    private fun createBeforeToDay(): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val text = "2022-01-06"
        return formatter.parse(text)
    }
}
