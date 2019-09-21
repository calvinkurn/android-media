package com.tokopedia.shop.search.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.search.data.model.UniverseSearchResponse
import com.tokopedia.shop.search.domain.GetSearchShopProductUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopSearchProductViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val coroutineDispatcher: CoroutineDispatcher,
        private val getSearchShopProductUseCase: GetSearchShopProductUseCase
) : BaseViewModel(coroutineDispatcher) {

    val shopSearchProductResult by lazy {
        MutableLiveData<Result<UniverseSearchResponse>>()
    }

    fun isLoggedIn() = userSessionInterface.isLoggedIn

    fun submitSearchQuery(searchQuery: String) {
        launchCatchError(block = {
            val result = withContext(Dispatchers.IO) {
                getSearchShopProductUseCase.requestParams = GetSearchShopProductUseCase
                        .createRequestParam(
                                "2400899".toIntOrZero(),
                                searchQuery
                        )
                getSearchShopProductUseCase.executeOnBackground()
            }
            shopSearchProductResult.value = Success(result)
        }) {
            shopSearchProductResult.value = Fail(it)
        }
    }
}