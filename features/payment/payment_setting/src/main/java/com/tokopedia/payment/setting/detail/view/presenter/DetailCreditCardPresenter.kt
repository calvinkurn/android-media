package com.tokopedia.payment.setting.detail.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.detail.model.DataResponseDeleteCC
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import java.util.*

class DetailCreditCardPresenter : BaseDaggerPresenter<DetailCreditCardContract.View>(), DetailCreditCardContract.Presenter {

    private val getCCListUseCase = GraphqlUseCase()

    override fun detachView() {
        getCCListUseCase.unsubscribe()
        super.detachView()
    }

    override fun deleteCreditCard(tokenId: String?, resources : Resources?) {
        view.showProgressDialog()
        val variables = HashMap<String, Any?>()
        variables.put(TOKEN_ID, tokenId)
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.delete_credit_card_query), DataResponseDeleteCC::class.java, variables, false)
        getCCListUseCase.clearRequest()
        getCCListUseCase.addRequest(graphqlRequest)
        getCCListUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideProgressDialog()
                    view.onErrorDeleteCC(e, tokenId)
                }
            }

            override fun onNext(objects: GraphqlResponse) {
                view.hideProgressDialog()
                val deleteCC = objects.getData<DataResponseDeleteCC>(DataResponseDeleteCC::class.java)
                view.onDeleteCCResult(deleteCC.removeCreditCard?.isSuccess, deleteCC.removeCreditCard?.message, tokenId)
            }
        })
    }

    companion object {
        val TOKEN_ID = "tokenId"
    }
}
