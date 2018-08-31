package com.tokopedia.notifcenter.view.subscriber

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.notifcenter.domain.pojo.NotifCenterError
import com.tokopedia.notifcenter.domain.pojo.NotifCenterPojo
import com.tokopedia.notifcenter.domain.pojo.UserNotification
import com.tokopedia.notifcenter.view.NotifCenterContract
import com.tokopedia.notifcenter.view.viewmodel.NotifItemViewModel
import rx.Subscriber

/**
 * @author by milhamj on 31/08/18.
 */
class NotifCenterSubscriber(val view: NotifCenterContract.View) : Subscriber<GraphqlResponse>() {

    override fun onError(e: Throwable?) {
        view.hideLoading()
        view.onErrorFetchData(ErrorHandler.getErrorMessage(view.getContext(), e))
    }

    override fun onCompleted() {

    }

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        view.hideLoading()
        graphqlResponse?.let {
            val errors = it.getError(NotifCenterError::class.java)

            if (!errors.isEmpty()) {
                if (!TextUtils.isEmpty(errors[0].message)){
                    view.onErrorFetchData(errors[0].message)
                    return
                }
                throw RuntimeException()
            }

            val notifCenterPojo : NotifCenterPojo = it.getData(NotifCenterPojo::class.java)
            val list : List<UserNotification> = notifCenterPojo.notifCenterList.list
            view.onSuccessFetchData(convertToViewModels(list))
        }
    }

    private fun convertToViewModels(notificationList : List<UserNotification>): List<Visitable<*>> {
        val visitables : ArrayList<Visitable<*>> = ArrayList()
        for (notification in notificationList) {
            //TODO milhamj map prettify create time
            visitables.add(
                    NotifItemViewModel(
                            notification.title,
                            notification.dataNotification.infoThumbnailUrl,
                            notification.createTime,
                            notification.createTime,
                            notification.sectionKey,
                            notification.dataNotification.appLink,
                            notification.readStatus,
                            notification.userId,
                            notification.shopId
                    )
            )
        }
        return visitables
    }
}