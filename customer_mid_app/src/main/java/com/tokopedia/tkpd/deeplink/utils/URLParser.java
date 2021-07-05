package com.tokopedia.tkpd.deeplink.utils;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import timber.log.Timber;

public class URLParser {

    private Uri uri;

    public URLParser(String url) {
        Timber.d(url);
        uri = Uri.parse(url);
    }

    public String getQuery() {
        return uri.getQuery();
    }

    public ArrayList<String> getSetQueryKey() {
        Set<String> query = uri.getQueryParameterNames();
        ArrayList<String> returnquery = new ArrayList<String>();
        returnquery.addAll(query);
        return returnquery;
    }

    public ArrayList<String> getSetQueryValue() {
        ArrayList<String> keylist = getSetQueryKey();
        ArrayList<String> valuelist = new ArrayList<String>();
        for (int i = 0; i < keylist.size(); i++) {
            valuelist.add(uri.getQueryParameter(keylist.get(i)));
        }
        return valuelist;
    }

    public String getType() {
        List<String> Path = uri.getPathSegments();
        return Path.get(0);
    }
}
