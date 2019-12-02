package com.tokopedia.filter.newdynamicfilter.helper

import androidx.appcompat.app.AppCompatActivity

import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.newdynamicfilter.DynamicFilterCategoryActivity
import com.tokopedia.filter.newdynamicfilter.DynamicFilterColorActivity
import com.tokopedia.filter.newdynamicfilter.DynamicFilterDetailBrandActivity
import com.tokopedia.filter.newdynamicfilter.DynamicFilterDetailGeneralActivity
import com.tokopedia.filter.newdynamicfilter.DynamicFilterLocationActivity
import com.tokopedia.filter.newdynamicfilter.DynamicFilterOfferingActivity
import com.tokopedia.filter.newdynamicfilter.DynamicFilterRatingActivity
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData

import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

object FilterDetailActivityRouter {

    fun launchDetailActivity(activity: AppCompatActivity, filter: Filter) {
        launchDetailActivity(activity, filter, false, null)
    }

    fun launchDetailActivity(activity: AppCompatActivity,
                             filter: Filter, isUsingTracking: Boolean = false, trackingData: FilterTrackingData?) {
        if (filter.isColorFilter) {
            DynamicFilterColorActivity
                    .moveTo(activity,
                            filter.title,
                            filter.filterOptions,
                            filter.search.searchable == 1,
                            filter.search.placeholder, isUsingTracking, trackingData)

        } else if (filter.isOfferingFilter) {
            DynamicFilterOfferingActivity
                    .moveTo(activity,
                            filter.title,
                            filter.filterOptions,
                            filter.search.searchable == 1,
                            filter.search.placeholder, isUsingTracking, trackingData)

        } else if (filter.isRatingFilter) {
            DynamicFilterRatingActivity
                    .moveTo(activity,
                            filter.title,
                            filter.filterOptions,
                            filter.search.searchable == 1,
                            filter.search.placeholder, isUsingTracking, trackingData)

        } else if (filter.isBrandFilter) {
            DynamicFilterDetailBrandActivity
                    .moveTo(activity,
                            filter.title,
                            filter.filterOptions,
                            filter.search.searchable == 1,
                            filter.search.placeholder, isUsingTracking, trackingData)

        } else if (filter.isLocationFilter) {
            launchLocationFilterPage(activity, filter, isUsingTracking, trackingData)
        } else {
            DynamicFilterDetailGeneralActivity
                    .moveTo(activity,
                            filter.title,
                            filter.filterOptions,
                            filter.search.searchable == 1,
                            filter.search.placeholder, isUsingTracking, trackingData)
        }
    }

    private fun launchLocationFilterPage(activity: AppCompatActivity,
                                         filter: Filter,
                                         isUsingTracking: Boolean, trackingData: FilterTrackingData?) {
        Observable.create(object : Observable.OnSubscribe<Boolean> {
            override fun call(subscriber: Subscriber<in Boolean>) {
                FilterDbHelper.storeLocationFilterOptions(activity, filter.getOptions())
                subscriber.onNext(true)
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Boolean>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {}

                    override fun onNext(aBoolean: Boolean) {
                        DynamicFilterLocationActivity
                                .moveTo(activity,
                                        filter.title,
                                        filter.search.searchable == 1,
                                        filter.search.placeholder, isUsingTracking, trackingData)
                    }
                })
    }

    fun launchCategoryActivity(activity: AppCompatActivity,
                               filter: Filter,
                               defaultCategoryRootId: String,
                               defaultCategoryId: String) {
        launchCategoryActivity(activity, filter,
                defaultCategoryRootId, defaultCategoryId, false, null)
    }

    fun launchCategoryActivity(activity: AppCompatActivity,
                               filter: Filter,
                               defaultCategoryRootId: String,
                               defaultCategoryId: String,
                               isUsingTracking: Boolean = false,
                               trackingData: FilterTrackingData?) {
        DynamicFilterCategoryActivity
                .moveTo(activity,
                        filter.filterOptions,
                        defaultCategoryRootId,
                        defaultCategoryId,
                        isUsingTracking,
                        trackingData)
    }
}
