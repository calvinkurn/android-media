package com.tokopedia.instantloan.ddcollector.contact

import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract

import com.tokopedia.instantloan.ddcollector.BaseContentCollector

import java.util.ArrayList

class Contact(contentResolver: ContentResolver) : BaseContentCollector(contentResolver) {

    override fun getType(): String {
        return DD_CONTACT
    }

    override fun getParameters(): List<String> {
        val params = ArrayList<String>()
        params.add(ContactsContract.CommonDataKinds.Phone.NUMBER)
        params.add(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        params.add(ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED)
        params.add(ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED)
        params.add(ContactsContract.CommonDataKinds.Phone.CONTACT_STATUS_TIMESTAMP)
        return params
    }

    override fun getLimit(): Int {
        return -1
    }

    override fun buildUri(): Uri {
        return ContactsContract.CommonDataKinds.Phone.CONTENT_URI
    }

    companion object {

        val DD_CONTACT = "contact"
    }
}
