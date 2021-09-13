package com.tokopedia.review.feature.inbox.buyerreview.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.facebook.CallbackManager
import com.facebook.CallbackManager.Factory.create
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
import com.tokopedia.review.common.util.ReviewErrorHandler.getErrorMessage
import com.tokopedia.review.feature.inbox.buyerreview.analytics.AppScreen
import com.tokopedia.review.feature.inbox.buyerreview.analytics.ReputationTracking
import com.tokopedia.review.feature.inbox.buyerreview.di.DaggerReputationComponent
import com.tokopedia.review.feature.inbox.buyerreview.view.activity.InboxReputationDetailActivity
import com.tokopedia.review.feature.inbox.buyerreview.view.activity.InboxReputationReportActivity
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.InboxReputationDetailAdapter
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.ReputationAdapter.ReputationListener
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactory
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactoryImpl
import com.tokopedia.review.feature.inbox.buyerreview.view.customview.ShareReviewDialog
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail
import com.tokopedia.review.feature.inbox.buyerreview.view.presenter.InboxReputationDetailPresenter
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.*
import com.tokopedia.review.inbox.R
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationDetailFragment constructor() : BaseDaggerFragment(),
    InboxReputationDetail.View, ReputationListener {
    private var listProduct: RecyclerView? = null
    private var swipeToRefresh: SwipeToRefresh? = null
    private var adapter: InboxReputationDetailAdapter? = null
    private var shareReviewDialog: ShareReviewDialog? = null
    private var callbackManager: CallbackManager? = null
    private var mainView: View? = null
    private var progressDialog: ProgressDialog? = null

    @kotlin.jvm.JvmField
    @Inject
    var presenter: InboxReputationDetailPresenter? = null

    @kotlin.jvm.JvmField
    @Inject
    var persistentCacheManager: PersistentCacheManager? = null

    @kotlin.jvm.JvmField
    @Inject
    override var userSession: UserSessionInterface? = null

    @kotlin.jvm.JvmField
    @Inject
    var reputationTracking: ReputationTracking? = null
    private var reputationId: String? = "0"
    var orderId: String? = "0"
        private set
    private var role: Int = 0
    override fun getScreenName(): String {
        return AppScreen.SCREEN_INBOX_REPUTATION_DETAIL
    }

    override fun initInjector() {
        val baseAppComponent: BaseAppComponent =
            (requireContext().getApplicationContext() as BaseMainApplication).getBaseAppComponent()
        val reputationComponent: DaggerReputationComponent = DaggerReputationComponent
            .builder()
            .baseAppComponent(baseAppComponent)
            .build() as DaggerReputationComponent
        reputationComponent.inject(this)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVar()
    }

    private fun initVar() {
        if (getArguments()!!.getBoolean(
                InboxReputationDetailActivity.Companion.ARGS_IS_FROM_APPLINK,
                false
            )
        ) {
            reputationId = getArguments()!!.getString(
                InboxReputationDetailActivity.Companion.REPUTATION_ID,
                "0"
            )
        } else if (persistentCacheManager != null) {
            try {
                val passModel: InboxReputationDetailPassModel? =
                    persistentCacheManager!!.get<InboxReputationDetailPassModel>(
                        InboxReputationDetailActivity.Companion.CACHE_PASS_DATA,
                        InboxReputationDetailPassModel::class.java
                    )
                reputationId = passModel.getReputationId()
                role = passModel.getRole()
                setToolbar(passModel.getInvoice(), passModel.getCreateTime())
            } catch (e: Exception) {
                // Ignore cache expired exception
            }
        }
        callbackManager = create()
        val typeFactory: InboxReputationDetailTypeFactory =
            InboxReputationDetailTypeFactoryImpl(this)
        adapter = InboxReputationDetailAdapter(typeFactory)
    }

    private fun setToolbar(title: String?, subtitle: String?) {
        if (getActivity() != null) {
            if ((getActivity() as AppCompatActivity?)!!.getSupportActionBar() != null) {
                (getActivity() as AppCompatActivity?)!!.getSupportActionBar()!!.hide()
            }
            val toolbar: HeaderUnify =
                getActivity()!!.findViewById(R.id.headerInboxReputationDetail)
            (getActivity() as AppCompatActivity?)!!.setSupportActionBar(toolbar)
            toolbar.setTitle(title)
            toolbar.headerSubTitle = (subtitle)!!
        }
    }

    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setRetainInstance(true)
        val parentView: View = inflater.inflate(
            R.layout.fragment_inbox_reputation_detail, container,
            false
        )
        mainView = parentView.findViewById(R.id.main)
        swipeToRefresh = parentView.findViewById(R.id.swipe_refresh_inbox_reputation_detail)
        listProduct = parentView.findViewById(R.id.product_list)
        prepareView()
        presenter!!.attachView(this)
        return parentView
    }

    @SuppressLint("WrongConstant")
    private fun prepareView() {
        listProduct!!.setLayoutManager(
            LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        listProduct!!.setAdapter(adapter)
        swipeToRefresh!!.setOnRefreshListener(onRefresh())
        initProgressDialog()
    }

    private fun onRefresh(): OnRefreshListener {
        return object : OnRefreshListener {
            public override fun onRefresh() {
                refreshPage()
            }
        }
    }

    private fun initProgressDialog() {
        if (getContext() != null) {
            progressDialog = ProgressDialog(getContext())
            progressDialog!!.setTitle("")
            progressDialog!!.setMessage(getContext()!!.getString(R.string.progress_dialog_loading))
            progressDialog!!.setCancelable(false)
        }
    }

    public override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!TextUtils.isEmpty(reputationId)) {
            presenter!!.getInboxDetail(
                reputationId,
                getArguments()!!.getInt(InboxReputationDetailActivity.Companion.ARGS_TAB, -1)
            )
        } else {
            getActivity()!!.finish()
        }
    }

    public override fun showLoading() {
        adapter!!.showLoading()
        adapter!!.notifyDataSetChanged()
    }

    public override fun onErrorGetInboxDetail(throwable: Throwable?) {
        if ((getActivity() != null) && (mainView != null) && (getContext() != null)) NetworkErrorHelper.showEmptyState(
            getActivity(),
            mainView,
            getErrorMessage(
                (getContext())!!, (throwable)!!
            ),
            NetworkErrorHelper.RetryClickedListener({
                presenter!!.getInboxDetail(
                    reputationId,
                    getArguments()!!.getInt(InboxReputationDetailActivity.Companion.ARGS_TAB, -1)
                )
            })
        )
    }

    public override fun finishLoading() {
        if (progressDialog != null && getActivity() != null) {
            adapter!!.removeLoading()
            adapter!!.notifyDataSetChanged()
        }
    }

    public override fun onSuccessGetInboxDetail(
        inboxReputationItemUiModel: InboxReputationItemUiModel?,
        list: List<Visitable<*>>
    ) {
        role = inboxReputationItemUiModel.getRole()
        if (!list.isEmpty() && list.get(0) is InboxReputationDetailItemUiModel) {
            orderId = (list.get(0) as InboxReputationDetailItemUiModel).getOrderId()
        }
        setToolbar(
            inboxReputationItemUiModel.getInvoice(),
            inboxReputationItemUiModel.getCreateTime()
        )
        adapter!!.clearList()
        adapter!!.addHeader(createHeaderModel(inboxReputationItemUiModel))
        adapter!!.addList(list)
        adapter!!.notifyDataSetChanged()
        reputationTracking!!.onSeeSellerFeedbackPage(orderId)
    }

    public override fun onErrorSendSmiley(errorMessage: String?) {
        if (getActivity() != null) NetworkErrorHelper.showSnackbar(getActivity(), errorMessage)
    }

    public override fun showLoadingDialog() {
        if (!progressDialog!!.isShowing() && getActivity() != null) progressDialog!!.show()
    }

    public override fun finishLoadingDialog() {
        if (progressDialog!!.isShowing() && (progressDialog != null) && (getContext() != null)) progressDialog!!.dismiss()
    }

    public override fun showRefresh() {
        swipeToRefresh!!.setRefreshing(true)
    }

    public override fun onErrorRefreshInboxDetail(throwable: Throwable?) {
        if (getActivity() != null!! and getContext() != null) NetworkErrorHelper.showSnackbar(
            getActivity(), getErrorMessage(
                (getContext())!!, (throwable)!!
            )
        )
    }

    public override fun onSuccessRefreshGetInboxDetail(
        inboxReputationViewModel: InboxReputationItemUiModel?,
        list: List<Visitable<*>>
    ) {
        if (!list.isEmpty() && list.get(0) is InboxReputationDetailItemUiModel) {
            orderId = (list.get(0) as InboxReputationDetailItemUiModel).getOrderId()
        }
        adapter!!.clearList()
        adapter!!.addHeader(createHeaderModel(inboxReputationViewModel))
        adapter!!.addList(list)
        adapter!!.notifyDataSetChanged()
        getActivity()!!.setResult(Activity.RESULT_OK)
    }

    private fun createHeaderModel(
        inboxReputationViewModel: InboxReputationItemUiModel?
    ): InboxReputationDetailHeaderUiModel {
        return InboxReputationDetailHeaderUiModel(
            inboxReputationViewModel.getRevieweePicture(),
            inboxReputationViewModel.getRevieweeName(),
            getTextDeadline(inboxReputationViewModel),
            inboxReputationViewModel.getReputationDataUiModel(),
            inboxReputationViewModel.getRole(),
            inboxReputationViewModel.getRevieweeBadgeCustomerUiModel(),
            inboxReputationViewModel.getRevieweeBadgeSellerUiModel(),
            inboxReputationViewModel.getShopId(),
            inboxReputationViewModel.getUserId()
        )
    }

    private fun getTextDeadline(element: InboxReputationItemUiModel?): String {
        return (getContext()!!.getString(R.string.deadline_prefix)
                + " " + element.getReputationDaysLeft() + " " +
                getContext()!!.getString(R.string.deadline_suffix))
    }

    public override fun finishRefresh() {
        swipeToRefresh!!.setRefreshing(false)
    }

    public override fun goToPreviewImage(position: Int, list: ArrayList<ImageUpload?>?) {
        val listLocation: ArrayList<String?> = ArrayList()
        val listDesc: ArrayList<String?> = ArrayList()
        for (image: ImageUpload? in list!!) {
            listLocation.add(image.getPicSrcLarge())
            listDesc.add(image.getDescription())
        }
        startActivity(
            getCallingIntent(
                (getContext())!!,
                listLocation,
                listDesc,
                position
            )
        )
    }

    override val tab: Int
        get() {
            return getArguments()!!.getInt(InboxReputationDetailActivity.Companion.ARGS_TAB)
        }

    public override fun onGoToReportReview(shopId: Long, reviewId: String?) {
        startActivityForResult(
            InboxReputationReportActivity.Companion.getCallingIntent(
                getActivity(),
                shopId,
                reviewId
            ),
            REQUEST_REPORT_REVIEW
        )
    }

    public override fun onSuccessSendSmiley(score: Int) {
        refreshPage()
    }

    public override fun onErrorFavoriteShop(errorMessage: String?) {
        if (getActivity() != null) NetworkErrorHelper.showSnackbar(getActivity(), errorMessage)
    }

    public override fun onSuccessFavoriteShop() {
        adapter.getHeader().getRevieweeBadgeSellerUiModel().setIsFavorited(
            if (adapter.getHeader().getRevieweeBadgeSellerUiModel().getIsFavorited() == 1) 0 else 1
        )
        adapter!!.notifyItemChanged(0)
    }

    public override fun onDeleteReviewResponse(element: InboxReputationDetailItemUiModel) {
        presenter!!.deleteReviewResponse(
            element.getReviewId(),
            element.getProductId(),
            element.getShopId().toString(),
            element.getReputationId().toString()
        )
    }

    public override fun onErrorDeleteReviewResponse(errorMessage: String?) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage)
    }

    public override fun onSuccessDeleteReviewResponse() {
        refreshPage()
    }

    public override fun onSendReplyReview(
        element: InboxReputationDetailItemUiModel,
        replyReview: String?
    ) {
        presenter!!.sendReplyReview(
            element.getReputationId(), element.getProductId(),
            element.getShopId(), element.getReviewId(), replyReview
        )
    }

    public override fun onErrorReplyReview(errorMessage: String?) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage)
    }

    public override fun onSuccessReplyReview() {
        refreshPage()
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.reply_response_send))
    }

    public override fun onShareReview(
        element: InboxReputationDetailItemUiModel,
        adapterPosition: Int
    ) {
        KeyboardHandler.DropKeyboard(getActivity(), getView())
        if (shareReviewDialog == null && callbackManager != null) {
            shareReviewDialog = ShareReviewDialog(
                getActivity(), callbackManager!!,
                this
            )
        }
        if (shareReviewDialog != null) {
            shareReviewDialog!!.setModel(
                ShareModel(
                    element.getProductName(),
                    element.getReview(),
                    element.getProductUrl(),
                    element.getProductAvatar()
                )
            )
            shareReviewDialog!!.show()
        }
        reputationTracking!!.onClickShareMenuReviewTracker(
            element.getOrderId(),
            element.getProductId(),
            adapterPosition
        )
    }

    public override fun onGoToProductDetail(
        productId: String?,
        productAvatar: String?,
        productName: String?
    ) {
        if (getContext() != null) {
            val intent: Intent = RouteManager.getIntent(
                getContext(),
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                productId
            )
            getContext()!!.startActivity(intent)
        }
    }

    public override fun onSmoothScrollToReplyView(adapterPosition: Int) {
        if ((adapterPosition > -1) && (adapterPosition < adapter!!.getList().size
                    ) && adapter!!.getList()
                .get(adapterPosition) is InboxReputationDetailItemUiModel
        ) {
            listProduct!!.smoothScrollToPosition(adapterPosition)
        }
    }

    public override fun onGoToProfile(reviewerId: Long) {
        startActivity(
            RouteManager.getIntent(
                getActivity(),
                ApplinkConst.PROFILE,
                reviewerId.toString()
            )
        )
    }

    public override fun onGoToShopInfo(shopId: Long) {
        val intent: Intent =
            RouteManager.getIntent(getActivity(), ApplinkConst.SHOP, shopId.toString())
        startActivity(intent)
    }

    public override fun onReputationSmileyClicked(name: String?, score: String?) {
        if (!TextUtils.isEmpty(score)) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(
                (getContext())!!
            )
            builder.setMessage(getReputationSmileyMessage(name))
            builder.setPositiveButton(getString(R.string.submit_review),
                object : DialogInterface.OnClickListener {
                    public override fun onClick(dialog: DialogInterface, which: Int) {
                        presenter!!.sendSmiley(reputationId, score, role)
                    }
                })
            builder.setNegativeButton(getString(R.string.title_cancel),
                object : DialogInterface.OnClickListener {
                    public override fun onClick(dialog: DialogInterface, param: Int) {
                        dialog.dismiss()
                    }
                })
            val dialog: Dialog = builder.create()
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.show()
            reputationTracking!!.onClickSmileyShopReviewTracker(name, orderId)
        }
    }

    public override fun onClickToggleReply(
        element: InboxReputationDetailItemUiModel,
        adapterPosition: Int
    ) {
        reputationTracking!!.onClickToggleReplyReviewTracker(
            element.getOrderId(),
            element.getProductId(),
            adapterPosition
        )
    }

    public override fun onGoToShopDetail(shopId: Long) {
        val intent: Intent =
            RouteManager.getIntent(getActivity(), ApplinkConst.SHOP, shopId.toString())
        startActivity(intent)
    }

    public override fun onGoToPeopleProfile(userId: Long) {
        startActivity(
            RouteManager.getIntent(
                getActivity(),
                ApplinkConst.PROFILE,
                userId.toString()
            )
        )
    }

    public override fun onClickReviewOverflowMenu(
        inboxReputationDetailItemUiModel: InboxReputationDetailItemUiModel,
        adapterPosition: Int
    ) {
        reputationTracking!!.onClickReviewOverflowMenuTracker(
            inboxReputationDetailItemUiModel.getOrderId(),
            inboxReputationDetailItemUiModel.getProductId(),
            adapterPosition
        )
    }

    private fun getReputationSmileyMessage(name: String?): String {
        return (getString(R.string.smiley_prompt_prefix) + " " + name
                + " " + smileySuffixMessage)
    }

    private val smileySuffixMessage: String
        private get() {
            return getString(R.string.smiley_prompt_suffix_shop)
        }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (callbackManager != null) {
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        }
        if (requestCode == REQUEST_GIVE_REVIEW && resultCode == Activity.RESULT_OK) {
            refreshPage()
            getActivity()!!.setResult(Activity.RESULT_OK)
        } else if (requestCode == REQUEST_REPORT_REVIEW && resultCode == Activity.RESULT_OK) {
            NetworkErrorHelper.showSnackbar(
                getActivity(),
                getString(R.string.success_report_review)
            )
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    private fun refreshPage() {
        presenter!!.refreshPage(
            reputationId,
            getArguments()!!.getInt(InboxReputationDetailActivity.Companion.ARGS_TAB, -1)
        )
    }

    public override fun onDestroyView() {
        super.onDestroyView()
        if (presenter != null) presenter!!.detachView()
        callbackManager = null
    }

    companion object {
        private val REQUEST_GIVE_REVIEW: Int = 101
        private val REQUEST_EDIT_REVIEW: Int = 102
        private val REQUEST_REPORT_REVIEW: Int = 103
        private val PUAS_SCORE: Int = 2 // FROM API
        fun createInstance(
            tab: Int,
            isFromApplink: Boolean,
            reputationId: String?
        ): InboxReputationDetailFragment {
            val fragment: InboxReputationDetailFragment = InboxReputationDetailFragment()
            val bundle: Bundle = Bundle()
            bundle.putInt(InboxReputationDetailActivity.Companion.ARGS_TAB, tab)
            bundle.putBoolean(
                InboxReputationDetailActivity.Companion.ARGS_IS_FROM_APPLINK,
                isFromApplink
            )
            bundle.putString(InboxReputationDetailActivity.Companion.REPUTATION_ID, reputationId)
            fragment.setArguments(bundle)
            return fragment
        }
    }
}