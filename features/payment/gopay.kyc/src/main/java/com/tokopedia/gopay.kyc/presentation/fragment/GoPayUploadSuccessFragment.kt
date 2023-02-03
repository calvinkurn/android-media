package com.tokopedia.gopay.kyc.presentation.fragment

import com.tokopedia.imageassets.ImageUrl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.gopay.kyc.R
import com.tokopedia.gopay.kyc.analytics.GoPayKycConstants
import com.tokopedia.gopay.kyc.analytics.GoPayKycEvent
import com.tokopedia.gopay.kyc.presentation.fragment.base.GoPayKycBaseFragment
import com.tokopedia.gopay.kyc.presentation.listener.GoPayKycNavigationListener
import kotlinx.android.synthetic.main.fragment_gopay_kyc_upload_success_layout.*
import kotlinx.android.synthetic.main.gopay_kyc_success_failed_empty_layout.*

class GoPayUploadSuccessFragment : GoPayKycBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gopay_kyc_upload_success_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uploadStatusTitle.text = getString(R.string.gopay_kyc_upload_success_title_text)
        uploadStatusDescription.text = getString(R.string.gopay_kyc_upload_success_description_text)
        deferredImage.loadRemoteImageDrawable(SUCCESS_IMAGE_NAME, SUCCESS_IMAGE_PATH)
        finishButton.setOnClickListener {
            sendAnalytics(
                GoPayKycEvent.Click.SubmitOkEvent(
                    GoPayKycConstants.ScreenNames.GOPAY_KYC_SUCCESS_PAGE
                )
            )
            (activity as GoPayKycNavigationListener).exitKycFlow()
        }
    }

    override fun getScreenName() = null
    override fun initInjector() {}
    fun sendAnalytics(event: GoPayKycEvent) =
        activity?.let { (it as GoPayKycNavigationListener).sendAnalytics(event) }

    override fun handleBackPressForGopay() {
        sendAnalytics(
            GoPayKycEvent.Click.BackPressEvent(
                GoPayKycConstants.ScreenNames.GOPAY_KYC_SUCCESS_PAGE
            )
        )
        activity?.let { (it as GoPayKycNavigationListener).exitKycFlow() }
    }

    override fun sendOpenScreenGopayEvent() {
        sendAnalytics(
            GoPayKycEvent.Impression.OpenScreenEvent(
                GoPayKycConstants.ScreenNames.GOPAY_KYC_SUCCESS_PAGE
            )
        )
    }

    companion object {
        fun newInstance() = GoPayUploadSuccessFragment()
        const val SUCCESS_IMAGE_NAME = "gopay_kyc_upload_success.png"
        const val SUCCESS_IMAGE_PATH = ImageUrl.SUCCESS_IMAGE_PATH

    }
}