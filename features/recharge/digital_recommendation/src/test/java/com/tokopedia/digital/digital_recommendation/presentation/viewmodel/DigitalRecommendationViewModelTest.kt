package com.tokopedia.digital.digital_recommendation.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.digital.digital_recommendation.domain.DigitalRecommendationUseCase
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationModel
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationTrackingModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
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
    private val dispatcher = CoroutineTestDispatchersProvider

    private lateinit var viewmodel: DigitalRecommendationViewModel

    @Before
    fun setUp() {
        viewmodel = DigitalRecommendationViewModel(digitalRecommendationUseCase, dispatcher)
    }

    @Test
    fun `Failed on fetch digital recommendation data`() {
        // given
        coEvery {
            digitalRecommendationUseCase.execute()
        } returns Fail(Throwable("Fetching Failed"))

        // when
        viewmodel.fetchDigitalRecommendation()

        // then
        val value = viewmodel.digitalRecommendationItems.value
        assert(value is Fail)
        assertEquals((value as Fail).throwable.message, "Fetching Failed")
    }

    @Test
    fun `Success on fetch digital recommendation data`() {
        // given
        coEvery {
            digitalRecommendationUseCase.execute()
        } returns Success(arrayListOf(
                DigitalRecommendationModel(
                        "someurl.com",
                        "dummy category",
                        "dummy product",
                        "dummy client number",
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
                        )
                )
        ))

        // when
        viewmodel.fetchDigitalRecommendation()

        // then
        val value = viewmodel.digitalRecommendationItems.value
        assert(value is Success)
        val successValue = value as Success
        assertEquals(successValue.data.size, 1)
        val dataIndexZero = successValue.data[0]
        assertEquals(dataIndexZero.iconUrl, "someurl.com")
        assertEquals(dataIndexZero.categoryName, "dummy category")
        assertEquals(dataIndexZero.productName, "dummy product")
        assertEquals(dataIndexZero.clientNumber, "dummy client number")
        assertEquals(dataIndexZero.applink, "tokopedia-test://dummy-applink")
        assertEquals(dataIndexZero.tracking.typeName, "dummy typename")
        assertEquals(dataIndexZero.tracking.businessUnit, "dummy business unit")
        assertEquals(dataIndexZero.tracking.categoryId, "dummy category id")
        assertEquals(dataIndexZero.tracking.categoryName, "dummy category name")
        assertEquals(dataIndexZero.tracking.itemLabel, "dummy item label")
        assertEquals(dataIndexZero.tracking.itemType, "dummy item type")
        assertEquals(dataIndexZero.tracking.operatorId, "dummy operator id")
        assertEquals(dataIndexZero.tracking.productId, "dummy product id")
    }

}