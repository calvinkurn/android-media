package com.tokopedia.buyerorder.common.util;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public interface UnifiedOrderListRouter {
    Fragment getFlightOrderListFragment();
    Intent getOrderHistoryIntent(Context context, String orderId);
}
