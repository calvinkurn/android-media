package com.tokopedia.navigation.presentation.presenter

import android.widget.Toast
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.navigation.data.mapper.GetNotificationUpdateMapper
import com.tokopedia.navigation.domain.GetNotificationUpdateUnreadUseCase
import com.tokopedia.navigation.domain.GetNotificationUpdateUseCase
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateContract
import com.tokopedia.navigation.presentation.view.subscriber.GetNotificationUpdateSubscriber
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterItemViewModel
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateViewModel
import rx.Subscriber
import javax.inject.Inject

class NotificationUpdatePresenter @Inject constructor(
        private var getNotificationUpdateUseCase: GetNotificationUpdateUseCase,
        private var getNotificationUpdateUnreadUseCase: GetNotificationUpdateUnreadUseCase,
        private var getNotificationUpdateMapper : GetNotificationUpdateMapper
)
    : BaseDaggerPresenter<NotificationUpdateContract.View>()
        , NotificationUpdateContract.Presenter {

    override fun filterBy(selectedItemList: HashMap<Int, Int>, filterViewModel: ArrayList<NotificationUpdateFilterItemViewModel>) {
        val variables: HashMap<String, Any> = HashMap()
        for ((key, value) in selectedItemList) {
            val filterType = filterViewModel[key].filterType
            val filterItemId = filterViewModel[key].list[value].id
            variables[filterType] = filterItemId
        }
        Toast.makeText(view.context, variables.toString(), Toast.LENGTH_LONG).show()
    }

    override fun loadData(lastNotifId: String, onSuccessInitiateData: (NotificationUpdateViewModel) -> Unit, onErrorInitiateData: (Throwable) -> Unit) {
        getNotificationUpdateUseCase.execute(
                GetNotificationUpdateUseCase.getRequestParams(1,0,0,lastNotifId),
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


}
