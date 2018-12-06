//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tokopedia.instantloan.ddcollector.sms

import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.provider.Telephony

import com.tokopedia.instantloan.ddcollector.BaseContentCollector

import java.util.ArrayList

/**
 * For reading the message from device
 * MESSAGE_TYPE_ALL    = 0;
 * MESSAGE_TYPE_INBOX  = 1;
 * MESSAGE_TYPE_SENT   = 2;
 * MESSAGE_TYPE_DRAFT  = 3;
 * MESSAGE_TYPE_OUTBOX = 4;
 * MESSAGE_TYPE_FAILED = 5; // for failed outgoing messages
 * MESSAGE_TYPE_QUEUED = 6; // for messages to send later
 */
class Sms(contentResolver: ContentResolver) : BaseContentCollector(contentResolver) {

    override fun getType(): String {
        return DD_SMS
    }

    override fun getParameters(): List<String> {
        val params = ArrayList<String>()
        params.add(Telephony.Sms.Inbox.TYPE)
        params.add(Telephony.Sms.Inbox.ADDRESS)
        params.add(Telephony.Sms.Inbox.BODY)
        params.add(Telephony.Sms.Inbox.DATE)
        return params
    }

    override fun buildUri(): Uri {
        return if (VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) android.provider.Telephony.Sms.CONTENT_URI else Uri.parse("content://sms")
    }

    override fun getLimit(): Int {
        return 1000
    }

    companion object {

        val DD_SMS = "sms"
    }
}
