import android.content.Intent
import android.view.View
import android.view.View.VISIBLE
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.activity.EventPDPActivity
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import kotlinx.android.synthetic.main.partial_event_pdp_price.*
import mock.PDPEventMockResponse
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.endsWith
import org.hamcrest.core.AllOf
import org.junit.Before
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
                    setupGraphqlMockResponse(PDPEventMockResponse())
                }
            }

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().subscribe()
    }


    @Test
    fun validatePDPEvent() {
        Thread.sleep(5000)
        click_lanjutkan_ticket()
        //click_check_ticket()
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ENTERTAINMENT_EVENT_PDP_VALIDATOR_QUERY), hasAllSuccess())

    }

    fun click_lanjutkan_ticket() {
        val viewInteraction = onView(withId(R.id.btn_event_pdp_cek_tiket))
        viewInteraction.perform(click())

    }

    fun click_check_ticket() {
        val viewCalendarInteraction = onView(allOf(withId(R.id.calendar_grid), hasSibling(withText("Agustus 2020")), withClassName(endsWith("CalendarGridView"))))
        viewCalendarInteraction.perform(click())
    }

    companion object {
        private const val ENTERTAINMENT_EVENT_PDP_VALIDATOR_QUERY = "tracker/event/pdpeventcheck.json"
    }
}