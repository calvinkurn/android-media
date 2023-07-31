package com.tokopedia.pdpsimulation.activateCheckout.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.pdpsimulation.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.downrate_bottomsheet_layout.*

class DownRateBottomsheet: BottomSheetUnify() {
    private val childLayoutRes = R.layout.downrate_bottomsheet_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initChildView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDefaultParams()
        setUI()
    }

    private fun initChildView() {
        val view = LayoutInflater.from(context).inflate(childLayoutRes, null, false)
        setChild(view)
    }

    private fun setDefaultParams() {
        showCloseIcon = true
        showHeader = true
    }

    private fun setUI() {
        arguments?.let {
            ivIllustration.setImageUrl(it.getString(DOWNRATE_IMAGE_URL, ""))
            tvTitle.text = it.getString(DOWNRATE_TITLE, "")
            tvDescription.text = it.getString(DOWNRATE_DESCRIPTION, "")
        }
    }

    companion object {
        private const val TAG = "DownRateBottomSheet"

        private const val DOWNRATE_IMAGE_URL = "downrate_image_url"
        private const val DOWNRATE_TITLE = "downrate_title"
        private const val DOWNRATE_DESCRIPTION = "downrate_description"

        fun createBundle(
            imageUrl: String,
            title: String,
            description: String
        ): Bundle {
            return Bundle().apply {
                putString(DOWNRATE_IMAGE_URL, imageUrl)
                putString(DOWNRATE_TITLE, title)
                putString(DOWNRATE_DESCRIPTION, description)
            }
        }

        fun show(
            bundle: Bundle,
            childFragmentManager: FragmentManager
        ): DownRateBottomsheet {
            val downRateBottomSheet = DownRateBottomsheet()
            downRateBottomSheet.arguments = bundle
            downRateBottomSheet.show(childFragmentManager, TAG)
            return downRateBottomSheet
        }
    }
}
