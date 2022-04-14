package com.tokopedia.tokofood.feature.ordertracking.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderStatusMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderStatusResponse
import com.tokopedia.tokofood.feature.ordertracking.domain.query.TokoFoodOrderStatusQuery
import com.tokopedia.tokofood.feature.ordertracking.domain.utils.FileUtilsTemp
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderStatusLiveTrackingUiModel
import javax.inject.Inject

class GetTokoFoodOrderStatusUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<TokoFoodOrderStatusResponse>,
    private val mapper: TokoFoodOrderStatusMapper,
    private val fileUtilsTemp: FileUtilsTemp,
    @ApplicationContext private val context: Context
) {
    init {
        useCase.setGraphqlQuery(TokoFoodOrderStatusQuery)
        useCase.setTypeClass(TokoFoodOrderStatusResponse::class.java)
    }

    suspend fun execute(orderId: String): OrderStatusLiveTrackingUiModel {
        useCase.setRequestParams(TokoFoodOrderStatusQuery.createRequestParamsOrderDetail(orderId))
        return try {
            mapper.mapToOrderStatusLiveTrackingUiModel(useCase.executeOnBackground().tokofoodOrderDetail)
        } catch (e: Exception) {
            throw MessageErrorException(ORDER_STATUS_POOL_STATE, e.localizedMessage)
        }
    }

    fun executeTemp(resourceId: Int): OrderStatusLiveTrackingUiModel {
        val json = fileUtilsTemp.getJsonFromRaw(context.resources, resourceId)
        val response = fileUtilsTemp.getJsonResources<TokoFoodOrderStatusResponse>(json).tokofoodOrderDetail
        return mapper.mapToOrderStatusLiveTrackingUiModel(response)
    }

    companion object {
        const val ORDER_STATUS_POOL_STATE = "orderStatusPoolState"
    }
}