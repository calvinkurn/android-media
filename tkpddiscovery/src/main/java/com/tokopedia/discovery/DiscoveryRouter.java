package com.tokopedia.discovery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author by nisie on 1/4/18.
 */

public interface DiscoveryRouter {

    Intent getLoginIntent(Context context);

    Intent getShopPageIntent(Context context, String shopId);
}
