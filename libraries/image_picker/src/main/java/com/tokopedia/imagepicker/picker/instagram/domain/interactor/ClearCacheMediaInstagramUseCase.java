package com.tokopedia.imagepicker.picker.instagram.domain.interactor;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase;
import com.tokopedia.imagepicker.picker.instagram.util.InstagramConstant;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 6/8/18.
 */

public class ClearCacheMediaInstagramUseCase extends CacheApiDataDeleteUseCase {

    @Inject
    public ClearCacheMediaInstagramUseCase(@ApplicationContext Context context) {
        super(context);
    }

    public Observable<Boolean> createObservable() {
        return createObservable(RequestParams.create());
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        RequestParams newRequestParams = CacheApiDataDeleteUseCase.createParams(InstagramConstant.URL_API_INSTAGRAM, InstagramConstant.URL_PATH_GET_LIST_MEDIA);
        return super.createObservable(newRequestParams);
    }
}