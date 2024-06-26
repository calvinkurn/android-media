package com.tkpd.atcvariant

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tkpd.atcvariant.data.uidata.VariantComponentDataModel
import com.tkpd.atcvariant.data.uidata.VariantHeaderDataModel
import com.tkpd.atcvariant.data.uidata.VariantQuantityDataModel
import com.tkpd.atcvariant.usecase.GetAggregatorAndMiniCartUseCase
import com.tkpd.atcvariant.util.AtcVariantJsonHelper
import com.tkpd.atcvariant.util.REMOTE_CONFIG_NEW_VARIANT_LOG
import com.tkpd.atcvariant.view.adapter.AtcVariantVisitable
import com.tkpd.atcvariant.view.viewmodel.AtcVariantViewModel
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.atc_common.domain.usecase.AddToCartOcsUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import com.tokopedia.product.detail.common.data.model.promoprice.PromoPriceUiModel
import com.tokopedia.product.detail.common.usecase.ToggleFavoriteUseCase
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import io.mockk.spyk
import org.junit.Assert
import org.junit.Before
import org.junit.Rule

/**
 * Created by Yehezkiel on 28/05/21
 */
abstract class BaseAtcVariantViewModelTest {

    @RelaxedMockK
    lateinit var aggregatorMiniCartUseCase: GetAggregatorAndMiniCartUseCase

    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase

    @RelaxedMockK
    lateinit var addToCartOcsUseCase: AddToCartOcsUseCase

    @RelaxedMockK
    lateinit var addToCartOccUseCase: AddToCartOccMultiUseCase

    @RelaxedMockK
    lateinit var addToWishlistV2UseCase: AddToWishlistV2UseCase

    @RelaxedMockK
    lateinit var updateCartUseCase: UpdateCartUseCase

    @RelaxedMockK
    lateinit var deleteCartUseCase: DeleteCartUseCase

    @RelaxedMockK
    lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase

    @RelaxedMockK
    lateinit var remoteConfig: RemoteConfig

    @get:Rule
    val rule = InstantTaskExecutorRule()

    val viewModel by lazy {
        AtcVariantViewModel(
            CoroutineTestDispatchersProvider, aggregatorMiniCartUseCase,
            addToCartUseCase, addToCartOcsUseCase, addToCartOccUseCase,
            addToWishlistV2UseCase, updateCartUseCase,
            deleteCartUseCase, toggleFavoriteUseCase, remoteConfig
        )
    }

    val spykViewModel by lazy {
        spyk(viewModel)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(AppLogAnalytics)
        every { AppLogAnalytics.getEntranceInfo(any()) } returns "{}"
        coEvery {
            remoteConfig.getBoolean(REMOTE_CONFIG_NEW_VARIANT_LOG, true)
        } returns true
    }

    //region assert helper
    fun assertButton(
        expectedIsBuyable: Boolean = true,
        expectedCartType: String? = "normal",
        expectedCartColor: String? = "primary_green",
        expectedCartText: String? = "+ Keranjang"
    ) {
        val data = (viewModel.buttonData.value as Success).data

        Assert.assertEquals(data.isProductSelectedBuyable, expectedIsBuyable)

        val cartType = data.cartTypeData?.availableButtons?.last()
        Assert.assertEquals(cartType?.cartType, expectedCartType)
        Assert.assertEquals(cartType?.color, expectedCartColor)
        Assert.assertEquals(cartType?.text, expectedCartText)
    }

    fun assertRestrictionData(
        assertSuccess: Boolean,
        expectedProductId: String = "",
        expectedDescription: String = "",
        expectedTitle: String = ""
    ) {
        if (assertSuccess) {
            val data = (viewModel.restrictionData.value as Success).data

            Assert.assertEquals(data.productId, expectedProductId)
            Assert.assertEquals(data.action.firstOrNull()?.description, expectedDescription)
            Assert.assertEquals(data.action.firstOrNull()?.title, expectedTitle)
        } else {
            Assert.assertTrue(viewModel.restrictionData.value is Fail)
        }
    }

    fun assertRatesData(
        assertSuccess: Boolean,
        containProductId: String = "",
        expectedSubtitle: String = "",
        expectedTitle: String = ""
    ) {
        if (assertSuccess) {
            val data = (viewModel.ratesLiveData.value as Success).data

            Assert.assertEquals(data.listfProductId.contains(containProductId), true)
            Assert.assertEquals(data.errorBottomSheet.title, expectedTitle)
            Assert.assertEquals(data.errorBottomSheet.subtitle, expectedSubtitle)
        } else {
            Assert.assertTrue(viewModel.ratesLiveData.value is Fail)
        }
    }

    fun assertStockCopy(expectedStockCopy: String) {
        val currentStockCopy = viewModel.stockCopy.value

        Assert.assertNotNull(currentStockCopy)
        Assert.assertEquals(currentStockCopy, expectedStockCopy)
    }

