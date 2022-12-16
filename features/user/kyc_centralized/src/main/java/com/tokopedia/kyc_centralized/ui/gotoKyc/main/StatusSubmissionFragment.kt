package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }

    private fun onRejected() {
        binding?.apply {
            divider.hide()
            layoutBenefit.root.hide()
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
        binding?.apply {
            divider.hide()
            layoutBenefit.root.hide()
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
        binding?.layoutStatusSubmission?.apply {
            tvHeader.text = getString(R.string.goto_kyc_status_verified_title)
            tvDescription.text = getString(R.string.goto_kyc_status_verified_subtitle)
            btnPrimary.text = getString(R.string.goto_kyc_status_verified_button, sourcePage)
            btnSecondary.hide()
            startTimerThenFinish()
        }
    }

    private fun startTimerThenFinish() {
        val maxTimeSecond = 7
        binding?.layoutStatusSubmission?.tvTimer?.text = getString(R.string.goto_kyc_status_verified_timer, maxTimeSecond.toString())
        lifecycleScope.launch(Dispatchers.Default) {
            for (i in 1 .. maxTimeSecond) {
                delay(1000)
                val remainingTime = maxTimeSecond - i
                withContext(Dispatchers.Main) {
                    binding?.layoutStatusSubmission?.tvTimer?.text = getString(R.string.goto_kyc_status_verified_timer, remainingTime.toString())
                    if (remainingTime == 0) {
                        activity?.finish()
                    }
                }
            }
        }
    }

    private fun onVerifiedNonProgressive() {
        binding?.layoutStatusSubmission?.apply {
            tvHeader.text = getString(R.string.goto_kyc_status_verified_title)
            tvDescription.text = getString(R.string.goto_kyc_status_verified_subtitle)
            btnPrimary.text = getString(R.string.goto_kyc_status_verified_button, sourcePage)
            btnSecondary.hide()
            tvTimer.hide()
        }
    }

    private fun onPending24Hours() {
        binding?.layoutStatusSubmission?.apply {
            tvHeader.text = getString(R.string.goto_kyc_status_pending_title)
            tvDescription.text = getString(R.string.goto_kyc_status_pending_subtitle_24_hours)
            btnPrimary.text = getString(R.string.goto_kyc_status_pending_button, sourcePage)
            btnSecondary.hide()
            tvTimer.hide()
        }
    }

    private fun goToTokopediaCare() {
        RouteManager.route(
            context,
            ApplinkConstInternalGlobal.WEBVIEW,
            TokopediaUrl.getInstance().MOBILEWEB.plus(PATH_TOKOPEDIA_CARE)
        )
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }

    companion object {
        private val SCREEN_NAME = StatusSubmissionFragment::class.java.simpleName
        private const val PATH_TOKOPEDIA_CARE = "help?lang=id?isBack=true"
    }
}
