package com.tokopedia.core.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.router.productdetail.passdata.ProductPass;

/**
 * @author by nisie on 9/14/17.
 */

public interface TkpdInboxRouter {

    String TX_ASK_SELLER = "tx_ask_seller";
    String TX_ASK_BUYER = "tx_ask_buyer";
    String SHOP = "shop";
    String PRODUCT = "product";
    String PROFILE = "profile";

    String IS_CHAT_BOT = "is_chat_bot";
    String CREATE_TASK_STACK = "create_task_stack";
    String INDICATOR_VISIBILITY = "indicator_groupchat";


    Intent getAskBuyerIntent(Context context, String toUserId,
                             String customerName, String customSubject,
                             String customMessage, String source, String avatarUrl);

    Intent getAskSellerIntent(Context context, String toShopId,
                              String shopName, String customSubject,
                              String customMessage, String source, String avatarUrl);

    Intent getAskSellerIntent(Context context, String toShopId, String shopName, String source,
                              String avatarUrl);

    Intent getAskUserIntent(Context context, String toUserId, String userName, String source, String avatarUrl);

    Intent getInboxMessageIntent(Context context);

    Intent getContactUsIntent(Context context);

    Intent getHomeIntent(Context context);

    Intent getShopPageIntent(Context context, String shopId);

    Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId);

    Fragment getChannelFragment(Bundle bundle);

    String getChannelFragmentTag();

    Intent getLoginIntent(Context context);

    void actionNavigateByApplinksUrl(Activity activity, String url, Bundle bundle);

    Intent getTopProfileIntent(Context context, String userId);

    void startAddProduct(Activity activity, String shopId);

    Intent getHelpUsIntent(Context context);

    Intent getWebviewActivityWithIntent(Context context, String url, String title);

    Intent getWebviewActivityWithIntent(Context context, String url);

    Intent getChatBotIntent(Context context, String messageId);

    boolean isIndicatorVisible();

    boolean isSupportedDelegateDeepLink(String url);
}