package com.tokopedia.digital_checkout

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.network.exception.HttpErrorException
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common_digital.atc.data.response.FintechProduct
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst.SummaryInfo.STRING_KODE_PROMO
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst.SummaryInfo.STRING_SUBTOTAL_TAGIHAN
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData
import com.tokopedia.digital_checkout.data.request.DigitalCheckoutDataParameter
import com.tokopedia.digital_checkout.data.response.CancelVoucherData
import com.tokopedia.digital_checkout.data.response.ResponsePatchOtpSuccess
import com.tokopedia.digital_checkout.data.response.getcart.RechargeGetCart
import com.tokopedia.digital_checkout.dummy.DigitalCartDummyData
import com.tokopedia.digital_checkout.dummy.DigitalCartDummyData.getDummyGetCartResponse
import com.tokopedia.digital_checkout.dummy.DigitalCartDummyData.getDummyGetCartResponseDisableVoucher
import com.tokopedia.digital_checkout.presentation.viewmodel.DigitalCartViewModel
import com.tokopedia.digital_checkout.usecase.DigitalCancelVoucherUseCase
import com.tokopedia.digital_checkout.usecase.DigitalCheckoutUseCase
import com.tokopedia.digital_checkout.usecase.DigitalGetCartUseCase
import com.tokopedia.digital_checkout.usecase.DigitalPatchOtpUseCase
import com.tokopedia.digital_checkout.utils.DeviceUtil
import com.tokopedia.digital_checkout.utils.DigitalCurrencyUtil.getStringIdrFormat
import com.tokopedia.digital_checkout.utils.analytics.DigitalAnalytics
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
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
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException
import java.lang.reflect.Type
import java.net.ConnectException
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
        digitalCartViewModel = DigitalCartViewModel(
            digitalAnalytics,
            digitalGetCartUseCase,
            digitalCancelVoucherUseCase, digitalPatchOtpUseCase,
            digitalCheckoutUseCase,
            userSession,
            Dispatchers.Unconfined
        )
    }

    @Test
    fun getCart_onSuccess_NoNeedOtpAndIsNotSubscribed() {
        //given
        val dummyResponse =
            DigitalCartDummyData.getDummyGetCartResponseWithDefaultCrossSellType().copy(
                isAdminFeeIncluded = true
            )
        coEvery { digitalGetCartUseCase.execute(any(), any(), any()) } coAnswers {
            secondArg<(RechargeGetCart.Response) -> Unit>().invoke(
                RechargeGetCart.Response(
                    dummyResponse
                )
            )
        }
        coEvery { userSession.isLoggedIn } returns true

        //when
        val categoryId = "1"
        digitalCartViewModel.getCart(categoryId, isSpecialProduct = false)

        // then
        // must show content and show loading
        assert(digitalCartViewModel.showContentCheckout.value != null)
        assert(digitalCartViewModel.showContentCheckout.value!!)
        assert(digitalCartViewModel.showLoading.value != null)
        assert(!digitalCartViewModel.showLoading.value!!)

        // show mapped cart data
        val mappedCartInfoData = digitalCartViewModel.cartDigitalInfoData.value
        assert(mappedCartInfoData != null)
        assert(
            mappedCartInfoData!!.mainInfo.getOrNull(0)?.label == dummyResponse.mainnInfo.getOrNull(
                0
            )?.label
        )
        assert(
            mappedCartInfoData.mainInfo.getOrNull(0)?.value == dummyResponse.mainnInfo.getOrNull(
                0
            )?.value
        )
        assert(
            mappedCartInfoData.additionalInfos.getOrNull(0)?.title == dummyResponse.additionalInfo.getOrNull(
                0
            )?.title
        )
        assert(
            mappedCartInfoData.additionalInfos.getOrNull(0)?.items?.getOrNull(0)?.label
                    == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.label
        )
        assert(
            mappedCartInfoData.additionalInfos.getOrNull(0)?.items?.getOrNull(0)?.value
                    == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.value
        )
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

        val (subscriptions, fintechProduct) = dummyResponse.fintechProduct.partition {
            it.transactionType == DigitalCheckoutConst.FintechProduct.AUTO_DEBIT
        }
        val (mappedSubscriptions, mappedFintechProduct) = mappedCartInfoData.attributes.fintechProduct.partition {
            it.transactionType == DigitalCheckoutConst.FintechProduct.AUTO_DEBIT
        }

        assert(mappedCartInfoData.isSubscribed == (subscriptions.isNotEmpty() && subscriptions[0].checkBoxDisabled))
        assert(mappedSubscriptions.size == subscriptions.size)
        assert(mappedSubscriptions.getOrNull(0)?.transactionType == subscriptions.getOrNull(0)?.transactionType)
        assert(mappedSubscriptions.getOrNull(0)?.fintechAmount == subscriptions.getOrNull(0)?.fintechAmount)
        assert(mappedFintechProduct.size == fintechProduct.size)
        assert(mappedFintechProduct.getOrNull(0)?.transactionType == fintechProduct.getOrNull(0)?.transactionType)
        assert(mappedFintechProduct.getOrNull(0)?.fintechAmount == fintechProduct.getOrNull(0)?.fintechAmount)
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
            .copy(isAdminFeeIncluded = false)
        coEvery { digitalGetCartUseCase.execute(any(), any(), any()) } coAnswers {
            secondArg<(RechargeGetCart.Response) -> Unit>().invoke(
                RechargeGetCart.Response(
                    dummyResponse
                )
            )
        }
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.phoneNumber } returns "number"

        //when
        val categoryId = "1"
        digitalCartViewModel.getCart(categoryId, isSpecialProduct = true)

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
        digitalCartViewModel.getCart(categoryId, userNotLoginMessage, isSpecialProduct = true)

        //then
        assert(digitalCartViewModel.errorThrowable.value!!.throwable.message == userNotLoginMessage)
    }

    @Test
    fun getCart_onSuccess_NoNeedOtpAndIsSubscribed() {
        //given
        val dummyResponse = getDummyGetCartResponse(true)
        coEvery { digitalGetCartUseCase.execute(any(), any(), any()) } coAnswers {
            secondArg<(RechargeGetCart.Response) -> Unit>().invoke(
                RechargeGetCart.Response(
                    dummyResponse
                )
            )
        }
        coEvery { userSession.isLoggedIn } returns true

        //when
        val categoryId = "1"
        digitalCartViewModel.getCart(categoryId, isSpecialProduct = false)

        // then
        // must show content and show loading
        assert(digitalCartViewModel.showContentCheckout.value != null)
        assert(digitalCartViewModel.showContentCheckout.value!!)
        assert(digitalCartViewModel.showLoading.value != null)
        assert(!digitalCartViewModel.showLoading.value!!)

        // show mapped cart data
        val mappedCartInfoData = digitalCartViewModel.cartDigitalInfoData.value
        assert(mappedCartInfoData != null)
        assert(
            mappedCartInfoData!!.mainInfo.getOrNull(0)?.label == dummyResponse.mainnInfo.getOrNull(
                0
            )?.label
        )
        assert(
            mappedCartInfoData.mainInfo.getOrNull(0)?.value == dummyResponse.mainnInfo.getOrNull(
                0
            )?.value
        )
        assert(
            mappedCartInfoData.additionalInfos.getOrNull(0)?.title == dummyResponse.additionalInfo.getOrNull(
                0
            )?.title
        )
        assert(
            mappedCartInfoData.additionalInfos.getOrNull(0)?.items?.getOrNull(0)?.label
                    == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.label
        )
        assert(
            mappedCartInfoData.additionalInfos.getOrNull(0)?.items?.getOrNull(0)?.value
                    == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.value
        )
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

        val (subscriptions, fintechProduct) = dummyResponse.fintechProduct.partition {
            it.transactionType == DigitalCheckoutConst.FintechProduct.AUTO_DEBIT
        }
        val (mappedSubscriptions, mappedFintechProduct) = mappedCartInfoData.attributes.fintechProduct.partition {
            it.transactionType == DigitalCheckoutConst.FintechProduct.AUTO_DEBIT
        }

        assert(mappedCartInfoData.isSubscribed == (subscriptions.isNotEmpty() && subscriptions[0].checkBoxDisabled))
        assert(mappedSubscriptions.size == subscriptions.size)
        assert(mappedSubscriptions.getOrNull(0)?.transactionType == subscriptions.getOrNull(0)?.transactionType)
        assert(mappedSubscriptions.getOrNull(0)?.fintechAmount == subscriptions.getOrNull(0)?.fintechAmount)
        assert(mappedFintechProduct.size == fintechProduct.size)
        assert(mappedFintechProduct.getOrNull(0)?.transactionType == fintechProduct.getOrNull(0)?.transactionType)
        assert(mappedFintechProduct.getOrNull(0)?.fintechAmount == fintechProduct.getOrNull(0)?.fintechAmount)
        assert(mappedCartInfoData.id == dummyResponse.id)
        assert(mappedCartInfoData.isInstantCheckout == dummyResponse.isInstantCheckout)

        // show correct total price
        assert(digitalCartViewModel.totalPrice.value != null)
        assert(digitalCartViewModel.totalPrice.value == dummyResponse.price + dummyResponse.adminFee)
    }

    @Test
    fun getCart_onSuccess_NoNeedOtpAndIsSubscribedNotOpenAmount() {
        //given
        val dummyResponse = getDummyGetCartResponse(false)
        coEvery { digitalGetCartUseCase.execute(any(), any(), any()) } coAnswers {
            secondArg<(RechargeGetCart.Response) -> Unit>().invoke(
                RechargeGetCart.Response(
                    dummyResponse
                )
            )
        }
        coEvery { userSession.isLoggedIn } returns true

        //when
        val categoryId = "1"
        digitalCartViewModel.getCart(categoryId, isSpecialProduct = true)

        // then
        // must show content and show loading
        assert(digitalCartViewModel.showContentCheckout.value != null)
        assert(digitalCartViewModel.showContentCheckout.value!!)
        assert(digitalCartViewModel.showLoading.value != null)
        assert(!digitalCartViewModel.showLoading.value!!)

        // show mapped cart data
        val mappedCartInfoData = digitalCartViewModel.cartDigitalInfoData.value
        assert(mappedCartInfoData != null)
        assert(
            mappedCartInfoData!!.mainInfo.getOrNull(0)?.label == dummyResponse.mainnInfo.getOrNull(
                0
            )?.label
        )
        assert(
            mappedCartInfoData.mainInfo.getOrNull(0)?.value == dummyResponse.mainnInfo.getOrNull(
                0
            )?.value
        )
        assert(
            mappedCartInfoData.additionalInfos.getOrNull(0)?.title == dummyResponse.additionalInfo.getOrNull(
                0
            )?.title
        )
        assert(
            mappedCartInfoData.additionalInfos.getOrNull(0)?.items?.getOrNull(0)?.label
                    == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.label
        )
        assert(
            mappedCartInfoData.additionalInfos.getOrNull(0)?.items?.getOrNull(0)?.value
                    == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.value
        )
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

        val (subscriptions, fintechProduct) = dummyResponse.fintechProduct.partition {
            it.transactionType == DigitalCheckoutConst.FintechProduct.AUTO_DEBIT
        }
        val (mappedSubscriptions, mappedFintechProduct) = mappedCartInfoData.attributes.fintechProduct.partition {
            it.transactionType == DigitalCheckoutConst.FintechProduct.AUTO_DEBIT
        }

        assert(mappedCartInfoData.isSubscribed == (subscriptions.isNotEmpty() && subscriptions[0].checkBoxDisabled))
        assert(mappedSubscriptions.size == subscriptions.size)
        assert(mappedSubscriptions.getOrNull(0)?.transactionType == subscriptions.getOrNull(0)?.transactionType)
        assert(mappedSubscriptions.getOrNull(0)?.fintechAmount == subscriptions.getOrNull(0)?.fintechAmount)
        assert(mappedFintechProduct.size == fintechProduct.size)
        assert(mappedFintechProduct.getOrNull(0)?.transactionType == fintechProduct.getOrNull(0)?.transactionType)
        assert(mappedFintechProduct.getOrNull(0)?.fintechAmount == fintechProduct.getOrNull(0)?.fintechAmount)
        assert(mappedCartInfoData.id == dummyResponse.id)
        assert(!mappedCartInfoData.attributes.isOpenAmount)
        assert(mappedCartInfoData.isInstantCheckout == dummyResponse.isInstantCheckout)

        // show correct total price
        assert(digitalCartViewModel.totalPrice.value != null)
        assert(digitalCartViewModel.totalPrice.value == dummyResponse.price)
    }

    @Test
    fun getCart_onSuccessWithCouponActive_NoNeedOtpAndIsSubscribed() {
        //given
        val dummyResponse = DigitalCartDummyData.getDummyGetCartResponseWithCouponActive()
        coEvery { digitalGetCartUseCase.execute(any(), any(), any()) } coAnswers {
            secondArg<(RechargeGetCart.Response) -> Unit>().invoke(
                RechargeGetCart.Response(
                    dummyResponse
                )
            )
        }
        coEvery { userSession.isLoggedIn } returns true

        //when
        val categoryId = "1"
        digitalCartViewModel.getCart(categoryId, isSpecialProduct = false)

        // then
        // must show content and show loading
        assert(digitalCartViewModel.showContentCheckout.value != null)
        assert(digitalCartViewModel.showContentCheckout.value!!)
        assert(digitalCartViewModel.showLoading.value != null)
        assert(!digitalCartViewModel.showLoading.value!!)

        // show mapped cart data
        val mappedCartInfoData = digitalCartViewModel.cartDigitalInfoData.value
        assert(mappedCartInfoData != null)
        assert(
            mappedCartInfoData!!.mainInfo.getOrNull(0)?.label == dummyResponse.mainnInfo.getOrNull(
                0
            )?.label
        )
        assert(
            mappedCartInfoData.mainInfo.getOrNull(0)?.value == dummyResponse.mainnInfo.getOrNull(
                0
            )?.value
        )
        assert(
            mappedCartInfoData.additionalInfos.getOrNull(0)?.title == dummyResponse.additionalInfo.getOrNull(
                0
            )?.title
        )
        assert(
            mappedCartInfoData.additionalInfos.getOrNull(0)?.items?.getOrNull(0)?.label
                    == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.label
        )
        assert(
            mappedCartInfoData.additionalInfos.getOrNull(0)?.items?.getOrNull(0)?.value
                    == dummyResponse.additionalInfo.getOrNull(0)?.detail?.getOrNull(0)?.value
        )
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

        val (subscriptions, fintechProduct) = dummyResponse.fintechProduct.partition {
            it.transactionType == DigitalCheckoutConst.FintechProduct.AUTO_DEBIT
        }
        val (mappedSubscriptions, mappedFintechProduct) = mappedCartInfoData.attributes.fintechProduct.partition {
            it.transactionType == DigitalCheckoutConst.FintechProduct.AUTO_DEBIT
        }

        assert(mappedCartInfoData.isSubscribed == (subscriptions.isNotEmpty() && subscriptions[0].checkBoxDisabled))
        assert(mappedSubscriptions.size == subscriptions.size)
        assert(mappedSubscriptions.getOrNull(0)?.transactionType == subscriptions.getOrNull(0)?.transactionType)
        assert(mappedSubscriptions.getOrNull(0)?.fintechAmount == subscriptions.getOrNull(0)?.fintechAmount)
        assert(mappedFintechProduct.size == fintechProduct.size)
        assert(mappedFintechProduct.getOrNull(0)?.transactionType == fintechProduct.getOrNull(0)?.transactionType)
        assert(mappedFintechProduct.getOrNull(0)?.fintechAmount == fintechProduct.getOrNull(0)?.fintechAmount)

        // show correct total price
        assert(digitalCartViewModel.totalPrice.value != null)
        assert(digitalCartViewModel.totalPrice.value == dummyResponse.price - dummyResponse.autoApply.discountAmount.toInt())

        assert(digitalCartViewModel.promoData.value != null)
        assert(digitalCartViewModel.promoData.value!!.amount == dummyResponse.autoApply.discountAmount.toLong())
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
        digitalCartViewModel.getCart(categoryId, isSpecialProduct = true)

        //then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorThrowable.value!!.throwable is UnknownHostException)
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
        digitalCartViewModel.getCart(categoryId, isSpecialProduct = false)

        //then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorThrowable.value!!.throwable is SocketTimeoutException)
    }

    @Test
    fun getCart_onFailed_socketConnectException() {
        //given
        coEvery { digitalGetCartUseCase.execute(any(), any(), any()) } coAnswers {
            thirdArg<(Throwable) -> Unit>().invoke(ConnectException())
        }
        coEvery { userSession.isLoggedIn } returns true

        //when
        val categoryId = "1"
        digitalCartViewModel.getCart(categoryId, isSpecialProduct = true)

        //then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorThrowable.value!!.throwable is ConnectException)
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
        digitalCartViewModel.getCart(categoryId, isSpecialProduct = false)

        //then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorThrowable.value!!.throwable is ResponseErrorException)
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
        digitalCartViewModel.getCart(categoryId, isSpecialProduct = true)

        //then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorThrowable.value!!.throwable is ResponseDataNullException)
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
        digitalCartViewModel.getCart(categoryId, isSpecialProduct = false)

        //then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorThrowable.value!!.throwable is HttpErrorException)
        assert((digitalCartViewModel.errorThrowable.value!!.throwable as HttpErrorException).errorCode == 504)
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
        digitalCartViewModel.getCart(categoryId, isSpecialProduct = true)

        //then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorThrowable.value!!.throwable.message == errorMessage)
    }

    @Test
    fun onCancelVoucher_onSuccess_dataIsSuccess() {
        // given
        val cancelVoucherData = CancelVoucherData(success = true)
        coEvery { digitalCancelVoucherUseCase.execute(any(), any(), any()) } coAnswers {
            secondArg<(CancelVoucherData.Response) -> Unit>().invoke(
                CancelVoucherData.Response(
                    cancelVoucherData
                )
            )
        }

        // when
        digitalCartViewModel.cancelVoucherCart("", "")

        // then
        assert(digitalCartViewModel.cancelVoucherData.value is Success)
        assert((digitalCartViewModel.cancelVoucherData.value as Success).data.success)
    }

    @Test
    fun onCancelVoucher_onSuccess_dataIsFailure() {
        // given
        val cancelVoucherData = CancelVoucherData(success = false)
        coEvery { digitalCancelVoucherUseCase.execute(any(), any(), any()) } coAnswers {
            secondArg<(CancelVoucherData.Response) -> Unit>().invoke(
                CancelVoucherData.Response(
                    cancelVoucherData
                )
            )
        }
        val defaultErrorMsg = "default error message"

        // when
        digitalCartViewModel.cancelVoucherCart("", defaultErrorMsg)

        // then
        assert(digitalCartViewModel.cancelVoucherData.value is Fail)
        assert(
            (digitalCartViewModel.cancelVoucherData.value as Fail).throwable.message?.equals(
                defaultErrorMsg
            )
                ?: false
        )
    }

    @Test
    fun onCancelVoucher_onFailed() {
        // given
        val errorMessage = "this is error"
        coEvery { digitalCancelVoucherUseCase.execute(any(), any(), any()) } coAnswers {
            thirdArg<(Throwable) -> Unit>().invoke(Throwable(errorMessage))
        }

        // when
        digitalCartViewModel.cancelVoucherCart("", "")

        // then
        assert(digitalCartViewModel.cancelVoucherData.value is Fail)
        assert((digitalCartViewModel.cancelVoucherData.value as Fail).throwable.message == errorMessage)
    }

    @Test
    fun onCancelVoucher_onFailedWithEmptyMessage() {
        // given
        val errorMessage = ""
        coEvery { digitalCancelVoucherUseCase.execute(any(), any(), any()) } coAnswers {
            thirdArg<(Throwable) -> Unit>().invoke(Throwable(errorMessage))
        }

        // when
        digitalCartViewModel.cancelVoucherCart("", "Default Error Message")

        // then
        assert(digitalCartViewModel.cancelVoucherData.value is Fail)
        assert((digitalCartViewModel.cancelVoucherData.value as Fail).throwable.message == "Default Error Message")
    }

    @Test
    fun onCancelVoucher_onFailedWithNullMessage() {
        // given
        val errorMessage: String? = null
        coEvery { digitalCancelVoucherUseCase.execute(any(), any(), any()) } coAnswers {
            thirdArg<(Throwable) -> Unit>().invoke(Throwable(errorMessage))
        }

        // when
        digitalCartViewModel.cancelVoucherCart("", "Default Error Message")

        // then
        assert(digitalCartViewModel.cancelVoucherData.value is Fail)
        assert((digitalCartViewModel.cancelVoucherData.value as Fail).throwable.message == "Default Error Message")
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
        digitalCartViewModel.processPatchOtpCart(
            RequestBodyIdentifier(),
            DigitalCheckoutPassData(),
            "",
            isSpecialProduct = true
        )

        // then get cart
        // show loading and hide content
        assert(digitalCartViewModel.showContentCheckout.value != null)
        assert(!digitalCartViewModel.showContentCheckout.value!!)
        assert(digitalCartViewModel.showLoading.value != null)
    }

    @Test
    fun onPatchOtp_onSuccess_withCategoryId() {
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
        digitalCartViewModel.processPatchOtpCart(
            RequestBodyIdentifier(),
            DigitalCheckoutPassData().apply {
                categoryId = "dummy"
            },
            "",
            isSpecialProduct = true
        )

        // then get cart
        // show loading and hide content
        assert(digitalCartViewModel.showContentCheckout.value != null)
        assert(!digitalCartViewModel.showContentCheckout.value!!)
        assert(digitalCartViewModel.showLoading.value != null)
    }

    @Test
    fun onPatchOtp_onSuccessFalse() {
        // given
        val dummyResponse = ResponsePatchOtpSuccess(false)
        val dataResponse = DataResponse<ResponsePatchOtpSuccess>()
        dataResponse.data = dummyResponse

        val token = object : TypeToken<DataResponse<ResponsePatchOtpSuccess>>() {}.type
        val response = RestResponse(dataResponse, 200, false)
        val responseMap = mapOf<Type, RestResponse>(token to response)

        coEvery { digitalPatchOtpUseCase.executeOnBackground() } returns responseMap
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        digitalCartViewModel.processPatchOtpCart(
            RequestBodyIdentifier(),
            DigitalCheckoutPassData(),
            "",
            isSpecialProduct = true
        )

        // then get cart
        // show loading and hide content
        assert(digitalCartViewModel.showLoading.value == null)
    }

    @Test
    fun onPatchOtp_onFailed() {
        // given
        coEvery { digitalPatchOtpUseCase.executeOnBackground() } throws IOException("error")
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        digitalCartViewModel.processPatchOtpCart(
            RequestBodyIdentifier(),
            DigitalCheckoutPassData(),
            isSpecialProduct = false
        )

        // then
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorThrowable.value!!.throwable is IOException)
    }

    @Test
    fun onPatchOtp_onFailedResponseError() {
        // given
        coEvery { digitalPatchOtpUseCase.executeOnBackground() } throws ResponseErrorException("error")
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        digitalCartViewModel.processPatchOtpCart(
            RequestBodyIdentifier(),
            DigitalCheckoutPassData(),
            isSpecialProduct = false
        )

        // then
        println("error: ${digitalCartViewModel.errorThrowable.value}")
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorThrowable.value!!.throwable is MessageErrorException)

        assert(digitalCartViewModel.errorThrowable.value!!.throwable.message == "error")
    }

    @Test
    fun onCheckout_onSuccess() {
        // given
        val dummyResponse = PaymentPassData()
        dummyResponse.queryString = "this is query"
        dummyResponse.redirectUrl = "www.tokopedia.com"
        dummyResponse.callbackSuccessUrl = "successurl"
        dummyResponse.callbackFailedUrl = "failedUrl"
        dummyResponse.transactionId = "transactionId"

        coEvery {
            digitalCheckoutUseCase.execute(any(), any(), any(), any())
        } returns dummyResponse
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.proceedToCheckout(RequestBodyIdentifier(), false)

        // then
        val paymentPassDataValue = digitalCartViewModel.paymentPassData.value
        assert(paymentPassDataValue != null)
        assert(paymentPassDataValue!!.callbackFailedUrl == dummyResponse.callbackFailedUrl)
        assert(paymentPassDataValue.callbackSuccessUrl == dummyResponse.callbackSuccessUrl)
        assert(paymentPassDataValue.redirectUrl == dummyResponse.redirectUrl)
        assert(paymentPassDataValue.queryString == dummyResponse.queryString)
        assert(paymentPassDataValue.transactionId == dummyResponse.transactionId)
    }

    @Test
    fun onCheckout_onSuccessWithGql() {
        // given
        val dummyResponse = PaymentPassData()
        dummyResponse.queryString = "this is query"
        dummyResponse.redirectUrl = "www.tokopedia.com"
        dummyResponse.callbackSuccessUrl = "successurl"
        dummyResponse.callbackFailedUrl = "failedUrl"
        dummyResponse.transactionId = "transactionId"

        coEvery {
            digitalCheckoutUseCase.execute(any(), any(), any(), any())
        } returns dummyResponse
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        digitalCartViewModel.requestCheckoutParam = DigitalCheckoutDataParameter(
            isNeedOtp = false,
            fintechProducts = hashMapOf(
                "First" to FintechProduct(
                    info = FintechProduct.FintechProductInfo(
                        iconUrl = "dummy icon url"
                    )
                ),
                "Second" to FintechProduct(
                    info = FintechProduct.FintechProductInfo(
                        iconUrl = ""
                    )
                )
            )
        )

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.proceedToCheckout(RequestBodyIdentifier(), true)

        // then
        val paymentPassDataValue = digitalCartViewModel.paymentPassData.value
        assert(paymentPassDataValue != null)
        assert(paymentPassDataValue!!.callbackFailedUrl == dummyResponse.callbackFailedUrl)
        assert(paymentPassDataValue.callbackSuccessUrl == dummyResponse.callbackSuccessUrl)
        assert(paymentPassDataValue.redirectUrl == dummyResponse.redirectUrl)
        assert(paymentPassDataValue.queryString == dummyResponse.queryString)
        assert(paymentPassDataValue.transactionId == dummyResponse.transactionId)

        coEvery { digitalAnalytics.eventProceedCheckoutTebusMurah(any(), any(), any()) }
        coEvery { digitalAnalytics.eventProceedCheckoutCrossell(any(), any(), any()) }
    }

    @Test
    fun onCheckout_onFailed() {
        // given
        coEvery {
            digitalCheckoutUseCase.execute(any(), any(), any(), any())
        } throws IOException("error")
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.proceedToCheckout(RequestBodyIdentifier(), false)

        // then
        val paymentPassDataValue = digitalCartViewModel.paymentPassData.value
        assert(paymentPassDataValue == null)
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorThrowable.value!!.throwable is IOException)
    }

    @Test
    fun onCheckout_onFailedWithPromoCode() {
        // given
        coEvery {
            digitalCheckoutUseCase.execute(any(), any(), any(), any())
        } throws IOException("error")
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        onApplyDiscountPromoCode_updateCheckoutSummary()
        digitalCartViewModel.proceedToCheckout(RequestBodyIdentifier(), false)

        // then
        val paymentPassDataValue = digitalCartViewModel.paymentPassData.value
        assert(paymentPassDataValue == null)
        assert(!digitalCartViewModel.showLoading.value!!)
        assert(digitalCartViewModel.errorThrowable.value!!.throwable is IOException)
    }

    @Test
    fun onCheckout_onPromoCodeEmptyAndCartEmpty_shouldNotCheckout() {
        // given
        coEvery {
            digitalCheckoutUseCase.execute(any(), any(), any(), any())
        } throws IOException("error")
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        digitalCartViewModel.proceedToCheckout(RequestBodyIdentifier(), false)

        // then
        assertNull(digitalCartViewModel.paymentPassData.value)
        assertNull(digitalCartViewModel.showLoading.value)
        assertNull(digitalCartViewModel.errorThrowable.value)
    }

    @Test
    fun onCheckout_needOtp() {
        // given
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()

        digitalCartViewModel.requestCheckoutParam = DigitalCheckoutDataParameter(
            isNeedOtp = true,
            fintechProducts = hashMapOf(
                "First" to FintechProduct(
                    info = FintechProduct.FintechProductInfo(
                        iconUrl = "dummy icon url"
                    )
                ),
                "Second" to FintechProduct(
                    info = FintechProduct.FintechProductInfo(
                        iconUrl = ""
                    )
                )
            )
        )
        digitalCartViewModel.proceedToCheckout(RequestBodyIdentifier(), false)

        // then
        assert(digitalCartViewModel.isNeedOtp.value != null)
    }

    @Test
    fun onCheckout_withFintechProduct() {
        // given
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()

        digitalCartViewModel.requestCheckoutParam = DigitalCheckoutDataParameter(
            isNeedOtp = false,
            fintechProducts = hashMapOf(
                "First" to FintechProduct(
                    info = FintechProduct.FintechProductInfo(
                        iconUrl = "dummy icon url"
                    )
                ),
                "Second" to FintechProduct(
                    info = FintechProduct.FintechProductInfo(
                        iconUrl = ""
                    )
                )
            )
        )
        digitalCartViewModel.proceedToCheckout(RequestBodyIdentifier(), false)

        // then
        assert(digitalCartViewModel.isNeedOtp.value == null)
    }

    @Test
    fun onCheckoutGql_withFintechProduct() {
        // given
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"
        digitalCartViewModel.setPromoData(PromoData())

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()

        digitalCartViewModel.requestCheckoutParam = DigitalCheckoutDataParameter(
            isNeedOtp = false,
            fintechProducts = hashMapOf(
                "First" to FintechProduct(
                    info = FintechProduct.FintechProductInfo(
                        iconUrl = "dummy icon url"
                    )
                ),
                "Second" to FintechProduct(
                    info = FintechProduct.FintechProductInfo(
                        iconUrl = ""
                    )
                )
            )
        )
        digitalCartViewModel.proceedToCheckout(RequestBodyIdentifier(), true)

        // then
        assert(digitalCartViewModel.isNeedOtp.value == null)
    }

    @Test
    fun onCheckout_throwResponseErrorException() {
        // given
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        val errorException = ResponseErrorException()
        coEvery {
            digitalCheckoutUseCase.execute(any(), any(), any(), any())
        } throws errorException

        digitalCartViewModel.requestCheckoutParam = DigitalCheckoutDataParameter(
            isNeedOtp = false,
            fintechProducts = hashMapOf(
                "First" to FintechProduct(
                    info = FintechProduct.FintechProductInfo(
                        iconUrl = "dummy icon url"
                    )
                ),
                "Second" to FintechProduct(
                    info = FintechProduct.FintechProductInfo(
                        iconUrl = ""
                    )
                )
            )
        )
        digitalCartViewModel.proceedToCheckout(RequestBodyIdentifier(), false)

        // then
        assert(digitalCartViewModel.errorThrowable.value is Fail)
        assert((digitalCartViewModel.errorThrowable.value as Fail).throwable is MessageErrorException)
        assert((digitalCartViewModel.errorThrowable.value as Fail).throwable.message == errorException.message)
    }

    @Test
    fun onCheckout_throwThrowable() {
        // given
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        val errorException = Throwable("dummy error")
        coEvery {
            digitalCheckoutUseCase.execute(any(), any(), any(), any())
        } throws errorException

        digitalCartViewModel.requestCheckoutParam = DigitalCheckoutDataParameter(
            isNeedOtp = false,
            fintechProducts = hashMapOf(
                "First" to FintechProduct(
                    info = FintechProduct.FintechProductInfo(
                        iconUrl = "dummy icon url"
                    )
                ),
                "Second" to FintechProduct(
                    info = FintechProduct.FintechProductInfo(
                        iconUrl = ""
                    )
                )
            )
        )
        digitalCartViewModel.proceedToCheckout(RequestBodyIdentifier(), false)

        // then
        assert(digitalCartViewModel.errorThrowable.value is Fail)
        assert((digitalCartViewModel.errorThrowable.value as Fail).throwable.message == errorException.message)
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
        assert(digitalCartViewModel.promoData.value?.amount == 0L)
        assert(digitalCartViewModel.promoData.value?.promoCode == "")
        assert(digitalCartViewModel.totalPrice.value == getDummyGetCartResponse().price + getDummyGetCartResponse().adminFee)
    }

    @Test
    fun onResetVoucherCart_addOnAdditionalInfoAndUpdateTotalPayment() {
        // given
        val promoData = PromoData()
        promoData.amount = 12000L
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
        promoData.amount = 12000L
        promoData.promoCode = "dummyPromoCode"
        promoData.state = TickerCheckoutView.State.ACTIVE

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.setPromoData(promoData)
        digitalCartViewModel.applyPromoData(promoData)

        val summary = digitalCartViewModel.payment.value!!.summaries.firstOrNull() {
            it.title == STRING_KODE_PROMO
        }
        assert(
            summary?.priceAmount == String.format(
                "-%s",
                getStringIdrFormat(promoData.amount.toDouble())
            )
        )
    }

    @Test
    fun onApplyDiscountPromoCodeWhenDisabledVoucher_updateCheckoutSummary() {
        //given
        val dummyResponse = getDummyGetCartResponseDisableVoucher()
        coEvery { digitalGetCartUseCase.execute(any(), any(), any()) } coAnswers {
            secondArg<(RechargeGetCart.Response) -> Unit>().invoke(
                RechargeGetCart.Response(
                    dummyResponse
                )
            )
        }
        coEvery { userSession.isLoggedIn } returns true

        //when
        val categoryId = "1"
        digitalCartViewModel.getCart(categoryId, isSpecialProduct = true)

        //then
        assertNotNull(digitalCartViewModel.promoData.value)
        val promoData = digitalCartViewModel.promoData.value ?: PromoData()
        assert(!promoData.isActive())
        assert(promoData.state == TickerCheckoutView.State.INACTIVE)
        assert(promoData.description == "PROMOO")
        assert(promoData.amount == 5000L)

        //when
        digitalCartViewModel.applyPromoData(promoData)

        //then
        val summary = digitalCartViewModel.payment.value!!.summaries.firstOrNull {
            it.title == "PROMOO"
        }
        assert(summary?.priceAmount == String.format("-%s", getStringIdrFormat(5000.0)))
    }

    @Test
    fun onApplyDiscountPromoCode_withEmptyState_updateCheckoutSummary() {
        // given
        val promoData = PromoData()
        promoData.amount = 12000L
        promoData.promoCode = "dummyPromoCode"
        promoData.state = TickerCheckoutView.State.EMPTY
        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.setPromoData(promoData)
        digitalCartViewModel.applyPromoData(promoData)

        val summary = digitalCartViewModel.payment.value!!.summaries.firstOrNull() {
            it.title == STRING_KODE_PROMO
        }
        assertNull(summary)
    }

    @Test
    fun onApplyDiscountPromoCode_withFailedState_updateCheckoutSummary() {
        // given
        val promoData = PromoData()
        promoData.amount = 12000L
        promoData.promoCode = "dummyPromoCode"
        promoData.state = TickerCheckoutView.State.FAILED

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.setPromoData(promoData)
        digitalCartViewModel.applyPromoData(promoData)

        val summary = digitalCartViewModel.payment.value!!.summaries.firstOrNull() {
            it.title == STRING_KODE_PROMO
        }
        assertNull(summary)
    }

    @Test
    fun onApplyDiscountPromoCode_withInactiveState_updateCheckoutSummary() {
        // given
        val promoData = PromoData()
        promoData.state = TickerCheckoutView.State.INACTIVE

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.setPromoData(promoData)
        digitalCartViewModel.applyPromoData(promoData)

        val summary = digitalCartViewModel.payment.value!!.summaries.firstOrNull() {
            it.title == STRING_KODE_PROMO
        }
        assertNull(summary)
    }

    @Test
    fun onReceivedPromoCode_withoutSetPromoData() {
        // given
        val promoData = PromoData()
        promoData.state = TickerCheckoutView.State.ACTIVE

        // when
        digitalCartViewModel.applyPromoData(promoData)

        //then
        val summary = digitalCartViewModel.payment.value!!.summaries.firstOrNull() {
            it.title == STRING_KODE_PROMO
        }
        assertNull(summary)
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
            it.title == STRING_KODE_PROMO
        }
        assertNull(summary)
    }

    @Test
    fun onDiscardPromoCode_updateCheckoutSummary() {
        // given
        val promoData1 = PromoData()
        promoData1.amount = 12000L
        promoData1.promoCode = "dummyPromoCode"
        promoData1.state = TickerCheckoutView.State.ACTIVE

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.setPromoData(promoData1)
        digitalCartViewModel.applyPromoData(promoData1)
        digitalCartViewModel.resetCheckoutSummaryPromoAndTotalPrice()

        val summary = digitalCartViewModel.payment.value!!.summaries.firstOrNull() {
            it.title == STRING_KODE_PROMO
        }
        assertNull(summary)
    }

    @Test
    fun onCheckedFintechProduct_updateCheckoutSummary() {
        // given
        val fintechInfo = FintechProduct.FintechProductInfo(title = "fintech A")
        val fintechProduct = FintechProduct(
            tierId = "3",
            fintechAmount = 2000.0,
            info = fintechInfo,
            transactionType = "Pulsa"
        )

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.onFintechProductChecked(fintechProduct, true, null)

        // then
        val fintechPrice =
            digitalCartViewModel.requestCheckoutParam.fintechProducts["3"]?.fintechAmount
                ?: 0.0
        val fintechName =
            digitalCartViewModel.requestCheckoutParam.fintechProducts["3"]?.transactionType
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
        val fintechName =
            digitalCartViewModel.cartDigitalInfoData.value?.attributes?.fintechProduct?.getOrNull(0)?.info?.title
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
        val fintechPrice =
            digitalCartViewModel.requestCheckoutParam.fintechProducts["3"]?.fintechAmount
                ?: 0.0
        assert(digitalCartViewModel.totalPrice.value == oldTotalPrice + fintechPrice + getDummyGetCartResponse().adminFee)
    }

    @Test
    fun updateTotalPriceWithFintechProduct_unChecked() {
        // given
        val fintechProduct = FintechProduct(tierId = "3", fintechAmount = 2000.0)

        // when
        updateTotalPriceWithFintechProduct_checked()
        digitalCartViewModel.onFintechProductChecked(fintechProduct, false, null)

        // then
        val oldTotalPrice = digitalCartViewModel.cartDigitalInfoData.value?.attributes?.pricePlain
            ?: 0.0
        assert(digitalCartViewModel.requestCheckoutParam.fintechProducts.isEmpty())
        assert(digitalCartViewModel.totalPrice.value == oldTotalPrice + getDummyGetCartResponse().adminFee)
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
        val fintechPrice =
            digitalCartViewModel.requestCheckoutParam.fintechProducts["3"]?.fintechAmount
                ?: 0.0
        assert(digitalCartViewModel.totalPrice.value == userInputPrice + fintechPrice + getDummyGetCartResponse().adminFee)
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
        assert(digitalCartViewModel.totalPrice.value == userInputPrice + getDummyGetCartResponse().adminFee)
    }

    @Test
    fun updateTotalPriceWithFintechProduct_nullCartDigitalInfoData() {
        // given
        val fintechProduct = FintechProduct(tierId = "3", fintechAmount = 2000.0)
        val userInputPrice = 30000.0

        // when
        digitalCartViewModel.onFintechProductChecked(fintechProduct, false, userInputPrice)

        // then
        // if fintech product checked, update total price
        assertNull(digitalCartViewModel.totalPrice.value)
    }

    @Test
    fun onFintechProductChecked_isCheckedTrue() {
        // given
        val fintechProduct = FintechProduct(tierId = "3", fintechAmount = 2000.0)
        val userInputPrice = 30000.0

        // when
        digitalCartViewModel.onFintechProductChecked(fintechProduct, true, userInputPrice)

        // then
        // if fintech product checked, update total price
        assertNull(digitalCartViewModel.totalPrice.value)
    }

    @Test
    fun setTotalPrice_afterUserInputNumber() {
        // given
        val userInput = 100000.0

        // when
        getCart_onSuccess_NoNeedOtpAndIsSubscribed()
        digitalCartViewModel.setTotalPriceBasedOnUserInput(userInput)

        // then
        assert(digitalCartViewModel.totalPrice.value == userInput + getDummyGetCartResponse().adminFee)
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
            it.title == DigitalCheckoutConst.SummaryInfo.STRING_SUBTOTAL_TAGIHAN
        }
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
        val promoDigitalModel =
            digitalCartViewModel.getPromoDigitalModel(digitalCheckoutPassData, 1000.0)

        //then
        assert(promoDigitalModel.categoryId.toString() == digitalCheckoutPassData.categoryId ?: "")
        assert(promoDigitalModel.productId.toString() == digitalCheckoutPassData.productId ?: "")
        assert(promoDigitalModel.clientNumber == digitalCheckoutPassData.clientNumber)
        assert(promoDigitalModel.categoryName == "Angsuran Kredit")
        assert(promoDigitalModel.operatorName == "JTrust Olympindo Multi Finance")
        assert(promoDigitalModel.price == 1000L)
    }

    @Test
    fun getPromoDigitalModel_whenCartPassDataEqualsToNull_shouldReturnCorrectData() {
        //given
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = null
        digitalCheckoutPassData.productId = null
        digitalCheckoutPassData.clientNumber = null

        //given
        val promoDigitalModel =
            digitalCartViewModel.getPromoDigitalModel(digitalCheckoutPassData, null)

        //then
        assert(promoDigitalModel.categoryId == 0)
        assert(promoDigitalModel.productId == 0)
        assert(promoDigitalModel.clientNumber == "")
        assert(promoDigitalModel.categoryName == "")
        assert(promoDigitalModel.operatorName == "")
        assert(promoDigitalModel.price == 0L)
    }

    @Test
    fun getPromoDigitalModel_whenCartPassDataNotValid_shouldReturnCorrectData() {
        //given
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = "AAB"
        digitalCheckoutPassData.productId = "AAC"
        digitalCheckoutPassData.clientNumber = null

        //given
        val promoDigitalModel =
            digitalCartViewModel.getPromoDigitalModel(digitalCheckoutPassData, null)

        //then
        assert(promoDigitalModel.categoryId == 0)
        assert(promoDigitalModel.productId == 0)
        assert(promoDigitalModel.clientNumber == "")
        assert(promoDigitalModel.categoryName == "")
        assert(promoDigitalModel.operatorName == "")
        assert(promoDigitalModel.price == 0L)
    }

    @Test
    fun getPromoDigitalModel_whenCartPassDataEqualsToNull_shouldReturnCorrectData2() {
        //when
        val promoDigitalModel = digitalCartViewModel.getPromoDigitalModel(null, null)

        //then
        assert(promoDigitalModel.categoryId == 0)
        assert(promoDigitalModel.productId == 0)
        assert(promoDigitalModel.clientNumber == "")
        assert(promoDigitalModel.categoryName == "")
        assert(promoDigitalModel.operatorName == "")
        assert(promoDigitalModel.price == 0L)
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
        val promoDigitalModel =
            digitalCartViewModel.getPromoDigitalModel(digitalCheckoutPassData, null)

        assert(promoDigitalModel.categoryId.toString() == digitalCheckoutPassData.categoryId ?: "")
        assert(promoDigitalModel.productId.toString() == digitalCheckoutPassData.productId ?: "")
        assert(promoDigitalModel.clientNumber == digitalCheckoutPassData.clientNumber)
        assert(promoDigitalModel.categoryName == "Angsuran Kredit")
        assert(promoDigitalModel.operatorName == "JTrust Olympindo Multi Finance")
        assert(promoDigitalModel.price == 12500L)
    }
}