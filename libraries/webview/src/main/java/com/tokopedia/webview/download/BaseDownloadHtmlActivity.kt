package com.tokopedia.webview.download

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.webview.R

open class BaseDownloadHtmlActivity : BaseSimpleActivity() {

    lateinit var baseDownloadFragment: BaseDownloadHtmlFragment

    var toolbarTitle: String = ""
    var invoiceId: String = ""
    var htmlContent: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        init(savedInstanceState)
        super.onCreate(savedInstanceState)
        setupToolbar()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(KEY_WEBVIEW_TITLE, toolbarTitle)
        outState.putString(KEY_INVOICE_ID, invoiceId)
        outState.putString(KEY_HTML_CONTENT, htmlContent)
    }

    override fun getNewFragment(): Fragment =
            BaseDownloadHtmlFragment.newInstance(invoiceId, htmlContent).also {
                baseDownloadFragment = it
            }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.menu_webview_download, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                R.id.menu_webview_download -> {
                    if (::baseDownloadFragment.isInitialized)
                        baseDownloadFragment.printEticket()
                    true
                }
                else -> false
            }

    private fun init(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            toolbarTitle = it.getString(KEY_WEBVIEW_TITLE, "")
            invoiceId = it.getString(KEY_INVOICE_ID, "")
            htmlContent = it.getString(KEY_HTML_CONTENT, "")
        }

        intent.getStringExtra(KEY_WEBVIEW_TITLE)?.let {
            if (it.isNotEmpty()) toolbarTitle = it
        }
        intent.getStringExtra(KEY_INVOICE_ID)?.let {
            if (it.isNotEmpty()) invoiceId = it
        }
        intent.getStringExtra(KEY_HTML_CONTENT)?.let {
            if (it.isNotEmpty()) htmlContent = it
        }
    }

    private fun setupToolbar() {
        updateTitle(toolbarTitle)
        supportActionBar?.show()
    }

    companion object {
        const val KEY_WEBVIEW_TITLE = "KEY_WEBVIEW_TITLE"
        const val KEY_INVOICE_ID = "KEY_INVOICE_ID"
        const val KEY_HTML_CONTENT = "KEY_HTML_CONTENT"

        fun getIntent(context: Context, title: String, invoiceId: String, htmlContent: String): Intent =
                Intent(context, BaseDownloadHtmlActivity::class.java)
                        .putExtra(KEY_WEBVIEW_TITLE, title)
                        .putExtra(KEY_INVOICE_ID, invoiceId)
                        .putExtra(KEY_HTML_CONTENT, htmlContent)
    }
}