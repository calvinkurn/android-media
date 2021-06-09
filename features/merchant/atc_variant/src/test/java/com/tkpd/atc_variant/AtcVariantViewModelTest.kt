package com.tkpd.atc_variant

import com.tkpd.atc_variant.data.uidata.VariantComponentDataModel
import com.tkpd.atc_variant.util.AtcVariantJsonHelper.generateParamsVariantFulfilled
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.minicart.common.data.response.updatecart.Data
import com.tokopedia.minicart.common.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.product.detail.common.data.model.aggregator.AggregatorMiniCartUiModel
import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.common.listener.WishListActionListener
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

/**
 * Created by Yehezkiel on 28/05/21
 */
class AtcVariantViewModelTest : BaseAtcVariantViewModelTest() {

    //region helper function
    @Test
    fun `fail get selected option ids`() {
        decideFailValueHitGqlAggregator()

        val selectedOptionIds = viewModel.getSelectedOptionIds()

        Assert.assertNull(selectedOptionIds)
    }

    @Test
    fun `success get selected option ids`() {
        decideSuccessValueHitGqlAggregator("2147818569", true)

        val selectedOptionIds = viewModel.getSelectedOptionIds()

        Assert.assertNotNull(selectedOptionIds)
    }

    @Test
    fun `success get variant data`() {
        decideSuccessValueHitGqlAggregator("2147818569", true)

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
        assertButton("Perbarui Keranjang")
    }
    //endregion

    //region test determine hit gql or not
    @Test
    fun `test ignore hit gql when !tokonow and mini cart null`() {
        val aggregatorParams = generateParamsVariantFulfilled("2147818569", false)

        coEvery {
            aggregatorMiniCartUseCase.executeOnBackground(any(), any(), false)
        } returns AggregatorMiniCartUiModel()

        viewModel.decideInitialValue(aggregatorParams)

        coVerify(inverse = true) {
            aggregatorMiniCartUseCase.executeOnBackground(any(), any(), false)
        }
    }

    @Test
    fun `test must hit gql when tokonow and mini cart null`() {
        val aggregatorParams = generateParamsVariantFulfilled("2147818569", true, true)

        coEvery {
            aggregatorMiniCartUseCase.executeOnBackground(any(), any(), true)
        } returns AggregatorMiniCartUiModel()

        viewModel.decideInitialValue(aggregatorParams)

        coVerify {
            aggregatorMiniCartUseCase.executeOnBackground(any(), any(), true)
        }
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
        decideSuccessValueHitGqlAggregator("2147818569", false)

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
        assertButton("")
    }

    /**
     * If open with parent, auto select first child buyable
     * In this case we are using Merah, M , as a first child
     * Has product in minicart
     */
    @Test
    fun `render initial variant with given parent id and hit gql tokonow`() {
        decideSuccessValueHitGqlAggregator("2147818569", true)

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
        assertButton("Perbarui Keranjang")
    }

    /**
     * If open with child id that buyable, auto select it to the child
     * Doesnt has product in minicart
     */
    @Test
    fun `render initial variant with given child id and hit gql tokonow`() {
        decideSuccessValueHitGqlAggregator("2147818593", true)
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
        assertButton("")
    }

