package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.common.KycStatus
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycStatusSubmissionBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatusSubmissionFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentGotoKycStatusSubmissionBinding>()

    private val args: StatusSubmissionFragmentArgs by navArgs()
    private var dataSource: String = ""
    private var sourcePage: String = ""
    private var status: String = ""
    private var listReason: List<String> = emptyList()
    private var isAccountPage: Boolean = false

    private var bottomSheetDetailBenefit: BenefitDetailBottomSheet? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGotoKycStatusSubmissionBinding.inflate(inflater, container, false)

        dataSource = args.parameter.dataSource
        status = args.parameter.status
        sourcePage = args.parameter.sourcePage
        listReason = args.parameter.listReason
        isAccountPage = args.parameter.isCameFromAccountPage

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        initListener()
        initToolbar()
    }

    private fun initToolbar() {
        binding?.unifyToolbar?.setNavigationOnClickListener { activity?.finish() }
    }

    private fun setUpView() {
        when(status) {
            KycStatus.REJECTED.code.toString() -> {
                onRejected()
            }
            KycStatus.PENDING.code.toString() -> {
                onPending24Hours()
            }
            KycStatus.VERIFIED.code.toString() -> {
                if (dataSource == KYCConstant.GotoDataSource.GOTO_PROGRESSIVE) {
                    onVerifiedProgressive()
                } else {
                    onVerifiedNonProgressive()
                }
            }
            KycStatus.BLACKLISTED.code.toString() -> {
                onBlackListed()
            }
        }
    }

    private fun initListener() {
        binding?.layoutStatusSubmission?.btnSecondary?.setOnClickListener {
            goToTokopediaCare()
        }

        binding?.layoutStatusSubmission?.btnPrimary?.setOnClickListener {
            when(status) {
                KycStatus.BLACKLISTED.code.toString() -> {
                    goToTokopediaCare()
                }
                KycStatus.REJECTED.code.toString() -> {

                }
                else -> {
                    activity?.finish()
                }
            }
        }

        binding?.layoutBenefitNonAccount?.seeMoreBenefitButton?.setOnClickListener {
            showBottomSheetDetailBenefit()
        }
    }

    private fun onRejected() {
        loadInitImage(getString(R.string.img_url_goto_kyc_status_submission_rejected))

        binding?.apply {
            divider.showWithCondition(isAccountPage)
            layoutBenefitAccount.root.showWithCondition(isAccountPage)
            layoutBenefitAccount.tvTitle.text = getString(R.string.goto_kyc_benefit_account_title_trouble)
            layoutBenefitNonAccount.root.hide()
        }

        binding?.layoutStatusSubmission?.apply {
            tvHeader.text = getString(R.string.goto_kyc_status_rejected_title)
            tvDescription.text = getString(R.string.goto_kyc_status_rejected_subtitle)
            btnPrimary.text = getString(R.string.goto_kyc_status_rejected_button_primary)
            btnSecondary.text = getString(R.string.goto_kyc_status_rejected_button_secondary)
            tvTimer.hide()
            cardReason.showWithCondition(listReason.isNotEmpty())

            listReason.forEachIndexed { index, reason ->
                when(index) {
                    0 -> {
                        icReason1.show()
                        tvReason1.text = reason
                        tvReason1.show()
                    }
                    1 -> {
                        icReason2.show()
                        tvReason2.text = reason
                        tvReason2.show()
                    }
                    2 -> {
                        icReason3.show()
                        tvReason3.text = reason
                        tvReason3.show()
                    }
                    3 -> {
                        icReason4.show()
                        tvReason4.text = reason
                        tvReason4.show()
                    }
                }
            }
        }
    }

    private fun onBlackListed() {
        loadInitImage(getString(R.string.img_url_goto_kyc_status_submission_blacklisted))

        binding?.apply {
            divider.showWithCondition(isAccountPage)
            layoutBenefitAccount.root.showWithCondition(isAccountPage)
            layoutBenefitNonAccount.root.hide()
            layoutBenefitAccount.tvTitle.text = getString(R.string.goto_kyc_benefit_account_title_trouble)
        }

        binding?.layoutStatusSubmission?.apply {
            tvHeader.text = getString(R.string.goto_kyc_status_blacklisted_title)
            tvDescription.text = getString(R.string.goto_kyc_status_blacklisted_subtitle)
            btnPrimary.text = getString(R.string.goto_kyc_status_blacklisted_button)
            btnSecondary.hide()
            tvTimer.hide()
        }
    }

    private fun onVerifiedProgressive() {
        loadInitImage(getString(R.string.img_url_goto_kyc_status_submission_verified))

        binding?.layoutStatusSubmission?.apply {
            tvHeader.text = getString(R.string.goto_kyc_status_verified_title)
            tvDescription.text = getString(R.string.goto_kyc_status_verified_subtitle)
            btnPrimary.text = getString(R.string.goto_kyc_status_verified_button, sourcePage)
            btnSecondary.hide()
            btnPrimary.showWithCondition(!isAccountPage)
            tvTimer.showWithCondition(!isAccountPage)
        }

        binding?.apply {
            layoutBenefitNonAccount.root.hide()
            divider.showWithCondition(isAccountPage)
            layoutBenefitAccount.root.showWithCondition(isAccountPage)
            layoutBenefitAccount.tvTitle.text = getString(R.string.goto_kyc_benefit_account_title_verified)
        }

        if (!isAccountPage) {
            startTimerThenFinish()
        }

    }

    private fun startTimerThenFinish() {
        val maxTimeSecond = 7
        binding?.layoutStatusSubmission?.tvTimer?.text = HtmlCompat.fromHtml(
                getString(R.string.goto_kyc_status_verified_timer, maxTimeSecond.toString()),
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        lifecycleScope.launch(Dispatchers.Default) {
            for (i in 1 .. maxTimeSecond) {
                delay(1000)
                val remainingTime = maxTimeSecond - i
                withContext(Dispatchers.Main) {
                    binding?.layoutStatusSubmission?.tvTimer?.text = HtmlCompat.fromHtml(
                            getString(R.string.goto_kyc_status_verified_timer, remainingTime.toString()),
                            HtmlCompat.FROM_HTML_MODE_COMPACT
                        )
                    if (remainingTime == 0) {
                        activity?.finish()
                    }
                }
            }
        }
    }

    private fun onVerifiedNonProgressive() {
        loadInitImage(getString(R.string.img_url_goto_kyc_status_submission_verified))

        binding?.layoutStatusSubmission?.apply {
            tvHeader.text = getString(R.string.goto_kyc_status_verified_title)
            tvDescription.text = getString(R.string.goto_kyc_status_verified_subtitle)
            btnPrimary.text = getString(R.string.goto_kyc_status_verified_button, sourcePage)
            btnSecondary.hide()
            tvTimer.hide()
        }

        binding?.apply {
            layoutBenefitNonAccount.root.showWithCondition(!isAccountPage)
            layoutBenefitAccount.root.showWithCondition(isAccountPage)
            layoutBenefitAccount.tvTitle.text = getString(R.string.goto_kyc_benefit_account_title_verified)
        }
    }

    private fun onPending24Hours() {
        loadInitImage(getString(R.string.img_url_goto_kyc_status_submission_pending))

        binding?.layoutStatusSubmission?.apply {
            tvHeader.text = getString(R.string.goto_kyc_status_pending_title)
            tvDescription.text = HtmlCompat.fromHtml(
                    getString(R.string.goto_kyc_status_pending_subtitle_24_hours),
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                )
            btnPrimary.text = getString(R.string.goto_kyc_status_pending_button, sourcePage)
            btnSecondary.hide()
            tvTimer.hide()

            btnPrimary.showWithCondition(!isAccountPage)
        }

        binding?.layoutBenefitNonAccount?.root?.showWithCondition(!isAccountPage)
        binding?.layoutBenefitAccount?.root?.showWithCondition(isAccountPage)
        binding?.layoutBenefitAccount?.tvTitle?.text = getString(R.string.goto_kyc_benefit_account_title_pending)
    }

    private fun showBottomSheetDetailBenefit() {
        bottomSheetDetailBenefit = BenefitDetailBottomSheet()
        bottomSheetDetailBenefit?.show(childFragmentManager, TAG_BOTTOM_SHEET_DETAIL_BENEFIT)
    }

    private fun goToTokopediaCare() {
        RouteManager.route(
            context,
            ApplinkConstInternalGlobal.WEBVIEW,
            TokopediaUrl.getInstance().MOBILEWEB.plus(PATH_TOKOPEDIA_CARE)
        )
    }

    private fun loadInitImage(imageUrl: String) {
        binding?.layoutStatusSubmission?.ivStatusSubmission?.loadImageWithoutPlaceholder(imageUrl)
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }

    companion object {
        private val SCREEN_NAME = StatusSubmissionFragment::class.java.simpleName
        private const val PATH_TOKOPEDIA_CARE = "help?lang=id?isBack=true"
        private const val TAG_BOTTOM_SHEET_DETAIL_BENEFIT = "tag bottom sheet detail benefit"
    }
}
