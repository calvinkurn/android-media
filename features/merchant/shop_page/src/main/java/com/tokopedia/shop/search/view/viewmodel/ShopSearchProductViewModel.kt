package com.tokopedia.shop.search.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.search.data.model.UniverseSearchResponse
import com.tokopedia.shop.search.domain.interactor.GetSearchShopProductUseCase
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
        private val getSearchShopProductUseCase: GetSearchShopProductUseCase,
        coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel(coroutineDispatcher) {

    private val _shopSearchProductResult by lazy {
        MutableLiveData<Result<UniverseSearchResponse>>()
    }

    val shopSearchProductResult: LiveData<Result<UniverseSearchResponse>> by lazy {
        _shopSearchProductResult
    }

    fun isLoggedIn() = userSessionInterface.isLoggedIn

    fun getSearchShopProduct(shopId: String, searchQuery: String) {
        launchCatchError(block = {
            val result = withContext(Dispatchers.IO) {
                getSearchShopProductUseCase.requestParams = GetSearchShopProductUseCase
                        .createRequestParam(
                                shopId.toIntOrZero(),
                                searchQuery
                        )
                getSearchShopProductUseCase.executeOnBackground()
            }
            _shopSearchProductResult.value = Success(result)
        }) {
            _shopSearchProductResult.value = Fail(it)
        }
    }
}