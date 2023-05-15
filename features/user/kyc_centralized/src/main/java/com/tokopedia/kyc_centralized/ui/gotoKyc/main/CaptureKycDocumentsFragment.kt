package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.gojek.kyc.sdk.core.broadcast.KycSdkStatusPublisher
import com.gojek.kyc.sdk.core.constants.UnifiedKycFlowResult
import com.gojek.kyc.sdk.core.extensions.checkSelfPermissionWithTryCatch
import com.gojek.kyc.sdk.core.utils.KycSdkPartner
import com.gojek.onekyc.OneKycSdk
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycFinalLoaderBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk.ProjectIdInterceptor
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CaptureKycDocumentsFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentGotoKycFinalLoaderBinding>()

    @Inject
    lateinit var oneKycSdk: OneKycSdk

    private val args: CaptureKycDocumentsFragmentArgs by navArgs()

    private val interactor = KycSdkStatusPublisher.get()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGotoKycFinalLoaderBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kycStateObserver()
        gotoCaptureKycDocuments()
        initListener()
    }

    private fun initListener() {
        binding?.globalError?.setActionClickListener {
            onUploadingDocuments()
            oneKycSdk.submitKycDocuments()
        }

        binding?.unifyToolbar?.setNavigationOnClickListener {
            activity?.setResult(Activity.RESULT_CANCELED)
            activity?.finish()
        }
    }

    private fun kycStateObserver() {
        interactor.observe(viewLifecycleOwner) {
            if (it != null && it is UnifiedKycFlowResult) {
                interactor.updateConsumed()
                when (it) {
                    UnifiedKycFlowResult.DOC_SUBMITTED -> {
                        //show loader screen
                        onUploadingDocuments()
                    }
                    UnifiedKycFlowResult.FAILURE -> {
                        //show failed page
                        onFailedUploadDocuments()
                    }
                    UnifiedKycFlowResult.SUCCESS -> {
                        // go to success page
                        gotoFinalLoader()
                    }
                    UnifiedKycFlowResult.USER_CANCELLED -> {
                        activity?.setResult(Activity.RESULT_CANCELED)
                        activity?.finish()
                    }
                    UnifiedKycFlowResult.STATE_UPDATED -> {
                        activity?.setResult(Activity.RESULT_CANCELED)
                        activity?.finish()
                    }
                    UnifiedKycFlowResult.DOC_TYPE_SWITCHED -> {
                        activity?.setResult(Activity.RESULT_CANCELED)
                        activity?.finish()
                    }
                }
            }
        }
    }

    private fun gotoCaptureKycDocuments() {
        activity?.let {
            if (checkSelfPermissionWithTryCatch(it, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                activity?.setResult(Activity.RESULT_CANCELED)
                activity?.finish()
            } else {
                oneKycSdk.launchKyc(launchSource = KycSdkPartner.TOKOPEDIA_CORE.name, partner = KycSdkPartner.TOKOPEDIA_CORE, activity = requireActivity())
            }
        }
    }

    private fun onUploadingDocuments() {
        binding?.apply {
            loader.show()
            tvTitle.show()
            tvSubtitle.show()
            unifyToolbar.hide()
            globalError.hide()
        }
    }

    private fun onFailedUploadDocuments() {
        binding?.apply {
            loader.hide()
            tvTitle.hide()
            tvSubtitle.hide()
            unifyToolbar.show()
            globalError.show()
        }
    }

    private fun gotoFinalLoader() {
        val parameter = FinalLoaderParam(
            source = args.parameter.source,
            projectId = args.parameter.projectId,
            gotoKycType = KYCConstant.GotoKycFlow.NON_PROGRESSIVE
        )
        val toFinalLoaderPage = CaptureKycDocumentsFragmentDirections.actionCaptureKycDocumentsFragmentToFinalLoaderFragment(parameter)
        view?.findNavController()?.navigate(toFinalLoaderPage)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }
}
