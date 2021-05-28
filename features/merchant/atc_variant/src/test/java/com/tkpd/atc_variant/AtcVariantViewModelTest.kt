package com.tkpd.atc_variant

import com.tkpd.atc_variant.data.uidata.VariantComponentDataModel
import com.tkpd.atc_variant.util.AtcVariantJsonHelper.generateParamsVariantFulfilled
import com.tokopedia.product.detail.common.data.model.aggregator.AggregatorMiniCartUiModel
import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.common.listener.WishListActionListener
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import org.junit.Assert
import org.junit.Test

/**
 * Created by Yehezkiel on 28/05/21
 */
class AtcVariantViewModelTest : BaseAtcVariantViewModelTest() {

    //region render initial data without calling gql
    /**
     * If open with parent, auto select first child buyable
     * render without calling gql, because all of the data already provided from PDP
     */
    @Test
    fun `render initial variant with given parent id and non hit gql tokonow`() {
        val aggregatorParams = generateParamsVariantFulfilled("2147818569", true)

        coEvery {
            aggregatorMiniCartUseCase.executeOnBackground(any(), any(), true)
        } returns AggregatorMiniCartUiModel()

        viewModel.decideInitialValue(aggregatorParams)

        coVerify(inverse = true) {
            aggregatorMiniCartUseCase.executeOnBackground(any(), any(), true)
        }

        Assert.assertEquals("Merah, M", viewModel.titleVariantName.value)

        val visitablesData = (viewModel.initialData.value as Success).data

        assertVisitables(visitablesData,
                showQuantityEditor = true,
                expectedSelectedOptionIds = listOf("254080", "254085"),
                expectedSelectedProductId = "2147818576",
                expectedSelectedMainPrice = 1000.getCurrencyFormatted(),
                expectedSelectedStockWording = "Stok tinggal &lt;20, beli segera!",
                expectedSelectedOptionIdsLevelOne = "254080",
                expectedSelectedOptionIdsLevelTwo = "254085",
                expectedQuantity = 23,
                expectedMinOrder = 1
        )
        assertButton((viewModel.buttonData.value as Success).data, "Perbarui Keranjang")
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
        decideInitialValueHitGql("2147818569", false)

        Assert.assertEquals("Merah, M", viewModel.titleVariantName.value)

        val visitablesData = (viewModel.initialData.value as Success).data

        assertVisitables(visitablesData,
                showQuantityEditor = false,
                expectedSelectedOptionIds = listOf("254080", "254085"),
                expectedSelectedProductId = "2147818576",
                expectedSelectedMainPrice = 1000.getCurrencyFormatted(),
                expectedSelectedStockWording = "Stok tinggal &lt;20, beli segera!",
                expectedSelectedOptionIdsLevelOne = "254080",
                expectedSelectedOptionIdsLevelTwo = "254085",
                expectedQuantity = 0,
                expectedMinOrder = 1
        )
        assertButton((viewModel.buttonData.value as Success).data, "")
    }

    /**
     * If open with parent, auto select first child buyable
     * In this case we are using Merah, M , as a first child
     * Has product in minicart
     */
    @Test
    fun `render initial variant with given parent id and hit gql tokonow`() {
        decideInitialValueHitGql("2147818569", true)

        Assert.assertEquals("Merah, M", viewModel.titleVariantName.value)

        val visitablesData = (viewModel.initialData.value as Success).data

        assertVisitables(visitablesData,
                showQuantityEditor = true,
                expectedSelectedOptionIds = listOf("254080", "254085"),
                expectedSelectedProductId = "2147818576",
                expectedSelectedMainPrice = 1000.getCurrencyFormatted(),
                expectedSelectedStockWording = "Stok tinggal &lt;20, beli segera!",
                expectedSelectedOptionIdsLevelOne = "254080",
                expectedSelectedOptionIdsLevelTwo = "254085",
                expectedQuantity = 23,
                expectedMinOrder = 1
        )
        assertButton((viewModel.buttonData.value as Success).data, "Perbarui Keranjang")
    }

    /**
     * If open with child id that buyable, auto select it to the child
     * Doesnt has product in minicart
     */
    @Test
    fun `render initial variant with given child id and hit gql tokonow`() {
        decideInitialValueHitGql("2147818593", true)
        Assert.assertEquals("Ungu, XL", viewModel.titleVariantName.value)

        val visitablesData = (viewModel.initialData.value as Success).data

        assertVisitables(visitablesData,
                showQuantityEditor = true,
                expectedSelectedOptionIds = listOf("254083", "254087"),
                expectedSelectedProductId = "2147818593",
                expectedSelectedMainPrice = 3000.getCurrencyFormatted(),
                expectedSelectedStockWording = "Stok Mau Habis",
                expectedSelectedOptionIdsLevelOne = "254083",
                expectedSelectedOptionIdsLevelTwo = "254087",
                expectedQuantity = 0,
                expectedMinOrder = 1
        )
        assertButton((viewModel.buttonData.value as Success).data, "")
    }

