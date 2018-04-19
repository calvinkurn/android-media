package com.tokopedia.topads.common.util;

import com.tokopedia.topads.common.constant.TopAdsConstant;

/**
 * Created by hadi.putra on 18/04/18.
 */

public class TopAdsAppLinkUtil {

    public static final String TOPADS_PRODUCT_CREATE_APPLINK_FORMAT = "%s?user_id=%s&product_id=%s&shop_id=%s";

    public static String createAppLink(String userId, String productId, String shopId, String source){
        return String.format(TOPADS_PRODUCT_CREATE_APPLINK_FORMAT+"&source=%s",
                TopAdsConstant.TOPADS_APPLINK_PRODUCT_CREATE,
                userId, productId, shopId, source);
    }
}
