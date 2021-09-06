package com.tokopedia.gopay_kyc.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.gopay_kyc.R
import com.tokopedia.gopay_kyc.domain.data.GoPayPlusBenefit
import com.tokopedia.gopay_kyc.presentation.viewholder.GoPayPlusBenefitItemViewHolder
import kotlinx.android.synthetic.main.fragment_gopay_benefits_layout.*

class GoPayPlusKycBenefitFragment : TkpdBaseV4Fragment() {

    private val benefitList = ArrayList<GoPayPlusBenefit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        populateBenefits()
    }

    private fun populateBenefits() {
        context?.let {
            benefitList.add(
                GoPayPlusBenefit(
                    R.drawable.ic_gopay_wallet,
                    getString(R.string.gopay_kyc_wallet_benefit_title_text),
                    getString(R.string.gopay_kyc_wallet_benefit_description_text)
                )
            )
            benefitList.add(
                GoPayPlusBenefit(
                    R.drawable.ic_gopay_atm_benefit,
                    getString(R.string.gopay_kyc_atm_benefit_title_text),
                    getString(R.string.gopay_kyc_atm_benefit_description_text)
                )
            )
            benefitList.add(
                GoPayPlusBenefit(
                    R.drawable.ic_gopay_transfer_money,
                    getString(R.string.gopay_kyc_instant_debit_benefit_title_text),
                    getString(R.string.gopay_kyc_instant_debit_benefit_description_text)
                )
            )
            benefitList.add(
                GoPayPlusBenefit(
                    R.drawable.ic_gopay_friends,
                    getString(R.string.gopay_kyc_friends_benefit_title_text),
                    getString(R.string.gopay_kyc_friends_benefit_description_text)
                )
            )
            benefitList.add(
                GoPayPlusBenefit(
                    R.drawable.ic_gopay_guarantee,
                    getString(R.string.gopay_kyc_payback_benefit_title_text),
                    getString(R.string.gopay_kyc_payback_benefit_description_text)
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gopay_benefits_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        upgradeNowButton.setOnClickListener {

        }
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        context?.let {
            for (goPayPlusBenefit in benefitList) {
                goPayBenefitLL.addView(
                    GoPayPlusBenefitItemViewHolder(
                        it,
                        layoutParams
                    ).bindData(goPayPlusBenefit)
                )
            }
        }
    }

    override fun getScreenName() = null

    companion object {
        fun newInstance() = GoPayPlusKycBenefitFragment()
    }
}