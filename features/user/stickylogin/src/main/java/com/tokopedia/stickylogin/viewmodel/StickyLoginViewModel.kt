package com.tokopedia.stickylogin.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.stickylogin.data.TickerPojo
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

class StickyLoginViewModel @Inject constructor(
    @Named("Main")
    private val dispatcher: CoroutineDispatcher,
    private val repository: GraphqlRepository
): BaseViewModel(dispatcher), StickyLoginContract {

    private val result = MutableLiveData<TickerPojo>()

    override fun getContent(query: String, page: StickyLoginConstant.Page, onError: ((Throwable) -> Unit)?) {
        launchCatchError(
            block = {
                val data = withContext(Dispatchers.IO) {
                    val params = mapOf(StickyLoginConstant.PARAMS_PAGE to page.toString())
                    val request = GraphqlRequest(query, TickerPojo.TickerResponse::class.java, params)
                    repository.getReseponse(listOf(request))
                }.getSuccessData<TickerPojo.TickerResponse>()

                result.value = data.response
            }
        ) {
            onError?.invoke(it)
        }
    }
}