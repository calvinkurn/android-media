package com.tokopedia.core.base.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.di.module.NetModule;

import dagger.Module;
import dagger.Provides;


/**
 * @author kulomady on 1/9/17.
 */
@Module(includes = {NetModule.class})
public class AppModule {

    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @ApplicationScope
    @Provides
    @ApplicationContext
    public Context provideContext() {
        return context.getApplicationContext();
    }

    @ApplicationScope
    @Provides
    public SharedPreferences provideSharedPreference(@ApplicationContext Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @ApplicationScope
    @Provides
    public ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @ApplicationScope
    @Provides
    public PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

}
