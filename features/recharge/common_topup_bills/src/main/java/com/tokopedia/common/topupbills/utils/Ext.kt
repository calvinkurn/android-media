package com.tokopedia.common.topupbills.utils

import android.content.ContentResolver
import android.content.res.Resources
import android.net.Uri
import android.provider.ContactsContract
import android.util.TypedValue
import com.tokopedia.common.topupbills.view.model.DigitalContactData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

/**
 * Created by nabillasabbaha on 07/05/19.
 */

const val IDN_CALLING_CODE = "62"
const val IDN_CALLING_CODE_WITH_PLUS = "+62"

fun Uri.covertContactUriToContactData(contentResolver: ContentResolver): DigitalContactData {
    val id = this.getLastPathSegment()
    val contactWhere = (ContactsContract.CommonDataKinds.Phone._ID + " = ? AND "
            + ContactsContract.Data.MIMETYPE + " = ?")

    val contactWhereParams = arrayOf(id, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
    val cursorPhone = contentResolver.query(
            ContactsContract.Data.CONTENT_URI, null, contactWhere, contactWhereParams, null)

    var givenName = "";
    var contactType = 0;
    var contactNumber = "";
    cursorPhone?.run {
        if (this.count > 0 && this.moveToNext()) {
            if (cursorPhone.getString(
                            cursorPhone.getColumnIndex(
                                    ContactsContract.Contacts.HAS_PHONE_NUMBER)).toInt() > 0) {
                givenName = cursorPhone.getString(
                        cursorPhone.getColumnIndex(
                                ContactsContract.Contacts.DISPLAY_NAME
                        ))

                contactType = cursorPhone.getInt(
                        cursorPhone.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.TYPE
                        ))
                contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER))
            }
            cursorPhone.moveToNext()
        }
        cursorPhone.close()
    }

    if (contactNumber.startsWith(IDN_CALLING_CODE)) {
        contactNumber.replaceFirst(IDN_CALLING_CODE.toRegex(), "0")
    }
    if (contactNumber.startsWith(IDN_CALLING_CODE_WITH_PLUS)) {
        contactNumber.replace(IDN_CALLING_CODE_WITH_PLUS, "0")
    }
    contactNumber.replace(".", "")

    //noinspection ResultOfMethodCallIgnored
    contactNumber.replace("[^0-9]+".toRegex(), "")
    contactNumber.replace("\\D+".toRegex(), "")
    return DigitalContactData(givenName, contactNumber, contactType)
}

fun String.generateRechargeCheckoutToken(): String {
    val timeMillis = System.currentTimeMillis().toString()
    val token = md5(timeMillis)
    return this + "_" + if (token.isEmpty()) timeMillis else token
}

private fun md5(s: String): String {
    try {
        val digest = MessageDigest.getInstance("MD5")
        digest.update(s.toByteArray())
        val messageDigest = digest.digest()
        val hexString = StringBuilder()
        for (b in messageDigest) {
            hexString.append(String.format("%02x", b and 0xff.toByte()))
        }
        return hexString.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        return ""
    }

}