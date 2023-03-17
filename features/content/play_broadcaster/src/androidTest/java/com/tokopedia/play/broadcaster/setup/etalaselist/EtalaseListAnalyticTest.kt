package com.tokopedia.play.broadcaster.setup.etalaselist

import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.helper.contains
import com.tokopedia.play.broadcaster.helper.containsEventAction
import com.tokopedia.play.broadcaster.setup.productSetupViewModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatusUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.test.application.annotations.CassavaTest
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kenny.hadisaputra on 08/03/22
 */
@CassavaTest
@RunWith(AndroidJUnit4ClassRunner::class)
class EtalaseListAnalyticTest {

    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val mockEtalaseList = List(3) {
        EtalaseUiModel(
            id = it.toString(),
            imageUrl = "",
            title = "Etalase $it",
            totalProduct = it * 100,
        )
    }

    private val mockCampaignList = List(3) {
        CampaignUiModel(
            id = it.toString(),
            title = "Campaign $it",
            imageUrl = "",
            startDateFmt = "",
            endDateFmt = "",
            status = CampaignStatusUiModel(
                status = CampaignStatus.Ongoing,
                text = "Berlangsung",
            ),
            totalProduct = it * 100,
        )
    }

    private val analyticFile = "tracker/content/playbroadcaster/play_broadcaster_analytic.json"

    init {
        coEvery { mockRepo.getEtalaseList() } returns mockEtalaseList
        coEvery { mockRepo.getCampaignList() } returns mockCampaignList
    }

    private fun createRobot() = EtalaseListRobot {
        productSetupViewModel(
            productSectionList = emptyList(),
            repo = mockRepo,
        )
    }

    @Test
    fun testAnalytic_clickEtalase() {
        val robot = createRobot()

        robot.selectEtalase()

        ViewMatchers.assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction("click - etalase card")
        )
    }

    @Test
    fun testAnalytic_clickCampaign() {
        val robot = createRobot()

        robot.selectCampaign()

        ViewMatchers.assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction("click - campaign card")
        )
    }
}
