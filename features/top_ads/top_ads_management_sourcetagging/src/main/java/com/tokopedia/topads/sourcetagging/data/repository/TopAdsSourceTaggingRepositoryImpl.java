package com.tokopedia.topads.sourcetagging.data.repository;

import android.text.TextUtils;

import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceTaggingConstant;
import com.tokopedia.topads.sourcetagging.data.TopAdsSourceTaggingModel;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingDataSource;
import com.tokopedia.topads.sourcetagging.domain.repository.TopAdsSourceTaggingRepository;
import com.tokopedia.usecase.RequestParams;

import java.util.Date;

import rx.Observable;
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
                return transformFromString(value);
            }
        });
    }

    @Override
    public Observable<Void> deleteSource() {
        return dataSource.remove();
    }

    @Override
    public Observable<Void> checkTimeAndSaveSource(final RequestParams requestParams){
        return getSource().switchMap(new Func1<TopAdsSourceTaggingModel, Observable<? extends Void>>() {
            @Override
            public Observable<? extends Void> call(TopAdsSourceTaggingModel topAdsSourceTaggingModel) {
                if (topAdsSourceTaggingModel == null)
                    return saveSource(requestParams);

                long diff = (new Date().getTime() - topAdsSourceTaggingModel.getTimestamp());

                if (diff > TopAdsSourceTaggingConstant.EXPIRING_TIME_IN_SECOND)
                    return saveSource(requestParams);

                return Observable.just(null);
            }
        });
    }

    private TopAdsSourceTaggingModel transformFromString(String value) throws NumberFormatException{
        if (!value.contains(TopAdsSourceTaggingConstant.SEPARATOR)) {
            throw new IllegalArgumentException("The value format is wrong");
        }
        String[] tmp = value.split(TopAdsSourceTaggingConstant.SEPARATOR);
        if (tmp.length <= 1 ) {
            throw new IllegalArgumentException("The value format is wrong");
        }
        long timestamp = Long.parseLong(tmp[1]);

        return new TopAdsSourceTaggingModel(tmp[0], timestamp);
    }
}
