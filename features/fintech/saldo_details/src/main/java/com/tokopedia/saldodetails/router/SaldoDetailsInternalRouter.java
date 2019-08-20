package com.tokopedia.saldodetails.router;

import android.content.Context;

import com.tokopedia.saldodetails.view.activity.SaldoDepositActivity;

public class SaldoDetailsInternalRouter {

    public static void startSaldoDepositIntent(Context context) {
        context.startActivity(SaldoDepositActivity.createInstance(context));
    }

    public static String getSaldoClassName() {
        return SaldoDepositActivity.class.getName();
    }
}
