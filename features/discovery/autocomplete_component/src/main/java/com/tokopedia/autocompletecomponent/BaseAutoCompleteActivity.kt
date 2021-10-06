package com.tokopedia.autocompletecomponent

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTracking
import com.tokopedia.autocompletecomponent.initialstate.InitialStateFragment
import com.tokopedia.autocompletecomponent.initialstate.InitialStateFragment.Companion.INITIAL_STATE_FRAGMENT_TAG
import com.tokopedia.autocompletecomponent.initialstate.InitialStateFragment.InitialStateViewUpdateListener
import com.tokopedia.autocompletecomponent.initialstate.di.DaggerInitialStateComponent
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateComponent
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateViewListenerModule
import com.tokopedia.autocompletecomponent.searchbar.SearchBarView
import com.tokopedia.autocompletecomponent.suggestion.SuggestionFragment
import com.tokopedia.autocompletecomponent.suggestion.SuggestionFragment.Companion.SUGGESTION_FRAGMENT_TAG
import com.tokopedia.autocompletecomponent.suggestion.SuggestionFragment.SuggestionViewUpdateListener
import com.tokopedia.autocompletecomponent.suggestion.di.DaggerSuggestionComponent
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionComponent
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionViewListenerModule
import com.tokopedia.autocompletecomponent.util.UrlParamHelper
import com.tokopedia.autocompletecomponent.util.getWithDefault
import com.tokopedia.autocompletecomponent.util.removeKeys
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.BASE_SRP_APPLINK
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.HINT
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery.common.utils.UrlParamUtils.isTokoNow
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

