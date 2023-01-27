package com.tokopedia.kyc_centralized.ui.gotoKyc.transparent

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

class OnboardNonProgressiveBottomSheet(private val source: String = "", private val isAccountLinked: Boolean, private val isKtpTaken: Boolean): BottomSheetUnify() {

    private var binding by autoClearedNullable<LayoutGotoKycOnboardNonProgressiveBinding>()

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
        initView()
    }

    private fun initView() {
        binding?.apply {
            tvTitle.text = if (source.isEmpty()) {
                getString(R.string.goto_kyc_onboard_non_progressive_title_account_page)
            } else {
                getString(R.string.goto_kyc_onboard_non_progressive_title_not_account_page, source)
            }

            layoutAccountLinking.imgItemOnboard.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_onboard_account_linking)
            )
            layoutKtp.imgItemOnboard.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_onboard_ktp)
            )
            layoutSelfie.imgItemOnboard.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_onboard_selfie)
            )

            layoutAccountLinking.tvItemTitle.text = getString(R.string.goto_kyc_onboard_non_progressive_item_account_linking_title)
            if (isAccountLinked) {
                layoutAccountLinking.tvItemTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.ic_checklist_circle_green, 0)
            }

            layoutAccountLinking.tvItemSubtitle.text = if (isAccountLinked) {
                getString(R.string.goto_kyc_onboard_non_progressive_item_account_linking_subtitle_linked)
            } else {
                getString(R.string.goto_kyc_onboard_non_progressive_item_account_linking_subtitle_unlinked)
            }

            layoutKtp.tvItemTitle.text = getString(R.string.goto_kyc_onboard_non_progressive_item_ktp_title)
            if (isKtpTaken) {
                layoutKtp.tvItemTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.ic_checklist_circle_green, 0)
                layoutKtp.tvShowFile.show()
            }

            layoutKtp.tvItemSubtitle.text = if (isKtpTaken) {
                getString(R.string.goto_kyc_onboard_non_progressive_item_ktp_subtitle_taken)
            } else {
                getString(R.string.goto_kyc_onboard_non_progressive_item_ktp_subtitle_not_taken)
            }

            layoutSelfie.tvItemTitle.text = getString(R.string.goto_kyc_onboard_non_progressive_item_selfie_title)
            layoutSelfie.tvItemSubtitle.text = getString(R.string.goto_kyc_onboard_non_progressive_item_selfie_subtitle)
        }
    }

}
