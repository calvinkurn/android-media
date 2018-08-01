//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tokopedia.instantloan.ddcollector.call;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.CallLog.Calls;

import com.tokopedia.instantloan.ddcollector.BaseContentCollector;

import java.util.ArrayList;
import java.util.List;

public class Call extends BaseContentCollector {

    public static final String DD_CALL = "call";

    public Call(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    public String getType() {
        return DD_CALL;
    }

    @Override
    public List<String> getParameters() {
        List<String> params = new ArrayList();
        params.add(Calls.TYPE);
        params.add(Calls.NUMBER);
        params.add(Calls.DURATION);
        params.add(Calls.DATE);
        return params;
    }

    @Override
    public Uri buildUri() {
        return Calls.CONTENT_URI;
    }

    @Override
    public int getLimit() {
        return -1;
    }
}
