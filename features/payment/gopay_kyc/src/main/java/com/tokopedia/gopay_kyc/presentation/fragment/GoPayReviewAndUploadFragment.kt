package com.tokopedia.gopay_kyc.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.gopay_kyc.R
import com.tokopedia.gopay_kyc.di.GoPayKycComponent
import com.tokopedia.gopay_kyc.presentation.activity.GoPayReviewActivity.Companion.KTP_PATH
import com.tokopedia.gopay_kyc.presentation.activity.GoPayReviewActivity.Companion.SELFIE_KTP_PATH
import com.tokopedia.gopay_kyc.presentation.listener.GoPayKycOpenCameraListener
import com.tokopedia.gopay_kyc.presentation.listener.GoPayKycReviewListener
import com.tokopedia.gopay_kyc.utils.ReviewCancelDialog
import kotlinx.android.synthetic.main.fragment_gopay_review_layout.*

class GoPayReviewAndUploadFragment : BaseDaggerFragment() {

    private var ktpImagePath = ""
    private var ktpSelfieImagePath = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gopay_review_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setImagePath()
        initViews()
        initListeners()
    }

    private fun initListeners() {
        setupOnBackPressed()
        sendKycButton.setOnClickListener {
            uploadPhotoForKyc()
        }
        retryKtpIcon.setOnClickListener { openKtpCameraScreen() }
        retryKtpText.setOnClickListener { openKtpCameraScreen() }
        retryKtpSelfieIcon.setOnClickListener { openSelfieKtpCameraScreen() }
        retryKtpSelfieText.setOnClickListener { openSelfieKtpCameraScreen() }
    }

    private fun uploadPhotoForKyc() {
        // @TODO remove afterwards
        var success = true
        if (success)
            showKycSuccessScreen()
        else showKycErrorBottomSheet()
    }

    private fun openKtpCameraScreen() {
        activity?.let { (it as GoPayKycOpenCameraListener).openKtpCameraScreen() }
    }

    private fun openSelfieKtpCameraScreen() {
        activity?.let { (it as GoPayKycOpenCameraListener).openSelfieKtpCameraScreen() }
    }

    private fun showKycErrorBottomSheet() {
        activity?.let {
            (it as GoPayKycReviewListener).showKycFailedBottomSheet()
        }
    }

    private fun showKycSuccessScreen() {
        activity?.let {
            (it as GoPayKycReviewListener).showKycSuccessScreen()
        }
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    ReviewCancelDialog.showReviewDialog(requireContext(), { uploadPhotoForKyc() }, {
                        activity?.let {
                            (it as GoPayKycOpenCameraListener).exitKycFlow()
                        }
                    })
                }
            })
    }

    private fun initViews() {

    }

    private fun setImagePath() {
        arguments?.let {
            ktpImagePath = it.getString(KTP_PATH, "")
            ktpSelfieImagePath = it.getString(SELFIE_KTP_PATH, "")
        }
    }

    override fun getScreenName() = null
    override fun initInjector() {}

    fun updateKtpImage(ktpPath: String) {
        ktpImagePath = ktpPath
    }

    fun updateSelfieKtpImage(selfieKtpPath: String) {
        ktpSelfieImagePath = selfieKtpPath

    }

    companion object {

        fun newInstance(bundle: Bundle?): GoPayReviewAndUploadFragment {
            val fragment = GoPayReviewAndUploadFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}