package com.tokopedia.analyticsdebugger.serverlogger.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.databinding.FragmentServerLoggerBinding
import com.tokopedia.analyticsdebugger.serverlogger.di.component.DaggerServerLoggerComponent
import com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.ServerLoggerAdapter
import com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.ServerLoggerAdapterTypeFactory
import com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.ServerLoggerListener
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.BaseServerLoggerUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ServerLoggerPriorityUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ItemServerLoggerUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.viewmodel.ServerLoggerViewModel
import com.tokopedia.analyticsdebugger.serverlogger.utils.ServerLoggerConstants
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class ServerLoggerFragment : BaseListFragment<Visitable<*>, ServerLoggerAdapterTypeFactory>(),
    ServerLoggerListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoClearedNullable<FragmentServerLoggerBinding>()

    private lateinit var viewModel: ServerLoggerViewModel

    private val serverLoggerAdapterTypeFactory by lazy(LazyThreadSafetyMode.NONE) {
        ServerLoggerAdapterTypeFactory(this)
    }

    private val serverLoggerAdapter by lazy { ServerLoggerAdapter(serverLoggerAdapterTypeFactory) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ServerLoggerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServerLoggerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchBarView()
        observeBaseServerLogger()
        observeServerLoggerList()
        observeMessageEvent()
        observeDeleteServerLogger()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_server_logger, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_menu_delete_server_logger) {
            viewModel.deleteAllServerLogger()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun loadData(page: Int) {
        viewModel.loadServerLogger(getSearchbarText(), getChipsSelectedName(), page)
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, ServerLoggerAdapterTypeFactory> {
        return serverLoggerAdapter
    }

    override fun getAdapterTypeFactory(): ServerLoggerAdapterTypeFactory {
        return serverLoggerAdapterTypeFactory
    }

    override fun getRecyclerView(view: View): RecyclerView? {
        return view.findViewById(R.id.rv_server_logger)
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return view.findViewById(R.id.swipe_refresh_sl)
    }

    override fun loadInitialData() {
        swipeToRefresh?.isRefreshing = false
        binding?.tvSlNotFound?.hide()
        serverLoggerAdapter.removeServerLoggerList()
        setLoading(true)
        endlessRecyclerViewScrollListener.resetState()
        viewModel.loadInitialData(getSearchbarText(), getChipsSelectedName())
    }

    override fun onSwipeRefresh() {
        loadInitialData()
    }

    override fun getScreenName(): String = TAG

    override fun initInjector() {
        activity?.let {
            val appComponent = (it.application as BaseMainApplication).baseAppComponent
            DaggerServerLoggerComponent.builder()
                .baseAppComponent(appComponent)
                .build()
                .inject(this)
        }
    }

    override fun onChipsClicked(position: Int, chipsName: String) {
        serverLoggerAdapter.updateChipsSelected(position)
        serverLoggerAdapter.removeServerLoggerList()
        endlessRecyclerViewScrollListener.resetState()
        setLoading(true)
        viewModel.loadServerLogger(
            getSearchbarText(),
            getChipsSelectedName(),
            ServerLoggerConstants.FIRST_PAGE
        )
    }

    override fun onItemClicked(item: ItemServerLoggerUiModel) {
        val bundle = bundleOf(
            ServerLoggerDetailFragment.EXTRA_TAG to item.tag,
            ServerLoggerDetailFragment.EXTRA_PRIORITY to item.priority,
            ServerLoggerDetailFragment.EXTRA_MESSAGE to item.message,
            ServerLoggerDetailFragment.EXTRA_DATE_TIME to item.dateTime
        )
        bundle.putStringArray(
            ServerLoggerDetailFragment.EXTRA_SERVER_CHANNEL,
            item.serverChannel.toTypedArray()
        )
        findNavController().navigate(
            R.id.action_serverLoggerFragment_to_serverLoggerDetailFragment,
            bundle
        )
    }

    override fun onItemClicked(t: Visitable<*>?) {
        //no op
    }

    private fun initSearchBarView() {
        binding?.searchbarSl?.searchBarTextField?.run {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    serverLoggerAdapter.removeServerLoggerList()
                    endlessRecyclerViewScrollListener.resetState()
                    setLoading(true)
                    viewModel.loadServerLogger(
                        getSearchbarText(),
                        getChipsSelectedName(),
                        ServerLoggerConstants.FIRST_PAGE
                    )
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }
        binding?.searchbarSl?.clearListener = {
            loadInitialData()
        }
    }

    private fun getSearchbarText(): String {
        return binding?.searchbarSl?.searchBarTextField?.text?.toString().orEmpty()
    }

    private fun getChipsSelectedName(): String {
        return serverLoggerAdapter.data.filterIsInstance<ServerLoggerPriorityUiModel>()
            .firstOrNull()
            ?.priority
            ?.find { it.isSelected }
            ?.priorityName.orEmpty()
    }

    private fun observeBaseServerLogger() {
        observe(viewModel.baseServerLogger) {
            setLoading(false)
            when (it) {
                is Success -> {
                    setBaseServerLoggerList(it.data)
                }
                else -> {}
            }
        }
    }

    private fun observeServerLoggerList() {
        observe(viewModel.itemServerLoggerUiList) {
            setLoading(false)
            when (it) {
                is Success -> {
                    setServerLoggerList(it.data)
                }
                else -> {}
            }
        }
    }

    private fun setLoading(loading: Boolean) = if (loading) showLoading() else hideLoading()

    private fun observeMessageEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.messageEvent.collectLatest {
                showToaster(it)
            }
        }
    }

    private fun observeDeleteServerLogger() {
        observe(viewModel.deleteServerLogger) {
            when (it) {
                is Success -> if (it.data) {
                    showToaster(getString(R.string.serverlogger_delete_all_message))
                    loadInitialData()
                }
                else -> {}
            }
        }
    }

    private fun showToaster(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setBaseServerLoggerList(items: List<BaseServerLoggerUiModel>) {
        val serverLoggerList = items.filterIsInstance<ItemServerLoggerUiModel>()
        if (serverLoggerList.isEmpty()) {
            binding?.tvSlNotFound?.show()
        } else {
            binding?.tvSlNotFound?.hide()
            serverLoggerAdapter.setServerLoggerData(items)
        }
    }

    private fun setServerLoggerList(items: List<ItemServerLoggerUiModel>) {
        val hasNext = ServerLoggerConstants.LIMIT == items.size
        val serverLoggerList = serverLoggerAdapter.list.filterIsInstance<ItemServerLoggerUiModel>()
        if (items.isEmpty() && serverLoggerList.isEmpty()
        ) {
            binding?.tvSlNotFound?.show()
        } else {
            binding?.tvSlNotFound?.hide()
            serverLoggerAdapter.addServerLoggerListData(items)
        }
        updateScrollListenerState(hasNext)
    }

    companion object {
        val TAG: String = ServerLoggerFragment::class.java.simpleName

        fun newInstance(): ServerLoggerFragment {
            return ServerLoggerFragment()
        }
    }
}