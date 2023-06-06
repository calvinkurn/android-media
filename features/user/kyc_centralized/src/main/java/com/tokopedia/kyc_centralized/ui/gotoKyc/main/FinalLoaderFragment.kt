package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycFinalLoaderBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.kyc_centralized.ui.gotoKyc.analytics.GotoKycAnalytics
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.utils.getGotoKycErrorMessage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class FinalLoaderFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentGotoKycFinalLoaderBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[FinalLoaderViewModel::class.java]
    }

    private val args: FinalLoaderFragmentArgs by navArgs()

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
        initLoader()
        initObserver()
        initListener()
    }

    private fun initListener() {
        binding?.globalError?.setActionClickListener {
            showScreenLoading(true)
            initLoader()
        }
        binding?.globalError?.setSecondaryActionClickListener {
            val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(intent)
        }
    }

    private fun initLoader() {
        if (args.parameter.gotoKycType == KYCConstant.GotoKycFlow.PROGRESSIVE) {
            viewModel.registerProgressiveUseCase(
                projectId = args.parameter.projectId,
                challengeId = args.parameter.challengeId
            )
        } else {
            showScreenLoading(true)
            viewModel.kycStatus(args.parameter.projectId)
        }
    }

    private fun initObserver() {
        viewModel.registerProgressive.observe(viewLifecycleOwner) {
            when (it) {
                is RegisterProgressiveResult.Loading -> {
                    showScreenLoading(true)
                }
                is RegisterProgressiveResult.RiskyUser -> {
                    val parameter = DobChallengeParam(
                        projectId = args.parameter.projectId,
                        challengeId = args.parameter.challengeId
                    )
                    gotoDobChallenge(parameter)
                }
                is RegisterProgressiveResult.NotRiskyUser -> {
                    val parameter = StatusSubmissionParam(
                        projectId = args.parameter.projectId,
                        gotoKycType = KYCConstant.GotoKycFlow.PROGRESSIVE,
                        status = it.status.toString(),
                        sourcePage = args.parameter.source,
                        rejectionReason = it.rejectionReason
                    )
                    gotoStatusSubmission(parameter)
                }
                is RegisterProgressiveResult.Failed -> {
                    showScreenLoading(false)
                    handleGlobalError(throwable = it.throwable)
                }
            }
        }

        viewModel.kycStatus.observe(viewLifecycleOwner) {
            when (it) {
                is ProjectInfoResult.StatusSubmission -> {
                    val parameter = StatusSubmissionParam(
                        projectId = args.parameter.projectId,
                        gotoKycType = KYCConstant.GotoKycFlow.NON_PROGRESSIVE,
                        status = it.status,
                        sourcePage = args.parameter.source,
                        rejectionReason = it.rejectionReason
                    )
                    gotoStatusSubmission(parameter)
                }
                else -> {
                    showScreenLoading(false)
                    handleGlobalError(it.throwable)
                }
            }
        }
    }

    private fun handleGlobalError(throwable: Throwable?) {
        binding?.globalError?.show()
        if (throwable != null) {
            val error = throwable.getGotoKycErrorMessage(requireContext())
            GotoKycAnalytics.sendViewOnErrorPageEvent(
                errorMessage = error,
                projectId = args.parameter.projectId
            )
            when (throwable) {
                is UnknownHostException, is ConnectException -> {
                    binding?.globalError?.setType(GlobalError.NO_CONNECTION)
                }
                is SocketTimeoutException -> {
                    binding?.globalError?.apply {
                    errorIllustration.loadImage(getString(R.string.img_url_goto_kyc_error_timeout))

                        errorTitle.text = getString(R.string.goto_kyc_timeout_title)
                        errorDescription.text = getString(R.string.goto_kyc_timeout_description)

                        errorSecondaryAction.hide()
                    }
                }
                else -> {
                    binding?.globalError?.apply {
                        setType(GlobalError.MAINTENANCE)
                        errorDescription.text = error
                    }
                }
            }
        } else {
            GotoKycAnalytics.sendViewOnErrorPageEvent(
                errorMessage = context?.getString(R.string.goto_kyc_error_from_be).toString(),
                projectId = args.parameter.projectId
            )
            binding?.globalError?.setType(GlobalError.MAINTENANCE)
        }
    }

    private fun showScreenLoading(isLoading: Boolean) {
        binding?.apply {
            loader.showWithCondition(isLoading)
            tvTitle.showWithCondition(isLoading)
            tvSubtitle.showWithCondition(isLoading)
        }
    }

    private fun gotoStatusSubmission(parameter: StatusSubmissionParam) {
        val toSubmissionStatusPage = FinalLoaderFragmentDirections.actionFinalLoaderFragmentToStatusSubmissionFragment(parameter)
        view?.findNavController()?.navigate(toSubmissionStatusPage)
    }

    private fun gotoDobChallenge(parameter: DobChallengeParam) {
        val toDobChallengePage = FinalLoaderFragmentDirections.actionFinalLoaderFragmentToDobChallengeFragment(parameter)
        view?.findNavController()?.navigate(toDobChallengePage)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }
}
