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
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.search.activity.EventCategoryActivity
import com.tokopedia.entertainment.search.adapter.viewholder.CategoryTextBubbleAdapter
import com.tokopedia.entertainment.search.adapter.viewholder.EventGridAdapter
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import mock.CategoryEventMockResponse
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
            gtmLogDBSource.deleteAll().subscribe()
            setupGraphqlMockResponse(CategoryEventMockResponse())
        }
    }

    @Test
    fun validateCategoryTest(){
        Thread.sleep(5000)
        clickCategory()
        impressionProduct()
        clickProduct()
        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ENTERTAINMENT_EVENT_CATEGORY_VALIDATOR_QUERY), hasAllSuccess())

    }

    fun impressionProduct(){
        onView(withId(R.id.recycler_viewParent)).perform(RecyclerViewActions.scrollToPosition<EventGridAdapter.EventGridViewHolder>(7))
        Thread.sleep(3000)
    }

    fun clickProduct(){
        onView(withId(R.id.recycler_viewParent)).perform(RecyclerViewActions.actionOnItemAtPosition<EventGridAdapter.EventGridViewHolder>(0, ViewActions.click()))
        Thread.sleep(3000)
        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack())
    }

    fun clickCategory(){
        onView(withId(R.id.recycler_view_category)).perform(RecyclerViewActions.actionOnItemAtPosition<CategoryTextBubbleAdapter.CategoryTextBubbleViewHolder>(0, ViewActions.click()))
        Thread.sleep(2000)
    }

    companion object {
        private const val ENTERTAINMENT_EVENT_CATEGORY_VALIDATOR_QUERY = "tracker/event/categorypageeventcheck.json"
    }
}