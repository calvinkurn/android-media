package com.tokopedia.core.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

/**
 * @author by nisie on 10/19/17.
 */

public interface SessionRouter {
    Intent getTrueCallerIntent(Context context);

    Intent getPhoneVerificationActivationIntent(Context context);

    Intent getPhoneVerificationProfileIntent(Context context);

    Intent getChangePhoneNumberRequestIntent(Context context);
}
