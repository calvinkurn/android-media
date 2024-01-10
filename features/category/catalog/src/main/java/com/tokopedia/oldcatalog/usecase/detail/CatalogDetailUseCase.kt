package com.tokopedia.oldcatalog.usecase.detail

import androidx.lifecycle.MutableLiveData
import com.tokopedia.catalog.ui.mapper.CatalogDetailUiMapper
import com.tokopedia.catalog.ui.model.CatalogDetailUiModel
import com.tokopedia.catalog.ui.model.WidgetTypes
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
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
    private val userSession: UserSessionInterface
) {

    private companion object {
        private const val DATA_STRUCT_ERROR_MESSAGE =
            "Ada gangguan yang lagi dibereskan. Coba lagi atau balik lagi nanti, ya."
    }

    suspend fun getCatalogDetail(
        catalogId: String,
        comparedCatalogId: String,
        userId: String,
        device: String,
        catalogDetailDataModel: MutableLiveData<Result<CatalogDetailDataModel>>
    ) {
        val gqlResponse = catalogDetailRepository.getCatalogDetail(catalogId, comparedCatalogId, userId, device)
        val data = gqlResponse?.getData<CatalogResponseData>(CatalogResponseData::class.java)
        if (data?.catalogGetDetailModular != null) {
            catalogDetailDataModel.value = Success(mapIntoModel(comparedCatalogId, data.catalogGetDetailModular))
        } else {
            catalogDetailDataModel.value = Fail(Throwable(MESSAGE_ERROR_NULL_DATA_SHORT))
        }
    }

    suspend fun getCatalogDetailV4(
        catalogId: String,
        comparedCatalogId: String,
        catalogDetailDataModel: MutableLiveData<Result<CatalogDetailUiModel>>
    ) {
        val gqlResponse = catalogDetailRepository.getCatalogDetail(
            catalogId,
            comparedCatalogId,
            userSession.userId,
            CatalogConstant.DEVICE
        )
        val data = gqlResponse?.getData<CatalogResponseData>(CatalogResponseData::class.java)
        if (data?.catalogGetDetailModular != null) {
            catalogDetailDataModel.postValue(Success(catalogDetailUiMapper.mapToCatalogDetailUiModel(data.catalogGetDetailModular)))
        } else {
            catalogDetailDataModel.postValue(Fail(Throwable(MESSAGE_ERROR_NULL_DATA_SHORT)))
        }
    }

    suspend fun getCatalogDetailV4Comparison(
        catalogId: String,
        comparedCatalogIds: List<String>
    ): ComparisonUiModel? {
        val gqlResponse = catalogDetailRepository.getCatalogDetail(
            catalogId,
            comparedCatalogIds.joinToString(","),
            userSession.userId,
            CatalogConstant.DEVICE,
            cacheType = CacheType.NONE
        )
        val data = gqlResponse?.getData<CatalogResponseData>(CatalogResponseData::class.java)
        if (data?.catalogGetDetailModular != null) {
            val inactiveCatalog = data.catalogGetDetailModular.layouts?.firstOrNull{
                it.type == WidgetTypes.CATALOG_COMPARISON.type
            }?.data?.comparison?.count {
                it.id.isEmpty() || it.id == Int.ZERO.toString()
            }

            if (inactiveCatalog.isMoreThanZero())
                throw InvalidCatalogComparisonException(inactiveCatalog.orZero())

            return catalogDetailUiMapper.mapToCatalogDetailUiModel(data.catalogGetDetailModular).widgets.firstOrNull { it is ComparisonUiModel } as? ComparisonUiModel
        } else {
            throw MessageErrorException(DATA_STRUCT_ERROR_MESSAGE)
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
        return if (data?.catalogGetDetailModular != null) {
            if (data.catalogGetDetailModular.layouts.orEmpty().isEmpty() &&
                data.catalogGetDetailModular.components.orEmpty().isEmpty()
            ) {
                throw MessageErrorException(MESSAGE_ERROR_NULL_DATA_SHORT)
            } else {
                catalogDetailUiMapper.isUsingAboveV4Layout(data.catalogGetDetailModular.version)
            }
        } else {
            throw MessageErrorException(MESSAGE_ERROR_NULL_DATA_SHORT)
        }
    }

    private fun mapIntoModel(comparedCatalogId: String, catalogGetDetailModular: CatalogResponseData.CatalogGetDetailModular): CatalogDetailDataModel {
        val components = CatalogDetailMapper.mapIntoVisitable(comparedCatalogId, catalogGetDetailModular)
        val fullSpecificationDataModel = CatalogDetailMapper.getFullSpecificationsModel(catalogGetDetailModular)
        return CatalogDetailDataModel(fullSpecificationDataModel, components)
    }
}
