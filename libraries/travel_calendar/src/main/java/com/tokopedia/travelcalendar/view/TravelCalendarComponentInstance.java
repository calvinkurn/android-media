package com.tokopedia.travelcalendar.view;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.travelcalendar.di.DaggerTravelCalendarComponent;
import com.tokopedia.travelcalendar.di.TravelCalendarComponent;

/**
 * Created by nabillasabbaha on 14/05/18.
 */
public class TravelCalendarComponentInstance {

    private static TravelCalendarComponent travelCalendarComponent;

    public static TravelCalendarComponent getComponent(Application application) {
        if (travelCalendarComponent == null) {
            travelCalendarComponent = DaggerTravelCalendarComponent.builder()
                    .baseAppComponent(((BaseMainApplication) application).getBaseAppComponent())
                    .build();
        }
        return travelCalendarComponent;
    }
}
