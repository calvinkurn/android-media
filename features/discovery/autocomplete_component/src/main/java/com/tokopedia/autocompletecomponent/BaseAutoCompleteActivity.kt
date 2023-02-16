package com.tokopedia.autocompletecomponent

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTracking
import com.tokopedia.autocompletecomponent.di.AutoCompleteComponent
import com.tokopedia.autocompletecomponent.di.DaggerAutoCompleteComponent
import com.tokopedia.autocompletecomponent.initialstate.InitialStateFragment
import com.tokopedia.autocompletecomponent.initialstate.InitialStateFragment.Companion.INITIAL_STATE_FRAGMENT_TAG
import com.tokopedia.autocompletecomponent.initialstate.InitialStateFragment.InitialStateViewUpdateListener
import com.tokopedia.autocompletecomponent.initialstate.di.DaggerInitialStateComponent
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateComponent
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateViewListenerModule
import com.tokopedia.autocompletecomponent.searchbar.SearchBarKeyword
import com.tokopedia.autocompletecomponent.searchbar.SearchBarKeywordAdapter
import com.tokopedia.autocompletecomponent.searchbar.SearchBarKeywordItemDecoration
import com.tokopedia.autocompletecomponent.searchbar.SearchBarKeywordListener
import com.tokopedia.autocompletecomponent.searchbar.SearchBarMpsState
import com.tokopedia.autocompletecomponent.searchbar.SearchBarView
import com.tokopedia.autocompletecomponent.searchbar.SearchBarViewModel
import com.tokopedia.autocompletecomponent.suggestion.SuggestionFragment
import com.tokopedia.autocompletecomponent.suggestion.SuggestionFragment.Companion.SUGGESTION_FRAGMENT_TAG
import com.tokopedia.autocompletecomponent.suggestion.SuggestionFragment.SuggestionViewUpdateListener
import com.tokopedia.autocompletecomponent.suggestion.di.DaggerSuggestionComponent
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionComponent
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionViewListenerModule
import com.tokopedia.autocompletecomponent.util.UrlParamHelper
import com.tokopedia.autocompletecomponent.util.addComponentId
import com.tokopedia.autocompletecomponent.util.addQueryIfEmpty
import com.tokopedia.autocompletecomponent.util.getWithDefault
import com.tokopedia.autocompletecomponent.util.removeKeys
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.BASE_SRP_APPLINK
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.HINT
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.PLACEHOLDER
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.discovery.common.utils.UrlParamUtils.isTokoNow
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import javax.inject.Inject

