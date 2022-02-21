package com.tokopedia.digital.digital_recommendation.domain

import com.tokopedia.digital.digital_recommendation.data.*
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationPage
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalUnifyConst
import com.tokopedia.unifycomponents.Label
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

    private companion object{
        const val DUMMY_BG = "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2021/7/29/148831929/148831929_109ef2c3-c536-406a-80ba-9a86ef03477a.jpg"
        const val ASSERT_DELTA = 0.0
    }

    private val mockResponse = DigitalRecommendationResponse(
        personalizedItems = PersonalizedItems(
            trackingData = UserTrackingData(
                userType = "non login"
            ),
            appLink = "tokopediatest://dummy-applink",
            mediaUrlType = "square",
            bannerAppLink = "",
            bannerWebLink = "",
            recommendationItems = arrayListOf(RecommendationItem(
                appLink = "tokopediatest://dummy_product_applink",
                backgroundColor = "",

                campaignLabelText ="Campaign Label",
                campaignLabelTextColor ="#FFFFFF",
                campaignLabelBackgroundUrl= DUMMY_BG,
                productInfo1 = ProductInfo(
                    text = "20 Sep",
                    color = "#009F92"
                ),
                productInfo2 = ProductInfo(
                    text = "&#8226; Kartu Prakerja",
                    color = "#6D7588"
                ),
                ratingType = "star",
                rating = 4.7,
                review = "125 ulasan",
                soldPercentageValue= 70,
                soldPercentageLabel= "Segera Habis",
                soldPercentageLabelColor="#F94D63",
                showSoldPercentage=true,
                slashedPrice="Rp150.000",
                discount="20%",
                cashback="cashback",
                specialDiscount="",
                price="Rp120.000",
                pricePrefix="Mulai dari",
                priceSuffix="/pc",
                specialInfoText="Special Info",
                specialInfoColor="#0094CF",

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
                campaignLabelText ="Campaign Label",
                campaignLabelTextColor ="#FFFFFF",
                campaignLabelBackgroundUrl= DUMMY_BG,
                productInfo1 = ProductInfo(
                    text = "20 Sep",
                    color = "#009F92"
                ),
                productInfo2 = ProductInfo(
                    text = "&#8226; Kartu Prakerja",
                    color = "#6D7588"
                ),
                ratingType = "star",
                rating = 4.7,
                review = "125 ulasan",
                soldPercentageValue= 70,
                soldPercentageLabel= "Segera Habis",
                soldPercentageLabelColor="#F94D63",
                showSoldPercentage=true,
                slashedPrice="Rp150.000",
                discount="20%",
                cashback="",
                specialDiscount="",
                price="Rp120.000",
                pricePrefix="Mulai dari",
                priceSuffix="/pc",
                specialInfoText="Special Info",
                specialInfoColor="#0094CF",
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
                campaignLabelText ="",
                campaignLabelTextColor ="",
                campaignLabelBackgroundUrl="",
                productInfo1 = ProductInfo(
                    text = "",
                    color = ""
                ),
                productInfo2 = ProductInfo(
                    text = "",
                    color = ""
                ),
                ratingType = "",
                rating = 0.0,
                review = "",
                soldPercentageValue= 0,
                soldPercentageLabel= "",
                soldPercentageLabelColor="",
                showSoldPercentage=false,
                slashedPrice="",
                discount="",
                cashback="",
                specialDiscount="",
                price="",
                pricePrefix="",
                priceSuffix="",
                specialInfoText="",
                specialInfoColor="",
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
                campaignLabelText ="",
                campaignLabelTextColor ="",
                campaignLabelBackgroundUrl="",
                productInfo1 = ProductInfo(
                    text = "",
                    color = ""
                ),
                productInfo2 = ProductInfo(
                    text = "",
                    color = ""
                ),
                ratingType = "",
                rating = 0.0,
                review = "",
                soldPercentageValue= 0,
                soldPercentageLabel= "",
                soldPercentageLabelColor="",
                showSoldPercentage=false,
                slashedPrice="",
                discount="",
                cashback="",
                specialDiscount="",
                price="",
                pricePrefix="",
                priceSuffix="",
                specialInfoText="",
                specialInfoColor="",
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
                mapOf( DigitalRecommendationResponse::class.java to mockResponse ),
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

            val successData = data.data.items
            assertEquals(successData.size, 4)

            // DG data
            val DGActualData = successData[0].unify
            val DGExpectedData = mockResponse.personalizedItems.recommendationItems[0]

            assertEquals(DGExpectedData.appLink, DGActualData.actionButton.applink)
            assertEquals(DGExpectedData.title.uppercase(), DGActualData.title)
            assertEquals(DGExpectedData.mediaURL, DGActualData.mediaUrl)
            assertEquals(mockResponse.personalizedItems.mediaUrlType, DGActualData.mediaType)
            assertEquals(DGExpectedData.iconURL, DGActualData.iconUrl)
            assertEquals(DGExpectedData.backgroundColor, DGActualData.iconBackgroundColor)
            assertEquals(DGExpectedData.campaignLabelText, DGActualData.campaign.text)
            assertEquals(DGExpectedData.campaignLabelTextColor, DGActualData.campaign.textColor)
            assertEquals(DGExpectedData.campaignLabelBackgroundUrl, DGActualData.campaign.backgroundUrl)
            assertEquals(DGExpectedData.productInfo1.text, DGActualData.productInfoLeft.text)
            assertEquals(DGExpectedData.productInfo1.color, DGActualData.productInfoLeft.textColor)
            assertEquals(DGExpectedData.productInfo2.color, DGActualData.productInfoRight.textColor)
            assertEquals(DGExpectedData.productInfo2.text, DGActualData.productInfoRight.text)
            assertEquals(DGExpectedData.title.uppercase(), DGActualData.title)
            assertEquals(DGExpectedData.ratingType, DGActualData.rating.ratingType)
            assertEquals(DGExpectedData.rating, DGActualData.rating.rating, ASSERT_DELTA)
            assertEquals(DGExpectedData.review, DGActualData.rating.review)
            assertEquals(DGExpectedData.specialInfoText, DGActualData.specialInfo.text)
            assertEquals(DGExpectedData.specialInfoColor, DGActualData.specialInfo.textColor)
            assertEquals(DGExpectedData.price, DGActualData.priceData.price)
            assertEquals(DGExpectedData.cashback, DGActualData.priceData.discountLabel)
            assertEquals(DigitalUnifyConst.DISCOUNT_CASHBACK, DGActualData.priceData.discountType)
            assertEquals(Label.HIGHLIGHT_LIGHT_GREEN, DGActualData.priceData.discountLabelType)
            assertEquals(DGExpectedData.slashedPrice, DGActualData.priceData.slashedPrice)
            assertEquals(DGExpectedData.pricePrefix, DGActualData.priceData.pricePrefix)
            assertEquals(DGExpectedData.priceSuffix, DGActualData.priceData.priceSuffix)
            assertEquals(DGExpectedData.cashback, DGActualData.cashback)
            assertEquals(DGExpectedData.subtitle, DGActualData.subtitle)
            assertEquals(DGExpectedData.showSoldPercentage, DGActualData.soldPercentage.showPercentage)
            assertEquals(DGExpectedData.soldPercentageValue, DGActualData.soldPercentage.value)
            assertEquals(DGExpectedData.soldPercentageLabel, DGActualData.soldPercentage.label)
            assertEquals(DGExpectedData.soldPercentageLabelColor, DGActualData.soldPercentage.labelColor)
            assertEquals(DGExpectedData.textLink, DGActualData.actionButton.text)
            assertEquals(DGExpectedData.appLink, DGActualData.actionButton.applink)
            assertEquals("", DGActualData.actionButton.buttonType)

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

            // PG data
            val PGActualData = successData[0].unify
            val PGExpectedData = mockResponse.personalizedItems.recommendationItems[0]

            assertEquals(PGExpectedData.appLink, PGActualData.actionButton.applink)
            assertEquals(PGExpectedData.title.uppercase(), PGActualData.title)
            assertEquals(PGExpectedData.mediaURL, PGActualData.mediaUrl)
            assertEquals(mockResponse.personalizedItems.mediaUrlType, PGActualData.mediaType)
            assertEquals(PGExpectedData.iconURL, PGActualData.iconUrl)
            assertEquals(PGExpectedData.backgroundColor, PGActualData.iconBackgroundColor)
            assertEquals(PGExpectedData.campaignLabelText, PGActualData.campaign.text)
            assertEquals(PGExpectedData.campaignLabelTextColor, PGActualData.campaign.textColor)
            assertEquals(PGExpectedData.campaignLabelBackgroundUrl, PGActualData.campaign.backgroundUrl)
            assertEquals(PGExpectedData.productInfo1.text, PGActualData.productInfoLeft.text)
            assertEquals(PGExpectedData.productInfo1.color, PGActualData.productInfoLeft.textColor)
            assertEquals(PGExpectedData.productInfo2.color, PGActualData.productInfoRight.textColor)
            assertEquals(PGExpectedData.productInfo2.text, PGActualData.productInfoRight.text)
            assertEquals(PGExpectedData.title.uppercase(), PGActualData.title)
            assertEquals(PGExpectedData.ratingType, PGActualData.rating.ratingType)
            assertEquals(PGExpectedData.rating, PGActualData.rating.rating, ASSERT_DELTA)
            assertEquals(PGExpectedData.review, PGActualData.rating.review)
            assertEquals(PGExpectedData.specialInfoText, PGActualData.specialInfo.text)
            assertEquals(PGExpectedData.specialInfoColor, PGActualData.specialInfo.textColor)
            assertEquals(PGExpectedData.price, PGActualData.priceData.price)
            assertEquals(PGExpectedData.cashback, PGActualData.priceData.discountLabel)
            assertEquals(DigitalUnifyConst.DISCOUNT_CASHBACK, PGActualData.priceData.discountType)
            assertEquals(Label.HIGHLIGHT_LIGHT_GREEN, PGActualData.priceData.discountLabelType)
            assertEquals(PGExpectedData.slashedPrice, PGActualData.priceData.slashedPrice)
            assertEquals(PGExpectedData.pricePrefix, PGActualData.priceData.pricePrefix)
            assertEquals(PGExpectedData.priceSuffix, PGActualData.priceData.priceSuffix)
            assertEquals(PGExpectedData.cashback, PGActualData.cashback)
            assertEquals(PGExpectedData.subtitle, PGActualData.subtitle)
            assertEquals(PGExpectedData.showSoldPercentage, PGActualData.soldPercentage.showPercentage)
            assertEquals(PGExpectedData.soldPercentageValue, PGActualData.soldPercentage.value)
            assertEquals(PGExpectedData.soldPercentageLabel, PGActualData.soldPercentage.label)
            assertEquals(PGExpectedData.soldPercentageLabelColor, PGActualData.soldPercentage.labelColor)
            assertEquals(PGExpectedData.textLink, PGActualData.actionButton.text)
            assertEquals(PGExpectedData.appLink, PGActualData.actionButton.applink)
            assertEquals("", PGActualData.actionButton.buttonType)

        }
    }
}