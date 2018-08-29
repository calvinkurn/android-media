package com.tokopedia.talk

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.user.session.UserSession
import rx.Subscriber

/**
 * @author by Steven
 */
class ProductTalkPresenter(private val userSession: UserSession,
                           private val getProductTalkUseCase : GetProductTalkUseCase) :
        ProductTalkContract.Presenter,
        BaseDaggerPresenter<ProductTalkContract.View>() {

    override fun getProductTalk() {
        getProductTalkUseCase.execute(GetProductTalkUseCase.getParam(userSession.userId, 1)
                , object : Subscriber<ProductTalkListViewModel>(){
            override fun onNext(t: ProductTalkListViewModel?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onCompleted() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(e: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    override fun detachView() {
        super.detachView()
    }

}