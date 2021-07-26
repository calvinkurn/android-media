package com.tkpd.atcvariant

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tkpd.atcvariant.data.uidata.VariantComponentDataModel
import com.tkpd.atcvariant.data.uidata.VariantHeaderDataModel
import com.tkpd.atcvariant.data.uidata.VariantQuantityDataModel
import com.tkpd.atcvariant.usecase.GetAggregatorAndMiniCartUseCase
import com.tkpd.atcvariant.util.AtcVariantJsonHelper
import com.tkpd.atcvariant.view.adapter.AtcVariantVisitable
import com.tkpd.atcvariant.view.viewmodel.AtcVariantViewModel
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartOcsUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
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

    @RelaxedMockK
    lateinit var deleteCartUseCase: DeleteCartUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    val viewModel by lazy {
        AtcVariantViewModel(CoroutineTestDispatchersProvider, aggregatorMiniCartUseCase,
                addToCartUseCase, addToCartOcsUseCase,
                addToCartOccUseCase, addWishListUseCase, updateCartUseCase, deleteCartUseCase)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    //region assert helper
    fun assertButton(expectedIsBuyable: Boolean = true,
                     expectedCartType: String? = "normal",
                     expectedCartColor: String? = "primary_green",
                     expectedCartText: String? = "+ Keranjang") {
        val data = (viewModel.buttonData.value as Success).data

        Assert.assertEquals(data.isProductSelectedBuyable, expectedIsBuyable)

        val cartType = data.cartTypeData?.availableButtons?.last()
        Assert.assertEquals(cartType?.cartType, expectedCartType)
        Assert.assertEquals(cartType?.color, expectedCartColor)
        Assert.assertEquals(cartType?.text, expectedCartText)
    }

    fun decideFailValueHitGqlAggregator() {
        coEvery {
            aggregatorMiniCartUseCase.executeOnBackground(any(), any(), any(), any(), any(), any(), false)
        } throws Throwable()

        viewModel.decideInitialValue(ProductVariantBottomSheetParams(), true)

        coVerify {
            aggregatorMiniCartUseCase.executeOnBackground(any(), any(), any(), any(), any(), any(), false)
        }

        Assert.assertTrue(viewModel.initialData.value is Fail)
        Assert.assertTrue(viewModel.buttonData.value is Fail)
    }

    fun decideSuccessValueHitGqlAggregator(productId: String, isTokoNow: Boolean) {
        val mockData = AtcVariantJsonHelper.generateAggregatorData(isTokoNow)
        val aggregatorParams = AtcVariantJsonHelper.generateParamsVariant(productId, isTokoNow)

        coEvery {
            aggregatorMiniCartUseCase.executeOnBackground(any(), any(), any(), any(), any(), any(), isTokoNow)
        } returns mockData

        viewModel.decideInitialValue(aggregatorParams, true)

        coVerify {
            aggregatorMiniCartUseCase.executeOnBackground(any(), any(), any(), any(), any(), any(), isTokoNow)
        }
    }

    fun assertVisitables(visitables: List<AtcVariantVisitable>,
                         showQuantityEditor: Boolean,
                         expectedSelectedOptionIds: List<String>,
                         expectedSelectedProductId: String,
                         expectedSelectedMainPrice: String,
                         expectedSelectedStock: String,
                         expectedSelectedOptionIdsLevelOne: String,
                         expectedSelectedOptionIdsLevelTwo: String,
                         expectedQuantity: Int,
                         expectedMinOrder: Int,
                         isEmptyStock: Boolean = false) {

        visitables.forEach {
            when (it) {
                is VariantHeaderDataModel -> {
                    Assert.assertEquals(it.productId, expectedSelectedProductId)
                    Assert.assertEquals(it.headerData.productMainPrice, expectedSelectedMainPrice)
                    Assert.assertEquals(it.headerData.productDiscountedPercentage, 0)
                    Assert.assertEquals(it.headerData.productStock, expectedSelectedStock)
                }
                is VariantComponentDataModel -> {
                    val currentSelectedLevelOne = it.listOfVariantCategory?.first()?.getSelectedOption()?.variantId
                            ?: "0"
                    val currentSelectedLevelTwo = it.listOfVariantCategory?.get(1)?.getSelectedOption()?.variantId
                            ?: "0"

                    Assert.assertEquals(currentSelectedLevelOne, expectedSelectedOptionIdsLevelOne)
                    Assert.assertEquals(currentSelectedLevelTwo, expectedSelectedOptionIdsLevelTwo)
                    Assert.assertTrue(it.mapOfSelectedVariant.values.toList().containsAll(expectedSelectedOptionIds))
                    Assert.assertEquals(it.isEmptyStock, isEmptyStock)
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