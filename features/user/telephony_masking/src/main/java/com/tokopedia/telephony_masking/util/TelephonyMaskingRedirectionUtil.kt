package com.tokopedia.telephony_masking.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.ContactsContract
import com.tokopedia.telephony_masking.R
import com.tokopedia.telephony_masking.util.TelephonyMaskingConst.CONTACT_NAME
import com.tokopedia.telephony_masking.util.TelephonyMaskingConst.SAVE_EXTRA
import java.io.ByteArrayOutputStream
import kotlin.collections.ArrayList

class TelephonyMaskingRedirectionUtil {

    fun createIntentSavePhoneNumbers(context: Context, listNumber: List<String>): Intent {
        val intent = createIntentToPutContacts()
        val data = ArrayList<ContentValues>()
        data.addMultipleNumbers(listNumber)
        data.addImageToContact(context)
        intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data)
        intent.putExtra(SAVE_EXTRA, true)

        return intent
    }

    private fun createIntentToPutContacts(): Intent {
        return Intent(ContactsContract.Intents.Insert.ACTION).apply {
            type = ContactsContract.RawContacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.NAME, CONTACT_NAME)
        }
    }

    private fun ArrayList<ContentValues>.addMultipleNumbers(phoneNumbers: List<String>) {
        phoneNumbers.map { number ->
            val row: ContentValues = ContentValues().apply {
                this.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                this.put(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                this.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
            }
            this.add(row)
        }
    }

    private fun ArrayList<ContentValues>.addImageToContact(context: Context) {
        try {
            val bit = BitmapFactory.decodeResource(context.resources, R.drawable.tokopedia_contact)
            val row = ContentValues()
            row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
            row.put(ContactsContract.CommonDataKinds.Photo.PHOTO, bitmapToByteArray(bit))
            this.add(row)
        } catch (ignored: Throwable) {}
    }

    private fun bitmapToByteArray(bit: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        bit.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        bit.recycle()
        return byteArray
    }
}