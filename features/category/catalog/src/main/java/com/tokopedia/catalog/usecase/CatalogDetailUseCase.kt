package com.tokopedia.catalog.usecase

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import com.tokopedia.catalog.model.datamodel.CatalogDetailDataModel
import com.tokopedia.catalog.model.raw.CatalogResponse
import com.tokopedia.catalog.model.util.CatalogDetailMapper
import com.tokopedia.catalog.repository.catalogdetail.CatalogDetailRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogDetailUseCase @Inject constructor(private val catalogDetailRepository: CatalogDetailRepository) {

    suspend fun getCatalogDetail(catalogID : String ,
                                 catalogDetailDataModel: MutableLiveData<Result<CatalogDetailDataModel>>)  {
        val gqlResponse = catalogDetailRepository.getCatalogDetail(catalogID)
        val error: List<GraphqlError>? = gqlResponse?.getError(CatalogResponse::class.java)
        var data: CatalogResponse.CatalogResponseData? = gqlResponse?.getData<CatalogResponse>(CatalogResponse::class.java)?.data
        // TODO CHANGE
        Handler().postDelayed({
            catalogDetailDataModel.value = Success(mapIntoModel(CatalogDetailMapper.getDummyCatalogData().data))
        },625)
    }

    private fun mapIntoModel(data :  CatalogResponse.CatalogResponseData) : CatalogDetailDataModel{
        val basicInfoModel = CatalogDetailMapper.mapToBasicInfo(data.catalogGetDetailModular.basicInfo)
        val components = CatalogDetailMapper.mapIntoVisitable(data.catalogGetDetailModular)
        return CatalogDetailDataModel(basicInfoModel,components)
    }

}