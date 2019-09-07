package com.tokopedia.filter.newdynamicfilter.helper;

import android.support.v7.app.AppCompatActivity;

import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.newdynamicfilter.DynamicFilterCategoryActivity;
import com.tokopedia.filter.newdynamicfilter.DynamicFilterColorActivity;
import com.tokopedia.filter.newdynamicfilter.DynamicFilterDetailBrandActivity;
import com.tokopedia.filter.newdynamicfilter.DynamicFilterDetailGeneralActivity;
import com.tokopedia.filter.newdynamicfilter.DynamicFilterLocationActivity;
import com.tokopedia.filter.newdynamicfilter.DynamicFilterOfferingActivity;
import com.tokopedia.filter.newdynamicfilter.DynamicFilterRatingActivity;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by henrypriyono on 8/16/17.
 */

public class FilterDetailActivityRouter {

    public static void launchDetailActivity(AppCompatActivity activity, Filter filter) {
        launchDetailActivity(activity, filter, false);
    }

    public static void launchDetailActivity(AppCompatActivity activity,
                                            Filter filter, boolean isUsingTracking) {
        if (filter.isColorFilter()) {
            DynamicFilterColorActivity
                    .moveTo(activity,
                            filter.getTitle(),
                            filter.getOptions(),
                            filter.getSearch().getSearchable() == 1,
                            filter.getSearch().getPlaceholder(), isUsingTracking);

        } else if (filter.isOfferingFilter()) {
            DynamicFilterOfferingActivity.Companion
                    .moveTo(activity,
                            filter.getTitle(),
                            filter.getOptions(),
                            filter.getSearch().getSearchable() == 1,
                            filter.getSearch().getPlaceholder(), isUsingTracking);

        } else if (filter.isRatingFilter()) {
            DynamicFilterRatingActivity
                    .moveTo(activity,
                            filter.getTitle(),
                            filter.getOptions(),
                            filter.getSearch().getSearchable() == 1,
                            filter.getSearch().getPlaceholder(), isUsingTracking);

        } else if (filter.isBrandFilter()) {
            DynamicFilterDetailBrandActivity
                    .moveTo(activity,
                            filter.getTitle(),
                            filter.getOptions(),
                            filter.getSearch().getSearchable() == 1,
                            filter.getSearch().getPlaceholder(), isUsingTracking);

        } else if (filter.isLocationFilter()) {
            launchLocationFilterPage(activity, filter, isUsingTracking);
        } else {
            DynamicFilterDetailGeneralActivity
                    .moveTo(activity,
                            filter.getTitle(),
                            filter.getOptions(),
                            filter.getSearch().getSearchable() == 1,
                            filter.getSearch().getPlaceholder(), isUsingTracking);
        }
    }

    private static void launchLocationFilterPage(final AppCompatActivity activity,
                                                 final Filter filter,
                                                 final boolean isUsingTracking) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                FilterDbHelper.storeLocationFilterOptions(activity, filter.getOptions());
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
                DynamicFilterLocationActivity
                        .moveTo(activity,
                                filter.getTitle(),
                                filter.getSearch().getSearchable() == 1,
                                filter.getSearch().getPlaceholder(), isUsingTracking);
            }
        });
    }

    public static void launchCategoryActivity(AppCompatActivity activity,
                                              Filter filter,
                                              String defaultCategoryRootId,
                                              String defaultCategoryId) {
        launchCategoryActivity(activity, filter,
                defaultCategoryRootId, defaultCategoryId, false);
    }

    public static void launchCategoryActivity(AppCompatActivity activity,
                                              Filter filter,
                                              String defaultCategoryRootId,
                                              String defaultCategoryId,
                                              boolean isUsingTracking) {
        DynamicFilterCategoryActivity
                .moveTo(activity,
                        filter.getOptions(),
                        defaultCategoryRootId,
                        defaultCategoryId,
                        isUsingTracking);
    }
}
