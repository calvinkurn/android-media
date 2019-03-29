package com.tokopedia.interestpick.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.interestpick.domain.usecase.GetInterestUseCase
import com.tokopedia.interestpick.domain.usecase.UpdateInterestUseCase
import com.tokopedia.interestpick.view.listener.InterestPickContract
import com.tokopedia.interestpick.view.subscriber.GetInterestSubscriber
import com.tokopedia.interestpick.view.subscriber.UpdateInterestSubscriber
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by milhamj on 07/09/18.
 */
class InterestPickPresenter @Inject constructor(private val getInterestUseCase: GetInterestUseCase,
                                                private val updateInterestUseCase: UpdateInterestUseCase)
    : BaseDaggerPresenter<InterestPickContract.View>(), InterestPickContract.Presenter {

    override fun detachView() {
        super.detachView()
        getInterestUseCase.unsubscribe()
        updateInterestUseCase.unsubscribe()
    }

    override fun fetchData() {
        view.showLoading()
        getInterestUseCase.execute(GetInterestSubscriber(view))
    }

    override fun updateInterest(interestIds: Array<Int>) {
        view.showProgress()
        updateInterestUseCase.execute(
                UpdateInterestUseCase.getRequestParams(interestIds), UpdateInterestSubscriber(view)
        )
    }

    override fun onBackPressed() {
        view.showProgress()
        updateInterestUseCase.execute(
                UpdateInterestUseCase.getRequestParamsSkip(),
                object : Subscriber<GraphqlResponse>() {
                    override fun onNext(t: GraphqlResponse?) {
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                    }
                }
        )
    }
}