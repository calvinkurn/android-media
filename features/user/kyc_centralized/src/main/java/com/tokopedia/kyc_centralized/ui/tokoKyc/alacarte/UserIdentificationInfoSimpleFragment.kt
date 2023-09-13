package com.tokopedia.kyc_centralized.ui.tokoKyc.alacarte

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.common.KycServerLogger
import com.tokopedia.kyc_centralized.common.KycUrl
import com.tokopedia.kyc_centralized.databinding.FragmentUserIdentificationInfoSimpleBinding
import com.tokopedia.kyc_centralized.di.UserIdentificationCommonComponent
import com.tokopedia.kyc_centralized.ui.customview.KycOnBoardingViewInflater
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.FailedSavePreferenceBottomSheet
import com.tokopedia.kyc_centralized.util.KycSharedPreference
import com.tokopedia.media.loader.loadImage
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam
import com.tokopedia.utils.lifecycle.autoClearedNullable
import timber.log.Timber
import javax.inject.Inject

class UserIdentificationInfoSimpleFragment : BaseDaggerFragment() {

    @Inject
    lateinit var kycSharedPreference: KycSharedPreference

    private var viewBinding by autoClearedNullable<FragmentUserIdentificationInfoSimpleBinding>()
    private var projectId = 0
    private var defaultStatusBarColor = 0
    private var showWrapperLayout = false
    private var redirectUrl = ""
    private var kycType = ""
    private var savedInstanceState: Bundle? = null

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

        this.savedInstanceState = savedInstanceState
        saveInitDataToPreference()
    }

    private fun initAction() {
        loadUserConsent()
        if (showWrapperLayout) {
            viewBinding?.loader?.hide()
            viewBinding?.uiiSimpleMainView?.hide()
            viewBinding?.layoutBenefit?.root?.show()
            viewBinding?.uiiSimpleMainImage?.loadImage(KycUrl.ICON_WAITING)
            viewBinding?.uiiSimpleButton?.setOnClickListener { _ ->
                finishAndRedirectKycResult()
            }
            setupKycBenefitView(requireView())
        } else {
            viewBinding?.loader?.show()
            //If savedInstanceState is null, then first time open (solve problem in ONE UI 3.1)
            if (savedInstanceState == null) {
                startKyc()
            }
        }
    }

    private fun saveInitDataToPreference() {
        viewBinding?.loader?.show()
        val isSuccessSavePreference = kycSharedPreference.saveStringCache(
            key = KYCConstant.SharedPreference.KEY_KYC_FLOW_TYPE,
            value = KYCConstant.SharedPreference.VALUE_KYC_FLOW_TYPE_ALA_CARTE
        )

        KycServerLogger.sendLogStatusSavePreferenceKyc(
            flow = KycServerLogger.FLOW_ALA_CARTE,
            isSuccess = isSuccessSavePreference
        )

        if (isSuccessSavePreference) {
            initAction()
        } else {
            viewBinding?.loader?.hide()
            showFailedSavePreferenceBottomSheet()
        }
    }

    private fun showFailedSavePreferenceBottomSheet() {
        val failedSavePreferenceBottomSheet = FailedSavePreferenceBottomSheet()

        failedSavePreferenceBottomSheet.show(
            childFragmentManager,
            TAG_BOTTOM_SHEET_FAILED_SAVE_PREFERENCE
        )

        failedSavePreferenceBottomSheet.setOnDismissWithDataListener { isReload ->
            if (isReload) {
                saveInitDataToPreference()
            } else {
                activity?.setResult(Activity.RESULT_CANCELED)
                activity?.finish()
            }
        }
    }

    private fun loadUserConsent() {
        val consentParam = ConsentCollectionParam(
            collectionId = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
                KYCConstant.consentCollectionIdStaging
            } else {
                KYCConstant.consentCollectionIdProduction
           }
        )
        viewBinding?.layoutBenefit?.userConsentKyc?.load(consentParam)

        viewBinding?.layoutBenefit?.kycBenefitBtn?.setOnClickListener {
            viewBinding?.layoutBenefit?.userConsentKyc?.submitConsent()
            startKyc()
        }

        viewBinding?.layoutBenefit?.userConsentKyc?.setOnCheckedChangeListener {isChecked ->
            viewBinding?.layoutBenefit?.kycBenefitBtn?.isEnabled = isChecked
        }
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
            kycSharedPreference.removeStringCache(KYCConstant.SharedPreference.KEY_KYC_FLOW_TYPE)
            it.setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(PARAM_REDIRECT_URL, redirectUrl)
            })
            Timber.d("redirectUrlFinal=$redirectUrl")
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
                    kycSharedPreference.removeStringCache(KYCConstant.SharedPreference.KEY_KYC_FLOW_TYPE)
                    activity?.setResult(resultCode)
                    activity?.finish()
                }
            }
        }
    }

    override fun getScreenName(): String = TAG
    override fun initInjector() {
        getComponent(UserIdentificationCommonComponent::class.java).inject(this)
    }

    companion object {
        private const val TAG = "UserIdentificationInfoSimpleFragment"
        private const val KYC_REQUEST_CODE = 9902
        private const val TAG_BOTTOM_SHEET_FAILED_SAVE_PREFERENCE = "bottom_sheet_failed_save_preference"
    }
}
