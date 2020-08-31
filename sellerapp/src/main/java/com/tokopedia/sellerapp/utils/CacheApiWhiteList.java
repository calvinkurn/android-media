package com.tokopedia.sellerapp.utils;

import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.gm.common.util.GMCacheApiWhiteList;
import com.tokopedia.imagepicker.picker.instagram.util.InstagramCacheApiWhiteList;
import com.tokopedia.seller.shop.common.utils.ShopOpenCacheApiWhiteList;
import com.tokopedia.topads.common.util.TopAdsCacheApiWhiteList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 9/16/17.
 */

public class CacheApiWhiteList {

    public static List<CacheApiWhiteListDomain> getWhiteList() {
        List<CacheApiWhiteListDomain> cacheApiWhiteList = new ArrayList<>();
        cacheApiWhiteList.addAll(ShopOpenCacheApiWhiteList.getWhiteList());
        cacheApiWhiteList.addAll(GMCacheApiWhiteList.getWhiteList());
        cacheApiWhiteList.addAll(TopAdsCacheApiWhiteList.getWhiteList());
        cacheApiWhiteList.addAll(InstagramCacheApiWhiteList.getWhiteList());
        return cacheApiWhiteList;
    }
}
