package com.tokopedia.tkpd.feed_component.browse.testcase

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.test.util.click
import com.tokopedia.content.test.util.clickItemRecyclerView
import com.tokopedia.content.test.util.pressBack
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.di.FeedBrowseInjector
import com.tokopedia.feedplus.browse.presentation.FeedSearchResultActivity
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.tkpd.feed_component.browse.container.FeedBrowseTestActivity
import com.tokopedia.tkpd.feed_component.browse.container.FeedBrowseTestActivityAction
import com.tokopedia.tkpd.feed_component.browse.di.DaggerFeedBrowseTestComponent
import com.tokopedia.tkpd.feed_component.browse.di.FeedBrowseDataTestModule
import com.tokopedia.tkpd.feed_component.builder.FeedBrowseModelBuilder
import com.tokopedia.tkpd.feed_component.helper.FeedCassavaHelper
import com.tokopedia.tkpd.feed_component.helper.delay
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import com.tokopedia.globalerror.R as globalerrorR

/**
 * Created by Jonathan Darwin on 01 April 2024
 */
@CassavaTest
class FeedSearchResultAnalyticTest {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val cassavaValidator = FeedCassavaHelper("tracker/content/feed/feed_browse.json", cassavaTestRule)

    private val context = InstrumentationRegistry.getInstrumentation().context

    private val modelBuilder = FeedBrowseModelBuilder()

    private val mockRepository: FeedBrowseRepository = mockk(relaxed = true)

    private val mockSearchKeyword = "pokemon"
    private val mockTitle = "Mock Title"
    private val mockChannelList = modelBuilder.buildChannelList()

    init {
        FeedBrowseInjector.set(
            DaggerFeedBrowseTestComponent.builder()
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
                .feedBrowseDataTestModule(FeedBrowseDataTestModule(mockRepository))
                .build()
        )
    }

    @Test
    fun `feedSearchResult_analytic`() {

        mockGetWidgetContentSlotResponse(isSuccess = false)

        launchTestActivity()

        delay(1000)

        mockGetWidgetContentSlotResponse(isSuccess = true)

        click(globalerrorR.id.globalerrors_action)

        verify("view - channel card")
        clickItemRecyclerView(R.id.rv_search_result, 1)
        verify("click - channel card")
        pressBack()
        verify("click - back exit page")
    }

    private fun launchTestActivity() {
        FeedBrowseTestActivityAction.onStartActivity = {
            Intent(context, FeedSearchResultActivity::class.java).apply {
                putExtra(FeedSearchResultActivity.KEYWORD_PARAM, mockSearchKeyword)
            }
        }

        ActivityScenario.launch<FeedBrowseTestActivity>(
            Intent(context, FeedBrowseTestActivity::class.java)
        )
    }

    private fun mockGetWidgetContentSlotResponse(isSuccess: Boolean) {
        if (isSuccess) {
            coEvery { mockRepository.getWidgetContentSlot(any()) } returns ContentSlotModel.ChannelBlock(
                title = mockTitle,
                channels = mockChannelList,
                config = PlayWidgetConfigUiModel.Empty,
                nextCursor = "",
            )
        } else {
            coEvery { mockRepository.getWidgetContentSlot(any()) } throws Exception("testing")
        }
    }

    private fun verify(eventAction: String) {
        cassavaValidator.assertCassavaByEventAction(eventAction)
    }
}
