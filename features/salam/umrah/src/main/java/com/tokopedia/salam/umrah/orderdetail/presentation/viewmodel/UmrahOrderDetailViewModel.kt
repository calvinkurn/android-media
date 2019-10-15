package com.tokopedia.salam.umrah.orderdetail.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.salam.umrah.common.data.UmrahValueLabelEntity
import com.tokopedia.salam.umrah.common.presentation.model.UmrahSimpleDetailModel
import com.tokopedia.salam.umrah.common.presentation.model.UmrahSimpleModel
import com.tokopedia.salam.umrah.orderdetail.data.UmrahOrderDetailsEntity
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by furqan on 08/10/2019
 */
class UmrahOrderDetailViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                    dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val orderDetailData = MutableLiveData<Result<UmrahOrderDetailsEntity>>()

    fun getOrderDetail(rawQuery: String, orderId: String, response: String) {
        val params = mapOf(PARAM_ORDER_ID to orderId,
                PARAM_ORDER_CATEGORY_STR to UMRAH_CATEGORY)

        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, UmrahOrderDetailsEntity.Response::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<UmrahOrderDetailsEntity.Response>()

            orderDetailData.value = Success(data.orderDetails)
        }) {
            // orderDetailData.value = Fail(it)
            val gson = Gson()
            orderDetailData.value = Success(gson.fromJson(response,
                    UmrahOrderDetailsEntity.Response::class.java).orderDetails)
        }
    }

    fun transformToSimpleModel(detailsData: List<UmrahOrderDetailsEntity.DataDetail> = arrayListOf(),
                               passengersData: List<UmrahOrderDetailsEntity.Passenger> = arrayListOf(),
                               valueLabelData: List<UmrahValueLabelEntity> = arrayListOf())
            : List<UmrahSimpleModel> {
        val data = arrayListOf<UmrahSimpleModel>()

        when {
            detailsData.isNotEmpty() -> for (item in detailsData) {
                data.add(UmrahSimpleModel(
                        title = item.label,
                        description = item.value,
                        textColor = item.textColor
                ))
            }
            passengersData.isNotEmpty() -> for ((index, item) in passengersData.withIndex()) {
                data.add(UmrahSimpleModel(
                        title = "${index + 1}. ${item.name}"
                ))
            }
            valueLabelData.isNotEmpty() -> for (item in valueLabelData) {
                data.add(UmrahSimpleModel(
                        title = item.label,
                        description = item.value
                ))
            }
        }

        return data
    }

    fun transformToSimpleDetailModel(detailsData: List<UmrahOrderDetailsEntity.DataDetail>): List<UmrahSimpleDetailModel> {
        val data = arrayListOf<UmrahSimpleDetailModel>()

        for (item in detailsData) {
            data.add(UmrahSimpleDetailModel(
                    title = item.label,
                    subtitle = item.value,
                    icon = item.imageUrl
            ))
        }

        return data
    }

    fun transformToButtonModel(actionButtons: List<UmrahOrderDetailsEntity.ActionButton>): List<UmrahOrderDetailButtonViewModel> {
        val data = arrayListOf<UmrahOrderDetailButtonViewModel>()

        for (item in actionButtons) {
            data.add(UmrahOrderDetailButtonViewModel(item.label, item.buttonType, item.body.appUrl))
        }

        return data
    }

    companion object {
        const val PARAM_ORDER_ID = "orderId"
        const val PARAM_ORDER_CATEGORY_STR = "orderCategoryStr"
        const val UMRAH_CATEGORY = "UMRAH"
    }

}