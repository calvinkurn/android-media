package com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GetProductListFilter
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.usecase.GetProductListUseCase
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by Rafli Syam on 2020-03-09
 */

class ShowcaseProductAddViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
    private val getProductListUseCase: GetProductListUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val shopId: String by lazy {
        userSession.shopId
    }
    private val _productList = MutableLiveData<Result<List<ShowcaseProduct>>>()
    private val _loadingState = MutableLiveData<Boolean>()
    private val _fetchingState = MutableLiveData<Boolean>()

    val productList: LiveData<Result<List<ShowcaseProduct>>> get() = _productList
    val loadingState: LiveData<Boolean> get() = _loadingState
    val fetchingState: LiveData<Boolean> get() = _fetchingState

    fun getProductList(filter: GetProductListFilter, isLoadMore: Boolean = false) {
        launchCatchError(block = {
            if(isLoadMore) {
                showProgressDialog()
                executeProductListUseCase(filter)
                hideProgressDialog()
            } else {
                showFetchingProgress()
                executeProductListUseCase(filter)
                hideFetchingProgress()
            }
        }, onError = {
            _productList.value = Fail(it)
        })
    }

    private suspend fun executeProductListUseCase(filter: GetProductListFilter) {
        _productList.value = Success(withContext(dispatchers.io){
            getProductListUseCase.params = GetProductListUseCase.createRequestParams(shopId, filter)
            return@withContext getProductListUseCase.executeOnBackground()
        })
    }

    private fun showProgressDialog() {
        _loadingState.value = true
    }

    private fun hideProgressDialog() {
        _loadingState.value = false
    }

    private fun showFetchingProgress() {
        _fetchingState.value = true
    }

    private fun hideFetchingProgress() {
        _fetchingState.value = false
    }

}