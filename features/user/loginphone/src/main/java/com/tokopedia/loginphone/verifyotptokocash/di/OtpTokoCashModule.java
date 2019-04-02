package com.tokopedia.loginphone.verifyotptokocash.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.otp.common.di.OtpScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author by nisie on 10/22/18.
 */
@Module
public class OtpTokoCashModule {

    @OtpScope
    @Provides
    public AnalyticTracker provideAnalyticTracker(@ApplicationContext Context context) {
        if (context instanceof AbstractionRouter) {
            return ((AbstractionRouter) context).getAnalyticTracker();
        }
        throw new RuntimeException("App should implement " + AbstractionRouter.class.getSimpleName());
    }
}
