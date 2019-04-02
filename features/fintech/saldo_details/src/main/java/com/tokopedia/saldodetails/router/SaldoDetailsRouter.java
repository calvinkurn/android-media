package com.tokopedia.saldodetails.router;

import android.content.Context;
import android.content.Intent;

public interface SaldoDetailsRouter {
    Intent getWithdrawIntent(Context context, boolean isSeller);

    Intent getAddPasswordIntent(Context context);

    boolean isSaldoNativeEnabled();

    boolean isMerchantCreditLineEnabled();

    Intent getInboxTicketCallingIntent(Context context);

    Intent getProfileSettingIntent(Context context);

    Intent getHomeIntent(Context context);
}
