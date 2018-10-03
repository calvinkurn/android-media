package com.tokopedia.talk.addtalk.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.addtalk.domain.usecase.CreateTalkUsecase
import com.tokopedia.talk.addtalk.view.listener.AddTalkContract
import com.tokopedia.talk.common.di.TalkScope
import com.tokopedia.talk.producttalk.view.viewmodel.TalkThreadViewModel
import com.tokopedia.user.session.UserSession
import rx.Subscriber
import javax.inject.Inject

/**
 * @author : Steven 17/09/18
 */

class AddTalkPresenter @Inject constructor(@TalkScope val userSession: UserSession,
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
                view.onSuccessCreateTalk(productId)
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                isRequesting = false

                view.onErrorCreateTalk(e)
            }

        })
    }

}