package com.tokopedia.core.dynamicfilter.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.dynamicfilter.facade.HadesNetwork;
import com.tokopedia.core.dynamicfilter.facade.models.HadesV1Model;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by noiz354 on 7/11/16.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class DynamicFilterBaseImpl extends DynamicFilterBase {
    public DynamicFilterBaseImpl(DynamicFilterBaseDetailView view) {
        super(view);
    }

    @Override
    public void hitHadesDemo() {
        // https://hades.tokopedia.com/v1/categories?filter=type==list%3Blevel==1
        compositeSubscription.add(HadesNetwork.fetchDepartment(0, 1, HadesNetwork.LIST)// fetch parent department
                .flatMap(new Func1<Response<HadesV1Model>, Observable<Response<HadesV1Model>>>() {
                    @Override
                    public Observable<Response<HadesV1Model>> call(Response<HadesV1Model> hadesV1ModelResponse) {
                        // https://hades.tokopedia.com/v1/categories/585?filter=type==list%3Blevel==breadcrumb
                        Response<HadesV1Model> first = HadesNetwork.fetchDepartment(585, HadesNetwork.BREADCRUMB_LEVEL, HadesNetwork.LIST).toBlocking().first();
                        Log.d(TAG, "1 " + getMessageTAG() + first.body());
                        return Observable.just(hadesV1ModelResponse);
                    }
                })
                .flatMap(new Func1<Response<HadesV1Model>, Observable<Response<HadesV1Model>>>() {
                    @Override
                    public Observable<Response<HadesV1Model>> call(Response<HadesV1Model> hadesV1ModelResponse) {
                        // https://hades.tokopedia.com/v1/categories/65?filter=type==list%3Blevel==3
                        Response<HadesV1Model> first = HadesNetwork.fetchDepartment(65, 3, HadesNetwork.LIST).toBlocking().first();
                        Log.d(TAG, "2 " + getMessageTAG() + first.body());
                        return Observable.just(hadesV1ModelResponse);
                    }
                })
                .flatMap(new Func1<Response<HadesV1Model>, Observable<Response<HadesV1Model>>>() {
                    @Override
                    public Observable<Response<HadesV1Model>> call(Response<HadesV1Model> hadesV1ModelResponse) {
                        // https://hades.tokopedia.com/v1/categories?filter=type==tree
                        Response<HadesV1Model> first = HadesNetwork.fetchDepartment(-1, -2, HadesNetwork.TREE).toBlocking().first();
                        Log.d(TAG, "3 " + getMessageTAG() + first.body());
                        return Observable.just(hadesV1ModelResponse);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<Response<HadesV1Model>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Response<HadesV1Model> hadesV1ModelResponse) {
                                Log.d(TAG, "4 " + getMessageTAG() + hadesV1ModelResponse);
                            }
                        }
                ));
    }

    @Override
    public String getMessageTAG() {
        return "DynamicFilterBaseImpl";
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return "DynamicFilterBaseImpl";
    }

    @Override
    public void initData(@NonNull Context context) {
        view.setImageUsingGlide();
    }

    @Override
    public void fetchArguments(Bundle argument) {

    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {

    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {

    }

    @Override
    public void initDataInstance(Context context) {

    }
}
