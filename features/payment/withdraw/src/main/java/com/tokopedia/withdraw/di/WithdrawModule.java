package com.tokopedia.withdraw.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;

/**
 * @author by StevenFredian on 30/07/18.
 */

@WithdrawScope
@Module
public class WithdrawModule {

    /*@WithdrawQualifier
    @Provides
    public ChuckInterceptor provideChuckInterceptor(@ApplicationContext Context context) {
        return new ChuckInterceptor(context).showNotification(GlobalConfig.isAllowDebuggingTools());
    }*/

    @WithdrawScope
    @Provides
    public UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    /*@WithdrawQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(
            @ApplicationContext Context context,
            @WithdrawQualifier ChuckInterceptor chuckInterceptor,
            HttpLoggingInterceptor httpLoggingInterceptor,
            @WithdrawQualifier UserSession userSession) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new ErrorResponseInterceptor(WSErrorResponse.class))
                .addInterceptor(new TkpdAuthInterceptor(context, (NetworkRouter) context.getApplicationContext(), userSession))
                .addInterceptor(new FingerprintInterceptor((NetworkRouter) context, userSession));

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
                    .addInterceptor(new DebugInterceptor());
        }
        return builder.build();
    }*/

    @WithdrawScope
    @Provides
    public AnalyticTracker provideAnalyticTracker(@ApplicationContext Context context) {
        if (context instanceof AbstractionRouter) {
            return ((AbstractionRouter) context).getAnalyticTracker();
        }
        throw new RuntimeException("App should implement " + AbstractionRouter.class.getSimpleName());
    }
}
