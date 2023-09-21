package com.tokopedia.oldcatalog.usecase.detail

import androidx.lifecycle.MutableLiveData
import com.tokopedia.catalog.ui.mapper.CatalogDetailUiMapper
import com.tokopedia.catalog.ui.model.CatalogDetailUiModel
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.network.constant.ErrorNetMessage.MESSAGE_ERROR_NULL_DATA_SHORT
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oldcatalog.model.datamodel.CatalogDetailDataModel
import com.tokopedia.oldcatalog.model.raw.CatalogResponseData
import com.tokopedia.oldcatalog.model.util.CatalogConstant
import com.tokopedia.oldcatalog.model.util.CatalogDetailMapper
import com.tokopedia.oldcatalog.repository.catalogdetail.CatalogDetailRepository
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class CatalogDetailUseCase @Inject constructor(
    private val catalogDetailRepository: CatalogDetailRepository,
    private val catalogDetailUiMapper: CatalogDetailUiMapper,
    private val userSession: UserSessionInterface,
) {

    suspend fun getCatalogDetail(catalogId : String ,comparedCatalogId : String, userId : String, device : String,
                                 catalogDetailDataModel: MutableLiveData<Result<CatalogDetailDataModel>>)  {
        val gqlResponse = catalogDetailRepository.getCatalogDetail(catalogId, comparedCatalogId, userId, device)
        val data = gqlResponse?.getData<CatalogResponseData>(CatalogResponseData::class.java)
        if(data?.catalogGetDetailModular != null)
            catalogDetailDataModel.value = Success(mapIntoModel(comparedCatalogId,data.catalogGetDetailModular))
        else{
            catalogDetailDataModel.value = Fail(Throwable(MESSAGE_ERROR_NULL_DATA_SHORT))
        }
    }

    suspend fun getCatalogDetailV4(
        catalogId : String,
        comparedCatalogId : String,
        catalogDetailDataModel: MutableLiveData<Result<CatalogDetailUiModel>>
    )  {
        val gqlResponse = catalogDetailRepository.getCatalogDetail(
            catalogId,comparedCatalogId, userSession.userId, CatalogConstant.DEVICE)
        val data = gqlResponse?.getData<CatalogResponseData>(CatalogResponseData::class.java)
        if (data?.catalogGetDetailModular != null)
            catalogDetailDataModel.postValue(Success(catalogDetailUiMapper.mapToCatalogDetailUiModel(data.catalogGetDetailModular)))
        else{
            catalogDetailDataModel.postValue(Fail(Throwable(MESSAGE_ERROR_NULL_DATA_SHORT)))
        }
    }

    suspend fun initializeGetCatalogDetail(
        catalogId: String
    ): Boolean {
        val gqlResponse = catalogDetailRepository.getCatalogDetail(
            catalogId,
            "",
            userSession.userId,
            CatalogConstant.DEVICE,
            cacheType = CacheType.ALWAYS_CLOUD
        )
        val data = gqlResponse?.getData<CatalogResponseData>(CatalogResponseData::class.java)
        return if (data?.catalogGetDetailModular != null){
            if (data.catalogGetDetailModular.layouts.orEmpty().isEmpty()
                && data.catalogGetDetailModular.components.orEmpty().isEmpty()) {
                throw MessageErrorException(MESSAGE_ERROR_NULL_DATA_SHORT)
            } else {
                catalogDetailUiMapper.isUsingAboveV4Layout(data.catalogGetDetailModular.version)
            }
        } else {
            throw MessageErrorException(MESSAGE_ERROR_NULL_DATA_SHORT)
        }
    }

    private fun mapIntoModel(comparedCatalogId : String,catalogGetDetailModular : CatalogResponseData.CatalogGetDetailModular) : CatalogDetailDataModel{
        val components = CatalogDetailMapper.mapIntoVisitable(comparedCatalogId, catalogGetDetailModular)
        val fullSpecificationDataModel = CatalogDetailMapper.getFullSpecificationsModel(catalogGetDetailModular)
        return CatalogDetailDataModel(fullSpecificationDataModel,components)
    }
}
