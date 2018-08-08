package com.tokopedia.core.router;

import android.app.Activity;
import android.content.Intent;

public interface OtpRouter {

    Intent getRidePhoneNumberActivityIntent(Activity activity);

    Intent getReferralPhoneNumberActivityIntent(Activity activity);
}
