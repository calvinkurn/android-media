package com.tokopedia.sellerapp.utils;

import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.gm.statistic.constant.StatisticConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by nathan on 9/16/17.
 */

public class WhitelistUtils {

    private static final long THIRTY_SECOND = TimeUnit.SECONDS.toSeconds(30);
    private static final long ONE_MINUTE = TimeUnit.MINUTES.toSeconds(1);
    private static final long FIVE_MINUTE = TimeUnit.MINUTES.toSeconds(5);
    private static final long ONE_DAY = TimeUnit.HOURS.toSeconds(24);

    public static List<CacheApiWhiteListDomain> getWhiteList() {
        List<CacheApiWhiteListDomain> cacheApiWhiteList = new ArrayList<>();
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.BASE_DOMAIN,
                TkpdBaseURL.Transaction.URL_DEPOSIT + TkpdBaseURL.Transaction.PATH_GET_DEPOSIT, THIRTY_SECOND));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.MOJITO_DOMAIN,
                TkpdBaseURL.Home.PATH_API_V1_ANNOUNCEMENT_TICKER, ONE_MINUTE));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.BASE_DOMAIN,
                TkpdBaseURL.User.URL_NOTIFICATION + TkpdBaseURL.User.PATH_GET_NOTIFICATION, THIRTY_SECOND));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.BASE_DOMAIN,
                TkpdBaseURL.Shop.PATH_SHOP + TkpdBaseURL.Shop.PATH_GET_SHOP_INFO, FIVE_MINUTE));

        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.GOLD_MERCHANT_DOMAIN,
                StatisticConstant.GET_TRANSACTION_GRAPH_URL, ONE_DAY));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.GOLD_MERCHANT_DOMAIN,
                StatisticConstant.GET_TRANSACTION_TABLE_URL, ONE_DAY));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.GOLD_MERCHANT_DOMAIN,
                StatisticConstant.GET_PRODUCT_GRAPH, ONE_DAY));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.GOLD_MERCHANT_DOMAIN,
                StatisticConstant.GET_POPULAR_PRODUCT, ONE_DAY));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.GOLD_MERCHANT_DOMAIN,
                StatisticConstant.GET_BUYER_GRAPH, ONE_DAY));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.GOLD_MERCHANT_DOMAIN,
                StatisticConstant.GET_KEYWORD, ONE_DAY));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.GOLD_MERCHANT_DOMAIN,
                StatisticConstant.GET_SHOP_CATEGORY, ONE_DAY));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.GOLD_MERCHANT_DOMAIN,
                StatisticConstant.GET_PRODUCT_TABLE, ONE_DAY));
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.GOLD_MERCHANT_DOMAIN,
                StatisticConstant.GET_BUYER_TABLE, ONE_DAY));
        return cacheApiWhiteList;
    }
}
