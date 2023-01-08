package com.tokopedia.tokofood.feature.search.container.presentation.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.util.Constant
import com.tokopedia.tokofood.databinding.FragmentSearchContainerBinding
import com.tokopedia.tokofood.feature.search.common.util.hideKeyboardOnTouchListener
import com.tokopedia.tokofood.feature.search.container.presentation.listener.InitialStateViewUpdateListener
import com.tokopedia.tokofood.feature.search.container.presentation.listener.SearchResultViewUpdateListener
import com.tokopedia.tokofood.feature.search.container.presentation.viewmodel.SearchContainerViewModel
import com.tokopedia.tokofood.feature.search.container.presentation.widget.GlobalSearchBarWidget
import com.tokopedia.tokofood.feature.search.di.component.DaggerTokoFoodSearchComponent
import com.tokopedia.tokofood.feature.search.initialstate.presentation.fragment.InitialSearchStateFragment
import com.tokopedia.tokofood.feature.search.searchresult.presentation.fragment.SearchResultFragment
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class SearchContainerFragment : BaseDaggerFragment(),
    GlobalSearchBarWidget.GlobalSearchBarWidgetListener,
    GlobalSearchBarWidget.SearchTextBoxListener,
    InitialStateViewUpdateListener, SearchResultViewUpdateListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(SearchContainerViewModel::class.java)
    }

    private var binding by autoClearedNullable<FragmentSearchContainerBinding>()

    private var searchResultFragment: SearchResultFragment? = null
    private var initialSearchStateFragment: InitialSearchStateFragment? = null
    private var globalSearchBarWidget: GlobalSearchBarWidget? = null

    private var keyword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments?.getString(Constant.DATA_KEY).orEmpty()
        if (bundle.isNotBlank()) {
            val uri = Uri.parse(bundle)
            keyword = uri.getQueryParameter(KEYWORD_PARAM).orEmpty()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchContainerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        observeSearchKeyword()
        keyword?.let { setKeywordSearchBarView(it) }
        globalSearchBarWidget.hideKeyboardOnTouchListener()
    }

    override fun onDestroyView() {
        initialSearchStateFragment = null
        searchResultFragment = null
        globalSearchBarWidget = null
        keyword = null
        super.onDestroyView()
    }

    override fun initInjector() {
        activity?.let {
            DaggerTokoFoodSearchComponent
                .builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        }
    }

    override fun getScreenName(): String = ""

    override fun setKeywordSearchBarView(keyword: String) {
        globalSearchBarWidget?.setKeywordSearchBar(keyword)
    }

    override fun hideKeyboard() {
        view?.let {
            KeyboardHandler.DropKeyboard(it.context, view)
        }
    }

    override fun showInitialStateView() {
        binding?.run {
            searchResultContainer.hide()
            initialStateContainer.show()
        }

        setShowInitialStateCommit()
    }

    private fun setShowInitialStateCommit() {
        initialSearchStateFragment?.let { initialStateFragment ->
            searchResultFragment?.let { searchResultFragment ->
                val ft = childFragmentManager.beginTransaction()
                if (searchResultFragment.isVisible && !initialStateFragment.isVisible) {
                    ft.hide(searchResultFragment)
                    ft.show(initialStateFragment)
                }
                ft.commit()
            }
        }
    }

    override fun showSearchResultView() {
        binding?.run {
            initialStateContainer.hide()
            searchResultContainer.show()
        }

        setShowSearchResultCommit()
    }

    private fun setShowSearchResultCommit() {
        searchResultFragment?.let { searchResultFragment ->
            initialSearchStateFragment?.let { initialStateFragment ->
                val ft = childFragmentManager.beginTransaction()
                if (!searchResultFragment.isVisible && initialStateFragment.isVisible) {
                    ft.hide(initialStateFragment)
                    ft.show(searchResultFragment)
                }
                ft.commit()
            }
        }
    }

    override fun onResetKeyword() {
        globalSearchBarWidget?.setKeywordSearchBar(String.EMPTY)
    }

    override fun onBackButtonSearchBarClicked(keyword: String) {
        onBackPressed()
    }

    override fun onTransactionListClicked() {
        context?.let {
            RouteManager.route(it, ApplinkConst.TokoFood.TOKOFOOD_ORDER)
        }
    }

    override fun onGlobalNavClicked() {
        context?.let {
            RouteManager.route(it, ApplinkConst.HOME_NAVIGATION)
        }
    }

    override fun onQueryTextChangeListener(keyword: String) {
        viewModel.setKeyword(keyword)
    }

    private fun initView(view: View) {
        globalSearchBarWidget = view.findViewById(R.id.globalSearchBarWidget)
        searchResultFragment =
            childFragmentManager.findFragmentById(R.id.searchResultContainer) as? SearchResultFragment
        initialSearchStateFragment =
            childFragmentManager.findFragmentById(R.id.initialStateContainer) as? InitialSearchStateFragment
        initialSearchStateFragment?.setInitialStateViewUpdateListener(this)
        searchResultFragment?.setSearchResultViewUpdateListener(this)
        globalSearchBarWidget?.setGlobalSearchBarWidgetListener(this, this)
    }

    private fun observeSearchKeyword() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.keywordResult.collect { keyword ->
                if (keyword.length < THREE_LETTERS) {
                    proceedInitialState(keyword)
                } else {
                    proceedSearchResult(keyword)
                }
            }
        }
    }

    private fun proceedInitialState(keyword: String) {
        initialSearchStateFragment?.showInitialSearchState(keyword)
    }

    private fun proceedSearchResult(keyword: String) {
        searchResultFragment?.showSearchResultState(keyword)
    }

    private fun onBackPressed() {
        context?.let {
            (it as? AppCompatActivity)?.onBackPressed()
        }
    }

    companion object {
        fun createInstance(): SearchContainerFragment {
            return SearchContainerFragment()
        }

        const val THREE_LETTERS = 3
        const val KEYWORD_PARAM = "q"
    }
}
