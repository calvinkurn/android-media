package com.tokopedia.filter.newdynamicfilter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.filter.newdynamicfilter.helper.FilterDbHelper

import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by henrypriyono on 11/24/17.
 */

class DynamicFilterLocationActivity : DynamicFilterDetailGeneralActivity() {

    override fun retrieveOptionListData(): Observable<Boolean> {
        return Observable.create { subscriber ->
            optionList = FilterDbHelper.loadLocationFilterOptions(this@DynamicFilterLocationActivity)
            subscriber.onNext(true)
        }
    }

    override fun applyFilter() {
        showLoading()
        Observable.create(Observable.OnSubscribe<Boolean> { subscriber ->
            FilterDbHelper.storeLocationFilterOptions(this@DynamicFilterLocationActivity, optionList)
            subscriber.onNext(true)
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Boolean>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {}

                    override fun onNext(aBoolean: Boolean) {
                        hideLoading()
                        setResult(RESULT_OK)
                        finish()
                    }
                })
    }

    companion object {

        const val REQUEST_CODE = 222

        fun moveTo(activity: AppCompatActivity?,
                   pageTitle: String,
                   isSearchable: Boolean,
                   searchHint: String,
                   isUsingTracking: Boolean,
                   trackingData: FilterTrackingData?) {

            if (activity != null) {
                val intent = Intent(activity, DynamicFilterLocationActivity::class.java)
                intent.putExtra(EXTRA_PAGE_TITLE, pageTitle)
                intent.putExtra(EXTRA_IS_SEARCHABLE, isSearchable)
                intent.putExtra(EXTRA_SEARCH_HINT, searchHint)
                intent.putExtra(EXTRA_IS_USING_TRACKING, isUsingTracking)
                intent.putExtra(EXTRA_TRACKING_DATA, trackingData)
                activity.startActivityForResult(intent, REQUEST_CODE)
            }
        }
    }
}
