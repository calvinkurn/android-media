package com.tokopedia.tkpd.flashsale.presentation.detail.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsBottomsheetCampaignDetailBinding
import com.tokopedia.tkpd.flashsale.common.adapter.TabPagerAdapter
import com.tokopedia.tkpd.flashsale.presentation.detail.fragment.CampaignCriteriaFragment
import com.tokopedia.tkpd.flashsale.presentation.detail.fragment.CampaignProductCriteriaFragment
import com.tokopedia.tkpd.flashsale.presentation.detail.fragment.CampaignTimelineFragment
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.TimelineStepModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CampaignDetailBottomSheet: BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_TIMELINE_STEP_MODEL = "TimelineStepModel"
        @JvmStatic
        fun newInstance(timelineSteps: List<TimelineStepModel>): CampaignDetailBottomSheet {
            return CampaignDetailBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(BUNDLE_KEY_TIMELINE_STEP_MODEL, ArrayList(timelineSteps))
                }
            }
        }
    }

    private var binding by autoClearedNullable<StfsBottomsheetCampaignDetailBinding>()
    private val timelineSteps by lazy {
        arguments?.getParcelableArrayList<TimelineStepModel>(BUNDLE_KEY_TIMELINE_STEP_MODEL)
    }

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
        val timelineTitle = getString(R.string.campaigndetail_timeline_title)
        val criteriaTitle = getString(R.string.campaigndetail_criteria_title)
        val productCriteriaTitle = getString(R.string.campaigndetail_product_criteria_title)

        val timelineFragment = CampaignTimelineFragment.newInstance(timelineSteps?.toList().orEmpty())
        val criteriaFragment = CampaignCriteriaFragment()
        val productCriteriaFragment = CampaignProductCriteriaFragment()

        val fragments = listOf(
            Pair(criteriaTitle, criteriaFragment),
            Pair(productCriteriaTitle, productCriteriaFragment),
            Pair(timelineTitle, timelineFragment),
        )
        val pagerAdapter = TabPagerAdapter(childFragmentManager,
            viewLifecycleOwner.lifecycle, fragments)

        binding?.run {
            viewPagerContent.adapter = pagerAdapter
            tabsDetails.customTabMode = TabLayout.MODE_FIXED

            TabsUnifyMediator(tabsDetails, viewPagerContent) { tab, currentPosition ->
                tab.setCustomText(pagerAdapter.getTitle(currentPosition))
            }
        }
    }

}