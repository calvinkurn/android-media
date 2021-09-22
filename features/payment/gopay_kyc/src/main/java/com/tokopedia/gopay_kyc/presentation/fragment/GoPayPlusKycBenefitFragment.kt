package com.tokopedia.gopay_kyc.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.gopay_kyc.R
import com.tokopedia.gopay_kyc.analytics.GoPayKycAnalytics
import com.tokopedia.gopay_kyc.analytics.GoPayKycConstants
import com.tokopedia.gopay_kyc.analytics.GoPayKycEvent
import com.tokopedia.gopay_kyc.di.GoPayKycComponent
import com.tokopedia.gopay_kyc.domain.data.GoPayPlusBenefit
import com.tokopedia.gopay_kyc.presentation.activity.GoPayKtpInstructionActivity
import com.tokopedia.gopay_kyc.presentation.viewholder.GoPayPlusBenefitItemViewHolder
import com.tokopedia.gopay_kyc.viewmodel.GoPayKycViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_gopay_benefits_layout.*
import javax.inject.Inject

class GoPayPlusKycBenefitFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var goPayKycAnalytics: dagger.Lazy<GoPayKycAnalytics>

    private val viewModel: GoPayKycViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(GoPayKycViewModel::class.java)
    }
    private val benefitList = ArrayList<GoPayPlusBenefit>()
    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val event = GoPayKycEvent.Click.BackPressEvent(GoPayKycConstants.ScreenNames.GOPAY_KYC_BENEFIT_PAGE)
            sendAnalytics(event)
            activity?.finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        populateBenefits()
        return inflater.inflate(R.layout.fragment_gopay_benefits_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val event = GoPayKycEvent.Impression.OpenScreenEvent(GoPayKycConstants.ScreenNames.GOPAY_KYC_BENEFIT_PAGE)
        sendAnalytics(event)
        initListeners()
        context?.let { setInstructionListView(it) }
    }

    private fun initListeners() {
        setUpOnBackPressed()
        observeViewModel()
        upgradeNowButton.setOnClickListener {
            upgradeKyc()
        }
    }

    private fun setInstructionListView(context: Context) {
        val layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        for (goPayPlusBenefit in benefitList) {
            goPayBenefitLL.addView(
                GoPayPlusBenefitItemViewHolder(context, layoutParams).bindData(goPayPlusBenefit)
            )
        }
    }

    private fun observeViewModel() {
        viewModel.isUpgradeLoading.observe(viewLifecycleOwner, { isLoading ->
            upgradeNowButton.isLoading = isLoading
        })
        viewModel.kycEligibilityStatus.observe(viewLifecycleOwner, { kycStatus ->
            if (kycStatus.isEligible)
                context?.let { it.startActivity(GoPayKtpInstructionActivity.getIntent(it)) }
            else showToastMessage(kycStatus.kycStatusMessage ?: "")
        })
    }

    private fun showToastMessage(message: String) {
        if (message.isNotEmpty())
            Toaster.build(upgradeNowButton, message, Toaster.TYPE_NORMAL, Toaster.LENGTH_LONG)
                .show()
    }

    private fun upgradeKyc() {
        val event =
            GoPayKycEvent.Click.UpgradeKycEvent(GoPayKycConstants.ScreenNames.GOPAY_KYC_BENEFIT_PAGE)
        sendAnalytics(event)
        viewModel.checkKycStatus()
    }

    private fun setUpOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backPressedCallback)
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

    override fun getScreenName() = null
    override fun initInjector() = getComponent(GoPayKycComponent::class.java).inject(this)
    fun sendAnalytics(event: GoPayKycEvent) = goPayKycAnalytics.get().sentKycEvent(event)

    companion object {
        fun newInstance() = GoPayPlusKycBenefitFragment()
    }
}