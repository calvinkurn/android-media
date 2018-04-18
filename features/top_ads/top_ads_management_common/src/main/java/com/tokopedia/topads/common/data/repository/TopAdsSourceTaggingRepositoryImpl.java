package com.tokopedia.topads.common.data.repository;

import android.text.TextUtils;

import com.tokopedia.topads.common.constant.TopAdsTimeConstant;
import com.tokopedia.topads.common.data.TopAdsSourceTaggingModel;
import com.tokopedia.topads.common.data.source.TopAdsSourceTaggingDataSource;
import com.tokopedia.topads.common.domain.repository.TopAdsSourceTaggingRepository;
import com.tokopedia.usecase.RequestParams;

import java.util.Date;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by hadi.putra on 17/04/18.
 */

public class TopAdsSourceTaggingRepositoryImpl implements TopAdsSourceTaggingRepository {
    private TopAdsSourceTaggingDataSource dataSource;

    public TopAdsSourceTaggingRepositoryImpl(TopAdsSourceTaggingDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Observable<Void> saveSource(RequestParams requestParams) {
        return dataSource.save(requestParams);
    }

    @Override
    public Observable<TopAdsSourceTaggingModel> getSource() {
        return dataSource.getSource().map(new Func1<String, TopAdsSourceTaggingModel>() {
            @Override
            public TopAdsSourceTaggingModel call(String value) {
                if (value == null || TextUtils.isEmpty(value)){
                    return null;
                }
                return new TopAdsSourceTaggingModel(value);
            }
        });
    }

    @Override
    public Observable<Void> deleteSource() {
        return dataSource.remove();
    }

    @Override
    public Observable<Void> checkAndSaveSource(final RequestParams requestParams){
        return getSource().switchMap(new Func1<TopAdsSourceTaggingModel, Observable<? extends Void>>() {
            @Override
            public Observable<? extends Void> call(TopAdsSourceTaggingModel topAdsSourceTaggingModel) {
                if (topAdsSourceTaggingModel == null)
                    return saveSource(requestParams);

                long diff = (new Date().getTime() - topAdsSourceTaggingModel.getTimestamp().getTime())/ TopAdsTimeConstant.MILISECOND;
                if (diff > TopAdsTimeConstant.EXPIRING_TIME_IN_SECOND)
                    return saveSource(requestParams);

                return Observable.just(null);
            }
        });
    }
}
