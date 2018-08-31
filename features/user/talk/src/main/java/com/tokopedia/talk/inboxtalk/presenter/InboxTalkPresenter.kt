package com.tokopedia.talk.inboxtalk.presenter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.talk.inboxtalk.listener.InboxTalkContract
import com.tokopedia.talk.inboxtalk.viewmodel.InboxTalkItemViewModel
import javax.inject.Inject

/**
 * @author by nisie on 8/29/18.
 */
class InboxTalkPresenter @Inject constructor()
    : BaseDaggerPresenter<InboxTalkContract.View>(),
        InboxTalkContract.Presenter {


    override fun getInboxTalk() {
        view.showLoadingFull()

        val list : ArrayList<Visitable<*>> = ArrayList()
        list.add(InboxTalkItemViewModel
        ("asdalaksjldakjsldkajsldkjalsdkjalskdlaksdlakjsldakjsldkajlsdjk " +
                "alsjdlakjsdlkajlsdalksjdlaksjdlakjsldakjsldkjalksjdlakjsdlakjsdsd1"))
        list.add(InboxTalkItemViewModel("asdasd2"))
        list.add(InboxTalkItemViewModel("asdasd3"))
        view.onSuccessGetInboxTalk(list)
    }

    override fun refreshTalk() {

    }

    override fun detachView() {
        super.detachView()
    }

}