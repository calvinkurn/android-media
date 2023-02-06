package com.tokopedia.product.addedit.preview.presentation.viewmodel

import androidx.lifecycle.MediatorLiveData
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.PREFIX_CACHE
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.preview.data.model.responses.ValidateProductNameResponse
import com.tokopedia.product.addedit.preview.data.source.api.response.Product
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.productlimitation.domain.model.ProductAddRuleResponse
import com.tokopedia.product.addedit.productlimitation.domain.model.ProductLimitationData
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.specification.domain.model.AnnotationCategoryData
import com.tokopedia.product.addedit.specification.domain.model.AnnotationCategoryResponse
import com.tokopedia.product.addedit.specification.domain.model.DrogonAnnotationCategoryV2
import com.tokopedia.product.addedit.specification.domain.model.Values
import com.tokopedia.product.addedit.util.getOrAwaitValue
import com.tokopedia.product.addedit.util.getPrivateProperty
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.ValidationResultModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.product.manage.common.feature.draft.data.model.ProductDraft
import com.tokopedia.product.manage.common.feature.getstatusshop.data.model.StatusInfo
import com.tokopedia.shop.common.constant.AccessId
import com.tokopedia.shop.common.graphql.data.shopopen.SaveShipmentLocation
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import junit.framework.Assert
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class AddEditProductPreviewViewModelTest: AddEditProductPreviewViewModelTestFixture() {

    private val defaultShopId = "123"

    @Test
    fun `When save and get product draft are success Expect can be saved and retrieved data draft`() = runBlocking {
        val productDraft = ProductDraft().apply {
            draftId = 1112
            productId = 220
        }

        onSaveProductDraft_thenReturn(productDraft.draftId)
        viewModel.saveProductDraft(productDraft, productDraft.draftId, false)

        onGetProductDraft_thenReturn(productDraft)
        viewModel.getProductDraft(productDraft.draftId)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify { saveProductDraftUseCase.executeOnBackground() }
        coVerify { getProductDraftUseCase.executeOnBackground() }

        verifySaveProductDraftResult(Success(productDraft.draftId))
        verifyGetProductDraftResult(Success(productDraft))
    }

    @Test
    fun  `When get remote product is success Expect set product input model`() = runBlocking {
        val product: Product = Product().copy(
                productID = "01919",
                productName = "mainan",
                price = 1000.toBigInteger()
        )
        onGetProduct_thenReturn(product)
        onGetIsShopOwner_thenReturn(true)
        onGetShopId_thenReturnDefault()
        onGetAdminProductPermission_thenReturn(true)
        onGetAdminEditStockPermission_thenReturn(true)
        viewModel.setProductId(product.productID)
        viewModel.getProductData(product.productID)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify { getProductUseCase.executeOnBackground() }
        verifyGetProductResult(Success(product))
    }

    @Test
    fun `When save and get product draft are fail Expect can't be saved and retrieved data draft`() = runBlocking {
        onSaveProductDraft_thenFailed()
        viewModel.saveProductDraft(ProductDraft(), 3, false)

        onGetProductDraft_thenFailed()
        viewModel.getProductDraft(3)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify { saveProductDraftUseCase.executeOnBackground() }
        coVerify { getProductDraftUseCase.executeOnBackground() }

        verifySaveProductDraftFailed()
        verifyGetProductDraftFailed()
    }

    @Test
    fun `When get remote product is fail Expect fail object`() = runBlocking {
        onGetProduct_thenFailed()
        onGetIsShopOwner_thenReturn(true)
        onGetShopId_thenReturnDefault()
        onGetAdminProductPermission_thenReturn(true)
        onGetAdminEditStockPermission_thenReturn(true)
        viewModel.setProductId("4")
        viewModel.getProductData("4")

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify { getProductUseCase.executeOnBackground() }
        verifyGetProductFailed()
    }

    @Test
    fun `When validate product input model Expect return error message`() {
        val detailInputModel = DetailInputModel()

        detailInputModel.categoryId = "123"
        detailInputModel.imageUrlOrPathList = listOf()
        assertEquals(resourceProvider.getInvalidCategoryIdErrorMessage(), viewModel.validateProductInput(detailInputModel))

        detailInputModel.categoryId = "0"
        assertEquals(resourceProvider.getInvalidCategoryIdErrorMessage(), viewModel.validateProductInput(detailInputModel))

        detailInputModel.categoryId = ""
        assertEquals(resourceProvider.getInvalidCategoryIdErrorMessage(), viewModel.validateProductInput(detailInputModel))

        detailInputModel.imageUrlOrPathList = listOf("one","two","three","four","five","six")
        assertEquals(resourceProvider.getInvalidPhotoCountErrorMessage(), viewModel.validateProductInput(detailInputModel))

        every {
            resourceProvider.getInvalidCategoryIdErrorMessage()
        } returns null

        every {
            resourceProvider.getInvalidPhotoCountErrorMessage()
        } returns null

        every {
            resourceProvider.getInvalidPhotoReachErrorMessage()
        } returns null

        detailInputModel.categoryId = "123"
        detailInputModel.imageUrlOrPathList = listOf()
        assertEquals("", viewModel.validateProductInput(detailInputModel))

        detailInputModel.categoryId = "0"
        assertEquals("", viewModel.validateProductInput(detailInputModel))

        detailInputModel.categoryId = ""
        assertEquals("", viewModel.validateProductInput(detailInputModel))

        detailInputModel.imageUrlOrPathList = listOf("one","two","three","four","five","six")
        assertEquals("", viewModel.validateProductInput(detailInputModel))
    }

    @Test
    fun `When set image url path list Expect should set image url path list`() {
        viewModel.productInputModel.value = ProductInputModel()
        viewModel.productInputModel.getOrAwaitValue()

        viewModel.getNewProductInputModel(arrayListOf())
        viewModel.productInputModel.getOrAwaitValue()

        assertTrue(viewModel.productInputModel.value?.detailInputModel != null)
    }

    @Test
    fun `When increase or decrease wholesale Expect should increase or decrease wholesale`() {
        var wholeSaleInputModel = WholeSaleInputModel().apply { quantity = "2" }
        assertEquals("3", viewModel.incrementWholeSaleMinOrder(listOf(wholeSaleInputModel)).last().quantity)

        wholeSaleInputModel = WholeSaleInputModel().apply { quantity = "2" }
        assertEquals("1", viewModel.decrementWholeSaleMinOrder(listOf(wholeSaleInputModel)).last().quantity)
    }

    @Test
    fun `When check product id and is adding Expect should return expected result`() {
        assertEquals("", viewModel.getProductId())
        assertEquals(true , viewModel.isAdding)

        viewModel.setProductId("112")
        assertEquals("112", viewModel.getProductId())
    }

    @Test
    fun `When check draft id Expect should return expected result`() {
        viewModel.setDraftId("10")
        assertEquals(10L , viewModel.getDraftId())

        viewModel.setDraftId("")
        assertEquals(0 , viewModel.getDraftId())
    }

    @Test
    fun `When check is duplicate Expect should return expected result`() = runBlocking {
        viewModel.setIsDuplicate(true)
        viewModel.isDuplicate = true

        val mGetProductResult = viewModel.getPrivateProperty<AddEditProductPreviewViewModel,
                MediatorLiveData<Result<Product>>>("mGetProductResult")
        mGetProductResult?.value = Success(Product())

        // assert product Id is reset to 0 when at duplicate product
        assertTrue(viewModel.productInputModel.value?.productId == 0L)
    }

    @Test
    fun `When check isDataChanged true Expect should return expected result`() = runBlocking {
        viewModel.productInputModel.value = ProductInputModel(productId = 123L, isDataChanged = true)

        val mGetProductResult = viewModel.getPrivateProperty<AddEditProductPreviewViewModel,
                MediatorLiveData<Result<Product>>>("mGetProductResult")
        mGetProductResult?.value = Success(Product())
        mGetProductResult?.value = Fail(Throwable())

        // assert product Id is not changed
        assertTrue(viewModel.productInputModel.value?.productId == 123L)
    }

    @Test
    fun `When check isDataChanged false Expect should return expected result`() = runBlocking {
        viewModel.productInputModel.value = ProductInputModel(productId = 123L, isDataChanged = false)

        val mGetProductResult = viewModel.getPrivateProperty<AddEditProductPreviewViewModel,
                MediatorLiveData<Result<Product>>>("mGetProductResult")
        mGetProductResult?.value = Success(Product())
        mGetProductResult?.value = Fail(Throwable())

        // assert product Id is changed
        assertTrue(viewModel.productInputModel.value?.productId != 123L)
    }

    @Test
    fun `When check is editing Expect should return expected result`() {
        viewModel.setProductId("112")

        viewModel.isEditing.getOrAwaitValue()
        assertEquals(true, viewModel.isEditing.value)
    }

    @Test
    fun `When check product domain Expect should return expected result`() {
        val product = Product(
                productID = "12312",
                productName = "Rice"
        )
        viewModel.productDomain = product
        assertEquals(product, viewModel.productDomain)
    }

    @Test
    fun `When check shouldShowMultiLocationTicker Expect should return expected result`() {
        viewModel.setProductId("")
        onGetIsMultiLocationShop_thenReturn(true)
        onGetIsShopOwner_thenReturn(true)
        onGetIsShopAdmin_thenReturn(true)
        assertTrue(viewModel.shouldShowMultiLocationTicker)

        viewModel.setProductId("")
        onGetIsMultiLocationShop_thenReturn(true)
        onGetIsShopOwner_thenReturn(true)
        onGetIsShopAdmin_thenReturn(false)
        assertTrue(viewModel.shouldShowMultiLocationTicker)

        viewModel.setProductId("")
        onGetIsMultiLocationShop_thenReturn(true)
        onGetIsShopOwner_thenReturn(false)
        onGetIsShopAdmin_thenReturn(true)
        assertTrue(viewModel.shouldShowMultiLocationTicker)

        viewModel.setProductId("")
        onGetIsMultiLocationShop_thenReturn(true)
        onGetIsShopOwner_thenReturn(false)
        onGetIsShopAdmin_thenReturn(false)
        assertFalse(viewModel.shouldShowMultiLocationTicker)

        viewModel.setProductId("")
        onGetIsMultiLocationShop_thenReturn(false)
        onGetIsShopOwner_thenReturn(true)
        onGetIsShopAdmin_thenReturn(true)
        assertFalse(viewModel.shouldShowMultiLocationTicker)

        viewModel.setProductId("12")
        onGetIsMultiLocationShop_thenReturn(true)
        onGetIsShopOwner_thenReturn(true)
        onGetIsShopAdmin_thenReturn(false)
        assertFalse(viewModel.shouldShowMultiLocationTicker)

    }

    @Test
    fun `When check product input model Expect should return expected result`() {
        val product = MediatorLiveData<ProductInputModel>()
        product.value = ProductInputModel()

        viewModel.productInputModel = product
        assertEquals(product, viewModel.productInputModel)
    }

    @Test
    fun `when getMaxProductPhotos, expect correct max product picture`() {
        every { userSession.isShopOfficialStore } returns true
        var maxPicture = viewModel.getMaxProductPhotos()
        Assert.assertEquals(AddEditProductDetailConstants.MAX_PRODUCT_PHOTOS_OS, maxPicture)

        every { userSession.isShopOfficialStore } returns false
        maxPicture = viewModel.getMaxProductPhotos()
        Assert.assertEquals(AddEditProductDetailConstants.MAX_PRODUCT_PHOTOS, maxPicture)
    }

    @Test
    fun `When update product photos Expect updated product photos`() {
        var pictureInputModel = PictureInputModel().apply {
            urlOriginal = "www.blank.com"
            fileName = "apa"
            urlThumbnail = "www.gmail.com"
        }
        var product = ProductInputModel().apply {
            detailInputModel.pictureList = listOf(pictureInputModel, pictureInputModel, pictureInputModel)
            detailInputModel.imageUrlOrPathList = listOf("ada", "apa", "ada")
        }
        viewModel.productInputModel.value = product
        viewModel.productInputModel.getOrAwaitValue()

        var imagePickerResult = arrayListOf("pict1.0","pict2","pict3")
        var originalImageUrl = arrayListOf("www.blank.com","num2","www.blank.com")
        var editted = arrayListOf(false,false,true)

        viewModel.updateProductPhotos(imagePickerResult, originalImageUrl, editted)
        viewModel.productInputModel.getOrAwaitValue()

        assertTrue(viewModel.imageUrlOrPathList.value != null)

        pictureInputModel = PictureInputModel().apply {
            urlOriginal = ""
            fileName = "apa"
        }
        product = ProductInputModel().apply {
            detailInputModel.pictureList = listOf(pictureInputModel, pictureInputModel)
            detailInputModel.imageUrlOrPathList = listOf("ada", "apa")
        }
        viewModel.productInputModel.value = product
        viewModel.productInputModel.getOrAwaitValue()

        imagePickerResult = arrayListOf("pict1","pict2","pict3")
        originalImageUrl = arrayListOf("www.blank.com","num2","num3")
        editted = arrayListOf(true,false,true)

        viewModel.updateProductPhotos(imagePickerResult, originalImageUrl, editted)
        viewModel.productInputModel.getOrAwaitValue()

        assertTrue(viewModel.imageUrlOrPathList.value != null)

        val pictureInputModel2 = PictureInputModel().apply {
            urlOriginal = "www.blank.com"
            fileName = "apa"
            urlThumbnail = ""
        }
        product = ProductInputModel().apply {
            detailInputModel.pictureList = listOf(pictureInputModel, pictureInputModel2)
            detailInputModel.imageUrlOrPathList = listOf("ada", "apa")
        }
        viewModel.productInputModel.value = product
        viewModel.productInputModel.getOrAwaitValue()

        imagePickerResult = arrayListOf("pict1","pict2","pict3")
        originalImageUrl = arrayListOf("www.blank.com","num2","num3")
        editted = arrayListOf(false,false,false)

        viewModel.updateProductPhotos(imagePickerResult, originalImageUrl, editted)
        viewModel.productInputModel.getOrAwaitValue()

        assertTrue(viewModel.imageUrlOrPathList.value != null)

        viewModel.productInputModel.value = null

        viewModel.updateProductPhotos(imagePickerResult, originalImageUrl, editted)
        viewModel.productInputModel.getOrAwaitValue()

        assertEquals(null, viewModel.productInputModel.value)
    }

    @Test
    fun `When update product photos Expect updated url path list`() {
        val successImageUrlOrPathList: List<String> = listOf(AddEditProductConstants.HTTP_PREFIX, "/path", AddEditProductConstants.HTTP_PREFIX + "/")
        val successPictureList1: List<PictureInputModel> = listOf(PictureInputModel(
            urlThumbnail = AddEditProductConstants.HTTP_PREFIX,
            urlOriginal = AddEditProductConstants.HTTP_PREFIX + "/"
        ))
        val successPictureList2: List<PictureInputModel> = listOf(PictureInputModel(
            urlThumbnail = AddEditProductConstants.HTTP_PREFIX,
        ))
        val errorImageUrlOrPathList: List<String> = listOf(AddEditProductConstants.HTTP_PREFIX, "/path")
        val errorPictureList: List<PictureInputModel> = listOf()

        viewModel.updateProductPhotos(successImageUrlOrPathList, successPictureList1)
        assert(viewModel.imageUrlOrPathList.getOrAwaitValue().isNotEmpty())
        viewModel.updateProductPhotos(successImageUrlOrPathList, successPictureList2)
        assert(viewModel.imageUrlOrPathList.getOrAwaitValue().isEmpty())
        viewModel.updateProductPhotos(errorImageUrlOrPathList, errorPictureList)
        assert(viewModel.imageUrlOrPathList.getOrAwaitValue().isEmpty())
    }

    @Test
    fun `When updatePhotos expect not any update photo`(){
        val originalImage = arrayListOf("www.blank.com/cache/", "www.blank.com/cache/", "www.blank.com/cache/")
        val imagePickerResult = arrayListOf("","","")
        val product = inputProductModelDummy()
        viewModel.productInputModel.value = product
        viewModel.productInputModel.getOrAwaitValue()

        viewModel.updateProductPhotos(imagePickerResult, originalImage)
        assert(viewModel.imageUrlOrPathList.getOrAwaitValue().isNotEmpty())
        assertEquals(viewModel.imageUrlOrPathList.getOrAwaitValue(), originalImage)
    }

    @Test
    fun `When updatePhotos expect have update photo`(){
        val originalImage = arrayListOf("0/tkpd/cache/${PREFIX_CACHE}1", "0/tkpd/cache/${PREFIX_CACHE}2", "0/tkpd/cache/${PREFIX_CACHE}3")
        val imagePickerResult = arrayListOf("a/0/tkpd/102012.jpg","","")
        val product = inputProductModelDummy()
        val expectedResult = arrayListOf("a/0/tkpd/102012.jpg","www.blank.com/cache/", "www.blank.com/cache/")
        viewModel.productInputModel.value = product
        viewModel.productInputModel.getOrAwaitValue()

        viewModel.updateProductPhotos(imagePickerResult, originalImage)
        assert(viewModel.imageUrlOrPathList.getOrAwaitValue().isNotEmpty())
        assertEquals(viewModel.imageUrlOrPathList.getOrAwaitValue(), expectedResult)
    }

    @Test
    fun `When updatePhotos expect have new photo but not edited and edited photo`(){
        val originalImage = arrayListOf("0/tkpd/cache/${PREFIX_CACHE}1", "0/tkpd/cache/${PREFIX_CACHE}2", "0/tkpd/cache/${PREFIX_CACHE}3", "a/0/tkpd/102013.jpg")
        val imagePickerResult = arrayListOf("a/0/tkpd/102012.jpg","", "","")
        val product = inputProductModelDummy()
        val expectedResult = arrayListOf("a/0/tkpd/102012.jpg","www.blank.com/cache/", "www.blank.com/cache/", "a/0/tkpd/102013.jpg")
        viewModel.productInputModel.value = product
        viewModel.productInputModel.getOrAwaitValue()

        viewModel.updateProductPhotos(imagePickerResult, originalImage)
        assert(viewModel.imageUrlOrPathList.getOrAwaitValue().isNotEmpty())
        assertEquals(viewModel.imageUrlOrPathList.getOrAwaitValue(), expectedResult)
    }

    @Test
    fun `When updatePhotos but productInputModel is null`(){
        val originalImage = arrayListOf("0/tkpd/cache/1", "0/tkpd/cache/2", "0/tkpd/cache/3", "a/0/tkpd/102013.jpg")
        val imagePickerResult = arrayListOf("a/0/tkpd/102012.jpg","", "","")
        viewModel.updateProductPhotos(imagePickerResult, originalImage)
    }

    @Test
    fun `When update product status Expect should update product status`() {
        val product = MediatorLiveData<ProductInputModel>()
        product.value = null
        viewModel.productInputModel = product
        viewModel.updateProductStatus(true)
        assertEquals(null, viewModel.productInputModel.value)

        val productVariantInputModel = ProductVariantInputModel().apply { status = "INACTIVE" }
        viewModel.productInputModel.value = ProductInputModel().apply {
            draftId = 109
            productId = 211
            detailInputModel.productName = "mainan"
            detailInputModel.status = 0
            variantInputModel.products = listOf(productVariantInputModel)
        }
        viewModel.productInputModel.getOrAwaitValue()

        viewModel.updateProductStatus(true)
        viewModel.productInputModel.getOrAwaitValue()
        var actualResult = viewModel.productInputModel.value?.detailInputModel?.status
        assertNotEquals(true, actualResult)

        viewModel.updateProductStatus(false)
        viewModel.productInputModel.getOrAwaitValue()
        actualResult = viewModel.productInputModel.value?.detailInputModel?.status
        assertNotEquals(false, actualResult)
    }

    @Test
    fun `When productInputModel data changed Expect changes to isDataChanged`() {
        viewModel.productInputModel.value = ProductInputModel()
        viewModel.productInputModel.getOrAwaitValue()

        viewModel.setIsDataChanged(true)
        assertTrue(viewModel.getIsDataChanged())

        viewModel.setIsDataChanged(false)
        assertFalse(viewModel.getIsDataChanged())

        viewModel.productInputModel.value = null
        viewModel.productInputModel.getOrAwaitValue()

        viewModel.setIsDataChanged(true)
        assertFalse(viewModel.getIsDataChanged())
    }

    @Test
    fun  `When validate shop location should be true`() = runBlocking {

        onGetShopInfoLocation_thenReturn()

        viewModel.validateShopLocation(121313)

        viewModel.locationValidation.getOrAwaitValue()
        verifyValidateShopLocation()
    }

    @Test
    fun `When validate shop location should be false`() = runBlocking {

        onGetShopInfoLocation_thenReturn_false()

        viewModel.validateShopLocation(121313)

        viewModel.locationValidation.getOrAwaitValue()
        verifyValidateShopLocationIsFlase()
    }

    @Test
    fun  `When validate shop location error, should post error to observer`() = runBlocking {
        coEvery { getShopInfoLocationUseCase.executeOnBackground() } throws MessageErrorException("")

        viewModel.validateShopLocation(121313)

        coVerify {
            getShopInfoLocationUseCase.executeOnBackground()
        }

        assert(viewModel.locationValidation.value is Fail)
    }

    @Test
    fun  `When save shop location should be successful`() = runBlocking {
        onSaveShopShipmentLocation_thenReturn()

        viewModel.saveShippingLocation(mutableMapOf())

        viewModel.saveShopShipmentLocationResponse.getOrAwaitValue()
        verifyGetShopInfoLocation()
    }

    @Test
    fun  `When save shop location error, should post error to observer`() = runBlocking {
        coEvery { saveShopShipmentLocationUseCase.executeOnBackground() } throws MessageErrorException("")

        viewModel.saveShippingLocation(mutableMapOf())

        coVerify {
            saveShopShipmentLocationUseCase.executeOnBackground()
        }

        assert(viewModel.saveShopShipmentLocationResponse.value is Fail)
    }

    @Test
    fun  `When validate product name and product name unchanged Expect return success result`() = runBlocking {
        val productName = "testing"

        viewModel.setProductId("123")
        viewModel.isEditing.getOrAwaitValue()
        viewModel.productInputModel.value = ProductInputModel(
                detailInputModel = DetailInputModel(currentProductName = productName)
        )

        viewModel.validateProductNameInput("not same")
        val failedResult = viewModel.validationResult.getOrAwaitValue()
        assertEquals(ValidationResultModel.Result.VALIDATION_ERROR, failedResult.result)

        viewModel.validateProductNameInput(productName)
        val successResult = viewModel.validationResult.getOrAwaitValue()
        assertEquals(ValidationResultModel.Result.VALIDATION_SUCCESS, successResult.result)
    }

    @Test
    fun  `When validate product name is success Expect get the result`() = runBlocking {
        viewModel.resetValidateResult()

        val response = ValidateProductNameResponse().apply {
            productValidateV3.isSuccess = true
            productValidateV3.data.validationResults = listOf()
        }

        onValidateProductName_thenReturn(response)
        viewModel.validateProductNameInput("testing")

        coVerify { validateProductNameUseCase.executeOnBackground() }

        viewModel.validationResult.getOrAwaitValue()

        val validationResult = response.productValidateV3.data.validationResults.joinToString("\n")
        assertEquals(viewModel.validationResult.value?.result, ValidationResultModel.Result.VALIDATION_SUCCESS)
        assertEquals(viewModel.validationResult.value?.exception?.message, validationResult)

        viewModel.resetValidateResult()
        viewModel.validationResult.getOrAwaitValue()

        assertEquals(viewModel.validationResult.value?.result, ValidationResultModel.Result.UNVALIDATED)
    }

    @Test
    fun  `When validate product name if product name equals to current product name Expect get the result`() = runBlocking {
        viewModel.resetValidateResult()

        val response = ValidateProductNameResponse().apply {
            productValidateV3.isSuccess = true
            productValidateV3.data.validationResults = listOf()
        }

        viewModel.productInputModel.value = ProductInputModel(
                detailInputModel = DetailInputModel(currentProductName = "testing")
        )

        onValidateProductName_thenReturn(response)
        viewModel.validateProductNameInput("testing")

        assertEquals(viewModel.validationResult.value?.result, ValidationResultModel.Result.VALIDATION_SUCCESS)

        viewModel.validateProductNameInput("another test")
        viewModel.validationResult.getOrAwaitValue()

        val validationResult = response.productValidateV3.data.validationResults.joinToString("\n")
        assertEquals(viewModel.validationResult.value?.result, ValidationResultModel.Result.VALIDATION_SUCCESS)
        assertEquals(viewModel.validationResult.value?.exception?.message, validationResult)

        viewModel.resetValidateResult()
        viewModel.validationResult.getOrAwaitValue()

        assertEquals(viewModel.validationResult.value?.result, ValidationResultModel.Result.UNVALIDATED)
    }

    @Test
    fun `When is shop owner should not get admin permission and role is eligible`() {
        onGetIsShopOwner_thenReturn(true)

        viewModel.setProductId("")

        verifyGetAdminProductPermissionNotCalled(getAccessId())
        verifyGetAdminEditStockPermissionNotCalled()
        verifyGetAdminProductPermissionResult(Success(true))
    }

    @Test
    fun `When is not shop owner but is shop admin should get admin permission`() {
        onGetIsShopOwner_thenReturn(false)
        onGetIsShopAdmin_thenReturn(true)
        onGetShopId_thenReturnDefault()

        viewModel.setProductId("")

        verifyGetAdminProductPermissionCalled(getAccessId())
        verifyGetAdminEditStockPermissionCalled()
    }

    @Test
    fun `When product id is not blank, is not shop owner, but is shop admin, and admin permission is not yet known, should get admin permission`() {
        onGetIsShopOwner_thenReturn(false)
        onGetIsShopAdmin_thenReturn(true)
        onGetShopId_thenReturnDefault()

        viewModel.setProductId("123")

        verifyGetAdminProductPermissionCalled(getAccessId())
        verifyGetAdminEditStockPermissionCalled()
    }

    @Test
    fun `When product id is not blank, is not shop owner, but is shop admin, and admin permission result is already known, should not get admin permission`() = runBlocking {
        val isEligible = true

        onGetIsShopOwner_thenReturn(false)
        onGetIsShopAdmin_thenReturn(true)
        onGetShopId_thenReturnDefault()
        onGetAdminProductPermission_thenReturn(isEligible)
        onGetAdminEditStockPermission_thenReturn(isEligible)

        viewModel.setProductId("123")
        viewModel.setProductId("123")

        verifyGetAdminProductPermissionCalledOnlyOnce(getAccessId())
        verifyGetAdminEditStockPermissionCalledOnlyOnce()
    }

    @Test
    fun `When both get admin permission use case success Expect admin is eligible`() = runBlocking {
        val isEligible = true

        onGetIsShopOwner_thenReturn(false)
        onGetIsShopAdmin_thenReturn(true)
        onGetShopId_thenReturnDefault()
        onGetAdminProductPermission_thenReturn(isEligible)
        onGetAdminEditStockPermission_thenReturn(isEligible)

        viewModel.setProductId("")

        verifyGetAdminProductPermissionCalled(getAccessId())
        verifyGetAdminEditStockPermissionCalled()
        verifyGetAdminProductPermissionResult(Success((isEligible)))
    }

    @Test
    fun `When product permission is eligible but edit stock permission is not eligible, admin is not eligible`() = runBlocking {
        val isProductManageEligible = true
        val isEditStockEligible = false

        onGetIsShopOwner_thenReturn(false)
        onGetIsShopAdmin_thenReturn(true)
        onGetShopId_thenReturnDefault()
        onGetAdminProductPermission_thenReturn(isProductManageEligible)
        onGetAdminEditStockPermission_thenReturn(isEditStockEligible)

        viewModel.setProductId("")

        verifyGetAdminProductPermissionCalled(getAccessId())
        verifyGetAdminEditStockPermissionCalled()
        verifyGetAdminProductPermissionResult(Success(false))
    }

    @Test
    fun `When product permission is not eligible but edit stock permission is eligible, admin is not eligible`() = runBlocking {
        val isProductManageEligible = false
        val isEditStockEligible = true

        onGetIsShopOwner_thenReturn(false)
        onGetIsShopAdmin_thenReturn(true)
        onGetShopId_thenReturnDefault()
        onGetAdminProductPermission_thenReturn(isProductManageEligible)
        onGetAdminEditStockPermission_thenReturn(isEditStockEligible)

        viewModel.setProductId("")

        verifyGetAdminProductPermissionCalled(getAccessId())
        verifyGetAdminEditStockPermissionCalled()
        verifyGetAdminProductPermissionResult(Success(false))
    }

    @Test
    fun `When both permissions are not eligible, admin is not eligible`() = runBlocking {
        val isProductManageEligible = false
        val isEditStockEligible = false

        onGetIsShopOwner_thenReturn(false)
        onGetIsShopAdmin_thenReturn(true)
        onGetShopId_thenReturnDefault()
        onGetAdminProductPermission_thenReturn(isProductManageEligible)
        onGetAdminEditStockPermission_thenReturn(isEditStockEligible)

        viewModel.setProductId("")

        verifyGetAdminProductPermissionCalled(getAccessId())
        verifyGetAdminEditStockPermissionCalled()
        verifyGetAdminProductPermissionResult(Success(false))
    }

    @Test
    fun `When get admin permission use case failed should return error object`() = runBlocking {
        onGetIsShopOwner_thenReturn(false)
        onGetIsShopAdmin_thenReturn(true)
        onGetAdminEditStockPermission_thenReturn(true)
        onGetAdminProductPermission_thenFailed()
        onGetShopId_thenReturnDefault()

        viewModel.setProductId("")

        verifyGetAdminProductPermissionCalled(getAccessId())
        verifyGetAdminProductPermissionFailed()
    }

    @Test
    fun `When get admin edit stock permission use case failed should return error object`() = runBlocking {
        onGetIsShopOwner_thenReturn(false)
        onGetIsShopAdmin_thenReturn(true)
        onGetAdminProductPermission_thenReturn(true)
        onGetAdminEditStockPermission_thenFailed()
        onGetShopId_thenReturnDefault()

        viewModel.setProductId("")

        verifyGetAdminProductPermissionCalled(getAccessId())
        verifyGetAdminProductPermissionFailed()
    }

    @Test
    fun `getAnnotationCategory should return specification data when productId is provided`() = runBlocking {
        val annotationCategoryData = listOf(
                AnnotationCategoryData(
                        variant = "Merek",
                        data = listOf(
                                Values("1", "Indomie", true, ""),
                                Values("1", "Seedap", false, ""))
                ),
                AnnotationCategoryData(
                        variant = "Rasa",
                        data = listOf(
                                Values("1", "Soto", false, ""),
                                Values("1", "Bawang", true, ""))
                )
        )

        coEvery {
            annotationCategoryUseCase.executeOnBackground()
        } returns AnnotationCategoryResponse(
                DrogonAnnotationCategoryV2(annotationCategoryData)
        )

        viewModel.productInputModel.value = ProductInputModel()
        viewModel.updateSpecificationFromRemote("", "11090")
        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            annotationCategoryUseCase.executeOnBackground()
        }

        val result = viewModel.productInputModel.getOrAwaitValue()
        assertEquals(2, result?.detailInputModel?.specifications?.size)
    }

    @Test
    fun `When getAnnotationCategory is error, should log error to crashlytics`() {
        coEvery { annotationCategoryUseCase.executeOnBackground() } throws MessageErrorException("")

        //Mock FirebaseCrashlytics because .getInstance() method is a static method
        mockkStatic(FirebaseCrashlytics::class)

        every { FirebaseCrashlytics.getInstance().recordException(any()) } returns mockk(relaxed = true)

        viewModel.updateSpecificationFromRemote("", "11090")

        coVerify { annotationCategoryUseCase.executeOnBackground() }

        coVerify { AddEditProductErrorHandler.logExceptionToCrashlytics(any()) }
    }

    @Test
    fun `updateSpecificationByAnnotationCategory should return empty when annotation category is not selected`() = runBlocking {
        val annotationCategoryData = listOf(
                AnnotationCategoryData(
                        variant = "Merek",
                        data = listOf(
                                Values("1", "Indomie", false, ""),
                                Values("1", "Seedap", false, ""))
                )
        )

        viewModel.productInputModel.value = null
        viewModel.updateSpecificationByAnnotationCategory(annotationCategoryData)
        var result = viewModel.productInputModel.getOrAwaitValue()
        assertEquals(null, result?.detailInputModel?.specifications?.size)

        viewModel.productInputModel.value = ProductInputModel()
        viewModel.updateSpecificationByAnnotationCategory(annotationCategoryData)
        result = viewModel.productInputModel.getOrAwaitValue()
        assertEquals(0, result?.detailInputModel?.specifications?.size)
    }

    @Test
    fun `When get product limitation should return success data`() = runBlocking {
        onGetProductLimitation_thenReturn(ProductAddRuleResponse())

        viewModel.getProductLimitation()

        val result = viewModel.productLimitationData.getOrAwaitValue()
        verifyProductLimitationData(result)
    }

    @Test
    fun `When get product limitation error, should post error to observer`() = runBlocking {
        coEvery { productLimitationUseCase.executeOnBackground() } throws MessageErrorException("")

        viewModel.getProductLimitation()

        coVerify { productLimitationUseCase.executeOnBackground() }

        assert(viewModel.productLimitationData.value is Fail)
    }

    @Test
    fun `When get isVariantEmpty value, Should return correct value`() {
        viewModel.productInputModel.value = ProductInputModel()
        viewModel.isVariantEmpty.getOrAwaitValue()

        viewModel.productInputModel.value = ProductInputModel(
            variantInputModel = VariantInputModel(products = listOf(ProductVariantInputModel()))
        )
        viewModel.isVariantEmpty.getOrAwaitValue()
    }

    @Test
    fun `When get priceRangeFormatted value, Should return correct value`() {
        viewModel.productInputModel.value = ProductInputModel(
            variantInputModel = VariantInputModel(products = listOf(ProductVariantInputModel())),
            shipmentInputModel = ShipmentInputModel(isUsingParentWeight = true)
        )
        viewModel.priceRangeFormatted.getOrAwaitValue()

        viewModel.productInputModel.value = ProductInputModel(
            variantInputModel = VariantInputModel(products = listOf(
                ProductVariantInputModel(price = 10000.toBigInteger()),
                ProductVariantInputModel(price = 1000.toBigInteger())
            )),
            shipmentInputModel = ShipmentInputModel(isUsingParentWeight = true)
        )
        viewModel.priceRangeFormatted.getOrAwaitValue()
    }

    @Test
    fun `When get stockFormatted value, Should return correct value`() {
        viewModel.productInputModel.value = ProductInputModel()
        viewModel.stockFormatted.getOrAwaitValue()

        viewModel.productInputModel.value = ProductInputModel(
            variantInputModel = VariantInputModel(products = listOf(ProductVariantInputModel()))
        )
        viewModel.stockFormatted.getOrAwaitValue()
    }

    @Test
    fun `When get mustFillParentWeight value, Should return correct value`() {
        viewModel.productInputModel.value = ProductInputModel(
            variantInputModel = VariantInputModel(products = listOf(ProductVariantInputModel())),
            shipmentInputModel = ShipmentInputModel(isUsingParentWeight = true)
        )
        viewModel.mustFillParentWeight.getOrAwaitValue()

        viewModel.productInputModel.value = ProductInputModel(
            variantInputModel = VariantInputModel(products = listOf(ProductVariantInputModel())),
            shipmentInputModel = ShipmentInputModel(isUsingParentWeight = false)
        )
        viewModel.mustFillParentWeight.getOrAwaitValue()

        viewModel.productInputModel.value = ProductInputModel(
            shipmentInputModel = ShipmentInputModel(isUsingParentWeight = false)
        )
        viewModel.mustFillParentWeight.getOrAwaitValue()
    }

    @Test
    fun  `When validate shop isModerate should be false`() = runBlocking {
        val shopStatus = StatusInfo(
            shopStatus = "1",
            statusTitle= "Open",
            statusMessage = "",
            tickerType ="warning"
        )
        onGetShopStatus_thenReturn(shopStatus)

        viewModel.validateShopIsOnModerated(121313)

        viewModel.isOnModerationMode.getOrAwaitValue()
        verifyValidateShopIsNotModerate()
    }

    @Test
    fun  `When validate shop isModerate should be true`() = runBlocking {
        val shopStatus = StatusInfo(
            shopStatus = "3",
            statusTitle= "Moderate",
            statusMessage = "Your shope is on moderate status",
            tickerType ="warning"
        )
        onGetShopStatus_thenReturn(shopStatus)

        viewModel.validateShopIsOnModerated(121313)

        viewModel.isOnModerationMode.getOrAwaitValue()
        verifyValidateShopIsModerate()
    }

    @Test
    fun  `When validate shop isModerate error, should post error to observer`() = runBlocking {
        coEvery { getStatusShopUseCase.executeOnBackground() } throws MessageErrorException("")

        viewModel.validateShopIsOnModerated(121313)

        coVerify {
            getStatusShopUseCase.executeOnBackground()
        }

        assert(viewModel.isOnModerationMode.value is Fail)
    }

    @Test
    fun `when update image list`(){
        val inputAndTarget = arrayListOf("/adasdsad", "/1234", "20123123")
        val product = inputProductModelDummy()
        viewModel.productInputModel.value = product
        viewModel.saveImageListToDetailInput(inputAndTarget)
        viewModel.productInputModel.getOrAwaitValue()
        val imageListActual = viewModel.productInputModel.getOrAwaitValue()
        assertEquals(inputAndTarget, imageListActual.detailInputModel.imageUrlOrPathList)
    }

    @Test
    fun `when update image list when model is null for the 1st place`(){
        val inputAndTarget = arrayListOf("/adasdsad", "/1234", "20123123")
        viewModel.productInputModel.value = null
        viewModel.saveImageListToDetailInput(inputAndTarget)
        viewModel.productInputModel.getOrAwaitValue()
        val imageListActual = viewModel.productInputModel.getOrAwaitValue()
        assertEquals(null, imageListActual)
    }

    private fun onGetProductLimitation_thenReturn(successResponse: ProductAddRuleResponse) {
        coEvery { productLimitationUseCase.executeOnBackground() } returns successResponse
    }

    private fun onGetProductDraft_thenReturn(draft: ProductDraft) {
        coEvery { getProductDraftUseCase.executeOnBackground() } returns draft
    }

    private fun onSaveProductDraft_thenReturn(draftId: Long) {
        coEvery { saveProductDraftUseCase.executeOnBackground() } returns draftId
    }

    private fun onGetProduct_thenReturn(product: Product) {
        coEvery { getProductUseCase.executeOnBackground() } returns product
    }

    private fun onValidateProductName_thenReturn(response: ValidateProductNameResponse) {
        coEvery { validateProductNameUseCase.executeOnBackground() } returns response
    }

    private fun onGetShopInfoLocation_thenReturn() {
        coEvery { getShopInfoLocationUseCase.executeOnBackground() } returns true
    }

    private fun onGetShopInfoLocation_thenReturn_false() {
        coEvery { getShopInfoLocationUseCase.executeOnBackground() } returns false
    }

    private fun onGetShopStatus_thenReturn(statusInfo: StatusInfo) {
        coEvery { getStatusShopUseCase.executeOnBackground() } returns statusInfo
    }

    private fun onSaveShopShipmentLocation_thenReturn() {
        coEvery { saveShopShipmentLocationUseCase.executeOnBackground() } returns SaveShipmentLocation()
    }

    private fun onGetAdminProductPermission_thenReturn(isEligible: Boolean) {
        coEvery { authorizeAccessUseCase.execute(any()) } returns isEligible
    }

    private fun onGetAdminEditStockPermission_thenReturn(isEligible: Boolean) {
        coEvery { authorizeEditStockUseCase.execute(any()) } returns isEligible
    }

    private fun onGetIsShopOwner_thenReturn(isShowOwner: Boolean) {
        coEvery { userSession.isShopOwner } returns isShowOwner
    }

    private fun onGetIsShopAdmin_thenReturn(isShopAdmin: Boolean) {
        coEvery { userSession.isShopAdmin } returns isShopAdmin
    }

    private fun onGetShopId_thenReturnDefault() {
        coEvery { userSession.shopId } returns defaultShopId
    }

    private fun onGetIsMultiLocationShop_thenReturn(isMultiLocationShop: Boolean) {
        coEvery { userSession.isMultiLocationShop } returns isMultiLocationShop
    }

    private fun onSaveProductDraft_thenFailed() {
        coEvery { saveProductDraftUseCase.executeOnBackground() } throws MessageErrorException("")
    }

    private fun onGetProductDraft_thenFailed() {
        coEvery { getProductDraftUseCase.executeOnBackground() } throws MessageErrorException("")
    }

    private fun onGetProduct_thenFailed() {
        coEvery { getProductUseCase.executeOnBackground() } throws MessageErrorException("")
    }

    private fun onGetAdminProductPermission_thenFailed() {
        coEvery { authorizeAccessUseCase.execute(any()) } throws MessageErrorException("")
    }

    private fun onGetAdminEditStockPermission_thenFailed() {
        coEvery { authorizeEditStockUseCase.execute(any()) } throws MessageErrorException("")
    }

    private fun verifyGetAdminProductPermissionCalled(accessId: Int) {
        coVerify {
            authorizeAccessUseCase.execute(any())
        }
    }

    private fun verifyGetAdminProductPermissionCalledOnlyOnce(accessId: Int) {
        coVerify(exactly = 1) {
            authorizeAccessUseCase.execute(any())
        }
    }

    private fun verifyGetAdminProductPermissionNotCalled(accessId: Int) {
        coVerify(exactly = 0) {
            authorizeAccessUseCase.execute(any())
        }
    }

    private fun verifyGetAdminEditStockPermissionCalled() {
        coVerify {
            authorizeEditStockUseCase.execute(any())
        }
    }

    private fun verifyGetAdminEditStockPermissionCalledOnlyOnce() {
        coVerify(exactly = 1) {
            authorizeEditStockUseCase.execute(any())
        }
    }

    private fun verifyGetAdminEditStockPermissionNotCalled() {
        coVerify(exactly = 0) {
            authorizeEditStockUseCase.execute(any())
        }
    }

    private fun verifyGetProductDraftResult(expectedResult: Success<ProductDraft>) {
        val actualResult = viewModel.getProductDraftResult.value as Success<ProductDraft>
        assertEquals(expectedResult, actualResult)
    }

    private fun verifySaveProductDraftResult(expectedResult: Success<Long>) {
        val actualResult = viewModel.saveProductDraftResultLiveData.value as Success<Long>
        assertEquals(expectedResult, actualResult)
    }

    private fun verifyGetProductResult(expectedResult: Success<Product>) {
        val actualResult = viewModel.getProductResult.value as Success<Product>
        assertEquals(expectedResult, actualResult)

        viewModel.isLoading.getOrAwaitValue()
        assertEquals(false, viewModel.isLoading.value)
    }

    private fun verifyGetAdminProductPermissionResult(expectedResult: Success<Boolean>) {
        val actualResult = viewModel.isProductManageAuthorized.value as Success<Boolean>
        assertEquals(expectedResult, actualResult)
    }

    private fun verifySaveProductDraftFailed() {
        val result = viewModel.saveProductDraftResultLiveData.value
        assertTrue(result is Fail)
    }

    private fun verifyGetProductDraftFailed() {
        val result = viewModel.getProductDraftResult.value
        assertTrue(result is Fail)
    }

    private fun verifyGetProductFailed() {
        val result = viewModel.getProductResult.value
        assertTrue(result is Fail)
    }

    private fun verifyGetShopInfoLocation() {
        assertTrue(viewModel.saveShopShipmentLocationResponse.value == Success(SaveShipmentLocation()))
    }

    private fun verifyValidateShopLocation() {
        assertTrue(viewModel.locationValidation.value == Success(true))
    }

    private fun verifyValidateShopLocationIsFlase() {
        assertTrue(viewModel.locationValidation.value == Success(false))
    }

    private fun verifyGetAdminProductPermissionFailed() {
        val result = viewModel.isProductManageAuthorized.value
        assertTrue(result is Fail)
    }

    private fun verifyProductLimitationData(result: Result<ProductLimitationData>) {
        coVerify {
            productLimitationUseCase.executeOnBackground()
        }
        assert(result is Success)
    }

    private fun getAccessId() =
            when {
                viewModel.isAdding -> AccessId.PRODUCT_ADD
                viewModel.isDuplicate -> AccessId.PRODUCT_DUPLICATE
                viewModel.isEditing.value == true -> AccessId.PRODUCT_EDIT
                else -> AccessId.PRODUCT_ADD
            }

    private fun verifyValidateShopIsModerate() {
        assertTrue(viewModel.isOnModerationMode.value == Success(true))
    }

    private fun verifyValidateShopIsNotModerate() {
        assertTrue(viewModel.isOnModerationMode.value == Success(false))
    }

    private fun inputProductModelDummy() : ProductInputModel{
        var pictureInputModel = PictureInputModel().apply {
            urlOriginal = "www.blank.com/cache/"
            fileName = "apa"
            urlThumbnail = "www.gmail.com"
        }
        var product = ProductInputModel().apply {
            detailInputModel.pictureList = listOf(pictureInputModel, pictureInputModel, pictureInputModel)
            detailInputModel.imageUrlOrPathList = listOf("ada", "apa", "ada")
        }

        return product
    }

}
