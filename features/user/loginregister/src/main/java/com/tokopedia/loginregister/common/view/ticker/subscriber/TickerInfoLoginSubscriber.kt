package com.tokopedia.loginregister.common.view.ticker.subscriber

import com.tokopedia.loginregister.login.view.listener.LoginEmailPhoneContract
import com.tokopedia.loginregister.common.view.ticker.domain.pojo.TickerInfoPojo
import rx.Subscriber

/**
 * @author by ade on 8/5/2019
 */
open class TickerInfoLoginSubscriber(val view: LoginEmailPhoneContract.View): Subscriber<List<TickerInfoPojo>>(){
    override fun onNext(t: List<TickerInfoPojo>?) {
        if(t != null)
          view.onSuccessGetTickerInfo(t)
    }

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        view.onErrorGetTickerInfo(e)
    }

}