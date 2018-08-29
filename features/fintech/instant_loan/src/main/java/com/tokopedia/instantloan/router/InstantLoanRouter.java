package com.tokopedia.instantloan.router;

import android.content.Context;
import android.content.Intent;

/**
 * Created by lavekush on 20/03/18.
 */

public interface InstantLoanRouter {
    Intent getInstantLoanActivityIntent(Context context);
    Intent getLoginIntent(Context context);
}
