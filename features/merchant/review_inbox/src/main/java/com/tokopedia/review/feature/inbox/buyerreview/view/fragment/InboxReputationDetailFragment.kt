package com.tokopedia.review.feature.inbox.buyerreview.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.header.HeaderUnify
import com.tokopedia.imagepreview.ImagePreviewActivity.Companion.getCallingIntent
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.review.common.util.ClipboardHandler
import com.tokopedia.review.common.util.ReviewErrorHandler.getErrorMessage
import com.tokopedia.review.feature.inbox.buyerreview.analytics.AppScreen
import com.tokopedia.review.feature.inbox.buyerreview.analytics.ReputationTracking
import com.tokopedia.review.feature.inbox.buyerreview.di.DaggerReputationComponent
import com.tokopedia.review.feature.inbox.buyerreview.view.activity.InboxReputationDetailActivity
import com.tokopedia.review.feature.inbox.buyerreview.view.activity.InboxReputationReportActivity
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.InboxReputationDetailAdapter
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.ReputationAdapter.ReputationListener
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactoryImpl
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail
import com.tokopedia.review.feature.inbox.buyerreview.view.presenter.InboxReputationDetailPresenter
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ImageUpload
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailHeaderUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailItemUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailPassModel
import com.tokopedia.review.feature.inbox.buyerreview.view.viewmodel.inboxdetail.InboxReputationDetailViewModel
import com.tokopedia.review.inbox.R
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationDetailFragment : BaseDaggerFragment(),
    InboxReputationDetail.View, ReputationListener {

    private var listProduct: RecyclerView? = null
    private var swipeToRefresh: SwipeToRefresh? = null
    private var mainView: View? = null
    private var progressDialog: ProgressDialog? = null

    private var adapter =
        InboxReputationDetailAdapter(InboxReputationDetailTypeFactoryImpl(this, this))

    @Inject
    lateinit var presenter: InboxReputationDetailPresenter

    @Inject
    lateinit var persistentCacheManager: PersistentCacheManager

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var reputationTracking: ReputationTracking

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val inboxReputationDetailViewModel: InboxReputationDetailViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(InboxReputationDetailViewModel::class.java)
    }

    private var reputationId: String = ""
    var orderId: String = ""
        private set
    private var role: Int = 0

    override fun getScreenName(): String {
        return AppScreen.SCREEN_INBOX_REPUTATION_DETAIL
    }

    override fun initInjector() {
        val baseAppComponent: BaseAppComponent =
            (requireContext().applicationContext as BaseMainApplication).baseAppComponent
        val reputationComponent: DaggerReputationComponent = DaggerReputationComponent
            .builder()
            .baseAppComponent(baseAppComponent)
            .build() as DaggerReputationComponent
        reputationComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVar()
    }

    override fun getShopId(): String {
        return userSession.shopId
    }

    private fun initVar() {
        if (arguments?.getBoolean(
                InboxReputationDetailActivity.ARGS_IS_FROM_APPLINK,
                false
            ) == true
        ) {
            reputationId = arguments?.getString(
                InboxReputationDetailActivity.REPUTATION_ID,
                ""
            ) ?: ""
        } else {
            try {
                val passModel: InboxReputationDetailPassModel? =
                    persistentCacheManager.get<InboxReputationDetailPassModel>(
                        InboxReputationDetailActivity.CACHE_PASS_DATA,
                        InboxReputationDetailPassModel::class.java
                    )
                passModel?.let {
                    reputationId = it.reputationId
                    role = it.role
                    setToolbar(it.invoice, it.createTime)
                }
            } catch (e: Exception) {
                // Ignore cache expired exception
            }
        }
    }

    private fun setToolbar(title: String, subtitle: String) {
        if (activity != null) {
            if ((activity as AppCompatActivity?)?.supportActionBar != null) {
                (activity as AppCompatActivity?)?.supportActionBar?.hide()
            }
            val toolbar: HeaderUnify? =
                activity?.findViewById(R.id.headerInboxReputationDetail)
            (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
            toolbar?.title = title
            toolbar?.headerSubTitle = subtitle
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        retainInstance = true
        val parentView: View = inflater.inflate(
            R.layout.fragment_inbox_reputation_detail, container,
            false
        )
        mainView = parentView.findViewById(R.id.main)
        swipeToRefresh = parentView.findViewById(R.id.swipe_refresh_inbox_reputation_detail)
        listProduct = parentView.findViewById(R.id.product_list)
        prepareView()
        presenter.attachView(this)
        return parentView
    }

    @SuppressLint("WrongConstant")
    private fun prepareView() {
        listProduct?.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        listProduct?.adapter = adapter
        swipeToRefresh?.setOnRefreshListener(onRefresh())
        initProgressDialog()
    }

    private fun onRefresh(): OnRefreshListener {
        return object : OnRefreshListener {
            override fun onRefresh() {
                refreshPage()
            }
        }
    }

    private fun initProgressDialog() {
        context?.let {
            progressDialog = ProgressDialog(it)
            progressDialog?.setTitle("")
            progressDialog?.setMessage(getContext()?.getString(R.string.progress_dialog_loading))
            progressDialog?.setCancelable(false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!TextUtils.isEmpty(reputationId)) {
            presenter.getInboxDetail(
                reputationId,
                arguments?.getInt(InboxReputationDetailActivity.ARGS_TAB, -1) ?: -1
            )
        } else {
            activity?.finish()
        }
        observeInsertReviewReply()
        observeDeleteReviewReply()
    }

    override fun showLoading() {
        adapter.showLoading()
    }

    override fun onErrorGetInboxDetail(throwable: Throwable) {
        NetworkErrorHelper.showEmptyState(
            activity,
            mainView,
            context?.let { getErrorMessage(it, throwable) }
        ) {
            arguments?.getInt(InboxReputationDetailActivity.ARGS_TAB, -1)?.let {
                presenter.getInboxDetail(
                    reputationId,
                    it
                )
            }
        }
    }

    override fun finishLoading() {
        if (progressDialog != null && activity != null) {
            adapter.removeLoading()
        }
    }

    override fun onSuccessGetInboxDetail(
        inboxReputationItemUiModel: InboxReputationItemUiModel,
        visitables: List<Visitable<*>>
    ) {
        role = inboxReputationItemUiModel.role
        if (visitables.isNotEmpty() && visitables[0] is InboxReputationDetailItemUiModel) {
            orderId = (visitables[0] as InboxReputationDetailItemUiModel).orderId
        }
        setToolbar(
            inboxReputationItemUiModel.invoice,
            inboxReputationItemUiModel.createTime
        )
        adapter.apply {
            clearList()
            addHeader(createHeaderModel(inboxReputationItemUiModel))
            addList(visitables)
            notifyDataSetChanged()
        }
        reputationTracking.onSeeSellerFeedbackPage(orderId)
    }

    override fun onErrorSendSmiley(errorMessage: String?) {
        if (activity != null) NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun showLoadingDialog() {
        progressDialog?.let {
            if (!it.isShowing) it.show()
        }
    }

    override fun finishLoadingDialog() {
        progressDialog?.let {
            if (it.isShowing) it.dismiss()
        }
    }

    override fun showRefresh() {
        swipeToRefresh?.isRefreshing = true
    }

    override fun onErrorRefreshInboxDetail(throwable: Throwable) {
        NetworkErrorHelper.showSnackbar(activity, context?.let { getErrorMessage(it, throwable) })
    }

    override fun onSuccessRefreshGetInboxDetail(
        inboxReputationViewModel: InboxReputationItemUiModel,
        visitables: List<Visitable<*>>
    ) {
        if (visitables.isNotEmpty() && visitables[0] is InboxReputationDetailItemUiModel) {
            orderId = (visitables[0] as InboxReputationDetailItemUiModel).orderId
        }
        adapter.apply {
            clearList()
            addHeader(createHeaderModel(inboxReputationViewModel))
            addList(visitables)
            notifyDataSetChanged()
        }
        activity?.setResult(Activity.RESULT_OK)
    }

    private fun createHeaderModel(
        inboxReputationViewModel: InboxReputationItemUiModel
    ): InboxReputationDetailHeaderUiModel {
        return InboxReputationDetailHeaderUiModel(
            inboxReputationViewModel.revieweePicture,
            inboxReputationViewModel.revieweeName,
            getTextDeadline(inboxReputationViewModel),
            inboxReputationViewModel.reputationDataUiModel,
            inboxReputationViewModel.role,
            inboxReputationViewModel.revieweeBadgeCustomerUiModel,
            inboxReputationViewModel.revieweeBadgeSellerUiModel,
            inboxReputationViewModel.shopId,
            inboxReputationViewModel.userId
        )
    }

    private fun getTextDeadline(element: InboxReputationItemUiModel): String {
        return (context?.getString(R.string.deadline_prefix)
                + " " + element.reputationDaysLeft + " " +
                context?.getString(R.string.deadline_suffix))
    }

    override fun finishRefresh() {
        swipeToRefresh?.isRefreshing = false
    }

    override fun goToPreviewImage(position: Int, list: ArrayList<ImageUpload>) {
        val listLocation: ArrayList<String> = ArrayList()
        val listDesc: ArrayList<String> = ArrayList()
        list.forEach {
            listLocation.add(it.picSrcLarge)
            listDesc.add(it.description)
        }
        startActivity(
            context?.let {
                getCallingIntent(
                    it,
                    listLocation,
                    listDesc,
                    position
                )
            }
        )
    }

    override val tab: Int
        get() {
            return arguments?.getInt(InboxReputationDetailActivity.ARGS_TAB) ?: 0
        }

    override fun getErrorMessage(throwable: Throwable): String {
        val message = context?.let { getErrorMessage(it, throwable) }
        if (message.isNullOrEmpty()) {
            return context?.getString(com.tokopedia.abstraction.R.string.default_request_error_unknown) ?: ""
        }
        return message
    }

    override fun onGoToReportReview(shopId: Long, reviewId: String?) {
        startActivityForResult(
            InboxReputationReportActivity.getCallingIntent(
                activity,
                shopId,
                reviewId
            ),
            REQUEST_REPORT_REVIEW
        )
    }

    override fun onSuccessSendSmiley(score: Int) {
        refreshPage()
    }

    override fun onErrorFavoriteShop(errorMessage: String?) {
        if (activity != null) NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun onSuccessFavoriteShop() {
        adapter.header?.revieweeBadgeSellerUiModel?.isFavorited =
            if (adapter.header?.revieweeBadgeSellerUiModel?.isFavorited == 1) 0 else 1
        adapter.notifyItemChanged(0)
    }

    override fun onDeleteReviewResponse(element: InboxReputationDetailItemUiModel) {
        showLoadingDialog()
        inboxReputationDetailViewModel.deleteReviewResponse(element.reviewId)
    }

    private fun onErrorDeleteReviewResponse(errorMessage: String?) {
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    private fun onSuccessDeleteReviewResponse() {
        refreshPage()
    }

    override fun onSendReplyReview(
        element: InboxReputationDetailItemUiModel,
        replyReview: String
    ) {
        showLoadingDialog()
        inboxReputationDetailViewModel.insertReviewReply(element.reviewId, replyReview)
    }

    private fun onErrorReplyReview(errorMessage: String?) {
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    private fun onSuccessReplyReview() {
        refreshPage()
        NetworkErrorHelper.showSnackbar(activity, getString(R.string.reply_response_send))
    }

    override fun onShareReview(
        inboxReputationDetailItemUiModel: InboxReputationDetailItemUiModel,
        adapterPosition: Int
    ) {
        KeyboardHandler.DropKeyboard(activity, view)
        ClipboardHandler.CopyToClipboard(activity, inboxReputationDetailItemUiModel.productUrl)
        Toast.makeText(activity, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        reputationTracking.onClickShareMenuReviewTracker(
            inboxReputationDetailItemUiModel.orderId,
            inboxReputationDetailItemUiModel.productId,
            adapterPosition
        )
    }

    override fun onGoToProductDetail(
        productId: String?,
        productAvatar: String?,
        productName: String?
    ) {
        context?.let {
            val intent: Intent = RouteManager.getIntent(
                it,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                productId
            )
            it.startActivity(intent)
        }
    }

    override fun onSmoothScrollToReplyView(adapterPosition: Int) {
        if ((adapterPosition > -1) && (adapterPosition < adapter.getList().size
                    ) && adapter.getList()[adapterPosition] is InboxReputationDetailItemUiModel
        ) {
            listProduct?.smoothScrollToPosition(adapterPosition)
        }
    }

    override fun onGoToProfile(reviewerId: Long) {
        startActivity(
            RouteManager.getIntent(
                activity,
                ApplinkConst.PROFILE,
                reviewerId.toString()
            )
        )
    }

    override fun onGoToShopInfo(shopId: Long) {
        val intent: Intent =
            RouteManager.getIntent(activity, ApplinkConst.SHOP, shopId.toString())
        startActivity(intent)
    }

    override fun onReputationSmileyClicked(name: String, value: String) {
        if (!TextUtils.isEmpty(value)) {
            context?.let {
                val builder = AlertDialog.Builder(it)
                builder.setMessage(getReputationSmileyMessage(name))
                builder.setPositiveButton(
                    getString(R.string.submit_review)
                ) { _, _ -> presenter.sendSmiley(reputationId, value, role) }
                builder.setNegativeButton(
                    getString(R.string.title_cancel)
                ) { dialog, _ -> dialog.dismiss() }
                val dialog: Dialog = builder.create()
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.show()
                reputationTracking.onClickSmileyShopReviewTracker(name, orderId)
            }
        }
    }

    override fun onClickToggleReply(
        inboxReputationDetailItemUiModel: InboxReputationDetailItemUiModel,
        adapterPosition: Int
    ) {
        reputationTracking.onClickToggleReplyReviewTracker(
            inboxReputationDetailItemUiModel.orderId,
            inboxReputationDetailItemUiModel.productId,
            adapterPosition
        )
    }

    override fun onGoToShopDetail(shopId: Long) {
        val intent: Intent =
            RouteManager.getIntent(activity, ApplinkConst.SHOP, shopId.toString())
        startActivity(intent)
    }

    override fun onGoToPeopleProfile(userId: Long) {
        startActivity(
            RouteManager.getIntent(
                activity,
                ApplinkConst.PROFILE,
                userId.toString()
            )
        )
    }

    override fun onClickReviewOverflowMenu(
        inboxReputationDetailItemUiModel: InboxReputationDetailItemUiModel,
        adapterPosition: Int
    ) {
        reputationTracking.onClickReviewOverflowMenuTracker(
            inboxReputationDetailItemUiModel.orderId,
            inboxReputationDetailItemUiModel.productId,
            adapterPosition
        )
    }

    private fun getReputationSmileyMessage(name: String?): String {
        return (getString(R.string.smiley_prompt_prefix) + " " + name
                + " " + smileySuffixMessage)
    }

    private val smileySuffixMessage: String
        get() {
            return getString(R.string.smiley_prompt_suffix_shop)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_GIVE_REVIEW && resultCode == Activity.RESULT_OK) {
            refreshPage()
            activity?.setResult(Activity.RESULT_OK)
        } else if (requestCode == REQUEST_REPORT_REVIEW && resultCode == Activity.RESULT_OK) {
            NetworkErrorHelper.showSnackbar(
                activity,
                getString(R.string.success_report_review)
            )
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    private fun refreshPage() {
        arguments?.getInt(InboxReputationDetailActivity.ARGS_TAB, -1)?.let {
            presenter.refreshPage(
                reputationId,
                it
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    private fun observeInsertReviewReply() {
        inboxReputationDetailViewModel.insertReviewReplyResult.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    finishLoadingDialog()
                    if (it.data.success) {
                        onSuccessReplyReview()
                    } else {
                        val message = ErrorHandler.getErrorMessage(
                            context,
                            MessageErrorException(getString(R.string.review_reply_unknown_error))
                        )
                        onErrorReplyReview(message)
                    }
                }
                is Fail -> {
                    finishLoadingDialog()
                    onErrorReplyReview(ErrorHandler.getErrorMessage(context, it.throwable))
                }
            }
        })
    }

    private fun observeDeleteReviewReply() {
        inboxReputationDetailViewModel.deleteReviewReplyResult.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    finishLoadingDialog()
                    if (it.data.success) {
                        onSuccessDeleteReviewResponse()
                    } else {
                        val message = ErrorHandler.getErrorMessage(
                            context,
                            MessageErrorException(getString(R.string.review_delete_unknown_error))
                        )
                        onErrorDeleteReviewResponse(message)
                    }
                }
                is Fail -> {
                    finishLoadingDialog()
                    onErrorDeleteReviewResponse(ErrorHandler.getErrorMessage(context, it.throwable))
                }
            }
        })
    }

    companion object {
        private const val REQUEST_GIVE_REVIEW: Int = 101
        private const val REQUEST_REPORT_REVIEW: Int = 103
        fun createInstance(
            tab: Int,
            isFromApplink: Boolean,
            reputationId: String?
        ): InboxReputationDetailFragment {
            val fragment = InboxReputationDetailFragment()
            val bundle = Bundle()
            bundle.putInt(InboxReputationDetailActivity.ARGS_TAB, tab)
            bundle.putBoolean(
                InboxReputationDetailActivity.ARGS_IS_FROM_APPLINK,
                isFromApplink
            )
            bundle.putString(InboxReputationDetailActivity.REPUTATION_ID, reputationId)
            fragment.arguments = bundle
            return fragment
        }
    }
}