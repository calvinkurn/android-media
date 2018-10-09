//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tokopedia.instantloan.ddcollector.contact;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.ContactsContract;

import com.tokopedia.instantloan.ddcollector.BaseContentCollector;

import java.util.ArrayList;
import java.util.List;

public class Contact extends BaseContentCollector {

    public static final String DD_CONTACT = "contact";

    public Contact(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    public String getType() {
        return DD_CONTACT;
    }

    @Override
    public List<String> getParameters() {
        ArrayList<String> params = new ArrayList<>();
        params.add(ContactsContract.CommonDataKinds.Phone.NUMBER);
        params.add(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        params.add(ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED);
        params.add(ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED);
        params.add(ContactsContract.CommonDataKinds.Phone.CONTACT_STATUS_TIMESTAMP);
        return params;
    }

    @Override
    public int getLimit() {
        return -1;
    }

    @Override
    public Uri buildUri() {
        return ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    }
}
