package com.tokopedia.contactus.inbox

import android.content.Intent
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
import com.tokopedia.contactus.initRulePage
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

    @Test
    fun successfulApiTicketList_showingListTicket() {
        initRulePage{
            launchInboxPage(mActivityTestRule, Intent())
            waitForData()
        }.verify {
            R.id.rv_email_list.isVisible()
            R.id.home_global_error.isGone()
        }
    }

    @Test
    fun failedApiTicektList_showingErrorState() {
        initRulePage{
            fakeGqlRepository.setResponseTypeForError(true)
        }.launchPage {
            launchInboxPage(mActivityTestRule, Intent())
        }.doing {
            waitForData()
        }.verify {
            R.id.rv_email_list.isGone()
            R.id.home_global_error.isVisible()
        }
    }

    @Test
    fun errorAPIAndClickRetry_showListTicket() {
        initRulePage {
            fakeGqlRepository.setResponseTypeForError(true)
        }.launchPage {
            launchInboxPage(mActivityTestRule,Intent())
            fakeGqlRepository.setResponseTypeForError(false)
        }.doing {
            click(com.tokopedia.globalerror.R.id.globalerrors_action)
        }.verify {
            R.id.rv_email_list.isVisible()
            R.id.home_global_error.isGone()
        }
    }

    @Test
    fun successfulReqTopChat_showingChatWidget() {
        initRulePage{
            fakeGqlRepository.setResponseTypeForError(false)
        }.launchPage {
            launchInboxPage(mActivityTestRule,Intent())
        }.doing {
            waitForData(1500L)
        }.verify {
            R.id.chat_widget.isVisible()
        }
    }

    @Test
    fun failedReqTopChat_hideChatWidget() {
        initRulePage {
            fakeGqlRepository.setResponseTypeForError(true)
        }.launchPage {
            launchInboxPage(mActivityTestRule,Intent())
        }.doing {
            waitForData(1500L)
        }.verify {
            R.id.chat_widget.isGone()
        }
    }


}
