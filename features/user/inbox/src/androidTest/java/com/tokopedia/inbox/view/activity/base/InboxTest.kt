package com.tokopedia.inbox.view.activity.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.config.GlobalConfig
import com.tokopedia.inbox.R
import com.tokopedia.inbox.common.viewaction.clickChildViewWithId
import com.tokopedia.inbox.common.viewmatcher.withTotalItem
import com.tokopedia.inbox.fake.InboxFakeDependency
import com.tokopedia.inbox.fake.di.DaggerFakeInboxComponent
import com.tokopedia.inbox.fake.di.FakeInboxComponent
import com.tokopedia.inbox.fake.di.chat.FakeChatListComponent
import com.tokopedia.inbox.fake.di.common.DaggerFakeBaseAppComponent
import com.tokopedia.inbox.fake.di.common.FakeAppModule
import com.tokopedia.inbox.fake.di.common.FakeBaseAppComponent
import com.tokopedia.inbox.fake.di.notifcenter.FakeNotificationComponent
import com.tokopedia.inbox.fake.view.activity.FakeInboxActivity
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.filter.ChatFilterViewHolder
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class InboxTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        FakeInboxActivity::class.java, false, false
    )

    protected lateinit var activity: FakeInboxActivity

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    protected var inboxDep = InboxFakeDependency()

    @Before
    open fun before() {
        setupGraphqlMockResponse(InboxModelConfig())
        setupDaggerBaseComponent()
        setupInboxDaggerComponent()
        inboxComponent!!.injectMembers(inboxDep)
        inboxDep.init()
    }

    @After
    open fun tearDown() {
        baseComponent = null
        inboxComponent = null
        chatListComponent = null
        notifcenterComponent = null
    }

    private fun setupDaggerBaseComponent() {
        baseComponent = DaggerFakeBaseAppComponent.builder()
            .fakeAppModule(FakeAppModule(applicationContext))
            .build()
    }

    private fun setupInboxDaggerComponent() {
        inboxComponent = DaggerFakeInboxComponent.builder()
            .fakeBaseAppComponent(baseComponent)
            .build()
    }

    protected fun clickFilterPositionAt(position: Int) {
        val viewAction = RecyclerViewActions
            .actionOnItemAtPosition<ChatFilterViewHolder>(
                position,
                clickChildViewWithId(R.id.chips_filter)
            )
        onView(withId(R.id.rv_filter)).perform(viewAction)
    }

    protected fun startInboxActivity(
        isSellerApp: Boolean = false,
        intentModifier: (Intent) -> Unit = {}
    ) {
        if (isSellerApp) {
            GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        }
        val uri = buildIntent().build()
        val intent = Intent().apply {
            data = uri
        }
        intentModifier(intent)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
    }

    protected open fun buildIntent(): Uri.Builder {
        val uriBuilder = Uri.parse(ApplinkConst.INBOX).buildUpon()
        onBuildUri(uriBuilder)
        return uriBuilder
    }

    /**
     * callback when building uri. use it when you want
     * to modify/update the uri (ex. query) on child class
     */
    abstract fun onBuildUri(uriBuilder: Uri.Builder)

    protected fun waitForIt(timeMillis: Long) {
        Thread.sleep(timeMillis)
    }

    companion object {
        var baseComponent: FakeBaseAppComponent? = null
        var inboxComponent: FakeInboxComponent? = null
        var chatListComponent: FakeChatListComponent? = null
        var notifcenterComponent: FakeNotificationComponent? = null
    }
}

/**
 * Dummy model config for query notification from
 * [com.tokopedia.searchbar.navigation_component.NavToolbar]
 * prevent it from requesting to the internet
 */
class InboxModelConfig : MockModelConfig() {
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
            "query Notification", "", FIND_BY_CONTAINS
        )
        return this
    }
}

object InboxAssertion {
    fun assertTotalGlobalNavIcon(totalItemMenu: Int) {
        onView(withId(R.id.rv_icon_list))
            .check(matches(withTotalItem(totalItemMenu)))
    }

    fun assertBottomNavShadowVisibility(
        viewMatcher: Matcher<in View>
    ) {
        onView(withId(R.id.bottom_nav_top_shadow)).check(matches(viewMatcher))
    }

    fun assertBottomNavVisibility(
        viewMatcher: Matcher<in View>
    ) {
        onView(withId(R.id.inbox_bottom_nav)).check(matches(viewMatcher))
    }

    fun assertTotalSwitchRoleCounter(text: String) {
        onView(withId(R.id.unread_header_counter)).check(
            matches(withText(text))
        )
    }
}
