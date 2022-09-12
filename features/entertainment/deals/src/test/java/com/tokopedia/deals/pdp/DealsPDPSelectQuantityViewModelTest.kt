package com.tokopedia.deals.pdp

import com.tokopedia.deals.pdp.data.DealsVerifyResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class DealsPDPSelectQuantityViewModelTest: DealsPDPSelectQuantityViewModelTestFixture() {

    @Test
    fun `when getting verify data should run and give the success result`() {
        onGetVerify_thenReturn(createVerify())
        val expectedResponse = createVerify()
        var actualResponse: Result<DealsVerifyResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowVerify.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setVerifyRequest(createPDPData().eventProductDetail.productDetailData)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting verify data should run and give the error result`() {
        onGetVerify_thenReturn(Throwable(errorMessageGeneral))

        var actualResponse: Result<DealsVerifyResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowVerify.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setVerifyRequest(createPDPData().eventProductDetail.productDetailData)
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals((actualResponse as Fail).throwable.message, errorMessageGeneral)
    }

    @Test
    fun `when getting currentQuantity return quantity `() {
        val currentQuantity = 5

        viewModel.currentQuantity = currentQuantity

        Assert.assertEquals(currentQuantity, viewModel.currentQuantity)
    }

    @Test
    fun `when map new verify to old veriy return old verify`() {
        val newVerify = createVerify()
        val oldVerify = createOldVerify()

        val actualOldVerify = viewModel.mapperOldVerify(newVerify.eventVerify)

        Assert.assertEquals(oldVerify.eventVerify, actualOldVerify)
    }

    @Test
    fun `when map new pdp to old veriy return old pdp`() {
        val newPDP = createPDPNew()
        val oldPDP = createPDPOld()

        val actualOldPDP = viewModel.mapOldProductDetailData(newPDP.eventProductDetail.productDetailData)

        Assert.assertEquals(oldPDP.id, actualOldPDP.id)
        Assert.assertEquals(oldPDP.brandId, actualOldPDP.brandId)
        Assert.assertEquals(oldPDP.categoryId, actualOldPDP.categoryId)
        Assert.assertEquals(oldPDP.providerId, actualOldPDP.providerId)
        Assert.assertEquals(oldPDP.providerProductId, actualOldPDP.providerProductId)
        Assert.assertEquals(oldPDP.providerProductName, actualOldPDP.providerProductName)
        Assert.assertEquals(oldPDP.displayName, actualOldPDP.displayName)
        Assert.assertEquals(oldPDP.url, actualOldPDP.url)
        Assert.assertEquals(oldPDP.imageWeb, actualOldPDP.imageWeb)
        Assert.assertEquals(oldPDP.thumbnailWeb, actualOldPDP.thumbnailWeb)
        Assert.assertEquals(oldPDP.longRichDesc, actualOldPDP.longRichDesc)
        Assert.assertEquals(oldPDP.mrp, actualOldPDP.mrp)
        Assert.assertEquals(oldPDP.salesPrice, actualOldPDP.salesPrice)
        Assert.assertEquals(oldPDP.quantity, actualOldPDP.quantity)
        Assert.assertEquals(oldPDP.soldQuantity, actualOldPDP.soldQuantity)
        Assert.assertEquals(oldPDP.sellRate, actualOldPDP.sellRate)
        Assert.assertEquals(oldPDP.thumbsUp, actualOldPDP.thumbsUp)
        Assert.assertEquals(oldPDP.thumbsDown, actualOldPDP.thumbsDown)
        Assert.assertEquals(oldPDP.status, actualOldPDP.status)
        Assert.assertEquals(oldPDP.minStartDate, actualOldPDP.minStartDate)
        Assert.assertEquals(oldPDP.maxEndDate, actualOldPDP.maxEndDate)
        Assert.assertEquals(oldPDP.saleStartDate, actualOldPDP.saleStartDate)
        Assert.assertEquals(oldPDP.saleEndDate, actualOldPDP.saleEndDate)
        Assert.assertEquals(oldPDP.createdAt, actualOldPDP.createdAt)
        Assert.assertEquals(oldPDP.updatedAt, actualOldPDP.updatedAt)
        Assert.assertEquals(oldPDP.minQty, actualOldPDP.minQty)
        Assert.assertEquals(oldPDP.maxQty, actualOldPDP.maxQty)
        Assert.assertEquals(oldPDP.minStartTime, actualOldPDP.minStartTime)
        Assert.assertEquals(oldPDP.maxEndTime, actualOldPDP.maxEndTime)
        Assert.assertEquals(oldPDP.saleStartTime, actualOldPDP.saleStartTime)
        Assert.assertEquals(oldPDP.saleEndTime, actualOldPDP.saleEndTime)
        Assert.assertEquals(oldPDP.dateRange, actualOldPDP.dateRange)
        Assert.assertEquals(oldPDP.cityName, actualOldPDP.cityName)
        Assert.assertEquals(oldPDP.rating, actualOldPDP.rating)
        Assert.assertEquals(oldPDP.likes, actualOldPDP.likes)
        Assert.assertEquals(oldPDP.savingPercentage, actualOldPDP.savingPercentage)
        Assert.assertEquals(oldPDP.recommendationUrl, actualOldPDP.recommendationUrl)
        Assert.assertEquals(oldPDP.tnc, actualOldPDP.tnc)
        Assert.assertEquals(oldPDP.seoUrl, actualOldPDP.seoUrl)
        Assert.assertEquals(oldPDP.isLiked, actualOldPDP.isLiked)
        Assert.assertEquals(oldPDP.webUrl, actualOldPDP.webUrl)
        Assert.assertEquals(oldPDP.appUrl, actualOldPDP.appUrl)
        Assert.assertEquals(oldPDP.customText1, actualOldPDP.customText1)
        Assert.assertEquals(oldPDP.checkoutBusinessType, actualOldPDP.checkoutBusinessType)
        Assert.assertEquals(oldPDP.checkoutDataType, actualOldPDP.checkoutDataType)
        Assert.assertEquals(oldPDP.outlets.first().id, actualOldPDP.outlets.first().id)
        Assert.assertEquals(oldPDP.outlets.first().productId, actualOldPDP.outlets.first().productId)
        Assert.assertEquals(oldPDP.outlets.first().locationId, actualOldPDP.outlets.first().locationId)
        Assert.assertEquals(oldPDP.outlets.first().name, actualOldPDP.outlets.first().name)
        Assert.assertEquals(oldPDP.outlets.first().searchName, actualOldPDP.outlets.first().searchName)
        Assert.assertEquals(oldPDP.outlets.first().metaTitle, actualOldPDP.outlets.first().metaTitle)
        Assert.assertEquals(oldPDP.outlets.first().metaDescription, actualOldPDP.outlets.first().metaDescription)
        Assert.assertEquals(oldPDP.outlets.first().district, actualOldPDP.outlets.first().district)
        Assert.assertEquals(oldPDP.outlets.first().gmapAddress, actualOldPDP.outlets.first().gmapAddress)
        Assert.assertEquals(oldPDP.outlets.first().neighbourhood, actualOldPDP.outlets.first().neighbourhood)
        Assert.assertEquals(oldPDP.outlets.first().coordinates, actualOldPDP.outlets.first().coordinates)
        Assert.assertEquals(oldPDP.outlets.first().state, actualOldPDP.outlets.first().state)
        Assert.assertEquals(oldPDP.outlets.first().country, actualOldPDP.outlets.first().country)
        Assert.assertEquals(oldPDP.outlets.first().isSearchable, actualOldPDP.outlets.first().isSearchable)
        Assert.assertEquals(oldPDP.outlets.first().locationStatus, actualOldPDP.outlets.first().locationStatus)
        Assert.assertEquals(oldPDP.mediaUrl.first().id, actualOldPDP.mediaUrl.first().id)
        Assert.assertEquals(oldPDP.mediaUrl.first().productId, actualOldPDP.mediaUrl.first().productId)
        Assert.assertEquals(oldPDP.mediaUrl.first().title, actualOldPDP.mediaUrl.first().title)
        Assert.assertEquals(oldPDP.mediaUrl.first().isThumbnail, actualOldPDP.mediaUrl.first().isThumbnail)
        Assert.assertEquals(oldPDP.mediaUrl.first().type, actualOldPDP.mediaUrl.first().type)
        Assert.assertEquals(oldPDP.mediaUrl.first().description, actualOldPDP.mediaUrl.first().description)
        Assert.assertEquals(oldPDP.mediaUrl.first().url, actualOldPDP.mediaUrl.first().url)
        Assert.assertEquals(oldPDP.mediaUrl.first().client, actualOldPDP.mediaUrl.first().client)
        Assert.assertEquals(oldPDP.mediaUrl.first().status, actualOldPDP.mediaUrl.first().status)
        Assert.assertEquals(oldPDP.brand.title, actualOldPDP.brand.title)
        Assert.assertEquals(oldPDP.brand.seoUrl, actualOldPDP.brand.seoUrl)
        Assert.assertEquals(oldPDP.brand.featuredImage, actualOldPDP.brand.featuredImage)
        Assert.assertEquals(oldPDP.brand.cityName, actualOldPDP.brand.cityName)
        Assert.assertEquals(oldPDP.catalog.digitalCategoryId, actualOldPDP.catalog.digitalCategoryId)
        Assert.assertEquals(oldPDP.catalog.digitalProductId, actualOldPDP.catalog.digitalProductId)
        Assert.assertEquals(oldPDP.catalog.digitalProductCode, actualOldPDP.catalog.digitalProductCode)
    }
}