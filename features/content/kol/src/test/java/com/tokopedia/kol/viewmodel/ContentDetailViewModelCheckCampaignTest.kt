package com.tokopedia.kol.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kol.common.util.ContentDetailResult
import com.tokopedia.kol.feature.postdetail.domain.ContentDetailRepository
import com.tokopedia.kol.feature.postdetail.view.viewmodel.ContentDetailViewModel
import com.tokopedia.kol.model.ContentDetailModelBuilder
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.Rule
import org.junit.Test

/**
 * Created by shruti.agarwal on 22/09/22.
 */
class ContentDetailViewModelCheckCampaignTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: ContentDetailRepository = mockk(relaxed = true)
    private val viewModel = ContentDetailViewModel(
        testDispatcher,
        mockRepo,
    )

    private val campaignId = 1234L
    private val builder = ContentDetailModelBuilder()

    @Test
    fun `when user impress asgc upcoming campaign widget, given response success, check if user has reminded, return true`() {
        val rowNumber = 0
        val isAvailable = true
        val expectedResult = builder.getCheckCampaignResponse(campaignId, rowNumber, isAvailable)

        coEvery { mockRepo.checkUpcomingCampaign(campaignId) } returns isAvailable

        viewModel.checkUpcomingCampaignInitialReminderStatus(campaignId, rowNumber)

        val result = viewModel.asgcReminderButtonInitialStatus.value

        Assertions
            .assertThat(result)
            .isEqualTo(ContentDetailResult.Success(expectedResult))
    }

    @Test
    fun `when user impress asgc upcoming campaign widget, given response success, check if user has not reminded, return false`() {
        val rowNumber = 0
        val isAvailable = false
        val expectedResult = builder.getCheckCampaignResponse(campaignId, rowNumber, isAvailable)

        coEvery { mockRepo.checkUpcomingCampaign(campaignId) } returns isAvailable

        viewModel.checkUpcomingCampaignInitialReminderStatus(campaignId, rowNumber)

        val result = viewModel.asgcReminderButtonInitialStatus.value

        Assertions
            .assertThat(result)
            .isEqualTo(ContentDetailResult.Success(expectedResult))
    }

    @Test
    fun `when user impress asgc upcoming campaign widget, given response error, returns error`() {
        val rowNumber = 0

        coEvery { mockRepo.checkUpcomingCampaign(campaignId) } throws Throwable()

        viewModel.checkUpcomingCampaignInitialReminderStatus(campaignId, rowNumber)

        val result = viewModel.asgcReminderButtonInitialStatus.value

        Assertions
            .assertThat(result)
            .isInstanceOf(ContentDetailResult.Failure::class.java)
    }
}