package com.tokopedia.shop.flashsale.presentation.list.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.data.model.ShopPageGetHomeType
import com.tokopedia.shop.common.domain.interactor.GqlShopPageGetHomeType
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.extension.advanceDayBy
import com.tokopedia.shop.flashsale.common.extension.decreaseHourBy
import com.tokopedia.shop.flashsale.common.tracker.ShopFlashSaleTracker
import com.tokopedia.shop.flashsale.domain.entity.CampaignMeta
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.Gradient
import com.tokopedia.shop.flashsale.domain.entity.VpsPackage
import com.tokopedia.shop.flashsale.domain.entity.VpsPackageAvailability
import com.tokopedia.shop.flashsale.domain.entity.aggregate.CampaignCreationEligibility
import com.tokopedia.shop.flashsale.domain.entity.aggregate.CampaignPrerequisiteData
import com.tokopedia.shop.flashsale.domain.entity.aggregate.ShareComponentMetadata
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignListUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignPackageListUseCase
import com.tokopedia.shop.flashsale.domain.usecase.aggregate.GenerateCampaignBannerUseCase
import com.tokopedia.shop.flashsale.domain.usecase.aggregate.GetCampaignPrerequisiteDataUseCase
import com.tokopedia.shop.flashsale.domain.usecase.aggregate.GetShareComponentMetadataUseCase
import com.tokopedia.shop.flashsale.domain.usecase.aggregate.ValidateCampaignCreationEligibilityUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class CampaignListViewModelTest {
    @RelaxedMockK
    lateinit var getSellerCampaignListUseCase: GetSellerCampaignListUseCase

    @RelaxedMockK
    lateinit var getCampaignPrerequisiteDataUseCase: GetCampaignPrerequisiteDataUseCase

    @RelaxedMockK
    lateinit var getShareComponentMetadataUseCase: GetShareComponentMetadataUseCase

    @RelaxedMockK
    lateinit var validateCampaignCreationEligibility: ValidateCampaignCreationEligibilityUseCase

    @RelaxedMockK
    lateinit var generateCampaignBannerUseCase: GenerateCampaignBannerUseCase

    @RelaxedMockK
    lateinit var getShopPageHomeTypeUseCase: GqlShopPageGetHomeType

    @RelaxedMockK
    lateinit var getSellerCampaignPackageListUseCase: GetSellerCampaignPackageListUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var tracker: ShopFlashSaleTracker

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel by lazy {
        CampaignListViewModel(
            CoroutineTestDispatchersProvider,
            getSellerCampaignListUseCase,
            getCampaignPrerequisiteDataUseCase,
            getShareComponentMetadataUseCase,
            validateCampaignCreationEligibility,
            generateCampaignBannerUseCase,
            getShopPageHomeTypeUseCase,
            getSellerCampaignPackageListUseCase,
            userSession,
            tracker
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }



    //region getCampaigns
    @Test
    fun `When search keyword is number, campaign name should be changed to empty string`() =
        runBlocking {
            //Given
            val rows = 0
            val offset = 10
            val statusIds = listOf(CampaignStatus.DRAFT.id)
            val searchKeyword = "12345"

            //When
            viewModel.getCampaigns(rows, offset, statusIds, searchKeyword)

            //Then
            coVerify {
                getSellerCampaignListUseCase.execute(
                    rows = rows,
                    offset = offset,
                    statusId = statusIds,
                    campaignId = 12345,
                    campaignName = ""
                )
            }
        }

    @Test
    fun `When search keyword is string, campaign id params should be changed to 0`() =
        runBlocking {
            //Given
            val rows = 0
            val offset = 10
            val statusIds = listOf(CampaignStatus.DRAFT.id)
            val searchKeyword = "Adidas Bosque"

            //When
            viewModel.getCampaigns(rows, offset, statusIds, searchKeyword)

            //Then
            coVerify {
                getSellerCampaignListUseCase.execute(
                    rows = rows,
                    offset = offset,
                    statusId = statusIds,
                    campaignId = 0,
                    campaignName = "Adidas Bosque"
                )
            }
        }

    @Test
    fun `When get campaign success, observer should successfully received the data`() =
        runBlocking {
            //Given
            val campaignMeta = CampaignMeta(
                totalCampaign = 20,
                totalCampaignActive = 5,
                totalCampaignFinished = 15,
                campaigns = listOf()
            )
            val expected = Success(campaignMeta)

            val rows = 0
            val offset = 10
            val statusIds = listOf(CampaignStatus.ONGOING.id)
            val searchKeyword = "Adidas Bosque"

            coEvery {
                getSellerCampaignListUseCase.execute(
                    rows = rows,
                    offset = offset,
                    statusId = statusIds,
                    campaignId = 0,
                    campaignName = searchKeyword
                )
            } returns campaignMeta

            //When
            viewModel.getCampaigns(rows, offset, statusIds, searchKeyword)


            //Then
            val actual = viewModel.campaigns.getOrAwaitValue()
            assertEquals(expected, actual)
        }


    @Test
    fun `When get campaign error, observer should receive error result`() =
        runBlocking {
            //Given
            val rows = 0
            val offset = 10
            val statusIds = listOf(CampaignStatus.ONGOING.id)
            val searchKeyword = "Adidas Bosque"
            val error = MessageErrorException("Server error")
            val expected = Fail(error)

            coEvery {
                getSellerCampaignListUseCase.execute(
                    rows = rows,
                    offset = offset,
                    statusId = statusIds,
                    campaignId = 0,
                    campaignName = searchKeyword
                )
            } throws error

            //When
            viewModel.getCampaigns(rows, offset, statusIds, searchKeyword)

            //Then
            val actual = viewModel.campaigns.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    //endregion

    //region getCampaignPrerequisiteData
    @Test
    fun `When get campaign prerequisite data success, observer should successfully received the data`() =
        runBlocking {
            //Given
            val campaignPrerequisiteData = CampaignPrerequisiteData(drafts = listOf())
            val expected = Success(campaignPrerequisiteData)

            coEvery { getCampaignPrerequisiteDataUseCase.execute() } returns campaignPrerequisiteData

            //When
            viewModel.getCampaignPrerequisiteData()

            //Then
            val actual = viewModel.campaignPrerequisiteData.getOrAwaitValue()
            assertEquals(expected, actual)
        }


    @Test
    fun `When get campaign prerequisite data error, observer should receive error result`() =
        runBlocking {
            //Given
            val error = MessageErrorException("Server error")
            val expected = Fail(error)

            coEvery { getCampaignPrerequisiteDataUseCase.execute() } throws error

            //When
            viewModel.getCampaignPrerequisiteData()

            //Then
            val actual = viewModel.campaignPrerequisiteData.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    //endregion

    //region getShareComponentMetadata
    @Test
    fun `When get share component metadata success, observer should successfully received the data`() =
        runBlocking {
            //Given
            val campaignId : Long = 1001
            val shareComponentMetadata = mockk<ShareComponentMetadata>()
            val expected = Success(shareComponentMetadata)

            coEvery { getShareComponentMetadataUseCase.execute(campaignId) } returns shareComponentMetadata

            //When
            viewModel.getShareComponentMetadata(campaignId)

            //Then
            val actual = viewModel.shareComponentMetadata.getOrAwaitValue()
            assertEquals(expected, actual)
        }


    @Test
    fun `When get share component metadata error, observer should receive error result`() =
        runBlocking {
            //Given
            val campaignId : Long = 1001
            val error = MessageErrorException("Server error")
            val expected = Fail(error)

            coEvery { getShareComponentMetadataUseCase.execute(campaignId) } throws error

            //When
            viewModel.getShareComponentMetadata(campaignId)

            //Then
            val actual = viewModel.shareComponentMetadata.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    //endregion

    //region getShareComponentThumbnailImageUrl
    @Test
    fun `When get share component thumbnail image url success, observer should successfully received the data`() =
        runBlocking {
            //Given
            val campaignId : Long = 1001
            val thumbnailImageUrl = "https://images.tokopedia.com/some-banner.png"
            val campaign = buildCampaignUiModel(campaignId)
            val expected = Success(thumbnailImageUrl)

            coEvery { generateCampaignBannerUseCase.execute(campaignId) } returns thumbnailImageUrl

            //When
            viewModel.onMoreMenuShareClicked(campaign)

            //Then
            val actual = viewModel.shareComponentThumbnailImageUrl.getOrAwaitValue()
            assertEquals(expected, actual)
        }


    @Test
    fun `When get share component thumbnail image url error, observer should receive error result`() =
        runBlocking {
            //Given
            val campaignId : Long = 1001
            val error = MessageErrorException("Server error")
            val campaign = buildCampaignUiModel(campaignId)
            val expected = Fail(error)

            coEvery { generateCampaignBannerUseCase.execute(campaignId) } throws error

            //When
            viewModel.onMoreMenuShareClicked(campaign)

            //Then
            val actual = viewModel.shareComponentThumbnailImageUrl.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    @Test
    fun `When get share component thumbnail image url, should send tracker`() =
        runBlocking {
            val campaignId: Long = 1001
            val campaign = buildCampaignUiModel(campaignId)

            //When
            viewModel.onMoreMenuShareClicked(campaign)

            //Then
            coVerify { tracker.sendClickShareCampaignPopupEvent(campaign) }
        }
    //endregion

    //region validateCampaignCreationEligibility
    @Test
    fun `When validating campaign creation eligibility success, observer should successfully received the data`() =
        runBlocking {
            //Given
            val vpsPackageId : Long = 1
            val campaignCreationEligibility = mockk<CampaignCreationEligibility>()
            val expected = Success(campaignCreationEligibility)

            coEvery { validateCampaignCreationEligibility.execute(vpsPackageId = vpsPackageId) } returns campaignCreationEligibility

            //When
            viewModel.validateCampaignCreationEligibility(vpsPackageId)

            //Then
            val actual = viewModel.creationEligibility.getOrAwaitValue()
            assertEquals(expected, actual)
        }


    @Test
    fun `When validating campaign creation eligibility error, observer should receive error result`() =
        runBlocking {
            //Given
            val vpsPackageId: Long = 1
            val error = MessageErrorException("Server error")
            val expected = Fail(error)

            coEvery { validateCampaignCreationEligibility.execute(vpsPackageId) } throws error

            //When
            viewModel.validateCampaignCreationEligibility(vpsPackageId)

            //Then
            val actual = viewModel.creationEligibility.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    //endregion

    //region getShopDecorStatus
    @Test
    fun `When get shop decor status success, observer should successfully received the data`() =
        runBlocking {
            //Given
            val mockedShopHomeType = ""
            val shopPageGetHomeType = ShopPageGetHomeType()
            val expected = Success(mockedShopHomeType)

            coEvery { getShopPageHomeTypeUseCase.executeOnBackground() } returns shopPageGetHomeType

            //When
            viewModel.getShopDecorStatus()

            //Then
            val actual = viewModel.shopDecorStatus.getOrAwaitValue()
            assertEquals(expected, actual)
        }


    @Test
    fun `When get shop decor status error, observer should receive error result`() =
        runBlocking {
            //Given
            val error = MessageErrorException("Server error")
            val expected = Fail(error)

            coEvery { getShopPageHomeTypeUseCase.executeOnBackground() } throws error

            //When
            viewModel.getShopDecorStatus()

            //Then
            val actual = viewModel.shopDecorStatus.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    //endregion

    //region getVpsPackages
    @Test
    fun `When get vps packages success, observer should successfully received the data`() =
        runBlocking {
            //Given
            val packageStartTime = Date().time
            val packageEndTime = Date().advanceDayBy(days = 1).time
            val vpsPackage = VpsPackage(
                remainingQuota = 45,
                currentQuota = 5,
                isDisabled = false,
                originalQuota = 50,
                packageEndTime = packageEndTime,
                packageId = "1",
                packageName = "Elite VPS Package",
                packageStartTime = packageStartTime
            )

            val response = listOf(vpsPackage)
            val expected = Success(listOf(vpsPackage))

            coEvery { getSellerCampaignPackageListUseCase.execute() } returns response

            //When
            viewModel.getVpsPackages()

            //Then
            val actual = viewModel.vpsPackages.getOrAwaitValue()
            assertEquals(expected, actual)
        }


    @Test
    fun `When get vps packages error, observer should receive error result`() =
        runBlocking {
            //Given
            val error = MessageErrorException("Server error")
            val expected = Fail(error)

            coEvery { getSellerCampaignPackageListUseCase.execute() } throws error

            //When
            viewModel.getVpsPackages()

            //Then
            val actual = viewModel.vpsPackages.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    //endregion

    @Test
    fun `When get campaign drafts, should return correct values`() {
        val drafts = listOf<CampaignUiModel>()
        viewModel.setCampaignDrafts(drafts)

        assertEquals(drafts, viewModel.getCampaignDrafts())
    }

    @Test
    fun `When get selected campaign id, should return correct values`() {
        val campaignId : Long = 1001
        viewModel.setSelectedCampaignId(campaignId)

        assertEquals(campaignId, viewModel.getSelectedCampaignId())
    }

    @Test
    fun `When get thumbnail image url, should return correct values`() {
        val thumbnailImageUrl = "https://images.tokopedia.com/some-banner.png"
        viewModel.setThumbnailImageUrl(thumbnailImageUrl)

        assertEquals(thumbnailImageUrl, viewModel.getThumbnailImageUrl())
    }

    @Test
    fun `When press view campaign detail menu, should send tracker`() {
        viewModel.onMoreMenuViewCampaignDetailClicked()

        coVerify { tracker.sendClickViewCampaignDetailPopUpEvent() }
    }


    @Test
    fun `When press cancel campaign, should send tracker`() {
        val campaignId : Long = 1001
        val campaign = buildCampaignUiModel(campaignId)

        viewModel.onMoreMenuCancelClicked(campaign)

        coVerify { tracker.sendClickCancelPopupEvent(campaign) }
    }

    @Test
    fun `When press stop campaign, should send tracker`() {
        val campaignId : Long = 1001
        val campaign = buildCampaignUiModel(campaignId)

        viewModel.onMoreMenuStopClicked(campaign)

        coVerify { tracker.sendClickStopPopupEvent(campaign) }
    }

    @Test
    fun `When press edit campaign, should send tracker`() {
        val campaignId : Long = 1001
        val campaign = buildCampaignUiModel(campaignId)

        viewModel.onMoreMenuEditClicked(campaign)

        coVerify { tracker.sendClickEditPopupEvent(campaign) }
    }

    @Test
    fun `When get vps packages success and function findActiveVpsPackagesCount invoked, should return correct vps packages count`() {
        //Given
        val packageStartTime = Date().time
        val packageEndTime = Date().advanceDayBy(days = 1).time
        val vpsPackage = VpsPackage(
            remainingQuota = 45,
            currentQuota = 5,
            isDisabled = false,
            originalQuota = 50,
            packageEndTime = packageEndTime,
            packageId = "1",
            packageName = "Elite VPS Package",
            packageStartTime = packageStartTime
        )

        val vpsPackages = listOf(vpsPackage)
        val expected = 1

        coEvery { getSellerCampaignPackageListUseCase.execute() } returns vpsPackages

        //When
        viewModel.getVpsPackages()

        //Then
        val actual = viewModel.findActiveVpsPackagesCount()
        assertEquals(expected, actual)
    }

    @Test
    fun `When get vps packages return empty list and function findActiveVpsPackagesCount invoked, vps package count should be 0`() {
        //Given
        val error = MessageErrorException("Server error")
        val expected = 0

        coEvery { getSellerCampaignPackageListUseCase.execute() } throws error

        //When
        viewModel.getVpsPackages()

        //Then
        val actual = viewModel.findActiveVpsPackagesCount()
        assertEquals(expected, actual)
    }

    @Test
    fun `When having vps package with expired date more than 3 days, should return correct data`() {
        //Given
        val packageStartTime = Date().time
        val packageEndTime = Date().advanceDayBy(days = 4).time
        val vpsPackage = VpsPackage(
            packageId = "1",
            remainingQuota = 45,
            currentQuota = 5,
            isDisabled = false,
            originalQuota = 50,
            packageEndTime = packageEndTime,
            packageName = "Elite VPS Package",
            packageStartTime = packageStartTime
        )
        val vpsPackages = listOf(vpsPackage)
        val expected = VpsPackageAvailability(totalQuota = 50, remainingQuota = 45, isNearExpirePackageAvailable = false, packageNearExpire = 0)

        //When
        val actual = viewModel.getPackageAvailability(vpsPackages)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When having vps package with expired date less than 3 days, isNearExpirePackageAvailable should be true`() {
        //Given
        val now = Date()
        val packageStartTime = now.decreaseHourBy(desiredHourToBeDecreased = 24).time
        val packageEndTime = now.advanceDayBy(days = 2).time
        val packageStartTimeEpoch = (packageStartTime / 1000)
        val packageEndTimeEpoch = (packageEndTime / 1000)

        val vpsPackage = VpsPackage(
            packageId = "1",
            remainingQuota = 45,
            currentQuota = 5,
            isDisabled = false,
            originalQuota = 50,
            packageEndTime = packageEndTimeEpoch,
            packageName = "Elite VPS Package",
            packageStartTime = packageStartTimeEpoch
        )
        val vpsPackages = listOf(vpsPackage)
        val expected = VpsPackageAvailability(totalQuota = 50, remainingQuota = 45, isNearExpirePackageAvailable = true, packageNearExpire = 1)

        //When
        val actual = viewModel.getPackageAvailability(vpsPackages)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When having vps package with expiration within 3 days, isNearExpirePackageAvailable should be true`() {
        //Given
        val now = Date()
        val packageStartTime = now.decreaseHourBy(desiredHourToBeDecreased = 24).time
        val packageEndTime = now.advanceDayBy(days = 3).time
        val packageStartTimeEpoch = (packageStartTime / 1000)
        val packageEndTimeEpoch = (packageEndTime / 1000)

        val vpsPackage = VpsPackage(
            packageId = "1",
            remainingQuota = 45,
            currentQuota = 5,
            isDisabled = false,
            originalQuota = 50,
            packageEndTime = packageEndTimeEpoch,
            packageName = "Elite VPS Package",
            packageStartTime = packageStartTimeEpoch
        )
        val vpsPackages = listOf(vpsPackage)
        val expected = VpsPackageAvailability(totalQuota = 50, remainingQuota = 45, isNearExpirePackageAvailable = true, packageNearExpire = 1)

        //When
        val actual = viewModel.getPackageAvailability(vpsPackages)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When having shop tier benefit package expiration less than 3 days, isNearExpirePackageAvailable should be false`() {
        //Given
        val now = Date()
        val packageStartTime = now.decreaseHourBy(desiredHourToBeDecreased = 24).time
        val packageEndTime = now.advanceDayBy(days = 3).time
        val packageStartTimeEpoch = (packageStartTime / 1000)
        val packageEndTimeEpoch = (packageEndTime / 1000)

        val vpsPackage = VpsPackage(
            packageId = Constant.DEFAULT_SHOP_TIER_BENEFIT_PACKAGE_ID,
            remainingQuota = 45,
            currentQuota = 5,
            isDisabled = false,
            originalQuota = 50,
            packageEndTime = packageEndTimeEpoch,
            packageName = "Elite VPS Package",
            packageStartTime = packageStartTimeEpoch
        )
        val vpsPackages = listOf(vpsPackage)
        val expected = VpsPackageAvailability(totalQuota = 50, remainingQuota = 45, isNearExpirePackageAvailable = false, packageNearExpire = 0)

        //When
        val actual = viewModel.getPackageAvailability(vpsPackages)

        //Then
        assertEquals(expected, actual)
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
            CampaignUiModel.PackageInfo(packageId = 1, packageName = "VPS Package Eltie")
        )
    }
}
