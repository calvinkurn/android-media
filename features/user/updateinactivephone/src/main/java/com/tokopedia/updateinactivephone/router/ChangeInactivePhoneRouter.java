package com.tokopedia.updateinactivephone.router;

import android.content.Context;
import android.content.Intent;

public interface ChangeInactivePhoneRouter {
    Intent getLoginIntent(Context context);
}
