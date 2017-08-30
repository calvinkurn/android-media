package com.tokopedia.discovery.helper;

import android.text.TextUtils;

import java.util.Arrays;

/**
 * Created by henrypriyono on 7/4/17.
 */

public class OfficialStoreQueryHelper {
    public static final String[] OFFICIAL_STORE_QUERY_KEYWORD_LIST = {
        "official store",
        "officialstore",
        "official-store"
    };

    public static boolean isOfficialStoreSearchQuery(String query) {
        return !TextUtils.isEmpty(query)
                && Arrays.asList(OFFICIAL_STORE_QUERY_KEYWORD_LIST).contains(query);
    }
}
