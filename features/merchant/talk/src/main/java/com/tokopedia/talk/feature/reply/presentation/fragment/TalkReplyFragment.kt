package com.tokopedia.talk.feature.reply.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_IS_SELLER_KEY
import com.tokopedia.applink.ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY
import com.tokopedia.applink.ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_SHOP_ID_KEY
import com.tokopedia.applink.ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.talk.R
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringContract
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringListener
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.common.constants.TalkConstants.PARAM_SHOP_ID
import com.tokopedia.talk.common.constants.TalkConstants.PARAM_SOURCE
import com.tokopedia.talk.common.constants.TalkConstants.PARAM_TYPE
import com.tokopedia.talk.common.constants.TalkConstants.QUESTION_ID
import com.tokopedia.talk.common.utils.FirebaseLogger
import com.tokopedia.talk.databinding.FragmentTalkReplyBinding
import com.tokopedia.talk.feature.reply.analytics.TalkReplyTracking
import com.tokopedia.talk.feature.reply.analytics.TalkReplyTrackingConstants
import com.tokopedia.talk.feature.reply.data.mapper.TalkReplyMapper
import com.tokopedia.talk.feature.reply.data.model.discussion.AttachedProduct
import com.tokopedia.talk.feature.reply.data.model.discussion.DiscussionDataByQuestionIDResponseWrapper
import com.tokopedia.talk.feature.reply.di.DaggerTalkReplyComponent
import com.tokopedia.talk.feature.reply.di.TalkReplyComponent
import com.tokopedia.talk.feature.reply.presentation.adapter.TalkReplyAdapter
import com.tokopedia.talk.feature.reply.presentation.adapter.TalkReplyAttachedProductAdapter
import com.tokopedia.talk.feature.reply.presentation.adapter.TalkReplyTemplateAdapter
import com.tokopedia.talk.feature.reply.presentation.adapter.factory.TalkReplyAdapterTypeFactory
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyAnswerCountModel
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyEmptyModel
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyProductHeaderModel
import com.tokopedia.talk.feature.reply.presentation.viewmodel.TalkReplyViewModel
import com.tokopedia.talk.feature.reply.presentation.widget.TalkReplyReportBottomSheet
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.AttachedProductCardListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.OnKebabClickedListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.OnReplyBottomSheetClickedListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyHeaderListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyProductHeaderListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyTemplateListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyTextboxListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.ThreadListener
import com.tokopedia.talk.feature.reporttalk.view.activity.ReportTalkActivity
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

