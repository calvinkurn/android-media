package com.tokopedia.shop.flash_sale.presentation.campaign_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.seller_shop_flash_sale.databinding.FragmentCampaignListBinding
import com.tokopedia.shop.flash_sale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CampaignListFragment: BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): CampaignListFragment {
            val fragment = CampaignListFragment()
            return fragment
        }

    }

    private var binding by autoClearedNullable<FragmentCampaignListBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignListViewModel::class.java) }

    override fun getScreenName(): String = CampaignListFragment::class.java.canonicalName.orEmpty()
    override fun initInjector() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCampaignListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeCampaignMeta()
        viewModel.getCampaignMeta(0)
    }

    private fun observeCampaignMeta() {
        viewModel.campaignListMeta.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    val campaignMeta = result.data
                }
                is Fail -> {

                }
            }
        }
    }

    private fun setupView() {

    }



}