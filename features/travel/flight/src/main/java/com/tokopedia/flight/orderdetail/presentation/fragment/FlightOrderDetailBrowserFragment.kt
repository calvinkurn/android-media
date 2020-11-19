package com.tokopedia.flight.orderdetail.presentation.fragment

import android.os.Bundle
import android.util.Base64
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.flight.R
import kotlinx.android.synthetic.main.activity_flight_order_detail_browser.*


/**
 * @author by furqan on 16/11/2020
 */
class FlightOrderDetailBrowserFragment : Fragment() {

    private lateinit var htmlContent: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        htmlContent = arguments?.getString(EXTRA_HTML_CONTENT) ?: ""
        savedInstanceState?.let {
            if (it.containsKey(EXTRA_HTML_CONTENT))
                htmlContent = it.getString(EXTRA_HTML_CONTENT) ?: ""
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.activity_flight_order_detail_browser, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

        htmlContent = Base64.encodeToString(htmlContent.toByteArray(), Base64.DEFAULT)

        wvFlightOrderDetail.settings.javaScriptEnabled = true
        wvFlightOrderDetail.settings.builtInZoomControls = true
        wvFlightOrderDetail.settings.displayZoomControls = false
        wvFlightOrderDetail.settings.loadWithOverviewMode = true
        wvFlightOrderDetail.loadData(htmlContent, MIME_TYPE, ENCODING)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_HTML_CONTENT, htmlContent)
    }

    companion object {
        private const val EXTRA_HTML_CONTENT = "EXTRA_HTML_CONTENT"

        private const val MIME_TYPE = "text/html; charset=UTF-8"
        private const val ENCODING = "base64"

        fun getInstance(htmlContent: String): FlightOrderDetailBrowserFragment =
                FlightOrderDetailBrowserFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_HTML_CONTENT, htmlContent)
                    }
                }
    }
}