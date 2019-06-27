package com.tokopedia.feedplus.view.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.feedplus.view.fragment.FeedPlusDetailFragment;
import com.tokopedia.feedplus.view.fragment.FeedPlusFragment;
import com.tokopedia.kol.common.di.KolComponent;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Component;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by nisie on 5/15/17.
 */

@FeedPlusScope
@Component(modules = FeedPlusModule.class, dependencies = KolComponent.class)
public interface FeedPlusComponent {

    @ApplicationContext
    Context context();

    Retrofit.Builder retrofitBuilder();

    HttpLoggingInterceptor httpLoggingInterceptor();

    UserSessionInterface userSessionUserSessionInterface();

    void inject(FeedPlusFragment feedPlusFragment);

    void inject(FeedPlusDetailFragment feedPlusDetailFragment);
}
