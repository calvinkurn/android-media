package com.tokopedia.talk.feature.reply.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.attachproduct.view.activity.AttachProductActivity
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.talk.common.TalkConstants
import com.tokopedia.talk.common.TalkConstants.QUESTION_ID
import com.tokopedia.talk.common.TalkConstants.SHOP_ID
import com.tokopedia.talk.feature.reply.data.mapper.TalkReplyMapper
import com.tokopedia.talk.feature.reply.data.model.discussion.AttachedProduct
import com.tokopedia.talk.feature.reply.data.model.discussion.DiscussionDataByQuestionIDResponseWrapper
import com.tokopedia.talk.feature.reply.di.DaggerTalkReplyComponent
import com.tokopedia.talk.feature.reply.di.TalkReplyComponent
import com.tokopedia.talk.feature.reply.presentation.adapter.TalkReplyAdapter
import com.tokopedia.talk.feature.reply.presentation.adapter.TalkReplyAttachedProductAdapter
import com.tokopedia.talk.feature.reply.presentation.adapter.factory.TalkReplyAdapterTypeFactory
import com.tokopedia.talk.feature.reply.presentation.uimodel.TalkReplyHeaderModel
import com.tokopedia.talk.feature.reply.presentation.viewmodel.TalkReplyViewModel
import com.tokopedia.talk.feature.reply.presentation.widget.TalkReplyReportBottomSheet
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.*
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_talk_reading.pageError
import kotlinx.android.synthetic.main.fragment_talk_reading.pageLoading
import kotlinx.android.synthetic.main.fragment_talk_reply.*
import kotlinx.android.synthetic.main.partial_talk_connection_error.view.*
import javax.inject.Inject