open class TalkReplyFragment : BaseDaggerFragment(), HasComponent<TalkReplyComponent>,
    OnReplyBottomSheetClickedListener,
    OnKebabClickedListener, AttachedProductCardListener, TalkReplyHeaderListener,
    TalkReplyTextboxListener, TalkPerformanceMonitoringContract, ThreadListener,
    TalkReplyProductHeaderListener, TalkReplyTemplateListener {

    companion object {

        const val FOLLOWING = true
        const val NOT_FOLLOWING = false
        const val REPORT_ACTIVITY_REQUEST_CODE = 201
        const val ATTACH_PRODUCT_ACTIVITY_REQUEST_CODE = 202
        const val TOASTER_ERROR_DEFAULT_HEIGHT = 50
        const val TOASTER_ERROR_WITH_ATTACHED_PRODUCTS_HEIGHT = 150
        const val NOT_IN_VIEWHOLDER = false
        const val MINIMUM_TEXT_LENGTH = 5
        const val SOURCE_TALK = "talk"

        @JvmStatic
        fun createNewInstance(
            questionId: String,
            shopId: String,
            source: String,
            inboxType: String
        ): TalkReplyFragment =
            TalkReplyFragment().apply {
                arguments = Bundle().apply {
                    putString(QUESTION_ID, questionId)
                    putString(PARAM_SHOP_ID, shopId)
                    putString(PARAM_SOURCE, source)
                    putString(PARAM_TYPE, inboxType)
                }
            }
    }

    @Inject
    lateinit var viewModel: TalkReplyViewModel

    private var binding by autoCleared<FragmentTalkReplyBinding>()

    private var questionId = ""
    private var shopId = ""
    private var productId = ""
    private var source = ""
    private var hideSuccessToaster = false
    private var adapter: TalkReplyAdapter? = null
    private var attachedProductAdapter: TalkReplyAttachedProductAdapter? = null
    private var talkPerformanceMonitoringListener: TalkPerformanceMonitoringListener? = null
    private var toaster: Snackbar? = null
    private var inboxType = ""
    private var templateAdapter: TalkReplyTemplateAdapter? = null
    private val blockDialog by lazy(LazyThreadSafetyMode.NONE) {
        context?.let {
            DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.talk_block_dialog_title))
                setDescription(getString(R.string.talk_block_dialog_description))
                setPrimaryCTAText(getString(R.string.talk_block_dialog_primary_cta_text))
                setSecondaryCTAText(getString(R.string.talk_block_dialog_secondary_cta_text))
                setPrimaryCTAClickListener {
                    viewModel.blockTalk(questionId)
                    dialogPrimaryCTA.isLoading = true
                    dialogSecondaryCTA.isEnabled = false
                }
                setSecondaryCTAClickListener { dismiss() }
                setOverlayClose(false)
            }
        }
    }

    override fun getScreenName(): String {
        return TalkReplyTrackingConstants.REPLY_SCREEN_NAME
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

    override fun onTermsAndConditionsClicked(): Boolean {
        return goToTermsAndConditionsPage()
    }

    override fun onFollowUnfollowButtonClicked() {
        followUnfollowTalk()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTalkReplyBinding.inflate(inflater, container, false)
        return binding.root
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
        observeUnmaskComment()
        observeUnmaskQuestion()
        observeReportComment()
        observeReportTalk()
        observeBlockTalk()
        observeTemplateList()
        super.onViewCreated(view, savedInstanceState)
        getDiscussionData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let {
            when (requestCode) {
                REPORT_ACTIVITY_REQUEST_CODE -> {
                    if (resultCode == Activity.RESULT_OK) {
                        onSuccessReport()
                    }
                }
                ATTACH_PRODUCT_ACTIVITY_REQUEST_CODE -> {
                    if (!it.hasExtra(TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY)) {
                        return
                    }
                    val resultProducts: ArrayList<ResultProduct> =
                        it.getParcelableArrayListExtra(TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY)
                            ?: ArrayList()
                    setAttachedProducts(
                        TalkReplyMapper.mapResultProductsToAttachedProducts(
                            resultProducts
                        )
                    )
                }
                else -> super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onReportOptionClicked(commentId: String) {
        goToReportActivity(commentId)
    }

    override fun onBlockOptionClicked() {
        showBlockDialog()
    }

    override fun onDeleteOptionClicked(commentId: String) {
        showDeleteDialog(commentId)
    }

    override fun onEditProductOptionClicked() {
        goToEditProduct()
    }

    override fun onKebabClicked(
        commentId: String,
        allowReport: Boolean,
        allowDelete: Boolean,
        allowBlock: Boolean
    ) {
        showBottomSheet(
            commentId = commentId,
            allowReport = allowReport,
            allowDelete = allowDelete,
            allowEdit = false,
            allowBlock = allowBlock
        )
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
        removeObservers(viewModel.markCommentNotFraudResult)
        removeObservers(viewModel.markNotFraudResult)
        removeObservers(viewModel.reportCommentResult)
        removeObservers(viewModel.reportTalkResult)
        removeObservers(viewModel.templateList)
        super.onDestroy()
    }

    override fun onAttachProductButtonClicked() {
        goToAttachProductActivity()
    }

    override fun onMaximumTextLimitReached() {
        onAnswerTooLong()
    }

    override fun onSendButtonClicked(text: String) {
        if (text.length < MINIMUM_TEXT_LENGTH && binding.replyTextBox.hasFocus()) {
            showErrorToaster(
                getString(R.string.reply_toaster_length_too_short_error),
                resources.getBoolean(R.bool.reply_adjust_toaster_height)
            )
        } else {
            TalkReplyTracking.eventSendAnswer(
                viewModel.userId,
                productId,
                questionId,
                adapter?.hasProductHeader() ?: false
            )
            sendComment(text)
        }
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
        binding.talkReplyRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                talkPerformanceMonitoringListener?.stopRenderPerformanceMonitoring()
                talkPerformanceMonitoringListener?.stopPerformanceMonitoring()
                binding.talkReplyRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun castContextToTalkPerformanceMonitoringListener(context: Context): TalkPerformanceMonitoringListener? {
        return if (context is TalkPerformanceMonitoringListener) {
            context
        } else {
            null
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        talkPerformanceMonitoringListener = castContextToTalkPerformanceMonitoringListener(context)
    }

    override fun onUserDetailsClicked(userId: String, isSeller: Boolean, shopId: String) {
        if (isSeller) {
            goToShopPageActivity(shopId)
            return
        }
        goToProfileActivity(userId)
    }

    override fun goToProfilePage(userId: String) {
        goToProfileActivity(userId)
    }

    override fun onUrlClicked(link: String): Boolean {
        return RouteManager.route(context, link)
    }

    override fun onUnmaskCommentOptionSelected(commentId: String) {
        showPageLoading()
        if (commentId.isNotBlank()) {
            viewModel.markCommentNotFraud(questionId, commentId)
            return
        }
        viewModel.markQuestionNotFraud(questionId)
    }

    override fun onDismissUnmaskCard(commentId: String) {
        showPageLoading()
        if (commentId.isNotBlank()) {
            viewModel.reportComment(commentId)
            return
        }
        viewModel.reportTalk(questionId)
    }

    override fun onProductCardClicked(productName: String, position: Int) {
        TalkReplyTracking.eventClickCard(
            inboxType,
            viewModel.userId,
            productName,
            productId,
            position
        )
        goToPdp(productId)
    }

    override fun onProductCardImpressed(productName: String, position: Int) {
        TalkReplyTracking.eventImpressCard(
            inboxType,
            viewModel.userId,
            productName,
            productId,
            position
        )
    }

    override fun onKebabClicked() {
        showBottomSheet(
            commentId = "",
            allowReport = false,
            allowDelete = false,
            allowEdit = true,
            allowBlock = false
        )
    }

    override fun onTemplateClicked(template: String) {
        binding.replyTextBox.setText(template)
    }

    private fun goToReportActivity(commentId: String) {
        val intent = if (commentId.isNotBlank()) {
            context?.let {
                ReportTalkActivity.createIntentReportComment(
                    it,
                    questionId,
                    commentId,
                    shopId,
                    productId
                )
            }
        } else {
            context?.let {
                ReportTalkActivity.createIntentReportTalk(
                    it,
                    questionId,
                    shopId,
                    productId
                )
            }
        }
        startActivityForResult(intent, REPORT_ACTIVITY_REQUEST_CODE)
    }

    private fun goToAttachProductActivity() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.ATTACH_PRODUCT)
        intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_ID_KEY, shopId)
        intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_IS_SELLER_KEY, viewModel.isMyShop)
        intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TALK)
        startActivityForResult(intent, ATTACH_PRODUCT_ACTIVITY_REQUEST_CODE)
    }

    private fun goToTermsAndConditionsPage(): Boolean {
        return RouteManager.route(
            activity,
            "${ApplinkConst.WEBVIEW}?url=${TalkConstants.TERMS_AND_CONDITIONS_PAGE_URL}"
        )
    }

    private fun goToPdp(productId: String) {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            productId
        )
        startActivity(intent)
    }

    private fun goToShopPageActivity(shopId: String) {
        startActivity(RouteManager.getIntent(context, ApplinkConst.SHOP, shopId))
    }

    private fun goToProfileActivity(userId: String) {
        val intent = RouteManager.getIntent(context, ApplinkConst.PROFILE, userId)
        startActivity(intent)
    }

    private fun showBlockDialog() {
        blockDialog?.run {
            dialogPrimaryCTA.isLoading = false
            dialogSecondaryCTA.isEnabled = true
            show()
        }
    }

    private fun showDeleteDialog(commentId: String) {
        context?.let {
            val deleteDialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            initDialog(deleteDialog, commentId)
        }
    }

    private fun showPageLoading() {
        binding.pageLoading.root.show()
    }

    private fun hidePageLoading() {
        binding.pageLoading.root.hide()
    }

    private fun showPageError() {
        binding.pageError.apply {
            root.show()
            readingImageError.loadImageDrawable(com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection)
            talkConnectionErrorRetryButton.setOnClickListener {
                showPageLoading()
                hidePageError()
                getDiscussionData()
            }
        }
    }

    private fun hidePageError() {
        binding.pageError.root.hide()
    }

    private fun showBottomSheet(
        commentId: String,
        allowReport: Boolean,
        allowDelete: Boolean,
        allowEdit: Boolean,
        allowBlock: Boolean
    ) {
        val reportBottomSheet = context?.let { context ->
            TalkReplyReportBottomSheet.createInstance(
                context,
                commentId,
                this,
                allowReport,
                allowDelete,
                allowEdit,
                allowBlock
            )
        }
        this.childFragmentManager.let { reportBottomSheet?.show(it, "") }
    }

    private fun showAttachedProduct() {
        binding.replyAttachedProductContainer.show()
    }

    private fun hideAttachedProduct() {
        binding.replyAttachedProductContainer.hide()
    }

    private fun showSuccessToaster(successMessage: String, adjustHeight: Boolean = false) {
        if (adjustHeight) {
            adjustToasterHeight()
        }
        view?.let {
            this.toaster = Toaster.build(
                it,
                successMessage,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(R.string.talk_ok)
            )
            toaster?.let { toaster ->
                if (toaster.isShownOrQueued) {
                    return
                }
                toaster.show()
            }
        }
    }

    private fun showErrorToaster(errorMessage: String, adjustHeight: Boolean = false) {
        if (adjustHeight) {
            adjustToasterHeight()
        }
        view?.let {
            this.toaster = Toaster.build(
                it,
                errorMessage,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(R.string.talk_ok)
            )
            toaster?.let { toaster ->
                if (toaster.isShownOrQueued) {
                    return
                }
                toaster.show()
            }
        }
    }

    private fun onSuccessFollowThread() {
        adapter?.setIsFollowingButton(FOLLOWING)
        showSuccessToaster(
            getString(R.string.toaster_follow_success),
            resources.getBoolean(R.bool.reply_adjust_toaster_height)
        )
        viewModel.setIsFollowing(FOLLOWING)
    }

    private fun onSuccessUnfollowThread() {
        adapter?.setIsFollowingButton(NOT_FOLLOWING)
        showSuccessToaster(
            getString(R.string.toaster_unfollow_success),
            resources.getBoolean(R.bool.reply_adjust_toaster_height)
        )
        viewModel.setIsFollowing(NOT_FOLLOWING)
    }

    private fun onFailFollowThread() {
        showErrorToaster(
            getString(R.string.toaster_follow_fail),
            resources.getBoolean(R.bool.reply_adjust_toaster_height)
        )
    }

    private fun onFailUnfollowThread() {
        showErrorToaster(
            getString(R.string.toaster_unfollow_fail),
            resources.getBoolean(R.bool.reply_adjust_toaster_height)
        )
    }

    private fun onSuccessDeleteComment() {
        showSuccessToaster(
            getString(R.string.delete_toaster_success),
            resources.getBoolean(R.bool.reply_adjust_toaster_height)
        )
        adapter?.clearAllElements()
        getDiscussionData()
    }

    private fun onFailDeleteComment(throwable: Throwable) {
        logException(throwable)
        showErrorToaster(
            getString(R.string.delete_toaster_network_error),
            resources.getBoolean(R.bool.reply_adjust_toaster_height)
        )
    }

    private fun onSuccessDeleteQuestion() {
        this.activity?.let {
            it.setResult(Activity.RESULT_FIRST_USER, Intent().putExtra(QUESTION_ID, questionId))
            it.finish()
        }
    }

    private fun onFailDeleteQuestion(throwable: Throwable) {
        logException(throwable)
        showErrorToaster(getString(R.string.delete_question_toaster_fail))
    }

    private fun onAnswerTooLong() {
        if (viewModel.attachedProducts.value?.isNotEmpty() == true) {
            Toaster.toasterCustomBottomHeight = 205.toPx()
        } else {
            Toaster.toasterCustomBottomHeight = 105.toPx()
        }
        view?.let {
            Toaster.build(
                it,
                getString(R.string.reply_toaster_message_too_long),
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(R.string.talk_ok)
            ).show()
        }
    }

    private fun onSuccessCreateComment() {
        showSuccessToaster(getString(R.string.reply_toaster_success))
        resetTextBox()
        resetAttachedProducts()
        adapter?.clearAllElements()
        getDiscussionData()
        showPageLoading()
    }

    private fun onFailCreateComment(throwable: Throwable) {
        logException(throwable)
        showErrorToaster(
            getString(R.string.reply_toaster_network_error),
            resources.getBoolean(R.bool.reply_adjust_toaster_height)
        )
    }

    private fun onSuccessUnmaskQuestion() {
        showSuccessToaster(
            getString(R.string.reply_unmask_toaster_positive),
            resources.getBoolean(R.bool.reply_adjust_toaster_height)
        )
        adapter?.clearAllElements()
        getDiscussionData()
    }

    private fun onSuccessUnmaskComment() {
        showSuccessToaster(
            getString(R.string.reply_unmask_toaster_positive),
            resources.getBoolean(R.bool.reply_adjust_toaster_height)
        )
        adapter?.clearAllElements()
        getDiscussionData()
    }

    private fun onHideReportedContent() {
        adapter?.clearAllElements()
        getDiscussionData()
        showSuccessToaster(
            getString(R.string.reply_unmask_toaster_negative),
            resources.getBoolean(R.bool.reply_adjust_toaster_height)
        )
        blockDialog?.dismiss()
    }

    private fun onFailUnmaskCommentOrQuestion(throwable: Throwable) {
        logException(throwable)
        hidePageLoading()
        showErrorToaster(
            getString(R.string.reply_unmask_toaster_error),
            resources.getBoolean(R.bool.reply_adjust_toaster_height)
        )
    }

    private fun onSuccessBlockContent() {
        adapter?.clearAllElements()
        getDiscussionData()
        showSuccessToaster(
            getString(R.string.talk_block_toaster_success_text),
            context?.resources?.getBoolean(R.bool.reply_adjust_toaster_height).orTrue()
        )
        blockDialog?.dismiss()
    }

    private fun onFailBlockCommentOrQuestion(throwable: Throwable) {
        logException(throwable)
        hidePageLoading()
        showErrorToaster(
            getString(R.string.talk_block_toaster_fail_text),
            context?.resources?.getBoolean(R.bool.reply_adjust_toaster_height).orTrue()
        )
    }

    private fun adjustToasterHeight() {
        if (viewModel.attachedProducts.value?.isNotEmpty() == true) {
            Toaster.toasterCustomBottomHeight = TOASTER_ERROR_WITH_ATTACHED_PRODUCTS_HEIGHT.toPx()
            return
        }
        Toaster.toasterCustomBottomHeight = TOASTER_ERROR_DEFAULT_HEIGHT.toPx()
    }

    private fun onSuccessReport() {
        showSuccessToaster(getString(R.string.toaster_report_success), false)
    }

    private fun observeFollowUnfollowResponse() {
        viewModel.followUnfollowResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> showFollowingSuccess()
                is Fail -> showFollowingError(it.throwable)
            }
        })
    }

    private fun observeDiscussionData() {
        viewModel.discussionData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    with(it.data) {
                        stopNetworkRequestPerformanceMonitoring()
                        startRenderPerformanceMonitoring()
                        viewModel.getAllTemplates(viewModel.isMyShop)
                        binding.talkReplyRecyclerView.show()
                        if (isFromInbox() || isFromNotif()) {
                            adapter?.showProductHeader(
                                TalkReplyProductHeaderModel(
                                    discussionDataByQuestionID.productName,
                                    discussionDataByQuestionID.thumbnail,
                                    discussionDataByQuestionID.productStock,
                                    discussionDataByQuestionID.productStockMessage,
                                    discussionDataByQuestionID.isSellerView
                                )
                            )
                        }
                        adapter?.showHeader(
                            TalkReplyMapper.mapDiscussionDataResponseToTalkReplyHeaderModel(
                                it.data
                            )
                        )
                        if (discussionDataByQuestionID.question.totalAnswer > 0) {
                            showAnswers(this, viewModel.userId)
                        } else {
                            onAnswersEmpty(discussionDataByQuestionID.question.questionState.isYours)
                        }
                        setIsFollowing(discussionDataByQuestionID.question.questionState.isFollowed)
                        initTextBox(discussionDataByQuestionID.maxAnswerLength)
                        setProductId(it.data.discussionDataByQuestionID.productId)
                        TalkReplyTracking.sendScreen(screenName, productId, viewModel.userId)
                        hidePageError()
                        hidePageLoading()
                        binding.replySwipeRefresh.isRefreshing = false
                        if (isFromWrite() && !hideSuccessToaster) {
                            hideSuccessToaster = true
                            showSuccessToaster(getString(R.string.reading_create_question_toaster_success))
                        }
                        shopId = discussionDataByQuestionID.shopID
                    }
                }
                else -> {
                    showPageError()
                }
            }
        })
    }

    private fun observeDeleteQuestionResponse() {
        viewModel.deleteTalkResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessDeleteQuestion()
                is Fail -> onFailDeleteQuestion(it.throwable)
            }
        })
    }

    private fun observeDeleteCommentResponse() {
        viewModel.deleteCommentResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessDeleteComment()
                is Fail -> onFailDeleteComment(it.throwable)
            }
        })
    }

    private fun observeCreateNewCommentResponse() {
        viewModel.createNewCommentResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessCreateComment()
                is Fail -> onFailCreateComment(it.throwable)
            }
        })
    }

    private fun observeAttachedProducts() {
        viewModel.attachedProducts.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                attachedProductAdapter?.setData(it)
                showAttachedProduct()
            } else {
                hideAttachedProduct()
            }
        })
    }

    private fun observeUnmaskComment() {
        viewModel.markCommentNotFraudResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessUnmaskComment()
                is Fail -> onFailUnmaskCommentOrQuestion(it.throwable)
            }
        })
    }

    private fun observeUnmaskQuestion() {
        viewModel.markNotFraudResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessUnmaskQuestion()
                is Fail -> onFailUnmaskCommentOrQuestion(it.throwable)
            }
        })
    }

    private fun observeReportComment() {
        viewModel.reportCommentResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onHideReportedContent()
                is Fail -> onFailUnmaskCommentOrQuestion(it.throwable)
            }
        })
    }

    private fun observeReportTalk() {
        viewModel.reportTalkResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onHideReportedContent()
                is Fail -> onFailUnmaskCommentOrQuestion(it.throwable)
            }
        })
    }

    private fun observeBlockTalk() {
        viewModel.blockTalkResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessBlockContent()
                is Fail -> onFailBlockCommentOrQuestion(it.throwable)
            }
        }
    }

    private fun observeTemplateList() {
        viewModel.templateList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (templateAdapter == null) {
                        templateAdapter = TalkReplyTemplateAdapter(this)
                        binding.replyTemplates.apply {
                            adapter = templateAdapter
                            layoutManager =
                                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                        }
                    }
                    if (viewModel.isMyShop) {
                        if (it.data.sellerTemplate.templates.isEmpty() || !it.data.sellerTemplate.isEnable) return@Observer
                        templateAdapter?.setData(it.data.sellerTemplate.templates)
                    } else {
                        if (it.data.buyerTemplate.templates.isEmpty() || !it.data.buyerTemplate.isEnable) return@Observer
                        templateAdapter?.setData(it.data.buyerTemplate.templates)
                    }
                    binding.replyTemplateContainer.show()
                }
                is Fail -> {
                    logException(it.throwable)
                    binding.replyTemplateContainer.hide()
                }
            }
        })
    }

    private fun initView() {
        activity?.window?.decorView?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                com.tokopedia.unifyprinciples.R.color.Unify_Background
            )
        )
        initAdapter()
        initAttachedProductAdapter()
        initRecyclerView()
        initAttachedProductRecyclerView()
        initSwipeRefresh()
        initToolbar()
    }

    private fun initAdapter() {
        adapter = TalkReplyAdapter(TalkReplyAdapterTypeFactory(this, this, this, this, this))
    }

    private fun initRecyclerView() {
        binding.talkReplyRecyclerView.adapter = adapter
    }

    private fun initTextBox(textLimit: Int) {
        val profilePicture = if (viewModel.isMyShop) {
            viewModel.shopAvatar
        } else {
            viewModel.profilePicture
        }
        binding.replyTextBox.bind(profilePicture, this, textLimit)
    }

    private fun initAttachedProductAdapter() {
        attachedProductAdapter = TalkReplyAttachedProductAdapter(this, NOT_IN_VIEWHOLDER)
    }

    private fun initAttachedProductRecyclerView() {
        binding.replyAttachedProductReview.adapter = attachedProductAdapter
    }

    private fun initSwipeRefresh() {
        binding.replySwipeRefresh.setOnRefreshListener {
            adapter?.clearAllElements()
            getDiscussionData()
            showPageLoading()
        }
    }

    private fun initDialog(dialog: DialogUnify, commentId: String) {
        if (commentId.isNotBlank()) {
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

    private fun onAnswersEmpty(isYours: Boolean) {
        showEmpty(isYours)
    }

    private fun showAnswers(
        discussionDataByQuestionIDResponseWrapper: DiscussionDataByQuestionIDResponseWrapper,
        userId: String
    ) {
        adapter?.displayAnswers(
            TalkReplyAnswerCountModel(discussionDataByQuestionIDResponseWrapper.discussionDataByQuestionID.question.totalAnswer),
            TalkReplyMapper.mapDiscussionDataResponseToTalkReplyModels(
                discussionDataByQuestionIDResponseWrapper,
                userId
            )
        )
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

    private fun showFollowingError(throwable: Throwable) {
        logException(throwable)
        if (viewModel.getIsFollowing()) {
            onFailUnfollowThread()
        } else {
            onFailFollowThread()
        }
    }

    private fun showFollowingSuccess() {
        if (viewModel.getIsFollowing()) {
            onSuccessUnfollowThread()
        } else {
            onSuccessFollowThread()
        }
    }

    private fun setAttachedProducts(attachedProducts: MutableList<AttachedProduct>) {
        viewModel.setAttachedProducts(attachedProducts)
    }

    private fun sendComment(text: String) {
        if (text.isNotBlank()) {
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
            source = it.getString(PARAM_SOURCE, "")
            inboxType = it.getString(PARAM_TYPE, "")
        }
    }

    private fun resetTextBox() {
        binding.replyTextBox.reset()
        KeyboardHandler.DropKeyboard(context, view)
    }

    private fun resetAttachedProducts() {
        viewModel.setAttachedProducts(mutableListOf())
    }

    private fun showEmpty(isYours: Boolean) {
        adapter?.showEmpty(TalkReplyEmptyModel(isYours))
        binding.replyTextBox.setOnFocusChangeListener(isYours, {
            activity.let {
                KeyboardHandler.showSoftKeyboard(it)
            }
        }, {
            activity.let {
                KeyboardHandler.hideSoftKeyboard(it)
            }
        })
    }

    private fun isFromInbox(): Boolean {
        return source == TalkConstants.INBOX_SOURCE
    }

    private fun isFromNotif(): Boolean {
        return source == TalkConstants.NOTIFICATION_SOURCE
    }

    private fun isFromWrite(): Boolean {
        return source == TalkConstants.WRITING_SOURCE
    }

    private fun setProductId(productId: String) {
        this.productId = productId
    }

    private fun goToEditProduct() {
        RouteManager.route(context, ApplinkConst.PRODUCT_EDIT, productId)
    }

    fun getDidUserWriteQuestion(): Boolean {
        return isFromWrite()
    }

    fun goToReading() {
        val intent = RouteManager.getIntent(
            context,
            Uri.parse(UriUtil.buildUri(ApplinkConstInternalGlobal.PRODUCT_TALK, productId))
                .buildUpon()
                .appendQueryParameter(PARAM_SHOP_ID, shopId)
                .build().toString()
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun initToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                supportActionBar?.hide()
                setSupportActionBar(binding.headerTalkReply)
                binding.headerTalkReply.title = getString(R.string.title_reply_page)
            }
        }
    }

    private fun logException(throwable: Throwable) {
        FirebaseLogger.logError(throwable)
    }
}
