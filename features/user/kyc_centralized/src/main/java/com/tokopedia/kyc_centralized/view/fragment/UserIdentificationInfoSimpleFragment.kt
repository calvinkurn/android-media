package com.tokopedia.kyc_centralized.view.fragment

import android.app.Activity
import android.content.Intent
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
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton

class UserIdentificationInfoSimpleFragment: BaseDaggerFragment() {

    private var projectId = 0
    private var mainView: ConstraintLayout? = null
    private var mainImage: ImageUnify? = null
    private var button: UnifyButton? = null
    private var loader: LoaderUnify? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_identification_info_simple, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        projectId = activity?.intent?.data?.getQueryParameter(
                ApplinkConstInternalGlobal.PARAM_PROJECT_ID).toIntOrZero()
        initViews()
        startKyc()
    }

    private fun initViews() {
        activity?.let {
            mainView = it.findViewById(R.id.uii_simple_main_view)
            mainImage = it.findViewById(R.id.uii_simple_main_image)
            button = it.findViewById(R.id.uii_simple_button)
            loader = it.findViewById(R.id.uii_simple_loader)

            mainView?.hide()
            mainImage?.urlSrc = KycUrl.ICON_WAITING
            button?.setOnClickListener { _ ->
                it.setResult(Activity.RESULT_OK)
                it.finish()
            }
            loader?.show()
        }
    }

    private fun startKyc() {
        val intent = RouteManager.getIntent(requireContext(),
                ApplinkConstInternalGlobal.USER_IDENTIFICATION_FORM, projectId.toString())
        startActivityForResult(intent, KYC_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == KYC_REQUEST_CODE) {
            when(resultCode) {
                Activity.RESULT_OK -> {
                    loader?.hide()
                    mainView?.show()
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