package com.tokopedia.payment.setting.detail.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.payment.setting.detail.domain.GQLDeleteCreditCardQueryUseCase
import com.tokopedia.payment.setting.detail.model.DataResponseDeleteCC
import rx.Subscriber
import javax.inject.Inject

class DetailCreditCardPresenter @Inject constructor(
        val gqlDeleteCreditCardQuery: GQLDeleteCreditCardQueryUseCase
) : BaseDaggerPresenter<DetailCreditCardContract.View>(),
        DetailCreditCardContract.Presenter {

    private val getCCListUseCase = GraphqlUseCase()

    override fun detachView() {
        getCCListUseCase.unsubscribe()
        super.detachView()
    }

    override fun deleteCreditCard(tokenId: String, resources: Resources?) {
        view.showProgressDialog()
        gqlDeleteCreditCardQuery.execute(tokenId, object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse?) {
                objects?.let {
                    if (isViewAttached) {
                        view.hideProgressDialog()
                        val deleteCC = objects
                                .getData<DataResponseDeleteCC>(DataResponseDeleteCC::class.java)
                        view.onDeleteCCResult(deleteCC?.removeCreditCard?.isSuccess,
                                deleteCC?.removeCreditCard?.message, tokenId)
                    }
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                e?.let {
                    if (isViewAttached) {
                        view.hideProgressDialog()
                        view.onErrorDeleteCC(e, tokenId)
                    }
                }
            }

        })
    }


}
