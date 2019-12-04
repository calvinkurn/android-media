package com.tokopedia.user_identification_common.subscriber

//
// Created by Yoris Prayogo on 2019-10-29.
//

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.user_identification_common.domain.pojo.CheckKtpStatusPojo
import com.tokopedia.user_identification_common.domain.pojo.KtpStatusPojo
import rx.Subscriber

class GetKtpStatusSubscriber(private val listener: GetKtpStatusListener?) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        listener?.onErrorGetKtpStatus(e)
    }

    override fun onNext(graphqlResponse: GraphqlResponse) {
        val pojo = graphqlResponse.getData<CheckKtpStatusPojo>(CheckKtpStatusPojo::class.java)
        val graphqlErrorList = graphqlResponse.getError(CheckKtpStatusPojo::class.java)
        if (listener != null && pojo != null && (graphqlErrorList == null || graphqlErrorList.isEmpty())) {
            routingOnNext(pojo.ktpStatus)
        }
    }

    private fun routingOnNext(pojo: KtpStatusPojo?) {
        if (pojo?.bypass == true || pojo?.valid == true) {
            listener?.onKtpValid()
        } else {
            listener?.onKtpInvalid(pojo?.error)
        }
    }

    interface GetKtpStatusListener {
        fun onErrorGetKtpStatus(throwable: Throwable)
        fun onKtpInvalid(message: String?)
        fun onKtpValid()
    }
}
