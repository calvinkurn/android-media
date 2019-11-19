package com.tokopedia.navigation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.navigation.data.entity.NotificationEntity
import com.tokopedia.navigation.data.consts.NotificationQueriesConstant.DRAWER_PUSH_NOTIFICATION
import com.tokopedia.navigation.util.coroutines.DispatcherProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

interface NotificationTransactionContract {
    fun getNotification()
}

class NotificationTransactionViewModel @Inject constructor(
        private val repository: GraphqlRepository,
        private val queries: Map<String, String>,
        private val dispatcher: DispatcherProvider
): BaseViewModel(dispatcher.io()), NotificationTransactionContract {

    private val _notification = MutableLiveData<Result<NotificationEntity>>()
    val notification: LiveData<Result<NotificationEntity>>
        get() = _notification

    override fun getNotification() {
        queries[DRAWER_PUSH_NOTIFICATION]?.let { query ->
            launchCatchError(block = {
                val data = withContext(dispatcher.io()) {
                    val request = GraphqlRequest(query, NotificationEntity::class.java)
                    repository.getReseponse(listOf(request))
                }.getSuccessData<NotificationEntity>()

                _notification.value = Success(data)
            }) {
                _notification.value = Fail(it)
            }
        }
    }

}