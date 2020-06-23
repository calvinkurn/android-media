package com.tokopedia.seller.search.feature.initialsearch.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ALL
import com.tokopedia.seller.search.common.util.FilterItemDecoration
import com.tokopedia.seller.search.common.util.OnScrollListenerAutocomplete
import com.tokopedia.seller.search.feature.initialsearch.di.component.InitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.view.activity.InitialSellerSearchActivity
import com.tokopedia.seller.search.feature.initialsearch.view.adapter.FilterSearchAdapter
import com.tokopedia.seller.search.feature.initialsearch.view.adapter.SuggestionSearchAdapter
import com.tokopedia.seller.search.feature.initialsearch.view.adapter.SuggestionSearchAdapterTypeFactory
import com.tokopedia.seller.search.feature.initialsearch.view.model.filter.FilterSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.ItemSellerSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.SellerSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.*
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.SuggestionSearchViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.suggestion_search_fragment.*
import javax.inject.Inject

class SuggestionSearchFragment: BaseDaggerFragment(),
        FilterSearchListener, ProductSearchListener, OrderSearchListener, NavigationSearchListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val filterAdapter by lazy { FilterSearchAdapter(this) }

    private val searchSellerAdapterTypeFactory by lazy {
        SuggestionSearchAdapterTypeFactory(this, this, this)
    }

    private val suggestionSearchAdapter by lazy { SuggestionSearchAdapter(searchSellerAdapterTypeFactory) }

    private var suggestionViewUpdateListener: SuggestionViewUpdateListener? = null

    private val viewModel: SuggestionSearchViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SuggestionSearchViewModel::class.java)
    }

    private var searchKeyword = ""
    private var shopId = ""
    private var positionFilter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        shopId = userSession.shopId.orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.suggestion_search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        observeLiveData()
    }

    override fun onDestroy() {
        viewModel.getSellerSearch.removeObservers(this)
        viewModel.insertSuccessSearch.removeObservers(this)
        viewModel.flush()
        super.onDestroy()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(InitialSearchComponent::class.java).inject(this)
    }

    private fun observeLiveData() {
        viewModel.getSellerSearch.observe(this, Observer {
            when(it) {
                is Success -> {
                    setSuggestionSearch(it.data.first, it.data.second)
                }
                is Fail -> { }
            }
        })

        viewModel.insertSuccessSearch.observe(this, Observer {
            when(it) {
                is Success -> {
                    dropKeyBoard()
                }
                is Fail -> {
                    dropKeyBoard()
                }
            }
        })
    }

    private fun setSuggestionSearch(data: List<SellerSearchUiModel>, filterList: List<FilterSearchUiModel>) {
        suggestionSearchAdapter.clearAllElements()
        if(data.isEmpty()) {
            rvSearchFilter?.hide()
            suggestionSearchAdapter.addNoResultState()
        } else {
            rvSearchFilter?.show()
            filterAdapter.setFilterSearch(filterList)
            filterAdapter.updatedSortFilter(positionFilter)
            suggestionSearchAdapter.addAll(data)
        }
        suggestionViewUpdateListener?.showSuggestionView()
    }

    private fun initRecyclerView(view: View) {
        rvSearchSuggestionSeller?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = suggestionSearchAdapter
            addOnScrollListener(OnScrollListenerAutocomplete(view.context, view))
        }
        rvSearchFilter?.apply {
            if(itemDecorationCount == 0) {
                addItemDecoration(FilterItemDecoration())
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = filterAdapter
        }
    }

    fun setSuggestionViewUpdateListener(suggestionViewUpdateListener: SuggestionViewUpdateListener) {
        this.suggestionViewUpdateListener = suggestionViewUpdateListener
    }

    fun suggestionSearch(keyword: String) {
        this.searchKeyword = keyword
        suggestionSearchAdapter.apply {
            clearAllElements()
            addLoading()
        }
        viewModel.getSellerSearch(keyword = searchKeyword, shopId = shopId)
    }

    private fun dropKeyBoard() {
        if (activity != null && activity is InitialSellerSearchActivity) {
            (activity as InitialSellerSearchActivity).dropKeyboardSuggestion()
        }
    }

    private fun startActivityFromAutoComplete(appLink: String) {
        if (activity == null) return

        RouteManager.route(activity, appLink)
        activity?.finish()
    }

    override fun onFilterItemClicked(title: String, chipType: String, position: Int) {
        val section = if(title == ALL) "" else title
        positionFilter = position
        onFilterSearchSuggestion(section)
    }

    override fun onNavigationItemClicked(data: ItemSellerSearchUiModel, position: Int) {
        viewModel.insertSearchSeller(searchKeyword, data.id.orEmpty(), data.title.orEmpty(), position)
        startActivityFromAutoComplete(data.appUrl.orEmpty())
        dropKeyBoard()
    }

    override fun onOrderItemClicked(data: ItemSellerSearchUiModel, position: Int) {
        viewModel.insertSearchSeller(searchKeyword, data.id.orEmpty(), data.title.orEmpty(), position)
        startActivityFromAutoComplete(data.appUrl.orEmpty())
        dropKeyBoard()
    }

    override fun onProductItemClicked(data: ItemSellerSearchUiModel, position: Int) {
        viewModel.insertSearchSeller(searchKeyword, data.id.orEmpty(), data.title.orEmpty(), position)
        startActivityFromAutoComplete(data.appUrl.orEmpty())
        dropKeyBoard()
    }

    private fun onFilterSearchSuggestion(section: String) {
        suggestionSearchAdapter.apply {
            rvSearchFilter?.hide()
            clearAllElements()
            addLoading()
        }
        viewModel.getSellerSearch(keyword = searchKeyword, section = section, shopId = shopId)
    }

}
