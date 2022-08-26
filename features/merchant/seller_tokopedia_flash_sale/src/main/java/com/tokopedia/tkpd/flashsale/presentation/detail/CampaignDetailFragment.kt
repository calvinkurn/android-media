package com.tokopedia.tkpd.flashsale.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.*
import com.tokopedia.tkpd.flashsale.common.extension.*
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.entity.enums.UpcomingCampaignStatus
import com.tokopedia.tkpd.flashsale.domain.entity.enums.isFlashSaleAvailable
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CampaignDetailFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance(flashSaleId: Long): CampaignDetailFragment {
            val fragment = CampaignDetailFragment()
            val bundle = Bundle()
            bundle.putLong(BundleConstant.BUNDLE_FLASH_SALE_ID, flashSaleId)
            fragment.arguments = bundle
            return fragment
        }

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

    private val flashSaleId by lazy {
        arguments?.getLong(BundleConstant.BUNDLE_FLASH_SALE_ID).orZero()
    }

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
        viewModel.getCampaignDetail(flashSaleId)
    }

    private fun observeCampaignDetail() {
        viewModel.campaign.observe(viewLifecycleOwner) { flashSale ->
            when (flashSale) {
                is Success -> {
                    setupUpcoming(flashSale.data)
                }
                is Fail -> {
                }
            }
        }
    }

    private fun setupUpcoming(flashSale: FlashSale) {
        val campaignStatus = when {
            !flashSale.hasEligibleProduct -> UpcomingCampaignStatus.NO_PRODUCT_ELIGIBLE
            flashSale.remainingQuota == 0 -> UpcomingCampaignStatus.FULL_QUOTA
            viewModel.isCampaignRegisterClosed(flashSale) -> UpcomingCampaignStatus.CLOSED
            else -> UpcomingCampaignStatus.AVAILABLE
        }

        binding?.run {
            if (campaignStatus.isFlashSaleAvailable()) {
                cardBottomButtonGroup.visible()
            } else {
                cardBottomButtonGroup.gone()
            }
        }

        setupUpcomingHeader(flashSale, campaignStatus)
        setupUpcomingMid(flashSale, campaignStatus)
        setupUpcomingBody(flashSale)
    }

    private fun setupUpcomingHeader(flashSale: FlashSale, campaignStatus: UpcomingCampaignStatus) {
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
            startUpcomingTimer(this, flashSale)
            imageCampaign.setImageUrl(flashSale.coverImage)
            tgCampaignName.text = flashSale.name
            setUpcomingCampaignPeriod(this, flashSale)
        }
    }

    private fun setupUpcomingMid(flashSale: FlashSale, campaignStatus: UpcomingCampaignStatus) {
        val binding = binding ?: return
        val inflatedView = binding.layoutMid
        inflatedView.layoutResource = R.layout.stfs_cdp_upcoming_mid
        inflatedView.inflate()
        upcomingCdpMidBinding?.run {
            when (campaignStatus) {
                UpcomingCampaignStatus.NO_PRODUCT_ELIGIBLE -> {
                    tgCampaignQuota.text = getString(
                        R.string.campaign_quota_value_placeholder,
                        flashSale.remainingQuota
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
                        flashSale.remainingQuota
                    )
                }
                else -> {
                    tgCampaignQuota.text = getString(
                        R.string.campaign_quota_value_placeholder,
                        flashSale.remainingQuota
                    )
                }
            }
            val startSubmissionDate = flashSale.submissionStartDateUnix.formatTo(
                DATE_MONTH_ONLY
            )
            val endSubmissionDate = flashSale.submissionEndDateUnix.formatTo(
                DATE_MONTH_ONLY
            )
            tgRegisterPeriod.text = getString(
                R.string.register_period_value_placeholder_2,
                startSubmissionDate,
                endSubmissionDate
            )
            tgCampaignQuota.text = getString(
                R.string.campaign_quota_value_placeholder,
                flashSale.remainingQuota
            )
        }
    }

    private fun setupUpcomingBody(flashSale: FlashSale) {
        val binding = binding ?: return
        val inflatedView = binding.layoutBody
        inflatedView.layoutResource = R.layout.stfs_cdp_upcoming_body
        inflatedView.inflate()
        upcomingCdpBodyBinding?.run {
            tgDescription.text = MethodChecker.fromHtml(
                flashSale.description
            )
        }
    }

    private fun setUpcomingCampaignPeriod(
        binding: StfsCdpUpcomingHeaderBinding,
        flashSale: FlashSale
    ) {
        binding.run {
            val startDate = flashSale.startDateUnix.formatTo(DATE_WITH_TIME)
            tgCampaignPeriod.text = if (viewModel.isFlashSalePeriodOnTheSameDate(flashSale)) {
                val endDate = flashSale.endDateUnix.formatTo(TIME_WIB)
                "$startDate - $endDate"
            } else {
                val endDate = flashSale.endDateUnix.formatTo(DATE_WITH_TIME)
                "$startDate - $endDate"
            }
        }
    }

    private fun startUpcomingTimer(binding: StfsCdpUpcomingHeaderBinding, flashSale: FlashSale) {
        val targetDate = flashSale.submissionEndDateUnix
        val onTimerFinished = { binding.timer.gone() }
        when {
            viewModel.isFlashSaleClosedMoreThan24Hours(targetDate) -> {
                binding.timer.timerFormat = TimerUnifySingle.FORMAT_DAY
                binding.timer.targetDate = targetDate.removeTimeZone().toCalendar()
                binding.timer.onFinish = onTimerFinished
            }
            viewModel.isFlashSaleClosedLessThan24Hour(targetDate) -> {
                binding.timer.timerFormat = TimerUnifySingle.FORMAT_HOUR
                binding.timer.targetDate = targetDate.removeTimeZone().toCalendar()
                binding.timer.onFinish = onTimerFinished
            }
            else -> {
                onTimerFinished
            }
        }
    }
}