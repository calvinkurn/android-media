package com.tokopedia.kyc_centralized.ui.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.databinding.LayoutDetailKycBenefitBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

class KycBenefitDetailBottomSheet: BottomSheetUnify() {

    private var viewBinding: LayoutDetailKycBenefitBinding? = null

    init {
        showCloseIcon = true
        isDragable = false
        showKnob = false
        clearContentPadding = true
        bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = LayoutDetailKycBenefitBinding.inflate(layoutInflater, container, false)
        setChild(viewBinding?.root)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(getString(R.string.kyc_benefit_detail_title))

        viewBinding?.apply {
            benefitReferral.iconUrl = URL_REFERRAL
            benefitAdultProduct.iconUrl = URL_PRODUCT_DEWASA
            benefitOpenBankAccount.iconUrl = URL_BANK_ACCOUNT
            benefitAffiliate.iconUrl = URL_AFFILIATE
            benefitPayLater.iconUrl = URL_PAY_LATER
            benefitRegisterMitra.iconUrl = URL_MITRA
            benefitUpgradeMerchantStatus.iconUrl = URL_POWER_MERCHANT
            benefitApplyForFund.iconUrl = URL_MODAL_TOKO
        }
    }

    companion object {
        const val TAG = "KycBenefitDetailBottomSheet"
        private const val URL_REFERRAL = "https://images.tokopedia.net/img/android/user/kyc/ic_kyc_benefit_referral.png"
        private const val URL_AFFILIATE = "https://images.tokopedia.net/img/android/user/kyc/ic_kyc_benefit_affiliatte.png"
        private const val URL_BANK_ACCOUNT = "https://images.tokopedia.net/img/android/user/kyc/ic_kyc_benefit_cc.png"
        private const val URL_PRODUCT_DEWASA = "https://images.tokopedia.net/img/android/user/kyc/ic_kyc_benefit_porudct_dewasa.png"
        private const val URL_PAY_LATER = "https://images.tokopedia.net/img/android/user/kyc/ic_kyc_benefit_pinjaman_online.png"
        private const val URL_MITRA = "https://images.tokopedia.net/img/android/user/kyc/ic_kyc_benefit_mitra.png"
        private const val URL_POWER_MERCHANT = "https://images.tokopedia.net/img/android/user/kyc/ic_kyc_benefit_power_merchant.png"
        private const val URL_MODAL_TOKO = "https://images.tokopedia.net/img/android/user/kyc/ic_kyc_benefit_modal_toko.png"

        fun createInstance(): KycBenefitDetailBottomSheet {
            return KycBenefitDetailBottomSheet()
        }
    }
}
