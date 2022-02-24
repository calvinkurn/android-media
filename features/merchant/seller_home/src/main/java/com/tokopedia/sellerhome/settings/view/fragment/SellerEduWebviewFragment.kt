package com.tokopedia.sellerhome.settings.view.fragment

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.webkit.WebView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.util.DownloadHelper
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.common.errorhandler.SellerHomeErrorHandler
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.KEY_URL
import javax.inject.Inject

class SellerEduWebviewFragment : BaseSessionWebViewFragment() {

    companion object {
        private const val GOOGLE_DOCS_LINK = "https://docs.google.com/viewer?url="
        private const val PDF_EXT = "pdf"

        private const val HTTP_SCHEME = "http://"
        private const val HTTPS_SCHEME = "https://"

        private const val

        fun newInstance(url: String): SellerEduWebviewFragment {
            val fragment = SellerEduWebviewFragment()
            val args = Bundle().apply {
                putString(KEY_URL, url)
            }
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    private var permissionCheckerHelper: PermissionCheckerHelper? = null

    override fun initInjector() {
        DaggerSellerHomeComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

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
            try {
                val correctedUrl = url.appendUrlIfInvalid()
                if (correctedUrl.isValidUrl()) {
                    checkPermissionAndDownload(correctedUrl.ejectFromGoogleDocs())
                } else {
                    sendErrorLog(url)
                }
            } catch (ex: Exception) {
                sendErrorLog(url, ex)
            }
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

    private fun sendErrorLog(url: String,
                             exception: Exception? = null) {
        val errorMessage = context?.getString(
            R.string.sah_seller_edu_error_message,
            url
        ).orEmpty()
        val throwable = exception ?: MessageErrorException(errorMessage)
        SellerHomeErrorHandler.logExceptionToServer(
            errorTag = SellerHomeErrorHandler.SELLER_EDU_TAG,
            throwable = throwable,
            errorType = errorMessage,
            deviceId = userSession.deviceId.orEmpty()
        )
    }

    private fun isPdf(uri: String): Boolean =
        uri.lowercase().endsWith(PDF_EXT)

    private fun fetchFileName(uri: String): String {
        return uri.substring(uri.lastIndexOf("/") + 1)
    }

    private fun String.ejectFromGoogleDocs(): String =
        this.replace(GOOGLE_DOCS_LINK, "")

    private fun String.appendUrlIfInvalid(): String {
        val isUrlHasScheme = startsWith(HTTP_SCHEME) || startsWith(HTTPS_SCHEME)
        var appendedUrl = this
        if (!isUrlHasScheme) {
            appendedUrl = HTTPS_SCHEME + this
        }
        return appendedUrl
    }

    private fun String.isValidUrl(): Boolean {
        return Patterns.WEB_URL.matcher(this).matches()
    }

}