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
import java.util.*
import javax.inject.Inject

class CampaignListContainerFragment: BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): CampaignListContainerFragment {
            val fragment = CampaignListContainerFragment()
            return fragment
        }

    }

    private var binding by autoClearedNullable<FragmentCampaignListBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignListContainerViewModel::class.java) }

    override fun getScreenName(): String = CampaignListContainerFragment::class.java.canonicalName.orEmpty()
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
        observeTabsMeta()
        observeCampaigns()
        observeCampaignAttribute()
        observeCampaignCreation()
        viewModel.getTabsMeta()
        viewModel.getCampaigns(10, 1, emptyList(), "", false)
        viewModel.getCampaignAttribute(5, 2022)
        viewModel.createCampaign(
            "campaign-test",
            GregorianCalendar(2022, 6, 10, 13, 0, 0).time,
            GregorianCalendar(2022, 6, 15, 13, 0, 0).time
        )
    }

    private fun observeTabsMeta() {
        viewModel.tabsMeta.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    val tabs = result.data
                }
                is Fail -> {

                }
            }
        }
    }


    private fun observeCampaigns() {
        viewModel.campaigns.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    val campaigns = result.data
                }
                is Fail -> {

                }
            }
        }
    }

    private fun observeCampaignAttribute() {
        viewModel.campaignAttribute.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    val attribute = result.data
                }
                is Fail -> {

                }
            }
        }
    }

    private fun observeCampaignCreation() {
        viewModel.campaignCreation.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    val creationResult = result.data
                }
                is Fail -> {

                }
            }
        }
    }

    private fun setupView() {

    }



}