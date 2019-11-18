package com.tokopedia.digital.home.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.common_digital.common.util.AnalyticUtils
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.di.DigitalHomePageComponent
import com.tokopedia.digital.home.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.presentation.Util.DigitalHomeTrackingUtil
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageSearchTypeFactory
import com.tokopedia.digital.home.presentation.adapter.viewholder.DigitalHomePageSearchViewHolder
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchergame.list.view.viewmodel.DigitalHomePageSearchViewModel
import kotlinx.android.synthetic.main.layout_digital_home_search.*
import javax.inject.Inject

class DigitalHomePageSearchFragment: BaseSearchListFragment<DigitalHomePageSearchCategoryModel, DigitalHomePageSearchTypeFactory>(),
        DigitalHomePageSearchViewHolder.OnSearchCategoryClickListener,
        SearchInputView.ResetListener{

    @Inject
    lateinit var trackingUtil: DigitalHomeTrackingUtil
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var viewModel: DigitalHomePageSearchViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_digital_home_search, container, false)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = viewModelProvider.get(DigitalHomePageSearchViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        digital_homepage_search_view_search_bar.setResetListener(this)
        digital_homepage_search_view_toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        // Show keyboard automatically
        digital_homepage_search_view_search_bar.searchTextView.requestFocus()
        context?.run {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(digital_homepage_search_view_search_bar.searchTextView, InputMethod.SHOW_FORCED)
        }

        val recyclerView = getRecyclerView(view) as VerticalRecyclerView
        recyclerView.clearItemDecoration()
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    override fun getSearchInputViewResourceId(): Int {
        return R.id.digital_homepage_search_view_search_bar
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(DigitalHomePageComponent::class.java).inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.searchCategoryList.observe(this, Observer {
            when(it) {
                is Success -> {
                    clearAllData()
                    renderList(it.data)
                    trackSearchResultCategories(it.data)
                }
                is Fail -> {
                    showGetListError(it.throwable)
                }
            }
        })
    }

    override fun loadData(page: Int) {
        clearAllData()
    }

    override fun getAdapterTypeFactory(): DigitalHomePageSearchTypeFactory {
        return DigitalHomePageSearchTypeFactory(this)
    }

    override fun onItemClicked(t: DigitalHomePageSearchCategoryModel?) {

    }

    override fun onSearchSubmitted(text: String?) {
        text?.run {
            if (this.isNotEmpty()) trackingUtil.eventClickSearch(this)
        }
    }

    override fun onSearchTextChanged(text: String?) {
        text?.let { query -> if (query.isNotEmpty()) searchCategory(query) }
    }

    override fun onSearchReset() {
        clearAllData()
    }

    private fun searchCategory(searchQuery: String) {
        viewModel.searchCategoryList(GraphqlHelper.loadRawString(resources, R.raw.query_digital_home_category), searchQuery)
    }

    override fun onSearchCategoryClicked(category: DigitalHomePageSearchCategoryModel, position: Int) {
        trackingUtil.eventSearchResultPageClick(category, position)
        RouteManager.route(context, category.applink)
    }

    private fun trackSearchResultCategories(list: List<DigitalHomePageSearchCategoryModel>) {
        trackingUtil.eventSearchResultPageImpression(list)
    }

    companion object {
        fun getInstance() = DigitalHomePageSearchFragment()
    }
}
