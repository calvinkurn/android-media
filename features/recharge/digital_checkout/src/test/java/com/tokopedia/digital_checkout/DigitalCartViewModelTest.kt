package com.tokopedia.digital_checkout

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.network.exception.HttpErrorException
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common_digital.atc.DigitalAddToCartUseCase
import com.tokopedia.common_digital.atc.data.response.FintechProduct
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst.SummaryInfo.STRING_KODE_PROMO
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst.SummaryInfo.STRING_SUBTOTAL_TAGIHAN
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData
import com.tokopedia.digital_checkout.data.request.DigitalCheckoutDataParameter
import com.tokopedia.digital_checkout.data.response.CancelVoucherData
import com.tokopedia.digital_checkout.data.response.ResponseCheckout
import com.tokopedia.digital_checkout.data.response.ResponsePatchOtpSuccess
import com.tokopedia.digital_checkout.data.response.getcart.RechargeGetCart
import com.tokopedia.digital_checkout.dummy.DigitalCartDummyData
import com.tokopedia.digital_checkout.dummy.DigitalCartDummyData.getAttributesCheckout
import com.tokopedia.digital_checkout.dummy.DigitalCartDummyData.getDummyGetCartResponse
import com.tokopedia.digital_checkout.presentation.viewmodel.DigitalCartViewModel
import com.tokopedia.digital_checkout.usecase.DigitalCancelVoucherUseCase
import com.tokopedia.digital_checkout.usecase.DigitalCheckoutUseCase
import com.tokopedia.digital_checkout.usecase.DigitalGetCartUseCase
import com.tokopedia.digital_checkout.usecase.DigitalPatchOtpUseCase
import com.tokopedia.digital_checkout.utils.DeviceUtil
import com.tokopedia.digital_checkout.utils.DigitalCurrencyUtil.getStringIdrFormat
import com.tokopedia.digital_checkout.utils.analytics.DigitalAnalytics
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.exception.ResponseDataNullException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertNull
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author by jessica on 25/01/21
 */

