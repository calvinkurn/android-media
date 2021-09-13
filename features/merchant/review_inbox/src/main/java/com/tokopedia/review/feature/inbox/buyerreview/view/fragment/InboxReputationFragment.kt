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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
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
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inbox.InboxReputationTypeFactory
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inbox.InboxReputationTypeFactoryImpl
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.SellerMigrationReviewViewHolder.SellerMigrationReviewClickListener
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation
import com.tokopedia.review.feature.inbox.buyerreview.view.presenter.InboxReputationPresenter
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.ReputationDataUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.SellerMigrationReviewModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailPassModel
import com.tokopedia.review.inbox.R
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity.Companion.createIntent
import com.tokopedia.user.session.UserSession
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author by nisie on 8/11/17.
 */
class InboxReputationFragment constructor() : BaseDaggerFragment(), InboxReputation.View,
    SearchInputView.Listener, SellerMigrationReviewClickListener {
    private var searchView: SearchInputView? = null
    private var mainList: RecyclerView? = null
    private var swipeToRefresh: SwipeToRefresh? = null
    private var layoutManager: LinearLayoutManager? = null
    private var adapter: InboxReputationAdapter? = null
    private var timeFilter: String? = null
    private var scoreFilter: String? = null
    private var filterButton: View? = null
    private val sellerMigrationReviewModel: SellerMigrationReviewModel =
        SellerMigrationReviewModel()

    @kotlin.jvm.JvmField
    @Inject
    var presenter: InboxReputationPresenter? = null

    @kotlin.jvm.JvmField
    @Inject
    var persistentCacheManager: PersistentCacheManager? = null

    @kotlin.jvm.JvmField
    @Inject
    var reputationTracking: ReputationTracking? = null

    @kotlin.jvm.JvmField
    @Inject
    var userSession: UserSession? = null
    override fun getScreenName(): String {
        return AppScreen.SCREEN_INBOX_REPUTATION
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
        setHasOptionsMenu(true)
        initVar(savedInstanceState)
    }

    private fun openFilter() {
        val intent: Intent = InboxReputationFilterActivity.Companion.createIntent(
            getActivity(),
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
        val typeFactory: InboxReputationTypeFactory =
            InboxReputationTypeFactoryImpl(getContext(), this, this)
        adapter = InboxReputationAdapter(typeFactory)
    }

    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setRetainInstance(true)
        val parentView: View =
            inflater.inflate(R.layout.fragment_inbox_reputation, container, false)
        mainList = parentView.findViewById<View>(R.id.review_list) as RecyclerView?
        swipeToRefresh =
            parentView.findViewById<View>(R.id.swipe_refresh_inbox_reputation) as SwipeToRefresh?
        searchView = parentView.findViewById<View>(R.id.search) as SearchInputView?
        searchView!!.setDelayTextChanged(DEFAULT_DELAY_TEXT_CHANGED)
        searchView!!.setListener(this)
        filterButton = parentView.findViewById(R.id.filter_button)
        prepareView()
        presenter!!.attachView(this)
        return parentView
    }

    private fun prepareView() {
        layoutManager = LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
        mainList!!.setLayoutManager(layoutManager)
        mainList!!.setAdapter(adapter)
        mainList!!.addOnScrollListener(onScroll())
        swipeToRefresh!!.setOnRefreshListener(object : OnRefreshListener {
            public override fun onRefresh() {
                refreshPage()
            }
        })
        setQueryHint()
        filterButton!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                openFilter()
            }
        })
    }

    private fun setQueryHint() {
        if (getContext() != null) {
            if (tab == ReviewInboxConstants.TAB_BUYER_REVIEW) {
                searchView!!.setSearchHint(getString(R.string.query_hint_review_seller))
            } else {
                searchView!!.setSearchHint(getString(R.string.query_hint_review_buyer))
            }
        }
    }

    fun refreshPage() {
        if (!swipeToRefresh!!.isRefreshing()) showRefreshing()
        presenter!!.refreshPage(
            query,
            timeFilter, scoreFilter, tab
        )
    }

    private fun onScroll(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            public override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastItemPosition: Int = layoutManager!!.findLastVisibleItemPosition()
                val visibleItem: Int = layoutManager!!.getItemCount() - 1
                if (!adapter!!.isLoading() && !adapter.isEmpty()) presenter!!.getNextPage(
                    lastItemPosition, visibleItem,
                    searchView!!.getSearchText().toString(), timeFilter, scoreFilter, tab
                )
            }
        }
    }

    public override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KeyboardHandler.hideSoftKeyboard(getActivity())
        if (savedInstanceState != null) presenter!!.getFilteredInboxReputation(
            savedInstanceState.getString(ARGS_QUERY, ""),
            savedInstanceState.getString(ARGS_TIME_FILTER, ""),
            savedInstanceState.getString(ARGS_SCORE_FILTER, ""),
            tab
        ) else {
            presenter!!.getFirstTimeInboxReputation(tab)
        }
    }

    val tab: Int
        get() {
            if (getArguments() != null) return getArguments()!!.getInt(PARAM_TAB, 1) else return -1
        }

    public override fun showLoadingFull() {
        adapter!!.showLoadingFull()
        adapter!!.notifyDataSetChanged()
    }

    public override fun onErrorGetFirstTimeInboxReputation(throwable: Throwable?) {
        if ((getActivity() != null) and (getView() != null) and (getContext() != null)) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), getErrorMessage(
                (getContext())!!, (throwable)!!
            ),
                NetworkErrorHelper.RetryClickedListener({
                    presenter!!.getFirstTimeInboxReputation(
                        tab
                    )
                })
            )
        }
    }

    public override fun onSuccessGetFirstTimeInboxReputation(inboxReputationUiModel: InboxReputationUiModel) {
        searchView!!.setVisibility(View.VISIBLE)
        filterButton!!.setVisibility(View.VISIBLE)
        if (!GlobalConfig.isSellerApp() && tab == ReviewInboxConstants.TAB_BUYER_REVIEW) {
            adapter!!.setList(inboxReputationUiModel.getList(), sellerMigrationReviewModel)
        } else {
            adapter!!.setList(inboxReputationUiModel.getList(), null)
        }
        presenter!!.setHasNextPage(inboxReputationUiModel.isHasNextPage())
    }

    public override fun finishLoadingFull() {
        adapter!!.removeLoadingFull()
        adapter!!.notifyDataSetChanged()
    }

    public override fun onErrorGetNextPage(throwable: Throwable?) {
        adapter!!.removeLoading()
        if (getContext() != null) {
            NetworkErrorHelper.createSnackbarWithAction(getActivity(),
                getErrorMessage((getContext())!!, (throwable)!!),
                NetworkErrorHelper.RetryClickedListener({
                    presenter!!.getFirstTimeInboxReputation(
                        tab
                    )
                })
            ).showRetrySnackbar()
        }
    }

    public override fun onSuccessGetNextPage(inboxReputationUiModel: InboxReputationUiModel?) {
        adapter!!.removeLoading()
        adapter!!.addList(inboxReputationUiModel.getList())
        presenter!!.setHasNextPage(inboxReputationUiModel!!.isHasNextPage())
    }

    public override fun onErrorRefresh(throwable: Throwable?) {
        if (getContext() != null) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), getErrorMessage(
                (getContext())!!, (throwable)!!
            ),
                NetworkErrorHelper.RetryClickedListener({
                    presenter!!.refreshPage(
                        query, timeFilter, scoreFilter, tab
                    )
                })
            )
        }
    }

    public override fun onSuccessRefresh(inboxReputationUiModel: InboxReputationUiModel?) {
        adapter!!.removeEmpty()
        if (!GlobalConfig.isSellerApp() && tab == ReviewInboxConstants.TAB_BUYER_REVIEW) {
            adapter!!.setList(inboxReputationUiModel.getList(), sellerMigrationReviewModel)
        } else {
            adapter!!.setList(inboxReputationUiModel.getList())
        }
        presenter!!.setHasNextPage(inboxReputationUiModel!!.isHasNextPage())
    }

    public override fun showLoadingNext() {
        adapter!!.showLoading()
        adapter!!.notifyDataSetChanged()
    }

    public override fun finishLoading() {
        adapter!!.removeLoading()
        adapter!!.notifyDataSetChanged()
    }

    public override fun onGoToDetail(
        reputationId: String?, invoice: String?, createTime: String?,
        revieweeName: String?, revieweeImage: String?,
        reputationDataUiModel: ReputationDataUiModel?, textDeadline: String,
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
            InboxReputationDetailActivity.Companion.getCallingIntent(
                getActivity(),
                adapterPosition,
                tab
            ),
            REQUEST_OPEN_DETAIL
        )
    }

    private fun savePassModelToDB(inboxReputationDetailPassModel: InboxReputationDetailPassModel) {
        if (persistentCacheManager != null) {
            persistentCacheManager!!.put(
                InboxReputationDetailActivity.Companion.CACHE_PASS_DATA,
                CacheUtil.convertModelToString(inboxReputationDetailPassModel,
                    object : TypeToken<InboxReputationDetailPassModel?>() {}.getType()
                )
            )
        }
    }

    private fun removeCachePassData() {
        if (persistentCacheManager != null) {
            persistentCacheManager!!.delete(InboxReputationDetailActivity.Companion.CACHE_PASS_DATA)
        }
    }

    private fun getInboxReputationDetailPassModel(
        reputationId: String?,
        invoice: String?,
        createTime: String?,
        revieweeImage: String?,
        revieweeName: String?,
        textDeadline: String,
        reputationDataUiModel: ReputationDataUiModel?,
        role: Int
    ): InboxReputationDetailPassModel {
        return InboxReputationDetailPassModel(
            reputationId, revieweeName, revieweeImage,
            textDeadline, invoice, createTime, reputationDataUiModel, role
        )
    }

    public override fun showRefreshing() {
        swipeToRefresh!!.setRefreshing(true)
    }

    public override fun onSuccessGetFilteredInboxReputation(inboxReputationUiModel: InboxReputationUiModel?) {
        adapter!!.removeEmpty()
        adapter!!.setList(inboxReputationUiModel.getList())
        presenter!!.setHasNextPage(inboxReputationUiModel!!.isHasNextPage())
    }

    public override fun onErrorGetFilteredInboxReputation(throwable: Throwable?) {
        if (getContext() != null) {
            NetworkErrorHelper.createSnackbarWithAction(getActivity(), getErrorMessage(
                (getContext())!!, (throwable)!!
            ),
                NetworkErrorHelper.RetryClickedListener({
                    presenter!!.getFilteredInboxReputation(
                        query, timeFilter, scoreFilter, tab
                    )
                })
            ).showRetrySnackbar()
        }
    }

    public override fun finishRefresh() {
        swipeToRefresh!!.setRefreshing(false)
    }

    public override fun onShowEmpty() {
        searchView!!.setVisibility(View.GONE)
        filterButton!!.setVisibility(View.GONE)
        adapter!!.clearList()
        if ((GlobalConfig.isSellerApp()
                    || tab == ReviewInboxConstants.TAB_BUYER_REVIEW)
        ) {
            adapter!!.showEmpty(getString(R.string.inbox_reputation_seller_empty_title))
        } else {
            adapter!!.showEmpty(getString(R.string.inbox_reputation_empty_title),
                getString(R.string.inbox_reputation_empty_button),
                object : View.OnClickListener {
                    public override fun onClick(v: View) {
                        goToHotlist()
                    }
                })
        }
        adapter!!.notifyDataSetChanged()
    }

    private fun goToHotlist() {
        RouteManager.route(getContext(), ApplinkConst.HOME)
        getActivity()!!.finish()
    }

    public override fun onShowEmptyFilteredInboxReputation() {
        adapter!!.clearList()
        adapter!!.showEmpty(getString(R.string.inbox_reputation_search_empty_title),
            getString(R.string.inbox_reputation_search_empty_button),
            object : View.OnClickListener {
                public override fun onClick(v: View) {
                    timeFilter = ""
                    scoreFilter = ""
                    onSearchSubmitted("")
                    searchView!!.setSearchText("")
                }
            })
        adapter!!.notifyDataSetChanged()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_OPEN_DETAIL) {
            removeCachePassData()
            if (resultCode == Activity.RESULT_OK) refreshPage()
        } else if ((requestCode == REQUEST_FILTER
                    ) && (resultCode == Activity.RESULT_OK
                    ) && (data != null)
        ) {
            timeFilter = data.getExtras()!!.getString(
                InboxReputationFilterFragment.Companion.SELECTED_TIME_FILTER, ""
            )
            scoreFilter = data.getExtras()!!
                .getString(InboxReputationFilterFragment.Companion.SELECTED_SCORE_FILTER, "")
            presenter!!.getFilteredInboxReputation(
                query,
                timeFilter,
                scoreFilter,
                tab
            )
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    private val query: String
        private get() {
            if (searchView != null) return searchView!!.getSearchText() else return ""
        }

    public override fun onResume() {
        super.onResume()
        KeyboardHandler.DropKeyboard(getActivity(), searchView)
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (presenter != null) presenter!!.detachView()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ARGS_TIME_FILTER, timeFilter)
        outState.putString(ARGS_SCORE_FILTER, scoreFilter)
        outState.putString(ARGS_QUERY, query)
    }

    public override fun onSearchSubmitted(text: String) {
        presenter!!.getFilteredInboxReputation(
            text,
            timeFilter,
            scoreFilter,
            tab
        )
    }

    public override fun onSearchTextChanged(text: String) {
        if (text.length == 0) {
            setQueryHint()
            presenter!!.getFilteredInboxReputation(
                "",
                timeFilter,
                scoreFilter,
                tab
            )
        }
    }

    public override fun onSellerMigrationReviewClicked() {
        val context: Context? = getContext()
        if (context != null) {
            val appLinks: ArrayList<String> = ArrayList()
            appLinks.add(ApplinkConstInternalSellerapp.SELLER_HOME)
            appLinks.add(ApplinkConst.REPUTATION)
            val intent: Intent = createIntent(
                context,
                SellerMigrationFeatureName.FEATURE_REVIEW_TEMPLATE_AND_STATISTICS,
                getScreenName(),
                appLinks
            )
            startActivity(intent)
        }
    }

    companion object {
        protected val DEFAULT_DELAY_TEXT_CHANGED: Long = TimeUnit.MILLISECONDS.toMillis(300)
        val PARAM_TAB: String = "tab"
        private val REQUEST_OPEN_DETAIL: Int = 101
        private val REQUEST_FILTER: Int = 102
        private val ARGS_TIME_FILTER: String = "ARGS_TIME_FILTER"
        private val ARGS_SCORE_FILTER: String = "ARGS_SCORE_FILTER"
        private val ARGS_QUERY: String = "ARGS_QUERY"
        @kotlin.jvm.JvmStatic
        fun createInstance(tab: Int): Fragment {
            val fragment: InboxReputationFragment = InboxReputationFragment()
            val bundle: Bundle = Bundle()
            bundle.putInt(PARAM_TAB, tab)
            fragment.setArguments(bundle)
            return fragment
        }
    }
}