package com.tokopedia.gopay.kyc.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.gopay.kyc.presentation.GoPayKycBenefitAdapter
import com.tokopedia.gopay.kyc.R
import com.tokopedia.gopay.kyc.analytics.GoPayKycAnalytics
import com.tokopedia.gopay.kyc.analytics.GoPayKycConstants
import com.tokopedia.gopay.kyc.analytics.GoPayKycEvent
import com.tokopedia.gopay.kyc.di.GoPayKycComponent
import com.tokopedia.gopay.kyc.domain.data.GoPayPlusBenefit
import com.tokopedia.gopay.kyc.presentation.GoPayKycBenefitFactory
import com.tokopedia.gopay.kyc.presentation.activity.GoPayKtpInstructionActivity
import com.tokopedia.gopay.kyc.presentation.fragment.base.GoPayKycBaseFragment
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
    private val benefitList = ArrayList<Visitable<*>>()

    private fun getAdapterTypeFactory(): GoPayKycBenefitFactory = GoPayKycBenefitFactory()

    private val goPayKycBenefitAdapter: GoPayKycBenefitAdapter by lazy {
        GoPayKycBenefitAdapter(getAdapterTypeFactory())
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
        setHeaderBackground()
        setUpGoPayBenefitRecyclerView()
        initListeners()
    }

    private fun setHeaderBackground(){
       goPayHeaderBackground.setImageUrl(GOPAY_HEADER_BACKGROUND_IMAGE)
    }

    private fun initListeners() {
        observeViewModel()
        upgradeNowButton.setOnClickListener {
            upgradeToGoPayPlus()
        }
    }

    private fun imageUrl(){

    }

    private fun setUpGoPayBenefitRecyclerView() {
        goPayBenefitRV.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = goPayKycBenefitAdapter
            goPayKycBenefitAdapter.addAllElements(benefitList)
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
                EmptyModel()
            )
            add(
                GoPayPlusBenefit(
                    getString(R.string.gopay_kyc_limit_balance_title_text),
                    getString(R.string.gopay_kyc_limit_balance_gopay_text),
                    getString(R.string.gopay_kyc_limit_balance_gopay_plus_text),
                )
            )
            add(
                GoPayPlusBenefit(
                    getString(R.string.gopay_kyc_limit_transaction_per_month_title_text),
                    getString(R.string.gopay_kyc_limit_transaction_per_month_gopay_text),
                    getString(R.string.gopay_kyc_limit_transaction_per_month_gopay_plus_text),
                )
            )
            add(
                GoPayPlusBenefit(
                    getString(R.string.gopay_kyc_transfer_to_fellow_gopay_title_text),
                    "",
                    ""
                )
            )
            add(
                GoPayPlusBenefit(
                    getString(R.string.gopay_kyc_transfer_to_bank_title_text),
                    "",
                    ""
                )
            )
            add(
                GoPayPlusBenefit(
                    getString(R.string.gopay_kyc_guarantee_balance_return_title_text),
                    "",
                    "",
                    isLastItem = true
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
        private const val GOPAY_HEADER_BACKGROUND_IMAGE="https://images.tokopedia.net/img/android/gopay_benefit_page/ic_gopay_benefit_blue_background.png"
    }
}