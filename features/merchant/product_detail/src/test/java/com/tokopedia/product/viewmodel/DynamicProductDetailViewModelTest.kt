package com.tokopedia.product.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliatecommon.domain.TrackAffiliateUseCase
import com.tokopedia.product.detail.updatecartcounter.interactor.UpdateCartCounterUseCase
import com.tokopedia.product.detail.usecase.*
import com.tokopedia.product.detail.view.viewmodel.DynamicProductDetailViewModel
import com.tokopedia.product.warehouse.model.ProductActionSubmit
import com.tokopedia.purchase_platform.common.usecase.SubmitHelpTicketUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Matchers.anyBoolean
import org.mockito.Matchers.anyString


@ExperimentalCoroutinesApi
class DynamicProductDetailViewModelTest {

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface
    @RelaxedMockK
    lateinit var stickyLoginUseCase: StickyLoginUseCase
    @RelaxedMockK
    lateinit var getPdpLayoutUseCase: GetPdpLayoutUseCase
    @RelaxedMockK
    lateinit var getProductInfoP2ShopUseCase: GetProductInfoP2ShopUseCase
    @RelaxedMockK
    lateinit var getProductInfoP2LoginUseCase: GetProductInfoP2LoginUseCase
    @RelaxedMockK
    lateinit var getProductInfoP2GeneralUseCase: GetProductInfoP2GeneralUseCase
    @RelaxedMockK
    lateinit var getProductInfoP3UseCase: GetProductInfoP3UseCase
    @RelaxedMockK
    lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase
    @RelaxedMockK
    lateinit var removeWishlistUseCase: RemoveWishListUseCase
    @RelaxedMockK
    lateinit var addWishListUseCase: AddWishListUseCase
    @RelaxedMockK
    lateinit var getRecommendationUseCase: GetRecommendationUseCase
    @RelaxedMockK
    lateinit var moveProductToWarehouseUseCase: MoveProductToWarehouseUseCase
    @RelaxedMockK
    lateinit var moveProductToEtalaseUseCase: MoveProductToEtalaseUseCase
    @RelaxedMockK
    lateinit var trackAffiliateUseCase: TrackAffiliateUseCase
    @RelaxedMockK
    lateinit var submitHelpTicketUseCase: SubmitHelpTicketUseCase
    @RelaxedMockK
    lateinit var updateCartCounterUseCase: UpdateCartCounterUseCase

    private val dispatcher by lazy {
        Dispatchers.Unconfined
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private val viewModel by lazy {
        DynamicProductDetailViewModel(dispatcher, stickyLoginUseCase, getPdpLayoutUseCase, getProductInfoP2ShopUseCase, getProductInfoP2LoginUseCase, getProductInfoP2GeneralUseCase, getProductInfoP3UseCase, toggleFavoriteUseCase, removeWishlistUseCase, addWishListUseCase, getRecommendationUseCase,
                moveProductToWarehouseUseCase, moveProductToEtalaseUseCase, trackAffiliateUseCase, submitHelpTicketUseCase, updateCartCounterUseCase, userSessionInterface)
    }


//    @Test
//    fun getStickyLoginTest() {
//        verify {
//            stickyLoginUseCase.setParams(StickyLoginConstant.Page.PDP)
//        }
//    }

    /**
     * ToggleFavorite
     */
    @Test
    fun onSuccessToggleFavoriteShop() {
        val shopId = "1234"
        coEvery {
            toggleFavoriteUseCase.executeOnBackground().followShop.isSuccess
        } returns anyBoolean()

        viewModel.toggleFavorite(shopId)

        verify {
            toggleFavoriteUseCase.createRequestParam(shopId)
        }
        coVerify {
            toggleFavoriteUseCase.executeOnBackground()
        }

        Assert.assertEquals((viewModel.toggleFavoriteResult.value as Success).data, anyBoolean())
    }


    @Test
    fun onErrorToggleFavoriteShop() {
        val shopId = "1234"
        coEvery {
            toggleFavoriteUseCase.executeOnBackground().followShop.isSuccess
        } throws Throwable()

        viewModel.toggleFavorite(shopId)

        verify {
            toggleFavoriteUseCase.createRequestParam(shopId)
        }
        coVerify {
            toggleFavoriteUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.toggleFavoriteResult.value is Fail)
    }

    /**
     * MoveToWareHouse
     */
    @Test
    fun onSuccessMoveProductToWareHouse() {
        val productId = "123"
        val productActionSubmit = ProductActionSubmit()

        coEvery {
            moveProductToWarehouseUseCase.executeOnBackground()
        } returns productActionSubmit

        viewModel.moveProductToWareHouse(productId)
        coVerify {
            moveProductToWarehouseUseCase.executeOnBackground()
        }

        Assert.assertEquals((viewModel.moveToWarehouseResult.value as Success).data, anyBoolean())
    }

    @Test
    fun onErrorMoveProductToWareHouse() {

        //Given
        coEvery {
            moveProductToWarehouseUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.moveProductToWareHouse(anyString())

        verify {
            moveProductToWarehouseUseCase.createParams(anyString(), anyString(), anyString())
        }
        coVerify {
            moveProductToWarehouseUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.moveToWarehouseResult.value is Fail)
    }

    /**
     * MoveToEtalase
     */
    @Test
    fun onSuccessMoveProductToEtalase() {
        coEvery {
            moveProductToEtalaseUseCase.executeOnBackground()
        } returns ProductActionSubmit()

        viewModel.moveProductToEtalase(anyString(), anyString(), anyString())

        verify {
            moveProductToEtalaseUseCase.createParams(anyString(), anyString(), anyString(), anyString(), anyString())
        }

        coVerify {
            moveProductToEtalaseUseCase.executeOnBackground()
        }

        Assert.assertEquals((viewModel.moveToEtalaseResult.value as Success).data, anyBoolean())
    }

    @Test
    fun onErrorMoveProductToEtalase() {
        coEvery {
            moveProductToEtalaseUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.moveProductToEtalase(anyString(), anyString(), anyString())

        verify {
            moveProductToEtalaseUseCase.createParams(anyString(), anyString(), anyString(), anyString(), anyString())
        }

        coVerify {
            moveProductToEtalaseUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.moveToEtalaseResult.value is Fail)
    }

    /**
     * RecommendationWidget
     */
    @Test
    fun onSuccessLoadRecommendation() {
        val listOfRecom = arrayListOf(RecommendationWidget(), RecommendationWidget())
        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns listOfRecom

        viewModel.loadRecommendation()

        coVerify {
            getRecommendationUseCase.createObservable(any())
        }

        Assert.assertEquals((viewModel.loadTopAdsProduct.value as Success).data, listOfRecom)
    }

    @Test
    fun onErrorLoadRecommendation() {
        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } throws Throwable()

        viewModel.loadRecommendation()

        coVerify {
            getRecommendationUseCase.createObservable(any())
        }

        Assert.assertTrue(viewModel.loadTopAdsProduct.value is Fail)
    }


}