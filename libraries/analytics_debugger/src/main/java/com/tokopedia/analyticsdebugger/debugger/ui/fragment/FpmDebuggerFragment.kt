package com.tokopedia.analytics.debugger.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.di.AnalyticsDebuggerComponent
import com.tokopedia.analyticsdebugger.debugger.di.DaggerAnalyticsDebuggerComponent
import com.tokopedia.analyticsdebugger.debugger.ui.activity.FpmDebuggerDetailActivity
import com.tokopedia.analyticsdebugger.debugger.ui.adapter.FpmDebuggerTypeFactory

class FpmDebuggerFragment : BaseSearchListFragment<Visitable<*>, FpmDebuggerTypeFactory>(), com.tokopedia.analyticsdebugger.debugger.ui.presenter.FpmDebugger.View {

    private var buttonSearch: Button? = null

    var presenter: com.tokopedia.analyticsdebugger.debugger.ui.presenter.FpmDebugger.Presenter? = null

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

    override fun getAdapterTypeFactory(): FpmDebuggerTypeFactory {
        return FpmDebuggerTypeFactory()
    }

    override fun onItemClicked(visitable: Visitable<*>) {
        if (visitable is com.tokopedia.analyticsdebugger.debugger.ui.model.FpmDebuggerViewModel) {
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
        presenter = component.fpmPresenter
    }

    override fun getScreenName(): String {
        return FpmDebuggerFragment::class.java.simpleName
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
        inflater.inflate(R.menu.menu_fpm_debugger, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.fpm_action_menu_delete) {
            presenter?.deleteAll()
            return true
        } else if (item.itemId == R.id.fpm_action_menu_save) {
            showSaveToDiskDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSaveToDiskDialog() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return

        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_TITLE, "${DEFAULT_FILE_PREFIX}${System.currentTimeMillis()}")
            type = "*/*"
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, GET_FILE_FOR_SAVING_REQUEST_CODE)
        } else {
            Toast.makeText(
                    requireContext(), R.string.fpm_save_failed_to_open_document,
                    Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == GET_FILE_FOR_SAVING_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri = resultData?.data
            uri?.let { it ->
                presenter?.writeAllDataToFile(uri)
            }
        }
    }

    override fun getViewContext(): Context {
        return requireContext()
    }
    private fun openDetail(viewModel: com.tokopedia.analyticsdebugger.debugger.ui.model.FpmDebuggerViewModel) {
        startActivity(FpmDebuggerDetailActivity.newInstance(requireContext(), viewModel))
    }

    companion object {

        const val GET_FILE_FOR_SAVING_REQUEST_CODE: Int = 43
        const val DEFAULT_FILE_PREFIX = "fpm-export-"

        @JvmStatic
        val TAG = FpmDebuggerFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): Fragment {
            return FpmDebuggerFragment()
        }
    }
}
