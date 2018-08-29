package com.tokopedia.topads;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsComponent;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;

/**
 * Created by hadi.putra on 25/04/18.
 */

public class TopAdsComponentInstance {
    public static TopAdsComponent topAdsComponent;

    public static TopAdsComponent getComponent(Application application){
        if (topAdsComponent == null){
            topAdsComponent = DaggerTopAdsComponent.builder().baseAppComponent(
                    ((BaseMainApplication) application).getBaseAppComponent()).build();
        }
        return topAdsComponent;
    }
}
