package com.tokopedia.talk.producttalk.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.talk.common.di.TalkScope
import com.tokopedia.talk.producttalk.domain.usecase.GetProductTalkUseCase
import com.tokopedia.talk.producttalk.view.listener.ProductTalkContract
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkViewModel
import com.tokopedia.user.session.UserSession
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by Steven
 */
class ProductTalkPresenter @Inject constructor(@TalkScope val userSession: UserSession,
                                               @TalkScope val getProductTalkUseCase : GetProductTalkUseCase) :
        ProductTalkContract.Presenter,
        BaseDaggerPresenter<ProductTalkContract.View>() {

    override fun attachView(view: ProductTalkContract.View?) {
        super.attachView(view)
    }

    override fun getProductTalk(productId: String) {
        getProductTalkUseCase.execute(GetProductTalkUseCase.getParam(userSession.userId, 1, productId)
                , object : Subscriber<ProductTalkViewModel>(){
            override fun onNext(viewModel: ProductTalkViewModel) {
                view.show(viewModel)
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.toString()
            }

        })
    }

    override fun detachView() {
        getProductTalkUseCase.unsubscribe()
        super.detachView()
    }

}