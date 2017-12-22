package com.tokopedia.sellerapp.utils;

import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.seller.product.variant.data.cloud.api.TomeApi;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by nathan on 9/16/17.
 */

public class WhitelistUtils {

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
        // Depositc
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(
                TkpdBaseURL.Transaction.URL_DEPOSIT + TkpdBaseURL.Transaction.PATH_GET_DEPOSIT, THIRTY_SECOND));
        // Ticker
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.MOJITO_DOMAIN,
                TkpdBaseURL.Home.PATH_API_V1_ANNOUNCEMENT_TICKER, ONE_MINUTE));
        // Notification
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(
                TkpdBaseURL.User.URL_NOTIFICATION + TkpdBaseURL.User.PATH_GET_NOTIFICATION, THIRTY_SECOND));
        // Shop info
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.BASE_DOMAIN,
                TkpdBaseURL.Shop.PATH_SHOP + TkpdBaseURL.Shop.PATH_GET_SHOP_INFO, THREE_HOURS));

        // Product
        // Product Recommended category
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.MERLIN_DOMAIN,
                TkpdBaseURL.Merlin.PATH_CATEGORY_RECOMMENDATION, ONE_DAY));
        // Product variant by category
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.TOME_DOMAIN,
                TomeApi.GET_VARIANT_BY_CAT_PATH, ONE_DAY));
        // Product catalog
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.ACE_DOMAIN,
                TkpdBaseURL.Ace.PATH_SEARCH + TkpdBaseURL.Ace.PATH_CATALOG, ONE_DAY));

        // TopAds
        // TopAds Deposit
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.TOPADS_DOMAIN,
                TopAdsNetworkConstant.PATH_DASHBOARD_DEPOSIT, TEN_SECOND));

        // Topads Credit
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.TOPADS_DOMAIN,
                TopAdsNetworkConstant.PATH_DASHBOARD_CREDIT, ONE_HOUR));

        // TopAds Statistic
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.TOPADS_DOMAIN,
                TopAdsNetworkConstant.PATH_DASHBOARD_STATISTIC, FIVE_MINUTE));

        // Open Shop
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.BASE_DOMAIN,
                TkpdBaseURL.Shop.PATH_MY_SHOP + TkpdBaseURL.Shop.PATH_GET_OPEN_SHOP_FORM, ONE_HOUR));

        return cacheApiWhiteList;
    }
}
