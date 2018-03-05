package com.tokopedia.discovery;

import android.content.Context;
import android.content.Intent;

/**
 * @author by nisie on 1/4/18.
 */

public interface DiscoveryRouter {

    Intent getLoginIntent(Context context);

    Intent getShopPageIntent(Context context, String shopId);

    Intent getShopPageIntentByDomain(Context context, String domain);
}
