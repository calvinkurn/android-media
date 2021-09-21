package com.tokopedia.salam.umrah.orderdetail.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.salam.umrah.common.data.MyUmrahEntity
import com.tokopedia.salam.umrah.common.data.UmrahValueLabelEntity
import com.tokopedia.salam.umrah.common.presentation.model.UmrahMyUmrahWidgetModel
import com.tokopedia.salam.umrah.common.presentation.model.UmrahSimpleDetailModel
import com.tokopedia.salam.umrah.common.presentation.model.UmrahSimpleModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.salam.umrah.orderdetail.data.UmrahOrderDetailButtonModel
import com.tokopedia.salam.umrah.orderdetail.data.UmrahOrderDetailsEntity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by furqan on 08/10/2019
 */
class UmrahOrderDetailViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                    private val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

    val orderDetailData = MutableLiveData<Result<UmrahOrderDetailsEntity>>()
    val myWidgetData = MutableLiveData<Result<UmrahMyUmrahWidgetModel>>()

    fun getOrderDetail(rawQuery: String, orderId: String) {
        val params = mapOf(PARAM_ORDER_ID to orderId,
                PARAM_ORDER_CATEGORY_STR to UMRAH_CATEGORY)

        launchCatchError(block = {
            val data = withContext(dispatcher.main) {
                val graphqlRequest = GraphqlRequest(rawQuery, UmrahOrderDetailsEntity.Response::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<UmrahOrderDetailsEntity.Response>()
            orderDetailData.value = Success(data.orderDetails)
        }) {
            orderDetailData.value = Fail(it)
        }
    }

    fun getMyUmrahWidget(rawQuery: String, orderId: String) {
        val params = mapOf(PARAM_ORDER_ID to orderId)

        launchCatchError(block = {
            val data = withContext(dispatcher.main) {
                val graphqlRequest = GraphqlRequest(rawQuery, MyUmrahEntity.Response::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<MyUmrahEntity.Response>()

            myWidgetData.value = Success(transformToMyUmrahWidgetModel(data.umrahWidgetSaya))
        }) {
            myWidgetData.value = Fail(it)
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

    fun transformToButtonModel(actionButtons: List<UmrahOrderDetailsEntity.ActionButton>): List<UmrahOrderDetailButtonModel> {
        val data = arrayListOf<UmrahOrderDetailButtonModel>()

        for (item in actionButtons) {
            data.add(UmrahOrderDetailButtonModel(item.label, item.buttonType, item.body.appUrl))
        }

        return data
    }

    private fun transformToMyUmrahWidgetModel(data: MyUmrahEntity): UmrahMyUmrahWidgetModel =
            UmrahMyUmrahWidgetModel(data.header, data.subHeader, data.nextActionText, data.mainButton.text, data.mainButton.link)

    companion object {
        const val PARAM_ORDER_ID = "orderId"
        const val PARAM_ORDER_CATEGORY_STR = "orderCategoryStr"
        const val UMRAH_CATEGORY = "UMROH"
    }

}