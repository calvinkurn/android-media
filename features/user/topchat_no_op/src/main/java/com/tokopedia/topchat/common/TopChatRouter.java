package com.tokopedia.topchat.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

/**
 * @author by nisie on 5/18/18.
 */
public interface TopChatRouter {

    String EXTRA_SHOP_STATUS_FAVORITE_FROM_SHOP = "SHOP_STATUS_FAVOURITE";

    Intent getHomeIntent(Context context);

    boolean isIndicatorVisible();

    Intent getTopProfileIntent(Context context, String userId);

    Intent getShopPageIntent(Context context, String shopId);

    Intent getCartIntent(Activity activity);

}
