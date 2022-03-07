package com.tokopedia.common.topupbills.data.source

import android.content.ContentResolver
import android.provider.ContactsContract
import com.tokopedia.common.topupbills.data.TopupBillsContact

class ContactDataSourceImpl(
    private val contentResolver: ContentResolver
): ContactDataSource {
    private val contactsProjection: Array<out String> = arrayOf(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI
    )

    override fun getContactList(): MutableList<TopupBillsContact> {
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        val cursor = contentResolver.query(uri, contactsProjection, null, null, sort)

        val contacts = mutableListOf<TopupBillsContact>()

        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber = cursor.getString(cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER))
                contacts.add(TopupBillsContact(name, formatPrefixClientNumber(phoneNumber)))
            }
        }
        cursor?.close()

        return contacts
    }

    private fun formatPrefixClientNumber(phoneNumber: String?): String {
        phoneNumber?.run {
            if ("".equals(phoneNumber.trim { it <= ' ' }, ignoreCase = true)) {
                return phoneNumber
            }
            var phoneNumberWithPrefix = validatePrefixClientNumber(phoneNumber)
            if (!phoneNumberWithPrefix.startsWith("0")) {
                phoneNumberWithPrefix = "0$phoneNumber"
            }
            return phoneNumberWithPrefix
        }
        return ""
    }

    private fun validatePrefixClientNumber(phoneNumber: String): String {
        var phoneNumber = phoneNumber
        if (phoneNumber.startsWith("62")) {
            phoneNumber = phoneNumber.replaceFirst("62".toRegex(), "0")
        }
        if (phoneNumber.startsWith("+62")) {
            phoneNumber = phoneNumber.replace("+62", "0")
        }
        return phoneNumber.replace("[^0-9]+".toRegex(), "")
    }
}