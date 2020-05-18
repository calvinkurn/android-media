package com.tokopedia.talk_old.addtalk.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.talk_old.addtalk.domain.usecase.CreateTalkUsecase
import com.tokopedia.talk_old.addtalk.view.listener.AddTalkContract
import com.tokopedia.talk_old.common.di.TalkScope
import com.tokopedia.talk_old.producttalk.view.viewmodel.TalkThreadViewModel
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

/**
 * @author : Steven 17/09/18
 */

class AddTalkPresenter @Inject constructor(@TalkScope val userSession: UserSessionInterface,
                                           @TalkScope val createTalkUsecase: CreateTalkUsecase) :
        AddTalkContract.Presenter,
        BaseDaggerPresenter<AddTalkContract.View>() {

    var isRequesting: Boolean = false

    override fun send(productId: String, text: String) {
        if(isRequesting){
            return
        }
        isRequesting = true
        createTalkUsecase.execute(CreateTalkUsecase.getParam(userSession.userId, productId, text), object : Subscriber<TalkThreadViewModel>(){
            override fun onNext(t: TalkThreadViewModel?) {
                isRequesting = false
                t?.let {
                    view.onSuccessCreateTalk(productId)
                }

            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                isRequesting = false

                view.onErrorCreateTalk(e)
            }

        })
    }

    override fun detachView() {
        super.detachView()
        createTalkUsecase.unsubscribe()
    }
}
