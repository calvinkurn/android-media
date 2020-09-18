package com.tokopedia.otp.notif.view.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.otp.R
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpFragment
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.notif.view.viewbinding.ResultNotifViewBinding
import com.tokopedia.unifycomponents.setImage

/**
 * Created by Ade Fulki on 14/09/20.
 */

class ResultNotifFragment : BaseOtpFragment(), IOnBackPressed {

    private var resultStatus: String? = ""

    override var viewBound = ResultNotifViewBinding()

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onBackPressed(): Boolean = true

    private fun initVar() {
        arguments?.run {
            resultStatus = getString(ApplinkConstInternalGlobal.PARAM_RESULT_STATUS, "")
        }
    }

    private fun initView() {
        context?.let { context ->
            when (resultStatus) {
                RESULT_STATUS_APPROVED -> {
                    viewBound.mainImage?.setImageUrl(URL_IMG_SUCCESS_RESULT)
                }
                RESULT_STATUS_EXPIRED -> {
                    viewBound.mainImage?.setImageUrl(URL_IMG_FAILED_EXPIRED_RESULT)
                }
                RESULT_STATUS_REJECTED -> {
                    viewBound.mainImage?.setImageUrl(URL_IMG_FAILED_RESULT)
                }
                else -> {
                    activity?.setResult(Activity.RESULT_CANCELED)
                    activity?.finish()
                }
            }
        }
    }

    companion object {

        const val RESULT_STATUS_APPROVED = "result-status-approved"
        const val RESULT_STATUS_REJECTED = "result-status-rejected"
        const val RESULT_STATUS_EXPIRED = "result-status-expired"
        const val URL_IMG_SUCCESS_RESULT = "https://ecs7.tokopedia.net/android/user/success_result_push_notif.png"
        const val URL_IMG_FAILED_RESULT = "https://ecs7.tokopedia.net/android/user/failed_result_push_notif.png"
        const val URL_IMG_FAILED_EXPIRED_RESULT = "https://ecs7.tokopedia.net/android/user/failed_expired_result_push_notif.png"

        fun createInstance(bundle: Bundle): ResultNotifFragment {
            val fragment = ResultNotifFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}