package com.tokopedia.analyticsdebugger.cassava.debugger

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.TextInputEditText
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.cassava.injector.DebuggerViewModelFactory
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.validator.MainValidatorActivity


class AnalyticsDebuggerFragment : Fragment() {

    private val viewModel: DebuggerListViewModel by lazy {
        val factory = DebuggerViewModelFactory(TkpdAnalyticsDatabase.getInstance(requireContext()).gtmLogDao())
        ViewModelProvider(this, factory).get(DebuggerListViewModel::class.java)
    }

    private val listAdapter = DebuggerListAdapter()
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var searchView: TextInputEditText
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_debugger_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_view)
        swipeLayout = view.findViewById(R.id.swipe_refresh_layout)
        searchView = view.findViewById(R.id.search_input_view)
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: ""
        viewModel.searchLogs(query)
        initSearch(query)
        with(recyclerView) {
            val lm = layoutManager as LinearLayoutManager
            setHasFixedSize(true)
            adapter = listAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = lm.itemCount
                    val lastVisibleItem = lm.findLastVisibleItemPosition()

                    if (dy > 0 && !isLoading &&
                            lastVisibleItem + VISIBLE_THRESHOLD >= totalItemCount) {
                        isLoading = true
                        viewModel.listScrolled()
                    }
                }
            })
        }
        swipeLayout.setOnRefreshListener {
            updateRepoListFromInput()
        }

        viewModel.logData.observe(viewLifecycleOwner, Observer {
            listAdapter.submitList(it)
            isLoading = false
            swipeLayout.isRefreshing = false
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_analytics_debugger, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerView.clearOnScrollListeners()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_menu_delete) {
            viewModel.delete()
            viewModel.searchLogs("")
            return true
        } else if (item.itemId == R.id.action_menu_cav) {
            goToValidator()
            return true
        } else return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, searchView.text?.trim().toString())
    }

    private fun initSearch(query: String) {
        searchView.setText(query)

        searchView.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
        searchView.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updateRepoListFromInput() {
        searchView.text?.trim()?.let {
            recyclerView.scrollToPosition(0)
            viewModel.searchLogs(it.toString())
        }
    }

    private fun goToValidator() {
        context?.let {
            val intent = MainValidatorActivity.newInstance(it)
            startActivity(intent)
        }
    }

    companion object {
        val TAG = AnalyticsDebuggerFragment::class.java.canonicalName
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val VISIBLE_THRESHOLD = 5

        fun newInstance(): Fragment {
            return AnalyticsDebuggerFragment()
        }
    }

}
