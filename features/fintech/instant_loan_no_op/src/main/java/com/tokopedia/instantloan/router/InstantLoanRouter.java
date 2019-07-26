package com.tokopedia.instantloan.router;

import android.content.Context;
import android.content.Intent;

public interface InstantLoanRouter {

    Intent getLoginIntent(Context context);

    boolean isInstantLoanEnabled();
}
