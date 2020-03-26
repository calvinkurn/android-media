package com.tokopedia.thankyou_native.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.thankyou_native.R
import kotlinx.android.synthetic.main.thank_payment_method_bottom_sheet.*

class HowToPayFragment : Fragment() {

    private var howToPayStr: String? = null

    private val mimeType = "text/html"
    private val encoding = "utf-8"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_HOW_TO_PAY_DATA)) {
                howToPayStr = it.getString(ARG_HOW_TO_PAY_DATA)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.thank_payment_method_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        howToPayWebView.settings.javaScriptEnabled = true
        howToPayStr?.let {
            howToPayWebView.loadData(howToPayStr,
                mimeType, encoding)
        }
    }

    companion object {
        private const val ARG_HOW_TO_PAY_DATA = "arg_how_to_pay_data"
        fun getInstance(howToPayStr: String): HowToPayFragment = HowToPayFragment().apply {
            val bundle = Bundle()
            bundle.putString(ARG_HOW_TO_PAY_DATA, howToPayStr)
            arguments = bundle
        }
    }

}