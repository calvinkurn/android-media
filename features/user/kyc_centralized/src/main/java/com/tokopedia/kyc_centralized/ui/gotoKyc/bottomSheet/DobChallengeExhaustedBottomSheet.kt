package com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils.getFormattedDate
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.databinding.LayoutGotoKycDobChallengeFailedBinding
import com.tokopedia.kyc_centralized.ui.gotoKyc.analytics.GotoKycAnalytics
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class DobChallengeExhaustedBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<LayoutGotoKycDobChallengeFailedBinding>()

    private var projectId = ""
    private var source = ""
    private var cooldownTimeInSeconds = ""
    private var maximumAttemptsAllowed = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            projectId = it.getString(PROJECT_ID).orEmpty()
            source = it.getString(SOURCE).orEmpty()
            cooldownTimeInSeconds = it.getString(COOLDOWN_TIME_IN_SECONDS).orEmpty()
            maximumAttemptsAllowed = it.getString(MAXIMUM_ATTEMPTS_ALLOWED).orEmpty()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutGotoKycDobChallengeFailedBinding.inflate(inflater, container, false)
        overlayClickDismiss = false
        showCloseIcon = false
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GotoKycAnalytics.sendViewDobPageFailed(projectId = projectId)
        setUpView()
        initListener()
    }

    private fun setUpView() {
        binding?.apply {
            ivDobFailed.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_dob_challenge_failed)
            )

            val subtitleMessageFirst = getString(R.string.goto_kyc_dob_challenge_wrong_answer_maximum_attempts_message, maximumAttemptsAllowed)
            val coolDownTimeInLong = cooldownTimeInSeconds.toLongOrNull()
            val subtitleMessageSecond = if (coolDownTimeInLong != null && coolDownTimeInLong >= 0) {
                val currentTime = System.currentTimeMillis()
                val finalDateExhausted = currentTime + coolDownTimeInLong * ONE_SECOND_IN_MILLIS + ONE_MINUTE_IN_MILLIS
                getString(R.string.goto_kyc_dob_challenge_wrong_answer_waiting_date_message, convertDate(finalDateExhausted))
            } else {
                getString(R.string.goto_kyc_dob_challenge_wrong_answer_waiting_seconds_message)
            }
            val description = "$subtitleMessageFirst $subtitleMessageSecond".trim()
            binding?.tvDescription?.text = description
        }
    }

    private fun convertDate(time: Long): String {
        return getFormattedDate(time, FORMAT_DATE)
    }

    private fun initListener() {
        binding?.btnPrimary?.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        private const val PROJECT_ID = "projectId"
        private const val SOURCE = "source"
        private const val ONE_SECOND_IN_MILLIS = 1000
        private const val ONE_MINUTE_IN_MILLIS = 60000
        private const val FORMAT_DATE = "dd MMM yyyy HH:mm"
        private const val COOLDOWN_TIME_IN_SECONDS = "cooldownTimeInSeconds"
        private const val MAXIMUM_ATTEMPTS_ALLOWED = "maximumAttemptsAllowed"

        fun newInstance(projectId: String, source: String, cooldownTimeInSeconds: String, maximumAttemptsAllowed: String) =
            DobChallengeExhaustedBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(PROJECT_ID, projectId)
                    putString(SOURCE, source)
                    putString(COOLDOWN_TIME_IN_SECONDS, cooldownTimeInSeconds)
                    putString(MAXIMUM_ATTEMPTS_ALLOWED, maximumAttemptsAllowed)
                }
            }
    }
}
