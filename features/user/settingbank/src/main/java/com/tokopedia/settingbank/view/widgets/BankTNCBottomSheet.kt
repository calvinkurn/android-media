package com.tokopedia.settingbank.view.widgets

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.domain.model.TemplateData
import com.tokopedia.unifycomponents.BottomSheetUnify


class BankTNCBottomSheet : BottomSheetUnify() {

    var templateData: TemplateData?= null

    val MIME_TYPE = "text/html"
    val ENCODING = "utf-8"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            if(containsKey(TEMPLATE_DATA)){
                templateData = getParcelable(TEMPLATE_DATA)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setDefaultParams()
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textHexColor = String.format("#%06x",
                MethodChecker.getColor(context, com.tokopedia.unifycomponents.R.color.Unify_N700) and 0xffffff)

        templateData?.let {
            val text = "<html><head><style type=\"text/css\">" +
                    "   body {" +
                    "      color:${textHexColor}" +
                    "   }" +
                    "</style></head><body>${it.template}</body></html>"
            view.findViewById<WebView>(R.id.tncWebView).setBackgroundColor(Color.TRANSPARENT)
            view.findViewById<WebView>(R.id.tncWebView).loadDataWithBaseURL(null, text,
                    MIME_TYPE, ENCODING, null)
        }
    }

    private fun initBottomSheet() {
        context?.run {
            val child = LayoutInflater.from(this)
                    .inflate(R.layout.bottom_sheets_tnc, ConstraintLayout(this), false)
            setTitle(getString(R.string.sbank_terms_and_condition) )
            setChild(child)
        }
    }

    private fun setDefaultParams() {
        isFullpage = true
        isDragable = true
        isHideable = true
    }

    companion object{
        private val TEMPLATE_DATA = "template_data"
        private val TAG = "BankTNCBottomSheet"
        fun showBankTNCBottomSheet(templateData: TemplateData, activity: FragmentActivity?){
            activity?.let {
                if(!activity.isFinishing){
                    val bottomSheet = BankTNCBottomSheet()
                    val argument = Bundle();
                    argument.putParcelable(TEMPLATE_DATA , templateData)
                    bottomSheet.arguments = argument
                    bottomSheet.show(activity.supportFragmentManager, TAG)
                }
            }
        }
    }
}