class TalkReplyFragment : BaseDaggerFragment(), HasComponent<TalkReplyComponent>, OnReplyBottomSheetClickedListener,
        OnKebabClickedListener, AttachedProductCardListener, TalkReplyHeaderListener,
        TalkReplyTextboxListener {

    companion object {

        const val FOLLOWING = true
        const val NOT_FOLLOWING = false
        const val REPORT_ACTIVITY_REQUEST_CODE = 201
        const val ATTACH_PRODUCT_ACTIVITY_REQUEST_CODE = 202

        @JvmStatic
        fun createNewInstance(questionId: String, shopId: String): TalkReplyFragment =
                TalkReplyFragment().apply {
                    arguments = Bundle()
                    arguments?.apply {
                        putString(QUESTION_ID, questionId)
                        putString(SHOP_ID, shopId)
                    }
                }
    }

    @Inject
    lateinit var viewModel: TalkReplyViewModel
    @Inject
    lateinit var userSession: UserSessionInterface

    private var questionId = ""
    private var shopId = ""
    private var adapter: TalkReplyAdapter? = null
    private var attachedProductAdapter: TalkReplyAttachedProductAdapter? = null

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun getComponent(): TalkReplyComponent? {
        return activity?.run {
            DaggerTalkReplyComponent
                    .builder()
                    .talkComponent(TalkInstance.getComponent(application))
                    .build()
        }
    }

    override fun onTermsAndConditionsClicked() {
        goToTermsAndConditionsPage()
    }

    override fun onFollowUnfollowButtonClicked() {
        followUnfollowTalk()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_reply, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showPageLoading()
        initView()
        getDataFromArguments()
        observeFollowUnfollowResponse()
        observeDiscussionData()
        observeDeleteCommentResponse()
        observeDeleteQuestionResponse()
        observeCreateNewCommentResponse()
        observeAttachedProducts()
        super.onViewCreated(view, savedInstanceState)
        getDiscussionData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let {
            when(requestCode) {
                REPORT_ACTIVITY_REQUEST_CODE -> {

                }
                ATTACH_PRODUCT_ACTIVITY_REQUEST_CODE -> {
                    if (!it.hasExtra(AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY))
                        return
                    val resultProducts: ArrayList<ResultProduct> = it.getParcelableArrayListExtra(AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY)
                    setAttachedProducts(TalkReplyMapper.mapResultProductsToAttachedProducts(resultProducts))
                }
                else -> super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onReportOptionClicked(commentId: String) {
        goToReportActivity(commentId)
    }

    override fun onDeleteOptionClicked(commentId: String) {
        showDeleteDialog(commentId)
    }

    override fun onKebabClicked(commentId: String, allowReport: Boolean, allowDelete: Boolean) {
        showBottomSheet(commentId, allowReport, allowDelete)
    }

    override fun onClickAttachedProduct(productId: String) {
        goToPdp(productId)
    }

    override fun onDeleteAttachedProduct(productId: String) {
        removeAttachedProduct(productId)
    }

    override fun onDestroy() {
        removeObservers(viewModel.discussionData)
        removeObservers(viewModel.deleteCommentResult)
        removeObservers(viewModel.deleteTalkResult)
        removeObservers(viewModel.followUnfollowResult)
        removeObservers(viewModel.createNewCommentResult)
        removeObservers(viewModel.attachedProducts)
        super.onDestroy()
    }

    override fun onAttachProductButtonClicked() {
        goToAttachProductActivity()
    }

    override fun onMaximumTextLimitReached() {
        onAnswerTooLong()
    }

    override fun onSendButtonClicked(text: String) {
        sendComment(text, listOf())
    }

    private fun goToReportActivity(commentId: String) {

    }

    private fun goToAttachProductActivity() {
        val intent = context?.let { AttachProductActivity.createInstance(it, shopId, "", userSession.shopId == shopId, AttachProductActivity.SOURCE_TALK) }
        startActivityForResult(intent, ATTACH_PRODUCT_ACTIVITY_REQUEST_CODE)
    }

    private fun goToTermsAndConditionsPage() {
        RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${TalkConstants.TERMS_AND_CONDITIONS_PAGE_URL}")
    }

    private fun goToPdp(productId: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        startActivity(intent)
    }

    private fun showDeleteDialog(commentId: String) {
        context?.let {
            val deleteDialog = DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
            initDialog(deleteDialog, commentId)
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
            getDiscussionData()
        }
        pageError.readingConnectionErrorGoToSettingsButton.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalGlobal.GENERAL_SETTING)
        }
    }

    private fun hidePageError() {
        pageError.visibility = View.GONE
    }

    private fun showBottomSheet(commentId: String, allowReport: Boolean, allowDelete: Boolean) {
        val reportBottomSheet = context?.let { context ->
            TalkReplyReportBottomSheet.createInstance(context, commentId, this, allowReport, allowDelete)
        }
        this.childFragmentManager.let { reportBottomSheet?.show(it,"BottomSheetTag") }
    }

    private fun showAttachedProduct() {
        replyAttachedProductContainer.visibility = View.VISIBLE
    }

    private fun hideAttachedProduct() {
        replyAttachedProductContainer.visibility = View.GONE
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

    private fun onSuccessDeleteComment() {
        showSuccessToaster(getString(R.string.delete_toaster_success))
    }

    private fun onFailDeleteComment() {
        showErrorToaster(getString(R.string.delete_toaster_network_error))
    }

    private fun onSuccessDeleteQuestion() {
        this.activity?.setResult(Activity.RESULT_OK)
    }

    private fun onFailDeleteQuestion() {
        showErrorToaster(getString(R.string.delete_question_toaster_fail))
    }

    private fun onAnswerTooLong() {
        showErrorToaster(getString(R.string.reply_toaster_message_too_long))
    }

    private fun onSuccessCreateComment() {
        showSuccessToaster(getString(R.string.reply_toaster_success))
    }

    private fun onFailCreateComment() {
        showErrorToaster(getString(R.string.reply_toaster_network_error))
    }

    private fun observeFollowUnfollowResponse() {
        viewModel.followUnfollowResult.observe(this, Observer {
            when(it) {
                is Success -> {
                    showFollowingSuccess()
                }
                else -> {
                    showFollowingError()
                }
            }
        })
    }

    private fun observeDiscussionData() {
        viewModel.discussionData.observe(this, Observer {
            when(it) {
                is Success -> {
                    hidePageError()
                    hidePageLoading()
                    bindHeader(TalkReplyMapper.mapDiscussionDataResponseToTalkReplyHeaderModel(it.data))
                    if(it.data.discussionDataByQuestionID.question.totalAnswer > 0) {
                        showAnswers(it.data)
                    } else {
                        onAnswersEmpty()
                    }
                    setIsFollowing(it.data.discussionDataByQuestionID.question.questionState.isFollowed)
                }
                else -> {
                    showPageError()
                }
            }
        })
    }

    private fun observeDeleteQuestionResponse() {
        viewModel.deleteTalkResult.observe(this, Observer {
            when(it) {
                is Success -> onSuccessDeleteQuestion()
                else -> onFailDeleteQuestion()
            }
        })
    }

    private fun observeDeleteCommentResponse() {
        viewModel.deleteCommentResult.observe(this, Observer {
            when(it) {
                is Success -> onSuccessDeleteComment()
                else -> onFailDeleteComment()
            }
        })
    }

    private fun observeCreateNewCommentResponse() {
        viewModel.createNewCommentResult.observe(this, Observer {
            when(it) {
                is Success -> onSuccessCreateComment()
                else -> onFailCreateComment()
            }
        })
    }

    private fun observeAttachedProducts() {
        viewModel.attachedProducts.observe(this, Observer {
            if(it.isNotEmpty()) {
                attachedProductAdapter?.setData(it)
                showAttachedProduct()
            } else {
                hideAttachedProduct()
            }
        })
    }

    private fun initView() {
        initAdapter()
        initAttachedProductAdapter()
        initRecyclerView()
        initAttachedProductRecyclerView()
        initTextBox()
    }

    private fun initAdapter() {
        adapter = TalkReplyAdapter(TalkReplyAdapterTypeFactory(this, this))
    }

    private fun initRecyclerView() {
        talkReplyRecyclerView.adapter = adapter
    }

    private fun initTextBox() {
        replyTextBox.bind(userSession.profilePicture, this)
    }

    private fun initAttachedProductAdapter() {
        attachedProductAdapter = TalkReplyAttachedProductAdapter(this)
    }

    private fun initAttachedProductRecyclerView() {
        replyAttachedProductReview.adapter = attachedProductAdapter
    }

    private fun bindHeader(talkReplyHeaderModel: TalkReplyHeaderModel) {
        talkReplyHeader.bind(talkReplyHeaderModel, this, this)
    }

    private fun bindTotalAnswers(totalAnswers: Int) {
        talkReplyTotalAnswers.text = String.format(getString(R.string.reply_total_answer), totalAnswers)
    }

    private fun initDialog(dialog: DialogUnify, commentId: String) {
        if(commentId.isNotBlank()) {
            dialog.setDescription(getString(R.string.delete_dialog_content))
            dialog.setPrimaryCTAClickListener {
                deleteReply(commentId)
                dialog.dismiss()
            }
        } else {
            dialog.setDescription(getString(R.string.delete_dialog_question_description))
            dialog.setPrimaryCTAClickListener {
                deleteQuestion()
                dialog.dismiss()
            }
        }
        dialog.setTitle(getString(R.string.delete_dialog_title))
        dialog.setPrimaryCTAText(getString(R.string.delete_confirm_button))
        dialog.setSecondaryCTAText(getString(R.string.delete_cancel_button))
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun onAnswersEmpty() {
        talkReplyTotalAnswers.visibility = View.GONE
        talkReplyRecyclerView.visibility = View.GONE
        talkReplyEmptyState.visibility = View.VISIBLE
    }

    private fun showAnswers(discussionDataByQuestionIDResponseWrapper: DiscussionDataByQuestionIDResponseWrapper) {
        bindTotalAnswers(discussionDataByQuestionIDResponseWrapper.discussionDataByQuestionID.question.totalAnswer)
        adapter?.displayAnswers(TalkReplyMapper.mapDiscussionDataResponseToTalkReplyModels(discussionDataByQuestionIDResponseWrapper))
        talkReplyTotalAnswers.visibility = View.VISIBLE
        talkReplyRecyclerView.visibility = View.VISIBLE
        talkReplyEmptyState.visibility = View.GONE
    }

    private fun deleteReply(commentId: String) {
        viewModel.deleteComment(questionId, commentId)
    }

    private fun deleteQuestion() {
        viewModel.deleteTalk(questionId)
    }

    private fun getDiscussionData() {
        viewModel.getDiscussionDataByQuestionID(questionId, shopId)
    }

    private fun followUnfollowTalk() {
        viewModel.followUnfollowTalk(questionId.toIntOrZero())
    }

    private fun setIsFollowing(isFollowing: Boolean) {
        viewModel.setIsFollowing(isFollowing)
    }

    private fun showFollowingError() {
        if(viewModel.getIsFollowing()) {
            onFailUnfollowThread()
        } else {
            onFailFollowThread()
        }
    }

    private fun showFollowingSuccess() {
        if(viewModel.getIsFollowing()) {
            onSuccessUnfollowThread()
        } else {
            onSuccessFollowThread()
        }
    }

    private fun setAttachedProducts(attachedProducts: MutableList<AttachedProduct>) {
        viewModel.setAttachedProducts(attachedProducts)
    }

    private fun sendComment(text: String, attachedProductIds: List<String>) {

    }

    private fun removeAttachedProduct(productId: String) {
        viewModel.removeAttachedProduct(productId)
    }

    private fun getDataFromArguments() {
        arguments?.let {
            questionId = it.getString(QUESTION_ID, "")
            shopId = it.getString(SHOP_ID, "")
        }
    }
}