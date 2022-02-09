package com.tokopedia.sellerhome.settings.view.fragment

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.webkit.WebView
import com.tokopedia.kotlin.util.DownloadHelper
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.KEY_URL

class SellerEduWebviewFragment : BaseSessionWebViewFragment() {

    companion object {
        private const val GOOGLE_DOCS_LINK = "https://docs.google.com/viewer?url="
        private const val PDF_EXT = "pdf"

        fun newInstance(url: String): SellerEduWebviewFragment {
            val fragment = SellerEduWebviewFragment()
            val args = Bundle().apply {
                putString(KEY_URL, url)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private var permissionCheckerHelper: PermissionCheckerHelper? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper?.onRequestPermissionsResult(
                requireActivity().applicationContext,
                requestCode,
                permissions,
                grantResults
            )
        }
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        if (isPdf(url)) {
            checkPermissionAndDownload(url.ejectFromGoogleDocs())
            return true
        }
        return super.shouldOverrideUrlLoading(webView, url)
    }

    private fun checkPermissionAndDownload(url: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            permissionCheckerHelper = PermissionCheckerHelper()
            permissionCheckerHelper?.checkPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                object : PermissionCheckerHelper.PermissionCheckListener {
                    override fun onPermissionDenied(permissionText: String) {
                    }

                    override fun onNeverAskAgain(permissionText: String) {
                    }

                    override fun onPermissionGranted() {
                        downloadFile(url)
                    }
                })
        } else {
            downloadFile(url)
        }
    }

    private fun downloadFile(url: String) {
        val downloadHelper = DownloadHelper(
            requireActivity(),
            url,
            fetchFileName(url),
            object : DownloadHelper.DownloadHelperListener {
                override fun onDownloadComplete() {}
            })

        downloadHelper.downloadFile(::isPdf)
    }

    private fun isPdf(uri: String): Boolean =
        uri.lowercase().endsWith(PDF_EXT)

    private fun fetchFileName(uri: String): String {
        return uri.substring(uri.lastIndexOf("/") + 1)
    }

    private fun String.ejectFromGoogleDocs(): String =
        this.replace(GOOGLE_DOCS_LINK, "")

}