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
    String PRODUCT = "product";

    String IS_CHAT_BOT = "is_chat_bot";
    String INDICATOR_VISIBILITY = "indicator_groupchat";


    Intent getAskBuyerIntent(Context context, String toUserId,
                             String customerName, String customSubject,
                             String customMessage, String source, String avatarUrl);

    Intent getAskSellerIntent(Context context, String toShopId,
                              String shopName, String customSubject,
                              String customMessage, String source, String avatarUrl);

    Intent getHomeIntent(Context context);

    Intent getShopPageIntent(Context context, String shopId);

    Intent getLoginIntent(Context context);

    void actionNavigateByApplinksUrl(Activity activity, String url, Bundle bundle);

    Intent getTopProfileIntent(Context context, String userId);

    Intent getWebviewActivityWithIntent(Context context, String url, String title);

    Intent getWebviewActivityWithIntent(Context context, String url);

    boolean isSupportedDelegateDeepLink(String url);
}