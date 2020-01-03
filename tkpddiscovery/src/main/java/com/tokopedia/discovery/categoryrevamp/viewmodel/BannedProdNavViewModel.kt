package com.tokopedia.discovery.categoryrevamp.viewmodel

import androidx.lifecycle.*
import com.tokopedia.discovery.categoryrevamp.data.bannedCategory.Data
import com.tokopedia.discovery.categoryrevamp.domain.repository.CategoryNavRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.seamless_login.subscriber.SeamlessLoginSubscriber
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val IS_BANNED = 1

class BannedProdNavViewModel @Inject constructor() : ViewModel(), CoroutineScope, LifecycleObserver {

    private val jobs = SupervisorJob()
    var categoryName: String = ""
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + jobs

    @Inject
    lateinit var categoryNavRepository: CategoryNavRepository

    @Inject
    lateinit var seamlessLoginUsecase: SeamlessLoginUsecase
    private val mSeamlessLogin: MutableLiveData<Result<String>> by lazy { MutableLiveData<Result<String>>() }
    private var bannedProduct = MutableLiveData<Result<Data>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun fetchBannedProduct() {
        launchCatchError(block = {
            val bannedResponse = categoryNavRepository.getCategoryDetail(categoryName)
            bannedResponse?.let {
                handleDataForBanned(it)
            }
        }, onError = {
            bannedProduct.value = Fail(it)
        })
    }

    private fun handleDataForBanned(data: Data) {
        if (data.isBanned == IS_BANNED) {
            bannedProduct.value = Success(data)
        } else {
            bannedProduct.value = Fail(Throwable())
        }
    }

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