package com.tokopedia.filter.newdynamicfilter;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData;
import com.tokopedia.filter.newdynamicfilter.helper.FilterDbHelper;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by henrypriyono on 11/24/17.
 */

public class DynamicFilterLocationActivity extends DynamicFilterDetailGeneralActivity {

    public static final int REQUEST_CODE = 222;

    @Override
    protected Observable<Boolean> retrieveOptionListData() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                optionList = FilterDbHelper.loadLocationFilterOptions(DynamicFilterLocationActivity.this);
                subscriber.onNext(true);
            }
        });
    }

    public static void moveTo(AppCompatActivity activity,
                              String pageTitle,
                              boolean isSearchable,
                              String searchHint,
                              boolean isUsingTracking,
                              FilterTrackingData trackingData) {

        if (activity != null) {
            Intent intent = new Intent(activity, DynamicFilterLocationActivity.class);
            intent.putExtra(EXTRA_PAGE_TITLE, pageTitle);
            intent.putExtra(EXTRA_IS_SEARCHABLE, isSearchable);
            intent.putExtra(EXTRA_SEARCH_HINT, searchHint);
            intent.putExtra(EXTRA_IS_USING_TRACKING, isUsingTracking);
            intent.putExtra(EXTRA_TRACKING_DATA, trackingData);
            activity.startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void applyFilter() {
        showLoading();
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                FilterDbHelper.storeLocationFilterOptions(DynamicFilterLocationActivity.this, optionList);
                subscriber.onNext(true);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                hideLoading();
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
