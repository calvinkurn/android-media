package com.tokopedia.product.manage.feature.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.product.manage.coroutine.TestCoroutineDispatchers
import com.tokopedia.product.manage.feature.filter.domain.GetProductListMetaUseCase
import com.tokopedia.product.manage.feature.list.domain.SetFeaturedProductUseCase
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModel
import com.tokopedia.product.manage.feature.multiedit.domain.MultiEditProductUseCase
import com.tokopedia.product.manage.feature.quickedit.delete.domain.DeleteProductUseCase
import com.tokopedia.product.manage.feature.quickedit.price.domain.EditPriceUseCase
import com.tokopedia.product.manage.feature.quickedit.stock.domain.EditStockUseCase
import com.tokopedia.product.manage.feature.quickedit.variant.domain.EditProductVariantUseCase
import com.tokopedia.product.manage.oldlist.domain.PopupManagerAddProductUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetProductListUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositGraphQLUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.*
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
                TestCoroutineDispatchers
        )
    }

    protected fun LiveData<*>.verifyValueEquals(expected: Any) {
        val actual = value
        assertEquals(expected, actual)
    }

    protected fun LiveData<*>.verifySuccessEquals(expected: Success<*>) {
        val expectedResult = expected.data
        val actualResult = (value as Success<*>).data
        assertEquals(expectedResult, actualResult)
    }

    protected fun LiveData<*>.verifyErrorEquals(expected: Fail) {
        val expectedResult = expected.throwable::class.java
        val actualResult = (value as Fail).throwable::class.java
        assertEquals(expectedResult, actualResult)
    }
}