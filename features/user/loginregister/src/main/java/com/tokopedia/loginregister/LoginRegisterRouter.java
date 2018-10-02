package com.tokopedia.loginregister;

import android.content.Context;
import android.content.Intent;

/**
 * @author by nisie on 10/2/18.
 */
public interface LoginRegisterRouter {
    Intent getForgotPasswordIntent(Context context, String email);

    void setTrackingUserId(String userId);
}
