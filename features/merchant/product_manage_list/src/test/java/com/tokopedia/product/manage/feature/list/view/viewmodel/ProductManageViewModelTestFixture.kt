package com.tokopedia.product.manage.feature.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.common.feature.quickedit.stock.domain.EditStatusUseCase
import com.tokopedia.product.manage.common.feature.variant.domain.EditProductVariantUseCase
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductListMetaUseCase
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductManageAccessUseCase
import com.tokopedia.product.manage.common.feature.variant.domain.GetProductVariantUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.product.manage.feature.list.domain.GetShopManagerPopupsUseCase
import com.tokopedia.product.manage.feature.list.domain.SetFeaturedProductUseCase
import com.tokopedia.product.manage.feature.list.view.datasource.TickerStaticDataProvider
import com.tokopedia.product.manage.feature.multiedit.domain.MultiEditProductUseCase
import com.tokopedia.product.manage.feature.quickedit.delete.domain.DeleteProductUseCase
import com.tokopedia.product.manage.feature.quickedit.price.domain.EditPriceUseCase
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption
import com.tokopedia.shop.common.domain.interactor.GQLGetProductListUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GetAdminInfoShopLocationUseCase
import com.tokopedia.shop.common.domain.interactor.GetShopInfoTopAdsUseCase
import com.tokopedia.shop.common.domain.interactor.UpdateProductStockWarehouseUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

open class ProductManageViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var editStatusUseCase: EditStatusUseCase

    @RelaxedMockK
    lateinit var editStockUseCase: UpdateProductStockWarehouseUseCase

    @RelaxedMockK
    lateinit var editPriceUseCase: EditPriceUseCase

    @RelaxedMockK
    lateinit var gqlGetShopInfoUseCase: GQLGetShopInfoUseCase

    @RelaxedMockK
    lateinit var getShopInfoTopAdsUseCase: GetShopInfoTopAdsUseCase

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var getShopManagerPopupsUseCase: GetShopManagerPopupsUseCase

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

    @RelaxedMockK
    lateinit var getProductVariantUseCase: GetProductVariantUseCase

    @RelaxedMockK
    lateinit var getProductManageAccessUseCase: GetProductManageAccessUseCase

    @RelaxedMockK
    lateinit var getAdminInfoShopLocationUseCase: GetAdminInfoShopLocationUseCase

    @RelaxedMockK
    lateinit var tickerStaticDataProvider: TickerStaticDataProvider

    protected lateinit var viewModel: ProductManageViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = ProductManageViewModel(
                editPriceUseCase,
                gqlGetShopInfoUseCase,
                getShopInfoTopAdsUseCase,
                userSessionInterface,
                getShopManagerPopupsUseCase,
                getProductListUseCase,
                setFeaturedProductUseCase,
                editStatusUseCase,
                editStockUseCase,
                deleteProductUseCase,
                multiEditProductUseCase,
                getProductListMetaUseCase,
                getProductManageAccessUseCase,
                editProductVariantUseCase,
                getProductVariantUseCase,
                getAdminInfoShopLocationUseCase,
                tickerStaticDataProvider,
                CoroutineTestDispatchersProvider
        )
    }

    protected fun createFilterOptions(page: Int): MutableList<FilterOption> {
        val selectedFilter = viewModel.selectedFilterAndSort.value
        val filterOptions = selectedFilter?.filterOptions.orEmpty().toMutableList()

        filterOptions.add(FilterOption.FilterByPage(page))
        return filterOptions
    }
}