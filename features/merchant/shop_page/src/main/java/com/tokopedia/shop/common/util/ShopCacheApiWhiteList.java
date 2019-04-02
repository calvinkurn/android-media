package com.tokopedia.shop.common.util;

import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.gm.common.constant.GMCommonUrl;
import com.tokopedia.reputation.common.constant.ReputationCommonUrl;
import com.tokopedia.shop.common.constant.ShopCommonUrl;
import com.tokopedia.shop.common.constant.ShopUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by nathan on 12/28/17.
 */

public class ShopCacheApiWhiteList {

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

        // Shop info
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(ShopCommonUrl.BASE_URL, ShopCommonUrl.SHOP_INFO_PATH, ONE_HOUR));

        // Shop statistic speed
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(ReputationCommonUrl.BASE_URL, ReputationCommonUrl.STATISTIC_SPEED_URL, ONE_HOUR, true));

        // Shop statistic speed_v2
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(ReputationCommonUrl.BASE_URL, ReputationCommonUrl.STATISTIC_SPEED_URL_V2, ONE_HOUR, true));

        // Featured Product
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(GMCommonUrl.BASE_URL, GMCommonUrl.FEATURED_PRODUCT_URL, ONE_HOUR, true));

        // Shop note
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(ShopCommonUrl.BASE_URL, ShopUrl.SHOP_NOTE_PATH, ONE_HOUR));

        // Shop Product list
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(ShopUrl.BASE_ACE_URL, ShopUrl.SHOP_PRODUCT_PATH, ONE_HOUR));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(ShopUrl.BASE_URL, ShopUrl.SHOP_PRODUCT_PATH, ONE_HOUR));

        // Shop Filter
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(ShopUrl.BASE_ACE_URL, ShopUrl.SHOP_DYNAMIC_FILTER, ONE_HOUR));

        // Shop Favourite
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(ShopUrl.BASE_WS_URL, ShopUrl.SHOP_FAVOURITE_USER, ONE_HOUR));

        // Official store discount
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(ShopUrl.BASE_OFFICIAL_STORE_URL, ShopUrl.SHOP_PRODUCT_OS_DISCOUNT, ONE_HOUR));
        return cacheApiWhiteList;
    }
}