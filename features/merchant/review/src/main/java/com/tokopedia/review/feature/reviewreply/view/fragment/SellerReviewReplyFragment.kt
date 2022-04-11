package com.tokopedia.review.feature.reviewreply.view.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.common.analytics.ReviewSellerPerformanceMonitoringContract
import com.tokopedia.review.common.analytics.ReviewSellerPerformanceMonitoringListener
import com.tokopedia.review.common.util.PaddingItemDecoratingReview
import com.tokopedia.review.common.util.toRelativeDate
import com.tokopedia.review.databinding.FragmentSellerReviewReplyBinding
import com.tokopedia.review.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.review.feature.reviewreply.analytics.SellerReviewReplyTracking
import com.tokopedia.review.feature.reviewreply.di.component.ReviewReplyComponent
import com.tokopedia.review.feature.reviewreply.insert.presentation.model.ReviewReplyInsertUiModel
import com.tokopedia.review.feature.reviewreply.update.presenter.model.ReviewReplyUpdateUiModel
import com.tokopedia.review.feature.reviewreply.util.mapper.SellerReviewReplyMapper
import com.tokopedia.review.feature.reviewreply.view.adapter.ReviewTemplateListAdapter
import com.tokopedia.review.feature.reviewreply.view.bottomsheet.AddTemplateBottomSheet
import com.tokopedia.review.feature.reviewreply.view.model.ProductReplyUiModel
import com.tokopedia.review.feature.reviewreply.view.model.ReplyTemplateUiModel
import com.tokopedia.review.feature.reviewreply.view.viewholder.ReviewTemplateListViewHolder
import com.tokopedia.review.feature.reviewreply.view.viewmodel.SellerReviewReplyViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.list.ListUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class SellerReviewReplyFragment : BaseDaggerFragment(),
    ReviewTemplateListViewHolder.ReviewTemplateListener,
    ReviewSellerPerformanceMonitoringContract {

    companion object {
        const val EXTRA_FEEDBACK_DATA = "EXTRA_FEEDBACK_DATA"
        const val EXTRA_PRODUCT_DATA = "EXTRA_PRODUCT_DATA"
        const val CACHE_OBJECT_ID = "CACHE_OBJECT_ID"
        const val EXTRA_SHOP_ID = "EXTRA_SHOP_ID"
        const val IS_EMPTY_REPLY_REVIEW = "IS_EMPTY_REPLY_REVIEW"
        const val DATE_REVIEW_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        const val TEMPLATE_MAX = 6
        const val POSITION_REPORT = 0
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var tracking: SellerReviewReplyTracking

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModelReviewReply: SellerReviewReplyViewModel? = null

    private var optionMenuReplyReview: ListUnify? = null
    private var bottomSheetReplyReview: BottomSheetUnify? = null

    private var feedbackUiModel: FeedbackUiModel? = null
    private var productReplyUiModel: ProductReplyUiModel? = null

    private var cacheManager: SaveInstanceCacheManager? = null

    private var bottomSheetAddTemplate: AddTemplateBottomSheet? = null

    private val reviewTemplateListAdapter by lazy {
        ReviewTemplateListAdapter(this)
    }

    private var shopId = ""
    private var isEmptyReply = false

    private var replyTemplateList: List<ReplyTemplateUiModel>? = null
    private var reviewSellerPerformanceMonitoringListener:
            ReviewSellerPerformanceMonitoringListener? = null

    private var binding by autoClearedNullable<FragmentSellerReviewReplyBinding>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        reviewSellerPerformanceMonitoringListener =
            castContextToTalkPerformanceMonitoringListener(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initData(savedInstanceState)
        super.onCreate(savedInstanceState)
        activity?.let {
            cacheManager = SaveInstanceCacheManager(it, savedInstanceState)
        }
        viewModelReviewReply =
            ViewModelProvider(this, viewModelFactory).get(SellerReviewReplyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSellerReviewReplyBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        startNetworkRequestPerformanceMonitoring()
        stopPreparePerformancePageMonitoring()
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                com.tokopedia.unifyprinciples.R.color.Unify_Background
            )
        )
        initToolbar()
        initViewBottomSheet()
        initWidgetView()
        observeReviewTemplate()
        observeInsertReviewReply()
        observeUpdateReviewReply()
        observeInsertTemplateReviewReply()
        observeLiveData()
    }

    override fun getScreenName(): String {
        return getString(R.string.title_review_reply)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_option_review_product_detail, menu)

        for (i in 0 until menu.size()) {
            menu.getItem(i)?.let { menuItem ->
                menuItem.actionView?.setOnClickListener {
                    onOptionsItemSelected(menuItem)
                }
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_option_product_detail -> {
                tracking.eventClickThreeDotsMenu(
                    shopId,
                    productReplyUiModel?.productID ?: "",
                    feedbackUiModel?.feedbackID ?: ""
                )
                initBottomSheetReplyReview()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        viewModelReviewReply?.reviewTemplate?.removeObservers(this)
        viewModelReviewReply?.updateReviewReply?.removeObservers(this)
        viewModelReviewReply?.insertTemplateReply?.removeObservers(this)
        viewModelReviewReply?.insertReviewReply?.removeObservers(this)
        super.onDestroy()
    }

    override fun stopPreparePerformancePageMonitoring() {
        reviewSellerPerformanceMonitoringListener?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        reviewSellerPerformanceMonitoringListener?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        reviewSellerPerformanceMonitoringListener?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        reviewSellerPerformanceMonitoringListener?.startRenderPerformanceMonitoring()
        binding?.reviewReplyTextBoxWidget?.getTemplatesRecyclerView()?.viewTreeObserver?.addOnGlobalLayoutListener(
            object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    reviewSellerPerformanceMonitoringListener?.stopRenderPerformanceMonitoring()
                    reviewSellerPerformanceMonitoringListener?.stopPerformanceMonitoring()
                    binding?.reviewReplyTextBoxWidget?.getTemplatesRecyclerView()?.viewTreeObserver?.removeOnGlobalLayoutListener(
                        this
                    )
                }
            })
    }

    override fun castContextToTalkPerformanceMonitoringListener(context: Context): ReviewSellerPerformanceMonitoringListener? {
        return if (context is ReviewSellerPerformanceMonitoringListener) {
            context
        } else {
            null
        }
    }

    private fun getReviewTemplate() {
        hideData()
        viewModelReviewReply?.getTemplateListReply(shopId)
    }

    private fun observeReviewTemplate() {
        viewModelReviewReply?.reviewTemplate?.observe(viewLifecycleOwner, {
            showData()
            when (it) {
                is Success -> {
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                    replyTemplateList = it.data
                    setTemplateList(it.data)
                }
                is Fail -> {
                    view?.let { view ->
                        Toaster.build(view,
                            context?.getString(R.string.error_message_load_more_review_product)
                                .orEmpty(),
                            type = Toaster.TYPE_ERROR,
                            actionText =
                            context?.getString(R.string.action_retry_toaster_review_product)
                                .orEmpty(),
                            clickListener = {
                                getReviewTemplate()
                            }).show()
                    }
                }
            }
        })
    }

    private fun observeInsertReviewReply() {
        viewModelReviewReply?.insertReviewReply?.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    isEmptyReply = false
                    insertReviewReplySuccess(it.data)
                }
                is Fail -> {
                    view?.let { view ->
                        Toaster.build(
                            view,
                            it.throwable.message.orEmpty(),
                            type = Toaster.TYPE_ERROR
                        ).show()
                    }
                }
            }
        })
    }

    private fun observeUpdateReviewReply() {
        viewModelReviewReply?.updateReviewReply?.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    updateReviewReplySuccess(it.data)
                }
                is Fail -> {
                    view?.let { view ->
                        Toaster.build(
                            view,
                            it.throwable.message.orEmpty(),
                            type = Toaster.TYPE_ERROR
                        ).show()
                    }
                }
            }
        })
    }

    private fun observeInsertTemplateReviewReply() {
        viewModelReviewReply?.insertTemplateReply?.observe(viewLifecycleOwner, {
            bottomSheetAddTemplate?.dismiss()
            when (it) {
                is Success -> {
                    if (it.data.isSuccess) {
                        getReviewTemplate()
                    }
                }
                is Fail -> {
                    view?.let { view ->
                        Toaster.build(
                            view,
                            it.throwable.message.orEmpty(),
                            type = Toaster.TYPE_ERROR,
                            duration = Toaster.LENGTH_LONG
                        ).show()
                    }
                }
            }
        })
    }

    private fun observeLiveData() {
        getReviewTemplate()
        submitReplyReview()
    }

    private fun submitReplyReview() {
        binding?.reviewReplyTextBoxWidget?.apply {
            getSendButton().setOnClickListener {
                if (getText().isNotEmpty()) {
                    tracking.eventClickSendReviewReply(
                        shopId,
                        productReplyUiModel?.productID ?: "",
                        feedbackUiModel?.feedbackID ?: "",
                        getText(),
                        (!isEmptyReply).toString()
                    )
                    if (isEmptyReply) {
                        viewModelReviewReply?.insertReviewReply(
                            feedbackUiModel?.feedbackID ?: "",
                            getText()
                        )
                    } else {
                        viewModelReviewReply?.updateReviewReply(
                            feedbackUiModel?.feedbackID ?: "",
                            getText()
                        )
                    }
                }
            }
        }
    }

    private fun insertReviewReplySuccess(data: ReviewReplyInsertUiModel) {
        if (data.success) {
            activity?.let { KeyboardHandler.showSoftKeyboard(it) }
            binding?.reviewReplyTextBoxWidget?.hide()
            binding?.feedbackItemReplyWidget?.apply {
                showGroupReply()
                setReplyUserText(context.getString(R.string.user_reply))
                setReplyDate(viewModelReviewReply?.replyTime.orEmpty() toRelativeDate (DATE_REVIEW_FORMAT))
                feedbackUiModel?.replyText = binding?.reviewReplyTextBoxWidget?.getText()
                setReplyComment(feedbackUiModel?.replyText ?: "")
            }
            binding?.reviewReplyTextBoxWidget?.clearEditText()
            activity?.setResult(Activity.RESULT_OK)
        } else {
            activity?.setResult(Activity.RESULT_CANCELED)
        }
        KeyboardHandler.DropKeyboard(requireContext(), binding?.reviewReplyTextBoxWidget)
    }

    private fun updateReviewReplySuccess(data: ReviewReplyUpdateUiModel) {
        if (data.success) {
            activity?.let { KeyboardHandler.showSoftKeyboard(it) }
            binding?.reviewReplyTextBoxWidget?.hide()
            binding?.reviewReplyTextBoxWidget?.clearEditText()
            binding?.feedbackItemReplyWidget?.apply {
                setReplyUserText(context.getString(R.string.user_reply))
                feedbackUiModel?.replyText = data.responseMessage
                setReplyDate(viewModelReviewReply?.replyTime.orEmpty() toRelativeDate (DATE_REVIEW_FORMAT))
                setReplyComment(feedbackUiModel?.replyText ?: "")
            }
            activity?.setResult(Activity.RESULT_OK)
        } else {
            activity?.setResult(Activity.RESULT_CANCELED)
        }
        KeyboardHandler.DropKeyboard(requireContext(), binding?.reviewReplyTextBoxWidget)
    }

    private fun showData() {
        binding?.apply {
            loaderReviewReply.gone()
            productItemReplyWidget.show()
            feedbackItemReplyWidget.show()
            reviewReplyTextBoxWidget.show()
            binding?.reviewReplyTextBoxWidget?.getAddTemplateArea()?.show()
        }
    }

    private fun hideData() {
        binding?.apply {
            loaderReviewReply.show()
            productItemReplyWidget.gone()
            feedbackItemReplyWidget.gone()
            reviewReplyTextBoxWidget.gone()
        }
    }

    private fun initData(savedInstanceState: Bundle?) {
        context?.let {
            activity?.intent?.run {
                shopId = getStringExtra(EXTRA_SHOP_ID) ?: ""
                isEmptyReply = getBooleanExtra(IS_EMPTY_REPLY_REVIEW, false)
                val objectId = getStringExtra(CACHE_OBJECT_ID)
                val manager = if (savedInstanceState == null) {
                    SaveInstanceCacheManager(it, objectId)
                } else {
                    cacheManager
                }
                feedbackUiModel = manager?.get(EXTRA_FEEDBACK_DATA, FeedbackUiModel::class.java)
                productReplyUiModel =
                    manager?.get(EXTRA_PRODUCT_DATA, ProductReplyUiModel::class.java)
            }
        }
    }

    private fun initWidgetView() {
        productReplyUiModel?.let { productReply ->
            binding?.productItemReplyWidget?.setItem(productReply)

            feedbackUiModel?.let { feedback ->
                binding?.feedbackItemReplyWidget?.setData(feedback, productReply)
            }
        }
        binding?.reviewReplyTextBoxWidget?.setReplyAction()
        initViewReply()
        initAdapterTemplateList()
    }

    private fun initViewReply() {
        if (isEmptyReply) {
            binding?.feedbackItemReplyWidget?.hideGroupReply()
            binding?.reviewReplyTextBoxWidget?.show()
        } else {
            binding?.feedbackItemReplyWidget?.showGroupReply()
            binding?.reviewReplyTextBoxWidget?.hide()
        }
        binding?.feedbackItemReplyWidget?.getEditReplyTypography()?.setOnClickListener {
            tracking.eventClickEditReviewResponse(
                shopId,
                productReplyUiModel?.productID ?: "",
                feedbackUiModel?.feedbackID ?: ""
            )
            binding?.reviewReplyTextBoxWidget?.show()
            showTextReplyEditText(feedbackUiModel?.replyText.orEmpty())
        }
        binding?.reviewReplyTextBoxWidget?.getEditText()?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) tracking.eventClickResponseReview(
                shopId,
                productReplyUiModel?.productID ?: "",
                feedbackUiModel?.feedbackID ?: ""
            )
        }
        binding?.reviewReplyTextBoxWidget?.clickAddTemplate {
            tracking.eventClickAddTemplateReview(
                shopId,
                productReplyUiModel?.productID ?: "",
                feedbackUiModel?.feedbackID ?: ""
            )
            initBottomSheetAddTemplate()
        }
    }

    private fun showTextReplyEditText(replyText: String) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding?.reviewReplyTextBoxWidget?.getEditText()?.run {
            isFocusable = true
            requestFocus()
            setText(replyText)
            text?.length?.let { setSelection(it) }
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun initToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                setSupportActionBar(binding?.reviewReplyToolbar)
                supportActionBar?.title = getString(R.string.title_review_reply)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowTitleEnabled(true)
                setHasOptionsMenu(true)
            }
        }
    }

    override fun initInjector() {
        getComponent(ReviewReplyComponent::class.java).inject(this)
    }

    private fun initBottomSheetAddTemplate() {
        val titleBottomSheet = getString(R.string.add_template_reply_label)
        bottomSheetAddTemplate =
            AddTemplateBottomSheet(activity, titleBottomSheet, ::submitTemplateReply)
        bottomSheetAddTemplate?.showDialog()
    }

    private fun submitTemplateReply(title: String, message: String) {
        viewModelReviewReply?.insertTemplateReviewReply(shopId, title, message)
    }

    private fun initBottomSheetReplyReview() {
        val optionMenuReport = context?.let { SellerReviewReplyMapper.mapToItemUnifyMenuReport(it) }
        optionMenuReport?.let { optionMenuReplyReview?.setData(it) }
        val title = getString(R.string.option_menu_label)
        bottomSheetReplyReview?.apply {
            setTitle(title)
            showCloseIcon = true
            setCloseClickListener {
                dismiss()
            }
        }

        optionMenuReplyReview?.let {
            it.onLoadFinish {
                it.setOnItemClickListener { _, _, position, _ ->
                    when (position) {
                        POSITION_REPORT -> {
                            tracking.eventClickItemReportOnBottomSheet(
                                shopId,
                                productReplyUiModel?.productID ?: "",
                                feedbackUiModel?.feedbackID ?: ""
                            )
                            val intent = RouteManager.getIntent(
                                context,
                                ApplinkConstInternalMarketplace.REVIEW_SELLER_REPORT
                            )
                            intent.putExtra(
                                ApplinkConstInternalMarketplace.ARGS_SHOP_ID,
                                shopId
                            )
                            intent.putExtra(
                                ApplinkConstInternalMarketplace.ARGS_REVIEW_ID,
                                feedbackUiModel?.feedbackID ?: ""
                            )
                            startActivity(intent)
                            bottomSheetReplyReview?.dismiss()
                        }
                    }
                }
            }
        }

        bottomSheetReplyReview?.show(childFragmentManager, title)
    }

    private fun initViewBottomSheet() {
        val viewMenu = View.inflate(context, com.tokopedia.review.R.layout.bottom_sheet_menu_option_review_reply, null)
        bottomSheetReplyReview = BottomSheetUnify()
        optionMenuReplyReview = viewMenu.findViewById(R.id.optionMenuReply)
        bottomSheetReplyReview?.setChild(viewMenu)
    }

    private fun initAdapterTemplateList() {
        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding?.reviewReplyTextBoxWidget?.getTemplatesRecyclerView()?.apply {
            layoutManager = linearLayoutManager
            if (itemDecorationCount.isZero()) {
                addItemDecoration(PaddingItemDecoratingReview())
            }
            adapter = reviewTemplateListAdapter
        }
    }

    private fun setTemplateList(data: List<ReplyTemplateUiModel>) {
        if (data.size >= TEMPLATE_MAX) {
            binding?.reviewReplyTextBoxWidget?.getAddTemplateButton()?.hide()
        }
        if (data.isEmpty()) {
            binding?.reviewReplyTextBoxWidget?.getTemplatesRecyclerView()?.hide()
        } else {
            reviewTemplateListAdapter.submitList(data)
            binding?.reviewReplyTextBoxWidget?.getTemplatesRecyclerView()?.show()
        }
    }

    override fun onItemReviewTemplateClicked(view: View, title: String) {
        val message = replyTemplateList?.firstOrNull { it.title == title }?.message
        tracking.eventClickItemReviewTemplate(
            shopId,
            productReplyUiModel?.productID ?: "",
            feedbackUiModel?.feedbackID ?: "",
            message.orEmpty()
        )
        val replyText =
            StringBuilder().append(binding?.reviewReplyTextBoxWidget?.getEditText()?.text.toString())
                .append(message.orEmpty())
                .toString()
        showTextReplyEditText(replyText)
    }
}