package com.tokopedia.home.beranda.presentation.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

class TabBusinessViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        @Named("Main")
        private val baseDispatcher: CoroutineDispatcher
) : ViewModel(), CoroutineScope {

    private val job = Job()
    val homeWidget : LiveData<HomeWidget>
    get() = _homeWidget

    private val _homeWidget = MutableLiveData<HomeWidget>()

    val homeWidgetErrorAction : LiveData<Fail>
        get() = _homeWidgetErrorAction

    private val _homeWidgetErrorAction = MutableLiveData<Fail>()

    companion object {
        private const val PARAM_TAB_ID = "tabId"
    }

    override val coroutineContext: CoroutineContext
        get() = baseDispatcher + job

    fun getTabList(rawQuery: String) {
        _homeWidget.value = HomeWidget()
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
                    val temp = data.getData<HomeWidget.Data>(HomeWidget.Data::class.java).homeWidget
                    if (temp.tabBusinessList.isEmpty().not()) {
                        _homeWidget.value = Success(temp).data
                    } else {
                        _homeWidgetErrorAction.value = Fail(ResponseErrorException("empty"))
                    }
                } else {
                    _homeWidgetErrorAction.value = Fail(ResponseErrorException("local handling error"))
                }
            } else {
                val message = data.getError(HomeWidget.Data::class.java)[0].message
                _homeWidgetErrorAction.value = Fail(ResponseErrorException(message))
            }

        }){
            it.printStackTrace()
            _homeWidgetErrorAction.value = Fail(it)
        }

    }

    fun getTabList(position: Int): HomeWidget.TabItem? {
        return homeWidget.value?.tabBusinessList?.get(position)
    }
}
