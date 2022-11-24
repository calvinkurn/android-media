package com.tokopedia.product.viewmodel

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliatecommon.domain.TrackAffiliateUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartOcsUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.config.GlobalConfig
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.product.detail.common.usecase.ToggleFavoriteUseCase
import com.tokopedia.product.detail.tracking.ProductDetailServerLogger
import com.tokopedia.product.detail.usecase.DiscussionMostHelpfulUseCase
import com.tokopedia.product.detail.usecase.GetP2DataAndMiniCartUseCase
import com.tokopedia.product.detail.usecase.GetPdpLayoutUseCase
import com.tokopedia.product.detail.usecase.GetProductInfoP2LoginUseCase
import com.tokopedia.product.detail.usecase.GetProductInfoP2OtherUseCase
import com.tokopedia.product.detail.usecase.GetProductRecommendationUseCase
import com.tokopedia.product.detail.usecase.ToggleNotifyMeUseCase
import com.tokopedia.product.detail.view.viewmodel.DynamicProductDetailViewModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.topads.sdk.domain.interactor.GetTopadsIsAdsUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.track.TrackApp
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.lang.reflect.Field
import java.lang.reflect.Modifier

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
    lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase

    @RelaxedMockK
    lateinit var deleteWishlistV2UseCase: DeleteWishlistV2UseCase

    @RelaxedMockK
    lateinit var addToWishlistV2UseCase: AddToWishlistV2UseCase

    @RelaxedMockK
    lateinit var trackAffiliateUseCase: TrackAffiliateUseCase

    @RelaxedMockK
    lateinit var updateCartCounterUseCase: UpdateCartCounterUseCase

    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase

    @RelaxedMockK
    lateinit var addToCartOcsUseCase: AddToCartOcsUseCase

    @RelaxedMockK
    lateinit var addToCartOccUseCase: AddToCartOccMultiUseCase

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
    lateinit var getTopadsIsAdsUseCase: GetTopadsIsAdsUseCase

    @RelaxedMockK
    lateinit var updateCartUseCase: UpdateCartUseCase

    @RelaxedMockK
    lateinit var deleteCartUseCase: DeleteCartUseCase

    @RelaxedMockK
    lateinit var playWidgetTools: PlayWidgetTools

    @RelaxedMockK
    lateinit var remoteConfigInstance: FirebaseRemoteConfigImpl

    @RelaxedMockK
    lateinit var getProductRecommendationUseCase: GetProductRecommendationUseCase

    @RelaxedMockK
    lateinit var getRecommendationUseCase: GetRecommendationUseCase

    @RelaxedMockK
    lateinit var affiliateCookieHelper: AffiliateCookieHelper

    lateinit var spykViewModel: DynamicProductDetailViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic(RemoteConfigInstance::class)
        mockkStatic(GlobalConfig::class)
        mockkObject(ProductDetailServerLogger)
        mockkStatic(TrackApp::class)
        normalizeOs()
        spykViewModel = spyk(viewModel)
    }

    private fun normalizeOs() {
        setOS(25)
    }

    fun setOS(newValue: Any?) {
        val field = Build.VERSION::class.java.getField("SDK_INT")
        field.isAccessible = true
        val modifiersField: Field = Field::class.java.getDeclaredField("modifiers")
        modifiersField.isAccessible = true
        modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
        field.set(null, newValue)
    }

    @After
    fun after() {
        unmockkAll()
    }

    val viewModel by lazy {
        createViewModel()
    }

    fun createViewModel(): DynamicProductDetailViewModel {
        return DynamicProductDetailViewModel(CoroutineTestDispatchersProvider,
                { getPdpLayoutUseCase },
                { getProductInfoP2LoginUseCase },
                { getProductInfoP2OtherUseCase },
                { getP2DataAndMiniCartUseCase },
                { toggleFavoriteUseCase },
                { deleteWishlistV2UseCase },
                { addToWishlistV2UseCase },
                { getProductRecommendationUseCase },
                { getRecommendationUseCase },
                { trackAffiliateUseCase },
                { updateCartCounterUseCase },
                { addToCartUseCase },
                { addToCartOcsUseCase },
                { addToCartOccUseCase },
                { toggleNotifyMeUseCase },
                { discussionMostHelpfulUseCase },
                { topAdsImageViewUseCase },
                { miniCartListSimplifiedUseCase },
                { updateCartUseCase },
                { deleteCartUseCase },
                { getTopadsIsAdsUseCase },
                playWidgetTools,
                remoteConfigInstance,
                userSessionInterface,
                { affiliateCookieHelper }
        )
    }
}