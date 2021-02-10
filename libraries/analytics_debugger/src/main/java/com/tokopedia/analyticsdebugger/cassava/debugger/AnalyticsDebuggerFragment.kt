package com.tokopedia.analyticsdebugger.cassava.debugger

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.fetchLogs()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_debugger_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_view)
        swipeLayout = view.findViewById(R.id.swipe_refresh_layout)
        searchView = view.findViewById(R.id.search_input_view)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = listAdapter
        }
        swipeLayout.setOnRefreshListener {
            viewModel.fetchLogs()
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
//            presenter!!.deleteAll()
            return true
        } else if (item.itemId == R.id.action_menu_cav) {
            goToValidator()
            return true
        } else return super.onOptionsItemSelected(item)
    }

    private fun goToValidator() {
        context?.let {
            val intent = MainValidatorActivity.newInstance(it)
            startActivity(intent)
        }
    }

    companion object {
        val TAG = AnalyticsDebuggerFragment::class.java.canonicalName
        fun newInstance(): Fragment {
            return AnalyticsDebuggerFragment()
        }
    }

}
