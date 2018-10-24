package com.tokopedia.notifications.domain;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by Ashwani Tyagi on 23/10/18.
 */
public class UpdateFcmTokenUseCase extends UseCase<Boolean> {

    private Context context;

    public UpdateFcmTokenUseCase(@ApplicationContext Context context){
        this.context = context;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return null;
    }
}
