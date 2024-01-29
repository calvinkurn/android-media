package com.tokopedia.shop_showcase.shop_showcase_add.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopShowcaseAddViewModel @Inject constructor(
    private val createShopShowcaseUseCase: CreateShopShowcaseUseCase,
    private val getProductListUseCase: GetProductListUseCase,
    private val updateShopShowcaseUseCase: UpdateShopShowcaseUseCase,
    private val appendShopShowcaseProductUseCase: AppendShopShowcaseProductUseCase,
    private val removeShopShowcaseProductUseCase: RemoveShopShowcaseProductUseCase,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

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
    private val _listOfUpdateShowcaseNameResponse = MutableLiveData<MutableList<Any>>()
    private val _listOfAppendResponse = MutableLiveData<MutableList<Any>>()
    private val _listOfRemoveResponse = MutableLiveData<MutableList<Any>>()
    private val _listOfUpdateShowcaseResponse = MutableLiveData<Result<UpdateShopShowcaseResponse>>()
    private val _listOfAppendProductShowcaseResponse = MutableLiveData<Result<AppendShowcaseProductResponse>>()
    private val _listOfRemovedProductShowcaseResponse = MutableLiveData<Result<RemoveShowcaseProductResponse>>()

    val createShopShowcase: LiveData<Result<AddShopShowcaseResponse>> get() = _createShopShowcase
    val selectedProductList: LiveData<Result<List<ShowcaseProduct>>> get() = _selectedProductList
    val loaderState: LiveData<Boolean> get() = _loaderState
    val listOfResponse: LiveData<MutableList<Any>> get() = _listOfResponse

    val listOfUpdateShowcaseNameResponse: LiveData<MutableList<Any>> get() = _listOfUpdateShowcaseNameResponse
    val listOfAppendResponse: LiveData<MutableList<Any>> get() = _listOfAppendResponse
    val listOfRemoveResponse: LiveData<MutableList<Any>> get() = _listOfRemoveResponse

    private val updateShopShowcase: LiveData<Result<UpdateShopShowcaseResponse>> get() = _updateShopShowcase
    private val appendNewShowcaseProduct: LiveData<Result<AppendShowcaseProductResponse>> get() = _appendNewShowcaseProduct
    private val removeShowcaseProduct: LiveData<Result<RemoveShowcaseProductResponse>> get() = _removeShowcaseProduct
//    private val listOfUpdateShowcaseResponse: LiveData<Result<UpdateShopShowcaseResponse>> get() = _listOfUpdateShowcaseResponse
//    private val listOfAppendProductShowcaseResponse: LiveData<Result<AppendShowcaseProductResponse>> get() = _listOfAppendProductShowcaseResponse
//    private val listOfRemovedProductShowcaseResponse: LiveData<Result<RemoveShowcaseProductResponse>> get() = _listOfRemovedProductShowcaseResponse

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
            if (isLoadMore) {
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

    fun updateShowcaseName(data: UpdateShopShowcaseParam) {
        launch(block = {
            showLoader()
            val listOfResponse: MutableList<Any> = mutableListOf()

            // Update showcase name
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

            _listOfUpdateShowcaseNameResponse.value = listOfResponse
            hideLoader()
        })
    }

    fun updateShowcaseAppendProduct(data: UpdateShopShowcaseParam, newAppendedProduct: AppendShowcaseProductParam) {
        launch(block = {
            showLoader()
            val listOfResponse: MutableList<Any> = mutableListOf()

            // Update showcase name
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

            // Update append product
            if (newAppendedProduct.listAppended.size.isMoreThanZero()) {
                asyncCatchError(
                    block = {
                        executeAppendNewShowcaseProduct(newAppendedProduct)
                    },
                    onError = {
                        _appendNewShowcaseProduct.postValue(Fail(it))
                    }
                ).await().let {
                    listOfResponse.add(appendNewShowcaseProduct.value as Result<AppendShowcaseProductResponse>)
                }
            }

            _listOfAppendResponse.value = listOfResponse
            hideLoader()
        })
    }

    fun updateShowcaseRemoveProduct(data: UpdateShopShowcaseParam, removedProduct: RemoveShowcaseProductParam) {
        launch(block = {
            showLoader()
            val listOfResponse: MutableList<Any> = mutableListOf()

            // Update showcase name
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

            // Update remove product
            asyncCatchError(
                block = {
                    executeRemoveShowcaseProduct(removedProduct)
                },
                onError = {
                    _removeShowcaseProduct.postValue(Fail(it))
                }
            ).await().let {
                listOfResponse.add(removeShowcaseProduct.value as Result<RemoveShowcaseProductResponse>)
            }

            _listOfRemoveResponse.value = listOfResponse
            hideLoader()
        })
    }

    fun updateShopShowcase(
        data: UpdateShopShowcaseParam,
        newAppendedProduct: AppendShowcaseProductParam,
        removedProduct: RemoveShowcaseProductParam
    ) {
        launch(block = {
            showLoader()
            val listOfResponse: MutableList<Any> = mutableListOf()

            /**
             * Asynchronous gql mutation call to update showcase name
             * Whatever changes trigger by the user in case of change the current showcase, it will always hit this usecase
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
             * Need to validate whether the `newAppendedProduct` is 0 or not
             * 0 Product means the user did not make any changes/ add new product to current showcase
             */
            if (newAppendedProduct.listAppended.size.isMoreThanZero()) {
                asyncCatchError(
                    block = {
                        executeAppendNewShowcaseProduct(newAppendedProduct)
                    },
                    onError = {
                        _appendNewShowcaseProduct.postValue(Fail(it))
                    }
                ).await().let {
                    listOfResponse.add(appendNewShowcaseProduct.value as Result<AppendShowcaseProductResponse>)
                }
            }

            /**
             * Asynchronous gql mutation call to remove showcase product
             * Need to validate whether the `removedProduct` is 0 or not
             * 0 Product means the user did not make any changes/ remove any product from current showcase
             */
            if (removedProduct.listRemoved.size.isMoreThanZero()) {
                asyncCatchError(
                    block = {
                        executeRemoveShowcaseProduct(removedProduct)
                    },
                    onError = {
                        _removeShowcaseProduct.postValue(Fail(it))
                    }
                ).await().let {
                    listOfResponse.add(removeShowcaseProduct.value as Result<RemoveShowcaseProductResponse>)
                }
            }

            _listOfResponse.value = listOfResponse
            hideLoader()
        })
    }

    private suspend fun executeAddShopShowcase(data: AddShopShowcaseParam) {
        _createShopShowcase.value = Success(
            withContext(dispatchers.io) {
                createShopShowcaseUseCase.params = CreateShopShowcaseUseCase.createRequestParams(data)
                return@withContext createShopShowcaseUseCase.executeOnBackground()
            }
        )
    }

    private suspend fun executeGetSelectedProductList(filter: GetProductListFilter) {
        _selectedProductList.value = Success(
            withContext(dispatchers.io) {
                getProductListUseCase.params = GetProductListUseCase.createRequestParams(shopId, filter)
                return@withContext getProductListUseCase.executeOnBackground()
            }
        )
    }

    private suspend fun executeUpdateShopShowcase(data: UpdateShopShowcaseParam) {
        _updateShopShowcase.value = Success(
            withContext(dispatchers.io) {
                updateShopShowcaseUseCase.params = UpdateShopShowcaseUseCase.createRequestParams(data)
                updateShopShowcaseUseCase.executeOnBackground()
            }
        )
    }

    private suspend fun executeAppendNewShowcaseProduct(data: AppendShowcaseProductParam) {
        _appendNewShowcaseProduct.value = Success(
            withContext(dispatchers.io) {
                appendShopShowcaseProductUseCase.params = AppendShopShowcaseProductUseCase.createRequestParams(data, shopId)
                return@withContext appendShopShowcaseProductUseCase.executeOnBackground()
            }
        )
    }

    private suspend fun executeRemoveShowcaseProduct(data: RemoveShowcaseProductParam) {
        _removeShowcaseProduct.value = Success(
            withContext(dispatchers.io) {
                removeShopShowcaseProductUseCase.params = RemoveShopShowcaseProductUseCase.createRequestParams(data, shopId)
                return@withContext removeShopShowcaseProductUseCase.executeOnBackground()
            }
        )
    }

    private fun showLoader() {
        _loaderState.value = true
    }

    private fun hideLoader() {
        _loaderState.value = false
    }
}
