package com.tokopedia.core.router;

import android.content.Context;
import android.content.Intent;

/**
 * @author by nisie on 9/14/17.
 */

public interface TkpdInboxRouter {
    String TX_ASK_SELLER = "tx_ask_seller";
    String TX_ASK_BUYER = "tx_ask_buyer";
    String SHOP = "shop";
    String PRODUCT = "product";
    String PROFILE = "profile";


    Intent getAskBuyerIntent(Context context, String toUserId,
                             String customerName, String customSubject,
                             String customMessage, String source);

    Intent getAskSellerIntent(Context context, String toShopId,
                              String shopName, String customSubject,
                              String customMessage, String source);

    Intent getAskSellerIntent(Context context, String toShopId, String shopName, String source);

    Intent getAskUserIntent(Context context, String toUserId, String userName, String source);

    Intent getAskSellerIntent(Context context, String toShopId, String shopName,
                              String customSubject, String source);
}
