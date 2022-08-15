package com.tokopedia.tkpd.flashsale.presentation.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentCampaignProductCriteriaBinding
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.campaigndetail.ProductCriteriaAdapter
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CampaignProductCriteriaFragment: BaseDaggerFragment() {

    private var binding by autoClearedNullable<StfsFragmentCampaignProductCriteriaBinding>()

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StfsFragmentCampaignProductCriteriaBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCriteriaList()
    }

    private fun setupCriteriaList() {
        binding?.rvProductCriteria?.apply {
            val criteriaItems = listOf(
                "Olahraga",
                "Otomotif, Fashion Pria",
                "Suplemen Diet, + 10 lainnya"
            )
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ProductCriteriaAdapter().apply {
                data = criteriaItems.toList()
            }
        }
    }
}