import android.app.Instrumentation
import android.content.Intent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.activity.EventPDPActivity
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import mock.PDPEventMockResponse
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test

class PDPEventActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule: IntentsTestRule<EventPDPActivity> =
            object : IntentsTestRule<EventPDPActivity>(EventPDPActivity::class.java) {
                override fun getActivityIntent(): Intent {
                    val seo = "bali-zoo-30995"
                    val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
                    val intent = Intent(targetContext, EventPDPActivity::class.java).apply {
                        putExtra(EventPDPActivity.EXTRA_URL_PDP, seo)
                    }
                    return intent
                }

                override fun beforeActivityLaunched() {
                    super.beforeActivityLaunched()
                    gtmLogDBSource.deleteAll().subscribe()
                    setupGraphqlMockResponse(PDPEventMockResponse())
                }
            }


    @Test
    fun validatePDPEvent() {
        Thread.sleep(5000)
        click_lanjutkan_ticket()
        click_check_ticket()
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ENTERTAINMENT_EVENT_PDP_VALIDATOR_QUERY), hasAllSuccess())

    }

    fun click_lanjutkan_ticket() {
        val viewInteraction = onView(withId(R.id.btn_event_pdp_cek_tiket))
        viewInteraction.perform(click())

    }

    fun click_check_ticket() {
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(0, null))

        onView(getElementFromMatchAtPosition(withText("23"), 0)).check(matches(isDisplayed()))
        onView(getElementFromMatchAtPosition(withText("23"), 0)).perform(click())
    }

    private fun getElementFromMatchAtPosition(
            matcher: Matcher<View>,
            position: Int
    ): Matcher<View?>? {
        return object : BaseMatcher<View?>() {
            var counter = 0
            override fun matches(item: Any): Boolean {
                if (matcher.matches(item)) {
                    if (counter == position) {
                        counter++
                        return true
                    }
                    counter++
                }
                return false
            }
            override fun describeTo(description: Description) {
                description.appendText("Element at hierarchy position $position")
            }
        }
    }


    companion object {
        private const val ENTERTAINMENT_EVENT_PDP_VALIDATOR_QUERY = "tracker/event/pdpeventcheck.json"
    }
}