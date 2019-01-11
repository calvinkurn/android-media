package com.tokopedia.instantloan.router;

import android.content.Context;
import android.content.Intent;

public interface InstantLoanRouter {
    Intent getInstantLoanActivityIntent(Context context);

    Intent getLoginIntent(Context context);

    boolean isInstantLoanEnabled();
}
