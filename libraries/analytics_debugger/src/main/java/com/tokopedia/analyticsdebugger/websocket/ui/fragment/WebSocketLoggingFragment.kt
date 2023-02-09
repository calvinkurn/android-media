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
import com.tokopedia.analyticsdebugger.databinding.FragmentWebsocketLoggingBinding
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
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 03, 2021
 */
class WebSocketLoggingFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val binding: FragmentWebsocketLoggingBinding? by viewBinding()

    private val adapter: WebSocketLogAdapter by lazy(LazyThreadSafetyMode.NONE) {
        WebSocketLogAdapter()
    }

    private val viewModel: WebSocketLoggingViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[WebSocketLoggingViewModel::class.java]
    }

    private var layoutManager: LinearLayoutManager? = null

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0 && layoutManager != null) {
                val totalItem = layoutManager!!.itemCount
                val visibleItem = layoutManager!!.childCount
                val pastVisibleItem = layoutManager!!.findFirstVisibleItemPosition()

                if ((visibleItem + pastVisibleItem) >= totalItem) {
                    viewModel.submitAction(WebSocketLoggingAction.LoadNextPageAction)
                }
            }
        }
    }

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
        initView()
        initObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.rvWebsocketLog?.removeOnScrollListener(scrollListener)
    }

    /**
     * Initialization
     */
    private fun initView() {
        layoutManager = LinearLayoutManager(context)

        binding?.rvWebsocketLog?.apply {
            layoutManager = this@WebSocketLoggingFragment.layoutManager
            adapter = this@WebSocketLoggingFragment.adapter
        }

        binding?.etWebsocketLogSearch?.setListener(object : SearchInputView.Listener {
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
                when (it) {
                    is WebSocketLoggingEvent.ShowInfoEvent -> {
                        when (it.uiString) {
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

        binding?.rvWebsocketLog?.addOnScrollListener(scrollListener)

        binding?.swipeRefreshWebSocketLog?.setOnRefreshListener {
            val action = binding?.etWebsocketLogSearch?.searchText.toString()
            viewModel.submitAction(WebSocketLoggingAction.SearchLogAction(action))

            binding?.swipeRefreshWebSocketLog?.isRefreshing = false
        }

        adapter.setOnClickListener {
            val direction = WebSocketLoggingFragmentDirections
                .actionWebSocketLoggingFragmentToWebSocketDetailLoggingFragment(it)

            findNavController().navigate(direction)
        }

        binding?.chipGroupWebsocketLog?.setOnCheckedListener(object : ChipGroup.Listener {
            override fun onChecked(model: ChipModel) {
                val action = binding?.etWebsocketLogSearch?.searchText.toString()
                viewModel.submitAction(WebSocketLoggingAction.SelectSource(model.value, action))
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
        if (loading) binding?.pbLoading?.visible()
        else binding?.pbLoading?.hide()
    }

    private fun updateChip(chips: List<ChipModel>) {
        binding?.chipGroupWebsocketLog?.setChips(chips)
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
