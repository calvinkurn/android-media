import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
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
import com.tokopedia.entertainment.search.activity.EventCategoryActivity
import com.tokopedia.entertainment.search.adapter.viewholder.CategoryTextBubbleAdapter
import com.tokopedia.entertainment.search.adapter.viewholder.EventGridAdapter
import kotlinx.android.synthetic.main.ent_search_fragment.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CategoryEventActivityTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule: ActivityTestRule<EventCategoryActivity> = object : IntentsTestRule<EventCategoryActivity>(EventCategoryActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
            val intent = Intent(targetContext, EventCategoryActivity::class.java).apply {
                putExtra("category_id", "")
                putExtra("id_city","80724")
                putExtra("query_text", "Hong Kong")
            }
            return intent
        }

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
    fun validateCategoryTest(){
        Thread.sleep(5000)
        clickProduct()
        clickCategory()
        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ENTERTAINMENT_EVENT_CATEGORY_VALIDATOR_QUERY), hasAllSuccess())

    }

    fun clickProduct(){
        onView(withId(R.id.recycler_viewParent)).perform(RecyclerViewActions.actionOnItemAtPosition<EventGridAdapter.EventGridViewHolder>(0, ViewActions.click()))
        Thread.sleep(5000)
        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack())
    }

    fun clickCategory(){
        onView(withId(R.id.recycler_view_category)).perform(RecyclerViewActions.actionOnItemAtPosition<CategoryTextBubbleAdapter.CategoryTextBubbleViewHolder>(0, ViewActions.click()))
        Thread.sleep(2000)
    }

    companion object {
        private const val ENTERTAINMENT_EVENT_CATEGORY_VALIDATOR_QUERY = "tracker/event/categorypageevent.json"
    }
}