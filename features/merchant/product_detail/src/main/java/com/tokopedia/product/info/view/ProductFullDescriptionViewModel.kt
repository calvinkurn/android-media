package com.tokopedia.product.info.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.view.util.DynamicProductDetailDispatcherProvider
import com.tokopedia.product.detail.view.util.asFail
import com.tokopedia.product.detail.view.util.asSuccess
import com.tokopedia.product.info.model.specification.ProductSpecificationResponse
import com.tokopedia.product.info.usecase.GetProductSpecificationUseCase
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Yehezkiel on 22/07/20
 */
class ProductFullDescriptionViewModel @Inject constructor(private val dispatcher: DynamicProductDetailDispatcherProvider,
                                                          private val getProductSpecificationUseCase: GetProductSpecificationUseCase)
    : BaseViewModel(dispatcher.ui()) {

    private val catalogId = MutableLiveData<String>()
    private val _specificationResponseData = MediatorLiveData<Result<ProductSpecificationResponse>>()
    val specificationResponseData: LiveData<Result<ProductSpecificationResponse>>
        get() = _specificationResponseData

    init {
        _specificationResponseData.addSource(catalogId) {
            launchCatchError(block = {
                withContext(dispatcher.io()) {
                    val data = getProductSpecificationUseCase.executeOnBackground(GetProductSpecificationUseCase.createParams(it))
                    _specificationResponseData.postValue(data.asSuccess())
                }
            }) {
                _specificationResponseData.postValue(it.asFail())
            }
        }
    }

    fun setCatalogId(catalogId:String) {
        this.catalogId.value = catalogId
    }

}