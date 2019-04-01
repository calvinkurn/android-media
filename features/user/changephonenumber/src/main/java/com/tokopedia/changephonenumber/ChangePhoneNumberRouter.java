package com.tokopedia.changephonenumber;

import android.content.Context;
import android.content.Intent;

import okhttp3.Interceptor;

/**
 * @author by alvinatin on 01/10/18.
 */

public interface ChangePhoneNumberRouter {

    Intent getWithdrawIntent(Context context, boolean isSeller);

    Interceptor getChuckInterceptor();
}
