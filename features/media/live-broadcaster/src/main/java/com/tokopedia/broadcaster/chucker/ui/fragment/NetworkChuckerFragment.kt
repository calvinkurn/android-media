package com.tokopedia.broadcaster.chucker.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.broadcaster.chucker.di.DaggerChuckerComponent
import com.tokopedia.broadcaster.chucker.di.module.ChuckerModule
import com.tokopedia.broadcaster.chucker.ui.uimodel.ChuckerLogUIModel
import com.tokopedia.broadcaster.chucker.ui.viewmodel.NetworkChuckerViewModel
import com.tokopedia.broadcaster.databinding.FragmentChuckerBroadcasterBinding
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NetworkChuckerFragment : BaseDaggerFragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private var binding by viewBinding<FragmentChuckerBroadcasterBinding>()

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(NetworkChuckerViewModel::class.java)
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
        initObservable()
    }

    private fun initObservable() {
        viewModel.broadcasterLog.observe(viewLifecycleOwner, {
            it.forEach { data ->
                Log.d("BROADCASTER_LOG", data.userId)
            }
        })

        GlobalScope.launch {
            repeat(20) {
                viewModel.log(ChuckerLogUIModel(
                    userId = "abcdefg1234".random().toString()
                ))
            }
        }
    }

    override fun initInjector() {
        DaggerChuckerComponent.builder()
            .chuckerModule(ChuckerModule(requireContext()))
            .build()
            .inject(this)
    }

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Broadcaster Chucker"
    }

}