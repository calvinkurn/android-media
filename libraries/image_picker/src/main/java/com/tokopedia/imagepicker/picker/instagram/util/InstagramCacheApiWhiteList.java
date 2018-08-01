package com.tokopedia.imagepicker.picker.instagram.util;

import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by zulfikarrahman on 6/8/18.
 */

public class InstagramCacheApiWhiteList {
    private static final long TEN_SECOND = TimeUnit.SECONDS.toSeconds(10);
    private static final long THIRTY_SECOND = TimeUnit.SECONDS.toSeconds(30);
    private static final long ONE_MINUTE = TimeUnit.MINUTES.toSeconds(1);
    private static final long FIVE_MINUTE = TimeUnit.MINUTES.toSeconds(5);
    private static final long FIFTEEN_MINUTE = TimeUnit.MINUTES.toSeconds(15);
    private static final long ONE_HOUR = TimeUnit.HOURS.toSeconds(1);
    private static final long THREE_HOURS = TimeUnit.HOURS.toSeconds(3);
    private static final long ONE_DAY = TimeUnit.HOURS.toSeconds(24);

    public static List<CacheApiWhiteListDomain> getWhiteList() {
        List<CacheApiWhiteListDomain> cacheApiWhiteList = new ArrayList<>();

        // Opportunity category
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(InstagramConstant.URL_API_INSTAGRAM, InstagramConstant.URL_PATH_GET_LIST_MEDIA, ONE_HOUR));

        return cacheApiWhiteList;
    }
}
