import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.search.activity.EventSearchActivity
import com.tokopedia.entertainment.search.adapter.SearchEventViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchEventListViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchLocationListViewHolder
import kotlinx.android.synthetic.main.ent_search_fragment.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchEventActivityTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule: ActivityTestRule<EventSearchActivity> = object : IntentsTestRule<EventSearchActivity>(EventSearchActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            //setupGraphqlMockResponse(HomeEventMockResponse())
        }
    }

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    @Test
    fun validateSearchTest(){
        Thread.sleep(5000)
        search_keyword()
        click_city()
        click_event()

        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ENTERTAINMENT_EVENT_SEARCH_VALIDATOR_QUERY), hasAllSuccess())

    }

    fun search_keyword(){
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click())
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(typeText("Hong Kong"))
        Thread.sleep(5000)
    }

    fun click_city(){
        onView(withId(R.id.recycler_view_location)).perform(RecyclerViewActions.actionOnItemAtPosition<SearchLocationListViewHolder>(0, click()))
        Thread.sleep(3000)
        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack())
    }

    fun click_event(){
        onView(withId(R.id.recycler_view_kegiatan)).perform(RecyclerViewActions.actionOnItemAtPosition<SearchEventListViewHolder>(0, click()))
        Thread.sleep(3000)
        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack())
    }

    companion object {
        private const val ENTERTAINMENT_EVENT_SEARCH_VALIDATOR_QUERY = "tracker/event/searchpageevent.json"
    }
}