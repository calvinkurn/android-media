package com.tokopedia.purchase_platform.express_checkout.view.variant

import com.tokopedia.atc_common.domain.usecase.AddToCartOcsUseCase
import com.tokopedia.purchase_platform.express_checkout.domain.mapper.atc.AtcDomainModelMapper
import com.tokopedia.purchase_platform.express_checkout.domain.mapper.checkout.CheckoutDomainModelMapper
import com.tokopedia.purchase_platform.express_checkout.domain.usecase.DoAtcExpressUseCase
import com.tokopedia.purchase_platform.express_checkout.domain.usecase.DoCheckoutExpressUseCase
import com.tokopedia.purchase_platform.express_checkout.view.variant.mapper.ViewModelMapper
import com.tokopedia.shipping_recommendation.domain.usecase.GetCourierRecommendationUseCase
import com.tokopedia.transactiondata.entity.shared.expresscheckout.AtcRequestParam
import com.tokopedia.user.session.UserSessionInterface
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.runners.MockitoJUnitRunner

/**
 * Created by Irfan Khoirul on 04/01/19.
 */

@RunWith(MockitoJUnitRunner::class)
class CheckoutVariantPresenterTest {

    @Mock
    lateinit var view: CheckoutVariantContract.View
    @Mock
    lateinit var doAtcExpressUseCase: DoAtcExpressUseCase
    @Mock
    lateinit var doCheckoutExpressUseCase: DoCheckoutExpressUseCase
    @Mock
    lateinit var getCourierRecommendationUseCase: GetCourierRecommendationUseCase
    @Mock
    lateinit var atcDomainModelMapper: AtcDomainModelMapper
    @Mock
    lateinit var addToCartOcsUseCase: AddToCartOcsUseCase
    @Mock
    lateinit var checkoutDomainModelMapper: CheckoutDomainModelMapper
    @Mock
    lateinit var viewModelMapper: ViewModelMapper
    @Mock
    lateinit var userSessionInterface: UserSessionInterface

    private lateinit var presenter: CheckoutVariantPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = CheckoutVariantPresenter(
                doAtcExpressUseCase,
                doCheckoutExpressUseCase,
                getCourierRecommendationUseCase,
                addToCartOcsUseCase,
                atcDomainModelMapper,
                checkoutDomainModelMapper,
                viewModelMapper,
                userSessionInterface
        )
        presenter.attachView(view)
    }

    /**
     * Test load atc express flow positive case
     * */
    @Test
    @Throws(Exception::class)
    fun OnLoadAtcExpressThenSuccess_ShouldLoadRates() {
        //Given
        val atcRequestParam = AtcRequestParam()
        atcRequestParam.notes = "this is notes"
        atcRequestParam.productId = 122132
        atcRequestParam.quantity = 12
        atcRequestParam.shopId = 412457

        //When
        presenter.loadExpressCheckoutData(atcRequestParam)

        //Then
        Mockito.verify(view).showLoading()
        Mockito.verify(doAtcExpressUseCase).setParams(atcRequestParam)
    }

    /**
     * Test load atc express flow negative case
     * */
    @Test
    @Throws(Exception::class)
    fun OnLoadAtcExpressThenFailed_ShouldShowError() {
        //Given

        //When

        //Then
    }

    /**
     * Test load atc express flow negative case
     * */
    @Test
    @Throws(Exception::class)
    fun OnLoadAtcExpressThenHasNoProfile_ShouldNavigateToOneClickShipment() {
        //Given

        //When

        //Then
    }

    /**
     * Test load rates flow positive case
     * */
    @Test
    @Throws(Exception::class)
    fun OnLoadRatesThenSuccess_ShouldRenderData() {
        //Given

        //When

        //Then
    }

    /**
     * Test load rates flow negative case
     * */
    @Test
    @Throws(Exception::class)
    fun OnLoadRatesThenNetworkError_ShouldShowError() {
        //Given

        //When

        //Then
    }

    /**
     * Test do checkout positive case
     * */
    @Test
    @Throws(Exception::class)
    fun OnDoCheckoutThenSuccess_ShouldNavigateToThankYouPage() {
        //Given

        //When

        //Then
    }

    /**
     * Test do checkout negative case
     * */
    @Test
    @Throws(Exception::class)
    fun OnDoCheckoutThenFailed_ShouldShowError() {
        //Given

        //When

        //Then
    }

}