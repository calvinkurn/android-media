package com.tokopedia.sellerapp.utils;

import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.seller.opportunity.common.util.OpportunityWhiteList;
import com.tokopedia.seller.product.common.utils.ProductWhiteList;
import com.tokopedia.seller.shop.common.utils.ShopWhiteList;
import com.tokopedia.topads.common.util.TopAdsWhiteList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by nathan on 9/16/17.
 */

public class WhiteList {

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
        // Deposit
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(
                TkpdBaseURL.Transaction.URL_DEPOSIT + TkpdBaseURL.Transaction.PATH_GET_DEPOSIT, THIRTY_SECOND));
        // Ticker
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(TkpdBaseURL.MOJITO_DOMAIN,
                TkpdBaseURL.Home.PATH_API_V1_ANNOUNCEMENT_TICKER, ONE_MINUTE));
        // Notification
        cacheApiWhiteList.add(new CacheApiWhiteListDomain(
                TkpdBaseURL.User.URL_NOTIFICATION + TkpdBaseURL.User.PATH_GET_NOTIFICATION, THIRTY_SECOND));

        cacheApiWhiteList.addAll(ShopWhiteList.getWhiteList());
        cacheApiWhiteList.addAll(ProductWhiteList.getWhiteList());
        cacheApiWhiteList.addAll(TopAdsWhiteList.getWhiteList());
        cacheApiWhiteList.addAll(OpportunityWhiteList.getWhiteList());
        return cacheApiWhiteList;
    }
}
