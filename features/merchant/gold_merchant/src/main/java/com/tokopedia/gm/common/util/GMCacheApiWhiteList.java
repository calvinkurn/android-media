package com.tokopedia.gm.common.util;

import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.gm.common.constant.GMCommonUrl;
import com.tokopedia.gm.statistic.constant.StatisticConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by nathan on 12/28/17.
 */

public class GMCacheApiWhiteList {

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
        // Statistic
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.GOLD_MERCHANT_DOMAIN, StatisticConstant.GET_TRANSACTION_GRAPH_URL, ONE_DAY, true));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.GOLD_MERCHANT_DOMAIN, StatisticConstant.GET_TRANSACTION_TABLE_URL, ONE_DAY, true));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.GOLD_MERCHANT_DOMAIN, StatisticConstant.GET_PRODUCT_GRAPH, ONE_DAY, true));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.GOLD_MERCHANT_DOMAIN, StatisticConstant.GET_POPULAR_PRODUCT, ONE_DAY, true));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.GOLD_MERCHANT_DOMAIN, StatisticConstant.GET_BUYER_GRAPH, ONE_DAY, true));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.GOLD_MERCHANT_DOMAIN, StatisticConstant.GET_KEYWORD, ONE_DAY, true));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.GOLD_MERCHANT_DOMAIN, StatisticConstant.GET_SHOP_CATEGORY, ONE_DAY, true));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.GOLD_MERCHANT_DOMAIN, StatisticConstant.GET_PRODUCT_TABLE, ONE_DAY, true));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.GOLD_MERCHANT_DOMAIN, StatisticConstant.GET_BUYER_TABLE, ONE_DAY, true));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.GOLD_MERCHANT_DOMAIN, GMCommonUrl.FEATURED_PRODUCT_URL, ONE_HOUR, true));
        return cacheApiWhiteList;
    }
}