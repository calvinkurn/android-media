package com.tokopedia.navigation.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.navigation.data.mapper.GetNotificationUpdateFilterMapper
import com.tokopedia.navigation.data.mapper.GetNotificationUpdateMapper
import com.tokopedia.navigation.domain.*
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateContract
import com.tokopedia.navigation.presentation.view.subscriber.GetNotificationUpdateFilterSubscriber
import com.tokopedia.navigation.presentation.view.subscriber.GetNotificationUpdateSubscriber
import com.tokopedia.navigation.presentation.view.subscriber.NotificationUpdateActionSubscriber
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterItemViewModel
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateViewModel
import rx.Subscriber
import javax.inject.Inject

class NotificationUpdatePresenter @Inject constructor(
        private var getNotificationUpdateUseCase: GetNotificationUpdateUseCase,
        private var getNotificationUpdateFilterUseCase: GetNotificationUpdateFilterUseCase,
        private var getNotificationUpdateUnreadUseCase: GetNotificationUpdateUnreadUseCase,
        private var getNotificationTotalUnreadUseCase: GetNotificationTotalUnreadUseCase,
        private var clearCounterNotificationUpdateUseCase: ClearCounterNotificationUpdateUseCase,
        private var markReadNotificationUpdateItemUseCase: MarkReadNotificationUpdateItemUseCase,
        private var markAllReadNotificationUpdateUseCase: MarkAllReadNotificationUpdateUseCase,
        private var getNotificationUpdateMapper : GetNotificationUpdateMapper,
        private var getNotificationUpdateFilterMapper : GetNotificationUpdateFilterMapper
)
    : BaseDaggerPresenter<NotificationUpdateContract.View>()
        , NotificationUpdateContract.Presenter {

    val variables: HashMap<String, Any> = HashMap()
    override fun filterBy(selectedItemList: HashMap<Int, Int>, filterViewModel: ArrayList<NotificationUpdateFilterItemViewModel>) {
        variables.clear()
        for ((key, value) in selectedItemList) {
            val filterType = filterViewModel[key].filterType
            val filterItemId = filterViewModel[key].list[value].id.toIntOrZero()
            variables[filterType] = filterItemId
        }
    }

    override fun loadData(lastNotifId: String, onSuccessInitiateData: (NotificationUpdateViewModel) -> Unit, onErrorInitiateData: (Throwable) -> Unit) {
        getNotificationUpdateUseCase.execute(
                GetNotificationUpdateUseCase.getRequestParams(1, variables, lastNotifId),
                GetNotificationUpdateSubscriber(view, getNotificationUpdateMapper, onSuccessInitiateData, onErrorInitiateData)
        )

        getNotificationUpdateUnreadUseCase.execute(
                object : Subscriber<GraphqlResponse>(){
                    override fun onNext(t: GraphqlResponse?) {
                        t
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                        e
                    }
                }
        )
    }

    override fun getFilter(onSuccessGetFilter: (ArrayList<NotificationUpdateFilterItemViewModel>) -> Unit) {
        getNotificationUpdateFilterUseCase.execute(GetNotificationUpdateFilterSubscriber(view, getNotificationUpdateFilterMapper, onSuccessGetFilter))
    }

    override fun clearNotifCounter() {
        clearCounterNotificationUpdateUseCase.execute(NotificationUpdateActionSubscriber(null, null))
    }

    override fun markReadNotif(notifId: String) {
        markReadNotificationUpdateItemUseCase.execute(
                MarkReadNotificationUpdateItemUseCase.getRequestParams(notifId),
                NotificationUpdateActionSubscriber(null,  null))
    }

    override fun markAllReadNotificationUpdate(onSuccessMarkAllReadNotificationUpdate: () -> Unit) {
        markAllReadNotificationUpdateUseCase.execute(
                NotificationUpdateActionSubscriber(onSuccessMarkAllReadNotificationUpdate,  null))
    }
}
