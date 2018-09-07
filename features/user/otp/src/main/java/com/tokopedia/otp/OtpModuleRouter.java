package com.tokopedia.otp;

import android.content.Context;
import android.content.Intent;

/**
 * @author by nisie on 4/24/18.
 */

public interface OtpModuleRouter {

    Intent getChangePhoneNumberRequestIntent(Context context);

    Intent getPhoneVerificationActivationIntent(Context context);
}
