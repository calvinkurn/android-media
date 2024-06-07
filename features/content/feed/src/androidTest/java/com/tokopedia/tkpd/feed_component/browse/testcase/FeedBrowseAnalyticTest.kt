package com.tokopedia.tkpd.feed_component.browse.testcase

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.test.util.click
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.model.FeedBrowseSlotUiModel
import com.tokopedia.feedplus.browse.data.model.HeaderDetailModel
import com.tokopedia.feedplus.browse.di.FeedBrowseInjector
import com.tokopedia.feedplus.browse.presentation.FeedBrowseActivity
import com.tokopedia.feedplus.browse.presentation.FeedLocalSearchActivity
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.tkpd.feed_component.browse.di.DaggerFeedBrowseTestComponent
import com.tokopedia.tkpd.feed_component.browse.di.FeedBrowseDataTestModule
import com.tokopedia.tkpd.feed_component.helper.FeedCassavaHelper
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.unifycomponents.R as unifycomponentsR

/**
 * Created by Jonathan Darwin on 01 April 2024
 */
@CassavaTest
class FeedBrowseAnalyticTest {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val cassavaValidator = FeedCassavaHelper("tracker/content/feed/feed_browse.json", cassavaTestRule)

    private val context = InstrumentationRegistry.getInstrumentation().context

    private val mockRepository: FeedBrowseRepository = mockk(relaxed = true)

    init {
        FeedBrowseInjector.set(
            DaggerFeedBrowseTestComponent.builder()
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
                .feedBrowseDataTestModule(FeedBrowseDataTestModule(mockRepository))
                .build()
        )
    }

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun clear() {
        Intents.release()
    }

    @Test
    fun should_open_search_page_and_match_the_analytics() {
        mockGetWidgetContentSlotResponse()

        launchActivity()

        click(unifycomponentsR.id.searchbar_textfield)

        Intents.intended(IntentMatchers.hasComponent(FeedLocalSearchActivity::class.java.name))

        cassavaValidator.assertCassavaByEventAction("click - search bar")
    }

    private fun launchActivity() {
        ActivityScenario.launch<FeedBrowseActivity>(
            Intent(context, FeedBrowseActivity::class.java)
        )
    }

    private fun mockGetWidgetContentSlotResponse() {
        coEvery { mockRepository.getSlots() } returns listOf(
            FeedBrowseSlotUiModel.ChannelsWithMenus.Empty
        )

        coEvery { mockRepository.getHeaderDetail() } returns HeaderDetailModel(
            title = "Title test",
            isShowSearchBar = true,
            searchBarPlaceholder = "Search placeholder test",
            applink = "tokopedia-android-internal://feed/search"
        )
    }
}
