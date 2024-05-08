package com.tokopedia.product.detail.webview

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import com.tokopedia.product.detail.R
import com.tokopedia.unifycomponents.BottomSheetUnify

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
            R.id.pdp_webbs_container,
            fragment
        ).commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val confirmButton = view.findViewById<View>(R.id.pdp_webbs_button_oke)
        confirmButton.setOnClickListener { dismiss() }
        confirmButton.post {
            val webViewContainer = view.findViewById<View>(R.id.pdp_webbs_container)
            webViewContainer.updatePadding(bottom = confirmButton.height)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }
}
