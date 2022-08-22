package com.tokopedia.tkpd.flashsale.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.*
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.Campaign
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CampaignDetailFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = CampaignDetailFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignDetailViewModel::class.java) }

    //View Binding
    private var binding by autoClearedNullable<StfsFragmentCampaignDetailBinding>()
    private var upcomingCdpHeaderBinding by autoClearedNullable<StfsCdpUpcomingHeaderBinding>()
    private var upcomingCdpMidBinding by autoClearedNullable<StfsCdpUpcomingMidBinding>()
    private var upcomingCdpBodyBinding: StfsCdpUpcomingBodyBinding? = null

    override fun getScreenName(): String =
        CampaignDetailFragment::class.java.canonicalName.orEmpty()

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
        binding = StfsFragmentCampaignDetailBinding.inflate(inflater, container, false)
        binding?.run {
            layoutHeader.setOnInflateListener { _, view ->
                upcomingCdpHeaderBinding = StfsCdpUpcomingHeaderBinding.bind(view)
            }
        }
        binding?.run {
            layoutMid.setOnInflateListener { _, view ->
                upcomingCdpMidBinding = StfsCdpUpcomingMidBinding.bind(view)
            }
        }
        binding?.run {
            layoutBody.setOnInflateListener { _, view ->
                upcomingCdpBodyBinding = StfsCdpUpcomingBodyBinding.bind(view)
            }
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCampaignDetail()
        viewModel.getCampaignDetail(784131)
    }

    private fun observeCampaignDetail() {
        viewModel.campaign.observe(viewLifecycleOwner) { campaign ->
            when (campaign) {
                is Success -> {
                    setupUpcoming(campaign.data)
                }
                is Fail -> {
                }
            }
        }
    }

    private fun setupUpcoming(campaign: Campaign) {
        setupUpcomingHeader(campaign)
        setupUpcomingMid(campaign)
        setupUpcomingBody(campaign)
    }

    private fun setupUpcomingHeader(campaign: Campaign) {
        val binding = binding ?: return
        val inflatedView = binding.layoutHeader
        inflatedView.layoutResource = R.layout.stfs_cdp_upcoming_header
        inflatedView.inflate()
        upcomingCdpHeaderBinding?.run {
            tgCampaignName.text = campaign.name
        }
    }

    private fun setupUpcomingMid(campaign: Campaign) {
        val binding = binding ?: return
        val inflatedView = binding.layoutMid
        inflatedView.layoutResource = R.layout.stfs_cdp_upcoming_mid
        inflatedView.inflate()
        upcomingCdpMidBinding?.run {
            tgRegisterPeriod.text = campaign.submissionStartDateUnix.toString()
            tgCampaignQuota.text = getString(
                R.string.campaign_quota_value_placeholder,
                campaign.remainingQuota
            )
        }
    }

    private fun setupUpcomingBody(campaign: Campaign) {
        val binding = binding ?: return
        val inflatedView = binding.layoutBody
        inflatedView.layoutResource = R.layout.stfs_cdp_upcoming_body
        inflatedView.inflate()
        upcomingCdpBodyBinding?.run {
            tgDescription.text = MethodChecker.fromHtml(
                campaign.description
            )
        }
    }
}