    fun decideFailValueHitGqlAggregator() {
        coEvery {
            aggregatorMiniCartUseCase.executeOnBackground(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                false,
                any(),
                any()
            )
        } throws Throwable()

        viewModel.decideInitialValue(ProductVariantBottomSheetParams(), true)

        coVerify {
            aggregatorMiniCartUseCase.executeOnBackground(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                false,
                any(),
                any()
            )
        }

        Assert.assertTrue(viewModel.initialData.value is Fail)
        Assert.assertTrue(viewModel.buttonData.value is Fail)
    }

    fun decideSuccessValueHitGqlAggregator(
        productId: String,
        isTokoNow: Boolean,
        showQtyEditor: Boolean
    ) {
        val mockData = AtcVariantJsonHelper.generateAggregatorData(isTokoNow)
        val aggregatorParams =
            AtcVariantJsonHelper.generateParamsVariant(productId, isTokoNow, showQtyEditor)

        coEvery {
            aggregatorMiniCartUseCase.executeOnBackground(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                isTokoNow,
                any(),
                any()
            )
        } returns mockData

        viewModel.decideInitialValue(aggregatorParams, true)

        coVerify {
            aggregatorMiniCartUseCase.executeOnBackground(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                isTokoNow,
                any(),
                any()
            )
        }
    }

    fun assertCampaign(
        visitables: List<AtcVariantVisitable>,
        expectedCampaignActive: Boolean,
        expectedMainPrice: String,
        expectedSlashPrice: String
    ) {
        visitables.first {
            it is VariantHeaderDataModel
        }.let {
            val headerData = it as VariantHeaderDataModel
            Assert.assertEquals(expectedCampaignActive, headerData.headerData.isCampaignActive)
            Assert.assertEquals(expectedMainPrice, headerData.headerData.productMainPrice)
            Assert.assertEquals(expectedSlashPrice, headerData.headerData.productSlashPrice)
        }
    }

    fun assertVisitables(
        visitables: List<AtcVariantVisitable>,
        showQuantityEditor: Boolean,
        expectedSelectedProductId: String,
        expectedSelectedMainPrice: String,
        expectedSelectedSlashPrice: String,
        expectedSelectedStockFmt: String,
        expectedSelectedOptionIdsLevelOne: String,
        expectedSelectedOptionIdsLevelTwo: String,
        expectedVariantName: List<String> = listOf(),
        expectedQuantity: Int,
        cashBackPercentage: Int,
        uspImageUrl: String,
        isTokoCabang: Boolean,
        expectedMinOrder: Int
    ) {
        visitables.forEach {
            when (it) {
                is VariantHeaderDataModel -> {
                    Assert.assertEquals(it.productId, expectedSelectedProductId)
                    Assert.assertEquals(it.headerData.productMainPrice, expectedSelectedMainPrice)
                    Assert.assertEquals(it.headerData.productSlashPrice, expectedSelectedSlashPrice)
                    Assert.assertEquals(it.headerData.productDiscountedPercentage, "0%")
                    Assert.assertEquals(it.headerData.productStockFmt, expectedSelectedStockFmt)
                    Assert.assertTrue(it.listOfVariantTitle.containsAll(expectedVariantName))
                    Assert.assertEquals(it.cashBackPercentage, cashBackPercentage)
                    Assert.assertEquals(it.uspImageUrl, uspImageUrl)
                    Assert.assertEquals(it.isTokoCabang, isTokoCabang)
                }

                is VariantComponentDataModel -> {
                    val currentSelectedLevelOne =
                        it.listOfVariantCategory?.first()?.getSelectedOption()?.variantId
                            ?: "0"
                    val currentSelectedLevelTwo =
                        it.listOfVariantCategory?.get(1)?.getSelectedOption()?.variantId
                            ?: "0"

                    Assert.assertEquals(currentSelectedLevelOne, expectedSelectedOptionIdsLevelOne)
                    Assert.assertEquals(currentSelectedLevelTwo, expectedSelectedOptionIdsLevelTwo)
                    Assert.assertTrue(
                        it.mapOfSelectedVariant.values.toList().containsAll(
                            listOf(
                                expectedSelectedOptionIdsLevelOne,
                                expectedSelectedOptionIdsLevelOne,
                                expectedSelectedOptionIdsLevelTwo
                            )
                        )
                    )
                }

                is VariantQuantityDataModel -> {
                    Assert.assertEquals(it.quantity, expectedQuantity)
                    Assert.assertEquals(it.minOrder, expectedMinOrder)
                    Assert.assertEquals(it.shouldShowView, showQuantityEditor)
                }
            }
        }
    }

    fun assertPromoPrice(
        visitables: List<AtcVariantVisitable>,
        expectedPromoPrice: PromoPriceUiModel? = null
    ) {
        visitables.forEach {
            if (it is VariantHeaderDataModel) {
                Assert.assertEquals(it.headerData.promoPrice, expectedPromoPrice)
            }
        }
    }
    //endregion
}
