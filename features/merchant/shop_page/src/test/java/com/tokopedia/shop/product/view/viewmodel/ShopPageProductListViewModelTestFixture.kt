package com.tokopedia.shop.product.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.GetMerchantVoucherListUseCase
import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoCacheUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.ClaimBenefitMembershipUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetMembershipUseCaseNew
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.product.di.ShopProductGetHighlightProductQualifier
import com.tokopedia.shop.product.domain.interactor.GetShopFeaturedProductUseCase
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
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
    lateinit var userSession: UserSessionInterface
    @RelaxedMockK
    lateinit var getShopInfoUseCase: GQLGetShopInfoUseCase

    protected lateinit var viewModelShopPageProductListViewModel: ShopPageProductListViewModel
    protected lateinit var viewModelShopPageProductListResultViewModel: ShopPageProductListResultViewModel

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
                Dispatchers.Unconfined
        )

        viewModelShopPageProductListResultViewModel = ShopPageProductListResultViewModel(
                userSessionInterface,
                getShopInfoUseCase,
                getShopEtalaseByShopUseCase,
                getShopProductUseCase,
                addWishListUseCase,
                removeWishlistUseCase,
                Dispatchers.Unconfined
        )
    }
}