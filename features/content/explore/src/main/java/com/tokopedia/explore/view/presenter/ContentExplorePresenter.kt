package com.tokopedia.explore.view.presenter

import android.net.Uri
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.config.GlobalConfig
import com.tokopedia.explore.domain.entity.GetExploreData
import com.tokopedia.explore.domain.interactor.ExploreDataUseCase
import com.tokopedia.explore.view.listener.ContentExploreContract
import com.tokopedia.explore.view.subscriber.GetExploreDataMapper
import com.tokopedia.explore.view.uimodel.ExploreCategoryViewModel
import com.tokopedia.explore.view.uimodel.ExploreImageViewModel
import com.tokopedia.explore.view.uimodel.ExploreViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.decodeToUtf8
import com.tokopedia.network.utils.ErrorHandler
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import rx.Subscriber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * @author by milhamj on 23/07/18.
 */

class ContentExplorePresenter @Inject constructor(
        private val getExploreDataUseCase: ExploreDataUseCase,
        private val trackAffiliateClickUseCase: TrackAffiliateClickUseCase
) : BaseDaggerPresenter<ContentExploreContract.View>(), ContentExploreContract.Presenter, CoroutineScope {

    companion object {
        private const val QUERY_ACTIVITY_ID = "activity_id"
        private const val QUERY_RECOM_ID = "recom_id"
        private const val TRACKING_IMPERESSION_MAX_BATCH_COUNT = 15
    }

    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

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
        getExploreDataUseCase.cancelJobs()
    }

    override fun getExploreData(clearData: Boolean) {
        if (clearData) {
            view.showRefreshing()
        } else {
            view.showLoading()
        }
        launchCatchError(
                block = {
                    getExploreDataUseCase.setParams(categoryId, cursor, search)
                    val response = getExploreDataUseCase.executeOnBackground()
                    onSuccess(response, clearData)
                },
                onError = {
                    onError(it, clearData)
                })
    }

    private fun onSuccess(response: GetExploreData, clearData: Boolean) {
        response.getDiscoveryKolData?.let { discoveryKolData ->
            if (clearData) {
                view.clearData()
                if (discoveryKolData.error.isNotBlank()) {
                    view.dismissLoading()
                    view.onErrorGetExploreDataFirstPage(discoveryKolData.error)
                    return
                }
            }

            view.updateCursor(discoveryKolData.lastCursor)
            val kolPostViewModelList: MutableList<ExploreImageViewModel> = GetExploreDataMapper.convertToKolPostViewModelList(discoveryKolData.postKol)
            val categoryViewModelList: MutableList<ExploreCategoryViewModel> = GetExploreDataMapper.convertToCategoryViewModelList(discoveryKolData.categories)
            view.onSuccessGetExploreData(
                    ExploreViewModel(kolPostViewModelList, categoryViewModelList),
                    clearData
            )
            view.dismissLoading()
            view.stopTrace()
        }
    }

    private fun onError(throwable: Throwable, clearData: Boolean) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            throwable.printStackTrace()
        }
        view.dismissLoading()
        view.stopTrace()
        if (clearData) {
            view.onErrorGetExploreDataFirstPage(ErrorHandler.getErrorMessage(view.context, throwable))
        } else {
            view.onErrorGetExploreDataMore()
        }
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

    override fun onPullToRefreshTriggered() {
        launch {
            if (impressionTrackList.isNotEmpty()) {
                trackBulkAffiliate(impressionTrackList)
                impressionTrackList.clear()
            }
        }
    }

    fun trackBulkAffiliate(urlList: MutableList<String>) {
        if (urlList.isNotEmpty()) {
            var finalUrl = ""
            try {
                val activityIdListString = urlList
                        .mapNotNull { Uri.parse(it).getQueryParameter(QUERY_ACTIVITY_ID) }
                        .joinToString(",")
                val recomIdListString = urlList
                        .mapNotNull { Uri.parse(it).getQueryParameter(QUERY_RECOM_ID) }
                        .joinToString(",")

                finalUrl = Uri.parse(urlList.first()).setParameter(QUERY_ACTIVITY_ID, activityIdListString).toString().decodeToUtf8()
                finalUrl = Uri.parse(finalUrl).setParameter(QUERY_RECOM_ID, recomIdListString).toString().decodeToUtf8()
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            trackAffiliate(finalUrl)

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
