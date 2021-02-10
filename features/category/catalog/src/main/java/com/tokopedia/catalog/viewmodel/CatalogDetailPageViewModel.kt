package com.tokopedia.catalog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.catalog.model.datamodel.CatalogDetailDataModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.catalog.model.raw.CatalogResponse
import com.tokopedia.catalog.model.util.CatalogDetailMapper
import com.tokopedia.catalog.usecase.CatalogDetailUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogDetailPageViewModel @Inject constructor(var catalogDetailUseCase: CatalogDetailUseCase) : ViewModel() {

    private val catalogDetailDataModel = MutableLiveData<Result<CatalogDetailDataModel>>()

    fun getProductCatalog(catalogId: String) {
        viewModelScope.launchCatchError(
                block = {
                    catalogDetailUseCase.getCatalogDetail(catalogId,catalogDetailDataModel)
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