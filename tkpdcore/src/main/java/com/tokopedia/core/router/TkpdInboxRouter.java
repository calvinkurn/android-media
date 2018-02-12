package com.tokopedia.core.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * @author by nisie on 9/14/17.
 */

public interface TkpdInboxRouter {

    String ENABLE_TOPCHAT = "enable_topchat";

    String TX_ASK_SELLER = "tx_ask_seller";
    String TX_ASK_BUYER = "tx_ask_buyer";
    String SHOP = "shop";
    String PRODUCT = "product";
    String PROFILE = "profile";


    Intent getAskBuyerIntent(Context context, String toUserId,
                             String customerName, String customSubject,
                             String customMessage, String source, String avatarUrl);

    Intent getAskSellerIntent(Context context, String toShopId,
                              String shopName, String customSubject,
                              String customMessage, String source, String avatarUrl);

    Intent getAskSellerIntent(Context context, String toShopId, String shopName, String source,
                              String avatarUrl);

    Intent getAskUserIntent(Context context, String toUserId, String userName, String source, String avatarUrl);

    Intent getTimeMachineIntent(Context context);

    Intent getInboxMessageIntent(Context context);

    Intent getContactUsIntent(Context context);

    Intent getGalleryIntent(Context context, boolean forceOpenCamera, int maxImageSelection, boolean compressToTkpd);
}
