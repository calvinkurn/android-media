package com.tokopedia.posapp.common;

import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author okasurya on 5/24/18.
 */
public class PosCacheWhitelist {
    private static final long THREE_HOURS = TimeUnit.HOURS.toSeconds(3);
    private static List<CacheApiWhiteListDomain> list;

    public static List<CacheApiWhiteListDomain> getWhitelist() {
        if(list == null) {
            list = new ArrayList<>();
            list.add(new CacheApiWhiteListDomain(PosUrl.POS_DOMAIN, PosUrl.Product.BASE_CUSTOMER_PRODUCT_LIST, THREE_HOURS));
            list.add(new CacheApiWhiteListDomain(PosUrl.POS_DOMAIN, PosUrl.Product.GET_ETALASE, THREE_HOURS));
        }

        return list;
    }
}
