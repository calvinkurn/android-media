package com.tokopedia.broadcaster.log.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.broadcaster.log.di.DaggerNetworkLogComponent
import com.tokopedia.broadcaster.log.di.module.NetworkLogModule
import com.tokopedia.broadcaster.log.ui.adapter.ChuckerAdapter
import com.tokopedia.broadcaster.log.ui.adapter.StatsNerdItemListener
import com.tokopedia.broadcaster.log.ui.fragment.detail.NetworkLogDetailSheet
import com.tokopedia.broadcaster.data.uimodel.LoggerUIModel
import com.tokopedia.broadcaster.log.ui.viewmodel.NetworkLogViewModel
import com.tokopedia.broadcaster.databinding.FragmentChuckerBroadcasterBinding
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class NetworkLogFragment : BaseDaggerFragment(), StatsNerdItemListener {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by viewBinding<FragmentChuckerBroadcasterBinding>()
    private val adapter by lazy { ChuckerAdapter(listener = this) }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(NetworkLogViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentChuckerBroadcasterBinding
        .inflate(inflater)
        .also {
            binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)

        initView()
        initObservable()
    }

    private fun initView() {
        binding?.lstLog?.adapter = adapter
    }

    private fun initObservable() {
        viewModel.chuckers.observe(viewLifecycleOwner, {
            adapter.updateItems(it)
        })
    }

    override fun onLogClicked(model: LoggerUIModel) {
        NetworkLogDetailSheet.create(childFragmentManager, model)
    }

    override fun initInjector() {
        DaggerNetworkLogComponent.builder()
            .networkLogModule(NetworkLogModule(requireContext()))
            .build()
            .inject(this)
    }

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Broadcaster Chucker"
    }

}