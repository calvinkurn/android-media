package com.tokopedia.catalog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.catalog.model.raw.CatalogResponse
import com.tokopedia.catalog.model.util.CatalogDetailMapper
import com.tokopedia.catalog.usecase.GetProductCatalogOneUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogDetailPageViewModel @Inject constructor(var getProductCatalogOneUseCase: GetProductCatalogOneUseCase) : ViewModel() {

    private val catalogResponseData = MutableLiveData<Result<CatalogResponse.CatalogResponseData>>()

    fun getProductCatalog(catalogId: String) {
        viewModelScope.launchCatchError(
                block = {
                    val response = getProductCatalogOneUseCase.getCatalogDetail(catalogId)
                    //val item: CatalogResponse.CatalogResponseData? = response?.getData<CatalogResponse.CatalogResponseData>(CatalogResponse.CatalogResponseData::class.java)

                    val item = CatalogDetailMapper.getDummyCatalogData().data
                    item?.let {
                        catalogResponseData.value = Success(it)
                    }
                },
                onError = {
                    catalogResponseData.value = Fail(it)
                }
        )
    }

    fun getCatalogResponseData(): MutableLiveData<Result<CatalogResponse.CatalogResponseData>> {
        return catalogResponseData
    }
}