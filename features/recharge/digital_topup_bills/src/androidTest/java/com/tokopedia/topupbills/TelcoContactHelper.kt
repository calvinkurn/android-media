package com.tokopedia.topupbills

import android.app.Activity
import android.app.Instrumentation
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract

class TelcoContactHelper {

    fun createUriContact(contentResolver: ContentResolver): Instrumentation.ActivityResult {
        val resultData = Intent()
        resultData.data = getContactUriByName("Tes", contentResolver)
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    /**
     * @param contactName should set based on contact in device test
     */
    private fun getContactUriByName(contactName: String, contentResolver: ContentResolver): Uri? {
        val cursor: Cursor? = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null)
        cursor?.let {
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id: String = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID))
                    val name: String = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    if (name == contactName) {
                        return Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, id)
                    }
                }
            }
        }
        return null
    }
}