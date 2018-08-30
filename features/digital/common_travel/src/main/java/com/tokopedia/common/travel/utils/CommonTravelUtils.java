package com.tokopedia.common.travel.utils;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.common.travel.di.CommonTravelComponent;
import com.tokopedia.common.travel.di.DaggerCommonTravelComponent;

/**
 * Created by nabillasabbaha on 13/08/18.
 */
public class CommonTravelUtils {
    private static CommonTravelComponent travelComponent;

    public static CommonTravelComponent getTrainComponent(Application application) {
        if (travelComponent == null) {
            travelComponent = DaggerCommonTravelComponent.builder().baseAppComponent(
                    ((BaseMainApplication) application).getBaseAppComponent()).build();
        }
        return travelComponent;
    }
}
