package com.tokopedia.analyticsdebugger.websocket.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.websocket.di.DaggerWebSocketLoggingComponent
import com.tokopedia.analyticsdebugger.websocket.ui.activity.WebSocketLoggingActivity
import com.tokopedia.analyticsdebugger.websocket.ui.adapter.WebSocketLogAdapter
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogPageSource
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLog
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.action.WebSocketLoggingAction
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.event.WebSocketLoggingEvent
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.helper.UiString
import com.tokopedia.analyticsdebugger.websocket.ui.view.ChipGroup
import com.tokopedia.analyticsdebugger.websocket.ui.view.ChipModel
import com.tokopedia.analyticsdebugger.websocket.ui.viewmodel.WebSocketLoggingViewModel
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
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
    private lateinit var layoutManager: LinearLayoutManager

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
    private lateinit var pbLoading: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var chipGroup: ChipGroup

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

        val pageSource = requireArguments()
            .getString(WebSocketLoggingActivity.EXTRA_PAGE_SOURCE)
            .orEmpty()

        viewModel = ViewModelProvider(this, viewModelFactory)[WebSocketLoggingViewModel::class.java]
        viewModel.setPageSource(WebSocketLogPageSource.fromString(pageSource))
        viewModel.submitAction(WebSocketLoggingAction.InitPage)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_websocket_logging, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        initObserver()
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
        pbLoading = view.findViewById(R.id.pb_loading)
        swipeRefresh = view.findViewById(R.id.swipe_refresh_web_socket_log)
        chipGroup = view.findViewById(R.id.chip_group_websocket_log)

        layoutManager = LinearLayoutManager(context)

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
                updateChip(it.chips)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect {
                when(it) {
                    is WebSocketLoggingEvent.ShowInfoEvent -> {
                        when(it.uiString) {
                            is UiString.Resource -> {
                                Toast.makeText(requireContext(), getString(it.uiString.idRes), Toast.LENGTH_SHORT).show()
                            }
                            is UiString.Text -> {
                                Toast.makeText(requireContext(), it.uiString.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    WebSocketLoggingEvent.DeleteAllLogEvent -> {
                        viewModel.submitAction(WebSocketLoggingAction.SearchLogAction(""))
                    }
                }
            }
        }

        rvWebsocketLog.addOnScrollListener(scrollListener)

        swipeRefresh.setOnRefreshListener {
            viewModel.submitAction(WebSocketLoggingAction.SearchLogAction(etSearchWebSocketLog.searchText))
            swipeRefresh.isRefreshing = false
        }

        adapter.setOnClickListener {
            val bundle = bundleOf(
                WebSocketDetailLoggingFragment.EXTRA_TITLE to it.event,
                WebSocketDetailLoggingFragment.EXTRA_DATE_TIME to it.dateTime,
                WebSocketDetailLoggingFragment.EXTRA_CHANNEL_ID to it.playGeneralInfo?.channelId,
                WebSocketDetailLoggingFragment.EXTRA_GC_TOKEN to it.playGeneralInfo?.gcToken,
                WebSocketDetailLoggingFragment.EXTRA_MESSAGE to it.message,
                WebSocketDetailLoggingFragment.EXTRA_WAREHOUSE_ID to it.playGeneralInfo?.warehouseId
            )
            findNavController().navigate(R.id.action_webSocketLoggingFragment_to_webSocketDetailLoggingFragment, bundle)
        }

        chipGroup.setOnCheckedListener(object: ChipGroup.Listener {
            override fun onChecked(model: ChipModel) {
                viewModel.submitAction(WebSocketLoggingAction.SelectSource(model.value, etSearchWebSocketLog.searchText))
            }
        })
    }

    /**
     * Update UI State
     */
    private fun updateList(webSocketLogList: List<WebSocketLog>) {
        adapter.submitList(webSocketLogList)
    }

    private fun updateLoading(loading: Boolean) {
        if(loading) pbLoading.visible()
        else pbLoading.hide()
    }

    private fun updateChip(chips: List<ChipModel>) {
        chipGroup.setChips(chips)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_web_socket_log, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_menu_delete_web_socket) {
            viewModel.submitAction(WebSocketLoggingAction.DeleteAllLogAction)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
