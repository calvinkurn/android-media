package com.tokopedia.explore.view.presenter

import android.net.Uri
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.explore.domain.interactor.GetExploreDataUseCase
import com.tokopedia.explore.view.listener.ContentExploreContract
import com.tokopedia.explore.view.subscriber.EmptySubscriber
import com.tokopedia.explore.view.subscriber.GetExploreDataSubscriber
import com.tokopedia.kotlin.extensions.view.decodeToUtf8
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import rx.Subscriber

import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * @author by milhamj on 23/07/18.
 */

class ContentExplorePresenter @Inject constructor(
        private val getExploreDataUseCase: GetExploreDataUseCase,
        private val trackAffiliateClickUseCase: TrackAffiliateClickUseCase
) : BaseDaggerPresenter<ContentExploreContract.View>(), ContentExploreContract.Presenter, CoroutineScope {

    companion object {
        private const val QUERY_ACTIVITY_ID = "activity_id"
        private const val TRACKING_IMPERESSION_MAX_BATCH_COUNT = 20
    }

    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    private var impressionTrackList: MutableList<String> = mutableListOf()
    private val impressionTrackingChannel: Channel<String> = Channel()

    private var cursor = ""
    private var categoryId = 0
    private var search = ""

    override fun attachView(view: ContentExploreContract.View?) {
        super.attachView(view)
        initTrackingChannel()

    }

    override fun detachView() {
        super.detachView()
        launch {
            if (impressionTrackList.isNotEmpty())
                trackBulkAffiliate(impressionTrackList)
            else
                trackAffiliateClickUseCase.unsubscribe()

            impressionTrackingChannel.close()
            job.cancel()
        }
        getExploreDataUseCase.unsubscribe()
    }

    override fun getExploreData(clearData: Boolean) {
        if (clearData) {
            view.showRefreshing()
        } else {
            view.showLoading()
        }
        getExploreDataUseCase.execute(
                GetExploreDataUseCase.getVariables(categoryId, cursor, search),
                GetExploreDataSubscriber(view, clearData)
        )
    }

    override fun updateCursor(cursor: String) {
        this.cursor = cursor
    }

    override fun updateCategoryId(categoryId: Int) {
        this.categoryId = categoryId
    }

    override fun updateSearch(search: String) {
        this.search = search
    }

    override fun trackAffiliate(url: String) {
        trackAffiliateClickUseCase.execute(
                TrackAffiliateClickUseCase.createRequestParams(url),
                object : Subscriber<Boolean>() {
                    override fun onNext(t: Boolean?) {
                        if (isViewNotAttached) trackAffiliateClickUseCase.unsubscribe()
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                    }
                }
        )
    }

    override fun appendImpressionTracking(url: String) {
        launch {
            impressionTrackingChannel.send(url)
        }
    }

    private fun trackBulkAffiliate(urlList: MutableList<String>) {
        if (urlList.isNotEmpty()) {
           val activityIdListString = urlList
                    .mapNotNull { Uri.parse(it).getQueryParameter(QUERY_ACTIVITY_ID) }
                    .joinToString(",")

            trackAffiliate(
                    Uri.parse(urlList.first()).setParameter(QUERY_ACTIVITY_ID, activityIdListString).toString().decodeToUtf8()
            )
        }
    }

    private fun initTrackingChannel() {
        launch {
            for (tracking in impressionTrackingChannel) {
                impressionTrackList.add(tracking)
                if (impressionTrackList.size == TRACKING_IMPERESSION_MAX_BATCH_COUNT) {
                    trackBulkAffiliate(impressionTrackList)
                    impressionTrackList.clear()
                }
            }
        }
    }

    private fun Uri.setParameter(key: String, value: String): Uri {
        val queryParams = queryParameterNames
        val newUri = buildUpon().clearQuery()
        for (param in queryParams) {
            newUri.appendQueryParameter(param, if (param == key) value else getQueryParameter(param))
        }

        return newUri.build()
    }
}
