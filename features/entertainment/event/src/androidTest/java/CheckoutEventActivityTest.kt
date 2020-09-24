import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.activity.EventCheckoutActivity
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventPDPTextFieldViewHolder
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import data.MockMetaData
import mock.CheckoutEventMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CheckoutEventActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule: IntentsTestRule<EventCheckoutActivity> =
            object : IntentsTestRule<EventCheckoutActivity>(EventCheckoutActivity::class.java) {
                override fun getActivityIntent(): Intent {
                    val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
                    val intent = Intent(targetContext, EventCheckoutActivity::class.java).apply {
                        putExtra(EventCheckoutActivity.EXTRA_URL_PDP, "7-day-jr-all-shikoku-pass-23233")
                        putExtra(EventCheckoutActivity.EXTRA_META_DATA, MockMetaData.getMetaDataResponse())
                        putExtra(EventCheckoutActivity.EXTRA_PACKAGE_ID, "2104")
                    }
                    return intent
                }

                override fun beforeActivityLaunched() {
                    super.beforeActivityLaunched()
                    gtmLogDBSource.deleteAll().subscribe()
                    setupGraphqlMockResponse(CheckoutEventMockResponse())

                }
            }

    @Before
    fun setup() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    @Test
    fun validateCheckoutEvent() {
        Thread.sleep(5000)
        onView(withId(R.id.tg_event_checkout_widget_pessanger_name)).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<EventPDPTextFieldViewHolder>(0, typeText("Firmanda Mulyawan Nugroho")))
        Thread.sleep(3000)
        onView(withId(R.id.simpanBtn)).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.rv_event_checkout_additional)).perform(RecyclerViewActions.actionOnItemAtPosition<EventPDPTextFieldViewHolder>(0, click()))
        Thread.sleep(9000)
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<EventPDPTextFieldViewHolder>(0, typeText("Firmanda Mulyawan Nugroho")))
        Thread.sleep(3000)
        onView(withId(R.id.simpanBtn)).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.item_checkout_event_data_tambahan_package)).perform(click())
        Thread.sleep(9000)
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<EventPDPTextFieldViewHolder>(0, typeText("085327499272")))
        Thread.sleep(3000)
        onView(withId(R.id.simpanBtn)).perform(click())
        Thread.sleep(3000)
        onView(withId(android.R.id.content)).perform(ViewActions.swipeUp())
        Thread.sleep(3000)
        onView(withId(R.id.cb_event_checkout)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.btn_event_checkout)).perform(click())

        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ENTERTAINMENT_EVENT_CHECKOUT_VALIDATOR_QUERY), hasAllSuccess())
    }

    companion object {
        private const val ENTERTAINMENT_EVENT_CHECKOUT_VALIDATOR_QUERY = "tracker/event/checkouteventcheck.json"
    }
}
