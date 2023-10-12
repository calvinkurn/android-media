package com.tokopedia.contactus.inboxdetail

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxdetail.di.DaggerTestAppComponentInboxDetail
import com.tokopedia.contactus.inboxdetail.di.FakeAppInboxDetailModule
import com.tokopedia.contactus.inboxdetail.di.FakeGraphqlRepositoryInboxDetail
import com.tokopedia.contactus.inboxdetail.di.FakeGraphqlRepositoryInboxDetail.TypeOfStatusTicket.*
import com.tokopedia.contactus.inboxtickets.view.activity.ContactUsProvideRatingActivity
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailActivity
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailActivity.Companion.BUNDLE_ID_TICKET
import com.tokopedia.contactus.initRulePage
import com.tokopedia.contactus.utils.InstrumentedTestUtil.viewChildPerformClick
import com.tokopedia.contactus.utils.InstrumentedTestUtil.isShowToaster
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class ContactUsInboxDetail {

    @get: Rule
    var mActivityTestRule = ActivityTestRule(InboxDetailActivity::class.java, false, false)

    @get: Rule
    var otherView = ActivityTestRule(ContactUsProvideRatingActivity::class.java, false, false)

    lateinit var fakeGqlRepository: FakeGraphqlRepositoryInboxDetail
    private val bundle = Bundle().apply {
        putString(BUNDLE_ID_TICKET, "1233")
        putBoolean(InboxDetailActivity.IS_OFFICIAL_STORE, true)
    }
    val intent = Intent().apply {
        putExtras(bundle)
    }

    @Before
    fun setUp() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val component = DaggerTestAppComponentInboxDetail.builder().fakeAppInboxDetailModule(
            FakeAppInboxDetailModule(ctx)
        ).build()
        fakeGqlRepository = component.fakeGraphqlRepository() as FakeGraphqlRepositoryInboxDetail
        ApplicationProvider.getApplicationContext<BaseMainApplication>().setComponent(component)
        login()
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
    }

    @Test
    fun ticketIsOnProgress_layoutReplayIsVisible() {
        initRulePage {
            fakeGqlRepository.setTypeOfTicket(ON_PROGRESS)
        }.launchPage {
            launchDetailInboxPage(mActivityTestRule, intent)
        }.verify {
            R.id.rv_message_list.isVisible()
            R.id.layout_replay_message.isVisible()
        }
    }

    @Test
    fun agentReplayChat_ButtonReplayVisible() {
        var lastPosition = 0
        var recyclerView : RecyclerView? = null
        initRulePage {
            fakeGqlRepository.setTypeOfTicket(ON_PROGRESS_NEED_TO_REPLAY)
        }.launchPage {
            launchDetailInboxPage(mActivityTestRule, intent)
        }.doing {
            recyclerView = findRecyclerViewWithId(R.id.rv_message_list, onActivityRule = mActivityTestRule)
            lastPosition = recyclerView.getSize() - 1
            recyclerView.clickOnPosition(lastPosition)
        }.verify {
            R.id.iv_csast_status_bad.isVisibleInPosition(lastPosition, onRecyclerView = recyclerView)
            R.id.tv_name.isTextShowedInPosition(lastPosition, withText = "Tim Tokopedia Care", onRecyclerView = recyclerView)
            R.id.view_rply_botton_before_csat_rating.isVisible()
        }
    }

    @Test
    fun ticketIsOnClosed_cannotClickReplayOrRating() {
        initRulePage {
            fakeGqlRepository.setTypeOfTicket(CLOSE)
        }.launchPage {
            launchDetailInboxPage(mActivityTestRule, intent)
        }.verify {
            R.id.rv_message_list.isVisible()
            R.id.view_link_bottom.isVisible()
            R.id.layout_replay_message.isGone()
            R.id.view_help_rate.isGone()
        }
    }

    @Test
    fun ticketIsNeedRating_viewReplayIsGoneAndViewHelpRateVisible() {
        initRulePage {
            fakeGqlRepository.setTypeOfTicket(NEED_RATING)
        }.launchPage {
            launchDetailInboxPage(mActivityTestRule, intent)
        }.verify {
            R.id.rv_message_list.isVisible()
            R.id.view_help_rate.isVisible()
            R.id.layout_replay_message.isGone()
        }
    }

    @Test
    fun failedGetDetailTicket_showToasterError() {
        initRulePage {
            fakeGqlRepository.setResponseTypeNotHappy(true)
        }.launchPage {
            launchDetailInboxPage(mActivityTestRule, Intent())
        }.verify {
            R.id.rv_message_list.isGone()
        }
    }

    @Test
    fun sendNegativeCSATPerReplaySuccess_showOnlyThumbDownOnLastReplay() {
        var lastItem = 0
        var recyclerView : RecyclerView? = null
        initRulePage {
            fakeGqlRepository.setTypeOfTicket(ON_PROGRESS_NEED_TO_REPLAY)
        }.launchPage {
            launchDetailInboxPage(mActivityTestRule, intent)
        }.doing {
            recyclerView = findRecyclerViewWithId(R.id.rv_message_list, onActivityRule = mActivityTestRule)
        }.verify {
            R.id.rv_message_list.isVisible()
            R.id.layout_replay_message.isVisible()
            R.id.view_rply_botton_before_csat_rating.isVisible()
        }.doing {
            click(R.id.tv_reply_button)
        }.verify {
            R.id.tv_no_button.isVisible()
            R.id.tv_yes_button.isVisible()
        }.doing {
            click(R.id.tv_no_button)
            lastItem = recyclerView.getSize() - 1
            recyclerView.scrollToPosition(lastItem, mActivityTestRule)
        }.verify {
            recyclerView.isHaveView(R.id.iv_csast_status_bad, onPosition = lastItem)
            recyclerView.isNotHaveView(R.id.iv_csast_status_good, onPosition = lastItem)
        }
    }

    @Test
    fun sendPositiveCSATPerReplaySuccess_showCloseTicketDialog() {
        initRulePage {
            fakeGqlRepository.setTypeOfTicket(ON_PROGRESS_NEED_TO_REPLAY)
        }.launchPage {
            launchDetailInboxPage(mActivityTestRule, intent)
        }.verify {
            R.id.rv_message_list.isVisible()
            R.id.layout_replay_message.isVisible()
            R.id.view_rply_botton_before_csat_rating.isVisible()
        }.doing {
            click(R.id.tv_reply_button)
        }.verify {
            R.id.tv_no_button.isVisible()
            R.id.tv_yes_button.isVisible()
        }.doing {
            click(R.id.tv_yes_button)
        }.verify {
            "Tutup komplain ini?".isVisible()
            R.id.tv_no_button.isVisible()
            R.id.tv_yes_button.isVisible()
        }
    }

    @Test
    fun failedSendCSATPerReplay_showToasErrorMessage() {
        initRulePage {
            fakeGqlRepository.setTypeOfTicket(ON_PROGRESS_NEED_TO_REPLAY)
        }.launchPage {
            launchDetailInboxPage(mActivityTestRule, intent)
        }.verify {
            R.id.rv_message_list.isVisible()
            R.id.layout_replay_message.isVisible()
            R.id.view_rply_botton_before_csat_rating.isVisible()
        }.doing {
            click(R.id.tv_reply_button)
        }.verify {
            R.id.tv_no_button.isVisible()
            R.id.tv_yes_button.isVisible()
        }.doing {
            fakeGqlRepository.setResponseTypeNotHappy(true)
            click(R.id.tv_no_button)
        }.verify {
            isShowToaster("Failed to send Rating")
        }
    }

    @Test
    fun failedToCloseTicket_shownErrorMessage() {
        initRulePage {
            fakeGqlRepository.setTypeOfTicket(ON_PROGRESS_NEED_TO_REPLAY)
        }.launchPage {
            launchDetailInboxPage(mActivityTestRule, intent)
        }.verify {
            R.id.rv_message_list.isVisible()
            R.id.layout_replay_message.isVisible()
            R.id.view_rply_botton_before_csat_rating.isVisible()
        }.doing {
            click(R.id.tv_reply_button)
        }.verify {
            R.id.tv_no_button.isVisible()
            R.id.tv_yes_button.isVisible()
        }.doing {
            click(R.id.tv_yes_button)
        }.verify {
            R.id.tv_no_button.isVisible()
            R.id.tv_yes_button.isVisible()
        }.doing {
            fakeGqlRepository.setResponseTypeNotHappy(true)
            click(R.id.tv_yes_button)
        }.verify {
            isShowToaster("Failed close user")
        }
    }

    @Test
    fun successfulCloseAndCSATTicket_helpRateAndReplayViewAreGone() {
        initRulePage {
            fakeGqlRepository.setTypeOfTicket(ON_PROGRESS_NEED_TO_REPLAY)
        }.launchPage {
            launchDetailInboxPage(mActivityTestRule, intent)
        }.verify {
            R.id.rv_message_list.isVisible()
            R.id.layout_replay_message.isVisible()
            R.id.view_rply_botton_before_csat_rating.isVisible()
        }.doing {
            click(R.id.tv_reply_button)
        }.verify {
            R.id.tv_no_button.isVisible()
            R.id.tv_yes_button.isVisible()
        }.doing {
            click(R.id.tv_yes_button)
        }.verify {
            R.id.tv_no_button.isVisible()
            R.id.tv_yes_button.isVisible()
        }.doing {
            click(R.id.tv_yes_button)
        }.verify {
            R.id.smile_layout.isVisible()
        }.doing {
            viewChildPerformClick(R.id.smile_layout, 2)
        }.verify {
            "Apa yang bisa ditingkatkan dari layanan kami?".isVisible()
            R.id.filter_review.isVisible()
        }.doing {
            viewChildPerformClick(R.id.rv_filter, 0)
            click(R.id.txt_finished)
        }.verify {
            R.id.view_link_bottom.isVisible()
            R.id.layout_replay_message.isGone()
            R.id.view_help_rate.isGone()
        }
    }

    @Test
    fun closeTicketAreSuccessButCSATSendFailed_stillShownReplayBox() {
        initRulePage {
            fakeGqlRepository.setTypeOfTicket(ON_PROGRESS_NEED_TO_REPLAY)
        }.launchPage {
            launchDetailInboxPage(mActivityTestRule, intent)
        }.verify {
            R.id.rv_message_list.isVisible()
            R.id.layout_replay_message.isVisible()
            R.id.view_rply_botton_before_csat_rating.isVisible()
        }.doing {
            click(R.id.tv_reply_button)
        }.verify {
            R.id.tv_no_button.isVisible()
            R.id.tv_yes_button.isVisible()
        }.doing {
            click(R.id.tv_yes_button)
        }.verify {
            R.id.tv_no_button.isVisible()
            R.id.tv_yes_button.isVisible()
        }.doing {
            click(R.id.tv_yes_button)
        }.verify {
            R.id.smile_layout.isVisible()
        }.doing {
            viewChildPerformClick(R.id.smile_layout, 2)
        }.verify {
            "Apa yang bisa ditingkatkan dari layanan kami?".isVisible()
            R.id.filter_review.isVisible()
        }.doing {
            viewChildPerformClick(R.id.rv_filter, 0)
            fakeGqlRepository.setResponseTypeNotHappy(true)
            click(R.id.txt_finished)
            waitForData(200)
        }.verify {
            R.id.iv_send_button.isVisible()
            R.id.ed_message.isVisible()
            R.id.iv_upload_img.isVisible()
        }
    }
}
