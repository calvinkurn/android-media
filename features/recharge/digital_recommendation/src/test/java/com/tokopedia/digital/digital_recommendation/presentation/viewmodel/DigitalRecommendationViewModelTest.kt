package com.tokopedia.digital.digital_recommendation.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.digital.digital_recommendation.domain.DigitalRecommendationUseCase
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationItemUnifyModel
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationModel
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationPage
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationTrackingModel
import com.tokopedia.recharge_component.digital_card.presentation.model.*
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author by furqan on 20/09/2021
 */
@RunWith(JUnit4::class)
class DigitalRecommendationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val digitalRecommendationUseCase: DigitalRecommendationUseCase = mockk()
    private val userSession: UserSessionInterface = mockk()
    private val dispatcher = CoroutineTestDispatchersProvider

    private lateinit var viewmodel: DigitalRecommendationViewModel

    private companion object{
        const val ASSERT_DELTA = 0.0
    }

    private val mockDigitalRecommendationItem = arrayListOf(
        DigitalRecommendationItemUnifyModel(
            unify = DigitalUnifyModel(
                id = "12091092",
                mediaUrl = "dummy media url",
                mediaType = "dummy media url type",
                mediaTitle = "dummy media title",
                iconUrl = "dummy icon url",
                iconBackgroundColor = "dummy background color",
                campaign = DigitalCardCampaignModel(
                    text = "dummy text campaign",
                    textColor = "dummy textColor campaign",
                    backgroundUrl = "dummy backgroundColor campaign"
                ),
                productInfoLeft = DigitalCardInfoModel(
                    text = "dummy text product info left",
                    textColor = "dummy textColor product info left"
                ),
                productInfoRight = DigitalCardInfoModel(
                    text = "dummy text product info right",
                    textColor = "dummy textColor product info right"
                ),
                title = "dummy title",
                rating = DigitalCardRatingModel(
                    ratingType = "dummy rating type",
                    rating = 4.5,
                    review = "dummy review"
                ),
                specialInfo = DigitalCardInfoModel(
                    text = "dummy special info text",
                    textColor = "dummy special info textColor"
                ),
                priceData = DigitalCardPriceModel(
                    price = "Rp 200.000",
                    discountLabelType = Label.HIGHLIGHT_DARK_GREEN,
                    discountLabel = "dummy discountlabel",
                    discountType = "dummy discount type",
                    slashedPrice = "dummy slashedPrice",
                    pricePrefix = "dummy pricePrefix",
                    priceSuffix = "dummy priceSuffix"
                ),
                cashback = "dummy cahsback",
                subtitle = "dummy subtitle",
                soldPercentage = DigitalCardSoldPercentageModel(
                    showPercentage = true,
                    value = 80,
                    label = "dummy label soldPercentage",
                    labelColor = "dummy labelColor"
                ),
                actionButton = DigitalCardActionModel(
                    text = "dummy text action button",
                    applink = "dummy applink",
                    buttonType = "dummy button type"
                )
            ),
            tracking = DigitalRecommendationTrackingModel(
                "dummy typename",
                "dummy business unit",
                "dummy category id",
                "dummy category name",
                "dummy item label",
                "dummy item type",
                "dummy operator id",
                "dummy product id"
            )
        )
    )

    @Before
    fun setUp() {
        viewmodel = DigitalRecommendationViewModel(digitalRecommendationUseCase, userSession, dispatcher)
    }

    @Test
    fun `Failed on fetch digital recommendation data`() {
        // given
        coEvery {
            digitalRecommendationUseCase.execute(any(), any(), any())
        } returns Fail(Throwable("Fetching Failed"))

        // when
        viewmodel.fetchDigitalRecommendation(DigitalRecommendationPage.DIGITAL_GOODS, emptyList(), emptyList())

        // then
        val value = viewmodel.digitalRecommendationItems.value
        assert(value is Fail)
        assertEquals((value as Fail).throwable.message, "Fetching Failed")
    }

    @Test
    fun `Success on fetch digital recommendation data`() {
        // given
        coEvery {
            digitalRecommendationUseCase.execute(any(), any(), any())
        } returns Success(DigitalRecommendationModel(
                userType = "non login",
                title = "",
                items = mockDigitalRecommendationItem
        ))

        // when
        viewmodel.fetchDigitalRecommendation(DigitalRecommendationPage.PHYSICAL_GOODS, emptyList(), emptyList())

        // then
        val value = viewmodel.digitalRecommendationItems.value
        assert(value is Success)
        val successValue = (value as Success).data
        assertEquals(successValue.items.size, 1)
        val dataIndexZero = successValue.items[0]
        val expectedDataIndexZero = mockDigitalRecommendationItem[0]
        with(dataIndexZero.unify){
            assertEquals(expectedDataIndexZero.unify.id, id)
            assertEquals(expectedDataIndexZero.unify.mediaUrl, mediaUrl)
            assertEquals(expectedDataIndexZero.unify.mediaType, mediaType)
            assertEquals(expectedDataIndexZero.unify.mediaTitle, mediaTitle)
            assertEquals(expectedDataIndexZero.unify.iconUrl, iconUrl)
            assertEquals(expectedDataIndexZero.unify.iconBackgroundColor, iconBackgroundColor)
            assertEquals(expectedDataIndexZero.unify.campaign.text, campaign.text)
            assertEquals(expectedDataIndexZero.unify.campaign.textColor, campaign.textColor)
            assertEquals(expectedDataIndexZero.unify.campaign.backgroundUrl, campaign.backgroundUrl)
            assertEquals(expectedDataIndexZero.unify.productInfoLeft.text, productInfoLeft.text)
            assertEquals(expectedDataIndexZero.unify.productInfoLeft.textColor, productInfoLeft.textColor)
            assertEquals(expectedDataIndexZero.unify.productInfoRight.text, productInfoRight.text)
            assertEquals(expectedDataIndexZero.unify.productInfoRight.textColor, productInfoRight.textColor)
            assertEquals(expectedDataIndexZero.unify.title, title)
            assertEquals(expectedDataIndexZero.unify.rating.ratingType, rating.ratingType)
            assertEquals(expectedDataIndexZero.unify.rating.rating, rating.rating, ASSERT_DELTA)
            assertEquals(expectedDataIndexZero.unify.rating.review, rating.review)
            assertEquals(expectedDataIndexZero.unify.specialInfo.textColor, specialInfo.textColor)
            assertEquals(expectedDataIndexZero.unify.specialInfo.text, specialInfo.text)
            assertEquals(expectedDataIndexZero.unify.priceData.price, priceData.price)
            assertEquals(expectedDataIndexZero.unify.priceData.discountLabelType, priceData.discountLabelType)
            assertEquals(expectedDataIndexZero.unify.priceData.discountLabel, priceData.discountLabel)
            assertEquals(expectedDataIndexZero.unify.priceData.discountType, priceData.discountType)
            assertEquals(expectedDataIndexZero.unify.priceData.slashedPrice, priceData.slashedPrice)
            assertEquals(expectedDataIndexZero.unify.priceData.pricePrefix, priceData.pricePrefix)
            assertEquals(expectedDataIndexZero.unify.priceData.priceSuffix, priceData.priceSuffix)
            assertEquals(expectedDataIndexZero.unify.cashback, cashback)
            assertEquals(expectedDataIndexZero.unify.subtitle, subtitle)
            assertEquals(expectedDataIndexZero.unify.soldPercentage.showPercentage, soldPercentage.showPercentage)
            assertEquals(expectedDataIndexZero.unify.soldPercentage.value, soldPercentage.value)
            assertEquals(expectedDataIndexZero.unify.soldPercentage.label, soldPercentage.label)
            assertEquals(expectedDataIndexZero.unify.soldPercentage.labelColor, soldPercentage.labelColor)
            assertEquals(expectedDataIndexZero.unify.actionButton.buttonType, actionButton.buttonType)
            assertEquals(expectedDataIndexZero.unify.actionButton.applink, actionButton.applink)
            assertEquals(expectedDataIndexZero.unify.actionButton.text, actionButton.text)
        }

        with(dataIndexZero.tracking){
            assertEquals(expectedDataIndexZero.tracking.typeName, typeName)
            assertEquals(expectedDataIndexZero.tracking.businessUnit, businessUnit)
            assertEquals(expectedDataIndexZero.tracking.categoryId, categoryId)
            assertEquals(expectedDataIndexZero.tracking.categoryName, categoryName)
            assertEquals(expectedDataIndexZero.tracking.itemLabel, itemLabel)
            assertEquals(expectedDataIndexZero.tracking.itemType, itemType)
            assertEquals(expectedDataIndexZero.tracking.operatorId, operatorId)
            assertEquals(expectedDataIndexZero.tracking.productId, productId)
        }
    }

    @Test
    fun getUserId() {
        // given
        every { userSession.userId } returns "123"

        // when
        val userId = viewmodel.getUserId()

        // then
        assertEquals(userId, "123")
    }

    @Test
    fun getEmptyUserId() {
        // given
        every { userSession.userId } returns ""

        // when
        val userId = viewmodel.getUserId()

        // then
        assertEquals(userId, "0")
    }

}