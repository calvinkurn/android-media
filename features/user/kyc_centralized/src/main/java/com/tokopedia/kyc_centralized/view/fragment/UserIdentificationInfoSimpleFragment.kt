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
        projectId = activity?.intent?.data?.getQueryParameter(
                ApplinkConstInternalGlobal.PARAM_PROJECT_ID).toIntOrZero()
        showWrapperLayout = activity?.intent?.data?.getQueryParameter(
            ApplinkConstInternalGlobal.PARAM_LAYOUT).toBoolean()
        initViews(view)
    }

    private fun initViews(view: View) {
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
                successKyc()
            }
            setupKycBenefitView(view)
        } else {
            loader?.show()
            startKyc()
        }
    }

    private fun setupKycBenefitView(view: View) {
        KycOnBoardingViewInflater.setupKycBenefitToolbar(activity)
        KycOnBoardingViewInflater.setupKycBenefitView(view, mainAction = {
            startKyc()
        }, closeButtonAction = {
            activity?.onBackPressed()
        })
    }

    private fun startKyc() {
        val intent = RouteManager.getIntent(requireContext(),
                ApplinkConstInternalGlobal.USER_IDENTIFICATION_FORM, projectId.toString())
        intent.putExtra("", "")
        startActivityForResult(intent, KYC_REQUEST_CODE)
    }

    private fun successKyc() {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
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
                        successKyc()
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