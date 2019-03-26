package com.tokopedia.instantdebitbca.data.view.utils;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.instantdebitbca.data.di.DaggerInstantDebitBcaComponent;
import com.tokopedia.instantdebitbca.data.di.InstantDebitBcaComponent;

/**
 * Created by nabillasabbaha on 25/03/19.
 */
public class InstantDebitBcaInstance {

    private static InstantDebitBcaComponent instantDebitBcaComponent;

    public static InstantDebitBcaComponent getComponent(Application application) {
        if (instantDebitBcaComponent == null) {
            instantDebitBcaComponent = DaggerInstantDebitBcaComponent.builder().baseAppComponent(
                    ((BaseMainApplication) application).getBaseAppComponent()).build();
        }
        return instantDebitBcaComponent;
    }
}
