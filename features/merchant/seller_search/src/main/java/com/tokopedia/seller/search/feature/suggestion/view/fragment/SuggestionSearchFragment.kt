package com.tokopedia.seller.search.feature.suggestion.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.util.OnScrollListenerAutocomplete
import com.tokopedia.seller.search.common.util.addWWWPrefix
import com.tokopedia.seller.search.feature.analytics.SellerSearchTracking
import com.tokopedia.seller.search.feature.initialsearch.di.component.InitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.view.activity.InitialSellerSearchActivity
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.*
import com.tokopedia.seller.search.feature.suggestion.view.adapter.SuggestionSearchAdapter
import com.tokopedia.seller.search.feature.suggestion.view.adapter.SuggestionSearchAdapterTypeFactory
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ItemSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.SellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.viewmodel.SuggestionSearchViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.suggestion_search_fragment.*
import javax.inject.Inject

class SuggestionSearchFragment : BaseDaggerFragment(),
        ProductSearchListener, OrderSearchListener, NavigationSearchListener, FaqSearchListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val searchSellerAdapterTypeFactory by lazy {
        SuggestionSearchAdapterTypeFactory(this, this, this, this)
    }

    private val suggestionSearchAdapter by lazy { SuggestionSearchAdapter(searchSellerAdapterTypeFactory) }

    private var suggestionViewUpdateListener: SuggestionViewUpdateListener? = null

    private val viewModel: SuggestionSearchViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SuggestionSearchViewModel::class.java)
    }

    private var searchKeyword = ""
    private var shopId = ""
    private var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        shopId = userSession.shopId.orEmpty()
        userId = userSession.userId.orEmpty()
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
        viewModel.getSellerSearch.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    setSuggestionSearch(it.data)
                }
            }
        })

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

    private fun setSuggestionSearch(data: List<SellerSearchUiModel>) {
        suggestionSearchAdapter.clearAllElements()
        if (data.isEmpty()) {
            suggestionSearchAdapter.addNoResultState()
            SellerSearchTracking.impressionEmptyResultEvent(userId)
        } else {
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

    override fun onNavigationItemClicked(data: ItemSellerSearchUiModel, position: Int) {
        viewModel.insertSearchSeller(data.title.orEmpty(), data.id.orEmpty(), data.title.orEmpty(), position)
        SellerSearchTracking.clickOnSearchResult(userId, data.section.orEmpty())
        startActivityFromAutoComplete(data.appUrl.orEmpty())
        dropKeyBoard()
    }

    override fun onOrderItemClicked(data: ItemSellerSearchUiModel, position: Int) {
        viewModel.insertSearchSeller(data.title.orEmpty(), data.id.orEmpty(), data.title.orEmpty(), position)
        SellerSearchTracking.clickOnSearchResult(userId, data.section.orEmpty())
        startActivityFromAutoComplete(data.appUrl.orEmpty())
        dropKeyBoard()
    }

    override fun onOrderMoreClicked(element: SellerSearchUiModel, position: Int) {
        SellerSearchTracking.clickOtherResult(userId, element.title.orEmpty())
        startActivityFromAutoComplete(element.appActionLink.orEmpty())
        dropKeyBoard()
    }

    override fun onProductItemClicked(data: ItemSellerSearchUiModel, position: Int) {
        viewModel.insertSearchSeller(data.title.orEmpty(), data.id.orEmpty(), data.title.orEmpty(), position)
        SellerSearchTracking.clickOnSearchResult(userId, data.section.orEmpty())
        startActivityFromAutoComplete(data.appUrl.orEmpty())
        dropKeyBoard()
    }

    override fun onProductMoreClicked(element: SellerSearchUiModel, position: Int) {
        SellerSearchTracking.clickOtherResult(userId, element.title.orEmpty())
        startActivityFromAutoComplete(element.appActionLink.orEmpty())
        dropKeyBoard()
    }

    override fun onFaqItemClicked(data: ItemSellerSearchUiModel, position: Int) {
        SellerSearchTracking.clickOnSearchResult(userId, data.section.orEmpty())
        viewModel.insertSearchSeller(data.title.orEmpty(), data.id.orEmpty(), data.title.orEmpty(), position)
        val appUrl = data.appUrl?.addWWWPrefix.orEmpty()
        RouteManager.route(activity, appUrl)
        dropKeyBoard()
    }

    override fun onFaqMoreClicked(element: SellerSearchUiModel, position: Int) {
        SellerSearchTracking.clickOtherResult(userId, element.title.orEmpty())
        val appUrl = element.appActionLink?.addWWWPrefix.orEmpty()
        RouteManager.route(activity, appUrl)
        dropKeyBoard()
    }
}
