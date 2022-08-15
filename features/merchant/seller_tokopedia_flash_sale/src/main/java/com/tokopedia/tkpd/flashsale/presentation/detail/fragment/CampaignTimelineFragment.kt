package com.tokopedia.tkpd.flashsale.presentation.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentCampaignTimelineBinding
import com.tokopedia.tkpd.flashsale.common.adapter.VerticalSpaceItemDecoration
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.campaigndetail.TimelineProcessAdapter
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CampaignTimelineFragment: BaseDaggerFragment() {

    private var binding by autoClearedNullable<StfsFragmentCampaignTimelineBinding>()

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
        binding = StfsFragmentCampaignTimelineBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTimelineList()
    }

    private fun setupTimelineList() {
        binding?.rvTimelineProcess?.apply {
            val spacingAmount = resources.getDimensionPixelSize(
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl7)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(VerticalSpaceItemDecoration(spacingAmount))
            adapter = TimelineProcessAdapter().apply {
                data = listOf("Satu", "Dua", "Tiga")
            }
        }
    }
}