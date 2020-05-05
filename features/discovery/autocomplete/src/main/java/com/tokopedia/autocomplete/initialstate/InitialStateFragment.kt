package com.tokopedia.autocomplete.initialstate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.autocomplete.AutoCompleteActivity
import com.tokopedia.autocomplete.OnScrollListenerAutocomplete
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.analytics.AppScreen
import com.tokopedia.autocomplete.analytics.AutocompleteTracking
import com.tokopedia.autocomplete.initialstate.di.DaggerInitialStateComponent
import com.tokopedia.autocomplete.initialstate.di.InitialStateComponent
import com.tokopedia.autocomplete.initialstate.di.InitialStateContextModule
import com.tokopedia.autocomplete.util.getModifiedApplink
import com.tokopedia.discovery.common.model.SearchParameter
import kotlinx.android.synthetic.main.fragment_initial_state.*
import javax.inject.Inject

class InitialStateFragment : BaseDaggerFragment(), InitialStateContract.View, InitialStateItemClickListener {

    private val SEARCH_PARAMETER = "SEARCH_PARAMETER"
    private val MP_SEARCH_AUTOCOMPLETE = "mp_search_autocomplete"

    @Inject
    lateinit var presenter: InitialStatePresenter

    private lateinit var networkErrorMessage: String

    private var performanceMonitoring: PerformanceMonitoring? = null

    private lateinit var adapter: InitialStateAdapter

    private var initialStateViewUpdateListener: InitialStateViewUpdateListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initResources()
        retainInstance = true
    }

    private fun initResources() {
        networkErrorMessage = getString(com.tokopedia.abstraction.R.string.msg_network_error)
    }

    override fun initInjector() {
        val component: InitialStateComponent = DaggerInitialStateComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .initialStateContextModule(activity?.let { InitialStateContextModule(it) })
                .build()
        component.inject(this)
        component.inject(presenter)
    }

    private fun getBaseAppComponent(): BaseAppComponent? {
        return if (activity == null || activity?.application == null) null else
            (activity?.application as BaseMainApplication).baseAppComponent

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_initial_state, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepareView(view)
        presenter.attachView(this)
    }

    private fun prepareView(view: View) {
        val typeFactory = InitialStateAdapterTypeFactory(this)
        val layoutManager = LinearLayoutManager(view.context,
                LinearLayoutManager.VERTICAL, false)
        adapter = InitialStateAdapter(typeFactory)
        recyclerViewInitialState?.adapter = adapter
        recyclerViewInitialState?.layoutManager = layoutManager
        recyclerViewInitialState?.addOnScrollListener(OnScrollListenerAutocomplete(view.context, view))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun getScreenName(): String {
        return AppScreen.SCREEN_UNIVERSEARCH
    }

    override fun showInitialStateResult(initialStateVisitableList: List<Visitable<*>>) {
        notifyAdapter(initialStateVisitableList)
    }

    private fun notifyAdapter(list: List<Visitable<*>>) {
        stopTracePerformanceMonitoring()
        adapter.clearData()
        adapter.addAll(list)

        initialStateViewUpdateListener?.showInitialStateView()
    }

    private fun stopTracePerformanceMonitoring() {
        performanceMonitoring?.stopTrace()
    }

    override fun refreshPopularSearch(list: List<Visitable<*>>) {
        notifyAdapter(list)
    }

    override fun deleteRecentSearch(list: List<Visitable<*>>) {
        notifyAdapter(list)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (savedInstanceState != null) {
            val searchParameter = savedInstanceState.getSerializable(SEARCH_PARAMETER) as HashMap<String, String>
            setSearchParameter(searchParameter)
            presenter.getInitialStateData()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SEARCH_PARAMETER, HashMap<String, Any>(presenter.getSearchParameter()))
    }

    fun getInitialStateData(searchParameter: HashMap<String, String>) {
        performanceMonitoring = PerformanceMonitoring.start(MP_SEARCH_AUTOCOMPLETE)
        setSearchParameter(searchParameter)
        presenter.getInitialStateData()
    }

    override fun onItemClicked(applink: String, webUrl: String) {
        dropKeyBoard()

        val modifiedApplink = getModifiedApplink(applink, presenter.getSearchParameter())
        startActivityFromAutoComplete(modifiedApplink)
    }

    private fun dropKeyBoard() {
        if (activity != null && activity is AutoCompleteActivity) {
            (activity as AutoCompleteActivity).dropKeyboard()
        }
    }

    private fun startActivityFromAutoComplete(applink: String) {
        if (activity == null) return

        RouteManager.route(activity, applink)
        activity?.finish()
    }

    override fun onDeleteRecentSearchItem(keyword: String) {
        deleteRecentSearch(keyword)
    }

    private fun deleteRecentSearch(keyword: String) {
        presenter.deleteRecentSearchItem(keyword)
    }

    override fun onDeleteAllRecentSearch() {
        deleteAllRecentSearch()
    }

    private fun deleteAllRecentSearch() {
        presenter.deleteAllRecentSearch()
    }

    override fun onRefreshPopularSearch() {
        refreshPopularSearch()
    }

    private fun refreshPopularSearch() {
        AutocompleteTracking.eventClickRefreshPopularSearch()
        presenter.refreshPopularSearch()
    }

    fun setSearchParameter(searchParameter: HashMap<String, String> ) {
        presenter.setSearchParameter(searchParameter)
    }

    fun setInitialStateViewUpdateListener(initialStateViewUpdateListener: InitialStateViewUpdateListener) {
        this.initialStateViewUpdateListener = initialStateViewUpdateListener
    }
}
