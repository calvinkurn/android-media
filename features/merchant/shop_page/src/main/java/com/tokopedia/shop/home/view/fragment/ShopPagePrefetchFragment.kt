package com.tokopedia.shop.home.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.shop.databinding.FragmentShopPagePrefetchBinding
import com.tokopedia.shop.pageheader.presentation.listener.ShopPageHeaderPerformanceMonitoringListener
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ShopPagePrefetchFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(): ShopPagePrefetchFragment = ShopPagePrefetchFragment()
    }

    private var binding by autoClearedNullable<FragmentShopPagePrefetchBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopPagePrefetchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stopMonitoringPltRenderPage()
    }

    private fun stopMonitoringPltRenderPage() {
        (activity as? ShopPageHeaderPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                // shopPageActivity.stopMonitoringPltRenderPage(it)
                shopPageActivity.stopTraceMonitoring(it)
            }
        }
    }
}
