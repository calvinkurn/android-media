package com.tokopedia.deals.pdp.ui.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.deals.pdp.data.CartData
import com.tokopedia.deals.pdp.data.DealsRatingUpdateResponse
import com.tokopedia.deals.pdp.data.DealsVerifyRequest
import com.tokopedia.deals.pdp.data.DealsVerifyResponse
import com.tokopedia.deals.pdp.data.ItemMap
import com.tokopedia.deals.pdp.data.MetaData
import com.tokopedia.deals.pdp.data.ProductDetailData
import com.tokopedia.deals.pdp.domain.DealsPDPVerifyUseCase
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext

class DealsPDPSelectQuantityViewModel @Inject constructor(
    private val dealsPDPVerifyUseCase: DealsPDPVerifyUseCase,
    private val dispatcher: CoroutineDispatchers,
):
    BaseViewModel(dispatcher.main) {

    var currentQuantity: Int = 1
    private val _inputVerifyState = MutableSharedFlow<DealsVerifyRequest>(Int.ONE)

    val flowVerify: SharedFlow<Result<DealsVerifyResponse>> =
        _inputVerifyState.flatMapConcat {
            flow {
                emit(verifyCheckout(it))
            }.catch {
                emit(Fail(it))
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARED_FLOW_STOP_TIMEOUT_MILLIS),
            replay = Int.ONE
        )

    fun setVerifyRequest(productDetailData: ProductDetailData) {
        _inputVerifyState.tryEmit(mapVerifyRequest(productDetailData))
    }

    private fun mapVerifyRequest(dealsResponse: ProductDetailData): DealsVerifyRequest{
        return DealsVerifyRequest(
            book = true,
            checkout = false,
            cartdata = CartData(
                metadata = MetaData(
                    categoryName = categoryName,
                    totalPrice = currentQuantity * dealsResponse.salesPrice.toIntSafely().toLong(),
                    quantity = currentQuantity,
                    productIds = listOf(dealsResponse.id),
                    productNames = listOf(dealsResponse.displayName),
                    providerIds = listOf(dealsResponse.providerId),
                    itemIds = listOf(dealsResponse.id),
                    itemMaps = listOf(
                        ItemMap(
                            id = dealsResponse.id,
                            name = dealsResponse.displayName,
                            productId = dealsResponse.id,
                            productName = dealsResponse.displayName,
                            providerId = dealsResponse.providerId,
                            categoryId = dealsResponse.categoryId,
                            startTime = getDateMilis(dealsResponse.minStartDate.toIntSafely()),
                            endTime = getDateMilis(dealsResponse.maxEndDate.toIntSafely()),
                            price = dealsResponse.salesPrice.toDouble(),
                            quantity = currentQuantity,
                            totalPrice = currentQuantity * dealsResponse.salesPrice.toIntSafely().toLong(),
                            scheduleTimestamp = dealsResponse.maxEndDate,
                            productImage = dealsResponse.imageWeb,
                            flagID = dealsResponse.customText1
                        )
                    )
                )
            )
        )
    }

    private fun getDateMilis(date: Int): String {
        val dateFormat = SimpleDateFormat(dateFormat, DEFAULT_LOCALE)
        val dateMilis = Date(TimeUnit.SECONDS.toMillis(date.toLong()))
        return dateFormat.format(dateMilis).toString()
    }

    private suspend fun verifyCheckout(dealsVerifyRequest: DealsVerifyRequest) : Result<DealsVerifyResponse> {
        val dealsVerifyResponse = withContext(dispatcher.io) {
            dealsPDPVerifyUseCase.execute(dealsVerifyRequest)
        }

        return Success(dealsVerifyResponse)
    }

    companion object {
        private const val SHARED_FLOW_STOP_TIMEOUT_MILLIS = 5000L
        private const val categoryName = "deal"
        private const val dateFormat = " dd MMM yyyy hh:mm"
        private val DEFAULT_LOCALE = Locale("in", "ID")
    }
}