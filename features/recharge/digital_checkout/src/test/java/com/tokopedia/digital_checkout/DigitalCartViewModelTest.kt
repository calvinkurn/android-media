package com.tokopedia.digital_checkout

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.network.exception.HttpErrorException
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData
import com.tokopedia.digital_checkout.data.response.CancelVoucherData
import com.tokopedia.digital_checkout.data.response.ResponseCheckout
import com.tokopedia.digital_checkout.data.response.ResponsePatchOtpSuccess
import com.tokopedia.digital_checkout.data.response.atc.DigitalSubscriptionParams
import com.tokopedia.digital_checkout.data.response.atc.ResponseCartData
import com.tokopedia.digital_checkout.data.response.getcart.RechargeGetCart
import com.tokopedia.digital_checkout.dummy.DigitalCartDummyData
import com.tokopedia.digital_checkout.dummy.DigitalCartDummyData.getAttributesCheckout
import com.tokopedia.digital_checkout.dummy.DigitalCartDummyData.getDummyCartData
import com.tokopedia.digital_checkout.presentation.viewmodel.DigitalCartViewModel
import com.tokopedia.digital_checkout.usecase.*
import com.tokopedia.digital_checkout.utils.DeviceUtil
import com.tokopedia.digital_checkout.utils.analytics.DigitalAnalytics
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.exception.ResponseDataNullException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import rx.Subscriber
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
                digitalGetCartUseCase, digitalAddToCartUseCase,
                digitalCancelVoucherUseCase, digitalPatchOtpUseCase,
                digitalCheckoutUseCase, userSession, Dispatchers.Unconfined)
    }

    @Test
    fun getCart_onSuccess_NoNeedOtpAndIsNotSubscribed() {
        //given
        val dummyResponse = DigitalCartDummyData.getDummyGetCartResponse()
        dummyResponse.crossSellingType = 0
        coEvery { digitalGetCartUseCase.execute(any(), any(), any()) } coAnswers {
            secondArg<(RechargeGetCart.Response) -> Unit>().invoke(RechargeGetCart.Response(dummyResponse))
        }
        coEvery { userSession.isLoggedIn } returns true

        //when
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = "1"
        digitalCartViewModel.getCart(digitalCheckoutPassData)

        // then
        // must show content and show loading
        assert(digitalCartViewModel.showContentCheckout.value != null)
        assert(digitalCartViewModel.showContentCheckout.value!!)
        assert(digitalCartViewModel.showLoading.value != null)
        assert(!digitalCartViewModel.showLoading.value!!)

        // show mapped cart data
        val mappedCartInfoData = digitalCartViewModel.cartDigitalInfoData.value
        assert(mappedCartInfoData != null)
        assert(mappedCartInfoData!!.mainInfo?.getOrNull(0)?.label == dummyResponse.mainnInfo.getOrNull(0)?.label)
        assert(mappedCartInfoData.mainInfo?.getOrNull(0)?.value == dummyResponse.mainnInfo.getOrNull(0)?.value)
        assert(mappedCartInfoData.additionalInfos?.getOrNull(0)?.title == dummyResponse.additionalInfo.getOrNull(0)?.title)
        assert(mappedCartInfoData.additionalInfos?.getOrNull(0)?.items?.getOrNull(0)?.label
                == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.label)
        assert(mappedCartInfoData.additionalInfos?.getOrNull(0)?.items?.getOrNull(0)?.value
                == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.value)
        assert(mappedCartInfoData.attributes?.categoryName == dummyResponse.categoryName)
        assert(mappedCartInfoData.attributes?.operatorName == dummyResponse.operatorName)
        assert(mappedCartInfoData.attributes?.clientNumber == dummyResponse.clientNumber)
        assert(mappedCartInfoData.attributes?.icon == dummyResponse.icon)
        assert(mappedCartInfoData.attributes?.isInstantCheckout == dummyResponse.isInstantCheckout)
        assert(mappedCartInfoData.attributes?.price == dummyResponse.priceText)
        assert(mappedCartInfoData.attributes?.pricePlain == dummyResponse.price)
        assert(mappedCartInfoData.attributes?.isEnableVoucher == dummyResponse.enableVoucher)
        assert(mappedCartInfoData.attributes?.isCouponActive == 1)
        assert(mappedCartInfoData.attributes?.voucherAutoCode == dummyResponse.voucher)
        assert(!mappedCartInfoData.isNeedOtp)
        assert(mappedCartInfoData.crossSellingType == dummyResponse.crossSellingType)
        assert(mappedCartInfoData.crossSellingConfig?.headerTitle == dummyResponse.crossSellingConfig.wording.headerTitle)
        assert(mappedCartInfoData.attributes?.fintechProduct?.getOrNull(0)?.transactionType == dummyResponse.fintechProduct.getOrNull(0)?.transactionType)
        assert(mappedCartInfoData.attributes?.fintechProduct?.getOrNull(0)?.fintechAmount ==
                dummyResponse.fintechProduct.getOrNull(0)?.fintechAmount)
        assert(mappedCartInfoData.id == dummyResponse.id)
        assert(mappedCartInfoData.isInstantCheckout == dummyResponse.isInstantCheckout)

        // show correct additional info list
        val additionalInfoValue = digitalCartViewModel.cartAdditionalInfoList.value
        assert(additionalInfoValue != null)
        assert(additionalInfoValue?.getOrNull(0)?.title == dummyResponse.additionalInfo.getOrNull(0)?.title)
        assert(additionalInfoValue?.getOrNull(0)?.items?.getOrNull(0)?.label
                == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.label)
        assert(additionalInfoValue?.getOrNull(0)?.items?.getOrNull(0)?.value
                == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.value)

        // show correct total price
        assert(digitalCartViewModel.totalPrice.value != null)
        assert(digitalCartViewModel.totalPrice.value == dummyResponse.price)
    }

    @Test
    fun getCart_onSuccess_NeedOtp() {
        //given
        val dummyResponse = DigitalCartDummyData.getDummyGetCartResponse()
        dummyResponse.isOtpRequired = true
        coEvery { digitalGetCartUseCase.execute(any(), any(), any()) } coAnswers {
            secondArg<(RechargeGetCart.Response) -> Unit>().invoke(RechargeGetCart.Response(dummyResponse))
        }
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.phoneNumber } returns "number"

        //when
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = "1"
        digitalCartViewModel.getCart(digitalCheckoutPassData)

        //then
        assert(digitalCartViewModel.isNeedOtp.value == userSession.phoneNumber)
    }

    @Test
    fun getCart_onSuccess_isNotLogIn() {
        //given
        coEvery { userSession.isLoggedIn } returns false

        //when
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = "1"
        val userNotLoginMessage = "user not login"
        digitalCartViewModel.getCart(digitalCheckoutPassData, userNotLoginMessage)

        //then
        assert(digitalCartViewModel.errorMessage.value == userNotLoginMessage)
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
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = "1"
        digitalCartViewModel.getCart(digitalCheckoutPassData)

        // then
        // must show content and show loading
        assert(digitalCartViewModel.showContentCheckout.value != null)
        assert(digitalCartViewModel.showContentCheckout.value!!)
        assert(digitalCartViewModel.showLoading.value != null)
        assert(!digitalCartViewModel.showLoading.value!!)

        // show mapped cart data
        val mappedCartInfoData = digitalCartViewModel.cartDigitalInfoData.value
        assert(mappedCartInfoData != null)
        assert(mappedCartInfoData!!.mainInfo?.getOrNull(0)?.label == dummyResponse.mainnInfo.getOrNull(0)?.label)
        assert(mappedCartInfoData.mainInfo?.getOrNull(0)?.value == dummyResponse.mainnInfo.getOrNull(0)?.value)
        assert(mappedCartInfoData.additionalInfos?.getOrNull(0)?.title == dummyResponse.additionalInfo.getOrNull(0)?.title)
        assert(mappedCartInfoData.additionalInfos?.getOrNull(0)?.items?.getOrNull(0)?.label
                == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.label)
        assert(mappedCartInfoData.additionalInfos?.getOrNull(0)?.items?.getOrNull(0)?.value
                == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.value)
        assert(mappedCartInfoData.attributes?.categoryName == dummyResponse.categoryName)
        assert(mappedCartInfoData.attributes?.operatorName == dummyResponse.operatorName)
        assert(mappedCartInfoData.attributes?.clientNumber == dummyResponse.clientNumber)
        assert(mappedCartInfoData.attributes?.icon == dummyResponse.icon)
        assert(mappedCartInfoData.attributes?.isInstantCheckout == dummyResponse.isInstantCheckout)
        assert(mappedCartInfoData.attributes?.price == dummyResponse.priceText)
        assert(mappedCartInfoData.attributes?.pricePlain == dummyResponse.price)
        assert(mappedCartInfoData.attributes?.isEnableVoucher == dummyResponse.enableVoucher)
        assert(mappedCartInfoData.attributes?.isCouponActive == 1)
        assert(mappedCartInfoData.attributes?.voucherAutoCode == dummyResponse.voucher)
        assert(!mappedCartInfoData.isNeedOtp)
        assert(mappedCartInfoData.crossSellingType == dummyResponse.crossSellingType)
        assert(mappedCartInfoData.crossSellingConfig?.headerTitle == dummyResponse.crossSellingConfig.wordingIsSubscribe.headerTitle)
        assert(mappedCartInfoData.attributes?.fintechProduct?.getOrNull(0)?.transactionType == dummyResponse.fintechProduct.getOrNull(0)?.transactionType)
        assert(mappedCartInfoData.attributes?.fintechProduct?.getOrNull(0)?.fintechAmount ==
                dummyResponse.fintechProduct.getOrNull(0)?.fintechAmount)
        assert(mappedCartInfoData.id == dummyResponse.id)
        assert(mappedCartInfoData.isInstantCheckout == dummyResponse.isInstantCheckout)

        // show correct additional info list
        val additionalInfoValue = digitalCartViewModel.cartAdditionalInfoList.value
        assert(additionalInfoValue != null)
        assert(additionalInfoValue?.getOrNull(0)?.title == dummyResponse.additionalInfo.getOrNull(0)?.title)
        assert(additionalInfoValue?.getOrNull(0)?.items?.getOrNull(0)?.label
                == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.label)
        assert(additionalInfoValue?.getOrNull(0)?.items?.getOrNull(0)?.value
                == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.value)

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
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = "1"
        digitalCartViewModel.getCart(digitalCheckoutPassData)

        //then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorMessage.value == ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL)
    }

    @Test
    fun getCart_onFailed_socketTimeoutException() {
        //given
        coEvery { digitalGetCartUseCase.execute(any(), any(), any()) } coAnswers {
            thirdArg<(Throwable) -> Unit>().invoke(SocketTimeoutException())
        }
        coEvery { userSession.isLoggedIn } returns true

        //when
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = "1"
        digitalCartViewModel.getCart(digitalCheckoutPassData)

        //then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorMessage.value == ErrorNetMessage.MESSAGE_ERROR_TIMEOUT)
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
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = "1"
        digitalCartViewModel.getCart(digitalCheckoutPassData)

        //then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorMessage.value == errorMessage)
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
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = "1"
        digitalCartViewModel.getCart(digitalCheckoutPassData)

        //then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorMessage.value == errorMessage)
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
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = "1"
        digitalCartViewModel.getCart(digitalCheckoutPassData)

        //then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorMessage.value == ErrorNetMessage.MESSAGE_ERROR_TIMEOUT)
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
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = "1"
        digitalCartViewModel.getCart(digitalCheckoutPassData)

        //then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorMessage.value == ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
    }


    @Test
    fun addToCart_onSuccess() {
        // given
        val dummyResponse = getDummyCartData()
        val dataResponse = DataResponse<ResponseCartData>()
        dataResponse.data = getDummyCartData()

        val token = object : TypeToken<DataResponse<ResponseCartData>>() {}.type
        val response = RestResponse(dataResponse, 200, false)
        val responseMap = mapOf<Type, RestResponse>(token to response)

        every {
            digitalAddToCartUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Map<Type, RestResponse>>>().onStart()
            secondArg<Subscriber<Map<Type, RestResponse>>>().onCompleted()
            secondArg<Subscriber<Map<Type, RestResponse>>>().onNext(responseMap)
        }
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = "1"
        digitalCartViewModel.addToCart(digitalCheckoutPassData,
                RequestBodyIdentifier(), DigitalSubscriptionParams())

        // then
        // must show content and show loading
        assert(digitalCartViewModel.showContentCheckout.value != null)
        assert(digitalCartViewModel.showContentCheckout.value!!)
        assert(digitalCartViewModel.showLoading.value != null)
        assert(!digitalCartViewModel.showLoading.value!!)

        // show mapped cart data
        val mappedCartInfoData = digitalCartViewModel.cartDigitalInfoData.value
        assert(mappedCartInfoData != null)
        assert(mappedCartInfoData!!.mainInfo?.getOrNull(0)?.label
                == dummyResponse.attributes?.mainInfo?.getOrNull(0)?.label)
        assert(mappedCartInfoData.mainInfo?.getOrNull(0)?.value
                == dummyResponse.attributes?.mainInfo?.getOrNull(0)?.value)
        assert(mappedCartInfoData.additionalInfos?.getOrNull(0)?.title
                == dummyResponse.attributes?.additionalInfo?.getOrNull(0)?.title)
        assert(mappedCartInfoData.additionalInfos?.getOrNull(0)?.items?.getOrNull(0)?.label
                == dummyResponse.attributes?.additionalInfo?.getOrNull(0)?.detail?.getOrNull(0)?.label)
        assert(mappedCartInfoData.additionalInfos?.getOrNull(0)?.items?.getOrNull(0)?.value
                == dummyResponse.attributes?.additionalInfo?.getOrNull(0)?.detail?.getOrNull(0)?.value)
        assert(mappedCartInfoData.attributes?.categoryName == dummyResponse.attributes?.categoryName)
        assert(mappedCartInfoData.attributes?.operatorName == dummyResponse.attributes?.operatorName)
        assert(mappedCartInfoData.attributes?.clientNumber == dummyResponse.attributes?.clientNumber)
        assert(mappedCartInfoData.attributes?.icon == dummyResponse.attributes?.icon)
        assert(mappedCartInfoData.attributes?.isInstantCheckout == dummyResponse.attributes?.isInstantCheckout)
        assert(mappedCartInfoData.attributes?.price == dummyResponse.attributes?.price)
        assert(mappedCartInfoData.attributes?.pricePlain == dummyResponse.attributes?.pricePlain)
        assert(mappedCartInfoData.attributes?.isEnableVoucher == dummyResponse.attributes?.isEnableVoucher)
        assert(mappedCartInfoData.attributes?.isCouponActive == 1)
        assert(mappedCartInfoData.attributes?.voucherAutoCode == dummyResponse?.attributes?.voucherAutoCode)
        assert(!mappedCartInfoData.isNeedOtp)
        assert(mappedCartInfoData.crossSellingType == dummyResponse.attributes?.crossSellingType)
        assert(mappedCartInfoData.crossSellingConfig?.headerTitle
                == dummyResponse.attributes?.crossSellingConfig?.wordingIsSubscribed?.headerTitle)
        assert(mappedCartInfoData.attributes?.fintechProduct?.getOrNull(0)?.transactionType
                == dummyResponse.attributes?.fintechProduct?.getOrNull(0)?.transactionType)
        assert(mappedCartInfoData.attributes?.fintechProduct?.getOrNull(0)?.fintechAmount ==
                dummyResponse.attributes?.fintechProduct?.getOrNull(0)?.fintechAmount)
        assert(mappedCartInfoData.id == dummyResponse.id)
        assert(mappedCartInfoData.isInstantCheckout == dummyResponse.attributes?.isInstantCheckout)

        // show correct total price
        assert(digitalCartViewModel.totalPrice.value != null)
        assert(digitalCartViewModel.totalPrice.value == dummyResponse.attributes?.pricePlain)
    }

    @Test
    fun addToCart_onFailed() {
        // given
        every {
            digitalAddToCartUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Map<Type, RestResponse>>>().onStart()
            secondArg<Subscriber<Map<Type, RestResponse>>>().onCompleted()
            secondArg<Subscriber<Map<Type, RestResponse>>>().onError(Throwable())
        }
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = "1"
        digitalCartViewModel.addToCart(digitalCheckoutPassData,
                RequestBodyIdentifier(), DigitalSubscriptionParams())

        // then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorMessage.value == ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
    }

    @Test
    fun addToCart_onUserIsNotLogin() {
        // given
        coEvery { userSession.isLoggedIn } returns false
        coEvery { userSession.userId } returns "123"
        val userNotLoginMessage = "User not login!!"

        // when
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = "1"
        digitalCartViewModel.addToCart(digitalCheckoutPassData,
                RequestBodyIdentifier(), DigitalSubscriptionParams(),
                userNotLoginMessage
        )

        // then
        assert(digitalCartViewModel.errorMessage.value == userNotLoginMessage)
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

        every {
            digitalPatchOtpUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Map<Type, RestResponse>>>().onStart()
            secondArg<Subscriber<Map<Type, RestResponse>>>().onCompleted()
            secondArg<Subscriber<Map<Type, RestResponse>>>().onNext(responseMap)
        }
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        digitalCartViewModel.processPatchOtpCart(RequestBodyIdentifier(), DigitalCheckoutPassData(), "")

        // then get cart
        // show loading and hide content
        assert(digitalCartViewModel.showContentCheckout.value != null)
        assert(!digitalCartViewModel.showContentCheckout.value!!)
        assert(digitalCartViewModel.showLoading.value != null)
        assert(digitalCartViewModel.showLoading.value!!)
    }

    @Test
    fun onPatchOtp_onFailed() {
        // given
        every {
            digitalPatchOtpUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Map<Type, RestResponse>>>().onStart()
            secondArg<Subscriber<Map<Type, RestResponse>>>().onCompleted()
            secondArg<Subscriber<Map<Type, RestResponse>>>().onError(Throwable())
        }
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        digitalCartViewModel.processPatchOtpCart(RequestBodyIdentifier(), DigitalCheckoutPassData())

        // then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorMessage.value == ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
    }

    @Test
    fun onCheckout_onSuccess() {
        // given
        val dummyResponse = ResponseCheckout(
                type = "null",
                id = 123,
                attributes = getAttributesCheckout()
        )
        val dataResponse = DataResponse<ResponseCheckout>()
        dataResponse.data = dummyResponse

        val token = object : TypeToken<DataResponse<ResponseCheckout>>() {}.type
        val response = RestResponse(dataResponse, 200, false)
        val responseMap = mapOf<Type, RestResponse>(token to response)

        every {
            digitalCheckoutUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Map<Type, RestResponse>>>().onStart()
            secondArg<Subscriber<Map<Type, RestResponse>>>().onCompleted()
            secondArg<Subscriber<Map<Type, RestResponse>>>().onNext(responseMap)
        }
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.proceedToCheckout("", RequestBodyIdentifier())

        // then
        val paymentPassDataValue = digitalCartViewModel.paymentPassData.value
        assert(paymentPassDataValue != null)
        assert(paymentPassDataValue!!.callbackFailedUrl == dummyResponse.attributes?.callbackUrlFailed)
        assert(paymentPassDataValue.callbackSuccessUrl == dummyResponse.attributes?.callbackUrlSuccess)
        assert(paymentPassDataValue.redirectUrl == dummyResponse.attributes?.redirectUrl)
        assert(paymentPassDataValue.queryString == dummyResponse.attributes?.queryString)
        assert(paymentPassDataValue.transactionId == dummyResponse.attributes?.parameter?.transactionId)
    }

    @Test
    fun onCheckout_onFailed() {
        // given
        every {
            digitalCheckoutUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Map<Type, RestResponse>>>().onStart()
            secondArg<Subscriber<Map<Type, RestResponse>>>().onCompleted()
            secondArg<Subscriber<Map<Type, RestResponse>>>().onError(Throwable())
        }
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.proceedToCheckout("", RequestBodyIdentifier())

        // then
        val paymentPassDataValue = digitalCartViewModel.paymentPassData.value
        assert(paymentPassDataValue == null)
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorMessage.value == ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
    }

    @Test
    fun onRecievedPromoCode_addOnAdditionalInfoAndUpdateTotalPayment() {
        // given
        val promoData = PromoData()
        promoData.amount = 12000

        // when
        addToCart_onSuccess()
        digitalCartViewModel.onReceivedPromoCode(promoData)

        // then
        // if promo has its amount, then apply promo to total price and add additional info
        assert(digitalCartViewModel.totalPrice.value == getDummyCartData().attributes?.pricePlain ?: 0 - promoData.amount)
        assert(digitalCartViewModel.cartAdditionalInfoList.value?.size == 2)
        assert(digitalCartViewModel.cartAdditionalInfoList.value?.lastOrNull()?.title == "Pembayaran")
    }

    @Test
    fun onRecievedPromoCode_notUpdateTotalPayment() {
        // given
        val promoData = PromoData()

        // when
        addToCart_onSuccess()
        digitalCartViewModel.onReceivedPromoCode(promoData)

        // then
        // if amount == 0, then expected if total price not updated and no changes on additional info
        assert(digitalCartViewModel.totalPrice.value == getDummyCartData().attributes?.pricePlain)
        assert(digitalCartViewModel.cartAdditionalInfoList.value?.size == 1)
    }


    @Test
    fun onResetVoucherCart_addOnAdditionalInfoAndUpdateTotalPayment() {
        // given

        // when
        onRecievedPromoCode_addOnAdditionalInfoAndUpdateTotalPayment()
        digitalCartViewModel.resetVoucherCart()

        // then
        assert(digitalCartViewModel.totalPrice.value == getDummyCartData().attributes?.pricePlain)
        assert(digitalCartViewModel.cartAdditionalInfoList.value?.size == 1)
    }

    @Test
    fun updateTotalPriceWithFintechProduct_checked() {
        // given

        // when
        addToCart_onSuccess()
        digitalCartViewModel.updateTotalPriceWithFintechProduct(true)

        // then
        // if fintech product checked, update total price
        val oldTotalPrice = digitalCartViewModel.cartDigitalInfoData.value?.attributes?.pricePlain ?: 0
        val fintechPrice = digitalCartViewModel.cartDigitalInfoData.value?.attributes?.fintechProduct?.getOrNull(0)?.fintechAmount ?: 0
        assert(digitalCartViewModel.totalPrice.value == oldTotalPrice + fintechPrice)
    }

    @Test
    fun updateTotalPriceWithFintechProduct_unChecked() {
        // given

        // when
        updateTotalPriceWithFintechProduct_checked()
        digitalCartViewModel.updateTotalPriceWithFintechProduct(false)

        // then
        val oldTotalPrice = digitalCartViewModel.cartDigitalInfoData.value?.attributes?.pricePlain ?: 0
        assert(digitalCartViewModel.totalPrice.value == oldTotalPrice)
    }

    @Test
    fun setTotalPrice_afterUserInputNumber() {
        // given
        val userInput = 100000L

        // when
        digitalCartViewModel.setTotalPrice(userInput)

        // then
        assert(digitalCartViewModel.totalPrice.value == userInput)
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
            assert(it.walletRefreshToken?.isEmpty() ?: false)
            assert(it.ipAddress == DeviceUtil.localIpAddress)
            assert(it.relationId == cartInfoData.id)
            assert(it.relationType == cartInfoData.type)
            assert(it.transactionAmount == cartInfoData.attributes?.pricePlain)
            assert(it.userAgent == DeviceUtil.userAgentForApiCall)
            assert(it.isNeedOtp == cartInfoData.isNeedOtp)
        }

    }
}