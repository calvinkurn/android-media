package com.tokopedia.home.beranda.presentation.view.viewmodel


import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.presentation.view.fragment.BusinessUnitItemView
import rx.Subscriber


class ItemTabBusinessViewModel(
        private val graphqlUseCase: GraphqlUseCase
) {

    companion object {
        private const val PARAM_TAB_ID = "tabId"
    }

    fun getList(rawQuery: String, tabId: Int, listener: BusinessUnitItemView) {
        val params = mapOf(PARAM_TAB_ID to tabId)
        val graphqlRequest = GraphqlRequest(
                        rawQuery,
                        HomeWidget.Data::class.java,
                        params
                )
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(OnGetListSubscriber(listener))
    }

}

class OnGetListSubscriber(private val listener: BusinessUnitItemView) : Subscriber<GraphqlResponse>() {
    override fun onNext(data: GraphqlResponse) {
        if (data.getError(HomeWidget.Data::class.java) == null ||
                    data.getError(HomeWidget.Data::class.java).isEmpty()) {
                if (data.getData<HomeWidget.Data>(HomeWidget.Data::class.java) != null) {
                    listener.onSuccessGetData(data.getData<HomeWidget.Data>(HomeWidget.Data::class.java).homeWidget)
                } else {
                    listener.onErrorGetData(ResponseErrorException("local handling error"))
                }
            } else {
                val message = data.getError(HomeWidget.Data::class.java)[0].message
                listener.onErrorGetData(ResponseErrorException(message))
            }
    }

    override fun onCompleted() {

    }

    override fun onError(it: Throwable?) {
        listener.onErrorGetData(ResponseErrorException())
    }

}
