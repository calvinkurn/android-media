import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.activity.EventPDPTicketActivity
import com.tokopedia.entertainment.pdp.adapter.viewholder.PackageParentViewHolder
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PDPTicketEventActivityTest{

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule: IntentsTestRule<EventPDPTicketActivity> =
            object : IntentsTestRule<EventPDPTicketActivity>(EventPDPTicketActivity::class.java) {
                override fun getActivityIntent(): Intent {
                    val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
                    val intent = Intent(targetContext, EventPDPTicketActivity::class.java).apply {
                        putExtra(EventPDPTicketActivity.EXTRA_URL_PDP, "doolive-ala-carte-booking-36116")
                        putExtra(EventPDPTicketActivity.SELECTED_DATE,"1598806800")
                        putExtra(EventPDPTicketActivity.START_DATE,"")
                        putExtra(EventPDPTicketActivity.END_DATE,"")

                    }
                    return intent
                }
            }

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().subscribe()
    }


    @Test
    fun validatePDPTicketEvent() {
        Thread.sleep(5000)
        click_package()
        click_quantity()
        click_beli()

        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ENTERTAINMENT_EVENT_PDP_TICKET_VALIDATOR_QUERY), hasAllSuccess())


    }

    fun click_package(){
        val rvInteraction = onView(withId(R.id.recycler_viewParent)).check(matches(isDisplayed()))
        rvInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<PackageParentViewHolder>(0,click()))
    }

    fun click_quantity(){
        val pilihInteraction = onView(withId(R.id.txtPilih_ticket))
        pilihInteraction.perform(click())

        val addQuantiryinteraction = onView(withId(R.id.quantity_editor_add))
        addQuantiryinteraction.perform(click())
        addQuantiryinteraction.perform(click())

    }

    fun click_beli(){
        onView(withId(R.id.pilihTicketBtn)).perform(click())
    }

    companion object {
        private const val ENTERTAINMENT_EVENT_PDP_TICKET_VALIDATOR_QUERY = "tracker/event/pdpticketeventcheck.json"
    }

}