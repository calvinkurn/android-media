//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tokopedia.instantloan.ddcollector.sms;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Telephony;

import com.tokopedia.instantloan.ddcollector.BaseContentCollector;

import java.util.ArrayList;
import java.util.List;

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
public class Sms extends BaseContentCollector {

    public static final String DD_SMS = "sms";

    public Sms(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    public String getType() {
        return DD_SMS;
    }

    @Override
    public List<String> getParameters() {
        List<String> params = new ArrayList<>();
        params.add(Telephony.Sms.Inbox.TYPE);
        params.add(Telephony.Sms.Inbox.ADDRESS);
        params.add(Telephony.Sms.Inbox.BODY);
        params.add(Telephony.Sms.Inbox.DATE);
        return params;
    }

    @Override
    public Uri buildUri() {
        return VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? android.provider.Telephony.Sms.CONTENT_URI : Uri.parse("content://sms");
    }

    @Override
    public int getLimit() {
        return 1000;
    }
}
