package com.tokopedia.catalog.usecase.detail

import androidx.lifecycle.MutableLiveData
import com.tokopedia.catalog.model.datamodel.CatalogDetailDataModel
import com.tokopedia.catalog.model.raw.CatalogResponseData
import com.tokopedia.catalog.model.util.CatalogDetailMapper
import com.tokopedia.catalog.repository.catalogdetail.CatalogDetailRepository
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogDetailUseCase @Inject constructor(private val catalogDetailRepository: CatalogDetailRepository) {

    suspend fun  getCatalogDetail(catalogId : String ,comparedCatalogId : String, userId : String, device : String,
                                 catalogDetailDataModel: MutableLiveData<Result<CatalogDetailDataModel>>)  {
        val gqlResponse = catalogDetailRepository.getCatalogDetail(catalogId,comparedCatalogId, userId, device)
        val data = gqlResponse?.getData<CatalogResponseData>(CatalogResponseData::class.java)
        if(data?.catalogGetDetailModular != null)
            catalogDetailDataModel.value = Success(mapIntoModel(comparedCatalogId,data.catalogGetDetailModular))
        else{
            catalogDetailDataModel.value = Fail(Throwable("No data found"))
        }
    }

    private fun mapIntoModel(comparedCatalogId : String,catalogGetDetailModular : CatalogResponseData.CatalogGetDetailModular) : CatalogDetailDataModel{
        val components = CatalogDetailMapper.mapIntoVisitable(comparedCatalogId, catalogGetDetailModular)
        val fullSpecificationDataModel = CatalogDetailMapper.getFullSpecificationsModel(catalogGetDetailModular)
        return CatalogDetailDataModel(fullSpecificationDataModel,components)
    }
}