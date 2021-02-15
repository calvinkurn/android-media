package com.tokopedia.catalog.usecase

import androidx.lifecycle.MutableLiveData
import com.tokopedia.catalog.model.datamodel.CatalogDetailDataModel
import com.tokopedia.catalog.model.raw.CatalogResponseData
import com.tokopedia.catalog.model.util.CatalogDetailMapper
import com.tokopedia.catalog.repository.catalogdetail.CatalogDetailRepository
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogDetailUseCase @Inject constructor(private val catalogDetailRepository: CatalogDetailRepository) {

    suspend fun getCatalogDetail(catalogID : String ,
                                 catalogDetailDataModel: MutableLiveData<Result<CatalogDetailDataModel>>)  {
        val gqlResponse = catalogDetailRepository.getCatalogDetail(catalogID)
        val data = gqlResponse?.getData<CatalogResponseData>(CatalogResponseData::class.java)
        if(data != null)
            catalogDetailDataModel.value = Success(mapIntoModel(data))
//        Handler().postDelayed({
//            catalogDetailDataModel.value = Success(mapIntoModel(CatalogDetailMapper.getDummyCatalogData()))
//        },625)
    }

    private fun mapIntoModel(data :  CatalogResponseData) : CatalogDetailDataModel{
        val components = CatalogDetailMapper.mapIntoVisitable(data.catalogGetDetailModular)
        return CatalogDetailDataModel(components)
    }
}