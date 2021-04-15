package com.tokopedia.hotel.evoucher.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.evoucher.data.entity.SharePdfDataParam
import com.tokopedia.hotel.evoucher.data.entity.SharePdfDataResponse
import com.tokopedia.hotel.orderdetail.data.model.HotelOrderDetail
import com.tokopedia.hotel.orderdetail.presentation.activity.HotelOrderDetailActivity
import com.tokopedia.hotel.orderdetail.usecase.GetHotelOrderDetailUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by furqan on 23/05/19
 */
class HotelEVoucherViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                 private val dispatcher: CoroutineDispatchers,
                                                 private val useCase: GetHotelOrderDetailUseCase)
    : BaseViewModel(dispatcher.io) {

    val orderDetailData = MutableLiveData<Result<HotelOrderDetail>>()
    val sharePdfData = MutableLiveData<Result<SharePdfDataResponse>>()

    fun getOrderDetail(rawQuery: String, orderId: String) {
        launch {
            orderDetailData.postValue(useCase.execute(rawQuery, orderId, HotelOrderDetailActivity.HOTEL_ORDER_CATEGORY, false))
        }
    }

    fun sendPdf(rawQuery: String, emailList: List<String>, orderId: String) {
        val requestParams = SharePdfDataParam(
                orderId = orderId,
                emailTarget = emailList)
        val sharePdfParams = mapOf(PARAM_HOTEL_PDF_PARAM to requestParams)

        launchCatchError(block = {
            val response = withContext(dispatcher.main) {
                val sharePdfRequest = GraphqlRequest(rawQuery, TYPE_SHARE_PDF, sharePdfParams)
                graphqlRepository.getReseponse(listOf(sharePdfRequest))
            }.getSuccessData<SharePdfDataResponse>()

            sharePdfData.postValue(Success(response))
        }) {
            sharePdfData.postValue(Fail(it))
        }
    }

    companion object {
        const val PARAM_HOTEL_PDF_PARAM = "data"

        private val TYPE_SHARE_PDF = SharePdfDataResponse::class.java
    }
}