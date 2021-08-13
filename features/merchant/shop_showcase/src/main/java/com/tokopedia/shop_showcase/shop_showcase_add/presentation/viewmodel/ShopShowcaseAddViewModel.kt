package com.tokopedia.shop_showcase.shop_showcase_add.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.*
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.AppendShopShowcaseProductUseCase
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.CreateShopShowcaseUseCase
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.RemoveShopShowcaseProductUseCase
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.UpdateShopShowcaseUseCase
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GetProductListFilter
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.usecase.GetProductListUseCase
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopShowcaseAddViewModel @Inject constructor (
        private val createShopShowcaseUseCase: CreateShopShowcaseUseCase,
        private val getProductListUseCase: GetProductListUseCase,
        private val updateShopShowcaseUseCase: UpdateShopShowcaseUseCase,
        private val appendShopShowcaseProductUseCase: AppendShopShowcaseProductUseCase,
        private val removeShopShowcaseProductUseCase: RemoveShopShowcaseProductUseCase,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val shopId: String by lazy {
        userSession.shopId
    }

    private val _createShopShowcase = MutableLiveData<Result<AddShopShowcaseResponse>>()
    private val _updateShopShowcase = MutableLiveData<Result<UpdateShopShowcaseResponse>>()
    private val _appendNewShowcaseProduct = MutableLiveData<Result<AppendShowcaseProductResponse>>()
    private val _removeShowcaseProduct = MutableLiveData<Result<RemoveShowcaseProductResponse>>()
    private val _selectedProductList = MutableLiveData<Result<List<ShowcaseProduct>>>()
    private val _loaderState = MutableLiveData<Boolean>()
    private val _listOfResponse = MutableLiveData<MutableList<Any>>()

    val createShopShowcase: LiveData<Result<AddShopShowcaseResponse>> get() = _createShopShowcase
    val selectedProductList: LiveData<Result<List<ShowcaseProduct>>> get() = _selectedProductList
    val loaderState: LiveData<Boolean> get() = _loaderState
    val listOfResponse: LiveData<MutableList<Any>> get() = _listOfResponse
    private val updateShopShowcase: LiveData<Result<UpdateShopShowcaseResponse>> get() = _updateShopShowcase
    private val appendNewShowcaseProduct: LiveData<Result<AppendShowcaseProductResponse>> get() = _appendNewShowcaseProduct
    private val removeShowcaseProduct: LiveData<Result<RemoveShowcaseProductResponse>> get() = _removeShowcaseProduct


    fun addShopShowcase(data: AddShopShowcaseParam) {
        launchCatchError(block = {
            showLoader()
            executeAddShopShowcase(data)
            hideLoader()
        }, onError = {
            _createShopShowcase.value = Fail(it)
        })
    }

    fun getSelectedProductList(filter: GetProductListFilter, isLoadMore: Boolean = false) {
        launchCatchError(block = {
            if(isLoadMore) {
                executeGetSelectedProductList(filter)
            } else {
                showLoader()
                executeGetSelectedProductList(filter)
                hideLoader()
            }
        }, onError = {
            _selectedProductList.value = Fail(it)
        })
    }

    fun updateShopShowcase(data: UpdateShopShowcaseParam, newAppendedProduct: AppendShowcaseProductParam, removedProduct: RemoveShowcaseProductParam) {
        launchCatchError(block = {

            showLoader()
            val listOfResponse: MutableList<Any> = mutableListOf()

            /**
             * Asynchronous gql mutation call to update showcase name
             */
            asyncCatchError(
                    block = {
                        executeUpdateShopShowcase(data)
                    },
                    onError = {
                        _updateShopShowcase.postValue(Fail(it))
                    }
            ).await().let {
                listOfResponse.add(updateShopShowcase.value as Result<UpdateShopShowcaseResponse>)
            }

            /**
             * Asynchronous gql mutation call to append new showcase product
             */
            asyncCatchError(
                    block = {
                        executeAppendNewShowcaseProduct(newAppendedProduct)
                    },
                    onError = {
                        it.printStackTrace()
                    }
            ).await().let {
                listOfResponse.add(appendNewShowcaseProduct.value as Result<AppendShowcaseProductResponse>)
            }

            /**
             * Asynchronous gql mutation call to remove showcase product
             */
            asyncCatchError(
                    block = {
                        executeRemoveShowcaseProduct(removedProduct)
                    },
                    onError = {
                        it.printStackTrace()
                    }
            ).await().let {
                listOfResponse.add(removeShowcaseProduct.value as Result<RemoveShowcaseProductResponse>)
            }

            _listOfResponse.value = listOfResponse
            hideLoader()

        }, onError = {
            _updateShopShowcase.value = Fail(it)
        })
    }

    private suspend fun executeAddShopShowcase(data: AddShopShowcaseParam) {
        _createShopShowcase.value = Success(withContext(dispatchers.io){
            createShopShowcaseUseCase.params = CreateShopShowcaseUseCase.createRequestParams(data)
            return@withContext createShopShowcaseUseCase.executeOnBackground()
        })
    }

    private suspend fun executeGetSelectedProductList(filter: GetProductListFilter) {
        _selectedProductList.value = Success(withContext(dispatchers.io){
            getProductListUseCase.params = GetProductListUseCase.createRequestParams(shopId, filter)
            return@withContext getProductListUseCase.executeOnBackground()
        })
    }

    private suspend fun executeUpdateShopShowcase(data: UpdateShopShowcaseParam) {
        _updateShopShowcase.value = Success(withContext(dispatchers.io){
            updateShopShowcaseUseCase.params = UpdateShopShowcaseUseCase.createRequestParams(data)
            updateShopShowcaseUseCase.executeOnBackground()
        })
    }

    private suspend fun executeAppendNewShowcaseProduct(data: AppendShowcaseProductParam) {
        _appendNewShowcaseProduct.value = Success(withContext(dispatchers.io){
            appendShopShowcaseProductUseCase.params = AppendShopShowcaseProductUseCase.createRequestParams(data, shopId)
            return@withContext appendShopShowcaseProductUseCase.executeOnBackground()
        })
    }

    private suspend fun executeRemoveShowcaseProduct(data: RemoveShowcaseProductParam) {
        _removeShowcaseProduct.value = Success(withContext(dispatchers.io){
            removeShopShowcaseProductUseCase.params = RemoveShopShowcaseProductUseCase.createRequestParams(data, shopId)
            return@withContext removeShopShowcaseProductUseCase.executeOnBackground()
        })
    }

    private fun showLoader() {
        _loaderState.value = true
    }

    private fun hideLoader() {
        _loaderState.value = false
    }

}