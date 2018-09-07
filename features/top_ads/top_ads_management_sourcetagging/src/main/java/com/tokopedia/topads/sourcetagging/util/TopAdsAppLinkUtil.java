package com.tokopedia.topads.sourcetagging.util;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceTaggingConstant;

/**
 * Created by hadi.putra on 18/04/18.
 */

public class TopAdsAppLinkUtil {

    public static final String TOPADS_PRODUCT_CREATE_APPLINK_FORMAT = "%s?"+TopAdsSourceTaggingConstant.PARAM_EXTRA_USER_ID
            +"=%s&"+TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID+"=%s&"+TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID+"=%s";

    public static String createAppLink(String userId, String productId, String shopId, String source){
        return String.format(TOPADS_PRODUCT_CREATE_APPLINK_FORMAT+"&"+TopAdsSourceTaggingConstant.PARAM_KEY_SOURCE+"=%s",
                ApplinkConst.SellerApp.TOPADS_PRODUCT_CREATE,
                userId, productId, shopId, source);
    }
}
