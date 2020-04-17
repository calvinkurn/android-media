package com.tokopedia.talk.feature.reply.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.talk.common.TalkConstants
import com.tokopedia.talk.common.TalkConstants.COMMENT_ID
import com.tokopedia.talk.common.TalkConstants.TALK_ID
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.reply.di.DaggerTalkReplyComponent
import com.tokopedia.talk.feature.reply.di.TalkReplyComponent
import com.tokopedia.talk.feature.reply.presentation.widget.OnReportClickedListener
import com.tokopedia.talk.feature.reply.presentation.viewmodel.TalkReplyViewModel
import com.tokopedia.talk.feature.reply.presentation.widget.OnKebabClickedListener
import com.tokopedia.talk.feature.reply.presentation.widget.OnTermsAndConditionsClickedListener
import com.tokopedia.talk.feature.reply.presentation.widget.TalkReplyReportBottomSheet
import com.tokopedia.talk.feature.report.presentation.fragment.TalkReportFragment
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_talk_reading.pageError
import kotlinx.android.synthetic.main.fragment_talk_reading.pageLoading
import kotlinx.android.synthetic.main.fragment_talk_reply.*
import kotlinx.android.synthetic.main.partial_talk_connection_error.view.*
import javax.inject.Inject

class TalkReplyFragment : BaseDaggerFragment(), HasComponent<TalkReplyComponent>,
    OnTermsAndConditionsClickedListener, OnReportClickedListener,
    OnKebabClickedListener {

    companion object {

        const val FOLLOWING = true
        const val NOT_FOLLOWING = false
        const val REPORT_ACTIVITY_REQUEST_CODE = 201

        @JvmStatic
        fun createNewInstance(talkId: Int = 0, commentId: Int = 0): TalkReportFragment =
                TalkReportFragment().apply {
                    arguments?.putInt(TALK_ID, talkId)
                    arguments?.putInt(COMMENT_ID, commentId)
                }
    }

    @Inject
    lateinit var viewModel: TalkReplyViewModel

    private var talkId = 0
    private var commentId = 0

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeFollowUnfollowResponse()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            REPORT_ACTIVITY_REQUEST_CODE -> {

            }
        }
    }

    override fun onReportOptionClicked(talkId: Int, commentId: Int) {
        goToReportActivity(talkId, commentId)
    }

    override fun onKebabClicked() {
        showBottomSheet()
    }

    private fun goToReportActivity(talkId: Int, commentId: Int) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.PRODUCT_TALK_REPORT, talkId.toString(), commentId.toString())
        activity?.startActivityForResult(intent, REPORT_ACTIVITY_REQUEST_CODE)
    }

    private fun showDeleteDialog() {
        context?.let {
            val deleteDialog = DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
            initDialog(deleteDialog)
        }
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
        val reportBottomSheet = context?.let { context ->
            TalkReplyReportBottomSheet.createInstance(context, talkId, commentId, this)
        }
        this.childFragmentManager.let { reportBottomSheet?.show(it,"BottomSheetTag") }
    }

    private fun showSuccessToaster(successMessage: String) {
        view?.let {
            Toaster.make(it, successMessage, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL, getString(R.string.talk_ok))
        }
    }

    private fun showErrorToaster(errorMessage: String) {
        view?.let {
            Toaster.make(it, errorMessage, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR, getString(R.string.talk_ok))
        }
    }

    private fun onSuccessFollowThread() {
        talkReplyHeader.setButtonToFollowed()
        showSuccessToaster(getString(R.string.toaster_follow_success))
        viewModel.setIsFollowing(FOLLOWING)
    }

    private fun onSuccessUnfollowThread() {
        talkReplyHeader.setButtonToUnfollowed()
        showSuccessToaster(getString(R.string.toaster_unfollow_success))
        viewModel.setIsFollowing(NOT_FOLLOWING)
    }

    private fun onFailFollowThread() {
        showErrorToaster(getString(R.string.toaster_follow_fail))
    }

    private fun onFailUnfollowThread() {
        showErrorToaster(getString(R.string.toaster_unfollow_fail))
    }

    private fun onSuccessDeleteThread() {
        showSuccessToaster(getString(R.string.delete_toaster_success))
    }

    private fun onFailDeleteThread() {
        showErrorToaster(getString(R.string.delete_toaster_network_error))
    }

    private fun onAnswerTooLong() {
        showErrorToaster(getString(R.string.reply_toaster_message_too_long))
    }

    private fun onSuccessSendQuestion() {
        showSuccessToaster(getString(R.string.reply_toaster_success))
    }

    private fun onFailSendQuestion() {
        showErrorToaster(getString(R.string.reply_toaster_network_error))
    }

    private fun goToTermsAndConditionsPage() {
        RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${TalkConstants.TERMS_AND_CONDITIONS_PAGE_URL}")
    }

    private fun observeFollowUnfollowResponse() {
        viewModel.followUnfollowResult.observe(this, Observer {
            when(it) {
                is Success -> {
                    if(viewModel.getIsFollowing()) {
                        onSuccessUnfollowThread()
                    } else {
                        onSuccessFollowThread()
                    }
                }
                else -> {
                    if(viewModel.getIsFollowing()) {
                        onFailFollowThread()
                    } else {
                        onFailUnfollowThread()
                    }
                }
            }
        })
    }

    private fun initDialog(dialog: DialogUnify) {
        dialog.setTitle(getString(R.string.delete_dialog_title))
        dialog.setDescription(getString(R.string.delete_dialog_content))
        dialog.setPrimaryCTAText(getString(R.string.delete_confirm_button))
        dialog.setSecondaryCTAText(getString(R.string.delete_cancel_button))
        dialog.setPrimaryCTAClickListener {
            deleteReply()
        }
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteReply() {

    }
}