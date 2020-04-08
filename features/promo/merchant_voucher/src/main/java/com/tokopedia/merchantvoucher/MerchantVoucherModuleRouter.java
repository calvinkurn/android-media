package com.tokopedia.merchantvoucher;

import android.app.Activity;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface MerchantVoucherModuleRouter {

    void goToShareShop(Activity activity, String shopId, String shopUrl, String shareLabel);
}
