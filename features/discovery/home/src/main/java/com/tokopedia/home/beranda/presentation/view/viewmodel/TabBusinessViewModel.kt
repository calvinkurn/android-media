package com.tokopedia.home.beranda.presentation.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.experimental.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.experimental.CoroutineContext

class TabBusinessViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        @Named("Main")
        private val baseDispatcher: CoroutineDispatcher
) : ViewModel(), CoroutineScope {

    private val job = Job()
    val homeWidget = MutableLiveData<Result<HomeWidget>>()

    companion object {
        private const val PARAM_TAB_ID = "tabId"
    }

    override val coroutineContext: CoroutineContext
        get() = baseDispatcher + job

    fun getTabList(rawQuery: String) {
        job.children.map { it.cancel() }

        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(
                        rawQuery,
                        HomeWidget.Data::class.java
                )
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }

            if (data.getError(HomeWidget.Data::class.java) == null ||
                    data.getError(HomeWidget.Data::class.java).isEmpty()) {
                if (data.getData<HomeWidget.Data>(HomeWidget.Data::class.java) != null) {
                    homeWidget.value = Success(data.getData<HomeWidget.Data>(HomeWidget.Data::class.java).homeWidget)
                } else {
                    homeWidget.value = Fail(ResponseErrorException("local handling error"))
                }
            } else {
                val message = data.getError(HomeWidget.Data::class.java)[0].message
                homeWidget.value = Fail(ResponseErrorException(message))
            }

        }){
            it.printStackTrace()
            homeWidget.value = Fail(it)
        }

    }

    fun clearJob() {
//        if (isActive) job.cancel()
    }
}
