package com.tokopedia.recentview.di;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.recentview.view.fragment.RecentViewFragment;

import dagger.Component;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by milhamj on 15/08/18.
 */

@RecentViewScope
@Component(modules = RecentViewModule.class, dependencies = BaseAppComponent.class)
public interface RecentViewComponent {

    @ApplicationContext
    Context context();

    Retrofit.Builder retrofitBuilder();

    HttpLoggingInterceptor httpLoggingInterceptor();

    UserSession userSession();

    void inject(RecentViewFragment fragment);
}
