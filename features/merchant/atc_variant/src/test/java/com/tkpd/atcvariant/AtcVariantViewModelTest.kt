package com.tkpd.atcvariant

import com.tkpd.atcvariant.data.uidata.VariantComponentDataModel
import com.tkpd.atcvariant.data.uidata.VariantQuantityDataModel
import com.tkpd.atcvariant.util.AtcVariantJsonHelper.generateParamsVariantFulfilled
import com.tkpd.atcvariant.view.adapter.AtcVariantVisitable
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.Data
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.aggregator.AggregatorMiniCartUiModel
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailGallery
import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.DataFollowShop
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.FollowShop
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yehezkiel on 28/05/21
 */
class AtcVariantViewModelTest : BaseAtcVariantViewModelTest() {

    @get:Rule
    val coroutineScopeRule = CoroutineTestRule()

    //region helper function
    @Test
    fun `get aggreagator data`() {
        `render initial variant with given child id and hit gql tokonow campaign hide gimmick`()
        val data = viewModel.getVariantAggregatorData()

        Assert.assertNotNull(data)
    }

    @Test
    fun `fail get selected option ids`() {
        decideFailValueHitGqlAggregator()

        val selectedOptionIds = viewModel.getSelectedOptionIds()

        Assert.assertNull(selectedOptionIds)
    }

    @Test
    fun `success get selected option ids`() {
        decideSuccessValueHitGqlAggregator("2147818569", true, true)

        val selectedOptionIds = viewModel.getSelectedOptionIds()

        Assert.assertNotNull(selectedOptionIds)
    }

    @Test
    fun `success get variant data`() {
        decideSuccessValueHitGqlAggregator("2147818569", true, true)

        val variantData = viewModel.getVariantData()

        Assert.assertNotNull(variantData)
    }

    @Test
    fun `fail get variant data`() {
        decideFailValueHitGqlAggregator()
        val variantData = viewModel.getVariantData()
        Assert.assertNull(variantData)
    }
    //endregion

    //region render initial data without calling gql
    /**
     * If open with parent, auto select first child buyable
     * render without calling gql, because all of the data already provided from PDP
     */
    @Test
    fun `render initial variant with given parent id and non hit gql tokonow tokocabang campaign`() {
        val aggregatorParams = generateParamsVariantFulfilled("2147818569", true)

        coEvery {
            aggregatorMiniCartUseCase.executeOnBackground(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                true,
                any(),
                any()
            )
        } returns AggregatorMiniCartUiModel()

        viewModel.decideInitialValue(aggregatorParams, true)

        coVerify(inverse = true) {
            aggregatorMiniCartUseCase.executeOnBackground(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                true,
                any(),
                any()
            )
        }

        val visitablesData = (viewModel.initialData.value as Success).data

        assertVisitables(
            visitablesData,
            showQuantityEditor = true,
            expectedSelectedProductId = "2147818576",
            expectedSelectedMainPrice = 1000.getCurrencyFormatted(),
            expectedSelectedStockFmt = "Stock : 10",
            expectedSelectedOptionIdsLevelOne = "254080",
            expectedSelectedOptionIdsLevelTwo = "254085",
            expectedVariantName = listOf("Merah", "M"),
            expectedQuantity = 23,
            expectedMinOrder = 3, // use min order campaign
            cashBackPercentage = 102,
            uspImageUrl = "icon usp",
            isTokoCabang = true
        )

        assertCampaign(
            visitablesData,
            expectedCampaignActive = true,
            expectedDiscountedPrice = 208000.getCurrencyFormatted()
        )
        assertStockCopy("")
        assertButton(expectedCartText = "Simpan Perubahan")
        assertRestrictionData(assertSuccess = false)
    }
    //endregion

    //region test determine hit gql or not
    @Test
    fun `test ignore hit gql when !tokonow and mini cart null`() {
        val aggregatorParams = generateParamsVariantFulfilled("2147818569", false)

        coEvery {
            aggregatorMiniCartUseCase.executeOnBackground(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                true,
                any(),
                any()
            )
        } returns AggregatorMiniCartUiModel()

        viewModel.decideInitialValue(aggregatorParams, true)

        coVerify(inverse = true) {
            aggregatorMiniCartUseCase.executeOnBackground(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                true,
                any(),
                any()
            )
        }
    }

    @Test
    fun `test must hit gql when tokonow and mini cart null`() {
        val aggregatorParams = generateParamsVariantFulfilled("2147818569", true, true)

        coEvery {
            aggregatorMiniCartUseCase.executeOnBackground(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                true,
                any(),
                any()
            )
        } returns AggregatorMiniCartUiModel()

        viewModel.decideInitialValue(aggregatorParams, true)

        coVerify {
            aggregatorMiniCartUseCase.executeOnBackground(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                true,
                any(),
                any()
            )
        }

        Assert.assertEquals(viewModel.getActivityResultData().shouldRefreshPreviousPage, true)
    }

