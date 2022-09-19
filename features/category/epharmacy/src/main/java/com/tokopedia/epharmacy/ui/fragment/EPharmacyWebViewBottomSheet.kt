package com.tokopedia.epharmacy.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.epharmacy.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class EPharmacyWebViewBottomSheet: BottomSheetUnify() {
    private var contentView: View? = null
    private var webUrl = ""
    private var sheetTitle = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    companion object {

        const val TAG = "EPharmacyWebViewBottomSheet"
        fun newInstance(title : String , url: String): EPharmacyWebViewBottomSheet {
            return EPharmacyWebViewBottomSheet().apply {
                webUrl = url
                sheetTitle = title
            }
        }
    }

    private fun init() {
        showCloseIcon = true
        showKnob = false
        clearContentPadding = true
        contentView = getContentView()
        setChild(contentView)
        childFragmentManager.beginTransaction().replace(R.id.epharmacy_frame_content_view,
            EPharmacyTnCFragment.getFragmentInstance(webUrl)).commit()
    }

    private fun getContentView(): View {
        return View.inflate(requireContext(), R.layout.epharmacy_web_view_bottom_sheet, null)
    }
}