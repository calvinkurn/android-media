package com.tokopedia.topads.common.util;

import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.topads.common.constant.TopAdsCommonConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by nathan on 12/28/17.
 */

public class TopAdsCacheApiWhiteList {

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

        // TopAds Deposit
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TopAdsCommonConstant.BASE_DOMAIN_URL,
                TopAdsNetworkConstant.PATH_DASHBOARD_DEPOSIT, TEN_SECOND));

        // TopAds Statistic
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TopAdsCommonConstant.BASE_DOMAIN_URL,
                TopAdsNetworkConstant.PATH_DASHBOARD_STATISTIC, FIVE_MINUTE));

        // TopAds totalAd
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TopAdsCommonConstant.BASE_DOMAIN_URL,
                TopAdsNetworkConstant.PATH_DASHBOARD_TOTAL_AD, ONE_HOUR));

        // Suggestion Bid
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TopAdsCommonConstant.BASE_DOMAIN_URL,
                TopAdsNetworkConstant.GET_SUGGESTION, ONE_HOUR));

        return cacheApiWhiteList;
    }
}
