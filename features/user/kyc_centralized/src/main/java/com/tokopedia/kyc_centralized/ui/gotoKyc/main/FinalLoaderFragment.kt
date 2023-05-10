package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycFinalLoaderBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.KycStatusResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveResult
import com.tokopedia.utils.lifecycle.autoClearedNullable
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
    }

    private fun initLoader() {
        if (args.parameter.gotoKycType == KYCConstant.GotoKycFlow.PROGRESSIVE) {
            viewModel.registerProgressiveUseCase(
                projectId = args.parameter.projectId,
                challengeId = args.parameter.challengeId
            )
        } else {
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
                        gotoKycType = KYCConstant.GotoKycFlow.PROGRESSIVE,
                        status = if (it.isPending) "0" else "1",
                        sourcePage = args.parameter.source
                    )
                    gotoStatusSubmission(parameter)
                }
                is RegisterProgressiveResult.Failed -> {
                    showScreenLoading(false)
                    //TODO global error
                }
            }
        }

        viewModel.kycStatus.observe(viewLifecycleOwner) {
            when (it) {
                is KycStatusResult.Loading -> {
                    showScreenLoading(true)
                }
                is KycStatusResult.Success -> {
                    val parameter = StatusSubmissionParam(
                        gotoKycType = KYCConstant.GotoKycFlow.NON_PROGRESSIVE,
                        status = it.status,
                        sourcePage = args.parameter.source
                    )
                    gotoStatusSubmission(parameter)
                }
                is KycStatusResult.Failed -> {
                    showScreenLoading(false)
                    //TODO global error
                }
            }
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
