package com.tokopedia.talk.inboxtalk.presenter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.talk.ProductTalkChildThreadTypeFactory
import com.tokopedia.talk.ProductTalkItemViewModel
import com.tokopedia.talk.ProductTalkThreadViewModel
import com.tokopedia.talk.inboxtalk.listener.InboxTalkContract
import com.tokopedia.talk.inboxtalk.viewmodel.InboxTalkItemViewModel
import com.tokopedia.talk.inboxtalk.viewmodel.ProductHeader
import javax.inject.Inject

/**
 * @author by nisie on 8/29/18.
 */
class InboxTalkPresenter @Inject constructor()
    : BaseDaggerPresenter<InboxTalkContract.View>(),
        InboxTalkContract.Presenter {


    override fun getInboxTalk() {
        view.showLoadingFull()

        val list: ArrayList<Visitable<*>> = ArrayList()

        val listChild: ArrayList<Visitable<ProductTalkChildThreadTypeFactory>> = ArrayList()


        list.add(InboxTalkItemViewModel
        (ProductHeader("alskjdlakjsdlakjsldkajlsdjlasjkdlaskjdlaksjdlakjsld",
                "https://cdn-images-1.medium.com/fit/c/72/72/1*EZ5_P3ZgjQwUJWO9CosQHw.jpeg"),
                ProductTalkThreadViewModel(
                        ProductTalkItemViewModel(
                                "https://cdn-images-1.medium" +
                                        ".com/fit/c/72/72/1*EZ5_P3ZgjQwUJWO9CosQHw.jpeg",
                                "Nisie",
                                "15 Sep pukul 15.40",
                                "Gan ready gaaak?",
                                ArrayList()
                        ), listChild)))


        list.add(InboxTalkItemViewModel
        (ProductHeader("dsa",
                "https://cdn-images-1.medium.com/fit/c/72/72/1*EZ5_P3ZgjQwUJWO9CosQHw.jpeg"),
                ProductTalkThreadViewModel(
                        ProductTalkItemViewModel(
                                "https://cdn-images-1.medium" +
                                        ".com/fit/c/72/72/1*EZ5_P3ZgjQwUJWO9CosQHw.jpeg",
                                "Nisie",
                                "15 Sep pukul 15.40",
                                "Gan ready gaaak?",
                                ArrayList()
                        ), listChild)))

        list.add(InboxTalkItemViewModel
        (ProductHeader("asd",
                "https://cdn-images-1.medium.com/fit/c/72/72/1*EZ5_P3ZgjQwUJWO9CosQHw.jpeg"),
                ProductTalkThreadViewModel(
                        ProductTalkItemViewModel(
                                "https://cdn-images-1.medium" +
                                        ".com/fit/c/72/72/1*EZ5_P3ZgjQwUJWO9CosQHw.jpeg",
                                "Nisie",
                                "15 Sep pukul 15.40",
                                "Gan ready gaaak?",
                                ArrayList()
                        ), listChild)))

        view.onSuccessGetInboxTalk(list)
    }

    override fun refreshTalk() {

    }

    override fun detachView() {
        super.detachView()
    }

}