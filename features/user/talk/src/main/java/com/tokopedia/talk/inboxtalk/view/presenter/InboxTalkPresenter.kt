package com.tokopedia.talk.inboxtalk.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.common.di.TalkScope
import com.tokopedia.talk.inboxtalk.domain.GetInboxTalkUseCase
import com.tokopedia.talk.inboxtalk.view.listener.InboxTalkContract
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkViewModel
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by nisie on 8/29/18.
 */
class InboxTalkPresenter @Inject constructor(@TalkScope val getInboxTalkUseCase:
                                             GetInboxTalkUseCase)
    : BaseDaggerPresenter<InboxTalkContract.View>(),
        InboxTalkContract.Presenter {

    var page: Int = 0
    var page_id: Int = 0

    override fun getInboxTalk(filter: String, nav: String) {
        view.showLoadingFull()
//
//        val list: ArrayList<Visitable<*>> = ArrayList()
//
//        val listChild: ArrayList<Visitable<ProductTalkChildThreadTypeFactory>> = ArrayList()
//
//
//        list.add(InboxTalkItemViewModel
//        (ProductHeader("alskjdlakjsdlakjsldkajlsdjlasjkdlaskjdlaksjdlakjsld",
//                "https://cdn-images-1.medium.com/fit/c/72/72/1*EZ5_P3ZgjQwUJWO9CosQHw.jpeg"),
//                TalkThreadViewModel(
//                        ProductTalkItemViewModel(
//                                "https://cdn-images-1.medium" +
//                                        ".com/fit/c/72/72/1*EZ5_P3ZgjQwUJWO9CosQHw.jpeg",
//                                "Nisie",
//                                "15 Sep pukul 15.40",
//                                "Gan ready gaaak?",
//                                ArrayList()
//                        ), listChild)))
//
//
//        list.add(InboxTalkItemViewModel
//        (ProductHeader("dsa",
//                "https://cdn-images-1.medium.com/fit/c/72/72/1*EZ5_P3ZgjQwUJWO9CosQHw.jpeg"),
//                TalkThreadViewModel(
//                        ProductTalkItemViewModel(
//                                "https://cdn-images-1.medium" +
//                                        ".com/fit/c/72/72/1*EZ5_P3ZgjQwUJWO9CosQHw.jpeg",
//                                "Nisie",
//                                "15 Sep pukul 15.40",
//                                "Gan ready gaaak?",
//                                ArrayList()
//                        ), listChild)))
//
//        list.add(InboxTalkItemViewModel
//        (ProductHeader("asd",
//                "https://cdn-images-1.medium.com/fit/c/72/72/1*EZ5_P3ZgjQwUJWO9CosQHw.jpeg"),
//                TalkThreadViewModel(
//                        ProductTalkItemViewModel(
//                                "https://cdn-images-1.medium" +
//                                        ".com/fit/c/72/72/1*EZ5_P3ZgjQwUJWO9CosQHw.jpeg",
//                                "Nisie",
//                                "15 Sep pukul 15.40",
//                                "Gan ready gaaak?",
//                                ArrayList()
//                        ), listChild)))
//
//        view.onSuccessGetInboxTalk(list)

        getInboxTalkUseCase.execute(GetInboxTalkUseCase.getParam(
                filter,
                nav,
                page,
                page_id
        ), object : Subscriber<InboxTalkViewModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.hideLoadingFull()
                if (e is MessageErrorException) {
                    view.onErrorGetInboxTalk(e.message ?: "")
                } else {
                    view.onErrorGetInboxTalk(ErrorHandler.getErrorMessage(view.getContext(), e))
                }
            }

            override fun onNext(talkViewModel: InboxTalkViewModel) {
                view.hideLoadingFull()
                if (talkViewModel.listTalk.isEmpty()) {
                    view.onEmptyTalk()
                } else {
                    view.onSuccessGetInboxTalk(talkViewModel.listTalk)
                    if (talkViewModel.hasNextPage) {
                        page += 1
                        page_id = talkViewModel.page_id
                    }
                }
            }
        })
    }

    override fun refreshTalk() {

    }

    override fun detachView() {
        super.detachView()
        getInboxTalkUseCase.unsubscribe()
    }

}