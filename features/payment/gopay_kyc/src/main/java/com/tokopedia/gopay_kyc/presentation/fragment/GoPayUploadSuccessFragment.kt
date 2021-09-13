package com.tokopedia.gopay_kyc.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.gopay_kyc.R
import com.tokopedia.gopay_kyc.presentation.listener.GoPayKycOpenCameraListener
import kotlinx.android.synthetic.main.fragment_gopay_kyc_upload_success_layout.*

class GoPayUploadSuccessFragment : BaseDaggerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gopay_kyc_upload_success_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressed()

        finishButton.setOnClickListener {
            (activity as GoPayKycOpenCameraListener).exitKycFlow()
        }

    }

    private fun setupOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.let {
                        (it as GoPayKycOpenCameraListener).exitKycFlow()
                    }
                }
            })
    }

    override fun getScreenName() = null
    override fun initInjector() {}

    companion object {
        const val TAG = "GopayUploadSuccess"
        fun newInstance() = GoPayUploadSuccessFragment()
    }
}