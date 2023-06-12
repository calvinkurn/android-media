package com.tokopedia.indodana.presentation.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.homecredit.view.activity.HomeCreditRegisterActivity
import com.tokopedia.homecredit.view.fragment.HomeCreditCameraV2Fragment
import com.tokopedia.indodana.presentation.utils.PartnerWebAppInterface
import com.tokopedia.webview.BaseWebViewFragment
import com.tokopedia.webview.KEY_URL
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

class PartnerKycFragment: BaseWebViewFragment() {

    override fun getUrl(): String {
        return arguments?.getString(KEY_URL) ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        webView.loadUrl(url)
        webView.addJavascriptInterface(
            PartnerWebAppInterface { docType -> this.takePictureFromCamera(docType, "") },
            CAMERA_PICKER
        )

        requestPermission()
    }

    private fun requestPermission() {
        if (activity == null) return

        context?.let {
            if (ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.CAMERA
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.CAMERA
                    )
                ) {
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.MODIFY_AUDIO_SETTINGS
                        ),
                        0
                    )
                }
            }
        }
    }

    private fun takePictureFromCamera(docType: String?, lang: String?) {
        val applink = if (docType == HomeCreditCameraV2Fragment.TYPE_KTP) {
            ApplinkConst.HOME_CREDIT_KTP_WITHOUT_TYPE
        } else {
            ApplinkConst.HOME_CREDIT_KTP_WITHOUT_TYPE
        }

        val intent = RouteManager.getIntent(context, applink)
        intent.putExtra(HomeCreditRegisterActivity.isV2, true)
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
    }

    // Handler to inject image back to webview
    private fun finishTakePicture(docType: String?, imageBase64: String?) {
        if (imageBase64 != null && docType != null) {
            val script = String.format(
                "var event = new CustomEvent('cameraTriggered'," +
                    "{ detail: {document: '%s', image: 'data:image/jpeg;base64,%s'}});" +
                    "window.dispatchEvent(event);",
                docType,
                imageBase64
            )
            executeJs(script)
        }
    }

    private fun executeJs(script: String) {
        webView.evaluateJavascript(
            script
        ) { value: String -> Timber.e("executeJS result: $value") }
    }

    private fun getBase64FromImagePath(imagePath: String): String? {
        try {
            val file = File(imagePath)
            val inputStream = FileInputStream(file)
            val buffer = ByteArray(1024)
            val outputStream = ByteArrayOutputStream()
            var length: Int
            while (inputStream.read(buffer).also { length = it } != -1) {
                outputStream.write(buffer, 0, length)
            }
            inputStream.close()
            val imageBytes = outputStream.toByteArray()
            return Base64.encodeToString(imageBytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @SuppressLint("PII Data Exposure")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK) {
            val imageFilePath = data?.getStringExtra(HomeCreditCameraV2Fragment.FILE_PATH)
            val type = data?.getStringExtra(HomeCreditCameraV2Fragment.TYPE)

            val imageBase64 = getBase64FromImagePath(imageFilePath ?: "")?.replace("\n", "")

            finishTakePicture(type, imageBase64)
        }
    }

    companion object {
        fun create(url: String): PartnerKycFragment {
            val fragment = PartnerKycFragment()
            val args = Bundle()
            args.putString(KEY_URL, url)
            fragment.arguments = args

            return fragment
        }

        private const val REQUEST_CODE_IMAGE = 123
        private const val CAMERA_PICKER = "CameraPicker"
    }
}