    @Test
    fun `test must hit gql when showQtyEditor and mini cart null`() {
        val aggregatorParams = generateParamsVariantFulfilled(
            "2147818569",
            false,
            true
        )

        aggregatorParams.showQtyEditor = true

        coEvery {
            aggregatorMiniCartUseCase.executeOnBackground(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns AggregatorMiniCartUiModel()

        viewModel.decideInitialValue(aggregatorParams, true)

        coVerify {
            aggregatorMiniCartUseCase.executeOnBackground(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        }

        Assert.assertEquals(viewModel.getActivityResultData().shouldRefreshPreviousPage, true)
    }
    //endregion

    //region Render Initial With Calling Gql
    /**
     * If open with parent, auto select first child buyable
     * In this case we are using Merah, M , as a first child
     * Doenst has product in minicart cause !isTokoNow
     */
    @Test
    fun `render initial variant with given parent id and hit gql non tokonow`() {
        decideSuccessValueHitGqlAggregator("2147818569", false, false)

        val visitablesData = (viewModel.initialData.value as Success).data

        assertVisitables(
            visitablesData,
            showQuantityEditor = false,
            expectedSelectedProductId = "2147818576",
            expectedSelectedMainPrice = 1000.getCurrencyFormatted(),
            expectedSelectedStockFmt = "Stock : 10",
            expectedSelectedOptionIdsLevelOne = "254080",
            expectedSelectedOptionIdsLevelTwo = "254085",
            expectedVariantName = listOf("Merah", "M"),
            expectedQuantity = 0,
            expectedMinOrder = 3,
            cashBackPercentage = 102,
            uspImageUrl = "icon usp",
            isTokoCabang = true
        )
        assertStockCopy("")
        assertButton()
        assertRestrictionData(assertSuccess = false)
    }

    @Test
    fun `render initial variant with given parent id and hit gql non tokonow showQtyEditor`() {
        decideSuccessValueHitGqlAggregator("2147818569", false, true)

        val visitablesData = (viewModel.initialData.value as Success).data

        assertVisitables(
            visitablesData,
            showQuantityEditor = true,
            expectedSelectedProductId = "2147818576",
            expectedSelectedMainPrice = 1000.getCurrencyFormatted(),
            expectedSelectedStockFmt = "Stock : 10",
            expectedSelectedOptionIdsLevelOne = "254080",
            expectedSelectedOptionIdsLevelTwo = "254085",
            expectedVariantName = listOf("Merah", "M"),
            expectedQuantity = 0,
            expectedMinOrder = 3,
            cashBackPercentage = 102,
            uspImageUrl = "icon usp",
            isTokoCabang = true
        )
        assertStockCopy("")
        assertButton()
        assertRestrictionData(assertSuccess = false)
    }

    /**
     * If open with parent, auto select first child buyable
     * In this case we are using Merah, M , as a first child
     * Has product in minicart
     */
    @Test
    fun `render initial variant with given parent id and hit gql tokonow`() {
        decideSuccessValueHitGqlAggregator("2147818569", true, true)

        val visitablesData = (viewModel.initialData.value as Success).data

        assertVisitables(
            visitablesData,
            showQuantityEditor = true,
            expectedSelectedProductId = "2147818576",
            expectedSelectedMainPrice = 1000.getCurrencyFormatted(),
            expectedSelectedStockFmt = "Stock : 10",
            expectedSelectedOptionIdsLevelOne = "254080",
            expectedSelectedOptionIdsLevelTwo = "254085",
            expectedVariantName = listOf("Merah", "M"),
            expectedQuantity = 23,
            expectedMinOrder = 3,
            cashBackPercentage = 102,
            uspImageUrl = "icon usp",
            isTokoCabang = true
        )
        assertStockCopy("")
        assertButton(expectedCartText = "Simpan Perubahan")
        assertRestrictionData(assertSuccess = false)
    }

    /**
     * If open with child id that buyable, auto select it to the child
     * Doesnt has product in minicart
     */
    @Test
    fun `render initial variant with given child id and hit gql tokonow campaign hide gimmick`() {
        decideSuccessValueHitGqlAggregator("2147818593", true, true)

        val visitablesData = (viewModel.initialData.value as Success).data

        assertVisitables(
            visitablesData,
            showQuantityEditor = true,
            expectedSelectedProductId = "2147818593",
            expectedSelectedMainPrice = 3000.getCurrencyFormatted(),
            expectedSelectedStockFmt = "Stock : 120",
            expectedSelectedOptionIdsLevelOne = "254083",
            expectedSelectedOptionIdsLevelTwo = "254087",
            expectedVariantName = listOf("Ungu", "XL"),
            expectedQuantity = 0,
            expectedMinOrder = 2,
            cashBackPercentage = 102,
            uspImageUrl = "icon usp",
            isTokoCabang = false
        )
        assertCampaign(
            visitablesData,
            expectedCampaignActive = false,
            expectedDiscountedPrice = 2000.getCurrencyFormatted()
        )
        assertStockCopy("")
        assertButton()
        assertRestrictionData(
            assertSuccess = true,
            expectedProductId = "2147818593",
            expectedDescription = "desc re gan",
            expectedTitle = "title re gan platinum"
        )
    }

    /**
     * If open with child id that not buyable, select header and title as child selected.
     * But dont select variant component
     * Has product in minicart
     * The button is Ingatkan saya
     */
    @Test
    fun `render initial variant with given child id not buyable and hit gql tokonow`() {
        decideSuccessValueHitGqlAggregator("2147818570", true, true)

        val visitablesData = (viewModel.initialData.value as Success).data

        assertVisitables(
            visitablesData,
            showQuantityEditor = false,
            expectedSelectedProductId = "2147818570",
            expectedSelectedMainPrice = 1000.getCurrencyFormatted(),
            expectedSelectedStockFmt = "kosong bro",
            expectedSelectedOptionIdsLevelOne = "254079",
            expectedSelectedOptionIdsLevelTwo = "254084",
            expectedQuantity = 2,
            expectedVariantName = listOf("Biru", "S"),
            expectedMinOrder = 3,
            cashBackPercentage = 102,
            uspImageUrl = "icon usp",
            isTokoCabang = false
        )

        assertStockCopy("kosong bro copy")
        assertButton(
            expectedIsBuyable = false,
            expectedCartType = "remind_me",
            expectedCartColor = "secondary_green",
            expectedCartText = "Ingatkan Saya"
        )

        assertRestrictionData(assertSuccess = false)
    }

    //endregion

    //region render data after click variant
    /**
     * Clicked variant level 1, with condition level 2 already clicked
     * means there are 2 levels variant, level one and level two already selected
     * expected header,variant, quantity, and button change to respective variant
     * expected updated data getting updated
     */
    @Test
    fun `render variant after change level one in already selected variant`() {
        // call to fill initial value
        `render initial variant with given parent id and hit gql non tokonow`()

        val warnaId = "121018"
        val hijauId = "254082"

        viewModel.onVariantClicked(false, false, warnaId, hijauId, "image variant", 1)

        val visitablesData = (viewModel.initialData.value as Success).data
        assertVisitables(
            visitablesData,
            showQuantityEditor = false,
            expectedSelectedProductId = "2147818586",
            expectedSelectedMainPrice = 7000.getCurrencyFormatted(),
            expectedSelectedStockFmt = "Stock : 10",
            expectedSelectedOptionIdsLevelOne = "254082",
            expectedSelectedOptionIdsLevelTwo = "254085",
            expectedVariantName = listOf("Hijau", "M"),
            expectedQuantity = 0,
            expectedMinOrder = 1,
            cashBackPercentage = 102,
            uspImageUrl = "icon usp",
            isTokoCabang = false
        )
        assertStockCopy("")
        assertButton(
            expectedIsBuyable = true,
            expectedCartText = "+ Keranjang Hijau M"
        )

        val updateResultData = viewModel.getActivityResultData()
        val variantDataVisitable = visitablesData[1] as VariantComponentDataModel
        Assert.assertTrue(
            updateResultData.mapOfSelectedVariantOption?.values?.toList()
                ?.containsAll(variantDataVisitable.mapOfSelectedVariant.values.toList())
                ?: false
        )
        Assert.assertEquals(updateResultData.selectedProductId, "2147818586")

        assertRestrictionData(
            assertSuccess = true,
            expectedProductId = "2147818586",
            expectedDescription = "desc re gan",
            expectedTitle = "title re gan gold"
        )

        assertRatesData(
            assertSuccess = true,
            containProductId = "2147818586",
            expectedTitle = "Jarak pengiriman terlalu jauh",
            expectedSubtitle = "Tidak ada kurir yang mendukung pengiriman barang ini ke alamat kamu."
        )
    }

    /**
     * Clicked variant level 1, with condition level 2 not being selected
     * means there are 2 levels variant, no one has selected
     * expected header and quantity editor not being updated only selected being updated
     */
    @Test
    fun `render variant after change level one in empty selected variant`() {
        // call to fill initial value, with not buyable because we dont want to select any variant here
        `render initial variant with given child id not buyable and hit gql tokonow`()

        val warnaId = "121018"
        val merahId = "254080"

        viewModel.onVariantClicked(true, false, warnaId, merahId, "image variant", 1)

        val visitablesData = (viewModel.initialData.value as Success).data
        val expectedLevelOneVariantIdChanged = merahId
        assertVisitables(
            visitablesData,
            showQuantityEditor = false,
            expectedSelectedProductId = "2147818575",
            expectedSelectedMainPrice = 1000.getCurrencyFormatted(),
            expectedSelectedStockFmt = "kosong bro",
            expectedSelectedOptionIdsLevelOne = expectedLevelOneVariantIdChanged,
            expectedSelectedOptionIdsLevelTwo = "254084",
            expectedVariantName = listOf("Merah", "S"),
            expectedQuantity = 0,
            expectedMinOrder = 1,
            cashBackPercentage = 102,
            uspImageUrl = "icon usp",
            isTokoCabang = false
        )
        assertStockCopy("kosong bro copy")
        assertButton(
            expectedIsBuyable = false,
            expectedCartType = "empty",
            expectedCartColor = "disabled",
            expectedCartText = "Stok Habis"
        )
        assertRestrictionData(assertSuccess = false)
    }

    @Test
    fun `bottom sheet rates not null but not error after click variant`() {
        `render initial variant with given child id not buyable and hit gql tokonow`()

        val warnaId = "121018"
        val unguId = "254083"

        viewModel.onVariantClicked(true, false, warnaId, unguId, "image variant", 1)

        assertRestrictionData(assertSuccess = false)
    }

    /**
     * initial selected variant
     * level one: Merah, mapOfSelected[121018, 254080]
     * level two: M, mapOfSelected[121019, 254085]
     */
    private fun `select variant oos by new logic`(
        optionKey: String,
        optionId: String,
        selectedLevel: Int,
        block: (List<AtcVariantVisitable>) -> Unit
    ) {
        `render initial variant with given parent id and hit gql non tokonow`()

        viewModel.onVariantClicked(
            showQtyEditor = false,
            isTokoNow = false,
            selectedOptionKey = optionKey,
            selectedOptionId = optionId,
            variantImage = "image variant",
            variantLevel = selectedLevel
        )

        val visitableData = (viewModel.initialData.value as Success).data
        block.invoke(visitableData)
    }

    @Test
    fun `ensure variant level one is right(oos or not) after variant level one selected`() {
        // please check more detail to GQL_VARIANT_AGGREGATOR_RESPONSE_JSON
        val variantLevelOne = "121018"
        val variantMerahID = "254080"

        `select variant oos by new logic`(
            optionKey = variantLevelOne,
            optionId = variantMerahID,
            selectedLevel = 1
        ) {
            // get variant level one
            val variantComponentLevelOne = it.firstOrNull { component ->
                component is VariantComponentDataModel
            } as VariantComponentDataModel
            val optionLevelOne =
                variantComponentLevelOne.listOfVariantCategory?.firstOrNull()?.variantOptions
                    ?: emptyList()

            assertTrue(optionLevelOne[0].currentState == VariantConstant.STATE_EMPTY) // 254079
            assertTrue(optionLevelOne[1].currentState == VariantConstant.STATE_SELECTED) // 254080
            assertTrue(optionLevelOne[2].currentState == VariantConstant.STATE_EMPTY) // 254081
            assertTrue(optionLevelOne[3].currentState == VariantConstant.STATE_UNSELECTED) // 254082
            assertTrue(optionLevelOne[4].currentState == VariantConstant.STATE_UNSELECTED) // 254083

            assertFalse(optionLevelOne[0].flashSale)
            assertTrue(optionLevelOne[1].flashSale)
            assertFalse(optionLevelOne[2].flashSale)
            assertFalse(optionLevelOne[3].flashSale)
            assertFalse(optionLevelOne[4].flashSale)
        }
    }

    @Test
    fun `ensure variant level two is right(oos or not) after variant level one selected`() {
        // please check more detail to GQL_VARIANT_AGGREGATOR_RESPONSE_JSON
        val variantLevelOne = "121018"
        val variantUnguID = "254083"

        `select variant oos by new logic`(
            optionKey = variantLevelOne,
            optionId = variantUnguID,
            selectedLevel = 1
        ) {
            // get variant level one
            val variantComponentLevelTwo = it.lastOrNull { component ->
                component is VariantComponentDataModel
            } as VariantComponentDataModel
            val optionLevelTwo =
                variantComponentLevelTwo.listOfVariantCategory?.lastOrNull()?.variantOptions
                    ?: emptyList()

            assertTrue(optionLevelTwo[0].currentState == VariantConstant.STATE_UNSELECTED) // 254084
            assertTrue(optionLevelTwo[1].currentState == VariantConstant.STATE_SELECTED) // 254085
            assertTrue(optionLevelTwo[2].currentState == VariantConstant.STATE_EMPTY) // 254086
            assertTrue(optionLevelTwo[3].currentState == VariantConstant.STATE_UNSELECTED) // 254087
            assertTrue(optionLevelTwo[4].currentState == VariantConstant.STATE_UNSELECTED) // 254088

            assertFalse(optionLevelTwo[0].flashSale)
            assertFalse(optionLevelTwo[1].flashSale)
            assertFalse(optionLevelTwo[2].flashSale)
            assertTrue(optionLevelTwo[3].flashSale)
            assertFalse(optionLevelTwo[4].flashSale)
        }
    }

    @Test
    fun `ensure variant level one is right(oos or not) after variant level two selected`() {
        // please check more detail to GQL_VARIANT_AGGREGATOR_RESPONSE_JSON
        val variantLevelTwo = "121019"
        val variantXXLID = "254088"

        `select variant oos by new logic`(
            optionKey = variantLevelTwo,
            optionId = variantXXLID,
            selectedLevel = 2
        ) {
            // get variant level one
            val variantComponentLevelOne = it.firstOrNull { component ->
                component is VariantComponentDataModel
            } as VariantComponentDataModel
            val optionLevelOne =
                variantComponentLevelOne.listOfVariantCategory?.firstOrNull()?.variantOptions
                    ?: emptyList()

            assertTrue(optionLevelOne[0].currentState == VariantConstant.STATE_EMPTY) // 254079
            assertTrue(optionLevelOne[1].currentState == VariantConstant.STATE_SELECTED) // 254080
            assertTrue(optionLevelOne[2].currentState == VariantConstant.STATE_EMPTY) // 254081
            assertTrue(optionLevelOne[3].currentState == VariantConstant.STATE_UNSELECTED) // 254082
            assertTrue(optionLevelOne[4].currentState == VariantConstant.STATE_UNSELECTED) // 254083

            assertFalse(optionLevelOne[0].flashSale)
            assertFalse(optionLevelOne[1].flashSale)
            assertFalse(optionLevelOne[2].flashSale)
            assertFalse(optionLevelOne[3].flashSale)
            assertFalse(optionLevelOne[4].flashSale)
        }
    }

    @Test
    fun `ensure variant level two is right(oos or not) after variant level two selected`() {
        // please check more detail to GQL_VARIANT_AGGREGATOR_RESPONSE_JSON
        val variantLevelTwo = "121019"
        val variantXXLID = "254088"

        `select variant oos by new logic`(
            optionKey = variantLevelTwo,
            optionId = variantXXLID,
            selectedLevel = 1
        ) {
            // get variant level one
            val variantComponentLevelTwo = it.lastOrNull { component ->
                component is VariantComponentDataModel
            } as VariantComponentDataModel
            val optionLevelTwo =
                variantComponentLevelTwo.listOfVariantCategory?.lastOrNull()?.variantOptions
                    ?: emptyList()

            assertTrue(optionLevelTwo[0].currentState == VariantConstant.STATE_EMPTY) // 254084
            assertTrue(optionLevelTwo[1].currentState == VariantConstant.STATE_UNSELECTED) // 254085
            assertTrue(optionLevelTwo[2].currentState == VariantConstant.STATE_EMPTY) // 254086
            assertTrue(optionLevelTwo[3].currentState == VariantConstant.STATE_UNSELECTED) // 254087
            assertTrue(optionLevelTwo[4].currentState == VariantConstant.STATE_SELECTED) // 254088

            assertFalse(optionLevelTwo[0].flashSale)
            assertTrue(optionLevelTwo[1].flashSale)
            assertFalse(optionLevelTwo[2].flashSale)
            assertTrue(optionLevelTwo[3].flashSale)
            assertFalse(optionLevelTwo[4].flashSale)
        }
    }

    @Test
    fun `variant level one oos selected`() {
        // please check more detail to GQL_VARIANT_AGGREGATOR_RESPONSE_JSON
        val variantLevelOne = "121018"
        val variantBiruID = "254079"

        `select variant oos by new logic`(
            optionKey = variantLevelOne,
            optionId = variantBiruID,
            selectedLevel = 1
        ) {
            // get variant level one
            val variantComponentLevelOne = it.firstOrNull { component ->
                component is VariantComponentDataModel
            } as VariantComponentDataModel
            val optionLevelOne =
                variantComponentLevelOne.listOfVariantCategory?.firstOrNull()?.variantOptions
                    ?: emptyList()

            assertTrue(optionLevelOne[0].currentState == VariantConstant.STATE_SELECTED_EMPTY) // 254079
            assertTrue(optionLevelOne[1].currentState == VariantConstant.STATE_UNSELECTED) // 254080
            assertTrue(optionLevelOne[2].currentState == VariantConstant.STATE_EMPTY) // 254081
            assertTrue(optionLevelOne[3].currentState == VariantConstant.STATE_UNSELECTED) // 254082
            assertTrue(optionLevelOne[4].currentState == VariantConstant.STATE_UNSELECTED) // 254083
        }
    }

    @Test
    fun `variant level two oos selected`() {
        // please check more detail to GQL_VARIANT_AGGREGATOR_RESPONSE_JSON
        val variantLevelTwo = "121019"
        val variantLID = "254086"

        `select variant oos by new logic`(
            optionKey = variantLevelTwo,
            optionId = variantLID,
            selectedLevel = 1
        ) {
            // get variant level one
            val variantComponentLevelTwo = it.lastOrNull { component ->
                component is VariantComponentDataModel
            } as VariantComponentDataModel
            val optionLevelTwo =
                variantComponentLevelTwo.listOfVariantCategory?.lastOrNull()?.variantOptions
                    ?: emptyList()

            assertTrue(optionLevelTwo[0].currentState == VariantConstant.STATE_EMPTY) // 254084
            assertTrue(optionLevelTwo[1].currentState == VariantConstant.STATE_UNSELECTED) // 254085
            assertTrue(optionLevelTwo[2].currentState == VariantConstant.STATE_SELECTED_EMPTY) // 254086
            assertTrue(optionLevelTwo[3].currentState == VariantConstant.STATE_UNSELECTED) // 254087
            assertTrue(optionLevelTwo[4].currentState == VariantConstant.STATE_UNSELECTED) // 254088
        }
    }
    //endregion

    //region atc
    @Test
    fun `on success delete cart`() {
        `render initial variant with given child id and hit gql tokonow campaign hide gimmick`()
        `on success atc tokonow`()

        val mockData = RemoveFromCartData(
            data = com.tokopedia.cartcommon.data.response.deletecart.Data(
                message = listOf("sukses delete cart")
            )
        )
        coEvery {
            deleteCartUseCase.executeOnBackground()
        } returns mockData

        viewModel.deleteProductInCart("2147818593")

        val cartId = slot<List<String>>()

        coVerify { deleteCartUseCase.executeOnBackground() }
        verify { deleteCartUseCase.setParams(capture(cartId)) }

        val cartIdDeleted = cartId.captured.firstOrNull()
        Assert.assertNotNull(cartIdDeleted)
        Assert.assertEquals(cartIdDeleted ?: "", "2147818593")

        Assert.assertNotNull(viewModel.deleteCartLiveData.value)
        Assert.assertTrue(viewModel.deleteCartLiveData.value is Success)
        Assert.assertNotNull(
            (viewModel.deleteCartLiveData.value as Success).data,
            "sukses delete cart"
        )

        // after delete cart, delete icon must not visible
        val quantityDataModel = (viewModel.initialData.value as Success).data.first {
            it is VariantQuantityDataModel
        } as VariantQuantityDataModel

        Assert.assertEquals(quantityDataModel.shouldShowDeleteButton, false)

        // after delete cart, button should back to cart redir + keranjang
        assertButton(expectedCartText = "+ Keranjang", expectedIsBuyable = true)
    }

    @Test
    fun `on fail delete cart`() {
        `render initial variant with given child id and hit gql tokonow campaign hide gimmick`()
        `on success atc tokonow`()

        coEvery {
            deleteCartUseCase.executeOnBackground()
        } throws Throwable("gagal delete cart")

        viewModel.deleteProductInCart("2147818593")

        val cartId = slot<List<String>>()

        coVerify { deleteCartUseCase.executeOnBackground() }
        verify { deleteCartUseCase.setParams(capture(cartId)) }

        val cartIdDeleted = cartId.captured.firstOrNull()
        Assert.assertNotNull(cartIdDeleted)
        Assert.assertEquals(cartIdDeleted ?: "", "2147818593")

        Assert.assertNotNull(viewModel.deleteCartLiveData.value)
        Assert.assertTrue(viewModel.deleteCartLiveData.value is Fail)
        Assert.assertNotNull(
            (viewModel.deleteCartLiveData.value as Fail).throwable.message,
            "gagal delete cart"
        )

        val quantityDataModel = (viewModel.initialData.value as Success).data.first {
            it is VariantQuantityDataModel
        } as VariantQuantityDataModel

        Assert.assertEquals(quantityDataModel.shouldShowDeleteButton, true)

        assertButton(expectedCartText = "Simpan Perubahan", expectedIsBuyable = true)
    }

    @Test
    fun `on success atc tokonow`() {
        `render initial variant with given child id and hit gql tokonow campaign hide gimmick`()
        val actionButtonAtc = 2

        val atcResponseSuccess = AddToCartDataModel(
            data = DataModel(
                success = 1,
                productId = "2147818593",
                cartId = "2147818593"
            ), status = "OK"
        )
        val slotRequest = slot<RequestParams>()
        coEvery {
            addToCartUseCase.createObservable(capture(slotRequest)).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0.0, "", "", true)
        verifyAtcUsecase(verifyAtc = true)

        val request =
            slotRequest.captured.getObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST) as AddToCartRequestParams
        Assert.assertTrue(viewModel.addToCartLiveData.value is Success)
        Assert.assertEquals(request.quantity, 2)
        assertButton(expectedCartText = "Simpan Perubahan", expectedIsBuyable = true)
    }

    @Test
    fun `on success atc non tokonow`() {
        `render initial variant with given parent id and hit gql non tokonow`()
        val actionButtonAtc = 2
        val slot = slot<RequestParams>()

        val atcResponseSuccess = AddToCartDataModel(
            data = DataModel(success = 1, productId = "2147818576"),
            status = "OK"
        )

        coEvery {
            addToCartUseCase.createObservable(capture(slot)).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0.0, "attribution", "trackerlist", false)
        verifyAtcUsecase(verifyAtc = true)

        val requestParams =
            slot.captured.getObject("REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST") as AddToCartRequestParams
        Assert.assertEquals(requestParams.listTracker, "trackerlist")
        Assert.assertEquals(requestParams.attribution, "attribution")
        Assert.assertTrue(viewModel.addToCartLiveData.value is Success)
        assertButton(expectedIsBuyable = true)
    }

    @Test
    fun `on fail atc`() {
        `render initial variant with given child id and hit gql tokonow campaign hide gimmick`()
        val actionButtonAtc = 2
        val atcResponseError = AddToCartDataModel(
            data = DataModel(success = 0),
            status = "",
            errorMessage = arrayListOf("gagal ya")
        )

        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseError

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0.0, "", "", true)
        verifyAtcUsecase(verifyAtc = true)

        Assert.assertTrue(viewModel.addToCartLiveData.value is Fail)
        assertButton(expectedIsBuyable = true)
    }

    @Test
    fun `on atc fail cause by throw`() {
        `render initial variant with given child id and hit gql tokonow campaign hide gimmick`()
        val actionButtonAtc = 2

        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } throws Throwable()

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0.0, "", "", true)

