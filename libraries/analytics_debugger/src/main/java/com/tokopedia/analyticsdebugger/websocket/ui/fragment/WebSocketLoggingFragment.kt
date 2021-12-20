package com.tokopedia.analyticsdebugger.websocket.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.websocket.di.DaggerWebSocketLoggingComponent
import com.tokopedia.analyticsdebugger.websocket.ui.adapter.WebSocketLogAdapter
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.action.WebSocketLoggingAction
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.event.WebSocketLoggingEvent
import com.tokopedia.analyticsdebugger.websocket.ui.viewmodel.WebSocketLoggingViewModel
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 03, 2021
 */
class WebSocketLoggingFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: WebSocketLoggingViewModel
    private val adapter: WebSocketLogAdapter by lazy(LazyThreadSafetyMode.NONE) { WebSocketLogAdapter() }
    private val layoutManager = LinearLayoutManager(context)
    private val scrollListener = object: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if(dy > 0) {
                val totalItem = layoutManager.itemCount
                val visibleItem = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()

                if((visibleItem + pastVisibleItem) >= totalItem) {
                    viewModel.submitAction(WebSocketLoggingAction.LoadNextPageAction)
                }
            }
        }
    }

    /**
     * View Attribute
     */
    private lateinit var rvWebsocketLog: RecyclerView
    private lateinit var etSearchWebSocketLog: SearchInputView
    private lateinit var tvNoData: Typography
    private lateinit var pbLoading: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout

    /**
     * Lifecycle
     */
    override fun onAttach(context: Context) {
        inject()
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory).get(WebSocketLoggingViewModel::class.java)
        return inflater.inflate(R.layout.fragment_websocket_logging, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        initObserver()

        viewModel.submitAction(WebSocketLoggingAction.SearchLogAction(""))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rvWebsocketLog.removeOnScrollListener(scrollListener)
    }

    /**
     * Initialization
     */
    private fun initView(view: View) {
        rvWebsocketLog = view.findViewById(R.id.rv_websocket_log)
        etSearchWebSocketLog = view.findViewById(R.id.et_websocket_log_search)
        tvNoData = view.findViewById(R.id.tv_websocket_log_no_data)
        pbLoading = view.findViewById(R.id.pb_loading)
        swipeRefresh = view.findViewById(R.id.swipe_refresh_web_socket_log)

        rvWebsocketLog.apply {
            layoutManager = this@WebSocketLoggingFragment.layoutManager
            adapter = this@WebSocketLoggingFragment.adapter
        }

        etSearchWebSocketLog.setListener(object : SearchInputView.Listener {
            override fun onSearchSubmitted(text: String?) {
                viewModel.submitAction(WebSocketLoggingAction.SearchLogAction(text.toString()))
            }

            override fun onSearchTextChanged(text: String?) {}
        })
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest {
                updateList(it.webSocketLogPagination.webSocketLoggingList)
                updateLoading(it.loading)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect {
                when(it) {
                    is WebSocketLoggingEvent.ShowInfoEvent -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        rvWebsocketLog.addOnScrollListener(scrollListener)
    }

    /**
     * Update UI State
     */
    private fun updateList(webSocketLogList: List<WebSocketLogUiModel>) {
        adapter.submitList(webSocketLogList)
    }

    private fun updateLoading(loading: Boolean) {
        if(loading) pbLoading.visible()
        else pbLoading.hide()
    }

    /**
     * Helper
     */
    private fun inject() {
        DaggerWebSocketLoggingComponent.builder()
            .baseAppComponent(
                (requireActivity().application as BaseMainApplication).baseAppComponent
            )
            .build()
            .inject(this)
    }
}