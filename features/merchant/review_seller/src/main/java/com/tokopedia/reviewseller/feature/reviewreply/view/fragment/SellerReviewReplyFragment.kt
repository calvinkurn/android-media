package com.tokopedia.reviewseller.feature.reviewreply.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.PaddingItemDecoratingReviewSeller
import com.tokopedia.reviewseller.common.util.toRelativeDate
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.reviewseller.feature.reviewreply.di.component.ReviewReplyComponent
import com.tokopedia.reviewseller.feature.reviewreply.util.mapper.SellerReviewReplyMapper
import com.tokopedia.reviewseller.feature.reviewreply.view.adapter.ReviewTemplateListAdapter
import com.tokopedia.reviewseller.feature.reviewreply.view.model.InsertReplyResponseUiModel
import com.tokopedia.reviewseller.feature.reviewreply.view.model.ProductReplyUiModel
import com.tokopedia.reviewseller.feature.reviewreply.view.model.ReplyTemplateUiModel
import com.tokopedia.reviewseller.feature.reviewreply.view.model.UpdateReplyResponseUiModel
import com.tokopedia.reviewseller.feature.reviewreply.view.viewholder.ReviewTemplateListViewHolder
import com.tokopedia.reviewseller.feature.reviewreply.view.viewmodel.SellerReviewReplyViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.list.ListUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_seller_review_reply.*
import kotlinx.android.synthetic.main.widget_reply_feedback_item.*
import kotlinx.android.synthetic.main.widget_reply_textbox.*
import javax.inject.Inject

class SellerReviewReplyFragment : BaseDaggerFragment(), ReviewTemplateListViewHolder.ReviewTemplateListener {

