package com.tokopedia.saldodetails.router;

import android.content.Context;
import android.content.Intent;

public interface SaldoDetailsRouter {
    Intent getWithdrawIntent(Context context);
    Intent getAddPasswordIntent(Context context);
}
