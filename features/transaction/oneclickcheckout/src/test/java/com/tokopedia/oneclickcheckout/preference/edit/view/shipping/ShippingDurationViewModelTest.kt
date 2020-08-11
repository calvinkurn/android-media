package com.tokopedia.oneclickcheckout.preference.edit.view.shipping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorServiceData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceTextData
import com.tokopedia.oneclickcheckout.common.dispatchers.TestDispatchers
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.preference.edit.domain.shipping.GetShippingDurationUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.shipping.mapper.ShippingDurationModelWithPriceMapper
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.model.ServicesItemModel
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.model.ServicesItemModelNoPrice
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.model.ShippingListModel
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.model.TextsModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable

class ShippingDurationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val useCase: GetShippingDurationUseCase = mockk()
    private val useCaseRates: GetRatesUseCase = mockk()
    private val mapperPrice = ShippingDurationModelWithPriceMapper()

    private val shippingParam = ShippingParam().apply {
        token = ""
        ut = ""
        shopId = ""
        categoryIds = ""
        uniqueId = ""
        originDistrictId = ""
        destinationDistrictId = ""
    }

    private lateinit var shippingDurationViewModel: ShippingDurationViewModel

    @Before
    fun setUp() {
        shippingDurationViewModel = ShippingDurationViewModel(useCase, useCaseRates, mapperPrice, TestDispatchers)
    }

    @Test
    fun `Get Shipping Duration Success`() {
        val response = ShippingListModel()
        every { useCase.execute(any(), any()) } answers { (firstArg() as ((ShippingListModel) -> Unit)).invoke(response) }

        shippingDurationViewModel.getShippingDuration()

        assertEquals(OccState.Success(ShippingListModel()), shippingDurationViewModel.shippingDuration.value)
    }

    @Test
    fun `Get Shipping Duration Failed`() {
        val response = Throwable()
        every { useCase.execute(any(), any()) } answers { (secondArg() as ((Throwable) -> Unit)).invoke(response) }

        shippingDurationViewModel.getShippingDuration()

        assertEquals(OccState.Fail(false, response, ""), shippingDurationViewModel.shippingDuration.value)
    }

    @Test
    fun `Consume Shipping Duration Failed`() {
        val response = Throwable()
        every { useCase.execute(any(), any()) } answers { (secondArg() as ((Throwable) -> Unit)).invoke(response) }

        shippingDurationViewModel.getShippingDuration()
        shippingDurationViewModel.consumeGetShippingDurationFail()

        assertEquals(OccState.Fail(true, response, ""), shippingDurationViewModel.shippingDuration.value)
    }

    @Test
    fun `Get Rates Success`() {
        val response = ShippingRecommendationData().apply {
            shippingDurationViewModels = emptyList()
        }
        every { useCaseRates.execute(any()) } returns Observable.just(response)

        shippingDurationViewModel.getRates(ArrayList(), shippingParam)

        assertEquals(OccState.Success(ShippingListModel()), shippingDurationViewModel.shippingDuration.value)
    }

    @Test
    fun `Get Rates Failed`() {
        val response = Throwable()
        every { useCaseRates.execute(any()) } returns Observable.error(response)

        shippingDurationViewModel.getRates(ArrayList(), shippingParam)

        assertEquals(OccState.Fail(false, response, ""), shippingDurationViewModel.shippingDuration.value)
    }

    @Test
    fun `Set Selected Shipping`() {
        val response = ShippingListModel(listOf(ServicesItemModelNoPrice(serviceId = 1), ServicesItemModelNoPrice(serviceId = 2)))
        every { useCase.execute(any(), any()) } answers { (firstArg() as ((ShippingListModel) -> Unit)).invoke(response) }

        shippingDurationViewModel.getShippingDuration()
        shippingDurationViewModel.setSelectedShipping(2)

        val data = ShippingListModel(listOf(ServicesItemModelNoPrice(serviceId = 1), ServicesItemModelNoPrice(serviceId = 2, isSelected = true)))
        assertEquals(OccState.Success(data), shippingDurationViewModel.shippingDuration.value)
    }

    @Test
    fun `Set Selected Shipping With Price`() {
        val response = ShippingRecommendationData().apply {
            shippingDurationViewModels = listOf(
                    ShippingDurationUiModel().apply {
                        serviceData = ServiceData().apply {
                            serviceId = 1
                            error = ErrorServiceData().apply {
                                errorId = ""
                                errorMessage = ""
                            }
                            texts = ServiceTextData().apply {
                                textServiceDesc = ""
                            }
                        }
                    },
                    ShippingDurationUiModel().apply {
                        serviceData = ServiceData().apply {
                            serviceId = 2
                            error = ErrorServiceData().apply {
                                errorId = ""
                                errorMessage = ""
                            }
                            texts = ServiceTextData().apply {
                                textServiceDesc = ""
                            }
                        }
                    }
            )
        }
        every { useCaseRates.execute(any()) } returns Observable.just(response)

        shippingDurationViewModel.getRates(ArrayList(), shippingParam)
        shippingDurationViewModel.setSelectedShipping(2)

        val data = ShippingListModel(listOf(ServicesItemModel(servicesId = 1, texts = TextsModel()), ServicesItemModel(servicesId = 2, isSelected = true, texts = TextsModel())))
        assertEquals(OccState.Success(data), shippingDurationViewModel.shippingDuration.value)
    }

}
