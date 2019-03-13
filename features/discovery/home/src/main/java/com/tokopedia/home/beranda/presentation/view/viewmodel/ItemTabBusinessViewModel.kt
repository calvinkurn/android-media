package com.tokopedia.home.beranda.presentation.view.viewmodel


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home.beranda.data.model.HomeWidget
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


    fun getList(rawQuery: String, tabId: Int) {
        job.children.map { it.cancel() }
        val params = mapOf(PARAM_TAB_ID to tabId)
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default){
                val graphqlRequest = GraphqlRequest(
                        rawQuery,
                        HomeWidget.Response::class.java,
                        params
                )
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<HomeWidget.Response>()

            if (data.errors.isEmpty()) {
                homeWidget.value = Success(validateTemplate(data.data.homeWidget))
            } else {
                val listMessage = arrayListOf<String>()
                data.errors.forEach { listMessage.add(it.message) }
                val message = TextUtils.join("\n", listMessage)
                homeWidget.value = Fail(ResponseErrorException(message))
            }
        }){
            homeWidget.value = Fail(it)
        }
    }

    private fun validateTemplate(homeWidget: HomeWidget): HomeWidget {
        return HomeWidget(
                homeWidget.tabBusinessList,
                homeWidget.contentItemTabList.filter {
                    it.templateId == 1 || it.templateId == 2 || it.templateId == 3
                }
        )
    }

}
