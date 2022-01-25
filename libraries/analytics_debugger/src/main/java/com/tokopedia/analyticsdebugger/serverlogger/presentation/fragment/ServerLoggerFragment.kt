package com.tokopedia.analyticsdebugger.serverlogger.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.databinding.FragmentServerLoggerBinding
import com.tokopedia.analyticsdebugger.serverlogger.di.component.DaggerServerLoggerComponent
import com.tokopedia.analyticsdebugger.serverlogger.di.component.ServerLoggerComponent
import com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.ServerLoggerAdapter
import com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.ServerLoggerAdapterTypeFactory
import com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.ServerLoggerListener
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ServerLoggerPriorityUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.viewmodel.ServerLoggerViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ServerLoggerFragment : BaseListFragment<Visitable<*>, ServerLoggerAdapterTypeFactory>(),
    ServerLoggerListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoClearedNullable<FragmentServerLoggerBinding>()

    private val viewModel: ServerLoggerViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ServerLoggerViewModel::class.java)
    }

    private val serverLoggerAdapterTypeFactory by lazy(LazyThreadSafetyMode.NONE) {
        ServerLoggerAdapterTypeFactory(this)
    }

    private val serverLoggerAdapter by lazy { ServerLoggerAdapter(serverLoggerAdapterTypeFactory) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
    }

    override fun loadInitialData() {
        super.loadInitialData()
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
        val chipsNameSelected = getChipsSelectedName()
    }

    override fun onItemClicked(t: Visitable<*>?) {
        //no op
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

    companion object {
        val TAG = ServerLoggerFragment::class.java.simpleName

        fun newInstance(): ServerLoggerFragment {
            return ServerLoggerFragment()
        }
    }
}