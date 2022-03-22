package com.tokopedia.analyticsdebugger.serverlogger.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.analyticsdebugger.databinding.FragmentServerLoggerDetailBinding
import com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.ServerChannelAdapter
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.ArrayList

class ServerLoggerDetailFragment : Fragment() {

    private var binding by autoClearedNullable<FragmentServerLoggerDetailBinding>()

    private val serverChannelAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ServerChannelAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServerLoggerDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding?.run {
            requireArguments().let {
                tvServerLoggerDetailTag.text = it.getString(EXTRA_TAG)
                tvServerLoggerDetailPriority.text = it.getString(EXTRA_PRIORITY)
                setupChannels(it.getStringArray(EXTRA_SERVER_CHANNEL))
                tvServerLoggerDetailMessage.text = it.getString(EXTRA_MESSAGE)
                tvServerLoggerDetailDateTime.text = it.getString(EXTRA_DATE_TIME)
            }
        }
    }

    private fun setupChannels(serverChannelList: Array<String>?) {
        binding?.rvServerLoggerDetailChannel?.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = serverChannelAdapter
            serverChannelAdapter.setServerChannelList(serverChannelList?.toList().orEmpty())
        }
    }

    companion object {
        const val EXTRA_TAG = "EXTRA_TAG"
        const val EXTRA_PRIORITY = "EXTRA_PRIORITY"
        const val EXTRA_SERVER_CHANNEL = "EXTRA_SERVER_CHANNEL"
        const val EXTRA_DATE_TIME = "EXTRA_DATE_TIME"
        const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
    }
}