package com.tokopedia.talk.talkdetails.view.contract

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView

/**
 * Created by Hendri on 28/08/18.
 */
class TalkDetailsContract {
    interface Presenter {
        fun loadTalkDetails(id:String)
        fun reportTalkComment(id:String)
        fun deleteTalkComment(id:String)
        fun onDestroy()
    }
    interface View:CustomerView {
        fun onError(message:String)
        fun onSuccessLoadTalkDetails(data:List<Visitable<*>>)
        fun onSuccessReportTalkComment(id:String)
        fun onSuccessDeleteTalkComment(id:String)
    }
}