    /**
     * If open with child id that not buyable, select header and title as child selected.
     * But dont select variant component
     * Has product in minicart
     * The button is Ingatkan saya
     */
    @Test
    fun `render initial variant with given child id not buyable and hit gql tokonow`() {
        decideSuccessValueHitGqlAggregator("2147818570", true)

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

        assertButton(expectedAlternateCopy = "Perbarui Keranjang",
                expectedIsBuyable = false,
                expectedCartType = "remind_me",
                expectedCartColor = "secondary_green",
                expectedCartText = "Ingatkan Saya")
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

        assertButton(expectedAlternateCopy = "",
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

        assertButton(expectedAlternateCopy = "Perbarui Keranjang",
                expectedIsBuyable = false,
                expectedCartType = "remind_me",
                expectedCartColor = "secondary_green",
                expectedCartText = "Ingatkan Saya")
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

        assertButton("", false, "check_wishlist", "secondary_gray", expectedTextChanged)
        Assert.assertTrue(viewModel.addWishlistResult.value is Success)
    }

    @Test
    fun `on success clicked ingatkan saya with empty data`() {
        decideFailValueHitGqlAggregator()

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

        assertButton("", false, null, null, null)
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

        assertButton("Perbarui Keranjang", false, "remind_me", "secondary_green", "Ingatkan Saya")

        Assert.assertTrue(viewModel.addWishlistResult.value is Fail)
    }
    //endregion

    //region atc
    @Test
    fun `on success atc tokonow`() {
        `render initial variant with given child id and hit gql tokonow`()
        val actionButtonAtc = 2

        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1, productId = 2147818593L), status = "OK")

        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0, "", "", true)
        verifyAtcUsecase(verifyAtc = true)

        Assert.assertTrue(viewModel.addToCartLiveData.value is Success)
        assertButton(expectedAlternateCopy = "Perbarui Keranjang", expectedIsBuyable = true)
    }

    @Test
    fun `on success atc non tokonow`() {
        `render initial variant with given parent id and hit gql non tokonow`()
        val actionButtonAtc = 2
        val slot = slot<RequestParams>()

        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1, productId = 2147818576), status = "OK")

        coEvery {
            addToCartUseCase.createObservable(capture(slot)).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0, "attribution", "trackerlist", false)
        verifyAtcUsecase(verifyAtc = true)

        val requestParams = slot.captured.getObject("REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST") as AddToCartRequestParams
        Assert.assertEquals(requestParams.listTracker, "trackerlist")
        Assert.assertEquals(requestParams.attribution, "attribution")
        Assert.assertTrue(viewModel.addToCartLiveData.value is Success)
        assertButton(expectedAlternateCopy = "", expectedIsBuyable = true)
    }

    @Test
    fun `on fail atc`() {
        `render initial variant with given child id and hit gql tokonow`()
        val actionButtonAtc = 2
        val atcResponseError = AddToCartDataModel(data = DataModel(success = 0), status = "", errorMessage = arrayListOf("gagal ya"))

        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseError

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0, "", "", true)
        verifyAtcUsecase(verifyAtc = true)

        Assert.assertTrue(viewModel.addToCartLiveData.value is Fail)
        assertButton(expectedAlternateCopy = "", expectedIsBuyable = true)
    }

    @Test
    fun `on success update atc tokonow`() = runBlocking {
        decideSuccessValueHitGqlAggregator("2147818576", true)
        val actionButtonAtc = 2
        val miniCartItem = slot<List<MiniCartItem>>()
        val updateAtcResponse = UpdateCartV2Data(status = "OK", data = Data(message = "sukses gan"))

        coEvery {
            updateCartUseCase.executeOnBackground()
        } returns updateAtcResponse

        viewModel.updateQuantity(50, "2147818576")
        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0, "", "", true)
        verifyAtcUsecase(verifyUpdateAtc = true)
        coVerify {
            updateCartUseCase.setParams(capture(miniCartItem))
        }
        print(miniCartItem)
        Assert.assertNotNull(miniCartItem.captured.firstOrNull { it.productId == "2147818576" })
        Assert.assertEquals(miniCartItem.captured.firstOrNull { it.productId == "2147818576" }!!.quantity, 50)
        Assert.assertTrue(viewModel.updateCartLiveData.value is Success)
        Assert.assertEquals((viewModel.updateCartLiveData.value as Success).data, "sukses gan")
        assertButton(expectedAlternateCopy = "Perbarui Keranjang", expectedIsBuyable = true)
    }

    @Test
    fun `on fail update atc`() = runBlocking {
        decideSuccessValueHitGqlAggregator("2147818576", true)
        val actionButtonAtc = 2
        val miniCartItem = slot<List<MiniCartItem>>()
        val failUpdataAtcResponse = UpdateCartV2Data(status = "", error = listOf("asd"))

        coEvery {
            updateCartUseCase.executeOnBackground()
        } returns failUpdataAtcResponse

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0, "", "", true)
        verifyAtcUsecase(verifyUpdateAtc = true)
        coVerify {
            updateCartUseCase.setParams(capture(miniCartItem))
        }
        print(miniCartItem)
        Assert.assertNotNull(miniCartItem.captured.firstOrNull { it.productId == "2147818576" })
        Assert.assertEquals(miniCartItem.captured.firstOrNull { it.productId == "2147818576" }!!.quantity, 23)
        Assert.assertTrue(viewModel.updateCartLiveData.value is Fail)
        assertButton(expectedAlternateCopy = "Perbarui Keranjang", expectedIsBuyable = true)
    }

    @Test
    fun `on success ocs`() {
        `render initial variant with given child id and hit gql tokonow`()
        val actionButtonAtc = 3
        val slot = slot<RequestParams>()

        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1, productId = 2147818593L), status = "OK")

        coEvery {
            addToCartOcsUseCase.createObservable(capture(slot)).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 30000, "", "", true)
        verifyAtcUsecase(verifyOcs = true)

        val requestParams = slot.captured.getObject("REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST") as AddToCartOcsRequestParams
        Assert.assertEquals(requestParams.shippingPrice, 30000)

        Assert.assertTrue(viewModel.addToCartLiveData.value is Success)
        assertButton(expectedAlternateCopy = "", expectedIsBuyable = true)
    }

    @Test
    fun `on fail ocs`() {
        `render initial variant with given child id and hit gql tokonow`()
        val actionButtonAtc = 3

        val atcResponseError = AddToCartDataModel(data = DataModel(success = 0), status = "", errorMessage = arrayListOf("gagal ya"))

        coEvery {
            addToCartOcsUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseError

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0, "", "", true)
        verifyAtcUsecase(verifyOcs = true)

        Assert.assertTrue(viewModel.addToCartLiveData.value is Fail)
        assertButton(expectedAlternateCopy = "", expectedIsBuyable = true)
    }

    @Test
    fun `on success occ`() {
        `render initial variant with given child id and hit gql tokonow`()
        val actionButtonAtc = 4
        val atcResponseError = AddToCartDataModel(data = DataModel(success = 0), status = "", errorMessage = arrayListOf("gagal ya"))

        coEvery {
            addToCartOccUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseError

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0, "", "", true)
        verifyAtcUsecase(verifyOcc = true)

        Assert.assertTrue(viewModel.addToCartLiveData.value is Fail)
        assertButton(expectedAlternateCopy = "", expectedIsBuyable = true)
    }

    @Test
    fun `on fail occ`() {
        `render initial variant with given child id and hit gql tokonow`()
        val actionButtonAtc = 4

        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1, productId = 2147818593L), status = "OK")

        coEvery {
            addToCartOccUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0, "", "", true)
        verifyAtcUsecase(verifyOcc = true)

        Assert.assertTrue(viewModel.addToCartLiveData.value is Success)
        assertButton(expectedAlternateCopy = "", expectedIsBuyable = true)
    }

    @Test
    fun `hit atc with empty variant data`() {
        decideFailValueHitGqlAggregator()
        val actionButtonAtc = 4

        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1, productId = 2147818593L), status = "OK")

        coEvery {
            addToCartOccUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.hitAtc(actionButtonAtc, 1234, "", "321", 0, "", "", false)
        verifyAtcUsecase(verifyOcc = true)

        Assert.assertTrue(viewModel.addToCartLiveData.value is Success)
    }
    //endregion

    private fun verifyAtcUsecase(verifyAtc: Boolean = false, verifyOcs: Boolean = false, verifyOcc: Boolean = false, verifyUpdateAtc: Boolean = false) {
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
            addToCartOccUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = inverseUpdateAtc) {
            updateCartUseCase.executeOnBackground()
        }
    }
}