package com.tokopedia.digital.digital_recommendation.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.digital.digital_recommendation.domain.DigitalRecommendationUseCase
import com.tokopedia.digital.digital_recommendation.presentation.model.*
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
                items = arrayListOf(
                        DigitalRecommendationItemModel(
                                "someurl.com",
                                "dummy category",
                                "dummy product",
                                "tokopedia-test://dummy-applink",
                                DigitalRecommendationTrackingModel(
                                        "dummy typename",
                                        "dummy business unit",
                                        "dummy category id",
                                        "dummy category name",
                                        "dummy item label",
                                        "dummy item type",
                                        "dummy operator id",
                                        "dummy product id"
                                ),
                                DigitalRecommendationType.CATEGORY,
                                "Rp10.000",
                                "Rp100.000",
                                "90%"
                        )
                )
        ))

        // when
        viewmodel.fetchDigitalRecommendation(DigitalRecommendationPage.PHYSICAL_GOODS, emptyList(), emptyList())

        // then
        val value = viewmodel.digitalRecommendationItems.value
        assert(value is Success)
        val successValue = (value as Success).data
        assertEquals(successValue.items.size, 1)
        val dataIndexZero = successValue.items[0]
        assertEquals(dataIndexZero.iconUrl, "someurl.com")
        assertEquals(dataIndexZero.categoryName, "dummy category")
        assertEquals(dataIndexZero.productName, "dummy product")
        assertEquals(dataIndexZero.applink, "tokopedia-test://dummy-applink")
        assertEquals(dataIndexZero.price, "Rp10.000")
        assertEquals(dataIndexZero.beforePrice, "Rp100.000")
        assertEquals(dataIndexZero.discountTag, "90%")
        assertEquals(dataIndexZero.tracking.typeName, "dummy typename")
        assertEquals(dataIndexZero.tracking.businessUnit, "dummy business unit")
        assertEquals(dataIndexZero.tracking.categoryId, "dummy category id")
        assertEquals(dataIndexZero.tracking.categoryName, "dummy category name")
        assertEquals(dataIndexZero.tracking.itemLabel, "dummy item label")
        assertEquals(dataIndexZero.tracking.itemType, "dummy item type")
        assertEquals(dataIndexZero.tracking.operatorId, "dummy operator id")
        assertEquals(dataIndexZero.tracking.productId, "dummy product id")
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