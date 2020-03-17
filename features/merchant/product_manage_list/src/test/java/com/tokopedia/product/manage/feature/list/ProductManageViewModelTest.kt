package com.tokopedia.product.manage.feature.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.feature.cashback.domain.SetCashbackUseCase
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModel
import com.tokopedia.product.manage.feature.multiedit.domain.MultiEditProductUseCase
import com.tokopedia.product.manage.feature.quickedit.delete.domain.DeleteProductUseCase
import com.tokopedia.product.manage.feature.quickedit.price.domain.EditPriceUseCase
import com.tokopedia.product.manage.feature.quickedit.stock.domain.EditStockUseCase
import com.tokopedia.product.manage.oldlist.domain.EditFeaturedProductUseCase
import com.tokopedia.product.manage.oldlist.domain.PopupManagerAddProductUseCase
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.shop.common.domain.interactor.GQLGetProductListUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositGraphQLUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductManageViewModelTest {

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

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher by lazy {
        Dispatchers.Unconfined
    }

    private val viewModel by lazy {
        ProductManageViewModel(
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
                dispatcher
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `editPrice should execute expected use case`() {
        mockkObject(editPriceUseCase)
        coEvery {
            editPriceUseCase.executeOnBackground()
        }
        val productId = "0"
        val price = "10000"
        val productName = "Amazing Product"
        viewModel.editPrice(productId, price, productName)
        coVerify {
            editPriceUseCase.executeOnBackground()
        }
    }

    @Test
    fun `editStock should execute expected use case`() {
        mockkObject(editStockUseCase)
        coEvery {
            editStockUseCase.executeOnBackground()
        }
        val productId = "0"
        val stock = 0
        val productName = "Amazing Product"
        val status = ProductStatus.ACTIVE
        viewModel.editStock(productId, stock, productName, status)
        coVerify {
            editStockUseCase.executeOnBackground()
        }
    }

    @Test
    fun `deleteProduct should execute expected use case`() {
        mockkObject(deleteProductUseCase)
        coEvery {
            deleteProductUseCase.executeOnBackground()
        }
        val productId = "0"
        val productName = "Amazing Product"
        viewModel.deleteSingleProduct(productName, productId)
        coVerify {
            deleteProductUseCase.executeOnBackground()
        }
    }
}