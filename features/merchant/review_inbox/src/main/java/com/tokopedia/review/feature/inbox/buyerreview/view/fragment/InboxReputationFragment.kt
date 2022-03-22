package com.tokopedia.review.feature.inbox.buyerreview.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.review.common.util.ReviewErrorHandler.getErrorMessage
import com.tokopedia.review.feature.inbox.buyerreview.analytics.AppScreen
import com.tokopedia.review.feature.inbox.buyerreview.analytics.ReputationTracking
import com.tokopedia.review.feature.inbox.buyerreview.di.DaggerReputationComponent
import com.tokopedia.review.feature.inbox.buyerreview.view.activity.InboxReputationDetailActivity
import com.tokopedia.review.feature.inbox.buyerreview.view.activity.InboxReputationFilterActivity
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.InboxReputationAdapter
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inbox.InboxReputationTypeFactoryImpl
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.SellerMigrationReviewViewHolder.SellerMigrationReviewClickListener
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation
import com.tokopedia.review.feature.inbox.buyerreview.view.presenter.InboxReputationPresenter
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.ReputationDataUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.SellerMigrationReviewModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailPassModel
import com.tokopedia.review.inbox.R
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import com.tokopedia.user.session.UserSession
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author by nisie on 8/11/17.
 */