    companion object {
        const val EXTRA_FEEDBACK_DATA = "EXTRA_FEEDBACK_DATA"
        const val EXTRA_PRODUCT_DATA = "EXTRA_PRODUCT_DATA"
        const val CACHE_OBJECT_ID = "CACHE_OBJECT_ID"
        const val EXTRA_SHOP_ID = "EXTRA_SHOP_ID"
        const val IS_EMPTY_REPLY_REVIEW = "IS_EMPTY_REPLY_REVIEW"
        const val DATE_REVIEW_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModelReviewReply: SellerReviewReplyViewModel? = null

    private var optionMenuReplyReview: ListUnify? = null
    private var bottomSheetReplyReview: BottomSheetUnify? = null

    private var feedbackUiModel: FeedbackUiModel? = null
    private var productReplyUiModel: ProductReplyUiModel? = null

    private var cacheManager: SaveInstanceCacheManager? = null

    private val reviewTemplateListAdapter by lazy {
        ReviewTemplateListAdapter(this)
    }

    private var shopId = 0
    private var isEmptyReply = false

    private var replyTemplateList: List<ReplyTemplateUiModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initData(savedInstanceState)
        super.onCreate(savedInstanceState)
        activity?.let {
            cacheManager = SaveInstanceCacheManager(it, savedInstanceState)
        }
        viewModelReviewReply = ViewModelProvider(this, viewModelFactory).get(SellerReviewReplyViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_seller_review_reply, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViewBottomSheet()
        initWidgetView()
        observeLiveData()
    }

    override fun getScreenName(): String {
        return getString(R.string.title_review_reply)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_option_review_product_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_option_product_detail -> {
                initBottomSheetReplyReview()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        viewModelReviewReply?.reviewTemplate?.removeObservers(this)
        viewModelReviewReply?.insertReviewReply?.removeObservers(this)
        viewModelReviewReply?.updateReviewReply?.removeObservers(this)
        viewModelReviewReply?.flush()
        super.onDestroy()
    }

    private fun getReviewTemplate() {
        hideData()
        viewModelReviewReply?.getTemplateListReply(userSession.shopId.toIntOrZero())
    }

    private fun observeLiveData() {
        viewModelReviewReply?.reviewTemplate?.observe(this, Observer {
            showData()
            when (it) {
                is Success -> {
                    replyTemplateList = it.data
                    setTemplateList(it.data)
                }
                is Fail -> {
                    view?.let { it1 ->
                        Toaster.make(it1, it.throwable.message.orEmpty(), Toaster.TYPE_ERROR,
                                actionText = context?.getString(R.string.retry_label).orEmpty(),
                                clickListener = View.OnClickListener {
                                    getReviewTemplate()
                                })
                    }
                }
            }
        })

        viewModelReviewReply?.insertReviewReply?.observe(this, Observer {
            when (it) {
                is Success -> {
                    insertReviewReplySuccess(it.data)
                }
                is Fail -> {
                    view?.let { it1 ->
                        Toaster.make(it1, it.throwable.message.orEmpty(), Toaster.TYPE_ERROR)
                    }
                }
            }
        })

        viewModelReviewReply?.updateReviewReply?.observe(this, Observer {
            when (it) {
                is Success -> {
                    updateReviewReplySuccess(it.data)
                }
                is Fail -> {
                    view?.let { it1 ->
                        Toaster.make(it1, it.throwable.message.orEmpty(), Toaster.TYPE_ERROR)
                    }
                }
            }
        })

        getReviewTemplate()

        replySendButton.setOnClickListener {
            if (replyEditText.text?.isNotEmpty() == true) {
                if (isEmptyReply) {
                    //Insert
                    viewModelReviewReply?.insertReviewReply(
                            feedbackUiModel?.feedbackID.orZero(),
                            productReplyUiModel?.productID.orZero(),
                            shopId,
                            replyEditText.text.toString())
                } else {
                    //Update
                    viewModelReviewReply?.updateReviewReply(feedbackUiModel?.feedbackID.orZero(),
                            replyEditText.text.toString())
                }
            }
        }
    }

    private fun insertReviewReplySuccess(data: InsertReplyResponseUiModel) {
        if (data.isSuccess == 1) {
            activity?.let { KeyboardHandler.showSoftKeyboard(it) }
            reviewReplyTextBoxWidget?.hide()
            groupReply?.show()
            tvReplyUser?.text = context?.getString(R.string.user_reply)
            tvReplyDate.text = viewModelReviewReply?.getReplyTime().orEmpty() toRelativeDate (DATE_REVIEW_FORMAT)
            feedbackUiModel?.replyText = replyEditText.text.toString()
            tvReplyComment.text = feedbackUiModel?.replyText
            replyEditText.text?.clear()
        }
    }

    private fun updateReviewReplySuccess(data: UpdateReplyResponseUiModel) {
        if (data.isSuccess) {
            activity?.let { KeyboardHandler.showSoftKeyboard(it) }
            reviewReplyTextBoxWidget?.hide()
            replyEditText.text?.clear()
            tvReplyUser?.text = context?.getString(R.string.user_reply)
            feedbackUiModel?.replyText = data.responseMessage
            tvReplyDate.text = viewModelReviewReply?.getReplyTime().orEmpty() toRelativeDate (DATE_REVIEW_FORMAT)
            tvReplyComment.text = feedbackUiModel?.replyText
        }
    }

    private fun showData() {
        loaderReviewReply?.gone()
        productItemReplyWidget?.show()
        feedbackItemReplyWidget?.show()
        reviewReplyTextBoxWidget?.show()
        add_template_area?.show()
    }

    private fun hideData() {
        loaderReviewReply?.show()
        productItemReplyWidget?.gone()
        feedbackItemReplyWidget?.gone()
        reviewReplyTextBoxWidget?.gone()
    }

    private fun initData(savedInstanceState: Bundle?) {
        context?.let {
            activity?.intent?.run {
                shopId = getStringExtra(EXTRA_SHOP_ID).toIntOrZero()
                isEmptyReply = getBooleanExtra(IS_EMPTY_REPLY_REVIEW, false)
                val objectId = getStringExtra(CACHE_OBJECT_ID)
                val manager = if (savedInstanceState == null) {
                    SaveInstanceCacheManager(it, objectId)
                } else {
                    cacheManager
                }
                feedbackUiModel = manager?.get(EXTRA_FEEDBACK_DATA, FeedbackUiModel::class.java)
                productReplyUiModel = manager?.get(EXTRA_PRODUCT_DATA, ProductReplyUiModel::class.java)
            }
        }
    }

    private fun initWidgetView() {
        productReplyUiModel?.let { productItemReplyWidget?.setItem(it) }
        feedbackUiModel?.let { feedbackItemReplyWidget?.setData(it) }
        reviewReplyTextBoxWidget?.setReplyAction()
        initViewReply()
    }

    private fun initViewReply() {
        if (isEmptyReply) {
            groupReply?.hide()
            reviewReplyTextBoxWidget?.show()
        } else {
            groupReply?.show()
            reviewReplyTextBoxWidget?.hide()
            tvReplyEdit?.setOnClickListener {
                reviewReplyTextBoxWidget?.show()
                showTextReplyEditText(feedbackUiModel?.replyText.orEmpty())
            }
        }
    }

    private fun showTextReplyEditText(replyText: String) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        replyEditText?.run {
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
            setText(replyText)
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun initToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                setSupportActionBar(review_reply_toolbar)
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

    private fun initBottomSheetReplyReview() {
        val optionMenuReport = context?.let { SellerReviewReplyMapper.mapToItemUnifyMenuReport(it) }
        optionMenuReport?.let { optionMenuReplyReview?.setData(it) }
        val title = context?.getString(R.string.option_menu_label)
        bottomSheetReplyReview?.apply {
            setTitle(title.orEmpty())
            showCloseIcon = true
            setCloseClickListener {
                dismiss()
            }
        }

        optionMenuReplyReview?.let {
            it.onLoadFinish {
                it.setOnItemClickListener { _, _, position, _ ->
                    when (position) {
                        0 -> {
                            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.REVIEW_SELLER_REPORT)
                            intent.putExtra(ApplinkConstInternalMarketplace.ARGS_SHOP_ID, userSession.shopId.toInt())
                            intent.putExtra(ApplinkConstInternalMarketplace.ARGS_REVIEW_ID, feedbackUiModel?.feedbackID.toString())
                            startActivity(intent)
                        }
                    }
                }
            }
        }

        fragmentManager?.let {
            bottomSheetReplyReview?.show(it, getString(R.string.option_menu_label))
        }
    }

    private fun initViewBottomSheet() {
        val viewMenu = View.inflate(context, R.layout.bottom_sheet_menu_option_review_reply, null)
        bottomSheetReplyReview = BottomSheetUnify()
        optionMenuReplyReview = viewMenu.findViewById(R.id.optionMenuReply)
        bottomSheetReplyReview?.setChild(viewMenu)
    }

    private fun setTemplateList(data: List<ReplyTemplateUiModel>) {
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        list_template?.apply {
            layoutManager = linearLayoutManager
            if (itemDecorationCount == 0) {
                addItemDecoration(PaddingItemDecoratingReviewSeller())
            }
            adapter = reviewTemplateListAdapter
        }
        if (data.isEmpty()) {
            list_template?.hide()
        } else {
            reviewTemplateListAdapter.submitList(data)
            list_template?.show()
        }
    }

    override fun onItemReviewTemplateClicked(view: View, title: String) {
        val message = replyTemplateList?.firstOrNull { it.title == title }?.message
        showTextReplyEditText(message.orEmpty())
    }

}
