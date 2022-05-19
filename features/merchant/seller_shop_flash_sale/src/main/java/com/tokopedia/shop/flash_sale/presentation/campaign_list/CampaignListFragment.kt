package com.tokopedia.shop.flash_sale.presentation.campaign_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentCampaignListBinding
import com.tokopedia.shop.flash_sale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CampaignListFragment: BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): CampaignListFragment {
            val fragment = CampaignListFragment()
            return fragment
        }

    }

    private var binding by autoClearedNullable<SsfsFragmentCampaignListBinding>()


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
        binding = SsfsFragmentCampaignListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {

    }

}