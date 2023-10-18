package com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.relatedcampaign

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.Gradient
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import com.tokopedia.shop.flashsale.domain.usecase.GetRelatedCampaignsUseCase
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.relatedcampaign.adapter.RelatedCampaignItem
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import java.util.*

class ChooseRelatedCampaignViewModelTest {

    @RelaxedMockK
    lateinit var usecase: GetRelatedCampaignsUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testRule = CoroutineTestRule()

    private lateinit var viewModel: ChooseRelatedCampaignViewModel

    @RelaxedMockK
    lateinit var relatedCampaignsResultObserver: Observer<in ChooseRelatedCampaignResult>

    @RelaxedMockK
    lateinit var isAddRelatedCampaignButtonActiveObserver: Observer<in Boolean>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = ChooseRelatedCampaignViewModel(
            usecase,
            CoroutineTestDispatchersProvider
        )

        with(viewModel) {
            relatedCampaignsResult.observeForever(relatedCampaignsResultObserver)
            isAddRelatedCampaignButtonActive.observeForever(isAddRelatedCampaignButtonActiveObserver)
        }
    }

    @After
    fun tearDown() {
        with(viewModel) {
            relatedCampaignsResult.removeObserver(relatedCampaignsResultObserver)
            isAddRelatedCampaignButtonActive.removeObserver(isAddRelatedCampaignButtonActiveObserver)
        }
    }

    @Test
    fun `check getPreviousCampaign() when keyword is empty`() {
        runBlocking {
            viewModel.setCampaignId(1)
            coEvery {
                usecase.execute("", 1)
            } returns arrayListOf()
            viewModel.getPreviousCampaign()
            delay(11000L)
            assertTrue(viewModel.relatedCampaignsResult.getOrAwaitValue() is ChooseRelatedCampaignResult.Success)
        }
    }

    @Test
    fun `check getPreviousCampaign() when keyword not empty but result is empty`() {
        runBlocking {
            viewModel.setCampaignId(1)
            coEvery {
                usecase.execute("A", 1)
            } returns arrayListOf()
            viewModel.getPreviousCampaign("A")
            delay(11000L)
            assertTrue(viewModel.relatedCampaignsResult.getOrAwaitValue() is ChooseRelatedCampaignResult.SearchEmptyResult)
        }
    }

    @Test
    fun `check getPreviousCampaign() when keyword not empty and result is found something`() {
        val objectOfPreviousCampaign = buildCampaignUiModel(1)
        runBlocking {
            viewModel.setCampaignId(1)
            coEvery {
                usecase.execute("A", 1)
            } returns arrayListOf(objectOfPreviousCampaign)
            viewModel.getPreviousCampaign("A")
            delay(11000L)
            assertTrue(viewModel.relatedCampaignsResult.getOrAwaitValue() is ChooseRelatedCampaignResult.Success)
            val result =
                viewModel.relatedCampaignsResult.getOrAwaitValue() as ChooseRelatedCampaignResult.Success
            assertEquals("A", result.keyword)
        }
    }

    @Test
    fun `check getPreviousCampaign() but it throw error`() {
        runBlocking {
            coEvery {
                usecase.execute("A", 1)
            } throws MessageErrorException()
            viewModel.getPreviousCampaign("A")
            delay(11000L)
            assertTrue(viewModel.relatedCampaignsResult.getOrAwaitValue() is ChooseRelatedCampaignResult.Fail)
        }
    }

    @Test
    fun `check setSelectedCampaigns`() {
        val input = buildRelatedCampaignItems()
        viewModel.setSelectedCampaigns(input)
        val actualResult = viewModel.getRelatedCampaignsSelected()
        assertEquals(10, actualResult.size)
    }

    @Test
    fun `check is 2nd object is selected when do map on get Previous Campaign`() {
        runBlocking {
            viewModel.setCampaignId(1)
            viewModel.setSelectedCampaigns(buildRelatedCampaignItemSelected())
            coEvery {
                usecase.execute("A", 1)
            } returns arrayListOf(
                buildCampaignUiModel(1),
                buildCampaignUiModel(2),
                buildCampaignUiModel(3)
            )
            viewModel.getPreviousCampaign("A")
            delay(11000L)
            val result =
                viewModel.relatedCampaignsResult.getOrAwaitValue() as ChooseRelatedCampaignResult.Success
            assertTrue(result.relatedCampaigns[1].isSelected)
        }
    }

    @Test
    fun `when getRelatedCampaignsSelected is empty`() {
        assertTrue(viewModel.getRelatedCampaignsSelected().isEmpty())
    }

    @Test
    fun `check onCampaignClicked when getPreviousCampaign is failed`() {
        val inputItem = RelatedCampaignItem(
            id = 2.toLong(),
            name = "Test_2",
            isMaxSelected = false,
            isSelected = false
        )
        runBlocking {
            viewModel.setCampaignId(1)
            coEvery {
                usecase.execute("A", 1)
            } throws MessageErrorException()
            viewModel.onCampaignClicked(inputItem)
        }
    }

    @Test
    fun `check onCampaignClicked when trying to add new campaign and list selected campaign already set`() {
        val inputItem = RelatedCampaignItem(
            id = 2.toLong(),
            name = "Test_2",
            isMaxSelected = false,
            isSelected = false
        )
        val alreadySelectedItem = RelatedCampaignItem(
            id = 1.toLong(),
            name = "Test_1",
            isMaxSelected = false,
            isSelected = true
        )
        runBlocking {
            viewModel.setCampaignId(1)
            viewModel.setSelectedCampaigns(arrayListOf(alreadySelectedItem))
            coEvery {
                usecase.execute("A", 1)
            } returns arrayListOf(
                buildCampaignUiModel(1),
                buildCampaignUiModel(2),
                buildCampaignUiModel(3)
            )
            viewModel.getPreviousCampaign("A")
            delay(11000L)
            viewModel.onCampaignClicked(inputItem)
            val actualSelectedItem = viewModel.getRelatedCampaignsSelected()
            assertEquals(actualSelectedItem.size, 2)
            assertEquals(inputItem.name, actualSelectedItem.get(1).name)
        }
    }

    @Test
    fun `check onCampaignClicked when trying to add new campaign and list selected campaign not set yet`() {
        val inputItem = RelatedCampaignItem(
            id = 2.toLong(),
            name = "Test_2",
            isMaxSelected = false,
            isSelected = false
        )
        runBlocking {
            viewModel.setCampaignId(1)
            coEvery {
                usecase.execute("A", 1)
            } returns arrayListOf(
                buildCampaignUiModel(1),
                buildCampaignUiModel(2),
                buildCampaignUiModel(3)
            )
            viewModel.getPreviousCampaign("A")
            delay(11000L)
            viewModel.onCampaignClicked(inputItem)
            val actualSelectedItem = viewModel.getRelatedCampaignsSelected()
            assertEquals(actualSelectedItem.get(0).id, inputItem.id)
        }
    }

    @Test
    fun `check onCampaignClicked when campaign to unselected and selected item is empty`() {
        val inputItem = RelatedCampaignItem(
            id = 2.toLong(),
            name = "Test_2",
            isMaxSelected = false,
            isSelected = true
        )
        val alreadySelectedItem = RelatedCampaignItem(
            id = 2.toLong(),
            name = "Test_2",
            isMaxSelected = false,
            isSelected = true
        )
        runBlocking {
            viewModel.setCampaignId(1)
            viewModel.setSelectedCampaigns(arrayListOf(alreadySelectedItem))
            coEvery {
                usecase.execute("A", 1)
            } returns arrayListOf(
                buildCampaignUiModel(1),
                buildCampaignUiModel(2),
                buildCampaignUiModel(3)
            )
            viewModel.getPreviousCampaign("A")
            delay(11000L)
            viewModel.onCampaignClicked(inputItem)
            val actualSelectedItem = viewModel.getRelatedCampaignsSelected()
            assertEquals(actualSelectedItem.size, 0)
        }
    }

    @Test
    fun `check onCampaignClicked when campaign to unselected and selected item is two item `() {
        val inputItem = RelatedCampaignItem(
            id = 2.toLong(),
            name = "Test_2",
            isMaxSelected = false,
            isSelected = true
        )

        runBlocking {
            viewModel.setCampaignId(1)
            viewModel.setSelectedCampaigns(buildRelatedCampaignItems())
            coEvery {
                usecase.execute("A", 1)
            } returns arrayListOf(
                buildCampaignUiModel(1),
                buildCampaignUiModel(2),
                buildCampaignUiModel(3)
            )
            viewModel.getPreviousCampaign("A")
            delay(11000L)
            viewModel.onCampaignClicked(inputItem)
            val actualSelectedItem = viewModel.getRelatedCampaignsSelected()
            assertEquals(actualSelectedItem.size, 9)
        }
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

    private fun buildRelatedCampaignItems(): List<RelatedCampaignItem> {
        val list = arrayListOf<RelatedCampaignItem>()
        for (i in 1..10) {
            list.add(
                RelatedCampaignItem(
                    id = i.toLong(),
                    name = "Test_$i",
                    isMaxSelected = i % 2 == 0,
                    isSelected = false
                )
            )
        }
        return list
    }

    private fun buildRelatedCampaignItemSelected(): List<RelatedCampaignItem> {
        val list = arrayListOf<RelatedCampaignItem>()
        list.add(
            RelatedCampaignItem(
                id = 2.toLong(),
                name = "Test_2",
                isMaxSelected = true,
                isSelected = true
            )
        )
        return list
    }
}
