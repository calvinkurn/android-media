package com.tokopedia.imagepicker.picker.instagram.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.imagepicker.picker.instagram.data.InstagramRepositoryImpl;
import com.tokopedia.imagepicker.picker.instagram.data.source.InstagramDataSourceFactory;
import com.tokopedia.imagepicker.picker.instagram.data.source.cloud.InstagramApi;
import com.tokopedia.imagepicker.picker.instagram.domain.InstagramRepository;
import com.tokopedia.imagepicker.picker.instagram.domain.interactor.ClearCacheMediaInstagramUseCase;
import com.tokopedia.imagepicker.picker.instagram.domain.interactor.GetListMediaInstagramUseCase;
import com.tokopedia.imagepicker.picker.instagram.domain.interactor.SaveCookiesInstagramUseCase;
import com.tokopedia.imagepicker.picker.instagram.util.InstagramConstant;
import com.tokopedia.imagepicker.picker.instagram.view.presenter.ImagePickerInstagramPresenter;
import com.tokopedia.imagepicker.picker.instagram.view.presenter.InstagramLoginPresenter;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 5/4/18.
 */

@Module
public class InstagramModule {

    public static final int TIMEOUT = 45;

    @InstagramScope
    @Provides
    ImagePickerInstagramPresenter provideImagePickerInstagramPresenter(GetListMediaInstagramUseCase getListMediaInstagramUseCase,
                                                                       ClearCacheMediaInstagramUseCase clearCacheMediaInstagramUseCase) {
        return new ImagePickerInstagramPresenter(getListMediaInstagramUseCase, clearCacheMediaInstagramUseCase);
    }

    @InstagramScope
    @Provides
    InstagramLoginPresenter provideInstagramLoginFragment(SaveCookiesInstagramUseCase saveCookiesInstagramUseCase){
        return new InstagramLoginPresenter(saveCookiesInstagramUseCase);
    }

    @InstagramScope
    @Provides
    InstagramRepository provideInstagramRepository(InstagramDataSourceFactory instagramDataSourceFactory) {
        return new InstagramRepositoryImpl(instagramDataSourceFactory);
    }

    @InstagramScope
    @Provides
    LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, InstagramConstant.INSTAGRAM_CACHE_KEY);
    }

    @InstagramScope
    @Provides
    InstagramApi provideInstagramApi(Retrofit.Builder retrofit) {
        return retrofit.client(new OkHttpClient.Builder()
                .addInterceptor(new CacheApiInterceptor())
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build())
                .baseUrl(InstagramConstant.URL_API_INSTAGRAM)
                .build()
                .create(InstagramApi.class);
    }


}
