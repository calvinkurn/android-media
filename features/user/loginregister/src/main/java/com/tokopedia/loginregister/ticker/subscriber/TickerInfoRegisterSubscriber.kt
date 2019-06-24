package com.tokopedia.loginregister.ticker.subscriber

import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.loginregister.login.view.listener.LoginEmailPhoneContract
import com.tokopedia.loginregister.registerinitial.view.listener.RegisterInitialContract
import com.tokopedia.loginregister.ticker.domain.pojo.TickerInfoPojo
import rx.Subscriber

/**
 * @author by ade on 8/5/2019
 */
open class TickerInfoRegisterSubscriber(val view: RegisterInitialContract.View): Subscriber<List<TickerInfoPojo>>(){
    override fun onNext(t: List<TickerInfoPojo>?) {
        if(t != null)
          view.onSuccessGetTickerInfo(t)
    }

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        view.onErrorGetTickerInfo(ErrorHandler.getErrorMessage(view.context, e))
    }

}