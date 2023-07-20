package com.tokopedia.seller.search.feature.initialsearch.view.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.seller.search.common.GlobalSearchSellerComponentBuilder
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant
import com.tokopedia.seller.search.common.plt.GlobalSearchSellerPerformanceMonitoring
import com.tokopedia.seller.search.common.plt.GlobalSearchSellerPerformanceMonitoringListener
import com.tokopedia.seller.search.common.plt.GlobalSearchSellerPerformanceMonitoringType
import com.tokopedia.seller.search.feature.analytics.SellerSearchTracking
import com.tokopedia.seller.search.feature.initialsearch.di.component.DaggerInitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.di.component.InitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.di.module.InitialSearchModule
import com.tokopedia.seller.search.feature.initialsearch.view.compose.InitialSearchActivityScreen
import com.tokopedia.seller.search.feature.initialsearch.view.fragment.InitialSearchFragment
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiEffect
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.InitialSearchActivityComposeViewModel
import com.tokopedia.seller.search.feature.suggestion.view.fragment.SuggestionSearchFragment
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class InitialSellerSearchComposeActivity :
    AppCompatActivity(),
    HasComponent<InitialSearchComponent>,
    GlobalSearchSellerPerformanceMonitoringListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val performanceMonitoring: GlobalSearchSellerPerformanceMonitoring by lazy {
        GlobalSearchSellerPerformanceMonitoring(GlobalSearchSellerPerformanceMonitoringType.SEARCH_SELLER)
    }

    private val viewModel: InitialSearchActivityComposeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(InitialSearchActivityComposeViewModel::class.java)
    }

    private var searchSuggestionFragment: SuggestionSearchFragment? = null
    private var initialSearchFragment: InitialSearchFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        performanceMonitoring.initGlobalSearchSellerPerformanceMonitoring()
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
//        initFragments()
        fetchSearchPlaceholder()
        setSearchKeyword()

        setContent {
            NestTheme {
                val uiState = viewModel.globalSearchUiState.collectAsState()

                InitialSearchActivityScreen(
                    uiEffect = ::onUiEffect,
                    uiState = uiState.value,
                    supportFragmentManager = supportFragmentManager
                )
            }
        }
    }

    override fun getComponent(): InitialSearchComponent {
        return DaggerInitialSearchComponent
            .builder()
            .globalSearchSellerComponent(GlobalSearchSellerComponentBuilder.getComponent(application))
            .initialSearchModule(InitialSearchModule())
            .build()
    }

    private fun fetchSearchPlaceholder() {
        viewModel.getSearchPlaceholder()
    }

    private fun setSearchKeyword() {
        viewModel.setTypingSearch(getKeywordFromIntent())
    }

    private fun getKeywordFromIntent(): String =
        intent?.data?.getQueryParameter(GlobalSearchSellerConstant.KEYWORD).orEmpty()

    private fun initInjector() {
        component.inject(this)
    }

    private fun onUiEffect(event: GlobalSearchUiEffect) {
        when (event) {
            is GlobalSearchUiEffect.OnSearchBarCleared -> {
                SellerSearchTracking.clickClearSearchBoxEvent(userSession.userId)
                viewModel.setTypingSearch(String.EMPTY)
            }
            is GlobalSearchUiEffect.OnKeyboardSearchSubmit -> {
                viewModel.setTypingSearch(event.searchBarKeyword)
            }
            is GlobalSearchUiEffect.OnKeywordTextChanged -> {
                viewModel.setTypingSearch(event.searchBarKeyword)
            }
            is GlobalSearchUiEffect.OnBackButtonClicked -> {
                SellerSearchTracking.clickBackButtonSearchEvent(userSession.userId, event.searchBarKeyword)
                finish()
            }
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
}
