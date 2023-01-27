package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycLoaderBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.utils.lifecycle.autoClearedNullable

class RouterFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentGotoKycLoaderBinding>()

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

        val intentExtras = requireActivity().intent.extras

        val sourcePage = intentExtras?.getString(PARAM_REQUEST_PAGE).orEmpty()
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intentExtras?.getParcelable(PARAM_DATA, GotoKycMainParam::class.java)
        } else {
            @Suppress("DEPRECATION")
            intentExtras?.getParcelable(PARAM_DATA)
        }

        when (sourcePage) {
            PAGE_STATUS_SUBMISSION -> {
                val parameter = StatusSubmissionParam(
                    dataSource = data?.dataSource.orEmpty(),
                    status = data?.status.orEmpty(),
                    sourcePage = data?.sourcePage.orEmpty(),
                    listReason = data?.listReason.orEmpty()
                )
                gotoStatusSubmission(parameter)
            }
        }
    }

    private fun gotoStatusSubmission(parameter: StatusSubmissionParam) {
        val toSubmissionStatusPage = RouterFragmentDirections.actionRouterFragmentToStatusSubmissionFragment(parameter)
        view?.findNavController()?.navigate(toSubmissionStatusPage)
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }

    companion object {
        const val PARAM_REQUEST_PAGE = "request_page"
        const val PAGE_STATUS_SUBMISSION = "page_status_submission"
        const val PARAM_DATA = "parameter"
        private val SCREEN_NAME = RouterFragment::class.java.simpleName
    }
}
