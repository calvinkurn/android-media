package com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.databinding.LayoutGotoKycOnboardNonProgressiveBinding
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycMainActivity
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycMainParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycRouterFragment
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class OnboardNonProgressiveBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<LayoutGotoKycOnboardNonProgressiveBinding>()

    private var projectId = ""
    private var source = ""
    private var isAccountLinked = false
    private var isKtpTaken = false
    private var isSelfieTaken = false

    private val startKycForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode != KYCConstant.ActivityResult.ACCOUNT_NOT_LINKED) {
            binding?.layoutAccountLinking?.root?.hide()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            projectId = it.getString(PROJECT_ID).orEmpty()
            source = it.getString(SOURCE).orEmpty()
            isAccountLinked = it.getBoolean(ACCOUNT_LINKED)
            isKtpTaken = it.getBoolean(IS_KTP_TAKEN)
            isSelfieTaken = it.getBoolean(IS_SELFIE_TAKEN)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutGotoKycOnboardNonProgressiveBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewAccountLinking()
        setUpViewKtp()
        setUpViewSelfie()
        initListener()
    }

    private fun initListener() {
        binding?.btnSubmit?.setOnClickListener {
            if (isAccountLinked or (binding?.layoutAccountLinking?.root?.isShown == false)) {
                //TODO goto take picture or KTP
            } else {
                val parameter = GotoKycMainParam(
                    projectId = projectId,
                    sourcePage = source
                )
                gotoBridgingAccountLinking(parameter)
            }
        }
    }

    private fun setUpViewAccountLinking() {
        binding?.layoutAccountLinking?.apply {
            if (!isAccountLinked) {
                imgItemOnboard.loadImageWithoutPlaceholder(
                    getString(R.string.img_url_goto_kyc_onboard_account_linking)
                )

                tvItemTitle.text = getString(R.string.goto_kyc_onboard_non_progressive_item_account_linking_title)

                tvItemSubtitle.text = getString(R.string.goto_kyc_onboard_non_progressive_item_account_linking_subtitle_unlinked)
            }
            root.showWithCondition(!isAccountLinked)
        }

    }

    private fun setUpViewKtp() {
        binding?.layoutKtp?.apply {
            imgItemOnboard.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_onboard_ktp)
            )

            tvItemTitle.text = getString(R.string.goto_kyc_onboard_non_progressive_item_ktp_title)
            if (isKtpTaken) {
                tvItemTitle.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_checklist_circle_green,
                    0
                )
            }

            tvItemSubtitle.text = if (isKtpTaken) {
                getString(R.string.goto_kyc_onboard_non_progressive_item_ktp_subtitle_taken)
            } else {
                getString(R.string.goto_kyc_onboard_non_progressive_item_ktp_subtitle_not_taken)
            }
        }
    }

    private fun setUpViewSelfie() {
        binding?.layoutSelfie?.apply {
            imgItemOnboard.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_onboard_selfie)
            )

            tvItemTitle.text = getString(R.string.goto_kyc_onboard_non_progressive_item_selfie_title)
            if (isSelfieTaken) {
                tvItemTitle.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_checklist_circle_green,
                    0
                )
            }

            tvItemSubtitle.text = if (isSelfieTaken) {
                getString(R.string.goto_kyc_onboard_non_progressive_item_selfie_subtitle_taken)
            } else {
                getString(R.string.goto_kyc_onboard_non_progressive_item_selfie_subtitle_not_taken)
            }
        }
    }

    private fun gotoBridgingAccountLinking(parameter: GotoKycMainParam) {
        val intent = Intent(activity, GotoKycMainActivity::class.java)
        intent.putExtra(GotoKycRouterFragment.PARAM_REQUEST_PAGE, GotoKycRouterFragment.PAGE_BRIDGING_ACCOUNT_LINKING)
        intent.putExtra(GotoKycRouterFragment.PARAM_DATA, parameter)
        startKycForResult.launch(intent)
    }

    companion object {
        private const val PROJECT_ID = "project_id"
        private const val SOURCE = "source"
        private const val ACCOUNT_LINKED = "account_linked"
        private const val IS_KTP_TAKEN = "is_ktp_taken"
        private const val IS_SELFIE_TAKEN = "is_selfie_taken"

        fun newInstance(projectId: String, source: String = "", isAccountLinked: Boolean, isKtpTaken: Boolean, isSelfieTaken: Boolean = false) =
            OnboardNonProgressiveBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(PROJECT_ID, projectId)
                    putString(SOURCE, source)
                    putBoolean(ACCOUNT_LINKED, isAccountLinked)
                    putBoolean(IS_KTP_TAKEN, isKtpTaken)
                    putBoolean(IS_SELFIE_TAKEN, isSelfieTaken)
                }
            }
    }
}
