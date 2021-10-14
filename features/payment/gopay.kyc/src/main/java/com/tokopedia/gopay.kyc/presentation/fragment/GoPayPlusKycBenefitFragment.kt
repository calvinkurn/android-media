package com.tokopedia.gopay.kyc.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.gopay.kyc.R
import com.tokopedia.gopay.kyc.analytics.GoPayKycAnalytics
import com.tokopedia.gopay.kyc.analytics.GoPayKycConstants
import com.tokopedia.gopay.kyc.analytics.GoPayKycEvent
import com.tokopedia.gopay.kyc.di.GoPayKycComponent
import com.tokopedia.gopay.kyc.domain.data.GoPayPlusBenefit
import com.tokopedia.gopay.kyc.presentation.activity.GoPayKtpInstructionActivity
import com.tokopedia.gopay.kyc.presentation.fragment.base.GoPayKycBaseFragment
import com.tokopedia.gopay.kyc.presentation.viewholder.GoPayPlusBenefitItemViewHolder
import com.tokopedia.gopay.kyc.viewmodel.GoPayKycViewModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_gopay_benefits_layout.*
import javax.inject.Inject

class GoPayPlusKycBenefitFragment : GoPayKycBaseFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var goPayKycAnalytics: dagger.Lazy<GoPayKycAnalytics>

    private val viewModel: GoPayKycViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(GoPayKycViewModel::class.java)
    }
    private val benefitList = ArrayList<GoPayPlusBenefit>()

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
        initListeners()
        context?.let { setInstructionListView(it) }
    }

    private fun initListeners() {
        observeViewModel()
        upgradeNowButton.setOnClickListener {
            upgradeToGoPayPlus()
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
        viewModel.errorLiveData.observe(viewLifecycleOwner, {
            showToastMessage(ErrorHandler.getErrorMessage(context, it), Toaster.TYPE_ERROR)
        })
    }

    private fun showToastMessage(message: String, toastType: Int = Toaster.TYPE_NORMAL) {
        if (message.isNotEmpty())
            Toaster.build(upgradeNowButton, message, Toaster.LENGTH_LONG, toastType)
                .show()
    }

    private fun upgradeToGoPayPlus() {
        val event = GoPayKycEvent.Click.UpgradeKycEvent(
            GoPayKycConstants.ScreenNames.GOPAY_KYC_BENEFIT_PAGE
        )
        sendAnalytics(event)
        viewModel.checkKycStatus()
    }

    private fun populateBenefits() {
        benefitList.apply {
            add(
                GoPayPlusBenefit(
                    R.drawable.ic_gopay_wallet,
                    getString(R.string.gopay_kyc_wallet_benefit_title_text),
                    getString(R.string.gopay_kyc_wallet_benefit_description_text)
                )
            )
            add(
                GoPayPlusBenefit(
                    R.drawable.ic_gopay_atm_benefit,
                    getString(R.string.gopay_kyc_atm_benefit_title_text),
                    getString(R.string.gopay_kyc_atm_benefit_description_text)
                )
            )
            add(
                GoPayPlusBenefit(
                    R.drawable.ic_gopay_transfer_money,
                    getString(R.string.gopay_kyc_instant_debit_benefit_title_text),
                    getString(R.string.gopay_kyc_instant_debit_benefit_description_text)
                )
            )
            add(
                GoPayPlusBenefit(
                    R.drawable.ic_gopay_friends,
                    getString(R.string.gopay_kyc_friends_benefit_title_text),
                    getString(R.string.gopay_kyc_friends_benefit_description_text)
                )
            )
            add(
                GoPayPlusBenefit(
                    R.drawable.ic_gopay_guarantee,
                    getString(R.string.gopay_kyc_payback_benefit_title_text),
                    getString(R.string.gopay_kyc_payback_benefit_description_text)
                )
            )
        }
    }

    fun sendAnalytics(event: GoPayKycEvent) = goPayKycAnalytics.get().sentKycEvent(event)
    override fun getScreenName() = null
    override fun initInjector() = getComponent(GoPayKycComponent::class.java).inject(this)

    override fun handleBackPressForGopay() {
        sendAnalytics(GoPayKycEvent.Click.BackPressEvent(GoPayKycConstants.ScreenNames.GOPAY_KYC_BENEFIT_PAGE))
        activity?.finish()
    }

    override fun sendOpenScreenGopayEvent() {
        sendAnalytics(GoPayKycEvent.Impression.OpenScreenEvent(GoPayKycConstants.ScreenNames.GOPAY_KYC_BENEFIT_PAGE))
    }

    companion object {
        fun newInstance() = GoPayPlusKycBenefitFragment()
    }
}