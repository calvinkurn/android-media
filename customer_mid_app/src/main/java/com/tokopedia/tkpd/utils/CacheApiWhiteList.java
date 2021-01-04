package com.tokopedia.tkpd.utils;

import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.imagepicker.picker.instagram.util.InstagramCacheApiWhiteList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 9/16/17.
 */

public class CacheApiWhiteList {

    public static List<CacheApiWhiteListDomain> getWhiteList() {
        List<CacheApiWhiteListDomain> cacheApiWhiteList = new ArrayList<>();
        cacheApiWhiteList.addAll(InstagramCacheApiWhiteList.getWhiteList());
        return cacheApiWhiteList;
    }

}