open class BaseAutoCompleteActivity: BaseActivity(),
    SearchBarView.OnQueryTextListener,
    SuggestionViewUpdateListener,
    InitialStateViewUpdateListener,
    SearchBarKeywordListener,
    SearchBarView.SearchBarViewListener {

    private val searchBarView by lazy {
        findViewById<SearchBarView?>(R.id.autocompleteSearchBar)
    }
    private val suggestionContainer by lazy {
        findViewById<ViewGroup?>(R.id.search_suggestion_container)
    }
    private val initialStateContainer by lazy {
        findViewById<ViewGroup?>(R.id.search_initial_state_container)
    }
    private val rvSearchBarKeyword by lazy {
        findViewById<RecyclerView>(R.id.rv_keyword_chips)
    }
    private val container by lazy {
        findViewById<View>(R.id.activity_container)
    }

    var viewModelFactory: ViewModelProvider.Factory? = null
        @Inject set

    private val viewModel: SearchBarViewModel? by lazy {
        viewModelFactory?.let {
            ViewModelProvider(this, it).get()
        }
    }

    protected val autoCompleteComponent: AutoCompleteComponent by lazy {
        createAutoCompleteComponent()
    }

    private lateinit var searchParameter: SearchParameter
    private lateinit var autoCompleteTracking: AutoCompleteTracking

    private var searchBarKeywordAdapter: SearchBarKeywordAdapter? = null

    private var coachMark: CoachMark2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        initInjector()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_complete)

        init(savedInstanceState)

        sendTracking()
    }

    private fun init(savedInstanceState: Bundle?) {
        initSearchParameter(savedInstanceState)
        initTracking()
        initViews()
        initObservers()
    }

    private fun initSearchParameter(savedInstanceState: Bundle?) {
        this.searchParameter = getSearchParameterFromIntent(savedInstanceState)
        if(savedInstanceState != null) {
            restoreViewModelSavedInstanceState(savedInstanceState)
        }
    }

    private fun restoreViewModelSavedInstanceState(savedInstanceState: Bundle) {
        val searchBarKeyword = getSearchBarKeyword(savedInstanceState)
        viewModel?.restoreSearchParameter(searchParameter, searchBarKeyword)
        val isIconPlusCoachMarkAlreadyDisplayed = savedInstanceState.getBoolean(KEY_ICON_PLUS_COACH_MARK_DISPLAYED, false)
        if(isIconPlusCoachMarkAlreadyDisplayed) viewModel?.markCoachMarkIconPlusAlreadyDisplayed()
        val isKeywordAddedCoachMarkAlreadyDisplayed = savedInstanceState.getBoolean(KEY_KEYWORD_ADDED_COACH_MARK_DISPLAYED, false)
        if(isKeywordAddedCoachMarkAlreadyDisplayed) viewModel?.markCoachMarkKeywordAddedAlreadyDisplayed()
    }

    private fun getSearchParameterFromIntent(savedInstanceState: Bundle?): SearchParameter {
        val uri = intent?.data

        val searchParameter = when {
            savedInstanceState != null -> savedInstanceState.getParcelable(
                KEY_SEARCH_PARAMETER
            ) as? SearchParameter ?: SearchParameter()
            uri == null -> SearchParameter()
            else -> SearchParameter(uri.toString())
        }

        searchParameter.cleanUpNullValuesInMap()
        searchParameter.modifyBaseSRPApplink(ApplinkConst.DISCOVERY_SEARCH)

        return searchParameter
    }

    private fun getSearchBarKeyword(savedInstanceState: Bundle) : SearchBarKeyword {
        return SearchBarKeyword(
            savedInstanceState.getInt(KEY_ACTIVE_KEYWORD_POSITION, 0),
            savedInstanceState.getString(KEY_ACTIVE_KEYWORD, ""),
        )
    }

    private fun SearchParameter.modifyBaseSRPApplink(defaultApplink: String) {
        val baseSRPApplinkParameter = get(BASE_SRP_APPLINK)

        if (baseSRPApplinkParameter.isEmpty())
            set(BASE_SRP_APPLINK, defaultApplink)
    }

    private fun initTracking() {
        autoCompleteTracking = AutoCompleteTracking(
            UserSession(this),
            IrisAnalytics.getInstance(this),
        )
    }

    private fun initViews() {
        setStatusBarColor()

        initSearchBarView()
        initRecyclerView()
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
        searchBarView.setViewListener(this)
    }

    private fun initRecyclerView() {
        searchBarKeywordAdapter = SearchBarKeywordAdapter(this)
        rvSearchBarKeyword?.apply {
            layoutManager = getLinearLayoutManager(context)
            adapter = searchBarKeywordAdapter
            addItemDecoration(SearchBarKeywordItemDecoration(context))
        }
    }

    private fun getLinearLayoutManager(context: Context) : LayoutManager {
        return LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false,
        )
    }

    private fun initFragments() {
        val initialStateComponent = createInitialStateComponent()
        val suggestionComponent = createSuggestionComponent()

        val initialStateFragment = InitialStateFragment.create(initialStateComponent)
        val suggestionFragment = SuggestionFragment.create(suggestionComponent)

        commitFragments(initialStateFragment, suggestionFragment)
    }

    private fun initObservers() {
        viewModel?.let {
            it.activeKeywordLiveData.observe(this) { keyword ->
                searchBarView.setActiveKeyword(keyword)
            }
            it.searchBarKeywords.observe(this) { searchBarKeywords ->
                renderSearchBarKeywords(searchBarKeywords)
            }
            it.mpsStateLiveData.observe(this) { mpsState ->
                renderMpsState(mpsState)
            }
            it.searchParameterLiveData.observe(this) { searchParameter ->
                onSearchParameterChange(searchParameter)
            }
        }
    }

    private fun renderSearchBarKeywords(searchBarKeywords: List<SearchBarKeyword>) {
        searchBarKeywordAdapter?.submitList(searchBarKeywords)
        if (searchBarKeywords.isNotEmpty()) {
            rvSearchBarKeyword.visible()
            searchBarView.preventKeyboardDismiss()
        } else {
            rvSearchBarKeyword.hide()
            searchBarView.allowKeyboardDismiss()
        }
    }

    protected open fun getBaseAppComponent(): BaseAppComponent? =
        (this.application as? BaseMainApplication)?.baseAppComponent

    private fun initInjector() {
        autoCompleteComponent.inject(this)
    }

    protected open fun createAutoCompleteComponent(): AutoCompleteComponent =
        DaggerAutoCompleteComponent.builder()
            .baseAppComponent(getBaseAppComponent())
            .build()

    protected open fun createInitialStateComponent(): InitialStateComponent =
        DaggerInitialStateComponent.builder()
            .baseAppComponent(getBaseAppComponent())
            .autoCompleteComponent(autoCompleteComponent)
            .initialStateViewListenerModule(getInitialStateViewListenerModule())
            .build()

    protected open fun getInitialStateViewListenerModule() =
        InitialStateViewListenerModule(this)

    protected open fun createSuggestionComponent(): SuggestionComponent =
        DaggerSuggestionComponent.builder()
            .baseAppComponent(getBaseAppComponent())
            .autoCompleteComponent(autoCompleteComponent)
            .suggestionViewListenerModule(getSuggestionViewListenerModule())
            .build()

    protected open fun getSuggestionViewListenerModule() =
        SuggestionViewListenerModule(this)

    private fun commitFragments(
        initialStateFragment: InitialStateFragment,
        suggestionFragment: SuggestionFragment,
    ) {
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

    private fun sendTracking() {
        sendTrackingInitiateSearchSession()
        sendTrackingFromAppShortcuts()
        sendTrackingVoiceSearchImpression()
    }

    private fun sendTrackingInitiateSearchSession() {
        val dimension90 = Dimension90Utils.getDimension90(searchParameter.getSearchParameterMap())
        autoCompleteTracking.eventInitiateSearchSession(dimension90)
    }

    private fun sendTrackingFromAppShortcuts() {
        val isFromAppShortcuts =
            intent?.getBooleanExtra(SearchConstant.FROM_APP_SHORTCUTS, false) ?: false

        if (isFromAppShortcuts)
            autoCompleteTracking.eventSearchShortcut()
    }

    private fun sendTrackingVoiceSearchImpression() {
        val pageSource = Dimension90Utils.getDimension90(searchParameter.getSearchParameterMap())
        autoCompleteTracking.eventImpressDiscoveryVoiceSearch(pageSource)
    }

    override fun onStart() {
        super.onStart()

        viewModel?.showSearch(searchParameter)
    }

    override fun isAllowShake(): Boolean = false

    override fun finish() {
        super.finish()

        overridePendingTransition(0, 0)
    }

    override fun onQueryTextSubmit(): Boolean {
        val searchParameterCopy = viewModel?.getSubmitSearchParameter() ?: return false

        if (getQueryOrHint(searchParameterCopy).isEmpty()) return true

        val searchResultApplink = createSearchResultApplink(searchParameterCopy)

        sendTrackingSubmitQuery(searchParameterCopy, searchResultApplink)
        clearFocusSearchView()
        moveToSearchPage(searchResultApplink)

        return true
    }

    private fun getQueryOrHint(searchParameter: SearchParameter): String {
        val query = searchParameter.getSearchQuery()

        return if (query.isNotEmpty()) query
        else searchParameter.get(HINT)
    }

    private fun createSearchResultApplink(searchParameter: SearchParameter): String {
        val parameter = searchParameter.getSearchParameterHashMap()
        val searchResultApplink = parameter.getWithDefault(
            BASE_SRP_APPLINK,
            ApplinkConstInternalDiscovery.SEARCH_RESULT
        )

        val modifiedParameter = parameter.toMutableMap().apply {
            addComponentId()
            addQueryIfEmpty()
            removeKeys(BASE_SRP_APPLINK, HINT, PLACEHOLDER)
        }

        return "$searchResultApplink?${UrlParamHelper.generateUrlParamString(modifiedParameter)}"
    }

    private fun sendTrackingSubmitQuery(
        searchParameter: SearchParameter,
        searchResultApplink: String,
    ) {
        val query = searchParameter.getSearchQuery()
        val queryOrHint = getQueryOrHint(searchParameter)
        val parameter = searchParameter.getSearchParameterMap()
        val pageSource = Dimension90Utils.getDimension90(parameter)
        val isInitialState = query.isEmpty()
        val fallback = { trackEventManualSearch(parameter, queryOrHint) }

        if (isInitialState)
            autoCompleteTracking.eventClickSubmitInitialState(
                queryOrHint,
                pageSource,
                searchResultApplink,
                fallback,
            )
        else
            autoCompleteTracking.eventClickSubmitAutoComplete(
                queryOrHint,
                pageSource,
                searchResultApplink,
                fallback,
            )
    }

    private fun trackEventManualSearch(parameter: Map<String, Any>, keyword: String) {
        if (isTokoNow(parameter))
            autoCompleteTracking.eventClickSubmitTokoNow(keyword)
        else
            autoCompleteTracking.eventClickSubmit(keyword)
    }

    private fun clearFocusSearchView() {
        searchBarView?.clearFocus()
    }

    private fun moveToSearchPage(applink: String) {
        RouteManager.route(this, applink)
        finish()
    }

    private fun onSearchParameterChange(searchParameter: SearchParameter) {
        this.searchParameter = SearchParameter(searchParameter)

        val shouldDisplayInitialState = searchParameter.getSearchQuery().isEmpty()
            || viewModel?.activeKeyword?.keyword.isNullOrBlank()

        if (shouldDisplayInitialState) {
            getInitialStateFragment()?.show(searchParameter.getSearchParameterHashMap())
        } else {
            val activeKeyword = viewModel?.activeKeyword ?: return
            val suggestionSearchParameter = if(searchParameter.isMps()) {
                SearchParameter(searchParameter).apply {
                    setMpsQuery(activeKeyword.keyword)
                }
            } else {
                searchParameter
            }
            getSuggestionFragment()?.getSuggestion(suggestionSearchParameter.getSearchParameterHashMap())
        }
    }

    override fun onQueryTextChanged(query: String) {
        viewModel?.onQueryUpdated(query)
    }

    override fun onKeywordRemoved(searchBarKeyword: SearchBarKeyword) {
        viewModel?.onKeywordRemoved(searchBarKeyword)
    }

    override fun onKeywordSelected(searchBarKeyword: SearchBarKeyword) {
        viewModel?.onKeywordSelected(searchBarKeyword)
    }

    override fun showAddedKeywordCoachMark(view: View) {
        if(coachMark != null || viewModel?.isCoachMarkKeywordAddedAlreadyDisplayed == true) return

        buildCoachMark2 {
            viewModel?.markCoachMarkKeywordAddedAlreadyDisplayed()
        }

        val coachMarkList = createAddedKeywordCoachMarkList(view)

        coachMark?.showCoachMark(coachMarkList, null, 0)
    }

    private fun showPlusIconCoachMark() {
        if(coachMark != null || viewModel?.isCoachMarkIconPlusAlreadyDisplayed == true) return

        buildCoachMark2 {
            viewModel?.markCoachMarkIconPlusAlreadyDisplayed()
        }

        val coachMarkList = createPlusIconCoachMark()

        if(coachMarkList.isEmpty()) return

        coachMark?.let {
            it.simpleMarginRight = 0
            it.simpleMarginLeft = 0
            it.showCoachMark(coachMarkList, null, 0)
        }
    }

    private fun getInitialStateFragment(): InitialStateFragment? =
        supportFragmentManager
            .findFragmentByTag(INITIAL_STATE_FRAGMENT_TAG) as? InitialStateFragment

    private fun getSuggestionFragment(): SuggestionFragment? =
        supportFragmentManager
            .findFragmentByTag(SUGGESTION_FRAGMENT_TAG) as? SuggestionFragment

    override fun showInitialStateView() {
        suggestionContainer?.hide()
        initialStateContainer?.show()
    }

    private fun renderMpsState(mpsState: SearchBarMpsState) {
        if (mpsState.isMpsEnabled) showMps() else hideMps()
        if (mpsState.isMpsAnimationEnabled) enableMpsIconAnimation() else disableMpsIconAnimation()
        if (mpsState.shouldShowCoachMark) {
            showPlusIconCoachMark()
        }
    }

    private fun showMps() {
        searchBarView.setMPSEnabled(true)
        searchBarView.showAddButton()
    }

    private fun hideMps() {
        searchBarView.setMPSEnabled(false)
        searchBarView.hideAddButton()
    }

    private fun enableMpsIconAnimation() {
        searchBarView.setMPSAnimationEnabled(true)
        searchBarView.startMpsAnimation()
    }
    private fun disableMpsIconAnimation() {
        searchBarView.stopMpsAnimation()
        searchBarView.setMPSAnimationEnabled(false)
    }

    override fun showSuggestionView() {
        initialStateContainer?.hide()
        suggestionContainer?.show()
    }

    override fun setIsTyping(isTyping: Boolean) {
        getSuggestionFragment()?.setIsTyping(isTyping)
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
        if (keyword != null && keyword.isNotEmpty()) {
            val pageSource = Dimension90Utils.getDimension90(searchParameter.getSearchParameterMap())
            autoCompleteTracking.eventClickDiscoveryVoiceSearch(keyword, pageSource)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val query = searchParameter.getSearchQuery()
        val pageSource = Dimension90Utils.getDimension90(searchParameter.getSearchParameterMap())

        autoCompleteTracking.eventCancelSearch(query, pageSource)
    }

    override fun onUpButtonClicked(view: View) {
        KeyboardHandler.DropKeyboard(this, view)
        onBackPressed()
    }

    override fun onAddButtonClicked(editable: Editable?) {
        val keyword = editable?.toString()
        if (viewModel?.isMpsEnabled == true && keyword.isNullOrBlank()) {
            Toaster.build(
                container,
                getString(R.string.searchbar_empty_keyword_error_message),
            )
                .show()
        } else {
            viewModel?.onKeywordAdded(keyword)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_SEARCH_PARAMETER, searchParameter)
        val activeKeyword = viewModel?.activeKeyword
        outState.putString(KEY_ACTIVE_KEYWORD, activeKeyword?.keyword)
        outState.putInt(KEY_ACTIVE_KEYWORD_POSITION, activeKeyword?.position ?: 0)
        outState.putBoolean(KEY_KEYWORD_ADDED_COACH_MARK_DISPLAYED, viewModel?.isCoachMarkKeywordAddedAlreadyDisplayed ?: false)
        outState.putBoolean(KEY_ICON_PLUS_COACH_MARK_DISPLAYED, viewModel?.isCoachMarkIconPlusAlreadyDisplayed ?: false)
        super.onSaveInstanceState(outState)
    }

    private fun buildCoachMark2(action: () -> Unit) {
        coachMark = CoachMark2(this).apply {
            setOnDismissListener {
                coachMark = null
            }
            onDismissListener = {
                action()
            }
        }
    }

    private fun createAddedKeywordCoachMarkList(
        view: View
    ) : ArrayList<CoachMark2Item> {
        return arrayListOf(
            createAddedKeywordCoachMark(view)
        )
    }

    private fun createAddedKeywordCoachMark(view: View): CoachMark2Item {
        return CoachMark2Item(
            view,
            getString(R.string.searchbar_added_keyword_coach_mark_title),
            getString(R.string.searchbar_added_keyword_coach_mark_message),
            CoachMark2.POSITION_BOTTOM
        )
    }

    private fun createPlusIconCoachMark(): ArrayList<CoachMark2Item> {
        val addButton = searchBarView?.addButton ?: return arrayListOf()
        return arrayListOf(
            CoachMark2Item(
                addButton,
                getString(R.string.searchbar_plus_icon_coach_mark_title),
                getString(R.string.searchbar_plus_icon_coach_mark_message),
                CoachMark2.POSITION_BOTTOM
            )
        )
    }

    companion object {
        private const val KEY_SEARCH_PARAMETER = "KEY_SEARCH_PARAMETER"
        private const val KEY_ACTIVE_KEYWORD = "KEY_ACTIVE_KEYWORD"
        private const val KEY_ACTIVE_KEYWORD_POSITION = "KEY_ACTIVE_KEYWORD_POSITION"
        private const val KEY_KEYWORD_ADDED_COACH_MARK_DISPLAYED = "KEY_KEYWORD_ADDED_COACH_MARK_DISPLAYED"
        private const val KEY_ICON_PLUS_COACH_MARK_DISPLAYED = "KEY_ICON_PLUS_COACH_MARK_DISPLAYED"
    }
}
