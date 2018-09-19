package com.tokopedia.saldodetails.di;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;

public class SaldoDetailsComponentInstance {

    private static SaldoDetailsComponent saldoDetailsComponent;

    public static SaldoDetailsComponent getComponent(Application application) {
        if (saldoDetailsComponent == null) {
            saldoDetailsComponent = DaggerSaldoDetailsComponent.builder()
                    .baseAppComponent(((BaseMainApplication) application).getBaseAppComponent())
                    .build();
        }
        return saldoDetailsComponent;
    }
}
