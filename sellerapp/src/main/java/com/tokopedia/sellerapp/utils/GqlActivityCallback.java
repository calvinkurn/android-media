package com.tokopedia.sellerapp.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tokopedia.graphql.data.GraphqlClient;

/**
 * Created by devarafikry on 09/03/2022
 */
@SuppressLint("NewApi")
public class GqlActivityCallback implements Application.ActivityLifecycleCallbacks {
    private static final String DEFAULT_MODULE_NAME = "tkpd";

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        updateModuleName(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        updateModuleName(activity);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    private void updateModuleName(Activity activity) {
        String packageName = activity.getPackageName();
        if (packageName != null) {
            String[] splittedPackageName = packageName.split("\\.");
            if (splittedPackageName.length >= 3) {
                GraphqlClient.moduleName = splittedPackageName[2];
            } else {
                GraphqlClient.moduleName = DEFAULT_MODULE_NAME;
            }
        } else {
            GraphqlClient.moduleName = DEFAULT_MODULE_NAME;
        }
    }
}