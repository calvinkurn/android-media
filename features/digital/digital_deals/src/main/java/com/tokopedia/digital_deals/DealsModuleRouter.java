package com.tokopedia.digital_deals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public interface DealsModuleRouter {

    Intent tkpdCartCheckoutGetLoyaltyOldCheckoutCouponActiveIntent(Context context, String platform, String category, String defaultSelectedTab);
}
