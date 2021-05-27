package com.tokopedia.inbox.view.activity.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.config.GlobalConfig
import com.tokopedia.inbox.R
import com.tokopedia.inbox.common.viewaction.clickChildViewWithId
import com.tokopedia.inbox.fake.InboxFakeDependency
import com.tokopedia.inbox.fake.di.DaggerFakeInboxComponent
import com.tokopedia.inbox.fake.di.FakeInboxComponent
import com.tokopedia.inbox.fake.di.chat.FakeChatListComponent
import com.tokopedia.inbox.fake.di.common.DaggerFakeBaseAppComponent
import com.tokopedia.inbox.fake.di.common.FakeAppModule
import com.tokopedia.inbox.fake.di.common.FakeBaseAppComponent
import com.tokopedia.inbox.fake.view.activity.FakeInboxActivity
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.filter.ChatFilterViewHolder
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

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

    @Inject
    protected lateinit var inboxDep: InboxFakeDependency

    @Before
    open fun before() {
        setupGraphqlMockResponse(InboxModelConfig())
        setupDaggerBaseComponent()
        setupInboxDaggerComponent()
        inboxComponent!!.inject(this)
    }

    @After
    fun tearDown() {
        baseComponent = null
        inboxComponent = null
        chatListComponent = null
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
        Espresso.onView(ViewMatchers.withId(R.id.rv_filter)).perform(viewAction)
    }

    protected fun startInboxActivity(
            isSellerApp: Boolean = false,
            intentModifier: (Intent) -> Unit = {}
    ) {
        val uri = buildIntent().build()
        val intent = Intent().apply {
            data = uri
        }
        intentModifier(intent)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
        if (isSellerApp) {
            GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        }
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
