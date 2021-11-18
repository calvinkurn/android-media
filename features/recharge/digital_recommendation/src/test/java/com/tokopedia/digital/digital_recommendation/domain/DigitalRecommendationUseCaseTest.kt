package com.tokopedia.digital.digital_recommendation.domain

import com.tokopedia.digital.digital_recommendation.data.*
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationPage
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * @author by furqan on 20/09/2021
 */
class DigitalRecommendationUseCaseTest {

    private val multiRequestUseCase: MultiRequestGraphqlUseCase = mockk()
    private val userSession: UserSessionInterface = mockk()

    private lateinit var usecase: DigitalRecommendationUseCase

    @Before
    fun setUp() {
        usecase = DigitalRecommendationUseCase(multiRequestUseCase, userSession)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Failed on fetch digital recommendation data and throw exception`() {
        // given
        coEvery { multiRequestUseCase.setCacheStrategy(any()) } coAnswers {}
        coEvery { multiRequestUseCase.clearRequest() } coAnswers {}
        coEvery { multiRequestUseCase.addRequest(any()) } coAnswers {}
        coEvery {
            multiRequestUseCase.executeOnBackground()
        } coAnswers {
            throw Throwable("Error fetching")
        }
        coEvery { userSession.phoneNumber } returns "080808080808"

        runBlockingTest {
            // when
            val data = usecase.execute(
                    DigitalRecommendationPage.DIGITAL_GOODS,
                    emptyList(),
                    emptyList()
            )

            // then
            assert(data is Fail)
            assertEquals((data as Fail).throwable.message, "Error fetching")

            // when
            val data1 = usecase.execute(
                    DigitalRecommendationPage.PHYSICAL_GOODS,
                    emptyList(),
                    emptyList()
            )

            // then
            assert(data1 is Fail)
            assertEquals((data1 as Fail).throwable.message, "Error fetching")
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Failed on fetch digital recommendation data from response`() {
        // given
        coEvery { multiRequestUseCase.setCacheStrategy(any()) } coAnswers {}
        coEvery { multiRequestUseCase.clearRequest() } coAnswers {}
        coEvery { multiRequestUseCase.addRequest(any()) } coAnswers {}
        coEvery {
            multiRequestUseCase.executeOnBackground()
        } returns GraphqlResponse(
                mapOf(),
                mapOf(
                        DigitalRecommendationResponse::class.java to arrayListOf(GraphqlError().apply {
                            message = "This is Error"
                            extensions = Extensions().also {
                                it.code = 123
                            }
                        })
                ),
                false
        )
        coEvery { userSession.phoneNumber } returns "080808080808"

        runBlockingTest {
            // when
            val data = usecase.execute(
                    DigitalRecommendationPage.DIGITAL_GOODS,
                    emptyList(),
                    emptyList()
            )

            // then
            assert(data is Fail)
            assert((data as Fail).throwable is MessageErrorException)
            assertEquals((data.throwable as MessageErrorException).message, "This is Error")
            assertEquals((data.throwable as MessageErrorException).errorCode, "123")

            // when
            val data1 = usecase.execute(
                    DigitalRecommendationPage.PHYSICAL_GOODS,
                    emptyList(),
                    emptyList()
            )

            // then
            assert(data1 is Fail)
            assert((data1 as Fail).throwable is MessageErrorException)
            assertEquals((data1.throwable as MessageErrorException).message, "This is Error")
            assertEquals((data1.throwable as MessageErrorException).errorCode, "123")
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Success on fetch digital recommendation data`() {
        // given
        coEvery { multiRequestUseCase.setCacheStrategy(any()) } coAnswers {}
        coEvery { multiRequestUseCase.clearRequest() } coAnswers {}
        coEvery { multiRequestUseCase.addRequest(any()) } coAnswers {}
        coEvery {
            multiRequestUseCase.executeOnBackground()
        } returns GraphqlResponse(
                mapOf(
                        DigitalRecommendationResponse::class.java to DigitalRecommendationResponse(
                                personalizedItems = PersonalizedItems(
                                        trackingData = UserTrackingData(
                                                userType = "non login"
                                        ),
                                        appLink = "tokopediatest://dummy-applink",
                                        bannerAppLink = "",
                                        bannerWebLink = "",
                                        recommendationItems = arrayListOf(RecommendationItem(
                                                appLink = "tokopediatest://dummy_product_applink",
                                                backgroundColor = "",
                                                id = "1234567890",
                                                label1 = "Discount 20%",
                                                label1Mode = "",
                                                label2 = "Rp100.000",
                                                label3 = "Rp80.000",
                                                mediaURL = "www.mediaurl.com/gambar.png",
                                                mediaUrlType = "",
                                                subtitle = "Subtitle",
                                                subtitleMode = "",
                                                title = "product title",
                                                trackingData = TrackingData(
                                                        __typename = "",
                                                        businessUnit = "dummy business unit",
                                                        categoryID = "cat id",
                                                        categoryName = "cat name",
                                                        itemLabel = "product",
                                                        itemType = "type",
                                                        operatorID = "123",
                                                        productID = "456"
                                                ),
                                                webLink = ""
                                        ), RecommendationItem(
                                                appLink = "tokopediatest://dummy_product_applink",
                                                backgroundColor = "",
                                                id = "1234567890",
                                                label1 = "",
                                                label1Mode = "",
                                                label2 = "",
                                                label3 = "",
                                                mediaURL = "www.mediaurl.com/gambar.png",
                                                mediaUrlType = "",
                                                subtitle = "Subtitle",
                                                subtitleMode = "",
                                                title = "title",
                                                trackingData = TrackingData(
                                                        __typename = "",
                                                        businessUnit = "dummy business unit",
                                                        categoryID = "cat id",
                                                        categoryName = "category name",
                                                        itemLabel = "category",
                                                        itemType = "type",
                                                        operatorID = "123",
                                                        productID = "456"
                                                ),
                                                webLink = ""
                                        ), RecommendationItem(
                                                appLink = "tokopediatest://dummy_product_applink",
                                                backgroundColor = "",
                                                id = "1234567890",
                                                label1 = "Discount 30%",
                                                label1Mode = "",
                                                label2 = "Rp100.000",
                                                label3 = "Rp70.000",
                                                mediaURL = "www.mediaurl.com/gambar.png",
                                                mediaUrlType = "",
                                                subtitle = "Subtitle",
                                                subtitleMode = "",
                                                title = "product title",
                                                trackingData = TrackingData(
                                                        __typename = "",
                                                        businessUnit = "dummy business unit",
                                                        categoryID = "cat id",
                                                        categoryName = "category name",
                                                        itemLabel = "somelabel",
                                                        itemType = "type",
                                                        operatorID = "123",
                                                        productID = "456"
                                                ),
                                                webLink = ""
                                        ), RecommendationItem(
                                                appLink = "tokopediatest://dummy_product_applink",
                                                backgroundColor = "",
                                                id = "1234567890",
                                                label1 = "",
                                                label1Mode = "",
                                                label2 = "",
                                                label3 = "Rp10.000",
                                                mediaURL = "www.mediaurl.com/gambar.png",
                                                mediaUrlType = "",
                                                subtitle = "Subtitle",
                                                subtitleMode = "",
                                                title = "product title",
                                                trackingData = TrackingData(
                                                        __typename = "",
                                                        businessUnit = "dummy business unit",
                                                        categoryID = "cat id",
                                                        categoryName = "category name",
                                                        itemLabel = "",
                                                        itemType = "type",
                                                        operatorID = "123",
                                                        productID = "456"
                                                ),
                                                webLink = ""
                                        )),
                                        mediaURL = "",
                                        option1 = "",
                                        option2 = "",
                                        option3 = "",
                                        textLink = "",
                                        title = "",
                                        tracking = emptyList(),
                                        webLink = ""
                                )
                        )
                ),
                mapOf(),
                false
        )
        coEvery { userSession.phoneNumber } returns "080808080808"

        runBlockingTest {
            // when
            val data = usecase.execute(
                    DigitalRecommendationPage.DIGITAL_GOODS,
                    emptyList(),
                    emptyList()
            )

            // then
            assert(data is Success)
            assertEquals((data as Success).data.userType, "non login")

            val successData = (data as Success).data.items
            assertEquals(successData.size, 4)

            // first data
            val firstData = successData[0]
            assertEquals(firstData.iconUrl, "www.mediaurl.com/gambar.png")
            assertEquals(firstData.productName, "Subtitle")
            assertEquals(firstData.discountTag, "Discount 20%")
            assertEquals(firstData.beforePrice, "Rp100.000")
            assertEquals(firstData.price, "Rp80.000")
            assertEquals(firstData.applink, "tokopediatest://dummy_product_applink")
            assertEquals(firstData.categoryName, "product title")

            // second data
            val secondData = successData[1]
            assertEquals(secondData.iconUrl, "www.mediaurl.com/gambar.png")
            assertEquals(secondData.productName, "Subtitle")
            assertEquals(secondData.discountTag, "")
            assertEquals(secondData.beforePrice, "")
            assertEquals(secondData.price, "")
            assertEquals(secondData.applink, "tokopediatest://dummy_product_applink")
            assertEquals(secondData.categoryName, "category name")

            // third data
            val thirdData = successData[2]
            assertEquals(thirdData.iconUrl, "www.mediaurl.com/gambar.png")
            assertEquals(thirdData.productName, "Subtitle")
            assertEquals(thirdData.discountTag, "Discount 30%")
            assertEquals(thirdData.beforePrice, "Rp100.000")
            assertEquals(thirdData.price, "Rp70.000")
            assertEquals(thirdData.applink, "tokopediatest://dummy_product_applink")
            assertEquals(thirdData.categoryName, "product title")

            // fourth data
            val fourthData = successData[3]
            assertEquals(fourthData.iconUrl, "www.mediaurl.com/gambar.png")
            assertEquals(fourthData.productName, "Subtitle")
            assertEquals(fourthData.discountTag, "")
            assertEquals(fourthData.beforePrice, "")
            assertEquals(fourthData.price, "Rp10.000")
            assertEquals(fourthData.applink, "tokopediatest://dummy_product_applink")
            assertEquals(fourthData.categoryName, "product title")

            // when
            val data1 = usecase.execute(
                    DigitalRecommendationPage.PHYSICAL_GOODS,
                    emptyList(),
                    emptyList()
            )

            // then
            assert(data1 is Success)

            val successData1 = (data1 as Success).data.items
            assertEquals(successData1.size, 4)

            // first data
            val firstData1 = successData1[0]
            assertEquals(firstData1.iconUrl, "www.mediaurl.com/gambar.png")
            assertEquals(firstData1.productName, "Subtitle")
            assertEquals(firstData1.discountTag, "Discount 20%")
            assertEquals(firstData1.beforePrice, "Rp100.000")
            assertEquals(firstData1.price, "Rp80.000")
            assertEquals(firstData1.applink, "tokopediatest://dummy_product_applink")
            assertEquals(firstData1.categoryName, "product title")

            // second data
            val secondData1 = successData1[1]
            assertEquals(secondData1.iconUrl, "www.mediaurl.com/gambar.png")
            assertEquals(secondData1.productName, "Subtitle")
            assertEquals(secondData1.discountTag, "")
            assertEquals(secondData1.beforePrice, "")
            assertEquals(secondData1.price, "")
            assertEquals(secondData1.applink, "tokopediatest://dummy_product_applink")
            assertEquals(secondData1.categoryName, "category name")

            // third data
            val thirdData1 = successData1[2]
            assertEquals(thirdData1.iconUrl, "www.mediaurl.com/gambar.png")
            assertEquals(thirdData1.productName, "Subtitle")
            assertEquals(thirdData1.discountTag, "Discount 30%")
            assertEquals(thirdData1.beforePrice, "Rp100.000")
            assertEquals(thirdData1.price, "Rp70.000")
            assertEquals(thirdData1.applink, "tokopediatest://dummy_product_applink")
            assertEquals(thirdData1.categoryName, "product title")

            // fourth data
            val fourthData1 = successData1[3]
            assertEquals(fourthData1.iconUrl, "www.mediaurl.com/gambar.png")
            assertEquals(fourthData1.productName, "Subtitle")
            assertEquals(fourthData1.discountTag, "")
            assertEquals(fourthData1.beforePrice, "")
            assertEquals(fourthData1.price, "Rp10.000")
            assertEquals(fourthData1.applink, "tokopediatest://dummy_product_applink")
            assertEquals(fourthData1.categoryName, "product title")
        }
    }
}