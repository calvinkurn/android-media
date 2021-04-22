package com.tokopedia.logisticaddaddress.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract
import com.tokopedia.logisticaddaddress.domain.model.add_address.ContactData

object AddEditAddressUtil {

    const val IDN_CALLING_CODE = "62"
    const val IDN_CALLING_CODE_WITH_PLUS = "+62"

    fun convertContactUriToData(contentResolver: ContentResolver, contactUri: Uri) : ContactData {
        val id = contactUri.lastPathSegment
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
        return ContactData(givenName, contactNumber, contactType)
    }
}