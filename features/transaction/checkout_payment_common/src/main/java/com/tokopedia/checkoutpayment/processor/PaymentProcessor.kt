package com.tokopedia.checkoutpayment.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkoutpayment.data.CreditCardTenorListRequest
import com.tokopedia.checkoutpayment.data.GetPaymentWidgetRequest
import com.tokopedia.checkoutpayment.data.GetPaymentWidgetResponse
import com.tokopedia.checkoutpayment.data.GoCicilInstallmentRequest
import com.tokopedia.checkoutpayment.data.PaymentFeeRequest
import com.tokopedia.checkoutpayment.domain.CreditCardTenorListUseCase
import com.tokopedia.checkoutpayment.domain.DynamicPaymentFeeUseCase
import com.tokopedia.checkoutpayment.domain.GetPaymentWidgetUseCase
import com.tokopedia.checkoutpayment.domain.GoCicilInstallmentData
import com.tokopedia.checkoutpayment.domain.GoCicilInstallmentOptionUseCase
import com.tokopedia.checkoutpayment.domain.TenorListData
import com.tokopedia.checkoutpayment.view.OrderPaymentFee
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class PaymentProcessor @Inject constructor(
    private val creditCardTenorListUseCase: CreditCardTenorListUseCase,
    private val goCicilInstallmentOptionUseCase: GoCicilInstallmentOptionUseCase,
    private val dynamicPaymentFeeUseCase: DynamicPaymentFeeUseCase,
    private val getPaymentWidgetUseCase: GetPaymentWidgetUseCase,
    private val dispatchers: CoroutineDispatchers
) {

    suspend fun getCreditCardAdminFee(param: CreditCardTenorListRequest): List<TenorListData>? {
        return withContext(dispatchers.io) {
            try {
                val creditCardData = creditCardTenorListUseCase(param)
                if (creditCardData.errorMsg.isNotEmpty()) {
                    return@withContext null
                }
                return@withContext creditCardData.tenorList
            } catch (e: Exception) {
                Timber.d(e)
                return@withContext null
            }
        }
    }

    suspend fun getGocicilInstallmentOption(param: GoCicilInstallmentRequest): GoCicilInstallmentData? {
        return withContext(dispatchers.io) {
            try {
                return@withContext goCicilInstallmentOptionUseCase(param)
            } catch (e: Exception) {
                Timber.d(e)
                return@withContext null
            }
        }
    }

    suspend fun getPaymentFee(param: PaymentFeeRequest): List<OrderPaymentFee>? {
        return withContext(dispatchers.io) {
            try {
                return@withContext dynamicPaymentFeeUseCase(param)
            } catch (e: Exception) {
                Timber.d(e)
                return@withContext null
            }
        }
    }
    
    suspend fun getPaymentWidget(param: GetPaymentWidgetRequest): GetPaymentWidgetResponse? {
        return withContext(dispatchers.io) {
            try {
                return@withContext getPaymentWidgetUseCase(param)
            } catch (e: Exception) {
                Timber.d(e)
                return@withContext null
            }
        }
    }
}
