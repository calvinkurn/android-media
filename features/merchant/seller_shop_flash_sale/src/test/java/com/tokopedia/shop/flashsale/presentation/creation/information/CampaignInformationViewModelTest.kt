package com.tokopedia.shop.flashsale.presentation.creation.information

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.extension.advanceDayBy
import com.tokopedia.shop.flashsale.common.extension.advanceHourBy
import com.tokopedia.shop.flashsale.common.extension.advanceMonthBy
import com.tokopedia.shop.flashsale.common.extension.decreaseHourBy
import com.tokopedia.shop.flashsale.common.extension.decreaseMinuteBy
import com.tokopedia.shop.flashsale.common.extension.removeTimeZone
import com.tokopedia.shop.flashsale.common.tracker.ShopFlashSaleTracker
import com.tokopedia.shop.flashsale.common.util.DateManager
import com.tokopedia.shop.flashsale.domain.entity.CampaignAction
import com.tokopedia.shop.flashsale.domain.entity.CampaignAttribute
import com.tokopedia.shop.flashsale.domain.entity.CampaignCreationResult
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.Gradient
import com.tokopedia.shop.flashsale.domain.entity.RelatedCampaign
import com.tokopedia.shop.flashsale.domain.entity.VpsPackage
import com.tokopedia.shop.flashsale.domain.entity.aggregate.CampaignWithVpsPackages
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignCreationUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignAttributeUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignDetailUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignPackageListUseCase
import com.tokopedia.shop.flashsale.presentation.creation.information.uimodel.VpsPackageUiModel
import com.tokopedia.shop.flashsale.presentation.creation.information.viewmodel.CampaignInformationViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong
import java.util.*

class CampaignInformationViewModelTest {
    @RelaxedMockK
    lateinit var emptyVpsPackageObserver: Observer<in Result<VpsPackageUiModel>>

    @RelaxedMockK
    lateinit var doSellerCampaignCreationUseCase: DoSellerCampaignCreationUseCase

    @RelaxedMockK
    lateinit var getSellerCampaignDetailUseCase: GetSellerCampaignDetailUseCase

    @RelaxedMockK
    lateinit var getSellerCampaignAttributeUseCase: GetSellerCampaignAttributeUseCase

    @RelaxedMockK
    lateinit var getSellerCampaignPackageListUseCase: GetSellerCampaignPackageListUseCase

    @RelaxedMockK
    lateinit var dateManager: DateManager

