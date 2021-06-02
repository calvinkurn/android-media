package com.tokopedia.applink.telephony_masking

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.ContactsContract
import com.tokopedia.applink.telephony_masking.TelephonyMaskingConst.CONTACT_NAME
import com.tokopedia.applink.telephony_masking.TelephonyMaskingConst.CONTACT_NUMBERS_DEFAULT
import com.tokopedia.applink.telephony_masking.TelephonyMaskingConst.TELEPHONY_MASKING_KEY
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import tokopedia.applink.R
import java.io.ByteArrayOutputStream
import kotlin.collections.ArrayList


class TelephonyMaskingRedirectionUtil {

    private var remoteConfig: RemoteConfig? = null

    fun saveTokopediaPhoneNumbers(context: Context) {
        val intent = createIntentToPutContacts()
        val data = ArrayList<ContentValues>()
        addMultipleNumbers(data, getListNumbers(context))
        addImageToContact(context, data)
        intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data)

        context.startActivity(intent)
    }

    private fun createIntentToPutContacts(): Intent {
        return Intent(ContactsContract.Intents.Insert.ACTION).apply {
            type = ContactsContract.RawContacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.NAME, CONTACT_NAME)
//            putExtra(ContactsContract.Intents.Insert.EMAIL, email)
//            putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE,
//                    ContactsContract.CommonDataKinds.Email.TYPE_WORK)
        }
    }

    private fun addMultipleNumbers(
            data: ArrayList<ContentValues>,
            phoneNumbers: List<String>
    ) {
        for (i in phoneNumbers.indices) {
            val row = ContentValues()
            row.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            row.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumbers.get(i))
            row.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
            data.add(row)
        }
    }

    private fun addImageToContact(
            context: Context,
            data: ArrayList<ContentValues>
    ) {
        try {
            val bit = BitmapFactory.decodeResource(context.resources, R.drawable.tokopedia)
            val row = ContentValues()
            row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
            row.put(ContactsContract.CommonDataKinds.Photo.PHOTO, bitmapToByteArray(bit))
            data.add(row)
        } catch (ignored: Throwable) {}
    }

    private fun bitmapToByteArray(bit: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        bit.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        bit.recycle()
        return byteArray
    }

    private fun getListNumbers(context: Context): List<String> {
        if(remoteConfig == null) {
            remoteConfig = FirebaseRemoteConfigImpl(context)
        }
        val contactNumbers = remoteConfig?.getString(TELEPHONY_MASKING_KEY, CONTACT_NUMBERS_DEFAULT)
                ?: CONTACT_NUMBERS_DEFAULT
        return contactNumbers.split(",")
    }
}