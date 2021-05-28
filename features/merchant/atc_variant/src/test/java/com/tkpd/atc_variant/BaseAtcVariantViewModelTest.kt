package com.tkpd.atc_variant

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tkpd.atc_variant.data.uidata.PartialButtonDataModel
import com.tkpd.atc_variant.data.uidata.VariantComponentDataModel
import com.tkpd.atc_variant.data.uidata.VariantHeaderDataModel
import com.tkpd.atc_variant.data.uidata.VariantQuantityDataModel
import com.tkpd.atc_variant.usecase.GetAggregatorAndMiniCartUseCase
import com.tkpd.atc_variant.util.AtcVariantJsonHelper
import com.tkpd.atc_variant.views.AtcVariantViewModel
import com.tkpd.atc_variant.views.adapter.AtcVariantVisitable
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartOcsUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.minicart.common.domain.usecase.UpdateCartUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
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
    lateinit var addToCartOccUseCase: AddToCartOccUseCase

    @RelaxedMockK
    lateinit var addWishListUseCase: AddWishListUseCase

    @RelaxedMockK
    lateinit var updateCartUseCase: UpdateCartUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    val viewModel by lazy {
        AtcVariantViewModel(CoroutineTestDispatchersProvider, aggregatorMiniCartUseCase,
                addToCartUseCase, addToCartOcsUseCase,
                addToCartOccUseCase, addWishListUseCase, updateCartUseCase)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    //region assert helper
    fun assertButton(data: PartialButtonDataModel,
                             expectedAlternateCopy: String = "",
                             expectedIsBuyable: Boolean = true,
                             expectedCartType: String = "normal",
                             expectedCartColor: String = "primary_green",
                             expectedCartText: String = "+ Keranjang") {
        //Todo alternate copy
        Assert.assertEquals(data.alternateText, expectedAlternateCopy)
        Assert.assertEquals(data.isProductSelectedBuyable, expectedIsBuyable)

        val cartType = data.cartTypeData?.availableButtons?.last()
        Assert.assertEquals(cartType?.cartType, expectedCartType)
        Assert.assertEquals(cartType?.color, expectedCartColor)
        Assert.assertEquals(cartType?.text, expectedCartText)
    }

    fun decideInitialValueHitGql(productId: String, isTokoNow: Boolean) {
        val mockData = AtcVariantJsonHelper.generateAggregatorData(isTokoNow)
        val aggregatorParams = AtcVariantJsonHelper.generateParamsVariant(productId, isTokoNow)

        coEvery {
            aggregatorMiniCartUseCase.executeOnBackground(any(), any(), isTokoNow)
        } returns mockData

        viewModel.decideInitialValue(aggregatorParams)

        coVerify {
            aggregatorMiniCartUseCase.executeOnBackground(any(), any(), isTokoNow)
        }
    }

    fun assertVisitables(visitables: List<AtcVariantVisitable>,
                                 showQuantityEditor: Boolean,
                                 expectedSelectedOptionIds: List<String>,
                                 expectedSelectedProductId: String,
                                 expectedSelectedMainPrice: String,
                                 expectedSelectedStockWording: String,
                                 expectedSelectedOptionIdsLevelOne: String,
                                 expectedSelectedOptionIdsLevelTwo: String,
                                 expectedQuantity: Int,
                                 expectedMinOrder: Int) {

        visitables.forEach {
            when (it) {
                is VariantHeaderDataModel -> {
                    Assert.assertEquals(it.headerData.productId, expectedSelectedProductId)
                    Assert.assertEquals(it.headerData.productMainPrice, expectedSelectedMainPrice)
                    Assert.assertEquals(it.headerData.productDiscountedPercentage, 0)
                    Assert.assertEquals(it.headerData.productStockWording, expectedSelectedStockWording)
                }
                is VariantComponentDataModel -> {
                    val currentSelectedLevelOne = it.listOfVariantCategory?.first()?.getSelectedOption()?.variantId
                            ?: "0"
                    val currentSelectedLevelTwo = it.listOfVariantCategory?.get(1)?.getSelectedOption()?.variantId
                            ?: "0"

                    Assert.assertEquals(currentSelectedLevelOne, expectedSelectedOptionIdsLevelOne)
                    Assert.assertEquals(currentSelectedLevelTwo, expectedSelectedOptionIdsLevelTwo)
                    Assert.assertTrue(it.mapOfSelectedVariant.values.toList().containsAll(expectedSelectedOptionIds))
                    Assert.assertEquals(it.isEmptyStock, false)
                    Assert.assertEquals(it.isTokoCabang, false)
                }
                is VariantQuantityDataModel -> {
                    Assert.assertEquals(it.quantity, expectedQuantity)
                    Assert.assertEquals(it.minOrder, expectedMinOrder)
                    Assert.assertEquals(it.shouldShowView, showQuantityEditor)
                }
            }
        }
    }
    //endregion

}