        Assert.assertTrue(viewModel.addToCartLiveData.value is Fail)
        assertButton(expectedIsBuyable = true)
    }

    @Test
    fun `on success update atc tokonow`() = runBlocking {
        decideSuccessValueHitGqlAggregator("2147818576", true, true)
        val actionButtonAtc = 2
        val updateCartRequest = slot<List<UpdateCartRequest>>()
        val updateAtcResponse = UpdateCartV2Data(status = "OK", data = Data(message = "sukses gan"))

        coEvery {
            updateCartUseCase.executeOnBackground()
        } returns updateAtcResponse

        viewModel.updateQuantity(50, "2147818576")
        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0.0, "", "", true)
        verifyAtcUsecase(verifyUpdateAtc = true)
        coVerify {
            updateCartUseCase.setParams(capture(updateCartRequest), any())
        }

        Assert.assertTrue(viewModel.updateCartLiveData.value is Success)
        Assert.assertEquals((viewModel.updateCartLiveData.value as Success).data, "sukses gan")
        assertButton(expectedCartText = "Simpan Perubahan", expectedIsBuyable = true)
    }

    @Test
    fun `on fail update atc`() = runBlocking {
        decideSuccessValueHitGqlAggregator("2147818576", true, true)
        val actionButtonAtc = 2
        val updateCartRequest = slot<List<UpdateCartRequest>>()
        val failUpdataAtcResponse = UpdateCartV2Data(status = "", error = listOf("asd"))

        coEvery {
            updateCartUseCase.executeOnBackground()
        } returns failUpdataAtcResponse

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0.0, "", "", true)
        verifyAtcUsecase(verifyUpdateAtc = true)
        coVerify {
            updateCartUseCase.setParams(capture(updateCartRequest), any())
        }

        Assert.assertTrue(viewModel.updateCartLiveData.value is Fail)
        assertButton(expectedCartText = "Simpan Perubahan", expectedIsBuyable = true)
    }

    @Test
    fun `on fail update atc by throwable`() = runBlocking {
        decideSuccessValueHitGqlAggregator("2147818576", true, true)
        val actionButtonAtc = 2

        coEvery {
            updateCartUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0.0, "", "", true)

        Assert.assertTrue(viewModel.updateCartLiveData.value is Fail)
        assertButton(expectedCartText = "Simpan Perubahan", expectedIsBuyable = true)
    }

    @Test
    fun `on success ocs`() {
        `render initial variant with given child id and hit gql tokonow campaign hide gimmick`()
        val actionButtonAtc = 3
        val slot = slot<RequestParams>()

        val atcResponseSuccess = AddToCartDataModel(
            data = DataModel(success = 1, productId = "2147818593"),
            status = "OK"
        )

        coEvery {
            addToCartOcsUseCase.createObservable(capture(slot)).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 30000.0, "", "", true)
        verifyAtcUsecase(verifyOcs = true)

        val requestParams =
            slot.captured.getObject("REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST") as AddToCartOcsRequestParams
        Assert.assertEquals(requestParams.shippingPrice, 30000.0, 0.0)

        Assert.assertTrue(viewModel.addToCartLiveData.value is Success)
        assertButton(expectedIsBuyable = true)
    }

    @Test
    fun `on fail ocs`() {
        `render initial variant with given child id and hit gql tokonow campaign hide gimmick`()
        val actionButtonAtc = 3

        val atcResponseError = AddToCartDataModel(
            data = DataModel(success = 0),
            status = "",
            errorMessage = arrayListOf("gagal ya")
        )

        coEvery {
            addToCartOcsUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseError

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0.0, "", "", true)
        verifyAtcUsecase(verifyOcs = true)

        Assert.assertTrue(viewModel.addToCartLiveData.value is Fail)
        assertButton(expectedIsBuyable = true)
    }

    @Test
    fun `on success occ`() {
        `render initial variant with given child id and hit gql tokonow campaign hide gimmick`()
        val actionButtonAtc = 4
        val atcResponseError = AddToCartDataModel(
            data = DataModel(success = 0),
            status = "",
            errorMessage = arrayListOf("gagal ya")
        )

        coEvery {
            addToCartOccUseCase.setParams(any()).executeOnBackground().mapToAddToCartDataModel()
        } returns atcResponseError

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0.0, "", "", true)
        verifyAtcUsecase(verifyOcc = true)

        Assert.assertTrue(viewModel.addToCartLiveData.value is Fail)
        assertButton(expectedIsBuyable = true)
    }

    @Test
    fun `on fail occ`() {
        `render initial variant with given child id and hit gql tokonow campaign hide gimmick`()
        val actionButtonAtc = 4

        val atcResponseSuccess = AddToCartDataModel(
            data = DataModel(success = 1, productId = "2147818593"),
            status = "OK"
        )

        coEvery {
            addToCartOccUseCase.setParams(any()).executeOnBackground().mapToAddToCartDataModel()
        } returns atcResponseSuccess

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0.0, "", "", true)
        verifyAtcUsecase(verifyOcc = true)

        Assert.assertTrue(viewModel.addToCartLiveData.value is Success)
        assertButton(expectedIsBuyable = true)
    }

    @Test
    fun `hit atc with empty variant data`() {
        decideFailValueHitGqlAggregator()
        val actionButtonAtc = 4

        val atcResponseSuccess = AddToCartDataModel(
            data = DataModel(success = 1, productId = "2147818593"),
            status = "OK"
        )

        coEvery {
            addToCartOccUseCase.setParams(any()).executeOnBackground().mapToAddToCartDataModel()
        } returns atcResponseSuccess

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0.0, "", "", false)
        verifyAtcUsecase(verifyOcc = true)

        Assert.assertTrue(viewModel.addToCartLiveData.value is Success)
    }
    //endregion

    //region favorite shop
    @Test
    fun `on success favorite shop`() {
        val shopId = "12345"
        val data = DataFollowShop()
        val captureParams = slot<RequestParams>()
        data.followShop = FollowShop().apply {
            isSuccess = true
            message = ""
        }
        coEvery {
            toggleFavoriteUseCase.executeOnBackground(capture(captureParams))
        } returns data

        viewModel.toggleFavorite(shopId)

        val result = viewModel.toggleFavoriteShop
        Assert.assertEquals((result.value as Success).data, true)

        // Assert request params
        Assert.assertEquals(captureParams.captured.getString("shopID", ""), "12345")
        Assert.assertEquals(captureParams.captured.getString("action", ""), "follow")
    }

    @Test
    fun `on success favorite shop but isSuccess false`() {
        val shopId = "12345"
        val data = DataFollowShop()
        val captureParams = slot<RequestParams>()

        data.followShop = FollowShop().apply {
            isSuccess = false
            message = "Fail"
        }
        coEvery {
            toggleFavoriteUseCase.executeOnBackground(capture(captureParams))
        } returns data

        viewModel.toggleFavorite(shopId)

        val result = viewModel.toggleFavoriteShop
        Assert.assertTrue(result.value is Fail)
        Assert.assertEquals((result.value as Fail).throwable.message, "Fail")

        // Assert request params
        Assert.assertEquals(captureParams.captured.getString("shopID", ""), "12345")
        Assert.assertEquals(captureParams.captured.getString("action", ""), "follow")
    }

    @Test
    fun `on fail favorite shop`() {
        val shopId = "12345"
        val captureParams = slot<RequestParams>()

        coEvery {
            toggleFavoriteUseCase.executeOnBackground(any())
        } throws Throwable("Fail")

        viewModel.toggleFavorite(shopId)

        coVerify {
            toggleFavoriteUseCase.executeOnBackground(capture(captureParams))
        }

        val result = viewModel.toggleFavoriteShop
        Assert.assertTrue(result.value is Fail)
        Assert.assertEquals((result.value as Fail).throwable.message, "Fail")

        // Assert request params
        Assert.assertEquals(captureParams.captured.getString("shopID", ""), "12345")
        Assert.assertEquals(captureParams.captured.getString("action", ""), "follow")
    }

    @Test
    fun `variant image clicked will post gallery data value`() {
        val imageUrl = "url1234"
        val productId = "2147818570"
        val userId = "123"
        val mainImageTag = "some tag"
        val type = ProductDetailGallery.Item.Type.Image

        decideSuccessValueHitGqlAggregator("2147818569", true, true)

        val defaultItem = ProductDetailGallery.Item(
            id = "",
            url = imageUrl,
            tag = mainImageTag,
            type = type
        )

        val optionIds = listOf("254079", "254080", "254081", "254082", "254083")
        val tags = listOf("Biru", "Merah", "Kuning", "Hijau", "Ungu")
        val expectedUrl =
            "https://images.tokopedia.net/img/cache/700/VqbcmM/2021/5/27/f4f95629-5b09-423c-a1c2-58691e1a2f30.jpg"

        val items = optionIds.mapIndexed { index, optionId ->
            ProductDetailGallery.Item(
                id = optionId,
                url = expectedUrl,
                tag = tags.getOrNull(index),
                type = ProductDetailGallery.Item.Type.Image
            )
        }
        val selectedId = "254079"

        viewModel.onVariantImageClicked(imageUrl, productId, userId, mainImageTag)

        val data = viewModel.variantImagesData.value

        Assert.assertEquals(defaultItem, data?.defaultItem)
        Assert.assertEquals(items, data?.items)
        Assert.assertEquals(selectedId, data?.selectedId)
        Assert.assertEquals(ProductDetailGallery.Page.VariantBottomSheet, data?.page)
    }

    @Test
    fun `variant image clicked fallback test with empty images and available default image`() {
        val imageUrl = "url1234"
        val productId = "2147818570"
        val userId = "123"
        val mainImageTag = "some tag"

        viewModel.onVariantImageClicked(imageUrl, productId, userId, mainImageTag)

        val data = viewModel.variantImagesData.value

        Assert.assertEquals(imageUrl, data?.defaultItem?.url)
        Assert.assertEquals(emptyList<ProductDetailGallery.Item>(), data?.items)
        Assert.assertEquals(ProductDetailGallery.Page.VariantBottomSheet, data?.page)
    }

    @Test
    fun `variant image clicked fallback test with empty images and empty default image`() {
        val imageUrl = ""
        val productId = "2147818570"
        val userId = "123"
        val mainImageTag = "some tag"

        viewModel.onVariantImageClicked(imageUrl, productId, userId, mainImageTag)

        val data = viewModel.variantImagesData.value

        Assert.assertEquals(null, data)
    }

    //endregion

    private fun verifyAtcUsecase(
        verifyAtc: Boolean = false,
        verifyOcs: Boolean = false,
        verifyOcc: Boolean = false,
        verifyUpdateAtc: Boolean = false
    ) {
        val inverseAtc = !verifyAtc
        val inverseOcs = !verifyOcs
        val inverseOcc = !verifyOcc
        val inverseUpdateAtc = !verifyUpdateAtc

        coVerify(inverse = inverseAtc) {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = inverseOcs) {
            addToCartOcsUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = inverseOcc) {
            addToCartOccUseCase.setParams(any()).executeOnBackground()
        }

        coVerify(inverse = inverseUpdateAtc) {
            updateCartUseCase.executeOnBackground()
        }
    }

    @Test
    fun `verify add to wishlistv2 returns success`() {
        val productId = "123"
        val resultWishlistAddV2 = AddToWishlistV2Response.Data.WishlistAddV2(success = true)

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistAddV2)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addWishlistV2(productId, "", mockListener)

        verify { addToWishlistV2UseCase.setParams(productId, "") }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify add to wishlistv2 returns fail`() {
        val productId = "123"
        val mockThrowable = mockk<Throwable>("fail")

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addWishlistV2(productId, "", mockListener)

        verify { addToWishlistV2UseCase.setParams(productId, "") }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
    }
}
