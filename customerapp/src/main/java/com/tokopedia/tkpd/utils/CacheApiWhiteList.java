package com.tokopedia.tkpd.utils;

import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.imagepicker.picker.instagram.util.InstagramCacheApiWhiteList;
//import com.tokopedia.opportunity.common.util.OpportunityCacheApiWhiteList;
import com.tokopedia.seller.product.common.utils.ProductCacheApiWhiteList;
import com.tokopedia.seller.shop.common.utils.ShopOpenCacheApiWhiteList;
import com.tokopedia.shop.common.util.ShopCacheApiWhiteList;
import com.tokopedia.url.TokopediaUrl;

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
        cacheApiWhiteList.addAll(ShopOpenCacheApiWhiteList.getWhiteList());
        cacheApiWhiteList.addAll(ShopCacheApiWhiteList.getWhiteList());
        cacheApiWhiteList.addAll(ProductCacheApiWhiteList.getWhiteList());
        cacheApiWhiteList.addAll(OpportunityCacheApiWhiteList.getWhiteList());
        cacheApiWhiteList.addAll(InstagramCacheApiWhiteList.getWhiteList());
        return cacheApiWhiteList;
    }

    static class OpportunityCacheApiWhiteList {

        private static final long TEN_SECOND = TimeUnit.SECONDS.toSeconds(10);
        private static final long THIRTY_SECOND = TimeUnit.SECONDS.toSeconds(30);
        private static final long ONE_MINUTE = TimeUnit.MINUTES.toSeconds(1);
        private static final long FIVE_MINUTE = TimeUnit.MINUTES.toSeconds(5);
        private static final long FIFTEEN_MINUTE = TimeUnit.MINUTES.toSeconds(15);
        private static final long ONE_HOUR = TimeUnit.HOURS.toSeconds(1);
        private static final long THREE_HOURS = TimeUnit.HOURS.toSeconds(3);
        private static final long ONE_DAY = TimeUnit.HOURS.toSeconds(24);

        static List<CacheApiWhiteListDomain> getWhiteList() {
            List<CacheApiWhiteListDomain> cacheApiWhiteList = new ArrayList<>();

            // Opportunity category
            cacheApiWhiteList.add(new CacheApiWhiteListDomain(TokopediaUrl.Companion.getInstance().getWS(), "/v4/order/replacement/category", ONE_DAY));

            return cacheApiWhiteList;
        }
    }
}
