package com.tokopedia.kyc_centralized.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_KYC_TYPE
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_REDIRECT_URL
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_SHOW_INTRO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kyc_centralized.KycUrl
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.databinding.FragmentUserIdentificationInfoSimpleBinding
import com.tokopedia.kyc_centralized.view.customview.KycOnBoardingViewInflater
import com.tokopedia.media.loader.loadImage
import com.tokopedia.usercomponents.userconsent.common.UserConsentPayload
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam
import com.tokopedia.usercomponents.userconsent.ui.UserConsentActionListener
import com.tokopedia.utils.lifecycle.autoClearedNullable

class UserIdentificationInfoSimpleFragment : BaseDaggerFragment() {

    private var viewBinding by autoClearedNullable<FragmentUserIdentificationInfoSimpleBinding>()
    private var projectId = 0
    private var defaultStatusBarColor = 0
    private var showWrapperLayout = false
    private var redirectUrl = ""
    private var kycType = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentUserIdentificationInfoSimpleBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.intent?.data?.let {
            projectId = it.getQueryParameter(PARAM_PROJECT_ID).toIntOrZero()
            showWrapperLayout = it.getQueryParameter(PARAM_SHOW_INTRO).toBoolean()
            redirectUrl = it.getQueryParameter(PARAM_REDIRECT_URL).orEmpty()
            kycType = it.getQueryParameter(PARAM_KYC_TYPE).orEmpty()
        }

        if (kycType.isEmpty()) {
            kycType = arguments?.getString(PARAM_KYC_TYPE).orEmpty()
        }

        loadUserConsent()
        if (showWrapperLayout) {
            viewBinding?.loader?.hide()
            viewBinding?.uiiSimpleMainView?.hide()
            viewBinding?.layoutBenefit?.root?.show()
            viewBinding?.uiiSimpleMainImage?.loadImage(KycUrl.ICON_WAITING)
            viewBinding?.uiiSimpleButton?.setOnClickListener { _ ->
                finishAndRedirectKycResult()
            }
            setupKycBenefitView(view)
        } else {
            viewBinding?.loader?.show()
            //If savedInstanceState is null, then first time open (solve problem in ONE UI 3.1)
            if (savedInstanceState == null) {
                startKyc()
            }
        }
    }

    private fun loadUserConsent() {
        val consentParam = ConsentCollectionParam(
            collectionId = KYCConstant.consentCollectionId,
            version = KYCConstant.consentVersion
        )
        viewBinding?.layoutBenefit?.userConsentKyc?.load(
            viewLifecycleOwner, this, consentParam, object : UserConsentActionListener {
                override fun onCheckedChange(isChecked: Boolean) { }

                override fun onActionClicked(payload: UserConsentPayload, isDefaultTemplate: Boolean) {
                    startKyc()
                }

                override fun onFailed(throwable: Throwable) {
                    Toast.makeText(context, throwable.message.orEmpty(), Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    private fun setupKycBenefitView(view: View) {
        KycOnBoardingViewInflater.setupKycBenefitToolbar(activity)
        KycOnBoardingViewInflater.setupKycBenefitView(requireActivity(), view, closeButtonAction = {
            activity?.onBackPressed()
        })
    }

    private fun startKyc() {
        val intent = RouteManager.getIntent(
            requireContext(),
            ApplinkConstInternalUserPlatform.KYC_FORM,
            projectId.toString(),
            redirectUrl
        )
        intent.putExtra(PARAM_KYC_TYPE, kycType)
        startActivityForResult(intent, KYC_REQUEST_CODE)
    }

    private fun finishAndRedirectKycResult() {
        activity?.let {
            it.setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(PARAM_REDIRECT_URL, redirectUrl)
            })
            it.finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == KYC_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    if (showWrapperLayout) {
                        viewBinding?.layoutBenefit?.root?.hide()
                        viewBinding?.uiiSimpleMainView?.show()
                        KycOnBoardingViewInflater.restoreStatusBar(activity, defaultStatusBarColor)
                    } else {
                        finishAndRedirectKycResult()
                    }
                }
                else -> {
                    activity?.setResult(resultCode)
                    activity?.finish()
                }
            }
        }
    }

    override fun getScreenName(): String = TAG
    override fun initInjector() {}

    companion object {
        private const val TAG = "UserIdentificationInfoSimpleFragment"
        private const val KYC_REQUEST_CODE = 9902
    }
}
