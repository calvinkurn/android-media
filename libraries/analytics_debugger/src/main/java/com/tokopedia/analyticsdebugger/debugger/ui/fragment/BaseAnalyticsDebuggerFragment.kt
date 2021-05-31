package com.tokopedia.analyticsdebugger.debugger.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.ui.activity.AnalyticsDebuggerDetailActivity
import com.tokopedia.analyticsdebugger.debugger.di.AnalyticsDebuggerComponent
import com.tokopedia.analyticsdebugger.debugger.di.DaggerAnalyticsDebuggerComponent
import com.tokopedia.analyticsdebugger.debugger.ui.AnalyticsDebugger
import com.tokopedia.analyticsdebugger.debugger.ui.adapter.AnalyticsDebuggerTypeFactory
import com.tokopedia.analyticsdebugger.debugger.ui.model.AnalyticsDebuggerViewModel

abstract class BaseAnalyticsDebuggerFragment : BaseSearchListFragment<Visitable<*>, AnalyticsDebuggerTypeFactory>(), AnalyticsDebugger.View {

    private var buttonSearch: Button? = null

    internal var presenter: AnalyticsDebugger.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_analytics_debugger, container, false)
        buttonSearch = view.findViewById(R.id.button_search)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonSearch!!.setOnClickListener { _ ->
            if (TextUtils.isEmpty(searchInputView.searchText)) {
                presenter!!.reloadData()
            } else {
                presenter!!.search(searchInputView.searchText)
            }
        }
        searchInputView.setSearchHint("Cari")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter!!.detachView()
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return true
    }

    override fun loadInitialData() {
        showLoading()
        presenter!!.reloadData()
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return view.findViewById(com.tokopedia.baselist.R.id.swipe_refresh_layout)
    }

    override fun loadData(page: Int) {
        presenter!!.loadMore()
    }

    override fun getAdapterTypeFactory(): AnalyticsDebuggerTypeFactory {
        return AnalyticsDebuggerTypeFactory()
    }

    override fun onItemClicked(visitable: Visitable<*>) {
        if (visitable is AnalyticsDebuggerViewModel) {
            openDetail(visitable)
        }
    }

    override fun initInjector() {
        val component = DaggerAnalyticsDebuggerComponent
                .builder()
                .baseAppComponent(
                        (activity!!.application as BaseMainApplication).baseAppComponent
                ).build()

        injectToFragment(component)
        presenter!!.attachView(this)
    }

    protected abstract fun injectToFragment(component: AnalyticsDebuggerComponent)

    override fun getScreenName(): String {
        return BaseAnalyticsDebuggerFragment::class.java.simpleName
    }

    override fun onSearchSubmitted(text: String) {
        presenter!!.search(text)
    }

    override fun onSearchTextChanged(text: String) {

    }

    override fun onLoadMoreCompleted(visitables: List<Visitable<*>>) {
        renderList(visitables, !visitables.isEmpty())
    }

    override fun onReloadCompleted(visitables: List<Visitable<*>>) {
        isLoadingInitialData = true
        renderList(visitables, !visitables.isEmpty())
    }

    override fun onSwipeRefresh() {
        searchInputView.searchTextView.setText("")
        super.onSwipeRefresh()
    }

    override fun onDeleteCompleted() {
        presenter!!.reloadData()
    }

    override fun showCount(count: Int) {
        //noop
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_analytics_debugger, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_menu_delete) {
            presenter!!.deleteAll()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDetail(viewModel: AnalyticsDebuggerViewModel) {
        startActivity(AnalyticsDebuggerDetailActivity.newInstance(requireContext(), viewModel))
    }

    companion object {
        val TAG = BaseAnalyticsDebuggerFragment::class.java.simpleName
    }
}
