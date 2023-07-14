package com.tokopedia.seller.search.feature.initialsearch.view.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerComponentBuilder
import com.tokopedia.seller.search.common.plt.GlobalSearchSellerPerformanceMonitoring
import com.tokopedia.seller.search.common.plt.GlobalSearchSellerPerformanceMonitoringListener
import com.tokopedia.seller.search.common.plt.GlobalSearchSellerPerformanceMonitoringType
import com.tokopedia.seller.search.feature.initialsearch.di.component.DaggerInitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.di.component.InitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.di.module.InitialSearchModule
import com.tokopedia.seller.search.feature.initialsearch.view.fragment.InitialSearchFragment
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.InitialSearchActivityComposeViewModel
import com.tokopedia.seller.search.feature.suggestion.view.fragment.SuggestionSearchFragment
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class InitialSellerSearchComposeActivity :
    BaseActivity(),
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
        super.onCreate(savedInstanceState)
        initInjector()
        initFragments()

        setContent {
            NestTheme {
//                InitialSearchActivityScreen()
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

    private fun initInjector() {
        component.inject(this)
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

    private fun initFragments() {
        searchSuggestionFragment = SuggestionSearchFragment()
        initialSearchFragment = InitialSearchFragment()

        searchSuggestionFragment?.let { searchSuggestionFragment ->
            initialSearchFragment?.let { initialSearchFragment ->
                supportFragmentManager.beginTransaction()
                    .hide(searchSuggestionFragment)
                    .add(android.R.id.content, searchSuggestionFragment)
                    .add(android.R.id.content, initialSearchFragment)
                    .commit()
            }
        }
    }
}
