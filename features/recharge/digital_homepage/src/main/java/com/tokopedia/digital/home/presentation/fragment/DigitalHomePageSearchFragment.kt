package com.tokopedia.digital.home.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.analytics.RechargeHomepageAnalytics
import com.tokopedia.digital.home.di.RechargeHomepageComponent
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageSearchTypeFactory
import com.tokopedia.digital.home.presentation.adapter.viewholder.DigitalHomePageSearchViewHolder
import com.tokopedia.digital.home.presentation.viewmodel.DigitalHomePageSearchViewModel
import com.tokopedia.digital.home.util.DigitalHomepageGqlQuery
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.view_recharge_home_search.*
import javax.inject.Inject

open class DigitalHomePageSearchFragment: BaseListFragment<DigitalHomePageSearchCategoryModel, DigitalHomePageSearchTypeFactory>(),
        DigitalHomePageSearchViewHolder.OnSearchCategoryClickListener {

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var rechargeHomepageAnalytics: RechargeHomepageAnalytics
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var viewModel: DigitalHomePageSearchViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.view_recharge_home_search, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
            viewModel = viewModelProvider.get(DigitalHomePageSearchViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        digital_homepage_search_view_search_bar.clearListener = {
            clearAllData()
        }
        digital_homepage_search_view_toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        // Show keyboard automatically
        digital_homepage_search_view_search_bar.searchBarTextField.requestFocus()
        digital_homepage_search_view_search_bar.searchBarTextField.setOnEditorActionListener(getSearchListener)
        digital_homepage_search_view_search_bar.searchBarTextField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
                onSearchBarTextChanged(text.toString())
            }
        })
        context?.run {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(digital_homepage_search_view_search_bar.searchBarTextField, InputMethod.SHOW_FORCED)
        }

        val recyclerView = getRecyclerView(view) as? VerticalRecyclerView
        recyclerView?.clearItemDecoration()
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    open fun onSearchBarTextChanged(text: String) { searchCategory(text) }

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(RechargeHomepageComponent::class.java).inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeSearchCategoryList()
    }

    private fun observeSearchCategoryList() {
        viewModel.searchCategoryList.observe(viewLifecycleOwner, Observer {
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

    private val getSearchListener = object : TextView.OnEditorActionListener {
        override fun onEditorAction(textView: TextView, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                KeyboardHandler.hideSoftKeyboard(activity)
                onSearchSubmitted(textView.text.toString())
                return true
            }
            return false
        }
    }

    fun onSearchSubmitted(text: String?) {
        text?.run {
            if (this.isNotEmpty()) rechargeHomepageAnalytics.eventClickSearch(this)
        }
    }

    override fun onSearchCategoryClicked(category: DigitalHomePageSearchCategoryModel, position: Int) {
        rechargeHomepageAnalytics.eventSearchResultPageClick(category, position, userSession.userId)
        RouteManager.route(context, category.applink)
    }

    private fun trackSearchResultCategories(list: List<DigitalHomePageSearchCategoryModel>) {
        rechargeHomepageAnalytics.eventSearchResultPageImpression(list, userSession.userId)
    }

    open fun searchCategory(searchQuery: String) {
        viewModel.searchCategoryList(DigitalHomepageGqlQuery.digitalHomeCategory, searchQuery)
    }

    companion object {
        fun getInstance() = DigitalHomePageSearchFragment()
    }
}
