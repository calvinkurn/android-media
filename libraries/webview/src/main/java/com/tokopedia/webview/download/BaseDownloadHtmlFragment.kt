package com.tokopedia.webview.download

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.webview.R
import com.tokopedia.webview.download.BaseDownloadHtmlActivity.Companion.KEY_HTML_CONTENT
import com.tokopedia.webview.download.BaseDownloadHtmlActivity.Companion.KEY_INVOICE_ID
import kotlinx.android.synthetic.main.fragment_general_web_view_lib.*

/**
 * @author by furqan on 20/01/2021
 */
open class BaseDownloadHtmlFragment : BaseDaggerFragment() {

    var invoiceId: String = ""
    var htmlContent: String = ""
    var fileName: String = DEFAULT_FILE_NAME

    override fun onCreate(savedInstanceState: Bundle?) {
        init(savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_general_web_view_lib, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderView()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        // noop
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(KEY_INVOICE_ID, invoiceId)
        outState.putString(KEY_HTML_CONTENT, htmlContent)
    }

    protected fun renderView() {
        htmlContent = Base64.encodeToString(htmlContent.toByteArray(), Base64.DEFAULT)

        general_web_view_lib_swipe_refresh_layout.isEnabled = false

        if (htmlContent.isNotEmpty()) {
            webview.settings.builtInZoomControls = true
            webview.settings.displayZoomControls = false
            webview.settings.loadWithOverviewMode = true
            webview.settings.useWideViewPort = true
            webview.loadData(htmlContent, MIME_TYPE, ENCODING)

            showWebviewContainer()
            hideLoading()
        } else {
            hideWebviewContainer()
            showLoading()
        }
    }

    protected fun showLoading() {
        progressbar.visibility = View.VISIBLE
    }

    protected fun hideLoading() {
        progressbar.visibility = View.GONE
    }

    protected fun showWebviewContainer() {
        general_web_view_lib_swipe_refresh_layout.visibility = View.VISIBLE
    }

    protected fun hideWebviewContainer() {
        general_web_view_lib_swipe_refresh_layout.visibility = View.GONE
    }

    fun printEticket() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context?.let {
                val printManager = it.getSystemService(Context.PRINT_SERVICE) as PrintManager
                val printAdapter = webview.createPrintDocumentAdapter(fileName)
                printManager.print(fileName, printAdapter, PrintAttributes.Builder().build())
                webview.setInitialScale(DEFAULT_INITIAL_SCALE)
                sendDownloadTrack()
            }
        }
    }

    protected open fun sendDownloadTrack() {
        // to be override
    }

    private fun init(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            invoiceId = it.getString(KEY_INVOICE_ID) ?: ""
            htmlContent = it.getString(KEY_HTML_CONTENT) ?: ""
        }

        arguments?.let { args ->
            args.getString(KEY_INVOICE_ID)?.let {
                invoiceId = it
            }
            args.getString(KEY_HTML_CONTENT)?.let {
                htmlContent = it
            }
        }
    }

    companion object {

        private const val MIME_TYPE = "text/html; charset=UTF-8"
        private const val ENCODING = "base64"

        private const val DEFAULT_FILE_NAME = "Tokopedia"
        private const val DEFAULT_INITIAL_SCALE = 1

        fun newInstance(invoiceId: String, htmlContent: String = ""): BaseDownloadHtmlFragment =
                BaseDownloadHtmlFragment().also {
                    it.arguments = Bundle().apply {
                        putString(KEY_INVOICE_ID, invoiceId)
                        putString(KEY_HTML_CONTENT, htmlContent)
                    }
                }
    }

}