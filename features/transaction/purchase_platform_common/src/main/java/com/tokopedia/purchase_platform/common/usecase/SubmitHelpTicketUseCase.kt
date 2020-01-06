package com.tokopedia.purchase_platform.common.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.data.model.response.helpticket.SubmitHelpTicketGqlResponse
import com.tokopedia.purchase_platform.common.sharedata.helpticket.SubmitTicketResult
import com.tokopedia.purchase_platform.common.sharedata.helpticket.SubmitTicketText
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject
import javax.inject.Named

class SubmitHelpTicketUseCase @Inject constructor(@Named(QUERY_NAME) val queryString: String, val graphqlUseCase: GraphqlUseCase) : UseCase<SubmitTicketResult>() {

    var compositeSubscription = CompositeSubscription()

    override fun createObservable(params: RequestParams): Observable<SubmitTicketResult> {
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(
                GraphqlRequest(queryString, SubmitHelpTicketGqlResponse::class.java,
                        mapOf(PARAM to params.getObject(PARAM)))
        )
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    val submitHelpTicketGql = it.getData<SubmitHelpTicketGqlResponse>(SubmitHelpTicketGqlResponse::class.java)
                    val submitHelpTicket = submitHelpTicketGql.submitHelpTicketResponse
                    val submitTicketResult = SubmitTicketResult()
                    if (submitHelpTicket.status == STATUS_OK) {
                        submitTicketResult.status = true
                        if (submitHelpTicket.data.message.isNotEmpty()) {
                            submitTicketResult.message = submitHelpTicket.data.message[0]
                        }
                        val submitTicketText = SubmitTicketText()
                        submitTicketText.submitTitle = submitHelpTicket.data.texts.submitTitle
                        submitTicketText.submitDescription = submitHelpTicket.data.texts.submitDescription
                        submitTicketText.successButton = submitHelpTicket.data.texts.successButton
                        submitTicketResult.texts = submitTicketText
                    } else {
                        submitTicketResult.status = false
                        if (submitHelpTicket.errorMessages.isNotEmpty()) {
                            submitTicketResult.message = submitHelpTicket.errorMessages[0]
                        }
                    }
                    submitTicketResult
                }
    }

    companion object {

        const val STATUS_OK = "OK"

        const val PARAM = "ErrorDetail"

        const val QUERY_NAME = "submit_ticket"

        const val PAGE_ATC = "atc"
        const val PAGE_CHECKOUT = "checkout"

        const val GQL_REQUEST_URL = "gql.tokopedia.com"
    }
}