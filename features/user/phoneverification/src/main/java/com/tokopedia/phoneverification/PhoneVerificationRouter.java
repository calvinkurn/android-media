package com.tokopedia.phoneverification;

import android.content.Context;
import android.content.Intent;

/**
 * @author by alvinatin on 16/10/18.
 */

public interface PhoneVerificationRouter {

    Intent getHomeIntent(Context context);

    Intent getIntentCreateShop(Context context);

    Intent getProfileSettingIntent(Context context);

}
