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
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.analytics.RechargeHomepageAnalytics
import com.tokopedia.digital.home.databinding.ViewRechargeHomeSearchBinding
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingAdditionalConstant.SCREEN_NAME_TOPUP_BILLS
import com.tokopedia.digital.home.di.RechargeHomepageComponent
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageSearchTypeFactory
import com.tokopedia.digital.home.presentation.adapter.viewholder.DigitalHomePageSearchDoubleLineViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.DigitalHomePageSearchViewHolder
import com.tokopedia.digital.home.presentation.viewmodel.DigitalHomePageSearchViewModel
import com.tokopedia.digital.home.util.DigitalHomepageGqlQuery
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoCleared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class DigitalHomePageSearchFragment : BaseListFragment<DigitalHomePageSearchCategoryModel, DigitalHomePageSearchTypeFactory>(),
        DigitalHomePageSearchViewHolder.OnSearchCategoryClickListener,
        DigitalHomePageSearchDoubleLineViewHolder.OnSearchDoubleLineClickListener,
        CoroutineScope
{

    protected var binding by autoCleared<ViewRechargeHomeSearchBinding>()
    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var rechargeHomepageAnalytics: RechargeHomepageAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: DigitalHomePageSearchViewModel

    var searchBarScreenName: String = SCREEN_NAME_TOPUP_BILLS

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ViewRechargeHomeSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            if (it.containsKey(PARAM_SEARCH_BAR_SCREEN_NAME))
                searchBarScreenName = it.getString(PARAM_SEARCH_BAR_SCREEN_NAME, SCREEN_NAME_TOPUP_BILLS)
        }

        activity?.run {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
            viewModel = viewModelProvider.get(DigitalHomePageSearchViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.digitalHomepageSearchViewSearchBar.clearListener = {
            clearAllData()
        }

        binding.digitalHomepageSearchViewToolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        // Show keyboard automatically
        binding.digitalHomepageSearchViewSearchBar.searchBarTextField.requestFocus()
        binding.digitalHomepageSearchViewSearchBar.searchBarTextField.setOnEditorActionListener(getSearchListener)
        binding.digitalHomepageSearchViewSearchBar.searchBarTextField.addTextChangedListener(object : TextWatcher {
            private var searchFor = ""
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
                val searchText = text.toString().trim()
                if (searchText == searchFor)
                    return

                searchFor = searchText

                launch {
                    delay(DELAY)
                    if (searchText != searchFor){
                        return@launch
                    } else {
                        if (searchText.isEmpty()) {
                            clearAllData()
                            renderList(emptyList())
                        } else {
                            onSearchBarTextChanged(text.toString())
                        }
                    }
                }
            }
        })
        context?.run {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(binding.digitalHomepageSearchViewSearchBar.searchBarTextField, InputMethod.SHOW_FORCED)
        }

        val recyclerView = getRecyclerView(view) as? VerticalRecyclerView
        recyclerView?.clearItemDecoration()
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PARAM_SEARCH_BAR_SCREEN_NAME, searchBarScreenName)
    }

    open fun onSearchBarTextChanged(text: String) {
        searchCategory(text)
    }

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
            when (it) {
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

    override fun showGetListError(throwable: Throwable) {
        hideLoading()

        updateStateScrollListener()

        if (adapter.itemCount > 0) {
            onGetListErrorWithExistingData(throwable)
        } else {
            val (errMsg, errCode) = ErrorHandler.getErrorMessagePair(
                    context, throwable, ErrorHandler.Builder().build())
            val errorNetworkModel = ErrorNetworkModel()

            errorNetworkModel.run {
                errorMessage = errMsg
                subErrorMessage = "${
                    getString(
                            com.tokopedia.kotlin.extensions.R.string.title_try_again)
                }. Kode Error: ($errCode)"
                onRetryListener = ErrorNetworkModel.OnRetryListener {
                    showLoading()
                    loadInitialData()
                }
            }
            adapter.run {
                setErrorNetworkModel(errorNetworkModel)
                showErrorNetwork()
            }
        }
    }

    override fun loadData(page: Int) {
        clearAllData()
    }

    override fun getAdapterTypeFactory(): DigitalHomePageSearchTypeFactory {
        return DigitalHomePageSearchTypeFactory(this, this)
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
        rechargeHomepageAnalytics.eventSearchResultPageClick(category, position,
                userSession.userId, searchBarScreenName)
        RouteManager.route(context, category.applink)
    }

    override fun onSearchDoubleLineClicked(category: DigitalHomePageSearchCategoryModel, position: Int) {
        RouteManager.route(context, category.applink)
    }

    private fun trackSearchResultCategories(list: List<DigitalHomePageSearchCategoryModel>) {
        rechargeHomepageAnalytics.eventSearchResultPageImpression(list,
                userSession.userId, searchBarScreenName)
    }

    open fun searchCategory(searchQuery: String) {
        viewModel.searchCategoryList(DigitalHomepageGqlQuery.digitalHomeCategory, searchQuery)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    companion object {
        private const val PARAM_SEARCH_BAR_SCREEN_NAME = "search_bar_screen_name"
        const val DELAY: Long = 200

        fun getInstance() = DigitalHomePageSearchFragment()
    }
}
