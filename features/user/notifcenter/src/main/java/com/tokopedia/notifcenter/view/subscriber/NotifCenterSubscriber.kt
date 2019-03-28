package com.tokopedia.notifcenter.view.subscriber

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.notifcenter.domain.pojo.NotifCenterError
import com.tokopedia.notifcenter.domain.pojo.NotifCenterPojo
import com.tokopedia.notifcenter.domain.pojo.NotifCenterSinglePojo
import com.tokopedia.notifcenter.domain.pojo.UserNotification
import com.tokopedia.notifcenter.view.listener.NotifCenterContract
import com.tokopedia.notifcenter.view.util.NotifCenterDateUtil
import com.tokopedia.notifcenter.view.viewmodel.NotifItemViewModel
import rx.Subscriber

/**
 * @author by milhamj on 31/08/18.
 */
class NotifCenterSubscriber(val view: NotifCenterContract.View, val dateUtil: NotifCenterDateUtil,
                            val isListPojo: Boolean)
    : Subscriber<GraphqlResponse>() {

    override fun onError(e: Throwable?) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e?.printStackTrace()
        }
        view.hideLoading()
        view.onErrorFetchData(ErrorHandler.getErrorMessage(view.getContext(), e))
    }

    override fun onCompleted() {
    }

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        view.hideLoading()
        graphqlResponse?.let {
            val errors = it.getError(NotifCenterError::class.java)

            if (!(errors?.isEmpty() ?: true)) {
                if (!TextUtils.isEmpty(errors[0].message)){
                    view.onErrorFetchData(errors[0].message)
                    return
                }
                throw RuntimeException()
            }

            val list: List<UserNotification>
            val hasNext: Boolean
            if (isListPojo) {
                val notifCenterPojo : NotifCenterPojo = it.getData(NotifCenterPojo::class.java)
                list = notifCenterPojo.notifCenterList.list
                hasNext = notifCenterPojo.notifCenterList.paging.hasNext
            } else {
                val notifCenterPojo : NotifCenterSinglePojo = it.getData(NotifCenterSinglePojo::class.java)
                list = notifCenterPojo.notifCenterList.list
                hasNext = notifCenterPojo.notifCenterList.paging.hasNext
            }
            view.onSuccessFetchData(
                    convertToViewModels(list),
                    hasNext
            )
        }
    }

    private fun convertToViewModels(notificationList : List<UserNotification>): List<Visitable<*>> {
        val visitables : ArrayList<Visitable<*>> = ArrayList()
        var lastPrettyDate = ""
        for (notification in notificationList) {
            val prettyDate = dateUtil.getPrettyDate(notification.createTimeUnix)
            visitables.add(
                    NotifItemViewModel(
                            notification.notifId,
                            notification.title,
                            notification.dataNotification.infoThumbnailUrl,
                            notification.createTime,
                            prettyDate,
                            notification.sectionKey,
                            notification.dataNotification.appLink,
                            notification.readStatus,
                            notification.userId,
                            notification.shopId,
                            (!TextUtils.equals(lastPrettyDate, prettyDate) || visitables.isEmpty()),
                            notification.templateKey
                    )
            )
            lastPrettyDate = prettyDate
        }
        return visitables
    }
}