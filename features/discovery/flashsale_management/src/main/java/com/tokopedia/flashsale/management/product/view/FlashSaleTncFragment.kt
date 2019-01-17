package com.tokopedia.flashsale.management.product.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.flashsale.management.R
import kotlinx.android.synthetic.main.fragment_flash_sale_tnc.*
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

class FlashSaleTncFragment : BaseDaggerFragment() {

    var tncString = ""
    var tncLastUpdated = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        tncString = arguments?.getString(EXTRA_TNC) ?: ""
        tncLastUpdated = arguments?.getString(EXTRA_TNC_LAST_UPDATED) ?: ""
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_flash_sale_tnc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webViewTnc.loadData(processWebViewHtmlStyle(tncString), "text/html; charset=utf-8", "UTF-8")
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            if (tncLastUpdated.isEmpty()) {
                label_last_updated.visibility = View.GONE
            } else {
                label_last_updated.text = tncLastUpdated
                label_last_updated.visibility = View.VISIBLE
            }
        }
    }

    fun processWebViewHtmlStyle(html_string: String): String {
        return getString(R.string.flash_sale_html_process_web_view, html_string)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        // no-op
    }

    companion object {
        @JvmStatic
        fun newInstance(tncString:String, tncLastUpdated:String): FlashSaleTncFragment {
            val f = FlashSaleTncFragment()
            f.arguments = Bundle().apply {
                putString(EXTRA_TNC, tncString)
                putString(EXTRA_TNC_LAST_UPDATED, tncLastUpdated)
            }
            return f
        }
        private const val EXTRA_TNC = "tnc"
        private const val EXTRA_TNC_LAST_UPDATED = "tnc_last_updated"
    }
}