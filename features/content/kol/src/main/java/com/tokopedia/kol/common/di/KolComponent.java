package com.tokopedia.kol.common.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.feedcomponent.di.FeedComponentModule;
import com.tokopedia.kol.common.data.source.api.KolApi;
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase;
import com.tokopedia.kol.feature.video.view.fragment.MediaPreviewFragment;
import com.tokopedia.kol.feature.video.view.fragment.VideoDetailFragment;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Component;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by milhamj on 06/02/18.
 */

@KolScope
@Component(modules = {KolModule.class, KolViewModelModule.class, FeedComponentModule.class}, dependencies = BaseAppComponent.class)
public interface KolComponent {
    KolApi kolApi();

    @ApplicationContext
    Context getContext();

    Retrofit.Builder retrofitBuilder();

    Gson gson();

    AbstractionRouter provideAbstractionRouter();

    TkpdAuthInterceptor tkpdAuthInterceptor();

    HeaderErrorResponseInterceptor headerErrorResponseInterceptor();

    HttpLoggingInterceptor httpLoggingInterceptor();

    CacheManager provideCacheManager();

    FollowKolPostGqlUseCase getFollowKolPostGqlUseCase();

    UserSessionInterface userSessionInterface();

    CoroutineDispatchers coroutineDispatchers();

    void inject(VideoDetailFragment videoDetailFragment);
    void inject(MediaPreviewFragment mediaPreviewFragment);
}
