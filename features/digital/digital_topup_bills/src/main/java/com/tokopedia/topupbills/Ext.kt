package com.tokopedia.topupbills

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.support.annotation.ColorRes
import com.tokopedia.topupbills.telco.view.model.DigitalContactData

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
            if (Integer.parseInt(cursorPhone.getString(
                            cursorPhone.getColumnIndex(
                                    ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
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

fun Resources.getColorText(context: Context, @ColorRes resId: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.getColor(resId, context.theme)
    } else {
        this.getColor(resId)
    }
}