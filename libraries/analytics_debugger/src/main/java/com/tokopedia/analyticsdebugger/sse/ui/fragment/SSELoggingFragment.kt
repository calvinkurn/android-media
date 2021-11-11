package com.tokopedia.analyticsdebugger.sse.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.hideLoading
import com.tokopedia.kotlin.extensions.view.show
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

    private lateinit var etSSELogSearch: EditText
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var rvSSELog: RecyclerView

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
        }
    }

    private fun initListener() {
        etSSELogSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                loadData()
            }
            true
        }

        swipeRefresh.setOnRefreshListener {
            loadData()
        }

        adapter.setOnClickListener {
            Toast.makeText(requireContext(), it.event, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadData() {
        val query = etSSELogSearch.text.toString()

        swipeRefresh.isRefreshing = true
        viewModel.getLog(query)
    }

    private fun hideKeyboard() {
        requireActivity().currentFocus?.let {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
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