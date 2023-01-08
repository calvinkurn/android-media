package com.tokopedia.shop.product.view.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.shop.common.domain.GetShopFilterBottomSheetDataUseCase
import com.tokopedia.shop.common.domain.GetShopFilterProductCountUseCase
import com.tokopedia.shop.common.domain.GqlGetShopSortUseCase
import com.tokopedia.shop.common.domain.RestrictionEngineNplUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GetFollowStatusUseCase
import com.tokopedia.shop.common.domain.interactor.GqlShopPageGetDynamicTabUseCase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.product.domain.interactor.ClaimBenefitMembershipUseCase
import com.tokopedia.shop.product.domain.interactor.GetMembershipUseCaseNew
import com.tokopedia.shop.product.domain.interactor.GetShopFeaturedProductUseCase
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import javax.inject.Provider

@ExperimentalCoroutinesApi
abstract class ShopPageProductListViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var claimBenefitMembershipUseCase: ClaimBenefitMembershipUseCase

    @RelaxedMockK
    lateinit var mvcSummaryUseCase: MVCSummaryUseCase

    @RelaxedMockK
    lateinit var getMembershipUseCase: GetMembershipUseCaseNew

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var getShopFeaturedProductUseCase: GetShopFeaturedProductUseCase

    @RelaxedMockK
    lateinit var getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase

    @RelaxedMockK
    lateinit var getShopProductUseCase: GqlGetShopProductUseCase

    @RelaxedMockK
    lateinit var getShopHighlightProductUseCase: Provider<GqlGetShopProductUseCase>

    @RelaxedMockK
    lateinit var getShopInfoUseCase: GQLGetShopInfoUseCase

    @RelaxedMockK
    lateinit var getShopFilterBottomSheetDataUseCase: GetShopFilterBottomSheetDataUseCase

    @RelaxedMockK
    lateinit var getShopFilterProductCountUseCase: GetShopFilterProductCountUseCase

    @RelaxedMockK
    lateinit var gqlGetShopSortUseCase: GqlGetShopSortUseCase

    @RelaxedMockK
    lateinit var shopProductSortMapper: ShopProductSortMapper

    @RelaxedMockK
    lateinit var restrictionEngineNplUseCase: RestrictionEngineNplUseCase

    @RelaxedMockK
    lateinit var toggleFavouriteShopUseCase: Lazy<ToggleFavouriteShopUseCase>

    @RelaxedMockK
    lateinit var getFollowStatusUseCase: GetFollowStatusUseCase

    @RelaxedMockK
    lateinit var gqlShopPageGetDynamicTabUseCase: GqlShopPageGetDynamicTabUseCase

    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase

    @RelaxedMockK
    lateinit var updateCartUseCase: UpdateCartUseCase

    @RelaxedMockK
    lateinit var deleteCartUseCase: DeleteCartUseCase

    @RelaxedMockK
    lateinit var sharedPreferences: SharedPreferences
    @RelaxedMockK
    lateinit var context: Context

    @RelaxedMockK
    lateinit var affiliateCookieHelper: AffiliateCookieHelper

    protected lateinit var viewModelShopPageProductListViewModel: ShopPageProductListViewModel
    protected lateinit var shopPageProductListResultViewModel: ShopPageProductListResultViewModel
    protected val addressWidgetData: LocalCacheModel = LocalCacheModel()
    protected val mockProductPerPage = 10
    protected val mockIsDirectPurchaseTrue = true
    protected val mockIsDirectPurchaseFalse = false
    protected val mockShopId = "123"
    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkObject(GetMembershipUseCaseNew)
        mockkObject(GQLGetShopInfoUseCase)
        mockkObject(GqlGetShopProductUseCase)
        mockkObject(GetShopFeaturedProductUseCase)

        viewModelShopPageProductListViewModel = ShopPageProductListViewModel(
            claimBenefitMembershipUseCase,
            mvcSummaryUseCase,
            getMembershipUseCase,
            userSessionInterface,
            getShopFeaturedProductUseCase,
            getShopEtalaseByShopUseCase,
            getShopProductUseCase,
            getShopHighlightProductUseCase,
            testCoroutineDispatcherProvider,
            getShopFilterBottomSheetDataUseCase,
            getShopFilterProductCountUseCase,
            gqlGetShopSortUseCase,
            shopProductSortMapper,
            addToCartUseCase,
            updateCartUseCase,
            deleteCartUseCase
        )

        shopPageProductListResultViewModel = ShopPageProductListResultViewModel(
                userSessionInterface,
                getShopInfoUseCase,
                getShopEtalaseByShopUseCase,
                getShopProductUseCase,
                gqlGetShopSortUseCase,
                shopProductSortMapper,
                testCoroutineDispatcherProvider,
                getShopFilterBottomSheetDataUseCase,
                getShopFilterProductCountUseCase,
                restrictionEngineNplUseCase,
                toggleFavouriteShopUseCase,
                getFollowStatusUseCase,
                gqlShopPageGetDynamicTabUseCase,
                addToCartUseCase,
                updateCartUseCase,
                deleteCartUseCase,
                sharedPreferences
        )
    }
}
