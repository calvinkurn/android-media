package com.tokopedia.kyc_centralized.ui.customview

import com.tokopedia.imageassets.ImageUrl

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
        private const val URL_REFERRAL = ImageUrl.URL_REFERRAL
        private const val URL_AFFILIATE = ImageUrl.URL_AFFILIATE
        private const val URL_BANK_ACCOUNT = ImageUrl.URL_BANK_ACCOUNT
        private const val URL_PRODUCT_DEWASA = ImageUrl.URL_PRODUCT_DEWASA
        private const val URL_PAY_LATER = ImageUrl.URL_PAY_LATER
        private const val URL_MITRA = ImageUrl.URL_MITRA
        private const val URL_POWER_MERCHANT = ImageUrl.URL_POWER_MERCHANT
        private const val URL_MODAL_TOKO = ImageUrl.URL_MODAL_TOKO

        fun createInstance(): KycBenefitDetailBottomSheet {
            return KycBenefitDetailBottomSheet()
        }
    }
}
