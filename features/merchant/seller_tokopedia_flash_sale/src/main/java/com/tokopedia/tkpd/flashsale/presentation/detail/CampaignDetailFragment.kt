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
import com.tokopedia.campaign.utils.constant.DateConstant.DATE_MONTH_ONLY
import com.tokopedia.campaign.utils.constant.DateConstant.DATE_WITH_TIME
import com.tokopedia.campaign.utils.constant.DateConstant.TIME_WIB
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.*
import com.tokopedia.tkpd.flashsale.common.extension.*
import com.tokopedia.tkpd.flashsale.common.extension.toCalendar
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
        private const val UPCOMING_TAB = "upcoming"
        private const val REGISTERED_TAB = "registered"
        private const val ONGOING_TAB = "ongoing"
        private const val FINISHED_TAB = "finished"

        @JvmStatic
        fun newInstance(flashSaleId: Long, tabName: String): CampaignDetailFragment {
            val fragment = CampaignDetailFragment()
            val bundle = Bundle()
            bundle.putLong(BundleConstant.BUNDLE_FLASH_SALE_ID, flashSaleId)
            bundle.putString(BundleConstant.BUNDLE_KEY_TAB_NAME, tabName)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignDetailViewModel::class.java) }

    //View Binding
    private var binding by autoClearedNullable<StfsFragmentCampaignDetailBinding>()
    private var upcomingCdpHeaderBinding by autoClearedNullable<StfsCdpUpcomingHeaderBinding>()
    private var upcomingCdpMidBinding by autoClearedNullable<StfsCdpUpcomingMidBinding>()
    private var upcomingCdpBodyBinding by autoClearedNullable<StfsCdpUpcomingBodyBinding>()

    private val flashSaleId by lazy {
        arguments?.getLong(BundleConstant.BUNDLE_FLASH_SALE_ID).orZero()
    }

    private val tabName by lazy {
        arguments?.getString(BundleConstant.BUNDLE_KEY_TAB_NAME).orEmpty()
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
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCampaignDetail()
        loadData()
    }

    private fun observeCampaignDetail() {
        viewModel.campaign.observe(viewLifecycleOwner) { flashSale ->
            hideLoading()
            when (flashSale) {
                is Success -> {
                    setupView(flashSale.data)
                }
                is Fail -> {
                    //TODO: Add Error Handling, not available on figma yet
                }
            }
        }
    }

    private fun loadData() {
        showLoading()
        viewModel.getCampaignDetail(flashSaleId, tabName)
    }

    private fun setupView(flashSale: FlashSale) {
        when (tabName) {
            UPCOMING_TAB -> setupUpcoming(flashSale)
            REGISTERED_TAB -> setupRegistered()
            ONGOING_TAB -> setupOngoing()
            FINISHED_TAB -> setupFinished()
        }
    }

    /**
     *Region Upcoming CDP
     */
    private fun setupUpcoming(flashSale: FlashSale) {
        val campaignStatus = when {
            viewModel.isCampaignRegisterClosed(flashSale) -> UpcomingCampaignStatus.CLOSED
            flashSale.remainingQuota.isZero() -> UpcomingCampaignStatus.FULL_QUOTA
            else -> {
                if (flashSale.hasEligibleProduct) {
                    UpcomingCampaignStatus.AVAILABLE
                } else {
                    UpcomingCampaignStatus.NO_PRODUCT_ELIGIBLE
                }
            }
        }

        binding?.run {
            layoutHeader.setOnInflateListener { _, view ->
                upcomingCdpHeaderBinding = StfsCdpUpcomingHeaderBinding.bind(view)
            }
            layoutMid.setOnInflateListener { _, view ->
                upcomingCdpMidBinding = StfsCdpUpcomingMidBinding.bind(view)
            }
            layoutBody.setOnInflateListener { _, view ->
                upcomingCdpBodyBinding = StfsCdpUpcomingBodyBinding.bind(view)
            }
            cardBottomButtonGroup.isVisible = campaignStatus.isFlashSaleAvailable()
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
        setupUpcomingHeaderStatus(flashSale, campaignStatus)
    }

    private fun setupUpcomingMid(flashSale: FlashSale, campaignStatus: UpcomingCampaignStatus) {
        val binding = binding ?: return
        val inflatedView = binding.layoutMid
        inflatedView.layoutResource = R.layout.stfs_cdp_upcoming_mid
        inflatedView.inflate()
        upcomingCdpMidBinding?.run {
            setupUpcomingMidStatus(flashSale, campaignStatus)
            val startSubmissionDate = flashSale.submissionStartDateUnix.formatTo(
                DATE_MONTH_ONLY
            )
            val endSubmissionDate = flashSale.submissionEndDateUnix.formatTo(DATE_MONTH_ONLY)
            tgRegisterPeriod.text = getString(
                R.string.register_period_value_placeholder_2,
                startSubmissionDate,
                endSubmissionDate
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

    private fun setupUpcomingHeaderStatus(flashSale: FlashSale, campaignStatus: UpcomingCampaignStatus) {
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
            imageCampaign.setImageUrl(flashSale.coverImage)
            tgCampaignName.text = flashSale.name
            setupUpcomingTimer(this, flashSale)
            setUpcomingCampaignPeriod(this, flashSale)
        }
    }

    private fun setupUpcomingMidStatus(flashSale: FlashSale, campaignStatus: UpcomingCampaignStatus) {
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

    private fun setupUpcomingTimer(binding: StfsCdpUpcomingHeaderBinding, flashSale: FlashSale) {
        val targetDate = flashSale.submissionEndDateUnix
        val onTimerFinished = { binding.timer.gone() }
        when {
            viewModel.isFlashSaleClosedMoreThan24Hours(targetDate) -> {
                binding.timer.timerFormat = TimerUnifySingle.FORMAT_DAY
                binding.timer.targetDate = targetDate.removeTimeZone().toCalendar()
                binding.timer.timerVariant = TimerUnifySingle.VARIANT_INFORMATIVE
                binding.timer.onFinish = onTimerFinished
            }
            viewModel.isFlashSaleClosedLessThan24Hour(targetDate) -> {
                binding.timer.timerFormat = TimerUnifySingle.FORMAT_HOUR
                binding.timer.targetDate = targetDate.removeTimeZone().toCalendar()
                binding.timer.timerVariant = TimerUnifySingle.VARIANT_GENERAL
                binding.timer.onFinish = onTimerFinished
            }
            viewModel.isFlashSaleClosedLessThan60Minutes(targetDate) -> {
                binding.timer.timerFormat = TimerUnifySingle.FORMAT_MINUTE
                binding.timer.targetDate = targetDate.removeTimeZone().toCalendar()
                binding.timer.timerVariant = TimerUnifySingle.VARIANT_GENERAL
                binding.timer.onFinish = onTimerFinished
            }
            else -> onTimerFinished
        }
    }

    /**
     * Region Registered CDP
     */
    private fun setupRegistered() {
        //TODO: implement registered CDP
    }

    /**
     * Region Ongoing CDP
     */
    private fun setupOngoing() {
        //TODO: implement ongoing CDP
    }

    /**
     * Region Finished CDP
     */
    private fun setupFinished() {
        //TODO: implement finished CDP
    }

    private fun showLoading() {
        binding?.run {
            loader.show()
            nsvContent.gone()
            cardBottomButtonGroup.gone()
        }
    }

    private fun hideLoading() {
        binding?.run {
            loader.gone()
            nsvContent.show()
        }
    }
}