package com.tokopedia.saldodetails.router;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.saldodetails.activity.SaldoDepositActivity;

public class SaldoDetailsInternalRouter {

    public static Intent getSaldoDepositIntent(Context context) {
        return SaldoDepositActivity.createInstance(context);
    }
}