open class BaseAutoCompleteActivity: BaseActivity(),
    SearchBarView.OnQueryTextListener,
    SuggestionViewUpdateListener,
    InitialStateViewUpdateListener {

    private val searchBarView by lazy {
        findViewById<SearchBarView?>(R.id.autocompleteSearchBar)
    }
    private val suggestionContainer by lazy {
        findViewById<ViewGroup?>(R.id.search_suggestion_container)
    }
    private val initialStateContainer by lazy {
        findViewById<ViewGroup?>(R.id.search_initial_state_container)
    }

    private lateinit var searchParameter: SearchParameter
    private var suggestionFragment: SuggestionFragment? = null
    private var initialStateFragment: InitialStateFragment? = null

    protected lateinit var autoCompleteTracking: AutoCompleteTracking

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_complete)

        init()

        sendTrackingFromAppShortcuts()
    }

    private fun init() {
        initSearchParameter()
        initTracking()
        initViews()
    }

    private fun initSearchParameter() {
        this.searchParameter = getSearchParameterFromIntent()
    }

    private fun getSearchParameterFromIntent(): SearchParameter {
        val uri = intent?.data

        val searchParameter = if (uri == null) SearchParameter()
        else SearchParameter(uri.toString())

        searchParameter.cleanUpNullValuesInMap()
        searchParameter.modifyBaseSRPApplink(ApplinkConst.DISCOVERY_SEARCH)

        return searchParameter
    }

    private fun SearchParameter.modifyBaseSRPApplink(defaultApplink: String) {
        val baseSRPApplinkParameter = get(BASE_SRP_APPLINK)

        if (baseSRPApplinkParameter.isEmpty())
            set(BASE_SRP_APPLINK, defaultApplink)
    }

    private fun initTracking() {
        autoCompleteTracking = AutoCompleteTracking(UserSession(this))
    }

    private fun initViews() {
        setStatusBarColor()

        initSearchBarView()
        initFragments()
    }

    private fun setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !this.isDarkMode())
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        window.apply {
            clearFlags(FLAG_TRANSLUCENT_STATUS)
            addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = ContextCompat.getColor(
                this@BaseAutoCompleteActivity,
                com.tokopedia.unifyprinciples.R.color.Unify_N0
            )
        }
    }

    private fun initSearchBarView() {
        val searchBarView = searchBarView ?: return

        searchBarView.setActivity(this)
        searchBarView.setOnQueryTextListener(this)
    }

    private fun initFragments() {
        val baseAppComponent = getBaseAppComponent() ?: return

        val initialStateComponent = createInitialStateComponent(baseAppComponent)
        val suggestionComponent = createSuggestionComponent(baseAppComponent)

        initialStateFragment = InitialStateFragment.create(initialStateComponent)
        suggestionFragment = SuggestionFragment.create(suggestionComponent)

        commitFragments()
    }

    protected open fun getBaseAppComponent(): BaseAppComponent? =
        (this.application as? BaseMainApplication)?.baseAppComponent

    protected open fun createInitialStateComponent(
        baseAppComponent: BaseAppComponent
    ): InitialStateComponent =
        DaggerInitialStateComponent.builder()
            .baseAppComponent(baseAppComponent)
            .initialStateViewListenerModule(InitialStateViewListenerModule(this))
            .build()

    protected open fun createSuggestionComponent(
        baseAppComponent: BaseAppComponent
    ): SuggestionComponent =
        DaggerSuggestionComponent.builder()
            .baseAppComponent(baseAppComponent)
            .suggestionViewListenerModule(SuggestionViewListenerModule(this))
            .build()

    private fun commitFragments() {
        val initialStateFragment = initialStateFragment ?: return
        val suggestionFragment = suggestionFragment ?: return

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.search_initial_state_container,
                initialStateFragment,
                INITIAL_STATE_FRAGMENT_TAG
            ).replace(
                R.id.search_suggestion_container,
                suggestionFragment,
                SUGGESTION_FRAGMENT_TAG
            ).commit()
    }

    private fun sendTrackingFromAppShortcuts() {
        val isFromAppShortcuts =
            intent?.getBooleanExtra(SearchConstant.FROM_APP_SHORTCUTS, false) ?: false

        if (isFromAppShortcuts)
            autoCompleteTracking.eventSearchShortcut()
    }

    override fun onStart() {
        super.onStart()

        searchBarView?.showSearch(searchParameter)
    }

    override fun isAllowShake(): Boolean = false

    override fun finish() {
        super.finish()

        overridePendingTransition(0, 0)
    }

    override fun onQueryTextSubmit(searchParameter: SearchParameter): Boolean {
        val searchParameterCopy = SearchParameter(searchParameter)

        sendTrackingSubmitQuery(searchParameterCopy)
        clearFocusSearchView()
        moveToSearchPage(searchParameterCopy)

        return true
    }

    private fun sendTrackingSubmitQuery(searchParameter: SearchParameter) {
        val query = searchParameter.getSearchQuery()
        val parameter = searchParameter.getSearchParameterMap()

        if (isTokoNow(parameter))
            autoCompleteTracking.eventClickSubmitTokoNow(query)
        else
            autoCompleteTracking.eventClickSubmit(query)
    }

    private fun clearFocusSearchView() {
        searchBarView?.clearFocus()
    }

    private fun moveToSearchPage(searchParameter: SearchParameter) {
        RouteManager.route(this, createSearchResultApplink(searchParameter))
        finish()
    }

    private fun createSearchResultApplink(searchParameter: SearchParameter): String {
        val parameter = searchParameter.getSearchParameterHashMap()
        val searchResultApplink = parameter.getWithDefault(
            BASE_SRP_APPLINK,
            ApplinkConstInternalDiscovery.SEARCH_RESULT
        )

        parameter.removeKeys(BASE_SRP_APPLINK, HINT)

        return "$searchResultApplink?${UrlParamHelper.generateUrlParamString(parameter)}"
    }

    override fun onQueryTextChange(searchParameter: SearchParameter) {
        this.searchParameter = SearchParameter(searchParameter)

        if (searchParameter.getSearchQuery().isEmpty())
            initialStateFragment?.show(searchParameter.getSearchParameterHashMap())
        else
            suggestionFragment?.getSuggestion(searchParameter.getSearchParameterHashMap())
    }

    override fun showInitialStateView() {
        suggestionContainer?.hide()
        initialStateContainer?.show()
    }

    override fun showSuggestionView() {
        initialStateContainer?.hide()
        suggestionContainer?.show()
    }

    override fun setIsTyping(isTyping: Boolean) {
        suggestionFragment?.setIsTyping(isTyping)
    }

    override fun setSearchQuery(keyword: String) {
        searchBarView?.setQuery(keyword, submit = false, copyText = true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == SearchBarView.REQUEST_VOICE) {
                onVoiceSearchClicked(data)
            }
        }
    }

    private fun onVoiceSearchClicked(data: Intent?) {
        val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
        if (results != null && results.isNotEmpty()) {
            val query = results[0]
            searchBarView?.setQuery(query, false)
            sendVoiceSearchGTM(query)
        }
    }

    private fun sendVoiceSearchGTM(keyword: String?) {
        if (keyword != null && keyword.isNotEmpty())
            autoCompleteTracking.eventDiscoveryVoiceSearch(keyword)
    }
}