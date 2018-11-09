package com.tokopedia.flashsale.management.product.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.flashsale.management.R
import kotlinx.android.synthetic.main.fragment_flash_sale_tnc.*

class FlashSaleTncFragment : BaseDaggerFragment() {

    var tncString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        tncString = arguments?.getString(EXTRA_TNC) ?: ""
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_flash_sale_tnc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webViewTnc.loadData(processWebViewHtmlStyle(tncString), "text/html; charset=utf-8", "UTF-8")
    }

    fun processWebViewHtmlStyle(html_string: String): String {
        return getString(R.string.html_process_web_view, html_string)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        // no-op
    }

    companion object {
        @JvmStatic
        fun newInstance(tncString:String): FlashSaleTncFragment {
            val f = FlashSaleTncFragment()
            f.arguments = Bundle().apply {
                putString(EXTRA_TNC, tncString)
            }
            return f
        }
        private const val EXTRA_TNC = "tnc"
    }
}