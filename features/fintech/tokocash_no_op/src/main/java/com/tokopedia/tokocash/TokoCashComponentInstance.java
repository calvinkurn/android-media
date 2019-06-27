package com.tokopedia.tokocash;

import android.app.Application;

import com.tokopedia.tokocash.common.di.TokoCashComponent;

/**
 * Created by nabillasabbaha on 09/05/18.
 */
public class TokoCashComponentInstance {

    private static TokoCashComponent tokoCashComponent;

    public static TokoCashComponent getComponent(Application application) {
        return tokoCashComponent;
    }
}
