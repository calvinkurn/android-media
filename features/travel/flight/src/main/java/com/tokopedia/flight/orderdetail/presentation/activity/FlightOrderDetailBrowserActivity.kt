package com.tokopedia.flight.orderdetail.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.flight.R
import com.tokopedia.flight.orderdetail.presentation.fragment.FlightOrderDetailBrowserFragment

class FlightOrderDetailBrowserActivity : BaseSimpleActivity() {

    private lateinit var title: String
    private lateinit var htmlContent: String

    override fun getNewFragment(): Fragment? =
            FlightOrderDetailBrowserFragment.getInstance(htmlContent)

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_flight_order_detail_browser)

        title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        htmlContent = intent.getStringExtra(EXTRA_HTML_CONTENT) ?: ""

        savedInstanceState?.let {
            title = it.getString(EXTRA_TITLE, "")
            htmlContent = it.getString(EXTRA_HTML_CONTENT, "")
        }

        super.onCreate(savedInstanceState)

        toolbar.title = title
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_HTML_CONTENT, htmlContent)
        outState.putString(EXTRA_TITLE, title)
    }

    companion object {
        private const val EXTRA_TITLE = "EXTRA_TITLE"
        private const val EXTRA_HTML_CONTENT = "EXTRA_HTML_CONTENT"

        fun getIntent(context: Context, title: String, htmlContent: String): Intent =
                Intent(context, FlightOrderDetailBrowserActivity::class.java)
                        .putExtra(EXTRA_TITLE, title)
                        .putExtra(EXTRA_HTML_CONTENT, htmlContent)
    }
}