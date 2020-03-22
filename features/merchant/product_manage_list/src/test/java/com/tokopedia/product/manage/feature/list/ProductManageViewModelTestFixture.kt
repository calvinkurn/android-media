package com.tokopedia.product.manage.feature.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.feature.filter.domain.GetProductListMetaUseCase
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModel
import com.tokopedia.product.manage.feature.multiedit.domain.MultiEditProductUseCase
import com.tokopedia.product.manage.feature.quickedit.delete.domain.DeleteProductUseCase
import com.tokopedia.product.manage.feature.quickedit.price.domain.EditPriceUseCase
import com.tokopedia.product.manage.feature.quickedit.stock.domain.EditStockUseCase
import com.tokopedia.product.manage.oldlist.domain.EditFeaturedProductUseCase
import com.tokopedia.product.manage.oldlist.domain.PopupManagerAddProductUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetProductListUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositGraphQLUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule

abstract class ProductManageViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var editStockUseCase: EditStockUseCase
    @RelaxedMockK
    lateinit var editPriceUseCase: EditPriceUseCase
    @RelaxedMockK
    lateinit var gqlGetShopInfoUseCase: GQLGetShopInfoUseCase
    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface
    @RelaxedMockK
    lateinit var topAdsGetShopDepositGraphQLUseCase: TopAdsGetShopDepositGraphQLUseCase
    @RelaxedMockK
    lateinit var popupManagerAddProductUseCase: PopupManagerAddProductUseCase
    @RelaxedMockK
    lateinit var getProductListUseCase: GQLGetProductListUseCase
    @RelaxedMockK
    lateinit var editFeaturedProductUseCase: EditFeaturedProductUseCase
    @RelaxedMockK
    lateinit var deleteProductUseCase: DeleteProductUseCase
    @RelaxedMockK
    lateinit var multiEditProductUseCase: MultiEditProductUseCase
    @RelaxedMockK
    lateinit var getProductListMetaUseCase: GetProductListMetaUseCase

    protected lateinit var viewModel: ProductManageViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = ProductManageViewModel(
                editPriceUseCase,
                gqlGetShopInfoUseCase,
                userSessionInterface,
                topAdsGetShopDepositGraphQLUseCase,
                popupManagerAddProductUseCase,
                getProductListUseCase,
                editFeaturedProductUseCase,
                editStockUseCase,
                deleteProductUseCase,
                multiEditProductUseCase,
                getProductListMetaUseCase,
                Dispatchers.Unconfined
        )
    }


}