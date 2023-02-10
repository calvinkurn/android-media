package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.databinding.LayoutGotoKycOnboardNonProgressiveBinding
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class OnboardNonProgressiveBottomSheet(private val title: String, private val isAccountLinked: Boolean, private val isKtpTaken: Boolean): BottomSheetUnify() {

    private var binding by autoClearedNullable<LayoutGotoKycOnboardNonProgressiveBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutGotoKycOnboardNonProgressiveBinding.inflate(inflater, container, false)
        setTitle(title)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewAccountLinking()
        setUpViewKtp()
        setUpViewSelfie()
    }

    private fun setUpViewAccountLinking() {
        binding?.layoutAccountLinking?.apply {
            imgItemOnboard.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_onboard_account_linking)
            )

            if (isAccountLinked) {
                tvItemTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_checklist_circle_green, 0)
            }

            tvItemSubtitle.text = if (isAccountLinked) {
                getString(R.string.goto_kyc_onboard_non_progressive_item_account_linking_subtitle_linked)
            } else {
                getString(R.string.goto_kyc_onboard_non_progressive_item_account_linking_subtitle_unlinked)
            }
        }
    }

    private fun setUpViewKtp() {
        binding?.layoutKtp?.apply {
            imgItemOnboard.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_onboard_ktp)
            )

            if (isKtpTaken) {
                tvItemTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_checklist_circle_green, 0)
                tvShowFile.show()
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
        }
    }

}
