package com.tokopedia.kyc_centralized.ui.gotoKyc.main.capture

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.gojek.kyc.sdk.core.broadcast.KycSdkStatusPublisher
import com.gojek.kyc.sdk.core.constants.UnifiedKycFlowResult
import com.gojek.kyc.sdk.core.utils.KycSdkPartner
import com.gojek.OneKycSdk
import com.gojek.kyc.plus.OneKycConstants
import com.gojek.kyc.plus.getKycSdkDocumentDirectoryPath
import com.gojek.kyc.plus.getKycSdkFrameDirectoryPath
import com.gojek.kyc.plus.getKycSdkLogDirectoryPath
import com.gojek.kyc.sdk.core.constants.OneKycResumeState
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycFinalLoaderBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.kyc_centralized.ui.gotoKyc.analytics.GotoKycAnalytics
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.submit.FinalLoaderParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.utils.isConnectionAvailable
import com.tokopedia.kyc_centralized.ui.gotoKyc.utils.removeGotoKycImage
import com.tokopedia.kyc_centralized.ui.gotoKyc.utils.removeGotoKycPreference
import com.tokopedia.kyc_centralized.ui.gotoKyc.worker.GotoKycCleanupStorageWorker
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CaptureKycDocumentsFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentGotoKycFinalLoaderBinding>()

    @Inject
    lateinit var oneKycSdk: OneKycSdk

    private val args: CaptureKycDocumentsFragmentArgs by navArgs()

    private val interactor = KycSdkStatusPublisher.get()

    private var errorMessage = ""

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
            GotoKycAnalytics.sendClickOnButtonKirimUlangErrorPageEvent(
                errorMessage = errorMessage,
                projectId = args.parameter.projectId
            )
            onUploadingDocuments()
            oneKycSdk.submitKycDocuments()
        }

        binding?.globalError?.setSecondaryActionClickListener {
            val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(intent)
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
                        GotoKycCleanupStorageWorker.cancelWorker(requireContext())
                        // go to success page
                        gotoFinalLoader()
                    }
                    UnifiedKycFlowResult.USER_CANCELLED -> {
                        finishWithResultCancelAndRemoveCache()
                    }
                    UnifiedKycFlowResult.STATE_UPDATED -> {
                        finishWithResultCancelAndRemoveCache()
                    }
                    UnifiedKycFlowResult.DOC_TYPE_SWITCHED -> {
                        finishWithResultCancelAndRemoveCache()
                    }
                    UnifiedKycFlowResult.CHALLENGE_IN_PROGRESS -> {
                        //NoOp
                    }
                }
            }
        }
    }

    private fun finishWithResultCancelAndRemoveCache() {
        val isUploading = oneKycSdk.getKycPlusPreferencesProvider()
            .getOneKycState()
        if (isUploading != OneKycResumeState.DOCUMENTS_UPLOADING) {
            removeGotoKycCache()
        }
        activity?.setResult(Activity.RESULT_CANCELED)
        activity?.finish()
    }

    private fun removeGotoKycCache() {
        val preferenceName = OneKycConstants.KYC_SDK_PREFERENCE_NAME
        val preferenceKey = OneKycConstants.KYC_UPLOAD_PROGRESS_STATE
        removeGotoKycPreference(
            context = requireContext(),
            preferenceName = preferenceName,
            preferenceKey = preferenceKey
        )

        val directory1 = getKycSdkDocumentDirectoryPath(requireContext())
        val directory2 = getKycSdkFrameDirectoryPath(requireContext())
        val directory3 = getKycSdkLogDirectoryPath(requireContext())
        removeGotoKycImage(directory1)
        removeGotoKycImage(directory2)
        removeGotoKycImage(directory3)
    }

    private fun gotoCaptureKycDocuments() {
        activity?.let {
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                activity?.setResult(Activity.RESULT_CANCELED)
                activity?.finish()
            } else {
                oneKycSdk.launchKyc(launchSource = KycSdkPartner.TOKOPEDIA_CORE.name, partner = KycSdkPartner.TOKOPEDIA_CORE, isSharia = false, activity = requireActivity())
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

        if (!isConnectionAvailable(requireContext())) {
            errorMessage = ERROR_NO_CONNECTION
            GotoKycAnalytics.sendViewOnErrorPageEvent(
                errorMessage = errorMessage,
                projectId = args.parameter.projectId
            )
            binding?.globalError?.apply {
                setType(GlobalError.NO_CONNECTION)
                errorSecondaryAction.show()
            }
        } else {
            errorMessage = FAILED_UPLOAD_DOCUMENT
            GotoKycAnalytics.sendViewOnErrorPageEvent(
                errorMessage = errorMessage,
                projectId = args.parameter.projectId
            )
            binding?.globalError?.apply {
                setType(GlobalError.MAINTENANCE)
                errorSecondaryAction.hide()
            }
        }

        binding?.globalError?.apply {
            errorAction.text = getString(R.string.goto_kyc_try_again)
            errorSecondaryAction.text = getString(R.string.goto_kyc_direct_to_setting)
        }
    }

    private fun gotoFinalLoader() {
        val parameter = FinalLoaderParam(
            source = args.parameter.source,
            projectId = args.parameter.projectId,
            gotoKycType = KYCConstant.GotoKycFlow.NON_PROGRESSIVE
        )
        val toFinalLoaderPage =
            CaptureKycDocumentsFragmentDirections.actionCaptureKycDocumentsFragmentToFinalLoaderFragment(
                parameter
            )
        view?.findNavController()?.navigate(toFinalLoaderPage)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }

    companion object {
        private const val FAILED_UPLOAD_DOCUMENT = "failed upload document"
        private const val ERROR_NO_CONNECTION = "no connection"
    }
}
