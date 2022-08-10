package com.tokopedia.tkpd.flashsale.presentation.list.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsBottomsheetCampaignDetailBinding
import com.tokopedia.tkpd.flashsale.common.adapter.TabPagerAdapter
import com.tokopedia.tkpd.flashsale.presentation.list.fragment.CampaignCriteriaFragment
import com.tokopedia.tkpd.flashsale.presentation.list.fragment.CampaignProductCriteriaFragment
import com.tokopedia.tkpd.flashsale.presentation.list.fragment.CampaignTimelineFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CampaignDetailBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<StfsBottomsheetCampaignDetailBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        clearContentPadding = true
        setupBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayTabs()
    }

    private fun setupBottomSheet() {
        binding = StfsBottomsheetCampaignDetailBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        setTitle("Detail Campaign")
    }

    private fun displayTabs() {
        val fragments = listOf(
            Pair("ABC", CampaignTimelineFragment()),
            Pair("DEF", CampaignCriteriaFragment()),
            Pair("ABC🇮🇩", CampaignProductCriteriaFragment())
        )
        val pagerAdapter = TabPagerAdapter(childFragmentManager,
            viewLifecycleOwner.lifecycle, fragments)

        binding?.run {
            viewPagerContent.adapter = pagerAdapter
            tabsDetails.customTabMode = TabLayout.MODE_FIXED

            TabsUnifyMediator(tabsDetails, viewPagerContent) { tab, currentPosition ->
                tab.setCustomText(fragments.getOrNull(currentPosition)?.first.orEmpty())
            }
        }
    }

}