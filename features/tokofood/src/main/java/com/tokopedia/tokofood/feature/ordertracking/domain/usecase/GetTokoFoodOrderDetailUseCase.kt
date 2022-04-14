package com.tokopedia.tokofood.feature.ordertracking.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderDetailMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderDetailResponse
import com.tokopedia.tokofood.feature.ordertracking.domain.query.TokoFoodOrderDetailQuery
import com.tokopedia.tokofood.feature.ordertracking.domain.utils.FileUtilsTemp
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailResultUiModel
import javax.inject.Inject

class GetTokoFoodOrderDetailUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val useCase: GraphqlUseCase<TokoFoodOrderDetailResponse>,
    private val tokoFoodOrderDetailMapper: TokoFoodOrderDetailMapper,
    private val fileUtilsTemp: FileUtilsTemp
) {
    init {
        useCase.setGraphqlQuery(TokoFoodOrderDetailQuery)
        useCase.setTypeClass(TokoFoodOrderDetailResponse::class.java)
    }

    suspend fun execute(orderId: String) {
        useCase.setRequestParams(TokoFoodOrderDetailQuery.createRequestParamsOrderDetail(orderId))
        //todo mapping
        try {
            useCase.executeOnBackground()
        } catch (e: Exception) {

        }
    }

    fun executeTemp(resourceId: Int): OrderDetailResultUiModel {
        val json = fileUtilsTemp.getJsonFromRaw(context.resources, resourceId)
        val response = fileUtilsTemp.getJsonResources<TokoFoodOrderDetailResponse>(json).tokofoodOrderDetail
        return tokoFoodOrderDetailMapper.mapToOrderDetailResultUiModel(response)
    }
}