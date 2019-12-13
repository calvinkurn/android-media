package com.tokopedia.discovery.categoryrevamp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.seamless_login.subscriber.SeamlessLoginSubscriber
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class BannedProdNavViewModel @Inject constructor() : ViewModel(){

    @Inject
    lateinit var seamlessLoginUsecase: SeamlessLoginUsecase
    val mSeamlessLogin: MutableLiveData<Result<String>> by lazy { MutableLiveData<Result<String>>() }


    fun openBrowserSeamlessly(url: String) {
        seamlessLoginUsecase.generateSeamlessUrl(url, seamlessLoginSubscriber)
    }

    private val seamlessLoginSubscriber: SeamlessLoginSubscriber? = object : SeamlessLoginSubscriber {
        override fun onUrlGenerated(url: String) {
            mSeamlessLogin.value = Success(url)
        }

        override fun onError(msg: String) {
            mSeamlessLogin.value = Fail(Throwable(msg))
        }
    }

}