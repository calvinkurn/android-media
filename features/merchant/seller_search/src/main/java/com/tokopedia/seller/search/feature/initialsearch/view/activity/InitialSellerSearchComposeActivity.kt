package com.tokopedia.seller.search.feature.initialsearch.view.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.seller.search.common.GlobalSearchSellerComponentBuilder
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant
import com.tokopedia.seller.search.common.plt.GlobalSearchSellerPerformanceMonitoring
import com.tokopedia.seller.search.common.plt.GlobalSearchSellerPerformanceMonitoringListener
import com.tokopedia.seller.search.common.plt.GlobalSearchSellerPerformanceMonitoringType
import com.tokopedia.seller.search.common.util.IdViewGenerator
import com.tokopedia.seller.search.feature.analytics.SellerSearchTracking
import com.tokopedia.seller.search.feature.initialsearch.di.component.DaggerInitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.di.component.InitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.di.module.InitialSearchModule
import com.tokopedia.seller.search.feature.initialsearch.view.compose.InitialSearchActivityScreen
import com.tokopedia.seller.search.feature.initialsearch.view.fragment.InitialSearchComposeFragment
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiEvent
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistoryViewUpdateComposeListener
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.InitialSearchActivityComposeViewModel
import com.tokopedia.seller.search.feature.suggestion.view.fragment.SuggestionSearchComposeFragment
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@OptIn(ExperimentalComposeUiApi::class)
class InitialSellerSearchComposeActivity :
    BaseActivity(),
    HasComponent<InitialSearchComponent>,
    HistoryViewUpdateComposeListener,
    GlobalSearchSellerPerformanceMonitoringListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val performanceMonitoring: GlobalSearchSellerPerformanceMonitoring by lazy {
        GlobalSearchSellerPerformanceMonitoring(GlobalSearchSellerPerformanceMonitoringType.SEARCH_SELLER)
    }

    private val viewModel: InitialSearchActivityComposeViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(InitialSearchActivityComposeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        performanceMonitoring.initGlobalSearchSellerPerformanceMonitoring()
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        initInjector()

        setContent {
            NestTheme {
                val initialStateContainerId =
                    rememberSaveable { IdViewGenerator.generateUniqueId(InitialSearchComposeFragment::class.java.simpleName) }
                val suggestionSearchContainerId = rememberSaveable {
                    IdViewGenerator.generateUniqueId(SuggestionSearchComposeFragment::class.java.name)
                }

                val fragmentManager = rememberSaveable { supportFragmentManager }

                val suggestionSearchFragment =
                    remember { fragmentManager.findFragmentById(suggestionSearchContainerId) as? SuggestionSearchComposeFragment }
                val initialSearchFragment = remember {
                    fragmentManager.findFragmentById(initialStateContainerId) as? InitialSearchComposeFragment
                }

                val softwareKeyboardController = LocalSoftwareKeyboardController.current

                val uiState = viewModel.globalSearchUiState.collectAsState()

                val showSearchSuggestions = uiState.value.searchBarKeyword.isNotBlank()

                LaunchedEffect(key1 = showSearchSuggestions, block = {
                    showSearchFragment(
                        fragmentManager,
                        initialSearchFragment,
                        suggestionSearchFragment,
                        initialStateContainerId,
                        suggestionSearchContainerId,
                        showSearchSuggestions
                    )

                    collectUiEffect(
                        showSearchSuggestions,
                        fragmentManager,
                        suggestionSearchFragment,
                        initialSearchFragment,
                        suggestionSearchContainerId,
                        initialStateContainerId,
                        softwareKeyboardController
                    )
                })

                InitialSearchActivityScreen(
                    uiEffect = viewModel::onUiEffect,
                    uiState = uiState.value,
                    showSearchSuggestions = showSearchSuggestions,
                    initialStateContainerId = initialStateContainerId,
                    suggestionSearchContainerId = suggestionSearchContainerId,
                    softwareKeyboardController = softwareKeyboardController
                )
            }
        }

        fetchSearchPlaceholder()
        setSearchKeyword()
    }

    override fun getComponent(): InitialSearchComponent {
        return DaggerInitialSearchComponent
            .builder()
            .globalSearchSellerComponent(GlobalSearchSellerComponentBuilder.getComponent(application))
            .initialSearchModule(InitialSearchModule())
            .build()
    }

    private suspend fun collectUiEffect(
        showSearchSuggestions: Boolean,
        fragmentManager: FragmentManager?,
        suggestionSearchFragment: SuggestionSearchComposeFragment?,
        initialSearchFragment: InitialSearchComposeFragment?,
        suggestionSearchContainerId: Int,
        initialStateContainerId: Int,
        softwareKeyboardController: SoftwareKeyboardController?
    ) {
        viewModel.uiEffect.collectLatest {
            when (it) {
                is GlobalSearchUiEvent.OnSearchBarCleared -> {
                    SellerSearchTracking.clickClearSearchBoxEvent(userSession.userId)
                    viewModel.setTypingSearch(String.EMPTY, TextRange(Int.ZERO))
                }

                is GlobalSearchUiEvent.OnBackButtonClicked -> {
                    SellerSearchTracking.clickBackButtonSearchEvent(
                        userSession.userId,
                        it.searchBarKeyword
                    )
                    finish()
                }

                is GlobalSearchUiEvent.OnKeyboardSearchSubmit -> {
                    val textRange = TextRange(it.searchBarKeyword.length)
                    viewModel.setTypingSearch(it.searchBarKeyword, textRange)
                    softwareKeyboardController?.hide()
                }

                is GlobalSearchUiEvent.OnSearchResultKeyword -> {
                    onSearchResultKeywordFetch(
                        showSearchSuggestions,
                        fragmentManager,
                        suggestionSearchFragment,
                        initialSearchFragment,
                        suggestionSearchContainerId,
                        initialStateContainerId,
                        it.searchBarKeyword
                    )
                }

                else -> {
                }
            }
        }
    }

    private fun onSearchResultKeywordFetch(
        showSearchSuggestions: Boolean,
        fragmentManager: FragmentManager?,
        suggestionSearchFragment: SuggestionSearchComposeFragment?,
        initialSearchFragment: InitialSearchComposeFragment?,
        suggestionSearchContainerId: Int,
        initialStateContainerId: Int,
        searchBarKeyword: String
    ) {
        if (showSearchSuggestions) {
            suggestionSearchFragment?.suggestionSearch(searchBarKeyword)
                ?: (fragmentManager?.findFragmentById(suggestionSearchContainerId) as? SuggestionSearchComposeFragment)?.apply {
                    suggestionSearch(searchBarKeyword)
                }
        } else {
            initialSearchFragment?.fetchHistorySearch(searchBarKeyword)
                ?: (fragmentManager?.findFragmentById(initialStateContainerId) as? InitialSearchComposeFragment)?.apply {
                    fetchHistorySearch(searchBarKeyword)
                }
        }
    }

    private fun fetchSearchPlaceholder() {
        viewModel.getSearchPlaceholder()
    }

    private fun setSearchKeyword() {
        val keyword = getKeywordFromIntent()
        viewModel.setTypingSearch(keyword, TextRange(keyword.length))
    }

    private fun getKeywordFromIntent(): String =
        intent?.data?.getQueryParameter(GlobalSearchSellerConstant.KEYWORD).orEmpty()

    private fun initInjector() {
        component.inject(this)
    }

    private fun showSearchFragment(
        fragmentManager: FragmentManager?,
        initialSearchFragment: InitialSearchComposeFragment?,
        suggestionSearchFragment: SuggestionSearchComposeFragment?,
        initialStateContainerId: Int,
        suggestionSearchContainerId: Int,
        showSearchSuggestions: Boolean
    ) {
        fragmentManager?.commit(allowStateLoss = true) {
            if (showSearchSuggestions) {
                showSuggestionSearchFragment(
                    initialSearchFragment,
                    suggestionSearchFragment,
                    suggestionSearchContainerId
                )
            } else {
                showInitialSearchFragment(
                    initialSearchFragment,
                    suggestionSearchFragment,
                    initialStateContainerId
                )
            }
        }
    }

    private fun FragmentTransaction.showSuggestionSearchFragment(
        initialSearchFragment: InitialSearchComposeFragment?,
        suggestionSearchFragment: SuggestionSearchComposeFragment?,
        suggestionSearchContainerId: Int
    ) {
        if (initialSearchFragment?.isVisible == true) {
            hide(initialSearchFragment)
        }
        if (suggestionSearchFragment?.isAdded == true) {
            if (!suggestionSearchFragment.isVisible) {
                show(suggestionSearchFragment)
            }
        } else {
            add(suggestionSearchContainerId, SuggestionSearchComposeFragment())
        }
    }

    private fun FragmentTransaction.showInitialSearchFragment(
        initialSearchFragment: InitialSearchComposeFragment?,
        suggestionSearchFragment: SuggestionSearchComposeFragment?,
        initialStateContainerId: Int
    ) {
        if (suggestionSearchFragment?.isVisible == true) {
            hide(suggestionSearchFragment)
        }
        if (initialSearchFragment?.isAdded == true) {
            if (!initialSearchFragment.isVisible) {
                show(initialSearchFragment)
            }
        } else {
            add(
                initialStateContainerId,
                InitialSearchComposeFragment().apply {
                    setHistoryViewUpdateListener(this@InitialSellerSearchComposeActivity)
                }
            )
        }
    }

    override fun startNetworkPerformanceMonitoring() {
        performanceMonitoring.startNetworkGlobalSearchSellerPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        performanceMonitoring.startRenderGlobalSearchSellerPerformanceMonitoring()
    }

    override fun finishMonitoring() {
        performanceMonitoring.stopPerformanceMonitoring()
    }

    override fun setKeywordSearchBarView(keyword: String) {
        val textRange = TextRange(keyword.length)
        viewModel.setTypingSearch(keyword, textRange)
    }
}
