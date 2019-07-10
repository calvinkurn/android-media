package com.tokopedia.product.detail.view.widget

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.product.detail.R

class ValuePropositionBottomSheet : BottomSheets() {

    var title: String = ""
    var desc: String = ""
    var url: String = ""

    companion object {
        const val ARGUMENT_TITLE = "ARGUMENT_TITLE"
        const val ARGUMENT_DESC = "ARGUMENT_DESC"
        const val ARGUMENT_URL = "ARGUMENT_URL"

        @JvmStatic
        fun newInstance(title: String, desc: String, url: String): ValuePropositionBottomSheet {
            val bundle = Bundle()
            bundle.putString(ARGUMENT_TITLE, title)
            bundle.putString(ARGUMENT_DESC, desc)
            bundle.putString(ARGUMENT_URL, url)

            val fragment = ValuePropositionBottomSheet()
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun getArgumentsValue() {
        title = arguments?.getString(ARGUMENT_TITLE) ?: ""
        desc = arguments?.getString(ARGUMENT_DESC) ?: ""
        url = arguments?.getString(ARGUMENT_URL) ?: ""
    }

    override fun getTheme(): Int {
        return R.style.BaseBottomSheetDialog
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.value_proposition_bottom_sheet
    }

    override fun getBaseLayoutResourceId(): Int {
        return R.layout.base_pdp_bottom_sheet
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        updateHeight()
    }

    override fun configView(parentView: View?) {
        super.configView(parentView)

        val displaymetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displaymetrics)
        val widthSpec = View.MeasureSpec.makeMeasureSpec(displaymetrics.widthPixels, View.MeasureSpec.EXACTLY)
        parentView?.post {
            parentView.measure(widthSpec, 0)
            updateHeight(parentView.measuredHeight)
        }
    }

    override fun initView(view: View) {
        getArgumentsValue()
        val txtTitle = view.findViewById<TextView>(R.id.txt_title_bs)
        val txtDesc = view.findViewById<TextView>(R.id.txt_desc_bs)
        val txtLearnMore = view.findViewById<TextView>(R.id.learn_more_btn_bs)
        txtTitle.text = title
        txtDesc.text = desc
        txtLearnMore.setOnClickListener {
            RouteManager.route(
                    view.context,
                    String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
        }
        updateHeight()
    }
}