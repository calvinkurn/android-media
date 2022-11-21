package com.tokopedia.tkpd.flashsale.presentation.detail.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsBottomsheetCampaignDetailBinding
import com.tokopedia.tkpd.flashsale.presentation.common.adapter.TabPagerAdapter
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.CampaignDetailBottomSheetModel
import com.tokopedia.tkpd.flashsale.presentation.detail.viewmodel.CampaignDetailBottomSheetViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CampaignDetailBottomSheet: BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_CAMPAIGN_DETAIL_MODEL = "CampaignDetailBottomSheetModel"
        @JvmStatic
        fun newInstance(campaignDetailBottomSheetModel: CampaignDetailBottomSheetModel): CampaignDetailBottomSheet {
            return CampaignDetailBottomSheet().apply {
                arguments = Bundle().apply { //TODO: use SaveInstanceCacheManager
                    putParcelable(BUNDLE_KEY_CAMPAIGN_DETAIL_MODEL, campaignDetailBottomSheetModel)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: CampaignDetailBottomSheetViewModel
    private var binding by autoClearedNullable<StfsBottomsheetCampaignDetailBinding>()
    private val campaignDetailBottomSheetModel by lazy {
        arguments?.getParcelable<CampaignDetailBottomSheetModel>(BUNDLE_KEY_CAMPAIGN_DETAIL_MODEL)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
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
        setupFragmentList()
    }

    private fun setupDependencyInjection() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    private fun setupBottomSheet() {
        binding = StfsBottomsheetCampaignDetailBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        setTitle(getString(R.string.campaigndetail_bottomsheet_title))
    }

    private fun setupFragmentList() {
        viewModel.setCampaignDetailData(campaignDetailBottomSheetModel ?: return)
        viewModel.fragmentList.observe(viewLifecycleOwner) {
            displayFragment(it)
        }
        viewModel.showTab.observe(viewLifecycleOwner) {
            binding?.tabsDetails?.isVisible = it
        }
    }

    private fun displayFragment(fragments: List<Pair<String, Fragment>>) {
        val pagerAdapter = TabPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle, fragments)
        binding?.run {
            viewPagerContent.adapter = pagerAdapter
            tabsDetails.customTabMode = TabLayout.MODE_FIXED

            TabsUnifyMediator(tabsDetails, viewPagerContent) { tab, currentPosition ->
                tab.setCustomText(pagerAdapter.getTitle(currentPosition))
            }
        }
    }

}
