package com.tokopedia.chat_common.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.chat_common.domain.GetChatUseCase
import com.tokopedia.chat_common.view.listener.BaseChatContract
import com.tokopedia.chat_common.view.viewmodel.ChatRoomViewModel
import rx.Subscriber
import javax.inject.Inject

/**
 * @author : Steven 29/11/18
 */

class BaseChatPresenter @Inject constructor(
        val getChatUseCase : GetChatUseCase)
    : BaseDaggerPresenter<BaseChatContract.View>(), BaseChatContract.Presenter {

    override fun attachView(view: BaseChatContract.View?) {
        super.attachView(view)
    }


    override fun detachView() {
        super.detachView()
    }

    override fun getChatUseCase(messageId: String) {
        getChatUseCase(messageId, 1)
    }

    override fun getChatUseCase(messageId: String, page: Int) {
        getChatUseCase.execute(GetChatUseCase.generateParam(messageId, page)
                , object : Subscriber<ChatRoomViewModel>(){
            override fun onNext(model: ChatRoomViewModel?) {
                view.developmentView()
                model?.listChat.let { view.onSuccessGetChat(it!!) }
            }

            override fun onCompleted() {
                return
            }

            override fun onError(e: Throwable?) {
                return
            }

        })
    }
}
