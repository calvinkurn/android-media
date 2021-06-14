package com.tokopedia.analyticsdebugger.cassava.debugger

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.TextInputEditText
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.cassava.di.DaggerCassavaComponent
import com.tokopedia.analyticsdebugger.cassava.throttleFirst
import com.tokopedia.analyticsdebugger.cassava.validator.MainValidatorActivity
import com.tokopedia.analyticsdebugger.cassava.validator.Utils
import com.tokopedia.design.text.SearchInputView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject


@ExperimentalCoroutinesApi
class AnalyticsDebuggerFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: DebuggerListViewModel by lazy {
        ViewModelProvider(this, factory).get(DebuggerListViewModel::class.java)
    }

    private val listAdapter = DebuggerListAdapter().apply {
        setItemClickListener {
            val bundle = bundleOf(
                    "name" to it.name,
                    "timestamp" to Utils.getTimeStampFormat(it.timestamp),
                    "datum" to it.data)
            findNavController().navigate(R.id.action_analyticsDebuggerFragment_to_analyticsDebuggerDetailFragment, bundle)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        DaggerCassavaComponent.builder()
                .context(requireContext())
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_debugger_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val swipeLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: ""
        initSearch(query, view)
        initRecyclerView(view)
        swipeLayout.setOnRefreshListener {
            updateRepoListFromInput()
        }

        viewModel.logData.observe(viewLifecycleOwner, Observer {
            listAdapter.submitList(it)
            swipeLayout.isRefreshing = false
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_analytics_debugger, menu)
        super.onCreateOptionsMenu(menu, inflater)
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
        outState.putString(LAST_SEARCH_QUERY, view?.findViewById<TextInputEditText>(R.id.search_input_view)?.text?.trim().toString())
    }

    private fun initSearch(query: String, view: View) {
        with(view.findViewById<TextInputEditText>(R.id.search_input_view)) {
            setText(query)

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    updateRepoListFromInput()
                    true
                } else {
                    false
                }
            }
            setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    updateRepoListFromInput()
                    true
                } else {
                    false
                }
            }
        }

    }

    private fun initRecyclerView(view: View) {
        val rv = view.findViewById<RecyclerView>(R.id.recycler_view)
        with(rv) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = listAdapter
        }
        lifecycleScope.launchWhenStarted {
            callbackFlow<Int> {
                val lm = rv.layoutManager as LinearLayoutManager
                val cb = object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val totalItemCount = lm.itemCount
                        val lastVisibleItem = lm.findLastVisibleItemPosition()

                        if (dy > 0 && lastVisibleItem + VISIBLE_THRESHOLD >= totalItemCount) {
                            offer(lastVisibleItem)
                        }
                    }
                }
                rv.addOnScrollListener(cb)
                awaitClose { rv.clearOnScrollListeners() }
            }.throttleFirst(1000).collect {
                Timber.d("initRecyclerView: firing requetsmore")
                viewModel.listScrolled()
            }
        }
    }

    private fun updateRepoListFromInput() {
        view?.findViewById<TextInputEditText>(R.id.search_input_view)?.text?.trim()?.let {
            view?.findViewById<RecyclerView>(R.id.recycler_view)?.scrollToPosition(0)
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
