package com.tokopedia.webview

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.webkit.WebView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tokopedia.utils.permission.PermissionCheckerHelper
import timber.log.Timber

interface ContactPickerListener {
    val permissionCheckerHelper: PermissionCheckerHelper
    fun openContactPicker(fragment: Fragment, action: IntentAction)
    fun onContactSelected(requestCode: Int, resultCode: Int, data: Intent?, contentResolver: ContentResolver, context: Context, webView: TkpdWebView)
}

interface IntentAction {
    fun performAction(intent: Intent)
}

class ContactPicker: ContactPickerListener {
    override val permissionCheckerHelper: PermissionCheckerHelper
        get() = PermissionCheckerHelper()

    override fun openContactPicker(fragment: Fragment, action: IntentAction) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper.checkPermissions(fragment,
                arrayOf(
                    PermissionCheckerHelper.Companion.PERMISSION_READ_CONTACT
                ),
                object : PermissionCheckerHelper.PermissionCheckListener {
                    override fun onPermissionDenied(permissionText: String) {
                        //no-op
                    }

                    override fun onNeverAskAgain(permissionText: String) {
                        //no-op
                    }

                    override fun onPermissionGranted() {
                        val contactPickerIntent = Intent(
                            Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
                        try {
                            action.performAction(contactPickerIntent)
                        } catch (e: ActivityNotFoundException) {
                            Timber.e(e)
                        }
                    }
                }, "Need Permission")
        }
    }

    override fun onContactSelected(requestCode: Int, resultCode: Int, data: Intent?, contentResolver: ContentResolver, context: Context, webView: TkpdWebView) {
        if (requestCode == CONTACT_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val contactUri: Uri? = data.data
            val projectionNumber: Array<String> = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val projectionName: Array<String> = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            contactUri?.let {
                var number = ""
                var name = ""

                contentResolver.query(contactUri, projectionNumber, null, null, null).use { cursor ->
                    if (cursor == null) return
                    // If the cursor returned is valid, get the phone number.
                    if (cursor.moveToFirst()) {
                        val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        number = cursor.getString(numberIndex)
                    }
                }

                contentResolver.query(contactUri, projectionName, null, null, null).use { cursor ->
                    if (cursor == null) return
                    // If the cursor returned is valid, get the phone number.
                    if (cursor.moveToFirst()) {
                        val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                        name = cursor.getString(numberIndex)
                    }
                }

                sendSelectedContact(name, number, webView)
            }
        }
    }

    private fun sendSelectedContact(name: String, number: String, webView: WebView) {
        val script = String.format(
            "var event = new CustomEvent('contactSelected'," +
                "{ detail: {name: '%s', number: '%s'}});" +
                "window.dispatchEvent(event);",
            name,
            number
        )
        webView.evaluateJavascript(
            script
        ) { value: String -> Timber.d("executeJS result: $value") }
    }

    companion object {
        const val CONTACT_PICKER_REQUEST_CODE = 12235
    }
}
