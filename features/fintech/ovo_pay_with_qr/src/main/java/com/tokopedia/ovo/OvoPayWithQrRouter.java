package com.tokopedia.ovo;

import android.content.Context;
import android.content.Intent;

public interface OvoPayWithQrRouter {
    Intent getHomeIntent(Context context);

    long getMinAmountFromRemoteConfig();

    long getMaxAmountFromRemoteConfig();

    Intent getOvoActivityIntent(Context context);
}
