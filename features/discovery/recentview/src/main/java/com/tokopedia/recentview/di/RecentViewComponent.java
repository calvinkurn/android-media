package com.tokopedia.recentview.di;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.recentview.view.fragment.RecentViewFragment;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Component;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by milhamj on 15/08/18.
 */

@RecentViewScope
@Component(modules = RecentViewModule.class, dependencies = BaseAppComponent.class)
public interface RecentViewComponent {

    @NonNull
    @ApplicationContext
    Context context();

    @NonNull
    Retrofit.Builder retrofitBuilder();

    @NonNull
    HttpLoggingInterceptor httpLoggingInterceptor();

    @NonNull
    UserSessionInterface userSessionInterface();

    void inject(@NonNull RecentViewFragment fragment);
}
