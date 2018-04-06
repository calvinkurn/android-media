package com.tokopedia.tkpdcontent;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.tkpdcontent.common.di.DaggerKolComponent;
import com.tokopedia.tkpdcontent.common.di.KolComponent;

/**
 * @author by milhamj on 20/02/18.
 */

public class KolComponentInstance {
    private static KolComponent kolComponent;

    public static KolComponent getKolComponent(Application application) {
        if (kolComponent == null) {
            kolComponent = DaggerKolComponent.builder()
                    .baseAppComponent(((BaseMainApplication) application).getBaseAppComponent())
                    .build();
        }
        return kolComponent;
    }
}
