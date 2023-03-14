package com.tokopedia.contactus.inboxdetail

import android.content.Intent
import android.os.Bundle
import androidx.core.view.size
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
import com.tokopedia.contactus.utils.InstrumentedTestUtil.scrollRecyclerViewToPosition
import com.tokopedia.contactus.utils.InstrumentedTestUtil.isGoneView
import com.tokopedia.contactus.utils.InstrumentedTestUtil.isTextDisplayed
import com.tokopedia.contactus.utils.InstrumentedTestUtil.isViewGoneInItemPosition
import com.tokopedia.contactus.utils.InstrumentedTestUtil.isViewVisibleInItemPosition
import com.tokopedia.contactus.utils.InstrumentedTestUtil.isVisible
import com.tokopedia.contactus.utils.InstrumentedTestUtil.performClick
import com.tokopedia.contactus.utils.InstrumentedTestUtil.viewChildPerformClick
import com.tokopedia.contactus.utils.InstrumentedTestUtil.viewToaster
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
            FakeAppInboxDetailModule(ctx)).build()
        fakeGqlRepository = component.fakeGraphqlRepository() as FakeGraphqlRepositoryInboxDetail
        ApplicationProvider.getApplicationContext<BaseMainApplication>().setComponent(component)
        login()
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
    }

    @Test
    fun whenGetDetailSuccessWhenTicketIsOnProgress() {
        mActivityTestRule.launchActivity(intent)
        isVisible(R.id.rv_message_list)
        isVisible(R.id.layout_replay_message)
    }

    @Test
    fun whenGetDetailSuccessWhenTicketIsOnProgressAfterAgentReplay() {
        fakeGqlRepository.setTypeOfTicket(ON_PROGRESS_NEED_TO_REPLAY)
        mActivityTestRule.launchActivity(intent)
        isVisible(R.id.rv_message_list)
        isVisible(R.id.layout_replay_message)
        isVisible(R.id.view_rply_botton_before_csat_rating)
    }

    @Test
    fun whenGetDetailSuccessWhenTicketIsOnClosed() {
        fakeGqlRepository.setTypeOfTicket(CLOSE)
        mActivityTestRule.launchActivity(intent)
        isVisible(R.id.rv_message_list)
        isVisible(R.id.view_link_bottom)
        isGoneView(R.id.layout_replay_message)
        isGoneView(R.id.view_help_rate)
    }

    @Test
    fun whenGetDetailSuccessWhenTicketIsNeedRating() {
        fakeGqlRepository.setTypeOfTicket(NEED_RATING)
        mActivityTestRule.launchActivity(intent)
        isVisible(R.id.rv_message_list)
        isVisible(R.id.view_help_rate)
        isGoneView(R.id.layout_replay_message)
    }

    @Test
    fun whenGetDetailFailed() {
        fakeGqlRepository.setResponseTypeNotHappy(true)
        mActivityTestRule.launchActivity(Intent())
        isGoneView(R.id.rv_message_list)
        viewToaster("Something went wrong")
    }

    @Test
    fun sendCSATPerReplayNegativeFeedBack() {
        fakeGqlRepository.setTypeOfTicket(ON_PROGRESS_NEED_TO_REPLAY)
        mActivityTestRule.launchActivity(intent)
        val recyclerView = mActivityTestRule.activity.findViewById<RecyclerView>(R.id.rv_message_list)
        isVisible(R.id.rv_message_list)
        isVisible(R.id.layout_replay_message)
        isVisible(R.id.view_rply_botton_before_csat_rating)
        performClick(R.id.tv_reply_button)
        isVisible(R.id.tv_no_button)
        isVisible(R.id.tv_yes_button)
        performClick(R.id.tv_no_button)
        val itemSize = recyclerView.size-1
        mActivityTestRule.scrollRecyclerViewToPosition(recyclerView, itemSize)
        recyclerView.isViewVisibleInItemPosition(itemSize, R.id.iv_csast_status_bad)
        recyclerView.isViewGoneInItemPosition(itemSize, R.id.iv_csast_status_good)
    }

    @Test
    fun sendCSATPerReplayPositiveFeedBack() {
        fakeGqlRepository.setTypeOfTicket(ON_PROGRESS_NEED_TO_REPLAY)
        mActivityTestRule.launchActivity(intent)
        val recyclerView = mActivityTestRule.activity.findViewById<RecyclerView>(R.id.rv_message_list)
        isVisible(R.id.rv_message_list)
        isVisible(R.id.layout_replay_message)
        isVisible(R.id.view_rply_botton_before_csat_rating)
        performClick(R.id.tv_reply_button)
        isVisible(R.id.tv_no_button)
        isVisible(R.id.tv_yes_button)
        performClick(R.id.tv_yes_button)
        isVisible(R.id.tv_no_button)
        isVisible(R.id.tv_yes_button)
        performClick(R.id.tv_no_button)
        val itemSize = recyclerView.size-1
        mActivityTestRule.scrollRecyclerViewToPosition(recyclerView, itemSize)
    }

    @Test
    fun sendCSATPerReplayAndFailed() {
        fakeGqlRepository.setTypeOfTicket(ON_PROGRESS_NEED_TO_REPLAY)
        mActivityTestRule.launchActivity(intent)
        isVisible(R.id.rv_message_list)
        isVisible(R.id.layout_replay_message)
        isVisible(R.id.view_rply_botton_before_csat_rating)
        performClick(R.id.tv_reply_button)
        isVisible(R.id.tv_no_button)
        isVisible(R.id.tv_yes_button)
        fakeGqlRepository.setResponseTypeNotHappy(true)
        performClick(R.id.tv_no_button)
        viewToaster("Failed to send Rating")
    }

    @Test
    fun closeTicketFailed() {
        fakeGqlRepository.setTypeOfTicket(ON_PROGRESS_NEED_TO_REPLAY)
        mActivityTestRule.launchActivity(intent)
        isVisible(R.id.rv_message_list)
        isVisible(R.id.layout_replay_message)
        isVisible(R.id.view_rply_botton_before_csat_rating)
        performClick(R.id.tv_reply_button)
        isVisible(R.id.tv_no_button)
        isVisible(R.id.tv_yes_button)
        performClick(R.id.tv_yes_button)
        isVisible(R.id.tv_no_button)
        isVisible(R.id.tv_yes_button)
        fakeGqlRepository.setResponseTypeNotHappy(true)
        performClick(R.id.tv_yes_button)
        viewToaster("Failed close user")
    }

    @Test
    fun closeTicketSuccessAndSendCSATTicket() {
        fakeGqlRepository.setTypeOfTicket(ON_PROGRESS_NEED_TO_REPLAY)
        mActivityTestRule.launchActivity(intent)
        isVisible(R.id.rv_message_list)
        isVisible(R.id.layout_replay_message)
        isVisible(R.id.view_rply_botton_before_csat_rating)
        performClick(R.id.tv_reply_button)
        isVisible(R.id.tv_no_button)
        isVisible(R.id.tv_yes_button)
        performClick(R.id.tv_yes_button)
        isVisible(R.id.tv_no_button)
        isVisible(R.id.tv_yes_button)
        performClick(R.id.tv_yes_button)
        isVisible(R.id.smile_layout)
        viewChildPerformClick(R.id.smile_layout, 2)
        isTextDisplayed("Apa yang bisa ditingkatkan dari layanan kami?")
        isVisible(R.id.filter_review)
        viewChildPerformClick(R.id.rv_filter, 0)
        performClick(R.id.txt_finished)
        isVisible(R.id.view_link_bottom)
        isGoneView(R.id.layout_replay_message)
        isGoneView(R.id.view_help_rate)
    }

    @Test
    fun closeTicketSuccessAndSendCSATTicketFailed() {
        fakeGqlRepository.setTypeOfTicket(ON_PROGRESS_NEED_TO_REPLAY)
        mActivityTestRule.launchActivity(intent)
        isVisible(R.id.rv_message_list)
        isVisible(R.id.layout_replay_message)
        isVisible(R.id.view_rply_botton_before_csat_rating)
        performClick(R.id.tv_reply_button)
        isVisible(R.id.tv_no_button)
        isVisible(R.id.tv_yes_button)
        performClick(R.id.tv_yes_button)
        isVisible(R.id.tv_no_button)
        isVisible(R.id.tv_yes_button)
        performClick(R.id.tv_yes_button)
        isVisible(R.id.smile_layout)
        viewChildPerformClick(R.id.smile_layout, 2)
        isTextDisplayed("Apa yang bisa ditingkatkan dari layanan kami?")
        isVisible(R.id.filter_review)
        viewChildPerformClick(R.id.rv_filter, 0)
        fakeGqlRepository.setResponseTypeNotHappy(true)
        performClick(R.id.txt_finished)
        viewToaster("Failed to send Rating")
    }
}
