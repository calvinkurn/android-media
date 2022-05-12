package com.tokopedia.vouchercreation.product.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.common.utils.ResourceProvider
import com.tokopedia.vouchercreation.product.list.domain.model.response.*
import com.tokopedia.vouchercreation.product.list.domain.usecase.*
import com.tokopedia.vouchercreation.product.list.view.model.ProductUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddProductViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    private lateinit var mViewModel: AddProductViewModel

    @RelaxedMockK
    lateinit var resourceProvider: ResourceProvider

    @RelaxedMockK
    lateinit var getProductListUseCase: GetProductListUseCase

    @RelaxedMockK
    lateinit var validateVoucherUseCase: ValidateVoucherUseCase

    @RelaxedMockK
    lateinit var getWarehouseLocationsUseCase: GetWarehouseLocationsUseCase

    @RelaxedMockK
    lateinit var getShowCasesByIdUseCase: GetShowCasesByIdUseCase

    @RelaxedMockK
    lateinit var getProductListMetaDataUseCase: GetProductListMetaDataUseCase

    @RelaxedMockK
    lateinit var productListMetaUiModel: ProductListMetaResponse

    @RelaxedMockK
    lateinit var voucherValidationPartialUiModel: VoucherValidationPartialResponse

    @RelaxedMockK
    lateinit var productListUiModel: ProductListResponse

    @RelaxedMockK
    lateinit var shopLocGetWarehouseByShopIdsUiModel: ShopLocGetWarehouseByShopIdsResponse

    @RelaxedMockK
    lateinit var shopShowcasesByShopIdUiModel: ShopShowcasesByShopIdResponse

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mViewModel = AddProductViewModel(
            resourceProvider,
            testCoroutineDispatcherProvider,
            getProductListUseCase,
            validateVoucherUseCase,
            getWarehouseLocationsUseCase,
            getShowCasesByIdUseCase,
            getProductListMetaDataUseCase
        )
    }

    @After
    fun cleanup() {

    }

    @Test
    fun `success get product list`() {
        with(mViewModel) {
            val dummyResult = productListUiModel

            coEvery {
                getProductListUseCase.executeOnBackground()
            } returns dummyResult

            getProductList(
                1,
                "test",
                "1",
                warehouseLocationId = 0,
                shopShowCaseIds = listOf(),
                categoryList = listOf(),
                sort = null
            )

            coVerify {
                getProductListUseCase.executeOnBackground()
            }

            assert(productListResult.value == Success(dummyResult))
        }
    }

    @Test
    fun `fail get product list`() {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                getProductListUseCase.executeOnBackground()
            } throws dummyThrowable

            getProductList()

            coVerify {
                getProductListUseCase.executeOnBackground()
            }

            assert(productListResult.value is Fail)
        }
    }

    @Test
    fun `success validate product list`() {
        with(mViewModel) {
            val dummyResult = voucherValidationPartialUiModel

            coEvery {
                validateVoucherUseCase.executeOnBackground()
            } returns dummyResult

            validateProductList(
                benefitType = "",
                couponType = "",
                benefitIdr = 10,
                benefitMax = 10,
                benefitPercent = 10,
                minPurchase = 10,
                productIds = listOf()
            )

            coVerify {
                validateVoucherUseCase.executeOnBackground()
            }

            assert(validateVoucherResult.value == Success(dummyResult))
        }
    }

    @Test
    fun `fail validate product list`() {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                validateVoucherUseCase.executeOnBackground()
            } throws dummyThrowable

            validateProductList(
                benefitType = "",
                couponType = "",
                benefitIdr = 10,
                benefitMax = 10,
                benefitPercent = 10,
                minPurchase = 10,
                productIds = listOf()
            )

            coVerify {
                validateVoucherUseCase.executeOnBackground()
            }

            assert(validateVoucherResult.value is Fail)
        }
    }

    @Test
    fun `success get warehouse location`() {
        with(mViewModel) {
            val dummyResult = shopLocGetWarehouseByShopIdsUiModel

            coEvery {
                getWarehouseLocationsUseCase.executeOnBackground()
            } returns dummyResult

            getWarehouseLocations(0)

            coVerify {
                getWarehouseLocationsUseCase.executeOnBackground()
            }

            assert(getWarehouseLocationsResult.value == Success(dummyResult))
        }
    }

    @Test
    fun `fail get warehouse location`() {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                getWarehouseLocationsUseCase.executeOnBackground()
            } throws  dummyThrowable

            getWarehouseLocations(0)

            coVerify {
                getWarehouseLocationsUseCase.executeOnBackground()
            }

            assert(getWarehouseLocationsResult.value is Fail)
        }
    }

    @Test
    fun `success get product list meta data`() {
        with(mViewModel) {
            val dummyResult = productListMetaUiModel

            coEvery {
                getProductListMetaDataUseCase.executeOnBackground()
            } returns dummyResult

            getProductListMetaData(
                "",
                0
            )

            coVerify {
                getProductListMetaDataUseCase.executeOnBackground()
            }

            assert(getProductListMetaDataResult.value == Success(dummyResult))
        }
    }

    @Test
    fun `fail to get product list meta data`() {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                getProductListMetaDataUseCase.executeOnBackground()
            } throws dummyThrowable

            getProductListMetaData(
                "",
                0
            )

            coVerify {
                getProductListMetaDataUseCase.executeOnBackground()
            }

            assert(getProductListMetaDataResult.value is Fail)
        }
    }

    @Test
    fun `success get show cases by id`() {
        with(mViewModel) {
            val dummyResult = shopShowcasesByShopIdUiModel

            coEvery {
                getShowCasesByIdUseCase.executeOnBackground()
            } returns dummyResult

            getShopShowCases("")

            coVerify {
                getShowCasesByIdUseCase.executeOnBackground()
            }

            assert(getShowCasesByIdResult.value == Success(shopShowcasesByShopIdUiModel))
        }
    }

    @Test
    fun `fail to get show cases by id`() {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                getShowCasesByIdUseCase.executeOnBackground()
            } throws dummyThrowable

            getShopShowCases("")

            coVerify {
                getShowCasesByIdUseCase.executeOnBackground()
            }

            assert(getShowCasesByIdResult.value is Fail)
        }
    }

    @Test
    fun `success map product data to ui model`() {
        with(mViewModel) {
            val productDataList = listOf<ProductData>()

            assert(mapProductDataToProductUiModel(productDataList) == listOf<ProductUiModel>())
        }
    }
}