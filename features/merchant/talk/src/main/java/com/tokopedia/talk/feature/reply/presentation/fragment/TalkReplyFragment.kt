package com.tokopedia.talk.feature.reply.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.attachproduct.view.activity.AttachProductActivity
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringContract
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringListener
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.common.constants.TalkConstants.IS_FROM_INBOX
import com.tokopedia.talk.common.constants.TalkConstants.PARAM_SHOP_ID
import com.tokopedia.talk.common.constants.TalkConstants.PRODUCT_ID
import com.tokopedia.talk.common.constants.TalkConstants.QUESTION_ID
import com.tokopedia.talk.feature.reply.analytics.TalkReplyTracking
import com.tokopedia.talk.feature.reply.data.mapper.TalkReplyMapper
import com.tokopedia.talk.feature.reply.data.model.discussion.AttachedProduct
import com.tokopedia.talk.feature.reply.data.model.discussion.DiscussionDataByQuestionIDResponseWrapper
import com.tokopedia.talk.feature.reply.di.DaggerTalkReplyComponent
import com.tokopedia.talk.feature.reply.di.TalkReplyComponent
import com.tokopedia.talk.feature.reply.presentation.adapter.TalkReplyAdapter
import com.tokopedia.talk.feature.reply.presentation.adapter.TalkReplyAttachedProductAdapter
import com.tokopedia.talk.feature.reply.presentation.adapter.factory.TalkReplyAdapterTypeFactory
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyAnswerCountModel
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyEmptyModel
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyProductHeaderModel
import com.tokopedia.talk.feature.reply.presentation.viewmodel.TalkReplyViewModel
import com.tokopedia.talk.feature.reply.presentation.widget.TalkReplyReportBottomSheet
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.*
import com.tokopedia.talk_old.R
import com.tokopedia.talk_old.reporttalk.view.activity.ReportTalkActivity
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_talk_reading.pageError
import kotlinx.android.synthetic.main.fragment_talk_reading.pageLoading
import kotlinx.android.synthetic.main.fragment_talk_reply.*
import kotlinx.android.synthetic.main.partial_talk_connection_error.view.*
import javax.inject.Inject

