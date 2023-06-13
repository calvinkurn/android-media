package com.tokopedia.shop.flashsale.presentation.list.container

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.flashsale.domain.entity.TabMeta
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignListTab
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignEligibilityUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignListMetaUseCase
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
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class CampaignListContainerViewModelTest {

    @RelaxedMockK
    lateinit var getSellerCampaignListMetaUseCase: GetSellerCampaignListMetaUseCase

    @RelaxedMockK
    lateinit var getSellerCampaignEligibilityUseCase: GetSellerCampaignEligibilityUseCase

    @RelaxedMockK
    lateinit var tabsMetaObserver: Observer<in Result<CampaignListContainerViewModel.TabMetaDataResult>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel by lazy {
        CampaignListContainerViewModel(
            CoroutineTestDispatchersProvider,
            getSellerCampaignListMetaUseCase,
            getSellerCampaignEligibilityUseCase
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel.tabsMeta.observeForever(tabsMetaObserver)
    }

    @After
    fun tearDown() {
        viewModel.tabsMeta.removeObserver(tabsMetaObserver)
    }

    //region getPrerequisiteData
    @Test
    fun `When get prerequisite data and seller is eligible, observer should receive true as a result`() =
        runBlocking {
            //Given
            val targetTabPosition = CampaignListTab.ACTIVE_CAMPAIGN.position
            val tabMeta = listOf(
                TabMeta(id = 1, totalCampaign = 20, "Campaign Aktif", status = listOf(CampaignStatus.ONGOING.id)),
                TabMeta(id = 2, totalCampaign = 20, "Riwayat Campaign", status = listOf(CampaignStatus.FINISHED.id))
            )
            val tabMetaResult = CampaignListContainerViewModel.TabMetaDataResult(targetTabPosition, tabMeta)
            val isEligible = true
            val expected = Success(isEligible)

            coEvery { getSellerCampaignEligibilityUseCase.execute() } returns isEligible
            coEvery { getSellerCampaignListMetaUseCase.execute() } returns tabMeta

            //When
            viewModel.getPrerequisiteData(targetTabPosition)


            //Then
            coVerify { tabsMetaObserver.onChanged(Success(tabMetaResult)) }
            val actual = viewModel.isEligible.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    @Test
    fun `When get prerequisite and fetch eligibility to remote error, observer should receive error result`() =
        runBlocking {
            //Given
            val targetTabPosition = CampaignListTab.ACTIVE_CAMPAIGN.position
            val error = MessageErrorException("Server error")
            val expected = Fail(error)

            coEvery { getSellerCampaignEligibilityUseCase.execute() } throws error

            //When
            viewModel.getPrerequisiteData(targetTabPosition)

            //Then
            val actual = viewModel.isEligible.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    @Test
    fun `When has no active campaign and has no history campaign, observer should receive false as a result`() =
        runBlocking {
            //Given
            val targetTabPosition = CampaignListTab.ACTIVE_CAMPAIGN.position
            val tabMeta = listOf(
                TabMeta(id = 1, totalCampaign = 0, "Campaign Aktif", status = listOf(CampaignStatus.ONGOING.id)),
                TabMeta(id = 2, totalCampaign = 0, "Riwayat Campaign", status = listOf(CampaignStatus.FINISHED.id))
            )
            val isEligible = false
            val expected = Success(isEligible)

            coEvery { getSellerCampaignEligibilityUseCase.execute() } returns isEligible
            coEvery { getSellerCampaignListMetaUseCase.execute() } returns tabMeta

            //When
            viewModel.getPrerequisiteData(targetTabPosition)


            //Then
            val actual = viewModel.isEligible.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    @Test
    fun `When got empty campaign metadata from remote, observer should receive false as a result`() =
        runBlocking {
            //Given
            val targetTabPosition = CampaignListTab.ACTIVE_CAMPAIGN.position
            val tabMeta = listOf<TabMeta>()
            val isEligible = false
            val expected = Success(isEligible)

            coEvery { getSellerCampaignEligibilityUseCase.execute() } returns isEligible
            coEvery { getSellerCampaignListMetaUseCase.execute() } returns tabMeta

            //When
            viewModel.getPrerequisiteData(targetTabPosition)


            //Then
            val actual = viewModel.isEligible.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    @Test
    fun `When got incorrect campaign status id from remote, observer should receive false as a result`() =
        runBlocking {
            //Given
            val incorrectActiveCampaignId = 4
            val incorrectHistoryCampaignId = 5
            val targetTabPosition = CampaignListTab.ACTIVE_CAMPAIGN.position
            val tabMeta = listOf(
                TabMeta(id = incorrectActiveCampaignId, totalCampaign = 0, "Campaign Aktif", status = listOf(CampaignStatus.ONGOING.id)),
                TabMeta(id = incorrectHistoryCampaignId, totalCampaign = 0, "Riwayat Campaign", status = listOf(CampaignStatus.FINISHED.id))
            )
            val isEligible = false
            val expected = Success(isEligible)

            coEvery { getSellerCampaignEligibilityUseCase.execute() } returns isEligible
            coEvery { getSellerCampaignListMetaUseCase.execute() } returns tabMeta

            //When
            viewModel.getPrerequisiteData(targetTabPosition)


            //Then
            val actual = viewModel.isEligible.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    @Test
    fun `When seller has campaign history, observer should receive true as a result`() =
        runBlocking {
            //Given
            val targetTabPosition = CampaignListTab.ACTIVE_CAMPAIGN.position
            val tabMeta = listOf(
                TabMeta(id = 1, totalCampaign = 0, "Campaign Aktif", status = listOf(CampaignStatus.ONGOING.id)),
                TabMeta(id = 2, totalCampaign = 20, "Riwayat Campaign", status = listOf(CampaignStatus.FINISHED.id))
            )
            val isEligible = false
            val expected = Success(true)

            coEvery { getSellerCampaignEligibilityUseCase.execute() } returns isEligible
            coEvery { getSellerCampaignListMetaUseCase.execute() } returns tabMeta

            //When
            viewModel.getPrerequisiteData(targetTabPosition)


            //Then
            val actual = viewModel.isEligible.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    //endregion


    @Test
    fun `When seller has active campaign, observer should receive true as a result`() =
        runBlocking {
            //Given
            val targetTabPosition = CampaignListTab.ACTIVE_CAMPAIGN.position
            val tabMeta = listOf(
                TabMeta(id = 1, totalCampaign = 20, "Campaign Aktif", status = listOf(CampaignStatus.ONGOING.id)),
                TabMeta(id = 2, totalCampaign = 0, "Riwayat Campaign", status = listOf(CampaignStatus.FINISHED.id))
            )
            val isEligible = false
            val expected = Success(true)

            coEvery { getSellerCampaignEligibilityUseCase.execute() } returns isEligible
            coEvery { getSellerCampaignListMetaUseCase.execute() } returns tabMeta

            //When
            viewModel.getPrerequisiteData(targetTabPosition)


            //Then
            val actual = viewModel.isEligible.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    //endregion

    @Test
    fun `When seller has both active campaign and campaign history, observer should receive true as a result`() =
        runBlocking {
            //Given
            val targetTabPosition = CampaignListTab.ACTIVE_CAMPAIGN.position
            val tabMeta = listOf(
                TabMeta(id = 1, totalCampaign = 20, "Campaign Aktif", status = listOf(CampaignStatus.ONGOING.id)),
                TabMeta(id = 2, totalCampaign = 20, "Riwayat Campaign", status = listOf(CampaignStatus.FINISHED.id))
            )
            val isEligible = false
            val expected = Success(true)

            coEvery { getSellerCampaignEligibilityUseCase.execute() } returns isEligible
            coEvery { getSellerCampaignListMetaUseCase.execute() } returns tabMeta

            //When
            viewModel.getPrerequisiteData(targetTabPosition)


            //Then
            val actual = viewModel.isEligible.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    //endregion

    //region getTabsMeta
    @Test
    fun `When get tabs metadata success, observer should successfully receive the data`() =
        runBlocking {
            //Given
            val targetTabPosition = CampaignListTab.ACTIVE_CAMPAIGN.position
            val tabMeta = listOf(TabMeta(id = 1, totalCampaign = 20, "Campaign Aktif", status = listOf(2,4,8,14)))
            val expected = Success(CampaignListContainerViewModel.TabMetaDataResult(targetTabPosition, tabMeta))

            coEvery { getSellerCampaignListMetaUseCase.execute() } returns tabMeta

            //When
            viewModel.getTabsMeta(targetTabPosition)


            //Then
            val actual = viewModel.tabsMeta.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    @Test
    fun `When get tabs metadata error, observer should receive error result`() =
        runBlocking {
            //Given
            val targetTabPosition = CampaignListTab.ACTIVE_CAMPAIGN.position
            val error = MessageErrorException("Server error")
            val expected = Fail(error)

            coEvery { getSellerCampaignListMetaUseCase.execute() } throws error

            //When
            viewModel.getTabsMeta(targetTabPosition)

            //Then
            val actual = viewModel.tabsMeta.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    //endregion
}