package com.tokopedia.merchantvoucher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import java.util.Map;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface MerchantVoucherModuleRouter {

    void goToShareShop(Activity activity, String shopId, String shopUrl, String shareLabel);
}
