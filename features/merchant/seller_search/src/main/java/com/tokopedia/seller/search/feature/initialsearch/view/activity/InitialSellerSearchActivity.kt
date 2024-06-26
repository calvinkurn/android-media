package com.tokopedia.seller.search.feature.initialsearch.view.activity

import android.os.Build
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerComponentBuilder
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant
import com.tokopedia.seller.search.common.plt.GlobalSearchSellerPerformanceMonitoring
import com.tokopedia.seller.search.common.plt.GlobalSearchSellerPerformanceMonitoringListener
import com.tokopedia.seller.search.common.plt.GlobalSearchSellerPerformanceMonitoringType
import com.tokopedia.seller.search.feature.analytics.SellerSearchTracking
import com.tokopedia.seller.search.feature.initialsearch.di.component.DaggerInitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.di.component.InitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.di.module.InitialSearchModule
import com.tokopedia.seller.search.feature.initialsearch.view.fragment.InitialSearchFragment
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistoryViewUpdateListener
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.SuggestionViewUpdateListener
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.InitialSearchActivityViewModel
import com.tokopedia.seller.search.feature.initialsearch.view.widget.GlobalSearchView
import com.tokopedia.seller.search.feature.suggestion.view.fragment.SuggestionSearchFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class InitialSellerSearchActivity :
    BaseActivity(),
    HasComponent<InitialSearchComponent>,
    GlobalSearchView.GlobalSearchViewListener,
    GlobalSearchView.SearchTextBoxListener,
    HistoryViewUpdateListener,
    SuggestionViewUpdateListener,
    GlobalSearchSellerPerformanceMonitoringListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModel: InitialSearchActivityViewModel

    private val performanceMonitoring: GlobalSearchSellerPerformanceMonitoring by lazy {
        GlobalSearchSellerPerformanceMonitoring(GlobalSearchSellerPerformanceMonitoringType.SEARCH_SELLER)
    }

    private var globalSearchView: GlobalSearchView? = null

    private var mSuggestionView: ConstraintLayout? = null
    private var mInitialStateView: ConstraintLayout? = null

    private var suggestionFragment: SuggestionSearchFragment? = null
    private var initialStateFragment: InitialSearchFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        performanceMonitoring.initGlobalSearchSellerPerformanceMonitoring()
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        initInjector()
        setContentView(R.layout.activity_initial_seller_search)
        setWhiteStatusBar()
        proceed()
    }

    override fun getComponent(): InitialSearchComponent {
        return DaggerInitialSearchComponent
            .builder()
            .globalSearchSellerComponent(GlobalSearchSellerComponentBuilder.getComponent(application))
            .initialSearchModule(InitialSearchModule())
            .build()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SellerSearchTracking.clickBackButtonSearchEvent(
            userSession.userId,
            globalSearchView?.binding?.searchBarView?.searchBarTextField?.text?.trim().toString().orEmpty()
        )
    }

    private fun proceed() {
        initView()
        initSearchBarView()
        observeSearchPlaceholder()
        observeSearchKeyword()
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun initSearchBarView() {
        globalSearchView?.setActivity(this)
        globalSearchView?.setSearchViewListener(this)
        globalSearchView?.setSearchTextBoxListener(this)
        initialStateFragment?.setHistoryViewUpdateListener(this)
        suggestionFragment?.setSuggestionViewUpdateListener(this)
        setInitialKeyword()
    }

    private fun initView() {
        globalSearchView = findViewById(R.id.globalSearchSeller)
        mSuggestionView = findViewById(R.id.search_suggestion_container)
        mInitialStateView = findViewById(R.id.search_initial_state_container)
        suggestionFragment =
            supportFragmentManager.findFragmentById(R.id.search_suggestion) as? SuggestionSearchFragment
        initialStateFragment =
            supportFragmentManager.findFragmentById(R.id.search_initial_state) as? InitialSearchFragment
    }

    private fun setInitialKeyword() {
        getKeywordFromIntent().let { keyword ->
            proceedSearchKeyword(keyword)
            globalSearchView?.setKeyword(keyword)
        }
    }

    private fun getKeywordFromIntent(): String =
        intent?.data?.getQueryParameter(GlobalSearchSellerConstant.KEYWORD).orEmpty()

    override fun onQueryTextChangeListener(keyword: String) {
        viewModel.getTypingSearch(keyword)
    }

    override fun onClearTextBoxListener() {
        SellerSearchTracking.clickClearSearchBoxEvent(userSession.userId)
    }

    override fun onBackButtonSearchBar(keyword: String) {
        SellerSearchTracking.clickBackButtonSearchEvent(userSession.userId, keyword)
    }

    override fun showHistoryView() {
        mSuggestionView?.hide()
        mInitialStateView?.show()
    }

    override fun showSuggestionView() {
        mInitialStateView?.hide()
        mSuggestionView?.show()
    }

    override fun setKeywordSearchBarView(keyword: String) {
        globalSearchView?.setKeywordSearchBar(keyword)
    }

    override fun dropKeyboardSuggestion() {
        globalSearchView?.clearFocus()
        KeyboardHandler.DropKeyboard(this, globalSearchView)
    }

    private fun setWhiteStatusBar() {
        window?.decorView?.setBackgroundColor(
            ContextCompat.getColor(
                this,
                com.tokopedia.unifyprinciples.R.color.Unify_Background
            )
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(
                ContextCompat.getColor(
                    this,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN0
                )
            )
            setLightStatusBar(true)
        }
    }

    private fun observeSearchPlaceholder() {
        observe(viewModel.searchPlaceholder) {
            val placeholder = when (it) {
                is Success -> it.data
                is Fail -> getString(R.string.placeholder_search_seller)
            }
            globalSearchView?.setPlaceholder(placeholder)
        }
        viewModel.getSearchPlaceholder()
    }

    private fun observeSearchKeyword() {
        observe(viewModel.searchKeyword) {
            proceedSearchKeyword(it)
        }
    }

    private fun proceedSearchKeyword(keyword: String) {
        if (keyword.isEmpty()) {
            initialStateFragment?.historySearch(keyword)
        } else {
            suggestionFragment?.suggestionSearch(keyword)
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
