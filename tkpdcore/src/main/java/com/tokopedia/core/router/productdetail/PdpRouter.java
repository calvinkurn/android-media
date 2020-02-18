package com.tokopedia.core.router.productdetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * @author madi on 5/15/17.
 */

public interface PdpRouter {

    Intent getCartIntent(Activity activity);

    Intent getLoginIntent(Context context);

    Intent getShopPageIntent(Context context, String shopId);
}