@RunWith(JUnit4::class)
class DigitalCartViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var digitalCartViewModel: DigitalCartViewModel

    @RelaxedMockK
    lateinit var digitalAnalytics: DigitalAnalytics

    @RelaxedMockK
    lateinit var digitalGetCartUseCase: DigitalGetCartUseCase

    @RelaxedMockK
    lateinit var digitalAddToCartUseCase: DigitalAddToCartUseCase

    @RelaxedMockK
    lateinit var digitalCancelVoucherUseCase: DigitalCancelVoucherUseCase

    @RelaxedMockK
    lateinit var digitalPatchOtpUseCase: DigitalPatchOtpUseCase

    @RelaxedMockK
    lateinit var digitalCheckoutUseCase: DigitalCheckoutUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        digitalCartViewModel = DigitalCartViewModel(digitalAnalytics,
                digitalGetCartUseCase,
                digitalCancelVoucherUseCase, digitalPatchOtpUseCase,
                digitalCheckoutUseCase, userSession, Dispatchers.Unconfined)
    }

    @Test
    fun getCart_onSuccess_NoNeedOtpAndIsNotSubscribed() {
        //given
        val dummyResponse = DigitalCartDummyData.getDummyGetCartResponseWithDefaultCrossSellType()
        coEvery { digitalGetCartUseCase.execute(any(), any(), any()) } coAnswers {
            secondArg<(RechargeGetCart.Response) -> Unit>().invoke(RechargeGetCart.Response(dummyResponse))
        }
        coEvery { userSession.isLoggedIn } returns true

        //when
        val categoryId = "1"
        digitalCartViewModel.getCart(categoryId)

        // then
        // must show content and show loading
        assert(digitalCartViewModel.showContentCheckout.value != null)
        assert(digitalCartViewModel.showContentCheckout.value!!)
        assert(digitalCartViewModel.showLoading.value != null)
        assert(!digitalCartViewModel.showLoading.value!!)

        // show mapped cart data
        val mappedCartInfoData = digitalCartViewModel.cartDigitalInfoData.value
        assert(mappedCartInfoData != null)
        assert(mappedCartInfoData!!.mainInfo.getOrNull(0)?.label == dummyResponse.mainnInfo.getOrNull(0)?.label)
        assert(mappedCartInfoData.mainInfo.getOrNull(0)?.value == dummyResponse.mainnInfo.getOrNull(0)?.value)
        assert(mappedCartInfoData.additionalInfos.getOrNull(0)?.title == dummyResponse.additionalInfo.getOrNull(0)?.title)
        assert(mappedCartInfoData.additionalInfos.getOrNull(0)?.items?.getOrNull(0)?.label
                == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.label)
        assert(mappedCartInfoData.additionalInfos.getOrNull(0)?.items?.getOrNull(0)?.value
                == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.value)
        assert(mappedCartInfoData.attributes.categoryName == dummyResponse.categoryName)
        assert(mappedCartInfoData.attributes.operatorName == dummyResponse.operatorName)
        assert(mappedCartInfoData.attributes.clientNumber == dummyResponse.clientNumber)
        assert(mappedCartInfoData.attributes.icon == dummyResponse.icon)
        assert(mappedCartInfoData.attributes.isInstantCheckout == dummyResponse.isInstantCheckout)
        assert(mappedCartInfoData.attributes.price == dummyResponse.priceText)
        assert(mappedCartInfoData.attributes.pricePlain == dummyResponse.price)
        assert(mappedCartInfoData.attributes.isEnableVoucher == dummyResponse.enableVoucher)
        assert(mappedCartInfoData.attributes.isCouponActive == 1)
        assert(mappedCartInfoData.attributes.voucherAutoCode == dummyResponse.voucher)
        assert(!mappedCartInfoData.isNeedOtp)
        assert(mappedCartInfoData.crossSellingType == dummyResponse.crossSellingType)
        assert(mappedCartInfoData.crossSellingConfig.headerTitle == dummyResponse.crossSellingConfig.wording.headerTitle)
        assert(mappedCartInfoData.attributes.fintechProduct.getOrNull(0)?.transactionType == dummyResponse.fintechProduct.getOrNull(0)?.transactionType)
        assert(mappedCartInfoData.attributes.fintechProduct.getOrNull(0)?.fintechAmount ==
                dummyResponse.fintechProduct.getOrNull(0)?.fintechAmount)
        assert(mappedCartInfoData.id == dummyResponse.id)
        assert(mappedCartInfoData.isInstantCheckout == dummyResponse.isInstantCheckout)

        // show correct total price
        assert(digitalCartViewModel.totalPrice.value != null)
        assert(digitalCartViewModel.totalPrice.value == dummyResponse.price)
    }

    @Test
    fun getCart_onSuccess_NeedOtp() {
        //given
        val dummyResponse = DigitalCartDummyData.getDummyGetCartResponseWithRequiredOtp()
        coEvery { digitalGetCartUseCase.execute(any(), any(), any()) } coAnswers {
            secondArg<(RechargeGetCart.Response) -> Unit>().invoke(RechargeGetCart.Response(dummyResponse))
        }
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.phoneNumber } returns "number"

        //when
        val categoryId = "1"
        digitalCartViewModel.getCart(categoryId)

        //then
        assert(digitalCartViewModel.isNeedOtp.value == userSession.phoneNumber)
    }

    @Test
    fun getCart_onSuccess_isNotLogIn() {
        //given
        coEvery { userSession.isLoggedIn } returns false

        //when
        val categoryId = "1"
        val userNotLoginMessage = "user not login"
        digitalCartViewModel.getCart(categoryId, userNotLoginMessage)

        //then
        assert(digitalCartViewModel.errorThrowable.value!!.message == userNotLoginMessage)
    }

    @Test
    fun getCart_onSuccess_NoNeedOtpAndIsSubscribed() {
        //given
        val dummyResponse = DigitalCartDummyData.getDummyGetCartResponse()
        coEvery { digitalGetCartUseCase.execute(any(), any(), any()) } coAnswers {
            secondArg<(RechargeGetCart.Response) -> Unit>().invoke(RechargeGetCart.Response(dummyResponse))
        }
        coEvery { userSession.isLoggedIn } returns true

        //when
        val categoryId = "1"
        digitalCartViewModel.getCart(categoryId)

        // then
        // must show content and show loading
        assert(digitalCartViewModel.showContentCheckout.value != null)
        assert(digitalCartViewModel.showContentCheckout.value!!)
        assert(digitalCartViewModel.showLoading.value != null)
        assert(!digitalCartViewModel.showLoading.value!!)

        // show mapped cart data
        val mappedCartInfoData = digitalCartViewModel.cartDigitalInfoData.value
        assert(mappedCartInfoData != null)
        assert(mappedCartInfoData!!.mainInfo.getOrNull(0)?.label == dummyResponse.mainnInfo.getOrNull(0)?.label)
        assert(mappedCartInfoData.mainInfo.getOrNull(0)?.value == dummyResponse.mainnInfo.getOrNull(0)?.value)
        assert(mappedCartInfoData.additionalInfos.getOrNull(0)?.title == dummyResponse.additionalInfo.getOrNull(0)?.title)
        assert(mappedCartInfoData.additionalInfos.getOrNull(0)?.items?.getOrNull(0)?.label
                == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.label)
        assert(mappedCartInfoData.additionalInfos.getOrNull(0)?.items?.getOrNull(0)?.value
                == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.value)
        assert(mappedCartInfoData.attributes.categoryName == dummyResponse.categoryName)
        assert(mappedCartInfoData.attributes.operatorName == dummyResponse.operatorName)
        assert(mappedCartInfoData.attributes.clientNumber == dummyResponse.clientNumber)
        assert(mappedCartInfoData.attributes.icon == dummyResponse.icon)
        assert(mappedCartInfoData.attributes.isInstantCheckout == dummyResponse.isInstantCheckout)
        assert(mappedCartInfoData.attributes.price == dummyResponse.priceText)
        assert(mappedCartInfoData.attributes.pricePlain == dummyResponse.price)
        assert(mappedCartInfoData.attributes.isEnableVoucher == dummyResponse.enableVoucher)
        assert(mappedCartInfoData.attributes.isCouponActive == 1)
        assert(mappedCartInfoData.attributes.voucherAutoCode == dummyResponse.voucher)
        assert(!mappedCartInfoData.isNeedOtp)
        assert(mappedCartInfoData.crossSellingType == dummyResponse.crossSellingType)
        assert(mappedCartInfoData.crossSellingConfig?.headerTitle == dummyResponse.crossSellingConfig.wordingIsSubscribe.headerTitle)
        assert(mappedCartInfoData.attributes?.fintechProduct?.getOrNull(0)?.transactionType == dummyResponse.fintechProduct.getOrNull(0)?.transactionType)
        assert(mappedCartInfoData.attributes?.fintechProduct?.getOrNull(0)?.fintechAmount ==
                dummyResponse.fintechProduct.getOrNull(0)?.fintechAmount)
        assert(mappedCartInfoData.id == dummyResponse.id)
        assert(mappedCartInfoData.isInstantCheckout == dummyResponse.isInstantCheckout)

        // show correct total price
        assert(digitalCartViewModel.totalPrice.value != null)
        assert(digitalCartViewModel.totalPrice.value == dummyResponse.price)
    }


    @Test
    fun getCart_onFailed_unknownHostException() {
        //given
        coEvery { digitalGetCartUseCase.execute(any(), any(), any()) } coAnswers {
            thirdArg<(Throwable) -> Unit>().invoke(UnknownHostException())
        }
        coEvery { userSession.isLoggedIn } returns true

        //when
        val categoryId = "1"
        digitalCartViewModel.getCart(categoryId)

        //then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorThrowable.value is UnknownHostException)
    }

    @Test
    fun getCart_onFailed_socketTimeoutException() {
        //given
        coEvery { digitalGetCartUseCase.execute(any(), any(), any()) } coAnswers {
            thirdArg<(Throwable) -> Unit>().invoke(SocketTimeoutException())
        }
        coEvery { userSession.isLoggedIn } returns true

        //when
        val categoryId = "1"
        digitalCartViewModel.getCart(categoryId)

        //then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorThrowable.value is SocketTimeoutException)
    }

    @Test
    fun getCart_onFailed_responseErrorException() {
        //given
        val errorMessage = "ini error"
        coEvery { digitalGetCartUseCase.execute(any(), any(), any()) } coAnswers {
            thirdArg<(Throwable) -> Unit>().invoke(ResponseErrorException(errorMessage))
        }
        coEvery { userSession.isLoggedIn } returns true

        //when
        val categoryId = "1"
        digitalCartViewModel.getCart(categoryId)

        //then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorThrowable.value is ResponseErrorException)
    }

    @Test
    fun getCart_onFailed_responseDataNullException() {
        //given
        val errorMessage = "ini error"
        coEvery { digitalGetCartUseCase.execute(any(), any(), any()) } coAnswers {
            thirdArg<(Throwable) -> Unit>().invoke(ResponseDataNullException(errorMessage))
        }
        coEvery { userSession.isLoggedIn } returns true

        //when
        val categoryId = "1"
        digitalCartViewModel.getCart(categoryId)

        //then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorThrowable.value is ResponseDataNullException)
    }

    @Test
    fun getCart_onFailed_httpErrorException() {
        //given
        val exception = HttpErrorException(504)
        coEvery { digitalGetCartUseCase.execute(any(), any(), any()) } coAnswers {
            thirdArg<(Throwable) -> Unit>().invoke(exception)
        }
        coEvery { userSession.isLoggedIn } returns true

        //when
        val categoryId = "1"
        digitalCartViewModel.getCart(categoryId)

        //then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorThrowable.value is HttpErrorException)
        assert((digitalCartViewModel.errorThrowable.value as HttpErrorException).errorCode == 504)
    }

    @Test
    fun getCart_onFailed_throwable() {
        //given
        val errorMessage = "ini error message"
        coEvery { digitalGetCartUseCase.execute(any(), any(), any()) } coAnswers {
            thirdArg<(Throwable) -> Unit>().invoke(Throwable(errorMessage))
        }
        coEvery { userSession.isLoggedIn } returns true

        //when
        val categoryId = "1"
        digitalCartViewModel.getCart(categoryId)

        //then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorThrowable.value!!.message == errorMessage)
    }

    @Test
    fun onCancelVoucher_onSuccess_dataIsSuccess() {
        // given
        val cancelVoucherData = CancelVoucherData(success = true)
        coEvery { digitalCancelVoucherUseCase.execute(any(), any()) } coAnswers {
            firstArg<(CancelVoucherData.Response) -> Unit>().invoke(CancelVoucherData.Response(cancelVoucherData))
        }

        // when
        digitalCartViewModel.cancelVoucherCart()

        // then
        assert(digitalCartViewModel.isSuccessCancelVoucherCart.value is Success)
        assert((digitalCartViewModel.isSuccessCancelVoucherCart.value as Success).data)
    }

    @Test
    fun onCancelVoucher_onSuccess_dataIsFailure() {
        // given
        val cancelVoucherData = CancelVoucherData(success = false)
        coEvery { digitalCancelVoucherUseCase.execute(any(), any()) } coAnswers {
            firstArg<(CancelVoucherData.Response) -> Unit>().invoke(CancelVoucherData.Response(cancelVoucherData))
        }

        // when
        digitalCartViewModel.cancelVoucherCart()

        // then
        assert(digitalCartViewModel.isSuccessCancelVoucherCart.value is Fail)
    }

    @Test
    fun onCancelVoucher_onFailed() {
        // given
        coEvery { digitalCancelVoucherUseCase.execute(any(), any()) } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }

        // when
        digitalCartViewModel.cancelVoucherCart()

        // then
        assert(digitalCartViewModel.isSuccessCancelVoucherCart.value is Fail)
    }

    @Test
    fun onPatchOtp_onSuccess() {
        // given
        val dummyResponse = ResponsePatchOtpSuccess(true)
        val dataResponse = DataResponse<ResponsePatchOtpSuccess>()
        dataResponse.data = dummyResponse

        val token = object : TypeToken<DataResponse<ResponsePatchOtpSuccess>>() {}.type
        val response = RestResponse(dataResponse, 200, false)
        val responseMap = mapOf<Type, RestResponse>(token to response)

        coEvery { digitalPatchOtpUseCase.executeOnBackground() } returns responseMap
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        digitalCartViewModel.processPatchOtpCart(RequestBodyIdentifier(), DigitalCheckoutPassData(), "")

        // then get cart
        // show loading and hide content
        assert(digitalCartViewModel.showContentCheckout.value != null)
        assert(!digitalCartViewModel.showContentCheckout.value!!)
        assert(digitalCartViewModel.showLoading.value != null)
    }

    @Test
    fun onPatchOtp_onFailed() {
        // given
        coEvery { digitalPatchOtpUseCase.executeOnBackground() } throws IOException("error")
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        digitalCartViewModel.processPatchOtpCart(RequestBodyIdentifier(), DigitalCheckoutPassData())

        // then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorThrowable.value is IOException)
    }

    @Test
    fun onCheckout_onSuccess() {
        // given
        val dummyResponse = ResponseCheckout(
                type = "null",
                id = "123",
                attributes = getAttributesCheckout()
        )
        val dataResponse = DataResponse<ResponseCheckout>()
        dataResponse.data = dummyResponse

        val token = object : TypeToken<DataResponse<ResponseCheckout>>() {}.type
        val response = RestResponse(dataResponse, 200, false)
        val responseMap = mapOf<Type, RestResponse>(token to response)

        coEvery { digitalCheckoutUseCase.executeOnBackground() } returns responseMap
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.proceedToCheckout(RequestBodyIdentifier())

        // then
        val paymentPassDataValue = digitalCartViewModel.paymentPassData.value
        assert(paymentPassDataValue != null)
        assert(paymentPassDataValue!!.callbackFailedUrl == dummyResponse.attributes.callbackUrlFailed)
        assert(paymentPassDataValue.callbackSuccessUrl == dummyResponse.attributes.callbackUrlSuccess)
        assert(paymentPassDataValue.redirectUrl == dummyResponse.attributes.redirectUrl)
        assert(paymentPassDataValue.queryString == dummyResponse.attributes.queryString)
        assert(paymentPassDataValue.transactionId == dummyResponse.attributes.parameter?.transactionId)
    }

    @Test
    fun onCheckout_onFailed() {
        // given
        coEvery { digitalCheckoutUseCase.executeOnBackground() } throws IOException("error")
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.proceedToCheckout(RequestBodyIdentifier())

        // then
        val paymentPassDataValue = digitalCartViewModel.paymentPassData.value
        assert(paymentPassDataValue == null)
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorThrowable.value is IOException)
    }

    @Test
    fun onCheckout_needOtp() {
        // given
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()

        digitalCartViewModel.requestCheckoutParam = DigitalCheckoutDataParameter(isNeedOtp = true)
        digitalCartViewModel.proceedToCheckout(RequestBodyIdentifier())

        // then
        assert(digitalCartViewModel.isNeedOtp.value != null)
    }

    @Test
    fun onRecievedPromoCode_notUpdateTotalPayment() {
        // given
        val promoData = PromoData()

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.setPromoData(promoData)
        digitalCartViewModel.applyPromoData(promoData)

        // then
        // if amount == 0, then expected if total price not updated and no changes on additional info
        assert(digitalCartViewModel.promoData.value?.amount == 0)
        assert(digitalCartViewModel.promoData.value?.promoCode == "")
        assert(digitalCartViewModel.totalPrice.value == getDummyGetCartResponse().price)
    }


    @Test
    fun onResetVoucherCart_addOnAdditionalInfoAndUpdateTotalPayment() {
        // given
        val promoData = PromoData()
        promoData.amount = 12000
        promoData.promoCode = "dummyPromoCode"
        promoData.state = TickerCheckoutView.State.ACTIVE

        // when
        getCart_onSuccess_NoNeedOtpAndIsNotSubscribed()
        digitalCartViewModel.setPromoData(promoData)
        digitalCartViewModel.applyPromoData(promoData)
        digitalCartViewModel.resetCheckoutSummaryPromoAndTotalPrice()

        // then
        assert(digitalCartViewModel.totalPrice.value == getDummyGetCartResponse().price)
    }

    @Test
    fun onApplyDiscountPromoCode_updateCheckoutSummary() {
        // given
        val promoData = PromoData()
        promoData.amount = 12000
        promoData.promoCode = "dummyPromoCode"
        promoData.state = TickerCheckoutView.State.ACTIVE

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.setPromoData(promoData)
        digitalCartViewModel.applyPromoData(promoData)

        val summary = digitalCartViewModel.payment.value!!.summaries.firstOrNull() {
            it.title == STRING_KODE_PROMO}
        assert(summary?.priceAmount == String.format("-%s", getStringIdrFormat(promoData.amount.toDouble())))
    }

    @Test
    fun onApplyNonDiscountPromoCode_notUpdateCheckoutSummary() {
        // given
        val promoData = PromoData()
        promoData.state = TickerCheckoutView.State.ACTIVE

        // when
        getCart_onSuccess_NoNeedOtpAndIsNotSubscribed()
        digitalCartViewModel.setPromoData(promoData)
        digitalCartViewModel.applyPromoData(promoData)

            val summary = digitalCartViewModel.payment.value!!.summaries.firstOrNull() {
            it.title == STRING_KODE_PROMO}
        assertNull(summary)
    }

    @Test
    fun onDiscardPromoCode_updateCheckoutSummary() {
        // given
        val promoData1 = PromoData()
        promoData1.amount = 12000
        promoData1.promoCode = "dummyPromoCode"
        promoData1.state = TickerCheckoutView.State.ACTIVE

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.setPromoData(promoData1)
        digitalCartViewModel.applyPromoData(promoData1)
        digitalCartViewModel.resetCheckoutSummaryPromoAndTotalPrice()

        val summary = digitalCartViewModel.payment.value!!.summaries.firstOrNull() {
            it.title == STRING_KODE_PROMO}
        assertNull(summary)
    }

    @Test
    fun onCheckedFintechProduct_updateCheckoutSummary() {
        // given
        val fintechInfo = FintechProduct.FintechProductInfo(title = "fintech A")
        val fintechProduct = FintechProduct(tierId = "3", fintechAmount = 2000.0, info = fintechInfo, transactionType = "Pulsa")

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.onFintechProductChecked(fintechProduct, true, null)

        // then
        val fintechPrice = digitalCartViewModel.requestCheckoutParam.fintechProducts["3"]?.fintechAmount
                ?: 0.0
        val fintechName = digitalCartViewModel.requestCheckoutParam.fintechProducts["3"]?.transactionType
        val summary = digitalCartViewModel.payment.value!!.summaries.firstOrNull {
            it.title == fintechName
        }

        assert(summary?.priceAmount == getStringIdrFormat(fintechPrice))
    }

    @Test
    fun onUncheckedFintechProduct_updateCheckoutSummary() {
        // given
        val fintechProduct = FintechProduct(tierId = "3", fintechAmount = 2000.0)

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.updateCheckoutSummaryWithFintechProduct(fintechProduct, true)
        digitalCartViewModel.updateCheckoutSummaryWithFintechProduct(fintechProduct, false)

        // then
        val fintechName = digitalCartViewModel.cartDigitalInfoData.value?.attributes?.fintechProduct?.getOrNull(0)?.info?.title
        val summary = digitalCartViewModel.payment.value!!.summaries.firstOrNull {
            it.title == fintechName
        }

        assertNull(summary)
        assert(digitalCartViewModel.requestCheckoutParam.fintechProducts.isEmpty())
    }

    @Test
    fun onInputPrice_UpdateCheckoutSummary() {
        // given
        val userInputPrice = 30000.0

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.setSubtotalPaymentSummaryOnUserInput(userInputPrice)

        // then
        val summary = digitalCartViewModel.payment.value!!.summaries.firstOrNull {
            it.title == STRING_SUBTOTAL_TAGIHAN
        }

        assert(summary?.priceAmount == getStringIdrFormat(userInputPrice))
    }

    @Test
    fun updateTotalPriceWithFintechProduct_checked() {
        // given
        val fintechProduct = FintechProduct(tierId = "3", fintechAmount = 2000.0)

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.onFintechProductChecked(fintechProduct, true, null)

        // then
        // if fintech product checked, update total price
        val oldTotalPrice = digitalCartViewModel.cartDigitalInfoData.value?.attributes?.pricePlain
                ?: 0.0
        val fintechPrice = digitalCartViewModel.requestCheckoutParam.fintechProducts["3"]?.fintechAmount
                ?: 0.0
        assert(digitalCartViewModel.totalPrice.value == oldTotalPrice + fintechPrice)
    }

    @Test
    fun updateTotalPriceWithFintechProduct_unChecked() {
        // given
        val fintechProduct = FintechProduct(tierId = "3", fintechAmount = 2000.0)

        // when
        updateTotalPriceWithFintechProduct_checked()
        digitalCartViewModel.onFintechProductChecked(fintechProduct, false, null)

        // then
        val oldTotalPrice = digitalCartViewModel.cartDigitalInfoData.value?.attributes?.pricePlain ?: 0
        assert(digitalCartViewModel.requestCheckoutParam.fintechProducts.isEmpty())
        assert(digitalCartViewModel.totalPrice.value == oldTotalPrice)
    }

    @Test
    fun updateTotalPriceWithFintechProductAndInputPrice_checked() {
        // given
        val fintechProduct = FintechProduct(tierId = "3", fintechAmount = 2000.0)
        val userInputPrice = 30000.0

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.onFintechProductChecked(fintechProduct, true, userInputPrice)

        // then
        // if fintech product checked, update total price
        val fintechPrice = digitalCartViewModel.requestCheckoutParam.fintechProducts["3"]?.fintechAmount
                ?: 0.0
        assert(digitalCartViewModel.totalPrice.value == userInputPrice + fintechPrice)
    }

    @Test
    fun updateTotalPriceWithFintechProductAndInputPrice_unChecked() {
        // given
        val fintechProduct = FintechProduct(tierId = "3", fintechAmount = 2000.0)
        val userInputPrice = 30000.0

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.onFintechProductChecked(fintechProduct, false, userInputPrice)
        digitalCartViewModel.onSubscriptionChecked(true)

        // then
        // if fintech product checked, update total price
        assert(digitalCartViewModel.totalPrice.value == userInputPrice)
    }

    @Test
    fun setTotalPrice_afterUserInputNumber() {
        // given
        val userInput = 100000.0

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.setTotalPriceBasedOnUserInput(userInput)

        // then
        assert(digitalCartViewModel.totalPrice.value == userInput)
    }

    @Test
    fun setSubtotalPayment_afterUserInputNumber() {
        // given
        val userInput = 100000.0

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.setSubtotalPaymentSummaryOnUserInput(userInput)

        // then
        val summary = digitalCartViewModel.payment.value!!.summaries.first {
            it.title == DigitalCheckoutConst.SummaryInfo.STRING_SUBTOTAL_TAGIHAN }
        assert(summary.priceAmount == getStringIdrFormat(userInput))
    }

    @Test
    fun buildCheckOutData_fromGetCart() {
        // given
        val accessTokenDummy = "dummy"
        coEvery { userSession.accessToken } returns accessTokenDummy

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()

        // then
        val cartInfoData = digitalCartViewModel.cartDigitalInfoData.value ?: CartDigitalInfoData()

        digitalCartViewModel.requestCheckoutParam.let {
            assert(it.cartId == cartInfoData.id)
            assert(it.accessToken == accessTokenDummy)
            assert(it.walletRefreshToken.isEmpty())
            assert(it.ipAddress == DeviceUtil.localIpAddress)
            assert(it.relationId == cartInfoData.id)
            assert(it.relationType == cartInfoData.type)
            assert(it.transactionAmount == cartInfoData.attributes?.pricePlain)
            assert(it.userAgent == DeviceUtil.userAgentForApiCall)
            assert(it.isNeedOtp == cartInfoData.isNeedOtp)
        }

    }

    @Test
    fun getPromoDigitalModel_withUserInputPrice_shouldReturnCorrectData() {
        //given
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = "1"
        digitalCheckoutPassData.productId = "2"
        digitalCheckoutPassData.clientNumber = "AAA123"

        //when
        getCart_onSuccess_NoNeedOtpAndIsNotSubscribed()
        val promoDigitalModel = digitalCartViewModel.getPromoDigitalModel(digitalCheckoutPassData, 1000.0)

        //then
        assert(promoDigitalModel.categoryId.toString() == digitalCheckoutPassData.categoryId ?: "")
        assert(promoDigitalModel.productId.toString() == digitalCheckoutPassData.productId ?: "")
        assert(promoDigitalModel.clientNumber == digitalCheckoutPassData.clientNumber)
        assert(promoDigitalModel.categoryName == "Angsuran Kredit")
        assert(promoDigitalModel.operatorName == "JTrust Olympindo Multi Finance")
        assert(promoDigitalModel.price == 1000L)
    }

    @Test
    fun getPromoDigitalModel_withoutUserInputPrice_shouldReturnCorrectData() {
        //given
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = "1"
        digitalCheckoutPassData.productId = "2"
        digitalCheckoutPassData.clientNumber = "AAA123"

        //when
        getCart_onSuccess_NoNeedOtpAndIsNotSubscribed()
        val promoDigitalModel = digitalCartViewModel.getPromoDigitalModel(digitalCheckoutPassData, null)

        assert(promoDigitalModel.categoryId.toString() == digitalCheckoutPassData.categoryId ?: "")
        assert(promoDigitalModel.productId.toString() == digitalCheckoutPassData.productId ?: "")
        assert(promoDigitalModel.clientNumber == digitalCheckoutPassData.clientNumber)
        assert(promoDigitalModel.categoryName == "Angsuran Kredit")
        assert(promoDigitalModel.operatorName == "JTrust Olympindo Multi Finance")
        assert(promoDigitalModel.price == 12500L)
    }
}