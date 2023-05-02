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
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycLoaderBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk.ProjectIdInterceptor
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CaptureKycDocumentsFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentGotoKycLoaderBinding>()

    @Inject
    lateinit var oneKycSdk: OneKycSdk

    @Inject
    lateinit var projectIdInterceptor: ProjectIdInterceptor

    private val args: CaptureKycDocumentsFragmentArgs by navArgs()

    private val interactor = KycSdkStatusPublisher.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectIdInterceptor.setProjectId(args.parameter.projectId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGotoKycLoaderBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kycStateObserver()
        gotoCaptureKycDocuments()
    }

    private fun kycStateObserver() {
        interactor.observe(viewLifecycleOwner) {
            if (it != null && it is UnifiedKycFlowResult) {
                interactor.updateConsumed()
                when (it) {
                    UnifiedKycFlowResult.DOC_SUBMITTED -> {
                        gotoFinalLoader()
                    }
                    UnifiedKycFlowResult.FAILURE -> {
                        //show failed page
                    }
                    else -> {
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
                oneKycSdk.init()
                oneKycSdk.launchKyc(launchSource = KycSdkPartner.TOKOPEDIA_CORE.name, partner = KycSdkPartner.TOKOPEDIA_CORE, activity = requireActivity())
            }
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
