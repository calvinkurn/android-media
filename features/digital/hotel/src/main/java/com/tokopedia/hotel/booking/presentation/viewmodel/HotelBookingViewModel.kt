package com.tokopedia.hotel.booking.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.booking.data.model.CartDataParam
import com.tokopedia.hotel.booking.data.model.HotelCart
import com.tokopedia.hotel.booking.data.model.HotelCheckoutParam
import com.tokopedia.hotel.booking.data.model.HotelCheckoutResponse
import com.tokopedia.hotel.common.getSuccessData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by resakemal on 13/05/19
 */

class HotelBookingViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val hotelCartResult = MutableLiveData<Result<HotelCart>>()
    val hotelCheckoutResult = MutableLiveData<Result<HotelCheckoutResponse>>()

    fun getCartData(rawQuery: String, cartId: String, dummy: String = "") {
        val requestParams = CartDataParam(cartId)
        val params = mapOf(PARAM_CART_PROPERTY to requestParams)
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TYPE_HOTEL_CART, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<HotelCart>()

            hotelCartResult.value = Success(data)
        }) {
            hotelCartResult.value = Fail(it)
//            val gson = Gson()
//            hotelCartResult.value = Success(gson.fromJson(dummy,
//                    HotelCart::class.java))
        }
    }

    fun checkoutCart(rawQuery: String, hotelCheckoutParam: HotelCheckoutParam) {
        val params = mapOf(PARAM_CART_PROPERTY to hotelCheckoutParam)
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TYPE_HOTEL_CHECKOUT, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<HotelCheckoutResponse>()

            hotelCheckoutResult.value = Success(data)
        }) {
            it.printStackTrace()
            val response = HotelCheckoutResponse(
                queryString = "DirectDebitBankID=&DirectDebitData=&ValidationType=&amount=10000&back_url=https%3A%2F%2Fstaging.tokopedia.com%2Fflight%2Fsummary%3Fid%3D8939501ae95b14d7fed4d7771f7727f2086a7fa¤cy=IDR&customer_email=marshall.harry%40tokopedia.com&customer_msisdn=6281314112287&customer_name=marshall&expired_on=&fee=&items%5Bname%5D=Tiket+Pesawat&items%5Bname%5D=Penggunaan+Voucher+Tokopedia&items%5Bprice%5D=15000&items%5Bprice%5D=-5000&items%5Bquantity%5D=1&items%5Bquantity%5D=1&language=id-ID&merchant_code=tokopediaflight&nid=10000000098&payment_metadata=%7B%22user_data%22%3A%7B%22device_id%22%3A%22asd%22%7D%2C%22order_data%22%3A%7B%22order_data%22%3A%5B%7B%22order_id%22%3A%22211124%22%2C%22product_code%22%3A%22tiket_pesawat%22%2C%22invoice_url%22%3A%22https%3A%2F%2Fpulsa-staging.tokopedia.com%2Finvoice%2F%3Fpdf%3DInvoice-Recharge-8939501-666-20190329110354-l7gxUouxjnpi.pdf%5Cu0026id%3D211124%22%2C%22product_data%22%3A%5B%7B%22product_id%22%3A%22666%22%2C%22product_name%22%3A%22Tokopedia+Travel+-+Flight+Flight%22%2C%22price%22%3A15000%2C%22quantity%22%3A1%2C%22campaign_id%22%3A%22%22%2C%22client_number%22%3A%22%22%2C%22identity_number%22%3A%22%22%2C%22is_product_pre_order%22%3Afalse%2C%22product_category%22%3A%7B%22id%22%3A27%2C%22name%22%3A%22Flight%22%2C%22identifier%22%3A%22%22%2C%22parent%22%3A0%7D%7D%5D%2C%22expire_time%22%3A%222019-03-29T04%3A26%3A15Z%22%7D%5D%7D%7D&pid=1065151889407&profile_code=FLIGHT_DEFAULT&signature=4ea93b06a378bd64d4aebf2be6d990ab88143cee&state=0&transaction_code=&transaction_date=2019-03-29T04%3A03%3A54Z&transaction_id=1000107000&user_defined_value=%7B%22user_id%22%3A8939501%2C%22voucher_code%22%3A%22FANNYDISKON%22%2C%22device%22%3A1%2C%22product_id%22%3A666%2C%22price%22%3A15000%2C%22client_number%22%3A%22%22%2C%22payment_description%22%3A%22%22%2C%22promo_code_id%22%3A10000000098%2C%22discount_amount%22%3A5000%2C%22cashback_amount%22%3A0%2C%22cashback_voucher_amount%22%3A0%2C%22cashback_top_cash_amount%22%3A0%2C%22va_code%22%3A%22081314112287%22%2C%22refresh_token%22%3A%22QVZnuVwdSoOPsjj7ay65vg%22%2C%22access_token%22%3A%22cKkGIYHHS_ycRqFh_vXSNw%22%2C%22fingerprint_id%22%3A%22%22%2C%22hide_header_flag%22%3Atrue%2C%22category_code%22%3A%2227%22%2C%22product_code%22%3A%22FLIGHT%22%7D",
                redirectUrl = "https://pay-staging.tokopedia.com/v2/payment"
            )
            hotelCheckoutResult.value = Success(response)
        }
    }

    companion object {
        const val PARAM_CART_PROPERTY = "data"
        private val TYPE_HOTEL_CART = HotelCart::class.java
        private val TYPE_HOTEL_CHECKOUT = HotelCheckoutResponse::class.java
    }
}