class InboxReputationFragment : BaseDaggerFragment(), InboxReputation.View,
    SearchInputView.Listener, SellerMigrationReviewClickListener {

    private var searchView: SearchInputView? = null
    private var mainList: RecyclerView? = null
    private var swipeToRefresh: SwipeToRefresh? = null
    private var adapter = InboxReputationAdapter(InboxReputationTypeFactoryImpl(this, this))

    private var timeFilter: String = ""
    private var scoreFilter: String = ""
    private var filterButton: View? = null
    private val sellerMigrationReviewModel: SellerMigrationReviewModel =
        SellerMigrationReviewModel()

    @Inject
    lateinit var presenter: InboxReputationPresenter

    @Inject
    lateinit var persistentCacheManager: PersistentCacheManager

    @Inject
    lateinit var reputationTracking: ReputationTracking

    @Inject
    lateinit var userSession: UserSession

    override fun getScreenName(): String {
        return AppScreen.SCREEN_INBOX_REPUTATION
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
        setHasOptionsMenu(true)
        initVar(savedInstanceState)
    }

    private fun openFilter() {
        val intent: Intent = InboxReputationFilterActivity.createIntent(
            activity,
            timeFilter, scoreFilter, tab
        )
        startActivityForResult(intent, REQUEST_FILTER)
    }

    private fun initVar(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            timeFilter = savedInstanceState.getString(ARGS_TIME_FILTER, "")
            scoreFilter = savedInstanceState.getString(ARGS_SCORE_FILTER, "")
        } else {
            timeFilter = ""
            scoreFilter = ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        retainInstance = true
        val parentView: View =
            inflater.inflate(R.layout.fragment_inbox_reputation, container, false)
        mainList = parentView.findViewById<View>(R.id.review_list) as RecyclerView?
        swipeToRefresh =
            parentView.findViewById<View>(R.id.swipe_refresh_inbox_reputation) as SwipeToRefresh?
        searchView = parentView.findViewById<View>(R.id.search) as SearchInputView?
        searchView?.setDelayTextChanged(DEFAULT_DELAY_TEXT_CHANGED)
        searchView?.setListener(this)
        filterButton = parentView.findViewById(R.id.filter_button)
        prepareView()
        presenter.attachView(this)
        return parentView
    }

    private fun prepareView() {
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mainList?.layoutManager = layoutManager
        mainList?.adapter = adapter
        mainList?.addOnScrollListener(onScroll(layoutManager))
        swipeToRefresh?.setOnRefreshListener { refreshPage() }
        setQueryHint()
        filterButton?.setOnClickListener { openFilter() }
    }

    private fun setQueryHint() {
        if (context != null) {
            if (tab == ReviewInboxConstants.TAB_BUYER_REVIEW) {
                searchView?.setSearchHint(getString(R.string.query_hint_review_seller))
            } else {
                searchView?.setSearchHint(getString(R.string.query_hint_review_buyer))
            }
        }
    }

    fun refreshPage() {
        if (swipeToRefresh?.isRefreshing == false) showRefreshing()
        presenter.refreshPage(
            query,
            timeFilter, scoreFilter, tab
        )
    }

    private fun onScroll(layoutManager: LinearLayoutManager): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastItemPosition: Int = layoutManager.findLastVisibleItemPosition()
                val visibleItem: Int = layoutManager.itemCount - 1
                if (!adapter.isLoading && !adapter.isEmpty) presenter.getNextPage(
                    lastItemPosition, visibleItem,
                    searchView?.searchText.toString(), timeFilter, scoreFilter, tab
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KeyboardHandler.hideSoftKeyboard(activity)
        if (savedInstanceState != null) presenter.getFilteredInboxReputation(
            savedInstanceState.getString(ARGS_QUERY, ""),
            savedInstanceState.getString(ARGS_TIME_FILTER, ""),
            savedInstanceState.getString(ARGS_SCORE_FILTER, ""),
            tab
        ) else {
            presenter.getFirstTimeInboxReputation(tab)
        }
    }

    val tab: Int
        get() {
            return if (arguments != null) arguments?.getInt(PARAM_TAB, 1) ?: 1 else -1
        }

    override fun showLoadingFull() {
        adapter.showLoadingFull()
        adapter.notifyDataSetChanged()
    }

    override fun onErrorGetFirstTimeInboxReputation(throwable: Throwable) {
        NetworkErrorHelper.showEmptyState(activity, view, context?.let {
            getErrorMessage(
                it, throwable
            )
        }) {
            presenter.getFirstTimeInboxReputation(tab)
        }
    }

    override fun onSuccessGetFirstTimeInboxReputation(inboxReputationUiModel: InboxReputationUiModel) {
        searchView?.visibility = View.VISIBLE
        filterButton?.visibility = View.VISIBLE
        if (!GlobalConfig.isSellerApp() && tab == ReviewInboxConstants.TAB_BUYER_REVIEW) {
            adapter.setList(inboxReputationUiModel.list, sellerMigrationReviewModel)
        } else {
            adapter.setList(inboxReputationUiModel.list, null)
        }
        presenter.setHasNextPage(inboxReputationUiModel.isHasNextPage)
    }

    override fun finishLoadingFull() {
        adapter.removeLoadingFull()
        adapter.notifyDataSetChanged()
    }

    override fun onErrorGetNextPage(throwable: Throwable) {
        adapter.removeLoading()
        context?.let {
            NetworkErrorHelper.createSnackbarWithAction(activity, getErrorMessage(it, throwable)) {
                presenter.getFirstTimeInboxReputation(tab)
            }.showRetrySnackbar()
        }
    }

    override fun onSuccessGetNextPage(inboxReputationUiModel: InboxReputationUiModel) {
        adapter.removeLoading()
        adapter.addList(inboxReputationUiModel.list)
        presenter.setHasNextPage(inboxReputationUiModel.isHasNextPage)
    }

    override fun onErrorRefresh(throwable: Throwable) {
        context?.let {
            NetworkErrorHelper.showEmptyState(activity, view, getErrorMessage(it, throwable)) {
                presenter.refreshPage(
                    query, timeFilter, scoreFilter, tab
                )
            }
        }
    }

    override fun onSuccessRefresh(inboxReputationUiModel: InboxReputationUiModel) {
        adapter.removeEmpty()
        if (!GlobalConfig.isSellerApp() && tab == ReviewInboxConstants.TAB_BUYER_REVIEW) {
            adapter.setList(inboxReputationUiModel.list, sellerMigrationReviewModel)
        } else {
            adapter.setList(inboxReputationUiModel.list)
        }
        presenter.setHasNextPage(inboxReputationUiModel.isHasNextPage)
    }

    override fun showLoadingNext() {
        adapter.showLoading()
        adapter.notifyDataSetChanged()
    }

    override fun finishLoading() {
        adapter.removeLoading()
        adapter.notifyDataSetChanged()
    }

    override fun onGoToDetail(
        reputationId: String, invoice: String, createTime: String,
        revieweeName: String, revieweeImage: String,
        reputationDataUiModel: ReputationDataUiModel, textDeadline: String,
        adapterPosition: Int, role: Int
    ) {
        savePassModelToDB(
            getInboxReputationDetailPassModel(
                reputationId, invoice, createTime,
                revieweeImage, revieweeName, textDeadline,
                reputationDataUiModel, role
            )
        )
        startActivityForResult(
            InboxReputationDetailActivity.getCallingIntent(
                activity,
                adapterPosition,
                tab
            ),
            REQUEST_OPEN_DETAIL
        )
    }

    private fun savePassModelToDB(inboxReputationDetailPassModel: InboxReputationDetailPassModel) {
        persistentCacheManager.put(
            InboxReputationDetailActivity.CACHE_PASS_DATA,
            CacheUtil.convertModelToString(
                inboxReputationDetailPassModel,
                object : TypeToken<InboxReputationDetailPassModel?>() {}.type
            )
        )
    }

    private fun removeCachePassData() {
        persistentCacheManager.delete(InboxReputationDetailActivity.CACHE_PASS_DATA)
    }

    private fun getInboxReputationDetailPassModel(
        reputationId: String,
        invoice: String,
        createTime: String,
        revieweeImage: String,
        revieweeName: String,
        textDeadline: String,
        reputationDataUiModel: ReputationDataUiModel,
        role: Int
    ): InboxReputationDetailPassModel {
        return InboxReputationDetailPassModel(
            reputationDataUiModel, reputationId, revieweeName, revieweeImage,
            textDeadline, invoice, createTime, role
        )
    }

    override fun showRefreshing() {
        swipeToRefresh?.isRefreshing = true
    }

    override fun onSuccessGetFilteredInboxReputation(inboxReputationUiModel: InboxReputationUiModel) {
        adapter.removeEmpty()
        adapter.setList(inboxReputationUiModel.list)
        presenter.setHasNextPage(inboxReputationUiModel.isHasNextPage)
    }

    override fun onErrorGetFilteredInboxReputation(throwable: Throwable) {
        context?.let {
            NetworkErrorHelper.createSnackbarWithAction(activity, getErrorMessage(it, throwable)) {
                presenter.getFilteredInboxReputation(query, timeFilter, scoreFilter, tab)
            }.showRetrySnackbar()
        }
    }

    override fun finishRefresh() {
        swipeToRefresh?.isRefreshing = false
    }

    override fun onShowEmpty() {
        searchView?.visibility = View.GONE
        filterButton?.visibility = View.GONE
        adapter.clearList()
        if ((GlobalConfig.isSellerApp()
                    || tab == ReviewInboxConstants.TAB_BUYER_REVIEW)
        ) {
            adapter.showEmpty(getString(R.string.inbox_reputation_seller_empty_title))
        } else {
            adapter.showEmpty(
                getString(R.string.inbox_reputation_empty_title),
                getString(R.string.inbox_reputation_empty_button)
            ) { goToHotlist() }
        }
        adapter.notifyDataSetChanged()
    }

    private fun goToHotlist() {
        RouteManager.route(context, ApplinkConst.HOME)
        activity?.finish()
    }

    override fun onShowEmptyFilteredInboxReputation() {
        adapter.clearList()
        adapter.showEmpty(
            getString(R.string.inbox_reputation_search_empty_title),
            getString(R.string.inbox_reputation_search_empty_button)
        ) {
            timeFilter = ""
            scoreFilter = ""
            onSearchSubmitted("")
            searchView?.searchText = ""
        }
        adapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_OPEN_DETAIL) {
            removeCachePassData()
            if (resultCode == Activity.RESULT_OK) refreshPage()
        } else if ((requestCode == REQUEST_FILTER
                    ) && (resultCode == Activity.RESULT_OK
                    ) && (data != null)
        ) {
            timeFilter =
                data.extras?.getString(InboxReputationFilterFragment.SELECTED_TIME_FILTER, "") ?: ""
            scoreFilter =
                data.extras?.getString(InboxReputationFilterFragment.SELECTED_SCORE_FILTER, "") ?: ""
            presenter.getFilteredInboxReputation(
                query,
                timeFilter,
                scoreFilter,
                tab
            )
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    private val query: String
        get() {
            return if (searchView != null) searchView?.searchText ?: "" else ""
        }

    override fun onResume() {
        super.onResume()
        KeyboardHandler.DropKeyboard(activity, searchView)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ARGS_TIME_FILTER, timeFilter)
        outState.putString(ARGS_SCORE_FILTER, scoreFilter)
        outState.putString(ARGS_QUERY, query)
    }

    override fun onSearchSubmitted(text: String) {
        presenter.getFilteredInboxReputation(
            text,
            timeFilter,
            scoreFilter,
            tab
        )
    }

    override fun onSearchTextChanged(text: String) {
        if (text.isEmpty()) {
            setQueryHint()
            presenter.getFilteredInboxReputation(
                "",
                timeFilter,
                scoreFilter,
                tab
            )
        }
    }

    override fun onSellerMigrationReviewClicked() {
        val context: Context? = context
        if (context != null) {
            val appLinks: ArrayList<String> = ArrayList()
            appLinks.add(ApplinkConstInternalSellerapp.SELLER_HOME)
            appLinks.add(ApplinkConst.REPUTATION)
            val intent: Intent = SellerMigrationActivity.createIntent(
                context,
                SellerMigrationFeatureName.FEATURE_REVIEW_TEMPLATE_AND_STATISTICS,
                screenName,
                appLinks
            )
            startActivity(intent)
        }
    }

    companion object {
        private val DEFAULT_DELAY_TEXT_CHANGED: Long = TimeUnit.MILLISECONDS.toMillis(300)
        const val PARAM_TAB: String = "tab"
        private const val REQUEST_OPEN_DETAIL: Int = 101
        private const val REQUEST_FILTER: Int = 102
        private const val ARGS_TIME_FILTER: String = "ARGS_TIME_FILTER"
        private const val ARGS_SCORE_FILTER: String = "ARGS_SCORE_FILTER"
        private const val ARGS_QUERY: String = "ARGS_QUERY"

        @kotlin.jvm.JvmStatic
        fun createInstance(tab: Int): Fragment {
            val fragment = InboxReputationFragment()
            val bundle = Bundle()
            bundle.putInt(PARAM_TAB, tab)
            fragment.arguments = bundle
            return fragment
        }
    }
}