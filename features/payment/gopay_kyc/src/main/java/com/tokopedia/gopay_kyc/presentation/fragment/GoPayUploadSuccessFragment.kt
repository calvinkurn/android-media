package com.tokopedia.gopay_kyc.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.gopay_kyc.R
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
        initViews()
        finishButton.setOnClickListener {
            // finish whole process
        }

    }

    private fun initViews() {

    }

    override fun getScreenName() = null
    override fun initInjector() {}

    companion object {
        const val TAG = "GopayUplaodSuccess"
        fun newInstance() = GoPayUploadSuccessFragment()
    }
}