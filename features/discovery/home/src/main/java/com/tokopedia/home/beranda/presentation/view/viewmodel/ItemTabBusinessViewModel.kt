package com.tokopedia.home.beranda.presentation.view.viewmodel


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.presentation.view.fragment.BusinessUnitItemFragment
import com.tokopedia.home.beranda.presentation.view.fragment.BusinessUnitItemView
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.experimental.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.experimental.CoroutineContext


class ItemTabBusinessViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        @Named("Main")
        private val baseDispatcher: CoroutineDispatcher
) : ViewModel(), CoroutineScope {

    companion object {
        private const val PARAM_TAB_ID = "tabId"
    }

    private val job = Job()
    val homeWidget = MutableLiveData<Result<HomeWidget>>()

    override val coroutineContext: CoroutineContext
        get() = baseDispatcher + job

    fun clearJob() {
        if (isActive) job.cancel()
    }

    fun getList(rawQuery: String, tabId: Int, listener: BusinessUnitItemView) {
        job.children.map { it.cancel() }
        val params = mapOf(PARAM_TAB_ID to tabId)

        launchCatchError(block = {
            val data = withContext(Dispatchers.Default){
                val graphqlRequest = GraphqlRequest(
                        rawQuery,
                        HomeWidget.Data::class.java,
                        params
                )
                graphqlRepository.getReseponse(
                        listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
                )
            }

            if (data.getError(HomeWidget.Data::class.java) == null ||
                    data.getError(HomeWidget.Data::class.java).isEmpty()) {
                if (data.getData<HomeWidget.Data>(HomeWidget.Data::class.java) != null) {
//                    homeWidget.value = Success(data.getData<HomeWidget.Data>(HomeWidget.Data::class.java).homeWidget)
                    listener.onSuccessGetData(data.getData<HomeWidget.Data>(HomeWidget.Data::class.java).homeWidget)
                } else {
//                    homeWidget.value = Fail(ResponseErrorException("local handling error"))
                    listener.onErrorGetData(ResponseErrorException("local handling error"))
                }
            } else {
                val message = data.getError(HomeWidget.Data::class.java)[0].message
//                homeWidget.value = Fail(ResponseErrorException(message))
                listener.onErrorGetData(ResponseErrorException(message))
            }

        }){
//            homeWidget.value = Fail(it)
            listener.onErrorGetData(it)
        }
    }

}
