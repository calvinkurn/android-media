package com.tokopedia.catalog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.catalog.model.datamodel.CatalogDetailDataModel
import com.tokopedia.catalog.usecase.detail.CatalogDetailUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class CatalogDetailPageViewModel @Inject constructor(private var catalogDetailUseCase: CatalogDetailUseCase) : ViewModel() {

    val catalogDetailDataModel = MutableLiveData<Result<CatalogDetailDataModel>>()

    fun getProductCatalog(catalogId: String,comparedCatalogId : String,userId : String, device : String) {
        viewModelScope.launchCatchError(
                block = {
                    catalogDetailUseCase.getCatalogDetail(catalogId,comparedCatalogId,userId,device,catalogDetailDataModel)
                },
                onError = {
                    catalogDetailDataModel.value = Fail(it)
                }
        )
    }

    fun getCatalogResponseData(): MutableLiveData<Result<CatalogDetailDataModel>> {
        return catalogDetailDataModel
    }
}