class TalkReplyFragment : BaseDaggerFragment(), HasComponent<TalkReplyComponent>, OnReplyBottomSheetClickedListener,
        OnKebabClickedListener, AttachedProductCardListener, TalkReplyHeaderListener,
        TalkReplyTextboxListener, TalkPerformanceMonitoringContract, ThreadListener, TalkReplyProductHeaderListener {

    companion object {

        const val FOLLOWING = true
        const val NOT_FOLLOWING = false
        const val REPORT_ACTIVITY_REQUEST_CODE = 201
        const val ATTACH_PRODUCT_ACTIVITY_REQUEST_CODE = 202
        const val TOASTER_ERROR_DEFAULT_HEIGHT = 50
        const val TOASTER_ERROR_WITH_ATTACHED_PRODUCTS_HEIGHT = 150
        const val NOT_IN_VIEWHOLDER = false

        @JvmStatic
        fun createNewInstance(questionId: String, shopId: String, productId: String, isFromInbox: Boolean): TalkReplyFragment =
                TalkReplyFragment().apply {
                    arguments = Bundle()
                    arguments?.apply {
                        putString(QUESTION_ID, questionId)
                        putString(PARAM_SHOP_ID, shopId)
                        putString(PRODUCT_ID, productId)
                        putBoolean(IS_FROM_INBOX, isFromInbox)
                    }
                }
    }

    @Inject
    lateinit var viewModel: TalkReplyViewModel

    private var questionId = ""
    private var shopId = ""
    private var productId = ""
    private var isFromInbox = false
    private var adapter: TalkReplyAdapter? = null
    private var attachedProductAdapter: TalkReplyAttachedProductAdapter? = null
    private var talkPerformanceMonitoringListener: TalkPerformanceMonitoringListener? = null

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
        stopPreparePerfomancePageMonitoring()
        startNetworkRequestPerformanceMonitoring()
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
                    if(resultCode == Activity.RESULT_OK) {
                        onSuccessReport()
                    }
                }
                ATTACH_PRODUCT_ACTIVITY_REQUEST_CODE -> {
                    if (!it.hasExtra(AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY)) {
                        return
                    }
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
        TalkReplyTracking.eventSendAnswer(viewModel.userId, productId, questionId)
        sendComment(text)
    }

    override fun stopPreparePerfomancePageMonitoring() {
        talkPerformanceMonitoringListener?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        talkPerformanceMonitoringListener?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        talkPerformanceMonitoringListener?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        talkPerformanceMonitoringListener?.startRenderPerformanceMonitoring()
        talkReplyRecyclerView.post {
            talkPerformanceMonitoringListener?.stopRenderPerformanceMonitoring()
            talkPerformanceMonitoringListener?.stopPerformanceMonitoring()
        }
    }

    override fun castContextToTalkPerformanceMonitoringListener(context: Context): TalkPerformanceMonitoringListener? {
        return if(context is TalkPerformanceMonitoringListener) {
            context
        } else {
            null
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        castContextToTalkPerformanceMonitoringListener(context)
    }

    override fun onUserDetailsClicked(userId: String) {
        goToProfileActivity(userId)
    }

    override fun onProductClicked() {
        goToPdp(productId)
    }

    private fun goToReportActivity(commentId: String) {
        val intent = if(commentId.isNotBlank()) {
            context?.let { ReportTalkActivity.createIntentReportComment(it, questionId, commentId, shopId, productId) }
        } else {
            context?.let { ReportTalkActivity.createIntentReportTalk(it, questionId, shopId, productId) }
        }
        startActivityForResult(intent, REPORT_ACTIVITY_REQUEST_CODE)
    }

    private fun goToAttachProductActivity() {
        val intent = context?.let { AttachProductActivity.createInstance(it, shopId, "", viewModel.isMyShop, AttachProductActivity.SOURCE_TALK) }
        startActivityForResult(intent, ATTACH_PRODUCT_ACTIVITY_REQUEST_CODE)
    }

    private fun goToTermsAndConditionsPage() {
        RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${TalkConstants.TERMS_AND_CONDITIONS_PAGE_URL}")
    }

    private fun goToPdp(productId: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        startActivity(intent)
    }

    private fun goToProfileActivity(userId: String) {
        val intent = RouteManager.getIntent(context, ApplinkConst.PROFILE, userId)
        startActivity(intent)
    }

    private fun showDeleteDialog(commentId: String) {
        context?.let {
            val deleteDialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
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
            showPageLoading()
            hidePageError()
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
        adjustToasterHeight()
        view?.let {
            Toaster.make(it, successMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, getString(R.string.talk_ok))
        }
    }

    private fun showErrorToaster(errorMessage: String) {
        adjustToasterHeight()
        view?.let {
            Toaster.make(it, errorMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.talk_ok))
        }
    }

    private fun onSuccessFollowThread() {
        adapter?.setIsFollowingButton(FOLLOWING)
        showSuccessToaster(getString(R.string.toaster_follow_success))
        viewModel.setIsFollowing(FOLLOWING)
    }

    private fun onSuccessUnfollowThread() {
        adapter?.setIsFollowingButton(NOT_FOLLOWING)
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
        adapter?.clearAllElements()
        getDiscussionData()
        showPageLoading()
    }

    private fun onFailDeleteComment() {
        showErrorToaster(getString(R.string.delete_toaster_network_error))
    }

    private fun onSuccessDeleteQuestion() {
        this.activity?.let {
            it.setResult(Activity.RESULT_OK, Intent().putExtra(QUESTION_ID, questionId))
            it.finish()
        }
    }

    private fun onFailDeleteQuestion() {
        showErrorToaster(getString(R.string.delete_question_toaster_fail))
    }

    private fun onAnswerTooLong() {
        adjustToasterHeight()
        view?.let { Toaster.make(it, getString(R.string.reply_toaster_message_too_long), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR) }
    }

    private fun onSuccessCreateComment() {
        showSuccessToaster(getString(R.string.reply_toaster_success))
        resetTextBox()
        resetAttachedProducts()
        adapter?.clearAllElements()
        getDiscussionData()
        showPageLoading()
    }

    private fun onFailCreateComment() {
        adjustToasterHeight()
        view?.let { Toaster.make(it, getString(R.string.reply_toaster_network_error), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR, getString(R.string.talk_ok)) }
    }

    private fun adjustToasterHeight() {
        if(viewModel.attachedProducts.value?.isNotEmpty() == true) {
            Toaster.toasterCustomBottomHeight = TOASTER_ERROR_WITH_ATTACHED_PRODUCTS_HEIGHT.toPx()
            return
        }
        Toaster.toasterCustomBottomHeight = TOASTER_ERROR_DEFAULT_HEIGHT.toPx()
    }

    private fun onSuccessReport() {
        showSuccessToaster(getString(R.string.toaster_report_success))
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
                    with(it.data) {
                        talkReplyRecyclerView.visibility = View.VISIBLE
                        if(isFromInbox && viewModel.isMyShop) {
                            adapter?.showProductHeader(TalkReplyProductHeaderModel(discussionDataByQuestionID.productName, discussionDataByQuestionID.thumbnail))
                        }
                        adapter?.showHeader(TalkReplyMapper.mapDiscussionDataResponseToTalkReplyHeaderModel(it.data, viewModel.isMyShop))
                        if(discussionDataByQuestionID.question.totalAnswer > 0) {
                            stopNetworkRequestPerformanceMonitoring()
                            startRenderPerformanceMonitoring()
                            showAnswers(this)
                        } else {
                            onAnswersEmpty(discussionDataByQuestionID.question.userId)
                        }
                        setIsFollowing(discussionDataByQuestionID.question.questionState.isFollowed)
                        initTextBox(discussionDataByQuestionID.maxAnswerLength)
                        hidePageError()
                        hidePageLoading()
                        replySwipeRefresh.isRefreshing = false
                    }
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
        activity?.window?.decorView?.setBackgroundColor(Color.WHITE)
        initAdapter()
        initAttachedProductAdapter()
        initRecyclerView()
        initAttachedProductRecyclerView()
        initSwipeRefresh()
    }

    private fun initAdapter() {
        adapter = TalkReplyAdapter(TalkReplyAdapterTypeFactory(this, this, this, this, this))
    }

    private fun initRecyclerView() {
        talkReplyRecyclerView.adapter = adapter
    }

    private fun initTextBox(textLimit: Int) {
        val profilePicture = if(viewModel.isMyShop) {
            viewModel.shopAvatar
        } else {
            viewModel.profilePicture
        }
        replyTextBox.bind(profilePicture, this, textLimit)
    }

    private fun initAttachedProductAdapter() {
        attachedProductAdapter = TalkReplyAttachedProductAdapter(this, NOT_IN_VIEWHOLDER)
    }

    private fun initAttachedProductRecyclerView() {
        replyAttachedProductReview.adapter = attachedProductAdapter
    }

    private fun initSwipeRefresh() {
        replySwipeRefresh.setOnRefreshListener {
            adapter?.clearAllElements()
            getDiscussionData()
            showPageLoading()
        }
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

    private fun onAnswersEmpty(userId: Int) {
        showEmpty(userId)
    }

    private fun showAnswers(discussionDataByQuestionIDResponseWrapper: DiscussionDataByQuestionIDResponseWrapper) {
        adapter?.displayAnswers(TalkReplyAnswerCountModel(discussionDataByQuestionIDResponseWrapper.discussionDataByQuestionID.question.totalAnswer),
                    TalkReplyMapper.mapDiscussionDataResponseToTalkReplyModels(discussionDataByQuestionIDResponseWrapper))
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

    private fun sendComment(text: String) {
        if(text.isNotBlank()) {
            viewModel.createNewComment(text, questionId)
        }
    }

    private fun removeAttachedProduct(productId: String) {
        viewModel.removeAttachedProduct(productId)
    }

    private fun getDataFromArguments() {
        arguments?.let {
            questionId = it.getString(QUESTION_ID, "")
            shopId = it.getString(PARAM_SHOP_ID, "")
            productId = it.getString(PRODUCT_ID, "")
            isFromInbox = it.getBoolean(IS_FROM_INBOX)
        }
        viewModel.setIsMyShop(shopId)
    }

    private fun resetTextBox() {
        replyTextBox.reset()
        KeyboardHandler.DropKeyboard(context, view)
    }

    private fun resetAttachedProducts() {
        viewModel.setAttachedProducts(mutableListOf())
    }

    private fun showEmpty(userId: Int) {
        adapter?.showEmpty(TalkReplyEmptyModel(viewModel.userId == userId.toString()))
        replyTextBox.requestFocus()
    }


}