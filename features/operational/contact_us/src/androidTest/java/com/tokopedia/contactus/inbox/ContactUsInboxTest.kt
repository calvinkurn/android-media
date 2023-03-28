package com.tokopedia.contactus.inbox

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inbox.di.DaggerTestAppComponentInbox
import com.tokopedia.contactus.inbox.di.FakeAppInboxModule
import com.tokopedia.contactus.inbox.di.FakeGraphqlRepositoryInbox
import com.tokopedia.contactus.inboxtickets.view.inbox.InboxContactUsActivity
import com.tokopedia.contactus.utils.InstrumentedTestUtil.scrollRecyclerViewToPosition
import com.tokopedia.contactus.utils.InstrumentedTestUtil.clickOnPosition
import com.tokopedia.contactus.utils.InstrumentedTestUtil.isGoneView
import com.tokopedia.contactus.utils.InstrumentedTestUtil.isVisible
import com.tokopedia.contactus.utils.InstrumentedTestUtil.performClick
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class ContactUsInboxTest {

    @get: Rule
    var mActivityTestRule = ActivityTestRule(InboxContactUsActivity::class.java, false, false)

    lateinit var fakeGqlRepository: FakeGraphqlRepositoryInbox

    @Before
    fun setUp() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val component = DaggerTestAppComponentInbox.builder().fakeAppInboxModule(FakeAppInboxModule(ctx)).build()
        fakeGqlRepository = component.fakeGraphqlRepository() as FakeGraphqlRepositoryInbox
        ApplicationProvider.getApplicationContext<BaseMainApplication>().setComponent(component)
        login()
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
    }

    private fun waitForData(longToWaint : Long = 1000L) {
        Thread.sleep(longToWaint)
    }

    @Test
    fun successfulApiTicketList_showingListTicket() {
        mActivityTestRule.launchActivity(Intent())
        waitForData()
        isVisible(R.id.rv_email_list)
        isGoneView(R.id.home_global_error)
    }

    @Test
    fun failedApiTicektList_showingErrorState() {
        fakeGqlRepository.setResponseTypeForError(true)
        mActivityTestRule.launchActivity(Intent())
        isGoneView(R.id.rv_email_list)
        isVisible(R.id.home_global_error)
    }

    @Test
    fun errorAPIAndClickRetry_showListTicket() {
        fakeGqlRepository.setResponseTypeForError(true)
        mActivityTestRule.launchActivity(Intent())
        isGoneView(R.id.rv_email_list)
        isVisible(R.id.home_global_error)
        fakeGqlRepository.setResponseTypeForError(false)
        performClick(R.id.globalerrors_action)
        isVisible(R.id.rv_email_list)
        isGoneView(R.id.home_global_error)
    }

    @Test
    fun successfulReqTopChat_showingChatWidget() {
        fakeGqlRepository.setResponseTypeForError(false)
        mActivityTestRule.launchActivity(Intent())
        waitForData(1500L)
        isVisible(R.id.chat_widget)
    }

    @Test
    fun failedReqTopChat_hideChatWidget() {
        fakeGqlRepository.setResponseTypeForError(true)
        mActivityTestRule.launchActivity(Intent())
        waitForData(1500L)
        isGoneView(R.id.chat_widget)
    }


}
