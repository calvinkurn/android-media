package com.tokopedia.talk.feature.reply.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.talk.common.TalkConstants
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.reply.di.DaggerTalkReplyComponent
import com.tokopedia.talk.feature.reply.di.TalkReplyComponent
import com.tokopedia.talk.feature.reply.presentation.widget.OnTermsAndConditionsClickedListener
import com.tokopedia.talk.feature.report.presentation.fragment.TalkReportFragment
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_talk_reading.*
import kotlinx.android.synthetic.main.fragment_talk_reading.pageError
import kotlinx.android.synthetic.main.fragment_talk_reading.pageLoading
import kotlinx.android.synthetic.main.fragment_talk_reply.*
import kotlinx.android.synthetic.main.partial_talk_connection_error.view.*

class TalkReplyFragment : BaseDaggerFragment(), HasComponent<TalkReplyComponent>,
    OnTermsAndConditionsClickedListener {

    companion object {

        const val TALK_ID = "talk_id"
        const val COMMENT_ID = "comment_id"

        @JvmStatic
        fun createNewInstance(talkId: Int = 0, commentId: Int = 0): TalkReportFragment =
                TalkReportFragment().apply {
                    arguments?.putInt(TALK_ID, talkId)
                    arguments?.putInt(COMMENT_ID, commentId)
                }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component.inject(this)
    }

    override fun getComponent(): TalkReplyComponent {
        return DaggerTalkReplyComponent.builder().talkComponent(
                getComponent(TalkComponent::class.java))
                .build()
    }

    override fun onTermsAndConditionsClicked() {
        goToTermsAndConditionsPage()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_reply, container, false)
    }

    private fun showPageLoading() {
        pageLoading.visibility = View.VISIBLE
    }

    private fun hidePageLoading() {
        pageLoading.visibility = View.GONE
    }

    private fun showPageError() {
        pageError.visibility = View.VISIBLE
        pageError.readingConnectionErrorRetryButton.setOnClickListener {

        }
        pageError.readingConnectionErrorGoToSettingsButton.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalGlobal.GENERAL_SETTING)
        }
    }

    private fun hidePageError() {
        pageError.visibility = View.GONE
    }

    private fun initView() {

    }

    private fun showBottomSheet() {

    }

    private fun showSuccessToaster(successMessage: String) {
        view?.let {
            Toaster.make(it, successMessage, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL, getString(R.string.talk_ok))
        }
    }

    private fun onSuccessFollowThread() {
        talkReplyHeader.setButtonToFollowed()
        showSuccessToaster(getString(R.string.toaster_follow_success))
    }

    private fun onSuccessUnfollowThread() {
        talkReplyHeader.setButtonToUnfollowed()
        showSuccessToaster(getString(R.string.toaster_unfollow_success))
    }

    private fun goToTermsAndConditionsPage() {
        RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${TalkConstants.TERMS_AND_CONDITIONS_PAGE_URL}")
    }
}