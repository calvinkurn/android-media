package com.tokopedia.core.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

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

    String ENABLE_GROUPCHAT = "enable_groupchat";
    String IS_CHAT_BOT = "is_chat_bot";


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

    Intent getGalleryIntent(Context context, boolean forceOpenCamera, int maxImageSelection, boolean compressToTkpd);

    Fragment getChannelFragment(Bundle bundle);

    String getChannelFragmentTag();

    Intent getLoginIntent(Context context);

    void actionNavigateByApplinksUrl(Activity activity, String url, Bundle bundle);

    Intent getTopProfileIntent(Context context, String userId);

    Intent getProductDetailIntent(Context context, ProductPass productPass);

    void startAddProduct(Activity activity, String shopId);

    boolean isEnabledGroupChat();
}
