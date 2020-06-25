package com.tokopedia.seller.search.feature.initialsearch.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.analytics.SellerSearchTracking
import com.tokopedia.seller.search.feature.initialsearch.di.component.InitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.view.adapter.InitialSearchAdapter
import com.tokopedia.seller.search.feature.initialsearch.view.adapter.InitialSearchAdapterTypeFactory
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.InitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistorySearchListener
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistoryViewUpdateListener
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.InitialSearchViewModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.initial_search_fragment.*
import kotlinx.android.synthetic.main.initial_search_with_history_section.*
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
        observeLiveData()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(InitialSearchComponent::class.java).inject(this)
    }

    override fun onDestroy() {
        viewModel.getSellerSearch.removeObservers(this)
        viewModel.deleteHistorySearch.removeObservers(this)
        viewModel.flush()
        super.onDestroy()
    }

    private fun initRecyclerView() {
        rvSearchHistorySeller?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = initialSearchAdapter
        }
        tvClearAll?.setOnClickListener {
            onClearAllSearch()
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

    private fun observeLiveData() {
        viewModel.getSellerSearch.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    setHistorySearch(it.data)
                }
            }
        })

        viewModel.deleteHistorySearch.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    removePositionHistory()
                }
            }
        })
    }

    private fun removePositionHistory() {
        if (isDeleteAll) {
            initialSearchAdapter.clearAllElements()
        } else {
            initialSearchAdapter.removeHistory(positionHistory)
        }

        if(initialSearchAdapter.itemCount.isZero()) {
            sectionSearchHistory?.hide()
            initialSearchAdapter.addNoHistoryState()
        }
    }

    private fun setHistorySearch(data: InitialSearchUiModel) {
        initialSearchAdapter.clearAllElements()
        if (data.sellerSearchList.isEmpty()) {
            sectionSearchHistory?.hide()
            initialSearchAdapter.addNoHistoryState()
        } else {
            sectionSearchHistory?.show()
            titleList = data.titleList
            initialSearchAdapter.addAll(data.sellerSearchList)
        }
        historyViewUpdateListener?.showHistoryView()
    }

    fun setHistoryViewUpdateListener(historyViewUpdateListener: HistoryViewUpdateListener) {
        this.historyViewUpdateListener = historyViewUpdateListener
        this.historyViewUpdateListener?.setUserIdFromFragment(userId)
    }

    fun historySearch(keyword: String) {
        this.searchKeyword = keyword
        viewModel.getSellerSearch(keyword = searchKeyword, shopId = shopId)
    }

    fun onMinCharState() {
        sectionSearchHistory?.hide()
        initialSearchAdapter.apply {
            clearAllElements()
            addMinCharState()
        }
        historyViewUpdateListener?.showHistoryView()
    }
}
