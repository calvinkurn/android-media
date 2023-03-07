package com.tokopedia.shop.flashsale.presentation.creation.manage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.flashsale.common.tracker.ShopFlashSaleTracker
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignProductListRequest.*
import com.tokopedia.shop.flashsale.domain.entity.*
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel.*
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList.*
import com.tokopedia.shop.flashsale.domain.entity.enums.*
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductBannerType.*
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignProductSubmissionUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignDetailUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignProductListUseCase
import com.tokopedia.shop.flashsale.presentation.creation.manage.mapper.ManageProductMapper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class ManageProductViewModelTest {

    @RelaxedMockK
    lateinit var userSessionInterface: UserSession

    @RelaxedMockK
    lateinit var getSellerCampaignProductListUseCase: GetSellerCampaignProductListUseCase

    @RelaxedMockK
    lateinit var doSellerCampaignProductSubmissionUseCase: DoSellerCampaignProductSubmissionUseCase

    @RelaxedMockK
    lateinit var getSellerCampaignDetailUseCase: GetSellerCampaignDetailUseCase

    @RelaxedMockK
    lateinit var tracker: ShopFlashSaleTracker

    @RelaxedMockK
    lateinit var gqlGetShopInfoUseCase: GQLGetShopInfoUseCase

    @RelaxedMockK
    lateinit var productObserver: Observer<in Result<SellerCampaignProductList>>

    @RelaxedMockK
    lateinit var incompleteProductObserver: Observer<in List<Product>>

    @RelaxedMockK
    lateinit var bannerTypeObserver: Observer<in ManageProductBannerType>

    @RelaxedMockK
    lateinit var removeProductObserver: Observer<in Result<Boolean>>

    @RelaxedMockK
    lateinit var shopStatusObserver: Observer<in Result<Int>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: ManageProductViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = ManageProductViewModel(
            CoroutineTestDispatchersProvider,
            userSessionInterface,
            getSellerCampaignProductListUseCase,
            doSellerCampaignProductSubmissionUseCase,
            getSellerCampaignDetailUseCase,
            tracker,
            gqlGetShopInfoUseCase
        )

        with(viewModel) {
            products.observeForever(productObserver)
            incompleteProducts.observeForever(incompleteProductObserver)
            bannerType.observeForever(bannerTypeObserver)
            removeProductsStatus.observeForever(removeProductObserver)
            shopStatus.observeForever(shopStatusObserver)
        }
    }

    @After
    fun tearDown() {
        with(viewModel) {
            products.removeObserver(productObserver)
            bannerType.removeObserver(bannerTypeObserver)
            removeProductsStatus.removeObserver(removeProductObserver)
            shopStatus.removeObserver(shopStatusObserver)
        }
    }

    @Test
    fun `when accessing incompleteProducts, will filter and map the data from products accordingly`() {
        runBlocking {
            with(viewModel) {
                //set products value
                val campaignId: Long = 1001
                val listType = 0
                val pagination = Pagination(
                    50,
                    0
                )
                coEvery {
                    getSellerCampaignProductListUseCase.execute(
                        campaignId,
                        productName = "",
                        listType,
                        pagination
                    )
                } returns generateIncompleteProduct()
                getProducts(campaignId, listType)

                //expected to have 1 product that is incomplete
                val expected = 1
                val actual = incompleteProducts.getOrAwaitValue().size

                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `when getProducts success, observer will successfully receive the data`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val listType = 0
                val pagination = Pagination(
                    50,
                    0
                )
                val expected = Success(generateCompleteProduct())
                coEvery {
                    getSellerCampaignProductListUseCase.execute(
                        campaignId,
                        productName = "",
                        listType,
                        pagination
                    )
                } returns generateCompleteProduct()

                getProducts(campaignId, listType)

                val actual = products.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `when getProducts error, observer will receive error result`() {
        runBlocking {
            with(viewModel) {
                val dummyThrowable = MessageErrorException("Error")
                val campaignId: Long = 1001
                val listType = 0
                val pagination = Pagination(
                    50,
                    0
                )
                val expected = Fail(dummyThrowable)
                coEvery {
                    getSellerCampaignProductListUseCase.execute(
                        campaignId,
                        productName = "",
                        listType,
                        pagination
                    )
                } throws dummyThrowable

                getProducts(campaignId, listType)

                val actual = products.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `when getCampaignDetail success, campaignName variable should be assigned with the correct campaign name`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val expected = "Flash Sale Toko"
                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } returns generateCampaignUiModel()

                getCampaignDetail(campaignId)

                val actual = campaignName
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `when getCampaignDetail error, campaignName variable should not be updated`() {
        runBlocking {
            with(viewModel) {
                val dummyThrowable = MessageErrorException("Error")
                val campaignId: Long = 1001
                val expected = ""
                coEvery {
                    getSellerCampaignDetailUseCase.execute(campaignId)
                } throws dummyThrowable

                getCampaignDetail(campaignId)

                val actual = campaignName
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `when productList data is not filled, observer will successfully receive the data accordingly`() {
        with(viewModel) {
            val expected = EMPTY_BANNER
            val productList = generateIncompleteProduct()
            getBannerType(productList)
            val actual = bannerType.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when productList data is containing error, observer will successfully receive the data accordingly`() {
        with(viewModel) {
            val expected = ERROR_BANNER
            val productList = generateErrorProduct()
            getBannerType(productList)
            val actual = bannerType.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when productList data is complete, observer will successfully receive the data accordingly`() {
        with(viewModel) {
            val expected = HIDE_BANNER
            val productList = generateCompleteProduct()
            getBannerType(productList)
            val actual = bannerType.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when discountedPrice value is 0, isProductInfoComplete will return false`() {
        with(viewModel) {
            val expected = false
            val product = SellerCampaignProductList(
                productList = listOf(
                    Product(
                        productMapData = ProductMapData(
                            originalPrice = 100000,
                            discountedPrice = 0,
                            discountPercentage = 20,
                            customStock = 90,
                            originalCustomStock = 90,
                            originalStock = 100,
                            campaignSoldCount = 10,
                            maxOrder = 90
                        )
                    )
                )
            )
            val actual =
                isProductInfoComplete(product.productList[0].productMapData)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when discountedPercentage value is 0, isProductInfoComplete will return false`() {
        with(viewModel) {
            val expected = false
            val product = SellerCampaignProductList(
                productList = listOf(
                    Product(
                        productMapData = ProductMapData(
                            originalPrice = 100000,
                            discountedPrice = 80000,
                            discountPercentage = 0,
                            customStock = 90,
                            originalCustomStock = 90,
                            originalStock = 100,
                            campaignSoldCount = 10,
                            maxOrder = 90
                        )
                    )
                )
            )
            val actual =
                isProductInfoComplete(product.productList[0].productMapData)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when originalCustomStock value is 0, isProductInfoComplete will return false`() {
        with(viewModel) {
            val expected = false
            val product = SellerCampaignProductList(
                productList = listOf(
                    Product(
                        productMapData = ProductMapData(
                            originalPrice = 100000,
                            discountedPrice = 80000,
                            discountPercentage = 20,
                            customStock = 90,
                            originalCustomStock = 0,
                            originalStock = 100,
                            campaignSoldCount = 10,
                            maxOrder = 90
                        )
                    )
                )
            )
            val actual =
                isProductInfoComplete(product.productList[0].productMapData)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when custom stock value is 0, isProductInfoComplete will return false`() {
        with(viewModel) {
            val expected = false
            val product = SellerCampaignProductList(
                productList = listOf(
                    Product(
                        productMapData = ProductMapData(
                            originalPrice = 100000,
                            discountedPrice = 80000,
                            discountPercentage = 20,
                            customStock = 0,
                            originalCustomStock = 90,
                            originalStock = 100,
                            campaignSoldCount = 10,
                            maxOrder = 90
                        )
                    )
                )
            )
            val actual =
                isProductInfoComplete(product.productList[0].productMapData)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when maxOrder value is 0, isProductInfoComplete will return false`() {
        with(viewModel) {
            val expected = false
            val product = SellerCampaignProductList(
                productList = listOf(
                    Product(
                        productMapData = ProductMapData(
                            originalPrice = 100000,
                            discountedPrice = 80000,
                            discountPercentage = 20,
                            customStock = 90,
                            originalCustomStock = 90,
                            originalStock = 100,
                            campaignSoldCount = 10,
                            maxOrder = 0
                        )
                    )
                )
            )
            val actual =
                isProductInfoComplete(product.productList[0].productMapData)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when product list data is complete, isProductInfoComplete will return true`() {
        with(viewModel) {
            val expected = true
            val actual =
                isProductInfoComplete(generateCompleteProduct().productList[0].productMapData)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when removeProducts is success, observer will successfully receive the data`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val productList = generateCompleteProduct().productList
                val action = ProductionSubmissionAction.DELETE
                val result = ProductSubmissionResult(
                    isSuccess = true,
                    errorMessage = "",
                    failedProducts = emptyList()
                )
                val expected = Success(true)
                coEvery {
                    doSellerCampaignProductSubmissionUseCase.execute(
                        campaignId = campaignId.toString(),
                        productData = ManageProductMapper.mapToProductDataList(productList),
                        action = action
                    )
                } returns result

                removeProducts(campaignId, productList)

                val actual = removeProductsStatus.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `when removeProducts is error, observer will receive error result`() {
        runBlocking {
            with(viewModel) {
                val dummyThrowable = MessageErrorException("Error")
                val campaignId: Long = 1001
                val productList = generateCompleteProduct().productList
                val action = ProductionSubmissionAction.DELETE
                val expected = Fail(dummyThrowable)

                coEvery {
                    doSellerCampaignProductSubmissionUseCase.execute(
                        campaignId = campaignId.toString(),
                        productData = ManageProductMapper.mapToProductDataList(productList),
                        action = action
                    )
                } throws dummyThrowable

                removeProducts(campaignId, productList)

                val actual = removeProductsStatus.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `when getShopStatus is success, observer will successfully receive the data`() {
        runBlocking {
            with(viewModel) {
                val result = ShopInfo()
                val expected = Success(result.statusInfo.shopStatus)
                val shopId = userSessionInterface.shopId.toIntOrZero()
                gqlGetShopInfoUseCase.params = GQLGetShopInfoUseCase.createParams(listOf(shopId))
                gqlGetShopInfoUseCase.isFromCacheFirst = true

                coEvery {
                    gqlGetShopInfoUseCase.executeOnBackground()
                } returns result

                getShopStatus()

                val actual = shopStatus.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `when getShopStatus is error, observer will successfully receive error result`() {
        runBlocking {
            with(viewModel) {
                val dummyThrowable = MessageErrorException("Error")
                val expected = Fail(dummyThrowable)
                val shopId = userSessionInterface.shopId.toIntOrZero()
                gqlGetShopInfoUseCase.params = GQLGetShopInfoUseCase.createParams(listOf(shopId))
                gqlGetShopInfoUseCase.isFromCacheFirst = true

                coEvery {
                    gqlGetShopInfoUseCase.executeOnBackground()
                } throws dummyThrowable

                getShopStatus()

                val actual = shopStatus.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `when buttton proceed is tapped, tracker will be sent` () {
        runBlocking {
            with(viewModel) {
                onButtonProceedTapped()

                coVerify {
                    tracker.sendClickButtonProceedOnManageProductPage()
                }
            }
        }
    }

    @Test
    fun `when isCoachMarkShown value is set, the value will be set with the correct value accordingly`() {
        with(viewModel) {
            setIsCoachMarkShown(true)
            val expected = true
            val actual = getIsCoachMarkShown()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `set auto navigation to choose product value, will change the value accordingly`() {
        with(viewModel) {
            val expected = true
            setAutoNavigateToChooseProduct(true)
            val actual = autoNavigateToChooseProduct()
            assertEquals(expected, actual)
        }
    }

    private fun generateCompleteProduct() = SellerCampaignProductList(
        productList = listOf(
            Product(
                productMapData = ProductMapData(
                    originalPrice = 100000,
                    discountedPrice = 80000,
                    discountPercentage = 20,
                    customStock = 90,
                    originalCustomStock = 90,
                    originalStock = 100,
                    campaignSoldCount = 10,
                    maxOrder = 90
                ),
                isInfoComplete = true,
                errorType = ManageProductErrorType.NOT_ERROR
            )
        )
    )

    private fun generateErrorProduct() = SellerCampaignProductList(
        productList = listOf(
            Product(
                productMapData = ProductMapData(
                    originalPrice = 100000,
                    discountedPrice = 80000,
                    discountPercentage = 20,
                    customStock = 90,
                    originalCustomStock = 90,
                    originalStock = 100,
                    maxOrder = 110
                ),
                isInfoComplete = true,
                errorType = ManageProductErrorType.MAX_ORDER
            )
        )
    )

    private fun generateIncompleteProduct() = SellerCampaignProductList(
        productList = listOf(
            Product(
                productMapData = ProductMapData(
                    originalPrice = 0,
                    discountedPrice = 0,
                    discountPercentage = 0,
                    customStock = 0,
                    originalCustomStock = 0,
                    originalStock = 0,
                    maxOrder = 0
                ),
                isInfoComplete = false
            )
        )
    )


    private fun generateCampaignUiModel() = CampaignUiModel(
        campaignId = 1001,
        campaignName = "Flash Sale Toko",
        "",
        "",
        isCancellable = true,
        isShareable = true,
        notifyMeCount = 0,
        "",
        "",
        CampaignStatus.READY,
        true,
        ProductSummary(0, 0, 0, 0, 0, 0),
        Date(),
        Date(),
        Gradient("", "", true),
        true,
        Date(),
        PaymentType.REGULAR,
        true,
        isCampaignRelation = true,
        relatedCampaigns = emptyList(),
        isCampaignRuleSubmit = true,
        relativeTimeDifferenceInMinute = 100,
        thematicInfo = ThematicInfo(0, 0, "", 0, ""),
        reviewStartDate = Date(),
        reviewEndDate = Date(),
        packageInfo = PackageInfo(
            packageId = 0,
            packageName = "VPS"
        )
    )
}
