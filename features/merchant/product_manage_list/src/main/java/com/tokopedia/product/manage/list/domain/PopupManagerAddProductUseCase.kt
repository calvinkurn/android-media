package com.tokopedia.product.manage.list.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.product.manage.list.data.model.PopUpManagerViewModel
import com.tokopedia.product.manage.list.view.presenter.ProductManagePresenterImpl.GQL_POPUP_NAME
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class PopupManagerAddProductUseCase @Inject constructor(@Named(GQL_POPUP_NAME) private val popUpQuery: String,
                                                        private val graphqlUseCase: GraphqlUseCase) : UseCase<Boolean>() {

    companion object {
        private const val SHOP_ID = "shopID"

        fun createRequestParams(shopId: Int): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putInt(SHOP_ID, shopId)
            return requestParams
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {

        val graphqlRequest = GraphqlRequest(popUpQuery, PopUpManagerViewModel::class.java, requestParams.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            val data: PopUpManagerViewModel? = it.getData(PopUpManagerViewModel::class.java)
            val error: MutableList<GraphqlError> = it.getError(GraphqlError::class.java)
                    ?: mutableListOf()

            if (data == null) {
                throw RuntimeException()
            } else if (error.isNotEmpty() && error.first().message.isNotEmpty()) {
                throw MessageErrorException(error.first().message)
            }

            data.shopManagerPopups.data.showPopUp
        }
    }

}