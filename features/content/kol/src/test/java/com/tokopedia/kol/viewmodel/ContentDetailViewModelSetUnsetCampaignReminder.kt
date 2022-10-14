package com.tokopedia.kol.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.feedcomponent.data.feedrevamp.FeedASGCUpcomingReminderStatus
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
 * Created by shruti.adgarwal on 22/09/22.
 */
class ContentDetailViewModelSetUnsetCampaignReminder {

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
    fun `when user click asgc upcoming campaign widget to set reminder, given response success, check if action is of reminder set, it returns FeedAsgcCampaignResponseModel`() {
        val rowNumber = 0
        val isSuccess = true
        val reminderStatus = FeedASGCUpcomingReminderStatus.On(campaignId)
        val expectedResult = builder.getSetUnsetCampaignResponse(campaignId, rowNumber, reminderStatus)

        coEvery {
            mockRepo.subscribeUpcomingCampaign(
                campaignId,
                reminderType = FeedASGCUpcomingReminderStatus.On(campaignId)
            )
        } returns Pair(isSuccess, "")

        viewModel.setUnsetReminder(campaignId, reminderStatus, rowNumber)

        val result = viewModel.asgcReminderButtonStatus.value

        Assertions
            .assertThat(result)
            .isEqualTo(ContentDetailResult.Success(expectedResult))
    }

    @Test
    fun `when user click asgc upcoming campaign widget to unset reminder, given response success, check if action is of reminder unset, it returns FeedAsgcCampaignResponseModel`() {
        val rowNumber = 0
        val isSuccess = true
        val reminderStatus = FeedASGCUpcomingReminderStatus.On(campaignId)
        val expectedResult = builder.getSetUnsetCampaignResponse(campaignId, rowNumber, reminderStatus)

        coEvery {
            mockRepo.subscribeUpcomingCampaign(
                campaignId,
                reminderType = FeedASGCUpcomingReminderStatus.On(campaignId)
            )
        } returns Pair(isSuccess, "")

        viewModel.setUnsetReminder(campaignId, reminderStatus, rowNumber)

        val result = viewModel.asgcReminderButtonStatus.value

        Assertions
            .assertThat(result)
            .isEqualTo(ContentDetailResult.Success(expectedResult))
    }

    @Test
    fun `when user click asgc upcoming campaign widget to set unset reminder, given response error, returns error`() {
        val rowNumber = 0
        val reminderStatus = FeedASGCUpcomingReminderStatus.On(campaignId)

        coEvery {
            mockRepo.subscribeUpcomingCampaign(
                campaignId,
                reminderType = FeedASGCUpcomingReminderStatus.On(campaignId)
            )
        } throws  Throwable()

        viewModel.setUnsetReminder(campaignId, reminderStatus, rowNumber)

        val result = viewModel.asgcReminderButtonStatus.value
        Assertions
            .assertThat(result)
            .isInstanceOf(ContentDetailResult.Failure::class.java)
    }
}