    /**
     * If open with child id that not buyable, select header and title as child selected.
     * But dont select variant component
     * Has product in minicart
     * The button is Ingatkan saya
     */
    @Test
    fun `render initial variant with given child id not buyable and hit gql tokonow`() {
        decideInitialValueHitGql("2147818570", true)

        Assert.assertEquals("Biru, S", viewModel.titleVariantName.value)

        val visitablesData = (viewModel.initialData.value as Success).data

        assertVisitables(visitablesData,
                showQuantityEditor = false,
                expectedSelectedOptionIds = listOf("0", "0"),
                expectedSelectedProductId = "2147818570",
                expectedSelectedMainPrice = 1000.getCurrencyFormatted(),
                expectedSelectedStockWording = "Stok tinggal &lt;20, beli segera!",
                expectedSelectedOptionIdsLevelOne = "0",
                expectedSelectedOptionIdsLevelTwo = "0",
                expectedQuantity = 2,
                expectedMinOrder = 3
        )

        assertButton((viewModel.buttonData.value as Success).data,
                expectedAlternateCopy = "Perbarui Keranjang",
                expectedIsBuyable = false,
                expectedCartText = "Ingatkan Saya",
                expectedCartColor = "secondary_green",
                expectedCartType = "remind_me")
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
        //call to fill initial value
        `render initial variant with given parent id and hit gql non tokonow`()

        val warnaId = "121018"
        val hijauId = "254082"

        viewModel.onVariantClicked(false, warnaId, hijauId, "image variant", 1)

        Assert.assertEquals("Hijau, M", viewModel.titleVariantName.value)

        val visitablesData = (viewModel.initialData.value as Success).data
        assertVisitables(visitablesData,
                showQuantityEditor = false,
                expectedSelectedOptionIds = listOf("254082", "254085"),
                expectedSelectedProductId = "2147818586",
                expectedSelectedMainPrice = 7000.getCurrencyFormatted(),
                expectedSelectedStockWording = "Stok tinggal &lt;20, beli segera!",
                expectedSelectedOptionIdsLevelOne = "254082",
                expectedSelectedOptionIdsLevelTwo = "254085",
                expectedQuantity = 0,
                expectedMinOrder = 1
        )

        assertButton((viewModel.buttonData.value as Success).data,
                expectedAlternateCopy = "",
                expectedIsBuyable = true,
                expectedCartText = "+ Keranjang Hijau M")

        val updateResultData = viewModel.variantActivityResult.value
        val variantDataVisitable = visitablesData[1] as VariantComponentDataModel
        Assert.assertTrue(updateResultData != null)
        Assert.assertTrue(updateResultData!!.listOfVariantSelected!!.containsAll(variantDataVisitable.listOfVariantCategory!!))
        Assert.assertTrue(updateResultData.mapOfSelectedVariantOption?.values?.toList()?.containsAll(variantDataVisitable.mapOfSelectedVariant.values.toList())
                ?: false)
        Assert.assertEquals(updateResultData.selectedProductId, "2147818586")
    }

    /**
     * Clicked variant level 1, with condition level 2 not being selected
     * means there are 2 levels variant, no one has selected
     * expected header and quantity editor not being updated only selected being updated
     */
    @Test
    fun `render variant after change level one in empty selected variant`() {
        //call to fill initial value, with not buyable because we dont want to select any variant here
        `render initial variant with given child id not buyable and hit gql tokonow`()

        val warnaId = "121018"
        val merahId = "254080"

        viewModel.onVariantClicked(true, warnaId, merahId, "image variant", 1)

        Assert.assertEquals("Merah", viewModel.titleVariantName.value)

        val visitablesData = (viewModel.initialData.value as Success).data
        val expectedLevelOneVariantIdChanged = merahId
        assertVisitables(visitablesData,
                showQuantityEditor = false,
                expectedSelectedOptionIds = listOf(expectedLevelOneVariantIdChanged, "0"),
                expectedSelectedProductId = "2147818570",
                expectedSelectedMainPrice = 1000.getCurrencyFormatted(),
                expectedSelectedStockWording = "Stok tinggal &lt;20, beli segera!",
                expectedSelectedOptionIdsLevelOne = expectedLevelOneVariantIdChanged,
                expectedSelectedOptionIdsLevelTwo = "0",
                expectedQuantity = 0,
                expectedMinOrder = 0
        )

        assertButton((viewModel.buttonData.value as Success).data,
                expectedAlternateCopy = "Perbarui Keranjang",
                expectedIsBuyable = false,
                expectedCartText = "Ingatkan Saya",
                expectedCartColor = "secondary_green",
                expectedCartType = "remind_me")
    }
    //endregion

    //region wishlist or ingatkan saya clicked
    @Test
    fun `on success clicked ingatkan saya`() {
        //fulfill cart redirection data
        `render initial variant with given child id not buyable and hit gql tokonow`()

        val productId = "2147818570"
        every { (addWishListUseCase.createObservable(any(), any(), any())) }.answers {
            val listener = args[2] as WishListActionListener
            listener.onSuccessAddWishlist(productId)
        }

        val expectedTextChanged = "cek wishlist"
        viewModel.addWishlist(productId, "", expectedTextChanged)

        val updateResultData = viewModel.variantActivityResult.value
        Assert.assertTrue(updateResultData != null)
        Assert.assertEquals(updateResultData?.shouldRefreshPreviousPage ?: false, true)

        val buttonData = (viewModel.buttonData.value as Success).data
        assertButton(buttonData, "", false, "check_wishlist", "secondary_gray", expectedTextChanged)

        Assert.assertTrue(viewModel.addWishlistResult.value is Success)
    }

    @Test
    fun `on fail clicked ingatkan saya`() {
        //fulfill cart redirection data
        `render initial variant with given child id not buyable and hit gql tokonow`()

        val productId = "2147818570"
        every { (addWishListUseCase.createObservable(any(), any(), any())) }.answers {
            val listener = args[2] as WishListActionListener
            listener.onErrorAddWishList("gagal", productId)
        }

        val expectedTextChanged = "cek wishlist"
        viewModel.addWishlist(productId, "", expectedTextChanged)

        val updateResultData = viewModel.variantActivityResult.value
        Assert.assertTrue(updateResultData == null)

        val buttonData = (viewModel.buttonData.value as Success).data
        assertButton(buttonData, "Perbarui Keranjang", false, "remind_me", "secondary_green", "Ingatkan Saya")

        Assert.assertTrue(viewModel.addWishlistResult.value is Fail)
    }
    //endregion
}