package com.tokopedia.seller.search.feature.initialsearch.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerComponentBuilder
import com.tokopedia.seller.search.feature.initialsearch.di.component.DaggerInitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.di.component.InitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.di.module.InitialSearchModule
import com.tokopedia.seller.search.feature.initialsearch.view.fragment.InitialSearchFragment
import com.tokopedia.seller.search.feature.initialsearch.view.fragment.SuggestionSearchFragment
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistoryViewUpdateListener
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.SuggestionViewUpdateListener
import com.tokopedia.seller.search.feature.initialsearch.view.widget.GlobalSearchView

class InitialSellerSearchActivity: BaseActivity(), HasComponent<InitialSearchComponent>,
        GlobalSearchView.GlobalSearchViewListener, HistoryViewUpdateListener, SuggestionViewUpdateListener {

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
        setStatusBarColor()
        proceed()
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
        searchBarView?.setSearchViewListener(this)
        searchBarView?.setActivity(this)
        initialStateFragment?.setHistoryViewUpdateListener(this)
        suggestionFragment?.setSuggestionViewUpdateListener(this)
    }

    private fun setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            getWindow().decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = resources.getColor(R.color.white, null)
        }
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
            suggestionFragment?.setSearchKeyword(keyword)
        } else {
            suggestionFragment?.suggestionSearch(keyword)
            initialStateFragment?.setSearchKeyword(keyword)
        }
    }

    override fun onMinCharState() {
        initialStateFragment?.onMinCharState()
    }

    override fun showHistoryView() {
        mSuggestionView?.hide()
        mInitialStateView?.show()
    }

    override fun dropKeyboardHistory() {
        searchBarView?.clearFocus()
        KeyboardHandler.DropKeyboard(this, searchBarView)
    }

    override fun showSuggestionView() {
        mSuggestionView?.show()
        mInitialStateView?.hide()
    }

    override fun dropKeyboardSuggestion() {
        searchBarView?.clearFocus()
        KeyboardHandler.DropKeyboard(this, searchBarView)
    }

}
