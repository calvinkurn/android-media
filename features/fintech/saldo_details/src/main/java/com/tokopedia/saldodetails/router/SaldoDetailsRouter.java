package com.tokopedia.saldodetails.router;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.abstraction.common.data.model.storage.CacheManager;

public interface SaldoDetailsRouter {
    Intent getWithdrawIntent(Context context);

    Intent getAddPasswordIntent(Context context);

    boolean isSaldoNativeEnabled();

    CacheManager getGlobalCacheManager();

    Intent getInboxTicketCallingIntent(Context context);

    Intent getProfileSettingIntent(Context context);

    Intent getHomeIntent(Context context);
}
