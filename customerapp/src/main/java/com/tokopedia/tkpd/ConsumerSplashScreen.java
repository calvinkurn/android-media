package com.tokopedia.tkpd;

import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.ride.bookingride.view.activity.RideHomeActivity;
import com.tokopedia.ride.common.configuration.RideConfiguration;

/**
 * Created by ricoharisin on 11/22/16.
 */

public class ConsumerSplashScreen extends SplashScreen {

    @Override
    public void finishSplashScreen() {
        RideConfiguration rideConfiguration = new RideConfiguration(getApplicationContext());

        Intent homeIntent = HomeRouter.getHomeActivity(this);
        if (TextUtils.isEmpty(rideConfiguration.getActiveRequestId())) {
            startActivity(homeIntent);
        } else {
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getApplicationContext());
            taskStackBuilder.addNextIntent(homeIntent);
            taskStackBuilder.addNextIntent(RideHomeActivity.getCallingIntent(this));
            taskStackBuilder.startActivities();
        }
        finish();
    }
}
