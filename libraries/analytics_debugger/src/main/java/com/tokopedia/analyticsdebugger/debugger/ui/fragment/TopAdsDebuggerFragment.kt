package com.tokopedia.analytics.debugger.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.di.AnalyticsDebuggerComponent
import com.tokopedia.analyticsdebugger.debugger.di.DaggerAnalyticsDebuggerComponent
import com.tokopedia.analyticsdebugger.debugger.ui.activity.TopAdsDebuggerDetailActivity
import com.tokopedia.analyticsdebugger.debugger.ui.adapter.TopAdsDebuggerTypeFactory
import com.tokopedia.analyticsdebugger.debugger.ui.model.TopAdsDebuggerViewModel
import com.tokopedia.analyticsdebugger.debugger.ui.presenter.TopAdsDebugger

class TopAdsDebuggerFragment : BaseSearchListFragment<Visitable<*>, TopAdsDebuggerTypeFactory>(), TopAdsDebugger.View {

    private var buttonSearch: Button? = null

    var presenter: TopAdsDebugger.Presenter? = null

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
        buttonSearch!!.setOnClickListener { v ->
            if (TextUtils.isEmpty(searchInputView.searchText)) {
                presenter?.reloadData()
            } else {
                presenter?.search(searchInputView.searchText)
            }
        }
        searchInputView.setSearchHint("Cari")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachView()
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return true
    }

    override fun loadInitialData() {
        showLoading()
        presenter?.reloadData()
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return view.findViewById(com.tokopedia.baselist.R.id.swipe_refresh_layout)
    }

    override fun loadData(page: Int) {
        presenter?.loadMore()
    }

    override fun getAdapterTypeFactory(): TopAdsDebuggerTypeFactory {
        return TopAdsDebuggerTypeFactory()
    }

    override fun onItemClicked(visitable: Visitable<*>) {
        if (visitable is TopAdsDebuggerViewModel) {
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
        presenter?.attachView(this)
    }

    private fun injectToFragment(component: AnalyticsDebuggerComponent) {
        presenter = component.topAdsPresenter
    }

    override fun getScreenName(): String {
        return TopAdsDebuggerFragment::class.java.simpleName
    }

    override fun onSearchSubmitted(text: String) {
        presenter?.search(text)
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
        presenter?.reloadData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_topads_debugger, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.topads_action_menu_delete) {
            presenter?.deleteAll()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getViewContext(): Context {
        return requireContext()
    }
    private fun openDetail(viewModel: TopAdsDebuggerViewModel) {
        startActivity(TopAdsDebuggerDetailActivity.newInstance(requireContext(), viewModel))
    }

    companion object {

        @JvmStatic
        val TAG = TopAdsDebuggerFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): Fragment {
            return TopAdsDebuggerFragment()
        }
    }
}
