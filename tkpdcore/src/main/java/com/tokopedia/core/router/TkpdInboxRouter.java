package com.tokopedia.core.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.shopinfo.ShopInfoActivity;

/**
 * @author by nisie on 9/14/17.
 */

public interface TkpdInboxRouter {
    Intent getAskBuyerIntent(Context context, String toUserId,
                             String customerName, String customSubject, String customMessage);

    Intent getAskSellerIntent(Context context, String toShopId,
                              String shopName, String customSubject, String customMessage);

    Intent getAskSellerIntent(Context context, String shopId, String shopName);

    Intent getAskUserIntent(Context context, String userId, String userName);

    Intent getAskSellerIntent(Context context, String toShopId, String shopName, String customSubject);
}
