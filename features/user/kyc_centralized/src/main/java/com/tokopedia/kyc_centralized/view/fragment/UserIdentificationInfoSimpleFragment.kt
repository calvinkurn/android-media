package com.tokopedia.kyc_centralized.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kyc_centralized.KycUrl
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.view.customview.KycOnBoardingViewInflater
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton

class UserIdentificationInfoSimpleFragment: BaseDaggerFragment() {

    private var projectId = 0
    private var mainView: ConstraintLayout? = null
    private var layoutBenefit: View? = null
    private var loader: LoaderUnify? = null
    private var defaultStatusBarColor = 0
    private var showWrapperLayout = false
    private var redirectUrl = ""
    private var kycType = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) defaultStatusBarColor = activity?.window?.statusBarColor ?: 0
        return inflater.inflate(
            R.layout.fragment_user_identification_info_simple, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.intent?.data?.let {
            projectId = it.getQueryParameter(ApplinkConstInternalGlobal.PARAM_PROJECT_ID).toIntOrZero()
            showWrapperLayout = it.getQueryParameter(ApplinkConstInternalGlobal.PARAM_SHOW_INTRO).toBoolean()
            redirectUrl = it.getQueryParameter(ApplinkConstInternalGlobal.PARAM_REDIRECT_URL).orEmpty()
            kycType = it.getQueryParameter(ApplinkConstInternalGlobal.PARAM_KYC_TYPE).orEmpty()
        }

        initViews(view, savedInstanceState)
    }

    private fun initViews(view: View, savedInstanceState: Bundle?) {
        mainView = view.findViewById(R.id.uii_simple_main_view)
        val mainImage: ImageUnify? = view.findViewById(R.id.uii_simple_main_image)
        val button: UnifyButton? = view.findViewById(R.id.uii_simple_button)
        layoutBenefit = view.findViewById(R.id.layout_benefit)
        loader = view.findViewById(R.id.loader)

        if (showWrapperLayout) {
            loader?.hide()
            mainView?.hide()
            layoutBenefit?.show()
            mainImage?.loadImage(KycUrl.ICON_WAITING)
            button?.setOnClickListener { _ ->
                finishAndRedirectKycResult()
            }
            setupKycBenefitView(view)
        } else {
            loader?.show()
            //If savedInstanceState is null, then first time open (solve problem in ONE UI 3.1)
            if (savedInstanceState == null) {
                startKyc()
            }
        }
    }

    private fun setupKycBenefitView(view: View) {
        KycOnBoardingViewInflater.setupKycBenefitToolbar(activity)
        KycOnBoardingViewInflater.setupKycBenefitView(requireActivity(), view, mainAction = {
            startKyc()
        }, closeButtonAction = {
            activity?.onBackPressed()
        }, onCheckedChanged = {})
    }

    private fun startKyc() {
        val intent = RouteManager.getIntent(requireContext(),
                ApplinkConstInternalGlobal.USER_IDENTIFICATION_FORM,
                projectId.toString(),
                kycType,
                redirectUrl
        )
        startActivityForResult(intent, KYC_REQUEST_CODE)
    }

    private fun finishAndRedirectKycResult() {
        activity?.let {
            it.setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(ApplinkConstInternalGlobal.PARAM_REDIRECT_URL, redirectUrl)
            })
            it.finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == KYC_REQUEST_CODE) {
            when(resultCode) {
                Activity.RESULT_OK -> {
                    if (showWrapperLayout) {
                        layoutBenefit?.hide()
                        mainView?.show()
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