package com.tokopedia.mitratoppers;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.mitratoppers.common.di.component.DaggerMitraToppersComponent;
import com.tokopedia.mitratoppers.common.di.component.MitraToppersComponent;
import com.tokopedia.mitratoppers.common.di.module.MitraToppersModule;

/**
 * Created by nakama on 18/01/18.
 */

public class MitraToppersComponentInstance {
    private static MitraToppersComponent mitraToppersComponentInstance;

    public static MitraToppersComponent get(BaseMainApplication baseMainApplication) {
        if (mitraToppersComponentInstance == null) {
            mitraToppersComponentInstance = DaggerMitraToppersComponent.builder()
                    .baseAppComponent(baseMainApplication.getBaseAppComponent())
                    .mitraToppersModule(new MitraToppersModule()).build();
        }
        return mitraToppersComponentInstance;
    }
}
