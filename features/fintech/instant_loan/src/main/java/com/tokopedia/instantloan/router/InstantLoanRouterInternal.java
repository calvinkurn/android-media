package com.tokopedia.instantloan.router;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.instantloan.view.activity.InstantLoanActivity;

/**
 * Created by lavekush on 20/03/18.
 */

public class InstantLoanRouterInternal {
    public static Intent getInstantLoanActivityIntent(Context context) {
        return new Intent(context, InstantLoanActivity.class);
    }

}
