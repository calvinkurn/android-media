package com.tokopedia.flight.orderdetail.presentation.fragment

import android.os.Bundle
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.activity_flight_order_detail_browser, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

        wvFlightOrderDetail.settings.builtInZoomControls = true
        wvFlightOrderDetail.settings.displayZoomControls = false
        wvFlightOrderDetail.settings.loadWithOverviewMode = true
        wvFlightOrderDetail.setInitialScale(DEFAULT_SCALE)
        wvFlightOrderDetail.loadData(htmlContent, MIME_TYPE, ENCODING)
        wvFlightOrderDetail.scrollTo(if (displayMetrics.widthPixels > 0) displayMetrics.widthPixels else DEFAULT_X_POSITION, DEFAULT_Y_POSITION)
    }

    companion object {
        private const val EXTRA_HTML_CONTENT = "EXTRA_HTML_CONTENT"

        private const val DEFAULT_X_POSITION = 1300
        private const val DEFAULT_Y_POSITION = 0
        private const val DEFAULT_SCALE = 130
        private const val MIME_TYPE = "text/html; charset=utf-8"
        private const val ENCODING = "UTF-8"

        fun getInstance(htmlContent: String): FlightOrderDetailBrowserFragment =
                FlightOrderDetailBrowserFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_HTML_CONTENT, htmlContent)
                    }
                }
    }
}