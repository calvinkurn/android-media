package com.tokopedia.analytics.mapper;

import android.content.Context;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.analytics.mapper.di.DaggerTkpdAppsflyerMapComponent;
import com.tokopedia.analytics.mapper.di.TkpdAppsflyerMapComponent;
import com.tokopedia.analytics.mapper.domain.TkpdAppsflyerMapUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

public class TkpdAppsFlyerMapper {
    private static TkpdAppsFlyerMapper ourInstance ;
    @Inject
    TkpdAppsflyerMapUseCase tkpdAppsflyerMapUseCase;
    Context context;


    public static TkpdAppsFlyerMapper getInstance(Context context) {
        ourInstance = new TkpdAppsFlyerMapper(context);

        return ourInstance;
    }


    private TkpdAppsFlyerMapper(Context context) {
        this.context = context;
        TkpdAppsflyerMapComponent component = DaggerTkpdAppsflyerMapComponent.builder()
                .baseAppComponent(((BaseMainApplication) context).getBaseAppComponent())
                .build();
        component.inject(this);
    }


    public void mapAnalytics() {
        tkpdAppsflyerMapUseCase.execute(RequestParams.EMPTY, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {

            }
        });
    }

}
