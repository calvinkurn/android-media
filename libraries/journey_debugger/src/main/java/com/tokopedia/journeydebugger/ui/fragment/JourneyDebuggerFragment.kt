package com.tokopedia.journeydebugger.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.journeydebugger.R
import com.tokopedia.journeydebugger.di.JourneyDebuggerComponent
import com.tokopedia.journeydebugger.di.DaggerJourneyDebuggerComponent
import com.tokopedia.journeydebugger.ui.activity.JourneyDebuggerDetailActivity
import com.tokopedia.journeydebugger.ui.adapter.JourneyDebuggerTypeFactory
import com.tokopedia.journeydebugger.ui.model.JourneyDebuggerUIModel
import com.tokopedia.journeydebugger.ui.presenter.JourneyDebugger

class JourneyDebuggerFragment : BaseSearchListFragment<Visitable<*>, JourneyDebuggerTypeFactory>(), JourneyDebugger.View {

    private var buttonSearch: Button? = null

    var presenter: JourneyDebugger.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_journey_debugger, container, false)
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

    override fun getAdapterTypeFactory(): JourneyDebuggerTypeFactory {
        return JourneyDebuggerTypeFactory()
    }

    override fun onItemClicked(visitable: Visitable<*>) {
        if (visitable is JourneyDebuggerUIModel) {
            openDetail(visitable)
        }
    }

    override fun initInjector() {
        val component = DaggerJourneyDebuggerComponent
                .builder()
                .baseAppComponent(
                        (requireActivity().application as BaseMainApplication).baseAppComponent
                ).build()

        injectToFragment(component)
        presenter?.attachView(this)
    }

    private fun injectToFragment(component: JourneyDebuggerComponent) {
        presenter = component.journeyPresenter
    }

    override fun getScreenName(): String {
        return JourneyDebuggerFragment::class.java.simpleName
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
        inflater.inflate(R.menu.menu_journey_debugger, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.journey_action_menu_delete) {
            presenter?.deleteAll()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDetail(viewModel: JourneyDebuggerUIModel) {
        startActivity(JourneyDebuggerDetailActivity.newInstance(requireContext(), viewModel))
    }

    companion object {

        @JvmStatic
        val TAG = JourneyDebuggerFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): Fragment {
            return JourneyDebuggerFragment()
        }
    }
}
