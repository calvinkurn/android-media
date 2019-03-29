package com.tokopedia.tokocash;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.tokocash.common.di.DaggerTokoCashComponent;
import com.tokopedia.tokocash.common.di.TokoCashComponent;

/**
 * Created by nabillasabbaha on 09/05/18.
 */
public class TokoCashComponentInstance {

    private static TokoCashComponent tokoCashComponent;

    public static TokoCashComponent getComponent(Application application) {
        if (tokoCashComponent == null) {
            tokoCashComponent = DaggerTokoCashComponent.builder()
                    .baseAppComponent(((BaseMainApplication) application).getBaseAppComponent())
                    .build();
        }
        return tokoCashComponent;
    }
}