    @RelaxedMockK
    lateinit var tracker: ShopFlashSaleTracker

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel by lazy {
        CampaignInformationViewModel(
            CoroutineTestDispatchersProvider,
            doSellerCampaignCreationUseCase,
            getSellerCampaignDetailUseCase,
            getSellerCampaignAttributeUseCase,
            getSellerCampaignPackageListUseCase,
            dateManager,
            tracker
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel.emptyQuotaVpsPackage.observeForever(emptyVpsPackageObserver)
    }

    //region validateCampaignName
    @Test
    fun `When campaign name is empty, should return CampaignNameIsEmpty error`() =
        runBlocking {
            // Given
            val campaignName = ""
            val expected = CampaignInformationViewModel.CampaignNameValidationResult.CampaignNameIsEmpty

            // When
            val actual = viewModel.validateCampaignName(campaignName)

            // Then
            assertEquals(expected, actual)
        }

    @Test
    fun `When campaign name length is below minimum, should return CampaignNameBelowMinCharacter`() =
        runBlocking {
            // Given
            val campaignName = "cam"
            val expected = CampaignInformationViewModel.CampaignNameValidationResult.CampaignNameBelowMinCharacter

            // When
            val actual = viewModel.validateCampaignName(campaignName)

            // Then
            assertEquals(expected, actual)
        }

    @Test
    fun `When campaign name has forbidden words, should return CampaignNameHasForbiddenWords`() =
        runBlocking {
            // Given
            val campaignName = "lazada"
            val expected = CampaignInformationViewModel.CampaignNameValidationResult.CampaignNameHasForbiddenWords

            // When
            val actual = viewModel.validateCampaignName(campaignName)

            // Then
            assertEquals(expected, actual)
        }

    @Test
    fun `When campaign name valid, should return valid`() =
        runBlocking {
            // Given
            val campaignName = "Adidas Sale"
            val expected = CampaignInformationViewModel.CampaignNameValidationResult.Valid

            // When
            val actual = viewModel.validateCampaignName(campaignName)

            // Then
            assertEquals(expected, actual)
        }

    //endregion
    //region validateInput
    @Test
    fun `When page mode is create and remaining quota is empty, should return NoRemainingQuota`() =
        runBlocking {
            // Given
            val mode = PageMode.CREATE
            val remainingQuota = 0
            val selection = CampaignInformationViewModel.Selection(
                "Adidas Sale",
                Date(),
                Date(),
                showTeaser = true,
                Date(),
                firstColor = "#00FF00",
                secondColor = "#00FF00",
                PaymentType.INSTANT,
                remainingQuota = remainingQuota,
                vpsPackageId = 101,
                isOosImprovement = true
            )
            val now = Date()
            val expected = CampaignInformationViewModel.ValidationResult.NoRemainingQuota

            // When
            viewModel.validateInput(mode, selection, now)
            val actual = viewModel.areInputValid.getOrAwaitValue()

            // Then
            assertEquals(expected, actual)
        }

    @Test
    fun `When page mode is update and has remaining quota, should return Valid`() =
        runBlocking {
            // Given
            val mode = PageMode.UPDATE
            val remainingQuota = 5
            val selection = CampaignInformationViewModel.Selection(
                "Adidas Sale",
                Date(),
                Date(),
                showTeaser = true,
                Date(),
                firstColor = "#00FF00",
                secondColor = "#00FF00",
                PaymentType.INSTANT,
                remainingQuota = remainingQuota,
                vpsPackageId = 101,
                isOosImprovement = true
            )
            val now = Date()
            val expected = CampaignInformationViewModel.ValidationResult.Valid

            // When
            viewModel.validateInput(mode, selection, now)
            val actual = viewModel.areInputValid.getOrAwaitValue()

            // Then
            assertEquals(expected, actual)
        }

    @Test
    fun `When current date is after selected start date, should return LapsedStartDate`() =
        runBlocking {
            // Given
            val mode = PageMode.CREATE
            val remainingQuota = 5
            val advancedNow = Date().advanceHourBy(hour = 2)
            val selectedStartDate = Date()
            val selection = CampaignInformationViewModel.Selection(
                "Adidas Sale",
                selectedStartDate,
                Date(),
                showTeaser = true,
                Date(),
                firstColor = "#00FF00",
                secondColor = "#00FF00",
                PaymentType.INSTANT,
                remainingQuota = remainingQuota,
                vpsPackageId = 101,
                isOosImprovement = true
            )

            val expected = CampaignInformationViewModel.ValidationResult.LapsedStartDate

            // When
            viewModel.validateInput(mode, selection, advancedNow)
            val actual = viewModel.areInputValid.getOrAwaitValue()

            // Then
            assertEquals(expected, actual)
        }

    @Test
    fun `When show teaser option is selected and current date is after selected teaser date, should return LapsedTeaserStartDate`() =
        runBlocking {
            // Given
            val mode = PageMode.CREATE
            val remainingQuota = 5
            val advancedNow = Date().advanceHourBy(1)
            val tomorrow = Date().advanceDayBy(days = 1)

            val selection = CampaignInformationViewModel.Selection(
                "Adidas Sale",
                startDate = tomorrow,
                endDate = Date(),
                showTeaser = true,
                teaserDate = Date(),
                firstColor = "#00FF00",
                secondColor = "#00FF00",
                PaymentType.INSTANT,
                remainingQuota = remainingQuota,
                vpsPackageId = 101,
                isOosImprovement = true
            )

            val expected = CampaignInformationViewModel.ValidationResult.LapsedTeaserStartDate

            // When
            viewModel.validateInput(mode, selection, advancedNow)
            val actual = viewModel.areInputValid.getOrAwaitValue()

            // Then
            assertEquals(expected, actual)
        }

    @Test
    fun `When show teaser option is not selected and current date is after selected teaser date, should return Valid`() =
        runBlocking {
            // Given
            val mode = PageMode.CREATE
            val remainingQuota = 5
            val advancedNow = Date().advanceHourBy(1)
            val tomorrow = Date().advanceDayBy(days = 1)

            val selection = CampaignInformationViewModel.Selection(
                "Adidas Sale",
                startDate = tomorrow,
                endDate = Date(),
                showTeaser = false,
                teaserDate = Date(),
                firstColor = "#00FF00",
                secondColor = "#00FF00",
                PaymentType.INSTANT,
                remainingQuota = remainingQuota,
                vpsPackageId = 101,
                isOosImprovement = true
            )

            val expected = CampaignInformationViewModel.ValidationResult.Valid

            // When
            viewModel.validateInput(mode, selection, advancedNow)
            val actual = viewModel.areInputValid.getOrAwaitValue()

            // Then
            assertEquals(expected, actual)
        }

    @Test
    fun `When first color length is below minimum length, should return InvalidHexColor`() =
        runBlocking {
            // Given
            val mode = PageMode.CREATE
            val remainingQuota = 5
            val advancedNow = Date().advanceHourBy(1)
            val tomorrow = Date().advanceDayBy(days = 1)

            val selection = CampaignInformationViewModel.Selection(
                "Adidas Sale",
                startDate = tomorrow,
                endDate = Date(),
                showTeaser = false,
                teaserDate = Date(),
                firstColor = "#FF",
                secondColor = "#00FF00",
                PaymentType.INSTANT,
                remainingQuota = remainingQuota,
                vpsPackageId = 101,
                isOosImprovement = true
            )

            val expected = CampaignInformationViewModel.ValidationResult.InvalidHexColor

            // When
            viewModel.validateInput(mode, selection, advancedNow)
            val actual = viewModel.areInputValid.getOrAwaitValue()

            // Then
            assertEquals(expected, actual)
        }

    @Test
    fun `When second color length is below minimum length, should return InvalidHexColor`() =
        runBlocking {
            // Given
            val mode = PageMode.CREATE
            val remainingQuota = 5
            val advancedNow = Date().advanceHourBy(1)
            val tomorrow = Date().advanceDayBy(days = 1)

            val selection = CampaignInformationViewModel.Selection(
                "Adidas Sale",
                startDate = tomorrow,
                endDate = Date(),
                showTeaser = false,
                teaserDate = Date(),
                firstColor = "#00FF00",
                secondColor = "#FF",
                PaymentType.INSTANT,
                remainingQuota = remainingQuota,
                vpsPackageId = 101,
                isOosImprovement = true
            )

            val expected = CampaignInformationViewModel.ValidationResult.InvalidHexColor

            // When
            viewModel.validateInput(mode, selection, advancedNow)
            val actual = viewModel.areInputValid.getOrAwaitValue()

            // Then
            assertEquals(expected, actual)
        }
    //endregion

    //region getCampaignDetail
    @Test
    fun `When get campaign detail success, observer should successfully receive the data`() =
        runBlocking {
            // Given
            val campaignId: Long = 1001

            val campaign = buildCampaignUiModel(campaignId)
            val vpsPackages = listOf<VpsPackage>()

            val expected = Success(CampaignWithVpsPackages(campaign, listOf()))

            coEvery { getSellerCampaignDetailUseCase.execute(campaignId) } returns campaign
            coEvery { getSellerCampaignPackageListUseCase.execute() } returns vpsPackages

            // When
            viewModel.getCampaignDetail(campaignId)

            // Then
            val actual = viewModel.campaignDetail.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    @Test
    fun `When get campaign detail error, observer should receive error result`() =
        runBlocking {
            // Given
            val campaignId: Long = 1001
            val error = MessageErrorException("Server error")
            val expected = Fail(error)

            coEvery { getSellerCampaignDetailUseCase.execute(campaignId) } throws error

            // When
            viewModel.getCampaignDetail(campaignId)

            // Then
            val actual = viewModel.campaignDetail.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    //endregion

    //region saveDraft

    @Test
    fun `When seller turned off show teaser, show teaser on save campaign draft param should be turned off`() = runBlocking {
        // Given
        val campaignId: Long = 1001

        val selection = buildSelectionObject().copy(showTeaser = false)
        val createCampaignParam = DoSellerCampaignCreationUseCase.Param(
            CampaignAction.Create,
            selection.campaignName,
            selection.startDate,
            selection.endDate,
            selection.teaserDate,
            showTeaser = selection.showTeaser,
            firstColor = selection.firstColor,
            secondColor = selection.secondColor,
            paymentType = selection.paymentType,
            packageId = 101,
            isOosImprovement = selection.isOosImprovement
        )

        // When
        viewModel.saveDraft(PageMode.UPDATE, campaignId, selection)

        // Then
        assertEquals(selection.showTeaser, createCampaignParam.showTeaser)
    }

    @Test
    fun `When seller turned on show teaser, show teaser on save campaign draft param should be turned on`() = runBlocking {
        // Given
        val campaignId: Long = 1001

        val selection = buildSelectionObject().copy(showTeaser = true)
        val createCampaignParam = DoSellerCampaignCreationUseCase.Param(
            CampaignAction.Create,
            selection.campaignName,
            selection.startDate,
            selection.endDate,
            selection.teaserDate,
            showTeaser = selection.showTeaser,
            firstColor = selection.firstColor,
            secondColor = selection.secondColor,
            paymentType = selection.paymentType,
            packageId = 1,
            isOosImprovement = selection.isOosImprovement
        )

        // When
        viewModel.saveDraft(PageMode.UPDATE, campaignId, selection)

        // Then
        assertEquals(selection.showTeaser, createCampaignParam.showTeaser)
    }

    @Test
    fun `When save draft success, observer should successfully receive the data`() =
        runBlocking {
            // Given
            val campaignId: Long = 1001
            val campaignCreationResult = CampaignCreationResult(
                campaignId = campaignId,
                isSuccess = true,
                totalProductFailed = 0,
                errorDescription = "",
                errorTitle = "",
                errorMessage = ""
            )
            val selection = buildSelectionObject()
            val createCampaignParam = DoSellerCampaignCreationUseCase.Param(
                CampaignAction.Create,
                selection.campaignName,
                selection.startDate,
                selection.endDate,
                selection.teaserDate,
                showTeaser = selection.showTeaser,
                firstColor = selection.firstColor,
                secondColor = selection.secondColor,
                paymentType = selection.paymentType,
                packageId = 1,
                isOosImprovement = selection.isOosImprovement
            )
            val expected = Success(campaignCreationResult)

            coEvery { doSellerCampaignCreationUseCase.execute(createCampaignParam) } returns campaignCreationResult

            // When
            viewModel.saveDraft(PageMode.CREATE, campaignId, selection)

            // Then
            val actual = viewModel.saveDraft.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    @Test
    fun `When save draft error, observer should receive error result`() =
        runBlocking {
            // Given
            val campaignId: Long = 1001
            val error = MessageErrorException("Server error")
            val expected = Fail(error)

            val selection = buildSelectionObject()
            val createCampaignParam = DoSellerCampaignCreationUseCase.Param(
                CampaignAction.Create,
                selection.campaignName,
                selection.startDate,
                selection.endDate,
                selection.teaserDate,
                showTeaser = selection.showTeaser,
                firstColor = selection.firstColor,
                secondColor = selection.secondColor,
                paymentType = selection.paymentType,
                packageId = 1,
                isOosImprovement = selection.isOosImprovement
            )

            coEvery { doSellerCampaignCreationUseCase.execute(createCampaignParam) } throws error
            // When
            viewModel.saveDraft(PageMode.CREATE, campaignId, selection)

            // Then
            val actual = viewModel.saveDraft.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    @Test
    fun `When save campaign draft after previously get campaign detail success, should get correct values`() {
        // Given
        val campaignId: Long = 1001
        val campaignRuleSubmit = true
        val relatedCampaign = listOf(RelatedCampaign(1, "Shoes Flash Sale"))
        val relatedCampaignId = listOf<Long>(1)
        val campaign = buildCampaignUiModel(campaignId).copy(relatedCampaigns = relatedCampaign, isCampaignRuleSubmit = campaignRuleSubmit)
        val selection = buildSelectionObject()
        val params = DoSellerCampaignCreationUseCase.Param(
            CampaignAction.Create,
            selection.campaignName,
            selection.startDate,
            selection.endDate,
            selection.teaserDate,
            showTeaser = selection.showTeaser,
            firstColor = selection.firstColor,
            secondColor = selection.secondColor,
            paymentType = selection.paymentType,
            campaignRelation = relatedCampaignId,
            isCampaignRuleSubmit = campaignRuleSubmit,
            packageId = selection.vpsPackageId,
            isOosImprovement = selection.isOosImprovement
        )

        val vpsPackages = listOf<VpsPackage>()

        coEvery { getSellerCampaignDetailUseCase.execute(campaignId) } returns campaign
        coEvery { getSellerCampaignPackageListUseCase.execute() } returns vpsPackages
        coEvery { getSellerCampaignDetailUseCase.execute(campaignId) } returns campaign

        // When
        viewModel.getCampaignDetail(campaignId)
        viewModel.saveDraft(PageMode.CREATE, campaignId, selection)

        // Then
        coVerify { doSellerCampaignCreationUseCase.execute(params) }
    }
    //endregion

    //region getCurrentMonthRemainingQuota
    @Test
    fun `When get current month remaining quota success, observer should successfully receive the data`() =
        runBlocking {
            // Given
            val remainingQuota = 5
            val campaignAttribute = CampaignAttribute(
                success = true,
                errorMessage = "",
                listOf(),
                remainingCampaignQuota = remainingQuota
            )
            val expected = Success(remainingQuota)

            coEvery {
                getSellerCampaignAttributeUseCase.execute(
                    month = anyInt(),
                    year = anyInt(),
                    vpsPackageId = anyLong()
                )
            } returns campaignAttribute

            // When
            viewModel.getCurrentMonthRemainingQuota()

            // Then
            val actual = viewModel.currentMonthRemainingQuota.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    @Test
    fun `When get current month remaining quota error, observer should receive error result`() =
        runBlocking {
            // Given
            val error = MessageErrorException("Server error")
            val expected = Fail(error)

            coEvery {
                getSellerCampaignAttributeUseCase.execute(
                    month = anyInt(),
                    year = anyInt(),
                    vpsPackageId = anyLong()
                )
            } throws error

            // When
            viewModel.getCurrentMonthRemainingQuota()

            // Then
            val actual = viewModel.currentMonthRemainingQuota.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    //endregion

    //region submit
    @Test
    fun `When campaign id is not created yet, should execute create campaign function`() = runBlocking {
        // Given
        val campaignIdNotCreated: Long = -1
        val selection = buildSelectionObject()
        val createCampaignParam = DoSellerCampaignCreationUseCase.Param(
            CampaignAction.Create,
            selection.campaignName,
            selection.startDate,
            selection.endDate,
            selection.teaserDate,
            showTeaser = selection.showTeaser,
            firstColor = selection.firstColor,
            secondColor = selection.secondColor,
            paymentType = selection.paymentType,
            packageId = 1,
            isOosImprovement = selection.isOosImprovement
        )

        // When
        viewModel.setCampaignId(campaignIdNotCreated)
        viewModel.submit(selection)

        // Then
        coVerify { doSellerCampaignCreationUseCase.execute(createCampaignParam) }
    }

    @Test
    fun `When already got campaign id, should execute update campaign function`() = runBlocking {
        // Given
        val campaignId: Long = 1001
        val selection = buildSelectionObject()
        val updateCampaignParam = DoSellerCampaignCreationUseCase.Param(
            CampaignAction.Update(campaignId),
            selection.campaignName,
            selection.startDate,
            selection.endDate,
            selection.teaserDate,
            showTeaser = selection.showTeaser,
            firstColor = selection.firstColor,
            secondColor = selection.secondColor,
            paymentType = selection.paymentType,
            packageId = 1,
            isOosImprovement = selection.isOosImprovement
        )

        // When
        viewModel.setCampaignId(campaignId)
        viewModel.submit(selection)

        // Then
        coVerify { doSellerCampaignCreationUseCase.execute(updateCampaignParam) }
    }
    //endregion

    //region createCampaign
    @Test
    fun `When seller turned off show teaser, show teaser on create campaign param should be turned off`() = runBlocking {
        // Given
        val campaignIdNotCreated: Long = -1

        val selection = buildSelectionObject().copy(showTeaser = false)
        val createCampaignParam = DoSellerCampaignCreationUseCase.Param(
            CampaignAction.Create,
            selection.campaignName,
            selection.startDate,
            selection.endDate,
            selection.teaserDate,
            showTeaser = selection.showTeaser,
            firstColor = selection.firstColor,
            secondColor = selection.secondColor,
            paymentType = selection.paymentType,
            packageId = 1,
            isOosImprovement = selection.isOosImprovement
        )

        // When
        viewModel.setCampaignId(campaignIdNotCreated)
        viewModel.submit(selection)

        // Then
        assertEquals(selection.showTeaser, createCampaignParam.showTeaser)
    }

    @Test
    fun `When seller turned on show teaser, show teaser on create campaign param should be turned on`() = runBlocking {
        // Given
        val campaignIdNotCreated: Long = -1

        val selection = buildSelectionObject().copy(showTeaser = true)
        val createCampaignParam = DoSellerCampaignCreationUseCase.Param(
            CampaignAction.Create,
            selection.campaignName,
            selection.startDate,
            selection.endDate,
            selection.teaserDate,
            showTeaser = selection.showTeaser,
            firstColor = selection.firstColor,
            secondColor = selection.secondColor,
            paymentType = selection.paymentType,
            packageId = 1,
            isOosImprovement = selection.isOosImprovement
        )

        // When
        viewModel.setCampaignId(campaignIdNotCreated)
        viewModel.submit(selection)

        // Then
        assertEquals(selection.showTeaser, createCampaignParam.showTeaser)
    }

    @Test
    fun `When create campaign success, should correctly store the campaignId`() = runBlocking {
        // Given
        val campaignIdNotCreated: Long = -1
        val remoteCampaignId: Long = 100
        val response = CampaignCreationResult(
            campaignId = remoteCampaignId,
            isSuccess = true,
            totalProductFailed = 0,
            errorDescription = "",
            errorTitle = "",
            errorMessage = ""
        )
        val selection = buildSelectionObject().copy(showTeaser = true)
        val createCampaignParam = DoSellerCampaignCreationUseCase.Param(
            CampaignAction.Create,
            selection.campaignName,
            selection.startDate,
            selection.endDate,
            selection.teaserDate,
            showTeaser = selection.showTeaser,
            firstColor = selection.firstColor,
            secondColor = selection.secondColor,
            paymentType = selection.paymentType,
            packageId = 1,
            isOosImprovement = selection.isOosImprovement
        )

        coEvery { doSellerCampaignCreationUseCase.execute(createCampaignParam) } returns response

        // When
        viewModel.setCampaignId(campaignIdNotCreated)
        viewModel.submit(selection)

        // Then
        coVerify { tracker.sendClickButtonProceedOnCampaignInfoPageEvent(remoteCampaignId, selection.vpsPackageId) }
        assertEquals(remoteCampaignId, viewModel.getCampaignId())
    }

    @Test
    fun `When create campaign error, campaignId value should stay -1`() = runBlocking {
        // Given
        val campaignIdNotCreated: Long = -1

        val response = CampaignCreationResult(
            campaignId = campaignIdNotCreated,
            isSuccess = false,
            totalProductFailed = 0,
            errorDescription = "",
            errorTitle = "",
            errorMessage = ""
        )
        val selection = buildSelectionObject().copy(showTeaser = true)
        val createCampaignParam = DoSellerCampaignCreationUseCase.Param(
            CampaignAction.Create,
            selection.campaignName,
            selection.startDate,
            selection.endDate,
            selection.teaserDate,
            showTeaser = selection.showTeaser,
            firstColor = selection.firstColor,
            secondColor = selection.secondColor,
            paymentType = selection.paymentType,
            packageId = 1,
            isOosImprovement = selection.isOosImprovement
        )

        coEvery { doSellerCampaignCreationUseCase.execute(createCampaignParam) } returns response

        // When
        viewModel.setCampaignId(campaignIdNotCreated)
        viewModel.submit(selection)

        // Then
        coVerify { tracker.sendClickButtonProceedOnCampaignInfoPageEvent(campaignIdNotCreated, selection.vpsPackageId) }
        assertEquals(campaignIdNotCreated, viewModel.getCampaignId())
    }

    @Test
    fun `When create campaign error, observer should receive error`() = runBlocking {
        // Given
        val campaignIdNotCreated: Long = -1
        val error = MessageErrorException("Server error")
        val expected = Fail(error)

        val selection = buildSelectionObject()
        val createCampaignParam = DoSellerCampaignCreationUseCase.Param(
            CampaignAction.Create,
            selection.campaignName,
            selection.startDate,
            selection.endDate,
            selection.teaserDate,
            showTeaser = selection.showTeaser,
            firstColor = selection.firstColor,
            secondColor = selection.secondColor,
            paymentType = selection.paymentType,
            packageId = 1,
            isOosImprovement = selection.isOosImprovement
        )

        coEvery { doSellerCampaignCreationUseCase.execute(createCampaignParam) } throws error

        // When
        viewModel.setCampaignId(campaignIdNotCreated)
        viewModel.submit(selection)

        // Then
        val actual = viewModel.campaignCreation.getOrAwaitValue()
        assertEquals(expected, actual)
    }
    //endregion

    //region updateCampaign
    @Test
    fun `When seller turned off show teaser, show teaser on update campaign param should be turned off`() = runBlocking {
        // Given
        val campaignId: Long = 1001

        val selection = buildSelectionObject().copy(showTeaser = false)
        val createCampaignParam = DoSellerCampaignCreationUseCase.Param(
            CampaignAction.Create,
            selection.campaignName,
            selection.startDate,
            selection.endDate,
            selection.teaserDate,
            showTeaser = selection.showTeaser,
            firstColor = selection.firstColor,
            secondColor = selection.secondColor,
            paymentType = selection.paymentType,
            packageId = 1,
            isOosImprovement = selection.isOosImprovement
        )

        // When
        viewModel.setCampaignId(campaignId)
        viewModel.submit(selection)

        // Then
        assertEquals(selection.showTeaser, createCampaignParam.showTeaser)
    }

    @Test
    fun `When seller turned on show teaser, show teaser on update campaign param should be turned on`() = runBlocking {
        // Given
        val campaignId: Long = 1001

        val selection = buildSelectionObject().copy(showTeaser = true)
        val createCampaignParam = DoSellerCampaignCreationUseCase.Param(
            CampaignAction.Create,
            selection.campaignName,
            selection.startDate,
            selection.endDate,
            selection.teaserDate,
            showTeaser = selection.showTeaser,
            firstColor = selection.firstColor,
            secondColor = selection.secondColor,
            paymentType = selection.paymentType,
            packageId = 1,
            isOosImprovement = selection.isOosImprovement
        )

        // When
        viewModel.setCampaignId(campaignId)
        viewModel.submit(selection)

        // Then
        assertEquals(selection.showTeaser, createCampaignParam.showTeaser)
    }

    @Test
    fun `When update campaign success, observer should successfully receive the data`() = runBlocking {
        // Given
        val campaignId: Long = 1001
        val campaignCreationResult = CampaignCreationResult(
            campaignId = campaignId,
            isSuccess = true,
            totalProductFailed = 0,
            errorDescription = "",
            errorTitle = "",
            errorMessage = ""
        )
        val selection = buildSelectionObject()
        val updateCampaignParam = DoSellerCampaignCreationUseCase.Param(
            CampaignAction.Update(campaignId),
            selection.campaignName,
            selection.startDate,
            selection.endDate,
            selection.teaserDate,
            showTeaser = selection.showTeaser,
            firstColor = selection.firstColor,
            secondColor = selection.secondColor,
            paymentType = selection.paymentType,
            packageId = 1,
            isOosImprovement = selection.isOosImprovement
        )
        val expected = Success(campaignCreationResult)

        coEvery { doSellerCampaignCreationUseCase.execute(updateCampaignParam) } returns campaignCreationResult

        // When
        viewModel.setCampaignId(campaignId)
        viewModel.submit(selection)

        // Then
        val actual = viewModel.campaignUpdate.getOrAwaitValue()
        assertEquals(expected, actual)
    }

    @Test
    fun `When update campaign error, observer should receive error`() = runBlocking {
        // Given
        val campaignId: Long = 1001
        val error = MessageErrorException("Server error")
        val expected = Fail(error)

        val selection = buildSelectionObject()
        val createCampaignParam = DoSellerCampaignCreationUseCase.Param(
            CampaignAction.Update(campaignId),
            selection.campaignName,
            selection.startDate,
            selection.endDate,
            selection.teaserDate,
            showTeaser = selection.showTeaser,
            firstColor = selection.firstColor,
            secondColor = selection.secondColor,
            paymentType = selection.paymentType,
            packageId = 1,
            isOosImprovement = selection.isOosImprovement
        )

        coEvery { doSellerCampaignCreationUseCase.execute(createCampaignParam) } throws error

        // When
        viewModel.setCampaignId(campaignId)
        viewModel.submit(selection)

        // Then
        val actual = viewModel.campaignUpdate.getOrAwaitValue()
        assertEquals(expected, actual)
    }

    @Test
    fun `When update campaign and previous campaign campaignRuleSubmit is false, should pass the correct params to remote`() {
        // Given
        val campaignId: Long = 1001
        val campaignRuleSubmit = false
        val relatedCampaign = listOf(RelatedCampaign(1, "Shoes Flash Sale"))
        val relatedCampaignId = listOf<Long>(1)
        val campaign = buildCampaignUiModel(campaignId).copy(relatedCampaigns = relatedCampaign, isCampaignRuleSubmit = campaignRuleSubmit)
        val selection = buildSelectionObject()
        val params = DoSellerCampaignCreationUseCase.Param(
            CampaignAction.Update(campaignId),
            selection.campaignName,
            selection.startDate,
            selection.endDate,
            selection.teaserDate,
            showTeaser = selection.showTeaser,
            firstColor = selection.firstColor,
            secondColor = selection.secondColor,
            paymentType = selection.paymentType,
            campaignRelation = relatedCampaignId,
            isCampaignRuleSubmit = campaignRuleSubmit,
            packageId = selection.vpsPackageId,
            isOosImprovement = selection.isOosImprovement
        )
        val vpsPackages = listOf<VpsPackage>()

        coEvery { getSellerCampaignPackageListUseCase.execute() } returns vpsPackages
        coEvery { getSellerCampaignDetailUseCase.execute(campaignId) } returns campaign

        // When
        viewModel.setCampaignId(campaignId)
        viewModel.getCampaignDetail(campaignId)
        viewModel.submit(selection)

        // Then
        coVerify { doSellerCampaignCreationUseCase.execute(params) }
    }

    @Test
    fun `When update campaign and previous campaign campaignRuleSubmit is true, should pass the correct params to remote`() {
        // Given
        val campaignId: Long = 1001
        val campaignRuleSubmit = true
        val relatedCampaign = listOf(RelatedCampaign(1, "Shoes Flash Sale"))
        val relatedCampaignId = listOf<Long>(1)
        val campaign = buildCampaignUiModel(campaignId).copy(relatedCampaigns = relatedCampaign, isCampaignRuleSubmit = campaignRuleSubmit)
        val selection = buildSelectionObject()
        val params = DoSellerCampaignCreationUseCase.Param(
            CampaignAction.Update(campaignId),
            selection.campaignName,
            selection.startDate,
            selection.endDate,
            selection.teaserDate,
            showTeaser = selection.showTeaser,
            firstColor = selection.firstColor,
            secondColor = selection.secondColor,
            paymentType = selection.paymentType,
            campaignRelation = relatedCampaignId,
            isCampaignRuleSubmit = campaignRuleSubmit,
            packageId = selection.vpsPackageId,
            isOosImprovement = selection.isOosImprovement
        )
        val vpsPackages = listOf<VpsPackage>()

        coEvery { getSellerCampaignPackageListUseCase.execute() } returns vpsPackages
        coEvery { getSellerCampaignDetailUseCase.execute(campaignId) } returns campaign

        // When
        viewModel.setCampaignId(campaignId)
        viewModel.getCampaignDetail(campaignId)
        viewModel.submit(selection)

        // Then
        coVerify { doSellerCampaignCreationUseCase.execute(params) }
    }
    //endregion

    //region markColorAsSelected

    @Test
    fun `When select a color, other color should be unselected`() {
        // Given
        val selectedColor = Gradient("#000000", "#e0e0ea", isSelected = false)
        val availableColors = listOf(
            Gradient("#000000", "#e0e0ea", isSelected = false),
            Gradient("#ffffff", "#e0e0e0", isSelected = false)
        )

        val expected = listOf(
            Gradient("#000000", "#e0e0ea", isSelected = true),
            Gradient("#ffffff", "#e0e0e0", isSelected = false)
        )

        // When
        val actual = viewModel.markColorAsSelected(selectedColor, availableColors)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When select a color but first color is different, color should be marked as unselected`() {
        // Given
        val selectedColor = Gradient("#000000", "#e0e0ea", isSelected = false)
        val availableColors = listOf(
            Gradient("#00000f", "#e0e0ea", isSelected = false)
        )

        val expected = listOf(
            Gradient("#00000f", "#e0e0ea", isSelected = false)
        )

        // When
        val actual = viewModel.markColorAsSelected(selectedColor, availableColors)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When select a color but second color is different, color should be marked as unselected`() {
        // Given
        val selectedColor = Gradient("#00000f", "#e0e0ea", isSelected = false)
        val availableColors = listOf(
            Gradient("#00000f", "#e0e0ef", isSelected = false)
        )

        val expected = listOf(
            Gradient("#00000f", "#e0e0ef", isSelected = false)
        )

        // When
        val actual = viewModel.markColorAsSelected(selectedColor, availableColors)

        // Then
        assertEquals(expected, actual)
    }

    //endregion

    //region deselectAllColor
    @Test
    fun `When deselect all colors, all color should be unselected`() {
        // Given
        val firstColor = Gradient("#000000", "#e0e0e0", isSelected = true)
        val secondColor = Gradient("#ffffff", "#e0e0e0", isSelected = false)
        val availableColors = listOf(firstColor, secondColor)
        val expected = listOf(firstColor.copy(isSelected = false), secondColor.copy(isSelected = false))

        // When
        val actual = viewModel.deselectAllColor(availableColors)

        // Then
        assertEquals(expected, actual)
    }
    //endregion

    //region normalizeEndDate

    @Test
    fun `When end date before start date, should return start date`() {
        // Given
        val startDate = Date()
        val endDate = Date().decreaseHourBy(desiredHourToBeDecreased = 1)

        // When
        val actual = viewModel.normalizeEndDate(startDate, endDate)

        // Then
        assertEquals(startDate, actual)
    }

    @Test
    fun `When end date after start date, should return end date`() {
        // Given
        val startDate = Date()
        val endDate = Date().advanceDayBy(days = 1)

        // When
        val actual = viewModel.normalizeEndDate(startDate, endDate)

        // Then
        assertEquals(endDate, actual)
    }
    //endregion

    //region getTeaserQuantityEditorMaxValue
    @Test
    fun `When get teaser max value, should return correct values`() {
        val startDate = Date().advanceHourBy(hour = 4)
        val now = Date()
        val actual = viewModel.getTeaserQuantityEditorMaxValue(startDate, now)
        assertEquals(3, actual)
    }

    @Test
    fun `When get teaser max value and difference is bigger than 24 hours, should return 24`() {
        val startDate = Date().advanceDayBy(days = 2)
        val now = Date()
        val actual = viewModel.getTeaserQuantityEditorMaxValue(startDate, now)
        assertEquals(24, actual)
    }

    @Test
    fun `When get teaser max value and difference is less than 0 hours, should return 0`() {
        val startDate = Date().decreaseHourBy(desiredHourToBeDecreased = 24)
        val now = Date()
        val actual = viewModel.getTeaserQuantityEditorMaxValue(startDate, now)
        assertEquals(0, actual)
    }

    @Test
    fun `When get teaser max value and difference is 0 hours, should return 0`() {
        val startDate = Date().decreaseHourBy(desiredHourToBeDecreased = 1)
        val now = Date()
        val actual = viewModel.getTeaserQuantityEditorMaxValue(startDate, now)
        assertEquals(0, actual)
    }
    //endregion

    //region isUsingHexColor
    @Test
    fun `When first color and second color are the same, should return true`() {
        val actual = viewModel.isUsingHexColor("#0f0f0f", "#0f0f0f")
        assertEquals(true, actual)
    }

    @Test
    fun `When first color and second color is different, should return false`() {
        val actual = viewModel.isUsingHexColor("#0f0f0a", "#0f0f0f")
        assertEquals(false, actual)
    }

    //endregion

    //region findUpcomingTimeDifferenceInHour
    @Test
    fun `When find hour difference from two date, should return correct values`() {
        // Given
        val startDate = Date().decreaseHourBy(desiredHourToBeDecreased = 2)
        val upcomingDate = Date()
        val expected = -2

        // When
        val actual = viewModel.findUpcomingTimeDifferenceInHour(startDate, upcomingDate)

        // Then
        assertEquals(expected, actual)
    }
    //endregion

    //region isDataChanged
    @Test
    fun `When both data is the same, should return false`() {
        // Given
        val previousData = buildSelectionObject()
        val currentData = previousData

        // When
        val actual = viewModel.isDataChanged(previousData, currentData)

        // Then
        assertEquals(false, actual)
    }

    @Test
    fun `When both data is different, should return true`() {
        // Given
        val previousData = buildSelectionObject()
        val currentData = previousData.copy(remainingQuota = 20)

        // When
        val actual = viewModel.isDataChanged(previousData, currentData)

        // Then
        assertEquals(true, actual)
    }
    //endregion

    //region Getter and Setters
    @Test
    fun `When get payment type, should return correct values`() {
        val paymentType = PaymentType.INSTANT
        viewModel.setPaymentType(paymentType)
        assertEquals(paymentType, viewModel.getPaymentType())
    }

    @Test
    fun `When get selected color, should return correct values`() {
        val color = Gradient("#FFFFFF", "#E0E0E0", isSelected = true)
        viewModel.setSelectedColor(color)
        assertEquals(color, viewModel.getColor())
    }

    @Test
    fun `When get remaining quota, should return correct values`() {
        val remainingQuota = 2
        viewModel.setRemainingQuota(remainingQuota)
        assertEquals(remainingQuota, viewModel.getRemainingQuota())
    }

    @Test
    fun `When get selected start date, should return correct values`() {
        val startDate = Date()
        viewModel.setSelectedStartDate(startDate)
        assertEquals(startDate, viewModel.getSelectedStartDate())
    }

    @Test
    fun `When get selected end date, should return correct values`() {
        val endDate = Date()
        viewModel.setSelectedEndDate(endDate)
        assertEquals(endDate, viewModel.getSelectedEndDate())
    }

    @Test
    fun `When get default selection, should return correct values`() {
        val selection = buildSelectionObject()
        viewModel.storeAsDefaultSelection(selection)
        assertEquals(selection, viewModel.getDefaultSelection())
    }
    //endregion

    //region getCampaignQuotaOfSelectedMonth
    @Test
    fun `When get campaign quota success, observer should successfully receive the data`() =
        runBlocking {
            // Given
            val vpsPackageId: Long = 101

            val remainingQuota = 5
            val campaignAttribute = CampaignAttribute(
                success = true,
                errorMessage = "",
                listOf(),
                remainingCampaignQuota = remainingQuota
            )
            val expected = Success(remainingQuota)

            coEvery { getSellerCampaignAttributeUseCase.execute(month = 8, year = 2022, vpsPackageId = vpsPackageId) } returns campaignAttribute

            // When
            viewModel.getCampaignQuotaOfSelectedMonth(month = 8, year = 2022, vpsPackageId = vpsPackageId)

            // Then
            val actual = viewModel.campaignQuota.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    @Test
    fun `When get campaign quota error, observer should receive error result`() =
        runBlocking {
            // Given
            val vpsPackageId: Long = 101
            val error = MessageErrorException("Server error")
            val expected = Fail(error)

            coEvery { getSellerCampaignAttributeUseCase.execute(month = 8, year = 2022, vpsPackageId = vpsPackageId) } throws error

            // When
            viewModel.getCampaignQuotaOfSelectedMonth(month = 8, year = 2022, vpsPackageId = vpsPackageId)

            // Then
            val actual = viewModel.campaignQuota.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    //endregion

    //region getVpsPackages
    @Test
    fun `When got no shop tier benefit on the vps package, isShopTierBenefit value should be false`() =
        runBlocking {
            // Given
            val selectedVpsPackageId: Long = 1
            val now = GregorianCalendar(2020, 8, 1, 7, 0, 0).time

            val packageStartTime = now
            val packageEndTime = now.advanceDayBy(days = 2)

            val packageStartTimeEpoch = (packageStartTime.time / 1000)
            val packageEndTimeEpoch = (packageEndTime.time / 1000)

            val packageStartTimeDate = packageStartTime.removeTimeZone()
            val packageEndTimeDate = packageEndTime.removeTimeZone()

            val vpsPackageId: Long = 101

            val response = listOf(
                VpsPackage(
                    packageId = selectedVpsPackageId.toString(),
                    remainingQuota = 45,
                    currentQuota = 5,
                    isDisabled = false,
                    originalQuota = 50,
                    packageEndTime = packageEndTimeEpoch,
                    packageName = "Shop Tier Benefit",
                    packageStartTime = packageStartTimeEpoch
                )
            )
            val vpsPackages = listOf(
                VpsPackageUiModel(
                    packageId = selectedVpsPackageId,
                    remainingQuota = 45,
                    currentQuota = 5,
                    originalQuota = 50,
                    packageEndTime = packageEndTimeDate,
                    packageName = "Shop Tier Benefit",
                    packageStartTime = packageStartTimeDate,
                    isSelected = false,
                    disabled = false,
                    isShopTierBenefit = false
                )
            )
            val expected = Success(vpsPackages)

            coEvery { getSellerCampaignPackageListUseCase.execute() } returns response

            // When
            viewModel.getVpsPackages(vpsPackageId)

            // Then
            val actual = viewModel.vpsPackages.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    @Test
    fun `When got shop tier benefit on the vps package, isShopTierBenefit value should be true`() =
        runBlocking {
            // Given
            val now = GregorianCalendar(2020, 8, 1, 7, 0, 0).time

            val packageStartTime = now
            val packageEndTime = now.advanceDayBy(days = 2)

            val packageStartTimeEpoch = (packageStartTime.time / 1000)
            val packageEndTimeEpoch = (packageEndTime.time / 1000)

            val packageStartTimeDate = packageStartTime.removeTimeZone()
            val packageEndTimeDate = packageEndTime.removeTimeZone()

            val vpsPackageId: Long = 101

            val response = listOf(
                VpsPackage(
                    packageId = Constant.DEFAULT_SHOP_TIER_BENEFIT_PACKAGE_ID,
                    remainingQuota = 45,
                    currentQuota = 5,
                    isDisabled = false,
                    originalQuota = 50,
                    packageEndTime = packageEndTimeEpoch,
                    packageName = "Shop Tier Benefit",
                    packageStartTime = packageStartTimeEpoch
                )
            )
            val vpsPackages = listOf(
                VpsPackageUiModel(
                    packageId = -1L,
                    remainingQuota = 45,
                    currentQuota = 5,
                    originalQuota = 50,
                    packageEndTime = packageEndTimeDate,
                    packageName = "Shop Tier Benefit",
                    packageStartTime = packageStartTimeDate,
                    isSelected = false,
                    disabled = false,
                    isShopTierBenefit = true
                )
            )
            val expected = Success(vpsPackages)

            coEvery { getSellerCampaignPackageListUseCase.execute() } returns response

            // When
            viewModel.getVpsPackages(vpsPackageId)

            // Then
            val actual = viewModel.vpsPackages.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    @Test
    fun `When got a matching vps package, isSelected value should be true`() =
        runBlocking {
            // Given
            val selectedVpsPackageId: Long = 101
            val now = GregorianCalendar(2020, 8, 1, 7, 0, 0).time

            val packageStartTime = now
            val packageEndTime = now.advanceDayBy(days = 2)

            val packageStartTimeEpoch = (packageStartTime.time / 1000)
            val packageEndTimeEpoch = (packageEndTime.time / 1000)

            val packageStartTimeDate = packageStartTime.removeTimeZone()
            val packageEndTimeDate = packageEndTime.removeTimeZone()

            val response = listOf(
                VpsPackage(
                    packageId = Constant.DEFAULT_SHOP_TIER_BENEFIT_PACKAGE_ID,
                    remainingQuota = 45,
                    currentQuota = 5,
                    isDisabled = false,
                    originalQuota = 50,
                    packageEndTime = packageEndTimeEpoch,
                    packageName = "Shop Tier Benefit",
                    packageStartTime = packageStartTimeEpoch
                ),
                VpsPackage(
                    packageId = selectedVpsPackageId.toString(),
                    remainingQuota = 5,
                    currentQuota = 5,
                    isDisabled = false,
                    originalQuota = 10,
                    packageEndTime = packageEndTimeEpoch,
                    packageName = "Paket VPS September",
                    packageStartTime = packageStartTimeEpoch
                )
            )
            val vpsPackages = listOf(
                VpsPackageUiModel(
                    packageId = Constant.DEFAULT_SHOP_TIER_BENEFIT_PACKAGE_ID.toLong(),
                    remainingQuota = 45,
                    currentQuota = 5,
                    originalQuota = 50,
                    packageEndTime = packageEndTimeDate,
                    packageName = "Shop Tier Benefit",
                    packageStartTime = packageStartTimeDate,
                    isSelected = false,
                    disabled = false,
                    isShopTierBenefit = true
                ),
                VpsPackageUiModel(
                    packageId = selectedVpsPackageId,
                    remainingQuota = 5,
                    currentQuota = 5,
                    originalQuota = 10,
                    packageEndTime = packageEndTimeDate,
                    packageName = "Paket VPS September",
                    packageStartTime = packageStartTimeDate,
                    isSelected = true,
                    disabled = false,
                    isShopTierBenefit = false
                )
            )
            val expected = Success(vpsPackages)

            coEvery { getSellerCampaignPackageListUseCase.execute() } returns response

            // When
            viewModel.getVpsPackages(selectedVpsPackageId)

            // Then
            val actual = viewModel.vpsPackages.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    @Test
    fun `When get vps packages error, observer should receive error result`() =
        runBlocking {
            // Given
            val vpsPackageId: Long = 101
            val error = MessageErrorException("Server error")
            val expected = Fail(error)

            coEvery { getSellerCampaignPackageListUseCase.execute() } throws error

            // When
            viewModel.getVpsPackages(vpsPackageId)

            // Then
            val actual = viewModel.vpsPackages.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    //endregion

    //region getSelectedVpsPackage
    @Test
    fun `When get selected vps package, should return correct values`() {
        // Given
        val vpsPackage = buildVpsPackageUiModel()
        viewModel.setSelectedVpsPackage(vpsPackage)

        // When
        val actual = viewModel.getSelectedVpsPackage()

        // Then
        assertEquals(vpsPackage, actual)
    }
    //endregion

    //region getStoredVpsPackages
    @Test
    fun `When get stored vps package, should return correct values`() {
        // Given
        val vpsPackages = listOf(buildVpsPackageUiModel())
        viewModel.storeVpsPackage(vpsPackages)

        // When
        val actual = viewModel.getStoredVpsPackages()

        // Then
        assertEquals(vpsPackages, actual)
    }
    //endregion

    //region findSelectedVpsPackage
    @Test
    fun `When found matching vps package, should return correct values`() {
        // Given
        val vpsPackage = buildVpsPackageUiModel()
        val vpsPackages = listOf(vpsPackage)

        // When
        val actual = viewModel.findSelectedVpsPackage(selectedVpsPackageId = 101, vpsPackages)

        // Then
        assertEquals(vpsPackage, actual)
    }

    @Test
    fun `When found no matching vps package, should return null`() {
        // Given
        val vpsPackages = listOf(buildVpsPackageUiModel())

        // When
        val actual = viewModel.findSelectedVpsPackage(selectedVpsPackageId = 102, vpsPackages)

        // Then
        assertEquals(null, actual)
    }
    //endregion

    //region findSuggestedVpsPackage
    @Test
    fun `When selected vps package is expired, should return first element from the vpsPackages list as the new selected vps package`() {
        // Given
        val now = Date()

        val vpsPackageEndTime = now.decreaseHourBy(desiredHourToBeDecreased = 24)
        val expiredSelectedVpsPackage = buildVpsPackageUiModel().copy(packageId = 101, packageEndTime = vpsPackageEndTime)

        val firstVpsPackage = buildVpsPackageUiModel().copy(packageId = 102, packageName = "Active VPS Package 1")
        val secondVpsPackage = buildVpsPackageUiModel().copy(packageId = 103, packageName = "Active VPS Package 2")
        val vpsPackages = listOf(firstVpsPackage, secondVpsPackage)

        // When
        val actual = viewModel.findSuggestedVpsPackage(now, expiredSelectedVpsPackage, vpsPackages)

        // Then
        assertEquals(firstVpsPackage, actual)
    }

    @Test
    fun `When selected vps package is still active, should return the selected vps package`() {
        // Given
        val now = Date()

        val vpsPackageEndTime = now.advanceDayBy(days = 7)
        val selectedVpsPackage = buildVpsPackageUiModel().copy(packageId = 101, packageEndTime = vpsPackageEndTime)

        val firstVpsPackage = buildVpsPackageUiModel().copy(packageId = 102, packageName = "Active VPS Package 1")
        val secondVpsPackage = buildVpsPackageUiModel().copy(packageId = 103, packageName = "Active VPS Package 2")
        val vpsPackages = listOf(firstVpsPackage, secondVpsPackage)

        // When
        val actual = viewModel.findSuggestedVpsPackage(now, selectedVpsPackage, vpsPackages)

        // Then
        assertEquals(selectedVpsPackage, actual)
    }

    //endregion

    //region findCampaignMaxEndDateByVpsRule
    @Test
    fun `When vps package selected, max campaign end date should equals to selected vps package decreased by 30 minute`() {
        // Given
        val now = Date()
        val defaultEndDate = now.advanceMonthBy(months = 3)
        val vpsPackageEndTime = now.advanceDayBy(days = 7)
        val expectedMaxCampaignEndDate = vpsPackageEndTime.decreaseMinuteBy(desiredMinuteBeDecreased = 30)

        val vpsPackage = buildVpsPackageUiModel().copy(packageId = 101, packageEndTime = vpsPackageEndTime)

        // When
        val actual = viewModel.findCampaignMaxEndDateByVpsRule(vpsPackage, defaultEndDate)

        // Then
        assertEquals(expectedMaxCampaignEndDate, actual)
    }

    @Test
    fun `When shop tier benefit selected, max campaign end date should equals to defaultEndDate`() {
        // Given
        val now = Date()
        val defaultEndDate = now.advanceMonthBy(months = 3)
        val vpsPackageEndTime = now.advanceDayBy(days = 7)
        val expectedMaxCampaignEndDate = defaultEndDate

        val vpsPackage = buildVpsPackageUiModel().copy(
            packageId = Constant.DEFAULT_SHOP_TIER_BENEFIT_PACKAGE_ID.toLong(),
            packageEndTime = vpsPackageEndTime
        )

        // When
        val actual = viewModel.findCampaignMaxEndDateByVpsRule(vpsPackage, defaultEndDate)

        // Then
        assertEquals(expectedMaxCampaignEndDate, actual)
    }
    //endregion

    //region shouldEnableProceedButton
    @Test
    fun `When campaign name length is below minimum character, should return false`() {
        // Given
        val vpsPackage = buildVpsPackageUiModel()

        // When
        val actual = viewModel.shouldEnableProceedButton("camp", vpsPackage)

        // Then
        assertEquals(false, actual)
    }

    @Test
    fun `When selected vps package is shop tier benefit, should return true`() {
        // Given
        val vpsPackage = buildVpsPackageUiModel().copy(isShopTierBenefit = true)

        // When
        val actual = viewModel.shouldEnableProceedButton("September Campaign", vpsPackage)

        // Then
        assertEquals(true, actual)
    }

    @Test
    fun `When selected vps package is non shop tier benefit and has remaining quota, should return true`() {
        // Given
        val vpsPackage = buildVpsPackageUiModel().copy(isShopTierBenefit = false, remainingQuota = 5)

        // When
        val actual = viewModel.shouldEnableProceedButton("September Campaign", vpsPackage)

        // Then
        assertEquals(true, actual)
    }

    @Test
    fun `When selected vps package is non shop tier benefit and has no remaining quota, should return false`() {
        // Given
        val vpsPackage = buildVpsPackageUiModel().copy(isShopTierBenefit = false, remainingQuota = 0)

        // When
        val actual = viewModel.shouldEnableProceedButton("September Campaign", vpsPackage)

        // Then
        assertEquals(false, actual)
    }
    //endregion

    //region recheckLatestSelectedVpsPackageQuota
    @Test
    fun `When recheck latest vps package quota success, remainingQuota value should be updated with the latest value from remote`() {
        runBlocking {
            // Given
            val now = GregorianCalendar(2020, 8, 1, 7, 0, 0).time

            val packageStartTime = now
            val packageEndTime = now.advanceDayBy(days = 2)

            val packageStartTimeEpoch = (packageStartTime.time / 1000)
            val packageEndTimeEpoch = (packageEndTime.time / 1000)

            val packageStartTimeDate = packageStartTime.removeTimeZone()
            val packageEndTimeDate = packageEndTime.removeTimeZone()

            val selectedVpsPackage = VpsPackageUiModel(
                packageId = 101,
                remainingQuota = 1,
                currentQuota = 49,
                originalQuota = 50,
                packageEndTime = packageEndTime,
                packageName = "VPS Package",
                packageStartTime = packageStartTime,
                isSelected = false,
                disabled = false,
                isShopTierBenefit = false
            )

            val emptyQuotaVpsPackage = VpsPackage(
                packageId = 101.toString(),
                remainingQuota = 0,
                currentQuota = 50,
                isDisabled = false,
                originalQuota = 50,
                packageEndTime = packageEndTimeEpoch,
                packageName = "VPS Package",
                packageStartTime = packageStartTimeEpoch
            )

            val response = listOf(emptyQuotaVpsPackage)
            val expected = Success(
                selectedVpsPackage.copy(
                    packageStartTime = packageStartTimeDate,
                    packageEndTime = packageEndTimeDate,
                    remainingQuota = 0,
                    currentQuota = 50,
                    originalQuota = 50,
                    isSelected = true
                )
            )

            coEvery { getSellerCampaignPackageListUseCase.execute() } returns response

            viewModel.setSelectedVpsPackage(selectedVpsPackage)

            // When
            viewModel.recheckLatestSelectedVpsPackageQuota()

            // Then
            coVerify { emptyVpsPackageObserver.onChanged(expected) }
        }
    }

    @Test
    fun `When recheck latest vps package quota success but has no selected vps package, should not update observer`() {
        runBlocking {
            // Given
            val now = GregorianCalendar(2020, 8, 1, 7, 0, 0).time

            val packageStartTime = now
            val packageEndTime = now.advanceDayBy(days = 2)

            val packageStartTimeEpoch = (packageStartTime.time / 1000)
            val packageEndTimeEpoch = (packageEndTime.time / 1000)

            val packageStartTimeDate = packageStartTime.removeTimeZone()
            val packageEndTimeDate = packageEndTime.removeTimeZone()

            val selectedVpsPackage = VpsPackageUiModel(
                packageId = 101,
                remainingQuota = 1,
                currentQuota = 49,
                originalQuota = 50,
                packageEndTime = packageEndTime,
                packageName = "VPS Package",
                packageStartTime = packageStartTime,
                isSelected = false,
                disabled = false,
                isShopTierBenefit = false
            )

            val emptyQuotaVpsPackage = VpsPackage(
                packageId = 101.toString(),
                remainingQuota = 0,
                currentQuota = 50,
                isDisabled = false,
                originalQuota = 50,
                packageEndTime = packageEndTimeEpoch,
                packageName = "VPS Package",
                packageStartTime = packageStartTimeEpoch
            )

            val response = listOf(emptyQuotaVpsPackage)
            val expected = Success(
                selectedVpsPackage.copy(
                    packageStartTime = packageStartTimeDate,
                    packageEndTime = packageEndTimeDate,
                    remainingQuota = 0,
                    currentQuota = 50,
                    originalQuota = 50,
                    isSelected = true
                )
            )

            coEvery { getSellerCampaignPackageListUseCase.execute() } returns response

            // When
            viewModel.recheckLatestSelectedVpsPackageQuota()

            coVerify(exactly = 0) { emptyVpsPackageObserver.onChanged(expected) }
        }
    }

    @Test
    fun `When recheck latest vps package quota error, observer should receive error result`() {
        runBlocking {
            // Given
            val error = MessageErrorException("Server error")
            val expected = Fail(error)

            coEvery { getSellerCampaignPackageListUseCase.execute() } throws error

            // When
            viewModel.recheckLatestSelectedVpsPackageQuota()

            // Then
            coVerify { emptyVpsPackageObserver.onChanged(expected) }
        }
    }
    //endregion

    //region isTodayInVpsPeriod
    @Test
    fun `When current date time is within selected package vps period, should return true`() {
        // Given
        val now = Date()
        val packageStartTime = now.decreaseHourBy(desiredHourToBeDecreased = 1)
        val packageEndTime = now.advanceDayBy(days = 7)
        val vpsPackage = buildVpsPackageUiModel().copy(packageStartTime = packageStartTime, packageEndTime = packageEndTime)

        // When
        val actual = viewModel.isTodayInVpsPeriod(vpsPackage)

        // Then
        assertEquals(true, actual)
    }

    @Test
    fun `When current date time is after selected package vps start time, should return false`() {
        // Given
        val now = Date()
        val packageStartTime = now.decreaseHourBy(desiredHourToBeDecreased = 1)
        val packageEndTime = now.decreaseHourBy(desiredHourToBeDecreased = 2)
        val vpsPackage = buildVpsPackageUiModel().copy(packageStartTime = packageStartTime, packageEndTime = packageEndTime)

        // When
        val actual = viewModel.isTodayInVpsPeriod(vpsPackage)

        // Then
        assertEquals(false, actual)
    }

    @Test
    fun `When current date time is before selected package vps start time, should return false`() {
        // Given
        val now = Date()
        val packageStartTime = now.advanceDayBy(days = 1)
        val packageEndTime = now.advanceDayBy(days = 7)
        val vpsPackage = buildVpsPackageUiModel().copy(packageStartTime = packageStartTime, packageEndTime = packageEndTime)

        // When
        val actual = viewModel.isTodayInVpsPeriod(vpsPackage)

        // Then
        assertEquals(false, actual)
    }

    @Test
    fun `When current date time is after selected package vps end time, should return false`() {
        // Given
        val now = Date()
        val packageStartTime = now.decreaseMinuteBy(desiredMinuteBeDecreased = 30)
        val packageEndTime = now.decreaseHourBy(desiredHourToBeDecreased = 1)
        val vpsPackage = buildVpsPackageUiModel().copy(packageStartTime = packageStartTime, packageEndTime = packageEndTime)

        // When
        val actual = viewModel.isTodayInVpsPeriod(vpsPackage)

        // Then
        assertEquals(false, actual)
    }
    //endregion

    //region Helper function
    private fun buildVpsPackageUiModel(): VpsPackageUiModel {
        return VpsPackageUiModel(
            packageId = 101,
            remainingQuota = 5,
            currentQuota = 5,
            originalQuota = 10,
            packageEndTime = Date().advanceDayBy(days = 7),
            packageName = "Paket VPS September",
            packageStartTime = Date(),
            isSelected = true,
            disabled = false,
            isShopTierBenefit = false
        )
    }
    private fun buildSelectionObject(): CampaignInformationViewModel.Selection {
        return CampaignInformationViewModel.Selection(
            "Adidas Sale",
            startDate = Date(),
            endDate = Date(),
            showTeaser = false,
            teaserDate = Date(),
            firstColor = "#FFFFFF",
            secondColor = "#00FF00",
            PaymentType.INSTANT,
            remainingQuota = 5,
            vpsPackageId = 1,
            isOosImprovement = false
        )
    }
    private fun buildCampaignUiModel(campaignId: Long): CampaignUiModel {
        return CampaignUiModel(
            campaignId,
            "",
            "",
            "",
            isCancellable = false,
            isShareable = false,
            0,
            "",
            "",
            CampaignStatus.ONGOING,
            false,
            CampaignUiModel.ProductSummary(0, 0, 0, 0, 0, 0),
            Date(),
            Date(),
            Gradient("", "", false),
            false,
            Date(),
            PaymentType.INSTANT,
            isUniqueBuyer = false,
            isCampaignRelation = false,
            listOf(),
            isCampaignRuleSubmit = false, 0,
            CampaignUiModel.ThematicInfo(0, 0, "", 0, ""),
            Date(),
            Date(),
            CampaignUiModel.PackageInfo(packageId = 1, packageName = "VPS Package Elite"),
            isOosImprovement = false
        )
    }
    //endregion
}
