package com.tokopedia.shop.flashsale.presentation.list.quotamonitoring

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SfsFragmentQuotaMonitoringBinding
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.presentation.list.container.CampaignListContainerFragment
import com.tokopedia.shop.flashsale.presentation.list.container.CampaignListContainerViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class QuotaMonitoringFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = QuotaMonitoringFragment()
    }

    private var binding by autoClearedNullable<SfsFragmentQuotaMonitoringBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(QuotaMonitoringViewModel::class.java) }

    override fun getScreenName(): String =
        CampaignListContainerFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SfsFragmentQuotaMonitoringBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}