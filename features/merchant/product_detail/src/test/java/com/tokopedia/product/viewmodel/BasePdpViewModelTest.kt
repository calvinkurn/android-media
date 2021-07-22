package com.tokopedia.product.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliatecommon.domain.TrackAffiliateUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartOcsUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.minicart.common.domain.usecase.DeleteCartUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.UpdateCartUseCase
import com.tokopedia.product.detail.usecase.*
import com.tokopedia.product.detail.view.viewmodel.DynamicProductDetailViewModel
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import io.mockk.spyk
import org.junit.After
import org.junit.Before
import org.junit.Rule

/**
 * Created by Yehezkiel on 08/06/21
 */
abstract class BasePdpViewModelTest {

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var getPdpLayoutUseCase: GetPdpLayoutUseCase

    @RelaxedMockK
    lateinit var getProductInfoP2LoginUseCase: GetProductInfoP2LoginUseCase

    @RelaxedMockK
    lateinit var getProductInfoP2OtherUseCase: GetProductInfoP2OtherUseCase

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

    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase

    @RelaxedMockK
    lateinit var addToCartOcsUseCase: AddToCartOcsUseCase

    @RelaxedMockK
    lateinit var addToCartOccUseCase: AddToCartOccUseCase

    @RelaxedMockK
    lateinit var toggleNotifyMeUseCase: ToggleNotifyMeUseCase

    @RelaxedMockK
    lateinit var discussionMostHelpfulUseCase: DiscussionMostHelpfulUseCase

    @RelaxedMockK
    lateinit var getP2DataAndMiniCartUseCase: GetP2DataAndMiniCartUseCase

    @RelaxedMockK
    lateinit var topAdsImageViewUseCase: TopAdsImageViewUseCase

    @RelaxedMockK
    lateinit var getRecommendationFilterChips: GetRecommendationFilterChips

    @RelaxedMockK
    lateinit var miniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase

    @RelaxedMockK
    lateinit var updateCartUseCase: UpdateCartUseCase

    @RelaxedMockK
    lateinit var deleteCartUseCase: DeleteCartUseCase

    lateinit var spykViewModel: DynamicProductDetailViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic(RemoteConfigInstance::class)
        spykViewModel = spyk(DynamicProductDetailViewModel(CoroutineTestDispatchersProvider, Lazy { getPdpLayoutUseCase }, Lazy { getProductInfoP2LoginUseCase }, Lazy { getProductInfoP2OtherUseCase }, Lazy { getP2DataAndMiniCartUseCase }, Lazy { getProductInfoP3UseCase }, Lazy { toggleFavoriteUseCase }, Lazy { removeWishlistUseCase }, Lazy { addWishListUseCase }, Lazy { getRecommendationUseCase },
                Lazy { getRecommendationFilterChips }, Lazy { moveProductToWarehouseUseCase }, Lazy { moveProductToEtalaseUseCase }, Lazy { trackAffiliateUseCase }, Lazy { submitHelpTicketUseCase }, Lazy { updateCartCounterUseCase }, Lazy { addToCartUseCase }, Lazy { addToCartOcsUseCase }, Lazy { addToCartOccUseCase }, Lazy { toggleNotifyMeUseCase }, Lazy { discussionMostHelpfulUseCase }, Lazy { topAdsImageViewUseCase },
                Lazy { miniCartListSimplifiedUseCase }, Lazy { updateCartUseCase }, Lazy { deleteCartUseCase },  userSessionInterface))
    }

    @After
    fun setupAfter() {
        viewModel.productInfoP3.removeObserver { }
    }

    val viewModel by lazy {
        createViewModel()
    }

    private fun createViewModel(): DynamicProductDetailViewModel {
        return DynamicProductDetailViewModel(CoroutineTestDispatchersProvider, Lazy { getPdpLayoutUseCase }, Lazy { getProductInfoP2LoginUseCase }, Lazy { getProductInfoP2OtherUseCase }, Lazy { getP2DataAndMiniCartUseCase }, Lazy { getProductInfoP3UseCase }, Lazy { toggleFavoriteUseCase }, Lazy { removeWishlistUseCase }, Lazy { addWishListUseCase }, Lazy { getRecommendationUseCase },
                Lazy { getRecommendationFilterChips }, Lazy { moveProductToWarehouseUseCase }, Lazy { moveProductToEtalaseUseCase }, Lazy { trackAffiliateUseCase }, Lazy { submitHelpTicketUseCase }, Lazy { updateCartCounterUseCase }, Lazy { addToCartUseCase }, Lazy { addToCartOcsUseCase }, Lazy { addToCartOccUseCase }, Lazy { toggleNotifyMeUseCase }, Lazy { discussionMostHelpfulUseCase }, Lazy { topAdsImageViewUseCase },
                Lazy { miniCartListSimplifiedUseCase }, Lazy { updateCartUseCase }, Lazy { deleteCartUseCase }, userSessionInterface)
    }
}