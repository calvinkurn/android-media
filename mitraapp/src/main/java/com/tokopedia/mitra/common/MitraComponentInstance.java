package com.tokopedia.mitra.common;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.mitra.common.di.DaggerMitraComponent;
import com.tokopedia.mitra.common.di.MitraComponent;

public class MitraComponentInstance {
    private static MitraComponent mitraComponent;

    public static MitraComponent getComponent(Application application) {
        if (mitraComponent == null) {
            mitraComponent = DaggerMitraComponent.builder().baseAppComponent(
                    ((BaseMainApplication)application).getBaseAppComponent()).build();
        }
        return mitraComponent;
    }
}
