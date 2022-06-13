package com.tokopedia.analyticsdebugger.sse.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.sse.di.DaggerSSELoggingComponent
import com.tokopedia.analyticsdebugger.sse.ui.adapter.SSELogAdapter
import com.tokopedia.analyticsdebugger.sse.ui.viewmodel.SSELoggingViewModel
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.fragment_sse_logging.*
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 09, 2021
 */
class SSELoggingFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: SSELoggingViewModel

    private val adapter: SSELogAdapter by lazy { SSELogAdapter() }

    private lateinit var etSSELogSearch: SearchInputView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var rvSSELog: RecyclerView
    private lateinit var tvNoData: Typography

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjection()
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory).get(SSELoggingViewModel::class.java)
        return inflater.inflate(R.layout.fragment_sse_logging, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        initObserver()
        initListener()

        loadData()
    }

    private fun initView(view: View) {
        etSSELogSearch = view.findViewById(R.id.et_sse_log_search)
        swipeRefresh = view.findViewById(R.id.swipe_refresh)
        rvSSELog = view.findViewById(R.id.rv_sse_log)
        tvNoData = view.findViewById(R.id.tv_sse_log_no_data)

        rvSSELog.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SSELoggingFragment.adapter
        }
    }

    private fun initInjection() {
        val component = DaggerSSELoggingComponent.builder().baseAppComponent(
            (requireActivity().application as BaseMainApplication).baseAppComponent
        ).build()
        component.inject(this)
    }

    private fun initObserver() {
        viewModel.observableSSELog.observe(viewLifecycleOwner) {
            swipeRefresh.isRefreshing = false
            adapter.updateData(it)

            if(it.isNullOrEmpty()) tvNoData.visible()
            else tvNoData.hide()
        }

        viewModel.observableError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initListener() {
        etSSELogSearch.setListener(object: SearchInputView.Listener {
            override fun onSearchSubmitted(text: String?) {
                loadData()
            }

            override fun onSearchTextChanged(text: String?) {}
        })

        swipeRefresh.setOnRefreshListener {
            loadData()
        }
    }

    private fun loadData() {
        val query = etSSELogSearch.searchText

        swipeRefresh.isRefreshing = true
        viewModel.getLog(query)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_sse_log, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_menu_delete) {
            viewModel.deleteAllLog()
            loadData()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        @JvmStatic
        val TAG = SSELoggingFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): Fragment {
            return SSELoggingFragment()
        }
    }
}