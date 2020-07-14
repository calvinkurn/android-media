package com.tokopedia.common_category.viewmodel

import androidx.lifecycle.*
import com.tokopedia.common_category.model.bannedCategory.Data
import com.tokopedia.common_category.usecase.repository.CategoryNavRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.seamless_login.subscriber.SeamlessLoginSubscriber
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

private const val IS_BANNED = 1

class BannedProdNavViewModel @Inject constructor() : ViewModel(), LifecycleObserver {

    var categoryName: String = ""

    @Inject
    lateinit var categoryNavRepository: CategoryNavRepository

    @Inject
    lateinit var seamlessLoginUsecase: SeamlessLoginUsecase
    private val mSeamlessLogin: MutableLiveData<Result<String>> by lazy { MutableLiveData<Result<String>>() }
    private var bannedProduct = MutableLiveData<Result<Data>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun fetchBannedProduct() {
        viewModelScope.launchCatchError(block = {
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