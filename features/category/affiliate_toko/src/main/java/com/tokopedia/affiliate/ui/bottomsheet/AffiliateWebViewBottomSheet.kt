package com.tokopedia.affiliate.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.affiliate.ui.fragment.AffiliateHelpFragment
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class AffiliateWebViewBottomSheet: BottomSheetUnify() {
    private var contentView: View? = null
    private var webUrl = ""
    private var sheetTitle = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    companion object {

        fun newInstance(title : String , url: String): AffiliateWebViewBottomSheet {
            return AffiliateWebViewBottomSheet().apply {
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
        childFragmentManager.beginTransaction().replace(R.id.frame_content_view,
                AffiliateHelpFragment.getFragmentInstance(webUrl)).commit()
    }

    private fun getContentView(): View {
        return View.inflate(requireContext(), R.layout.affiliate_web_view_bottom_sheet, null)
    }
}