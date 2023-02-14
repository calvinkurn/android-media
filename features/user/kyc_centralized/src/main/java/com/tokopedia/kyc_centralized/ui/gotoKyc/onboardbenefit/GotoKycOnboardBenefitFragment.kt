package com.tokopedia.kyc_centralized.ui.gotoKyc.onboardbenefit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycOnboardBenefitBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.utils.lifecycle.autoClearedNullable

class GotoKycOnboardBenefitFragment: BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentGotoKycOnboardBenefitBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGotoKycOnboardBenefitBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadInitImage()
        initListener()
    }

    private fun loadInitImage() {
        binding?.ivOnboardBenefit?.loadImageWithoutPlaceholder(
            getString(R.string.img_url_goto_kyc_onboard_benefit)
        )
    }

    private fun initListener() {
        binding?.btnVerification?.setOnClickListener {
            directToGotoKyc()
        }

        binding?.unifyToolbar?.setNavigationOnClickListener { activity?.finish() }
    }

    private fun directToGotoKyc() {
        val projectId = activity?.intent?.extras?.getString(ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID)
        val source = activity?.intent?.extras?.getString(ApplinkConstInternalUserPlatform.PARAM_SOURCE)
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.GOTO_KYC)
        intent.putExtra(ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID, projectId)
        intent.putExtra(ApplinkConstInternalUserPlatform.PARAM_SOURCE, source)
        startActivity(intent)
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }

    companion object {
        private val SCREEN_NAME = GotoKycOnboardBenefitFragment::class.java.simpleName

        fun createInstance(): Fragment = GotoKycOnboardBenefitFragment()
    }
}
