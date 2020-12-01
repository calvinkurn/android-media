package com.tokopedia.shop.product.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.GetMerchantVoucherListUseCase
import com.tokopedia.shop.common.domain.GetShopFilterBottomSheetDataUseCase
import com.tokopedia.shop.common.domain.GetShopFilterProductCountUseCase
import com.tokopedia.shop.common.domain.GqlGetShopSortUseCase
import com.tokopedia.shop.common.domain.RestrictionEngineNplUseCase
import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoCacheUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.ClaimBenefitMembershipUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetMembershipUseCaseNew
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.product.di.ShopProductGetHighlightProductQualifier
import com.tokopedia.shop.product.domain.interactor.GetShopFeaturedProductUseCase
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.sort.domain.interactor.GetShopProductSortUseCase
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
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
    lateinit var getMerchantVoucherListUseCase: GetMerchantVoucherListUseCase
    @RelaxedMockK
    lateinit var getMembershipUseCase: GetMembershipUseCaseNew
    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface
    @RelaxedMockK
    lateinit var getShopFeaturedProductUseCase: GetShopFeaturedProductUseCase
    @RelaxedMockK
    lateinit var getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase
    @RelaxedMockK
    lateinit var addWishListUseCase: AddWishListUseCase
    @RelaxedMockK
    lateinit var getShopProductUseCase: GqlGetShopProductUseCase
    @RelaxedMockK
    @ShopProductGetHighlightProductQualifier
    lateinit var getShopHighlightProductUseCase: Provider<GqlGetShopProductUseCase>
    @RelaxedMockK
    lateinit var removeWishlistUseCase: RemoveWishListUseCase
    @RelaxedMockK
    lateinit var deleteShopInfoUseCase: DeleteShopInfoCacheUseCase
    @RelaxedMockK
    lateinit var getShopInfoUseCase: GQLGetShopInfoUseCase
    @RelaxedMockK
    lateinit var getShopProductFilterUseCase: GetShopProductSortUseCase
    @RelaxedMockK
    lateinit var getShopFilterBottomSheetDataUseCase: GetShopFilterBottomSheetDataUseCase
    @RelaxedMockK
    lateinit var getShopFilterProductCountUseCase: GetShopFilterProductCountUseCase
    @RelaxedMockK
    lateinit var gqlGetShopSortUseCase: GqlGetShopSortUseCase
    @RelaxedMockK
    lateinit var userSession: UserSessionInterface
    @RelaxedMockK
    lateinit var shopProductSortMapper: ShopProductSortMapper
    @RelaxedMockK
    lateinit var restrictionEngineNplUseCase: RestrictionEngineNplUseCase
    @RelaxedMockK
    lateinit var toggleFavouriteShopUseCase: Lazy<ToggleFavouriteShopUseCase>

    protected lateinit var viewModelShopPageProductListViewModel: ShopPageProductListViewModel
    protected lateinit var viewModelShopPageProductListResultViewModel: ShopPageProductListResultViewModel
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
                getMerchantVoucherListUseCase,
                getMembershipUseCase,
                userSessionInterface,
                getShopFeaturedProductUseCase,
                getShopEtalaseByShopUseCase,
                addWishListUseCase,
                getShopProductUseCase,
                getShopHighlightProductUseCase,
                removeWishlistUseCase,
                deleteShopInfoUseCase,
                testCoroutineDispatcherProvider,
                getShopFilterBottomSheetDataUseCase,
                getShopFilterProductCountUseCase,
                gqlGetShopSortUseCase,
                shopProductSortMapper
        )

        viewModelShopPageProductListResultViewModel = ShopPageProductListResultViewModel(
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
                toggleFavouriteShopUseCase
        )
    }
}