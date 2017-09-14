package com.tokopedia.core.router;

import android.content.Context;
import android.content.Intent;

/**
 * @author by nisie on 9/14/17.
 */

public interface TkpdInboxRouter {
    Intent getAskBuyerIntent(Context context, String toUserId,
                             String customerName, String customSubject, String customMessage);

    Intent getAskSellerIntent(Context context, String toShopId,
                              String shopName, String customSubject, String customMessage);

    Intent getAskSellerIntent(Context context, String toShopId, String shopName);

    Intent getAskUserIntent(Context context, String toUserId, String userName);

    Intent getAskSellerIntent(Context context, String toShopId, String shopName, String customSubject);
}
