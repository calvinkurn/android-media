package com.tokopedia.topads.view.model

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.topads.UrlConstant
import com.tokopedia.topads.data.response.ResponseGroupValidate
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Author errysuprayogi on 06,November,2019
 */
class CreateGroupAdsViewModel @Inject constructor(
        @Named("Main")
        private val dispatcher: CoroutineDispatcher,
        private val userSession: UserSessionInterface,
        private val repository: RestRepository,
        private val urlMap: Map<String, String>): BaseViewModel(dispatcher) {

    fun validateGroup(groupName: String, onSuccess: ((ResponseGroupValidate.Data) -> Unit),
                      onError: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    val result = withContext(Dispatchers.IO) {
                        val queryMap = mutableMapOf<String, String>(
                                "group_name" to groupName,
                                "shop_id" to userSession.shopId
                        )
                        val restRequest = RestRequest.Builder(
                                urlMap[UrlConstant.PATH_GROUP_VALIDATE] ?: "",
                                object : TypeToken<ResponseGroupValidate>() {}.type)
                                .setRequestType(RequestType.POST)
                                .setBody(queryMap)
                                .build()
                        repository.getResponse(restRequest)
                    }
                    (result.getData() as ResponseGroupValidate).data?.let{
                        onSuccess(it)
                    }
                },
                onError = {
                    onError(it)
                }
        )
    }
}