package com.tokopedia.core.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author by nisie on 9/14/17.
 */

public interface TkpdInboxRouter {

    String TX_ASK_SELLER = "tx_ask_seller";
    String TX_ASK_BUYER = "tx_ask_buyer";
    String SHOP = "shop";

    String IS_CHAT_BOT = "is_chat_bot";


    Intent getAskBuyerIntent(Context context, String toUserId,
                             String customerName, String customSubject,
                             String customMessage, String source, String avatarUrl);

    Intent getAskSellerIntent(Context context, String toShopId,
                              String shopName, String customSubject,
                              String customMessage, String source, String avatarUrl);

    Intent getHomeIntent(Context context);

    void actionNavigateByApplinksUrl(Activity activity, String url, Bundle bundle);

    boolean isSupportedDelegateDeepLink(String url);
}