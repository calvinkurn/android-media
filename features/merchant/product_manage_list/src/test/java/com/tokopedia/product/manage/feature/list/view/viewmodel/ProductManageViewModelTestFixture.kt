package com.tokopedia.product.manage.feature.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.common.feature.quickedit.stock.domain.EditStockUseCase
import com.tokopedia.product.manage.common.feature.variant.domain.EditProductVariantUseCase
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductListMetaUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.product.manage.feature.list.domain.PopupManagerAddProductUseCase
import com.tokopedia.product.manage.feature.list.domain.SetFeaturedProductUseCase
import com.tokopedia.product.manage.feature.multiedit.domain.MultiEditProductUseCase
import com.tokopedia.product.manage.feature.quickedit.delete.domain.DeleteProductUseCase
import com.tokopedia.product.manage.feature.quickedit.price.domain.EditPriceUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetProductListUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GetShopInfoTopAdsUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositGraphQLUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

open class ProductManageViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var editStockUseCase: EditStockUseCase
    @RelaxedMockK
    lateinit var editPriceUseCase: EditPriceUseCase
    @RelaxedMockK
    lateinit var gqlGetShopInfoUseCase: GQLGetShopInfoUseCase
    @RelaxedMockK
    lateinit var geetShopInfoTopAdsUseCase: GetShopInfoTopAdsUseCase
    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface
    @RelaxedMockK
    lateinit var topAdsGetShopDepositGraphQLUseCase: TopAdsGetShopDepositGraphQLUseCase
    @RelaxedMockK
    lateinit var popupManagerAddProductUseCase: PopupManagerAddProductUseCase
    @RelaxedMockK
    lateinit var getProductListUseCase: GQLGetProductListUseCase
    @RelaxedMockK
    lateinit var setFeaturedProductUseCase: SetFeaturedProductUseCase
    @RelaxedMockK
    lateinit var deleteProductUseCase: DeleteProductUseCase
    @RelaxedMockK
    lateinit var multiEditProductUseCase: MultiEditProductUseCase
    @RelaxedMockK
    lateinit var getProductListMetaUseCase: GetProductListMetaUseCase
    @RelaxedMockK
    lateinit var editProductVariantUseCase: EditProductVariantUseCase

    protected lateinit var viewModel: ProductManageViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = ProductManageViewModel(
            editPriceUseCase,
            gqlGetShopInfoUseCase,
            geetShopInfoTopAdsUseCase,
            userSessionInterface,
            topAdsGetShopDepositGraphQLUseCase,
            popupManagerAddProductUseCase,
            getProductListUseCase,
            setFeaturedProductUseCase,
            editStockUseCase,
            deleteProductUseCase,
            multiEditProductUseCase,
            getProductListMetaUseCase,
            editProductVariantUseCase,
            CoroutineTestDispatchersProvider
        )
    }
}