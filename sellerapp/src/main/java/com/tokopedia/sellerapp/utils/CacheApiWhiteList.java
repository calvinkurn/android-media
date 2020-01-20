package com.tokopedia.sellerapp.utils;

import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.gm.common.util.GMCacheApiWhiteList;
import com.tokopedia.imagepicker.picker.instagram.util.InstagramCacheApiWhiteList;
import com.tokopedia.seller.product.common.utils.ProductCacheApiWhiteList;
import com.tokopedia.seller.shop.common.utils.ShopOpenCacheApiWhiteList;
import com.tokopedia.shop.common.util.ShopCacheApiWhiteList;
import com.tokopedia.topads.common.util.TopAdsCacheApiWhiteList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by nathan on 9/16/17.
 */

public class CacheApiWhiteList {

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
        // Notification
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(
                TkpdBaseURL.User.URL_NOTIFICATION + TkpdBaseURL.User.PATH_GET_NOTIFICATION, THIRTY_SECOND));

        cacheApiWhiteList.addAll(ShopOpenCacheApiWhiteList.getWhiteList());
        cacheApiWhiteList.addAll(ShopCacheApiWhiteList.getWhiteList());
        cacheApiWhiteList.addAll(ProductCacheApiWhiteList.getWhiteList());
        cacheApiWhiteList.addAll(GMCacheApiWhiteList.getWhiteList());
        cacheApiWhiteList.addAll(TopAdsCacheApiWhiteList.getWhiteList());
        cacheApiWhiteList.addAll(InstagramCacheApiWhiteList.getWhiteList());
        return cacheApiWhiteList;
    }
}
