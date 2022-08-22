package com.tokopedia.tkpd.flashsale.presentation.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.*
import com.tokopedia.tkpd.flashsale.common.extension.*
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.Campaign
import com.tokopedia.tkpd.flashsale.domain.entity.enums.UpcomingCampaignStatus
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CampaignDetailFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = CampaignDetailFragment()
        const val DATE_WITH_TIME = "dd MMM yyyy, HH:mm 'WIB'"
        const val TIME_WIB = "HH.mm 'WIB'"
        const val DATE_MONTH_ONLY = "dd MMM"
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
        val campaignStatus = when {
            !campaign.hasEligibleProduct -> UpcomingCampaignStatus.NO_PRODUCT_ELIGIBLE
            campaign.remainingQuota == 0 -> UpcomingCampaignStatus.FULL_QUOTA
            viewModel.isCampaignRegisterClosed(campaign) -> UpcomingCampaignStatus.CLOSED
            else -> UpcomingCampaignStatus.AVAILABLE
        }
        setupUpcomingHeader(campaign, campaignStatus)
        setupUpcomingMid(campaign, campaignStatus)
        setupUpcomingBody(campaign, campaignStatus)
    }

    private fun setupUpcomingHeader(campaign: Campaign, campaignStatus: UpcomingCampaignStatus) {
        val binding = binding ?: return
        val inflatedView = binding.layoutHeader
        inflatedView.layoutResource = R.layout.stfs_cdp_upcoming_header
        inflatedView.inflate()
        upcomingCdpHeaderBinding?.run {
            when (campaignStatus) {
                UpcomingCampaignStatus.NO_PRODUCT_ELIGIBLE -> {
                    tickerHeader.visible()
                    tickerHeader.setTextDescription(getString(R.string.stfs_description_ticker_upcoming_cdp_product_not_eligible_state))
                    tgCampaignStatus.text = getString(R.string.registration_over_in_label)
                }
                UpcomingCampaignStatus.FULL_QUOTA -> {
                    tickerHeader.visible()
                    tickerHeader.tickerTitle =
                        getString(R.string.stfs_title_ticker_upcoming_cdp_full_quota_state)
                    tickerHeader.setTextDescription(getString(R.string.stfs_description_ticker_upcoming_cdp_full_quota_state))
                    tgCampaignStatus.text = getString(R.string.registration_over_in_label)
                }
                UpcomingCampaignStatus.CLOSED -> {
                    tickerHeader.visible()
                    tickerHeader.tickerTitle =
                        getString(R.string.stfs_title_ticker_upcoming_cdp_registration_close_state)
                    tickerHeader.setTextDescription(getString(R.string.stfs_description_ticker_upcoming_cdp_registration_close_state))
                    tgCampaignStatus.text = getString(R.string.registration_closed_in_label)
                }
                else -> {
                    tickerHeader.gone()
                    tgCampaignStatus.text = getString(R.string.registration_over_in_label)
                }
            }
            startUpcomingTimer(this, campaign)
            tgCampaignName.text = campaign.name
            setUpcomingCampaignPeriod(this, campaign)
        }
    }

    private fun setupUpcomingMid(campaign: Campaign, campaignStatus: UpcomingCampaignStatus) {
        val binding = binding ?: return
        val inflatedView = binding.layoutMid
        inflatedView.layoutResource = R.layout.stfs_cdp_upcoming_mid
        inflatedView.inflate()
        upcomingCdpMidBinding?.run {
            when (campaignStatus) {
                UpcomingCampaignStatus.NO_PRODUCT_ELIGIBLE -> {
                    tgCampaignQuota.text = getString(
                        R.string.campaign_quota_value_placeholder,
                        campaign.remainingQuota
                    )
                    btnCheckReason.visible()
                }
                UpcomingCampaignStatus.FULL_QUOTA -> {
                    tgCampaignQuota.apply {
                        text = getString(R.string.full_quota_label)
                        setTextColor(
                            ContextCompat.getColor(
                                context,
                                com.tokopedia.unifyprinciples.R.color.Unify_R500
                            )
                        )
                    }
                }
                UpcomingCampaignStatus.CLOSED -> {
                    tgCampaignQuota.text = getString(
                        R.string.campaign_quota_value_placeholder_2,
                        campaign.remainingQuota
                    )
                }
                else -> {
                    tgCampaignQuota.text = getString(
                        R.string.campaign_quota_value_placeholder,
                        campaign.remainingQuota
                    )
                }
            }
            val startSubmissionDate = campaign.submissionStartDateUnix.epochToDate().formatTo(
                DATE_MONTH_ONLY
            )
            val endSubmissionDate = campaign.submissionEndDateUnix.epochToDate().formatTo(
                DATE_MONTH_ONLY
            )
            tgRegisterPeriod.text = getString(
                R.string.register_period_value_placeholder_2,
                startSubmissionDate,
                endSubmissionDate
            )
            tgCampaignQuota.text = getString(
                R.string.campaign_quota_value_placeholder,
                campaign.remainingQuota
            )
        }
    }

    private fun setupUpcomingBody(campaign: Campaign, campaignStatus: UpcomingCampaignStatus) {
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

    private fun setUpcomingCampaignPeriod(
        binding: StfsCdpUpcomingHeaderBinding,
        campaign: Campaign
    ) {
        binding.run {
            val startDate = campaign.startDateUnix.epochToDate().formatTo(DATE_WITH_TIME)
            tgCampaignPeriod.text = if (viewModel.isFlashSalePeriodOnTheSameDate(campaign)) {
                val endDate = campaign.endDateUnix.epochToDate().formatTo(TIME_WIB)
                "$startDate - $endDate"
            } else {
                val endDate = campaign.endDateUnix.epochToDate().formatTo(DATE_WITH_TIME)
                "$startDate - $endDate"
            }
        }
    }

    private fun startUpcomingTimer(binding: StfsCdpUpcomingHeaderBinding, campaign: Campaign) {
        val targetDate = campaign.submissionEndDateUnix.epochToDate()
        if (viewModel.isCampaignClosedMoreThan24Hours(campaign)) {
            binding.timer.timerFormat = TimerUnifySingle.FORMAT_DAY
            binding.timer.targetDate = targetDate.removeTimeZone().toCalendar()
        } else if (targetDate.removeTimeZone().minuteDifference() >= 60) {
            binding.timer.timerFormat = TimerUnifySingle.FORMAT_HOUR
            binding.timer.targetDate = targetDate.removeTimeZone().toCalendar()
        } else {
            binding.timer.timerFormat = TimerUnifySingle.FORMAT_MINUTE
            binding.timer.targetDate = targetDate.toCalendar()
        }
    }
}