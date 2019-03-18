package com.tokopedia.otp.common.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * @author by nisie on 4/24/18.
 */


@OtpScope
@Module(includes = {OtpNetModule.class})
public class OtpModule {

    @OtpScope
    @Provides
    public AnalyticTracker provideAnalyticTracker(@ApplicationContext Context context) {
        if (context instanceof AbstractionRouter) {
            return ((AbstractionRouter) context).getAnalyticTracker();
        }
        throw new RuntimeException("App should implement " + AbstractionRouter.class.getSimpleName());
    }

    @OtpScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

}