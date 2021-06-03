package com.tokopedia.logintest.common.view.ticker.subscriber

import com.tokopedia.logintest.common.view.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.logintest.login.view.listener.LoginTestAppContract
import rx.Subscriber

/**
 * @author by ade on 8/5/2019
 */
open class TickerInfoLoginSubscriber(val view: LoginTestAppContract.View): Subscriber<List<TickerInfoPojo>>(){
    override fun onNext(t: List<TickerInfoPojo>?) {
        if(t != null)
          view.onSuccessGetTickerInfo(t)
    }

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        view.onErrorGetTickerInfo(e)
    }

}