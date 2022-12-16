package com.tokopedia.kyc_centralized.ui.gotoKyc.transparent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycLoaderBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.utils.lifecycle.autoClearedNullable

class GotoKycTransparentFragment: BaseDaggerFragment() {

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
        val projectId = activity?.intent?.extras?.getString(ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID)
        validationProjectId(projectId)
    }

    private fun validationProjectId(projectId: String?) {
        if (projectId?.toIntOrNull() == null){
            activity?.finish()
        } else {
            //put valid project id
        }
    }

    private fun directTocKyc() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.KYC_INFO_BASE)
        intent.putExtra(ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID, "GET_PROJECT_ID_FROM_VIEW_MODEL")
        startActivity(intent)
        activity?.finish()
    }

    private fun directToGotoKyc() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.GOTO_KYC)
        intent.putExtra(ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID, "GET_PROJECT_ID_FROM_VIEW_MODEL")
        startActivity(intent)
        activity?.finish()
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }

    companion object {
        private val SCREEN_NAME = GotoKycTransparentFragment::class.java.simpleName

        fun createInstance(): Fragment = GotoKycTransparentFragment()
    }

}
