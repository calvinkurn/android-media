package com.tokopedia.otp;

import android.content.Context;
import android.content.Intent;

/**
 * @author by nisie on 4/24/18.
 */

public interface OtpModuleRouter {

    Intent getChangePhoneNumberRequestIntent(Context context, String userId, String oldPhoneNumber);

    Intent getPhoneVerificationActivationIntent(Context context);

    Intent getProfileSettingIntent(Context context);
}
