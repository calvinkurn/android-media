package com.tokopedia.seller.search.feature.initialsearch.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.plt.GlobalSearchSellerPerformanceMonitoringListener
import com.tokopedia.seller.search.feature.analytics.SellerSearchTracking
import com.tokopedia.seller.search.feature.initialsearch.di.component.InitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.view.activity.InitialSellerSearchActivity
import com.tokopedia.seller.search.feature.initialsearch.view.adapter.InitialSearchAdapter
import com.tokopedia.seller.search.feature.initialsearch.view.adapter.InitialSearchAdapterTypeFactory
import com.tokopedia.seller.search.feature.initialsearch.view.model.BaseInitialSearchSeller
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.*
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistorySearchListener
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistoryViewUpdateListener
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.InitialSearchViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.initial_search_fragment.*
import javax.inject.Inject


class InitialSearchFragment : BaseDaggerFragment(), HistorySearchListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val initialSearchAdapterTypeFactory by lazy {
        InitialSearchAdapterTypeFactory(this)
    }

    private val initialSearchAdapter by lazy { InitialSearchAdapter(initialSearchAdapterTypeFactory) }

    private val viewModel: InitialSearchViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(InitialSearchViewModel::class.java)
    }

    private var titleList: List<String>? = null

    private var searchKeyword = ""
    private var shopId = ""
    private var userId = ""
    private var positionHistory = 0

    private var historyViewUpdateListener: HistoryViewUpdateListener? = null
    private var isDeleteAll = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        shopId = userSession.shopId.orEmpty()
        userId = userSession.userId.orEmpty()
    }

    override fun onStart() {
        super.onStart()
        SellerSearchTracking.sendScreenSearchEvent(userSession.userId.orEmpty())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.initial_search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeGetSellerSearch()
        observeDeleteHistorySearch()
        observeInsertSearch()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(InitialSearchComponent::class.java).inject(this)
    }

    override fun onDestroy() {
        viewModel.getSellerSearch.removeObservers(this)
        viewModel.deleteHistorySearch.removeObservers(this)
        viewModel.insertSuccessSearch.removeObservers(this)
        viewModel.flush()
        super.onDestroy()
    }

    private fun initRecyclerView() {
        rvSearchHistorySeller?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = initialSearchAdapter
        }
    }

    override fun onClearSearchItem(keyword: String, adapterPosition: Int) {
        positionHistory = adapterPosition
        viewModel.deleteSuggestionSearch(listOf(keyword))
        isDeleteAll = false
        SellerSearchTracking.clickDeleteSelectedSearch(userId)
    }

    override fun onClearAllSearch() {
        viewModel.deleteSuggestionSearch(titleList ?: listOf())
        isDeleteAll = true
        SellerSearchTracking.clickDeleteAllSearchEvent(userId)
    }

    override fun onHistoryItemClicked(keyword: String) {
        historyViewUpdateListener?.setKeywordSearchBarView(keyword)
        SellerSearchTracking.clickRecommendWordingEvent(userId)
    }

    override fun onHighlightItemClicked(data: ItemHighlightInitialSearchUiModel, position: Int) {
        viewModel.insertSearchSeller(data.title.orEmpty(), data.id.orEmpty(), data.title.orEmpty(), position)
        startActivityFromAutoComplete(data.appUrl.orEmpty())
        SellerSearchTracking.clickOnItemSearchHighlights(userId)
    }

    private fun observeInsertSearch() {
        viewModel.insertSuccessSearch.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    dropKeyBoard()
                }
                is Fail -> {
                    dropKeyBoard()
                }
            }
        })
    }

    private fun observeDeleteHistorySearch() {
        viewModel.deleteHistorySearch.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    removePositionHistory()
                }
            }
        })
    }

    private fun observeGetSellerSearch() {
        viewModel.getSellerSearch.observe(viewLifecycleOwner, Observer {
            (activity as? GlobalSearchSellerPerformanceMonitoringListener)?.startRenderPerformanceMonitoring()
            when (it) {
                is Success -> {
                    setHistorySearch(it.data.first, it.data.second)
                    stopSearchResultPagePerformanceMonitoring()
                }
            }
        })
    }

    private fun dropKeyBoard() {
        (activity as? InitialSellerSearchActivity)?.dropKeyboardSuggestion()
    }

    private fun startActivityFromAutoComplete(appLink: String) {
        if (activity == null) return
        RouteManager.route(activity, appLink)
        activity?.finish()
    }

    private fun removePositionHistory() {
        if (isDeleteAll) {
            initialSearchAdapter.clearAllElements()
        } else {
            initialSearchAdapter.removeHistory(positionHistory)
        }

        val historySearch = initialSearchAdapter.list.filterIsInstance<ItemInitialSearchUiModel>()
        if (historySearch.isEmpty()) {
            initialSearchAdapter.addNoHistoryState()
            viewModel.getSellerSearch(keyword = searchKeyword, shopId = shopId)
        }
    }

    private fun setHistorySearch(data: List<BaseInitialSearchSeller>, titleList: List<String>) {
        initialSearchAdapter.clearAllElements()
        val itemInitialSearchUiModelList = data.filterIsInstance<ItemInitialSearchUiModel>()
        if (itemInitialSearchUiModelList.isEmpty()) {
            val itemTitleHighlightSearchUiModel = data.filterIsInstance<ItemTitleHighlightInitialSearchUiModel>().firstOrNull() ?: ItemTitleHighlightInitialSearchUiModel()
            val itemHighlightSearchUiModel = data.filterIsInstance<HighlightInitialSearchUiModel>().firstOrNull() ?: HighlightInitialSearchUiModel()
            val highlightSearchVisitable = mutableListOf(itemTitleHighlightSearchUiModel, itemHighlightSearchUiModel)
            initialSearchAdapter.addNoHistoryState()
            initialSearchAdapter.addAll(highlightSearchVisitable)
        } else {
            this.titleList = titleList
            initialSearchAdapter.addAll(data)
        }
        historyViewUpdateListener?.showHistoryView()
    }

    private fun stopSearchResultPagePerformanceMonitoring() {
        rvSearchHistorySeller?.viewTreeObserver
                ?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        (activity as? GlobalSearchSellerPerformanceMonitoringListener)?.finishMonitoring()
                        rvSearchHistorySeller.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
    }

    fun setHistoryViewUpdateListener(historyViewUpdateListener: HistoryViewUpdateListener) {
        this.historyViewUpdateListener = historyViewUpdateListener
        this.historyViewUpdateListener?.setUserIdFromFragment(userId)
    }

    fun historySearch(keyword: String) {
        this.searchKeyword = keyword
        (activity as? GlobalSearchSellerPerformanceMonitoringListener)?.startNetworkPerformanceMonitoring()
        viewModel.getSellerSearch(keyword = searchKeyword, shopId = shopId)
    }

    fun onMinCharState() {
        initialSearchAdapter.apply {
            clearAllElements()
            addMinCharState()
        }
        historyViewUpdateListener?.showHistoryView()
    }


}
