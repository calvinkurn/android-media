package com.tokopedia.product.detail.webview

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.product.detail.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.webview.TkpdWebView

class ProductWebViewBottomSheet : BottomSheetUnify() {
    companion object {
        const val TAG = "ProductWebViewBottomSheet"
        private const val ARG_TITLE = "args_title"
        fun instance(
            title: String,
            url: String
        ) = ProductWebViewBottomSheet().apply {
            arguments = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ProductWebViewFragment.ARG_URL, url)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun init() {
        val arguments = arguments ?: return
        val title = arguments.getString(ARG_TITLE) ?: ""


        clearContentPadding = true
        isFullpage = true
        setTitle(title)

        val view = View.inflate(requireContext(), R.layout.product_webview_container, null)
        setChild(view)

        val fragment = ProductWebViewFragment.getInstance(arguments)
        childFragmentManager.beginTransaction().replace(
            R.id.pdp_frame_container,
            fragment
        ).commit()
        // User Agent Override ke: "Tokopedia Webview - Liteapp"
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }
}
