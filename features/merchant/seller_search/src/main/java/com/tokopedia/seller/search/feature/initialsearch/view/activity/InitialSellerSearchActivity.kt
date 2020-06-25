package com.tokopedia.seller.search.feature.initialsearch.view.activity

import android.graphics.Color
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerComponentBuilder
import com.tokopedia.seller.search.feature.analytics.SellerSearchTracking
import com.tokopedia.seller.search.feature.initialsearch.di.component.DaggerInitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.di.component.InitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.di.module.InitialSearchModule
import com.tokopedia.seller.search.feature.initialsearch.view.fragment.InitialSearchFragment
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistoryViewUpdateListener
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.SuggestionViewUpdateListener
import com.tokopedia.seller.search.feature.initialsearch.view.widget.GlobalSearchView
import com.tokopedia.seller.search.feature.suggestion.view.fragment.SuggestionSearchFragment
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class InitialSellerSearchActivity: BaseActivity(), HasComponent<InitialSearchComponent>,
        GlobalSearchView.GlobalSearchViewListener, HistoryViewUpdateListener, SuggestionViewUpdateListener {

    companion object {
        const val MIN_CHARACTER_SEARCH = 3
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    private var searchBarView: GlobalSearchView? = null

    private var mSuggestionView: ConstraintLayout? = null
    private var mInitialStateView: ConstraintLayout? = null

    private var suggestionFragment: SuggestionSearchFragment? = null
    private var initialStateFragment: InitialSearchFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_seller_search)
        window?.decorView?.setBackgroundColor(Color.WHITE)
        proceed()
    }

    override fun onStart() {
        super.onStart()
        SellerSearchTracking.sendScreenSearchEvent(userSession.userId.orEmpty())
    }

    override fun getComponent(): InitialSearchComponent {
        return DaggerInitialSearchComponent
                .builder()
                .globalSearchSellerComponent(GlobalSearchSellerComponentBuilder.getComponent(application))
                .initialSearchModule(InitialSearchModule())
                .build()
    }

    private fun proceed() {
        initView()
        initSearchBarView()
    }

    private fun initSearchBarView() {
        searchBarView?.setActivity(this)
        searchBarView?.setSearchViewListener(this)
    }

    private fun initView() {
        searchBarView = findViewById(R.id.globalSearchSeller)
        mSuggestionView = findViewById(R.id.search_suggestion_container)
        mInitialStateView = findViewById(R.id.search_initial_state_container)
        suggestionFragment = supportFragmentManager.findFragmentById(R.id.search_suggestion) as? SuggestionSearchFragment
        initialStateFragment = supportFragmentManager.findFragmentById(R.id.search_initial_state) as? InitialSearchFragment
    }

    override fun onQueryTextChangeListener(keyword: String) {
        if (keyword.isEmpty()) {
            initialStateFragment?.historySearch(keyword)
            initialStateFragment?.setHistoryViewUpdateListener(this)
        } else {
            if(keyword.length < MIN_CHARACTER_SEARCH) {
                initialStateFragment?.onMinCharState()
                initialStateFragment?.setHistoryViewUpdateListener(this)
            } else {
                suggestionFragment?.suggestionSearch(keyword)
                suggestionFragment?.setSuggestionViewUpdateListener(this)
            }
        }
    }

    override fun onClearTextBoxListener() {
        SellerSearchTracking.clickClearSearchBoxEvent(userSession.userId.orEmpty())
    }

    override fun onBackButtonSearchBar() {
        SellerSearchTracking.clickBackButtonSearchEvent(userSession.userId.orEmpty())
    }

    override fun showHistoryView() {
        mSuggestionView?.hide()
        mInitialStateView?.show()
    }

    override fun showSuggestionView() {
        mInitialStateView?.hide()
        mSuggestionView?.show()
    }

    override fun dropKeyboardHistory() {
        searchBarView?.clearFocus()
        KeyboardHandler.DropKeyboard(this, searchBarView)
    }

    override fun setKeywordSearchBarView(keyword: String) {
        searchBarView?.setKeywordSearchBar(keyword)
    }

    override fun dropKeyboardSuggestion() {
        searchBarView?.clearFocus()
        KeyboardHandler.DropKeyboard(this, searchBarView)
    }

}
