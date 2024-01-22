package com.tokopedia.analytics.debugger.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.di.DaggerAnalyticsDebuggerComponent
import com.tokopedia.analyticsdebugger.debugger.ui.activity.ApplinkDebuggerDetailActivity
import com.tokopedia.analyticsdebugger.debugger.ui.adapter.ApplinkDebuggerTypeFactory
import com.tokopedia.analyticsdebugger.debugger.ui.presenter.ApplinkDebugger
import javax.inject.Inject

class ApplinkDebuggerFragment : BaseSearchListFragment<Visitable<*>, ApplinkDebuggerTypeFactory>(), com.tokopedia.analyticsdebugger.debugger.ui.presenter.ApplinkDebugger.View {

    private var buttonSearch: Button? = null

    @Inject
    lateinit var presenter: ApplinkDebugger.Presenter

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
            if (TextUtils.isEmpty(searchText)) {
                presenter?.reloadData()
            } else {
                presenter?.search(searchText)
            }
        }
        searchInputView.searchBarPlaceholder = "Cari"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return true
    }

    override fun loadInitialData() {
        showLoading()
        presenter.reloadData()
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return view.findViewById(com.tokopedia.baselist.R.id.swipe_refresh_layout)
    }

    override fun loadData(page: Int) {
        presenter.loadMore()
    }

    override fun getAdapterTypeFactory(): ApplinkDebuggerTypeFactory {
        return ApplinkDebuggerTypeFactory()
    }

    override fun onItemClicked(visitable: Visitable<*>) {
        if (visitable is com.tokopedia.analyticsdebugger.debugger.ui.model.ApplinkDebuggerViewModel) {
            openDetail(visitable)
        }
    }

    override fun initInjector() {
        val component = DaggerAnalyticsDebuggerComponent.builder()
            .context(activity!!.application).build()
        component.inject(this)
        presenter.attachView(this)
    }

    override fun getScreenName(): String {
        return ApplinkDebuggerFragment::class.java.simpleName
    }

    override fun onSearchSubmitted(text: String) {
        presenter.search(text)
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
        searchInputView.searchBarTextField.setText("")
        super.onSwipeRefresh()
    }

    override fun onDeleteCompleted() {
        presenter.reloadData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_applink_debugger, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.applink_action_menu_delete) {
            presenter.deleteAll()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDetail(viewModel: com.tokopedia.analyticsdebugger.debugger.ui.model.ApplinkDebuggerViewModel) {
        startActivity(ApplinkDebuggerDetailActivity.newInstance(requireContext(), viewModel))
    }

    companion object {

        @JvmStatic
        val TAG = ApplinkDebuggerFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): Fragment {
            return ApplinkDebuggerFragment()
        }
    }
}
