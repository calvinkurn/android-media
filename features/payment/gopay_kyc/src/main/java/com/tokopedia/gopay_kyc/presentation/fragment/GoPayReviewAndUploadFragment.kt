package com.tokopedia.gopay_kyc.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.gopay_kyc.R
import com.tokopedia.gopay_kyc.presentation.listener.GoPayKycFlowListener
import com.tokopedia.gopay_kyc.utils.ReviewCancelDialog
import kotlinx.android.synthetic.main.fragment_gopay_review_layout.*

class GoPayReviewAndUploadFragment : BaseDaggerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gopay_review_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupOnBackPressed()
        sendKycButton.setOnClickListener {
            // upload
           uploadPhotoForKyc()
        }

    }

    private fun uploadPhotoForKyc() {
        var success= false
        if (success)
            showKycSuccessScreen()
        else showKycErrorBottomSheet()
    }

    private fun showKycErrorBottomSheet() {
        activity?.let {
            (it as GoPayKycFlowListener).showKycFailedBottomSheet()
        }
    }

    private fun showKycSuccessScreen() {
        activity?.let {
            (it as GoPayKycFlowListener).showKycSuccessScreen()
        }
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    ReviewCancelDialog.showReviewDialog(requireContext(), { uploadPhotoForKyc() }, {
                        // finish All Activities
                        activity?.let {
                            (it as GoPayKycFlowListener).exitKycFlow()
                        }
                    })
                }
            })
    }

    private fun initViews() {

    }

    override fun getScreenName() = null
    override fun initInjector() {}

    companion object {

        fun newInstance() = GoPayReviewAndUploadFragment()
    }
}