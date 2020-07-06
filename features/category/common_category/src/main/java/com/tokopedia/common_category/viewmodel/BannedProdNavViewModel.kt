package com.tokopedia.common_category.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.common_category.model.bannedCategory.Data
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.seamless_login.subscriber.SeamlessLoginSubscriber
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class BannedProdNavViewModel @Inject constructor() : ViewModel(), LifecycleObserver {

    @Inject
    lateinit var seamlessLoginUsecase: SeamlessLoginUsecase

    private val mSeamlessLogin: MutableLiveData<Result<String>> by lazy { MutableLiveData<Result<String>>() }
    private var bannedProduct = MutableLiveData<Result<Data>>()

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

    fun getBannedProductLiveData(): LiveData<Result<Data>> {
        return bannedProduct
    }

    fun getSeamlessLoginLiveData(): LiveData<Result<String>> {
        return mSeamlessLogin
    }

}